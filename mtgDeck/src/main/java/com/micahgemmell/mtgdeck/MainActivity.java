package com.micahgemmell.mtgdeck;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
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

public class MainActivity extends Activity {
    public String json = "https://bitbucket.org/sonicemerald/mtgjsonfile/raw/f7b2f206127985b81d4296e4a0cfe4e5c87740b2/AllSets.json";
    public String jsonmtg = "http://mtgjson.com/json/THS.json";

    List<Card> cards;
    ListView listView;
    ArrayAdapter<Card> adapter;
    JSONArray jArray;
    String tempUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup card Container
        cards = new ArrayList<Card>();

        // Setup Listview for cards
        listView = (ListView) findViewById(R.id.listView);
        // NOTE: It is OK to use a SimpleObject, with an android.R.layout.simple_list_item_1 because
        // SimpleObject has a toString method declared, which the adapter calls to generate the
        // ListView text.
        adapter = new ArrayAdapter<Card>(this, android.R.layout.simple_list_item_1, cards);
        listView.setAdapter(adapter);

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
                    jArray = new JSONArray(response); // Parse JSON String to JSON Array
                    for (int i = 0; i < jArray.length(); ++i) { // Loop over Array
                        Card card = new Card(); // Create a new Card

                        JSONObject jObject = jArray.getJSONObject(i); // Fetch the ith JSON Object
                        // from the JSON Array
                        card.setName(jObject.getString("Set")); // Parse Name from the JSON
                        // Object, and put into our object
                        card.setName(jObject.getString("Name")); // Parse Name from the JSON
                        card.setType(jObject.getString("Type")); // Do the same for type

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
                        adapter.add(card);
                    }
                    Log.d("List Size", "Size of items: " + Integer.toString(cards.size()));
                } catch (JSONException e) {
                    Log.d("JSON Parse", e.toString());
                }
            }

        }  );

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * Created by MXPS on 11/29/13.
     */

    public class Card {
        public String set;
        public String name;
        public String imageName;
        public String type;
        public String subtype = "";
        public String color = "";
        public int manacost = 0;
        public String cardtext = "";
        public String flavortext = "";

        public Card() {
        }

        public Card(String set, String name, String type, String subtype, String color, int manacost, String cardtext, String flavortext, String imageName) {
            this.set = set;
            this.name = name;
            this.imageName = imageName;
            this.type = type;
            this.color = color;
            this.subtype = subtype;
            this.manacost = manacost;
            this.cardtext = cardtext;
            this.flavortext = flavortext;
        }

        @Override
        public String toString() {
            // Example of a String Builder like we talked about in class.
            StringBuilder builder = new StringBuilder();
            return builder.append(set).append(".")
                    .append(name).append(", ")
                    .append(type).append(", ")
                    .append(subtype).append(", ")
                    .append(color).append(", ")
                    .append(manacost).append(", ")
                    .append(cardtext).append(",")
                    .append(flavortext).append(", ")
                    .append(imageName).append(",")
                    .toString();
        }

        public String getSet() {
            return set;
        }

        public void setSet(String set) {
            this.set = set;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        public String getSubtype() {
            return subtype;
        }

        public void setSubtype(String subtype) {
            this.subtype = subtype;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getManacost() {
            return manacost;
        }

        public void setManacost(int manacost) {
            this.manacost = manacost;
        }

        public String getCardtext() {
            return cardtext;
        }

        public void setCardtext(String cardtext) {
            this.cardtext = cardtext;
        }

        public String getFlavortext() {
            return flavortext;
        }

        public void setFlavortext(String flavortext) {
            this.flavortext = flavortext;
        }

    }

}
