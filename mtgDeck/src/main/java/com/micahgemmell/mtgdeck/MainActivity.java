package com.micahgemmell.mtgdeck;

import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
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
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.prefs.AbstractPreferences;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.loopj.android.http.BinaryHttpResponseHandler;

public class MainActivity extends Activity implements ListViewFragment.OnCardView, DiceRollerFragment.OnDiceRoll {
    DiceRollerFragment dice;
    public String jsonmtg = "http://mtgjson.com/json/";
    public String json = ".json";
    public String URL = "";

    List<Card> deck;
    List<Card> cards;
    List<Card> Rarity;
    List<Card> SearchResults;

    ArrayAdapter<Card> adapter;
    static JSONArray jArray;

    ListViewFragment listView_f;
    String listview_tag = "listviewFragment";
    ListView NavigationDrawer_listView; // used for the "navigation"
    //ListViewFragment container_listView;
    ListView container_listView;
    DeckFragment deckView_f;
    CardImageFragment cardView_f;


    Spinner addSetSpinner; // dropdown list of magic card sets.
    public int spinnerPosition;
    String[] cardSet_array; // Set Names
    String[] cardSetCode_array; // Set Codes (used in URL)
    ArrayAdapter<String> adapterforStringArray; // currently used for the set list

    //Navigation Drawer
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    DrawerItemClickListener dListener;
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
    private List<Card> AllCards;
    private String query;
    private String cQuery;
    boolean abool;
    private SharedPreferences sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("on", "Create called");

        sharedPrefs = this.getSharedPreferences("com.micahgemmell.mtgDeck", Context.MODE_PRIVATE);
        spinnerPosition = sharedPrefs.getInt("spinnerPos", 0);

        Log.d("spinner", String.valueOf(spinnerPosition));


        // Setup Card Container
        AllCards = new ArrayList<Card>();
        cards = new ArrayList<Card>();
        deck = new ArrayList<Card>(); // a deck of cards
        Rarity = new ArrayList<Card>();
        SearchResults = new ArrayList<Card>();
        Log.d("searchResults Sze", String.valueOf(SearchResults.size()));

        cardSetCode_array = getResources().getStringArray(R.array.sets);

        adapter = new CardListAdapter(this, R.layout.card_list_row, cards);
        Log.d("adapter", String.valueOf(adapter.getCount()));
        listView_f = new ListViewFragment(cards);

        if (savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, listView_f, listview_tag)
                    .commit();
        }

        //also need to spin up a listView for navDrawer List.
       NavigationDrawer_listView = (ListView) findViewById(R.id.left_drawer); //Find where we want to put the list
       navMenuItems = getResources().getStringArray(R.array.nav_drawer_items); // Get the Array of items.
       adapterforStringArray = new ArrayAdapter<String>(this, R.layout.drawer_list_item, navMenuItems); // need to adapt the array of items
       //Now set the adapter.
       NavigationDrawer_listView.setAdapter(adapterforStringArray);
       NavigationDrawer_listView.setOnItemClickListener(dListener = new DrawerItemClickListener());
//   NavigationDrawer_listView.setOnItemLongClickListener(new DrawerItemLongClickListener());

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

        Intent intent = getIntent();
        Log.d("Sre", intent.toString());

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onNewIntent(intent);
                intent = getIntent();
                Log.d("Sre", intent.toString());
                if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                    query = intent.getStringExtra(SearchManager.QUERY).toLowerCase();
                    //performSearch(query);
                    System.out.println(query);
                    Log.d("SEArchResults", String.valueOf(SearchResults.size()));
                }
            }
        };

        IntentFilter filter = new IntentFilter("android.intent.action.SEARCH");

        registerReceiver(receiver, filter);


        SearchView.OnQueryTextListener qistener;




    }


//    protected void onStart(){
//        super.onStart();
//
//        //Search
//        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//
//            query = intent.getStringExtra(SearchManager.QUERY).toLowerCase();
//            sharedPrefs.edit().putString("query", query).commit();
//
//            //use the query to search your data somehow
//
//
//
//        performSearch(query);
//        System.out.println(query);
//        Log.d("SEArchResults", String.valueOf(SearchResults.size()));
//        adapter = new CardListAdapter(this, R.layout.card_list_row, SearchResults);
//        listView_f = new ListViewFragment(SearchResults);
//        getFragmentManager().beginTransaction()
//                .detach(listView_f)
//                .attach(listView_f)
//                .addToBackStack("Search")
//                .commit();
//        }
//    };

    protected void onRestart(){
        super.onRestart();
        Log.d("Restart", "R");
    };

    protected void onResume(){
        super.onResume();
        spinnerPosition = sharedPrefs.getInt("spinnerPos", 0);



        Log.d("resume", "res");
    };

    protected void onPause(){
        super.onPause();
        sharedPrefs.edit().putInt("spinnerPos", spinnerPosition).commit();



        Log.d("onPause", "pased".concat(String.valueOf(spinnerPosition)));
    };

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("onStop", "destry");
    }

    protected void onDestroy(){
        super.onDestroy();
    };



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("saved", "saved");//.concat(String.valueOf(spinnerPosition)));
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        sharedPrefs.edit().putInt("spinnerPos", spinnerPosition).commit();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

      //spinnerPosition = savedInstanceState.getInt("spinnerPos");
        spinnerPosition = sharedPrefs.getInt("spinnerPos", spinnerPosition);
        Log.d("restore", "restored");
    }


    public void performSearch(String query){

        Log.d("enteredSearch", "hi");
        SearchResults.clear();

        cQuery = query;

        if (cQuery.length()>1)
            cQuery = cQuery.substring(0,1).toUpperCase() + cQuery.substring(1);
        else
            cQuery = cQuery.toUpperCase();

        for (int i = 0; i < cards.size(); i++)
        {
            if(cards.get(i).getName().toLowerCase().contains(query) || cards.get(i).getColor().contains(cQuery)){
                SearchResults.add(cards.get(i));
            }
        }
        if(SearchResults.size() == 0){
            Card error404 = new Card();
            error404.setName("Sorry, no cards matched your search.");
            error404.setType("Error 404 - Not Found");
            error404.setImageName("Null");
            try {
                error404.setColor(new JSONArray("[Red]"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SearchResults.add(error404);
        }

        listView_f.adapter.clear();
        listView_f.adapter.addAll(SearchResults);
        listView_f.refresh();
     }

    @Override
    public void spinnerItemSelected(int position, int id) {
        switch(id){
            case R.id.filterSetSpinner: //set
                spinnerPosition = position;
                sharedPrefs.edit().putInt("spinnerPos", spinnerPosition).commit();

                String set = cardSetCode_array[position];
                URL = jsonmtg.concat(set).concat(json);
                //if(spinnerPosition != 999){

                if(set.equals("SET"))
                    break;
                Log.d("SPINNER", "selected".concat(String.valueOf(spinnerPosition)));
                //cards.clear();
                listView_f.adapter.clear();
                adapter.clear();
                AllCards.clear();
                ParseCardsFrom(URL);
                if(query != null){
                performSearch(query); }
                Log.d("allcards", String.valueOf(AllCards.size()));
                break;
            case R.id.sortRaritySpinner: //rarity
               //  listView_R = new ListViewFragment(Rarity);
                String rarity = null;
                switch(position){
                    case 0:
                        List<Card> card = AllCards;
                        listView_f.adapter.clear();
                        listView_f.adapter.addAll(card);
                        listView_f.refresh();
                        break;
                    case 1: // common
                        rarity = "Common";
                        setRarityAdapter(rarity);
                        break;
                    case 2: // uncommon
                        rarity = "Uncommon";
                        setRarityAdapter(rarity);
                        break;
                    case 3: // rare
                        rarity = "Rare";
                        setRarityAdapter(rarity);
                        break;
                    case 4: //mythic rare
                        rarity ="Mythic Rare";
                        setRarityAdapter(rarity);
                        break;
                    default: break;
                  }
                break;
            default: break;
                }

                }
    private void setRarityAdapter(String rarity){

        if(Rarity != null){
            Rarity.clear();
        }
        for (int i = 0, cardsSize = AllCards.size(); i < cardsSize; i++) {
            Card card = AllCards.get(i);

            if (card.getRarity().equals(rarity)){
                    Rarity.add(card);
            }
        }
        listView_f.adapter.clear();
        listView_f.adapter.addAll(Rarity);
        listView_f.refresh();

        Log.d("adapter size", String.valueOf(listView_f.adapter.getCount()));
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


                                        // Do something with the List, such as put it in a Member Object for later

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
                           AllCards.add(card);


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
    private void STARTDealingWithCardImage(){}
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
        // Stub for a later implementation. (Slide down cards.)
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
    private void ENDDealingWithCardImage(){}

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

    private void STARTNavigationDrawer(){}

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
    private void ENDNavigationDrawer(){}

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem mensu = (MenuItem) menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
//        View.OnClickListener istener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (searchView != null) {
//                    searchView.getQuery();
//try{                    Log.d("s", query);
//                } catch (NullPointerException n){}
//                }
//            }
//        };
//
//        searchView.setOnSearchClickListener(istener);
        SearchView.OnQueryTextListener q = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String searchquery = String.valueOf(searchView.getQuery());
                Log.d("Search", searchquery);
                performSearch(searchquery);
                mensu.collapseActionView();
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
        searchView.setOnQueryTextListener(q);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getSpinnerPosition() {
        sharedPrefs = this.getSharedPreferences("com.micahgemmell.mtgDeck", Context.MODE_PRIVATE);
        spinnerPosition = sharedPrefs.getInt("spinnerPos", 0);
        return spinnerPosition; }

}

