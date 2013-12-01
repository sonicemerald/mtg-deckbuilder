package com.micahgemmell.mtgdeck;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.micahgemmell.mtgdeck.Card;

public class MainActivity extends Activity implements listView_F.OnCardView {
    public String set = "THS";
    public String jsonmtg = "http://mtgjson.com/json/".concat(set).concat(".json");

    List<Card> cards;
    ListView listView;
    ArrayAdapter<Card> adapter;
    JSONArray jArray;
    String tempUrl;

    listView_F listView_f;
    cardView_F cardView_f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup card Container
        cards = new ArrayList<Card>();

       listView_f = new listView_F(cards);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, listView_f)
                    .commit();
        }

        // Setup Listview for cards (now done in fragment)
     //   listView = (ListView) findViewById(R.id.listView);

        // NOTE: It is OK to use a SimpleObject, with an android.R.layout.simple_list_item_1 because
        // SimpleObject has a toString method declared, which the adapter calls to generate the
        // ListView text.

     //   adapter = new ArrayAdapter<Card>(this, android.R.layout.simple_list_item_1, cards);
     //   listView.setAdapter(adapter);

        // Now that the adapter is setup, if we need to update the listView we will use adapter.add
        // instead of items.add, the adapter will take care of adding to the host container.
        // Why did we do it this way? Because the HttpClient is Asynchronous, meaning it is run
        // on a separate thread from the UI, so the Activity continues to run at the same time
        // as the HttpClient fetches the Data.

        // Setup the AsyncHttpClient to fetch the JSON String
        AsyncHttpClient client = new AsyncHttpClient();
        // Spin up a new thread, and fetch the JSON
        client.get(jsonmtg, new AsyncHttpResponseHandler() {

            // When the JSON has been fetched, we will process it
            // The JSON is simply a string at this point. Now because the JSON is formated as a
            // JSON Array, we will parse it as an Array, and then loop over each JSON Object
            // Fetch that object, and parse out two values from it, and put them into our
            // Simple Object Class.
            @Override
            public void onSuccess(String response) {
                try {
                    //aArray = new JSONArray(response);// Parse JSON String to JSON Array
                    JSONObject object = new JSONObject(response);
                    jArray = object.getJSONArray("cards");
                         try{
                                for (int i = 0; i < jArray.length(); ++i) { // Loop over Array
                                Card card = new Card(); // Create a new Card

                                JSONObject jObject = jArray.getJSONObject(i); // Fetch the ith JSON Object
                                // from the JSON Array
                                 //card.setName(jObject.getString("Set")); // Parse Name from the JSON
                                // Object, and put into our object
                                card.setColor(jObject.getString("colors"));
                                    try{
                                card.setSubtype(jObject.getString("subtypes"));
                                    } catch (JSONException e) {
                                       card.setSubtype("null");
                                    }
                                card.setName(jObject.getString("name")); // Parse Name from the JSON
                                card.setType(jObject.getString("type")); // Do the same for type
                                    try{
                                card.setManacost(jObject.getInt("cmc"));
                                    } catch (JSONException e) {
                                        card.setManacost(0);
                                    }
                                card.setImageName(jObject.getString("imageName"));
                                //card.setSet(jObject.getString("set"));

                        // Remember there are other items in the JSON Object, and they are of other
                        // Types, so you might want to switch based on type and create an object
                        // such as a Book, Periodical, or Member

                        // For Reference to get the Checked out Array of a Member to a List

                        /*
                        if (card.getType().equals("Member") && jObject.has("Checked_out")) {
                            List<String> checkedOut = new ArrayList<String>();
                            JSONArray checkedOutArray = jObject.getJSONArray("Checked_out");
                            for (int j = 0; j < checkedOutArray.length(); ++j)
                                checkedOut.add(checkedOutArray.getString(i));
                            // Do something with the List, such as put it in a Member Object for later
                        }*/

                        // Add an Item to the Adapter, which will add it to the items List, and
                        // update the List View
                        listView_f.adapter.add(card);
                        //adapter.add(card);
                                }
                             } catch (JSONException e) {
                                        Log.d("JSON Parse", e.toString());
                              };
                    Log.d("List Size", "Size of items: " + Integer.toString(cards.size()));
                    } catch (JSONException e) {
                    Log.d("JSON Parse", e.toString());
                }
            }

        }  );
    }


    @Override
    public void onCardViewUpdate(int position){
    cardView_f = new cardView_F(cards.get(position));
        getFragmentManager().beginTransaction()
                    .replace(R.id.container, cardView_f)
                    .addToBackStack("CardView Back")
                    .commit();

        String image = cards.get(position).getImageName();
        String set = cards.get(position).getSet();
        String url = "http://mtgimage.com/set/".concat(set).concat("/").concat(image).concat(".jpg");
        Log.d("tag", url);

        AsyncHttpClient client = new AsyncHttpClient();
        String[] allowedContentTypes = new String[] { "image/jpeg" };
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(byte[] fileData) {
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                if (cardView_f != null)
                    cardView_f.setImageView(imageBitmap);
            }
        });

    }
}