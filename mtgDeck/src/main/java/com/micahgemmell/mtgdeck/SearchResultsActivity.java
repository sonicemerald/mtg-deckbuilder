package com.micahgemmell.mtgdeck;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import static com.micahgemmell.mtgdeck.MainActivity.*;
import static java.lang.Character.toUpperCase;

public class SearchResultsActivity extends Activity implements ListViewFragment.OnCardView {
    String query;
    String cQuery;
    CardImageFragment cardView_f;
    ListViewFragment listView_f;
    List<Card> Cards;
    List<Card> SearchResults;
    ListView container_listView;
    ArrayAdapter<Card> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
       // setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY).toLowerCase();
            //use the query to search your data somehow
            System.out.println(query);
        }
        Cards= MainActivity.getCards();
        listView_f = new ListViewFragment(Cards);

        if (savedInstanceState == null) {
        getFragmentManager().beginTransaction()
                .add(R.id.finalSearch, listView_f)
                .commit();
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        Cards= MainActivity.getCards();

        SearchResults = new ArrayList<Card>();

        cQuery = new String(query);

        if (cQuery.length()>1)
            cQuery = cQuery.substring(0,1).toUpperCase() + cQuery.substring(1);
        else
            cQuery = cQuery.toUpperCase();

        for (int i = 0; i<Cards.size(); i++)
        {
            if(Cards.get(i).getName().toLowerCase().contains(query) || Cards.get(i).getColor().contains(cQuery))
                SearchResults.add(Cards.get(i));
        }




        container_listView = (ListView) findViewById(R.id.listView);
        adapter = new CardListAdapter(this, R.layout.card_list_row, SearchResults);
        container_listView.setAdapter(adapter);







        }
    @Override
    public void showCardInfo(int position) {
        //cardInforFragment = new CardImageFragment();
/*        getFragmentManager().beginTransaction()
                .detach(cardView_f)
                .attach(cardView_f)
                .commit();
                */
    }
    @Override
    public  void addCardToDeck(int position){
        Card card = SearchResults.get(position);
        MainActivity.deck.add(card);
        Log.d("card", "added ".concat(card.getName().toString()));
        //Toast.makeText(context, "added ".concat(cards.get(position).getName()), Toast.LENGTH_SHORT);
    }


    @Override
    public void onCardImageViewUpdate(int position, String calledBy){
        String image;
        String set;
        String imageURL;

            cardView_f = new CardImageFragment(cards.get(position));
           // image = cards.get(position).getImageName();
         //   set = cards.get(position).getSet();
            image = SearchResults.get(position).getImageName();
            set = SearchResults.get(position).getSet();
            imageURL = "http://mtgimage.com/set/".concat(set).concat("/").concat(image).concat(".jpg");
            Log.d("tag", imageURL);
            getCardImageFrom(imageURL);

        getFragmentManager().beginTransaction()
                .replace(R.id.finalSearch, cardView_f)
                .addToBackStack("CardView Back")
                .commit();
    }

    @Override
    public void spinnerItemSelected(int position, int id) {

         /*MainActivity.cards.removeAll(cards);
         String set = MainActivity.cardSetCode_array[position];
         String URL = jsonmtg.corncat(set).concat(json);
        ParseCardsFrom(URL);*/
    }

    public void getCardImageFrom(String imageURL){
        AsyncHttpClient client = new AsyncHttpClient();
        String[] allowedContentTypes = new String[] { "image/jpeg" };
        client.get(imageURL, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(byte[] fileData) {
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                if (cardView_f != null)
                    cardView_f.setImageView(imageBitmap);
            }
        });
    }


}



