package com.micahgemmell.mtgdeck;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.loopj.android.http.BinaryHttpResponseHandler;

public class MainActivity extends Activity implements ListViewFragment.OnCardView, DiceRollerFragment.OnDiceRoll {
    DiceRollerFragment dice;
    public String jsonmtg = "http://mtgjson.com/json/";
    public String json = ".json";
    public String URL = "";

    static List<Card> deck;
    static List<Card> cards;

    ArrayAdapter<Card> adapter;
    JSONArray jArray;

    ListViewFragment listView_f; // not used in navigationDrawer implementation..
    ListView NavigationDrawer_listView; // used for the "navigation"
    //ListViewFragment container_listView;
    ListView container_listView;
    DeckFragment deckView_f;
    CardImageFragment cardView_f;


    Spinner addSetSpinner; // dropdown list of magic card sets.
    String[] cardSet_array; // Set Names
    String[] cardSetCode_array; // Set Codes (used in URL)
    ArrayAdapter<String> adapterforStringArray; // currently used for the set list

    //Navigation Drawer
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    RelativeLayout mDrawerRelative;
    CharSequence mTitle;
    CharSequence mDrawerTitle;
    String[] navMenuItems;

    // Constants for diceroller & lifeviewer
    TextView rollResult;
    TextView livesLeft;
    int lives = 20;

    //Random number generator
    long randomSeed = System.currentTimeMillis();
    Random generator = new Random(randomSeed);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup Card Container
        cards = new ArrayList<Card>();
        deck = new ArrayList<Card>(); // a deck of cards
        cardSetCode_array = getResources().getStringArray(R.array.sets);

        adapter = new CardListAdapter(this, R.layout.card_list_row, cards);
        listView_f = new ListViewFragment(cards);


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, listView_f)
                    .addToBackStack("main")
                    .commit();
        }


        //also need to spin up a listView for navDrawer List.
       NavigationDrawer_listView = (ListView) findViewById(R.id.left_drawer); //Find where we want to put the list
       navMenuItems = getResources().getStringArray(R.array.nav_drawer_items); // Get the Array of items.
       adapterforStringArray = new ArrayAdapter<String>(this, R.layout.drawer_list_item, navMenuItems); // need to adapt the array of items
       //Now set the adapter.
       NavigationDrawer_listView.setAdapter(adapterforStringArray);
       NavigationDrawer_listView.setOnItemClickListener(new DrawerItemClickListener());
//       NavigationDrawer_listView.setOnItemLongClickListener(new DrawerItemLongClickListener());

        //setting up for open close drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRelative = (RelativeLayout) findViewById(R.id.drawer_layout_container);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){

          public void onDrawerClosed(View view) {
               getActionBar().setTitle(R.string.app_name);
               //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
           }
          public void onDrawerOpened(View drawerView) {
               getActionBar().setTitle(mDrawerTitle);
               //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
           }
       };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        //getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

    }

    @Override
    protected void onStart() {
        super.onStart();

     //   container_listView = (ListView) findViewById(R.id.listView);
      // adapter = new CardListAdapter(this, R.layout.card_list_row, cards);
   /*     container_listView.setAdapter(adapter);
        container_listView.setOnItemClickListener(new DrawerItemClickListener());*/

                /* Setting Up the Spinner
        cardSet_array = getResources().getStringArray(R.array.setNames);
        cardSetCode_array = getResources().getStringArray(R.array.sets);
        adapterforStringArray = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cardSet_array);
        addSetSpinner = (Spinner) findViewById(R.id.filterSetSpinner);
        addSetSpinner.setAdapter(adapterforStringArray);
        addSetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cards.removeAll(cards);
                int pos = addSetSpinner.getSelectedItemPosition();
                String set = cardSetCode_array[pos];
                URL = jsonmtg.concat(set).concat(json);

                ParseCardsFrom(URL);

                /*Log.d("d", "onspinnerSelected")

                    getFragmentManager().beginTransaction()
                           .detach(listView_f)
                           .attach(listView_f)
                           .commit();*/
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        /* End Setup for the Spinner */
/*
        adapter = new CardListAdapter(this, R.layout.card_list_row, cards);
        listView_f = new ListViewFragment(cards);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, listView_f)
                .addToBackStack("main")
                .commit();
  */  }

    @Override
    public void spinnerItemSelected(int position) {
        cards.removeAll(cards);
        String set = cardSetCode_array[position];
        URL = jsonmtg.concat(set).concat(json);
        ParseCardsFrom(URL);
        /*
        getFragmentManager().beginTransaction()
                           .detach(listView_f)
                           .attach(listView_f)
                           .commit();*/
    }

    public void ParseCardsFrom(String URL){
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
        client.get(URL, new AsyncHttpResponseHandler() {

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
                                card.setSet(object.getString("code"));

                                JSONObject jObject = jArray.getJSONObject(i); // Fetch the ith JSON Object
                                // from the JSON Array
                                 //card.setName(jObject.getString("Set")); // Parse Name from the JSON
                                // Object, and put into our object
                                try{
                                card.setColor(jObject.getJSONArray("colors"));
                                }
                                catch (JSONException e){
                                    card.setColor(new JSONArray(new String("n")));
                                }
                                    try{
                                card.setSubtype(jObject.getString("subtypes"));
                                    } catch (JSONException e) {
                                        card.setSubtype("null");
                                    }
                                        /*
                                        jObject.getJSONArray("subtypes"));
                                    } catch (JSONException e) {
                                       card.setSubtype(new JSONArray(new String("null")));
                                    }*/
                                card.setName(jObject.getString("name")); // Parse Name from the JSON
                                card.setType(jObject.getString("type")); // Do the same for type
                                card.setRarity(jObject.getString("rarity"));
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
                        //NavigationDrawer_listView.getAdapter();
                           listView_f.adapter.add(card);
                        //listView_f.adapter.add(card);
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
    public  void addCardToDeck(int position){
        Card card = cards.get(position);
        deck.add(card);
        Log.d("card", "added ".concat(card.getName().toString()));
        Toast.makeText(this, "added ".concat(cards.get(position).getName()), Toast.LENGTH_SHORT).show();
    }
    public void startDealingWithCardImage(){}
    @Override
    public void onCardImageViewUpdate(int position, String calledBy){
        String image;
        String set;
        String imageURL;
        if(calledBy == "deck")// == "deck")
        {
            cardView_f = new CardImageFragment(deck.get(position));
            image = deck.get(position).getImageName();
            set = deck.get(position).getSet();
            imageURL = "http://mtgimage.com/set/".concat(set).concat("/").concat(image).concat(".jpg");
            Log.d("tag", imageURL);
            getCardImageFrom(imageURL);
        } else if (calledBy == "set"){
            cardView_f = new CardImageFragment(cards.get(position));
            image = cards.get(position).getImageName();
            set = cards.get(position).getSet();
            imageURL = "http://mtgimage.com/set/".concat(set).concat("/").concat(image).concat(".jpg");
            Log.d("tag", imageURL);
            getCardImageFrom(imageURL);
        }
        getFragmentManager().beginTransaction()
                    .replace(R.id.container, cardView_f)
                    .addToBackStack("CardView Back")
                    .commit();
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
    public void endDealingWithCardImage(){}

    @Override
    public void diceRoller(int button) {
        switch(button)
        {
            case 10:
                int rand = generator.nextInt(9);
                rollResult= (TextView) findViewById(R.id.rollResult);
                rollResult.setText(String.valueOf(rand+1));
                break;
            case 12:
                rand = generator.nextInt(11);
                rollResult= (TextView) findViewById(R.id.rollResult);
                rollResult.setText(String.valueOf(rand+1));
                break;
            case 20:
                rand = generator.nextInt(19);
                rollResult= (TextView) findViewById(R.id.rollResult);
                rollResult.setText(String.valueOf(rand+1));
                break;
            case 50: //Lost Life Button
                livesLeft = (TextView) findViewById(R.id.livesLeft);
                lives--;
                livesLeft.setText("Life Total: " + String.valueOf(lives));
                break;
            case 60: //Add Life Button
                livesLeft = (TextView) findViewById(R.id.livesLeft);
                lives++;
                livesLeft.setText("Life Total: " + String.valueOf(lives));
                break;
        }

    }

    @Override
    public void diceRollerInit() {
        livesLeft = (TextView) findViewById(R.id.livesLeft);
        livesLeft.setText("Life Total: " + String.valueOf(lives));
    }

    public void startNavigationDrawer(){}
    protected class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    private void selectItem(int position){
        // update the main content by replacing fragments
        switch (position){
            case 0: // first item - "search cards"
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, listView_f)
                        .addToBackStack("Search")
                        .commit();
                break;
            case 1: //second item - decks
                deckView_f = new DeckFragment(deck);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, deckView_f)
                        .addToBackStack("Your Deck")
                        .commit();
                break;
            case 2: // third item - life/dice counter
                dice = new DiceRollerFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, dice)
                        .addToBackStack("Dice")
                        .commit();
                break;

        }

        mDrawerLayout.closeDrawer(mDrawerRelative);


        /*/ update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
       */ }
    public void endNavigationDrawer(){}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.myDeck:
                deckView_f = new DeckFragment(deck);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, deckView_f)
                        .addToBackStack("Your Deck")
                        .commit();
                return true;
            case R.id.searchCards:
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, listView_f)
                        .addToBackStack("Search")
                        .commit();
                return true;
            case R.id.diceRoller:
                dice = new DiceRollerFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, dice)
                        .addToBackStack("Dice")
                        .commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static List<Card> getCards()
    {
        return cards;
    }

}

