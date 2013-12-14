package com.micahgemmell.mtgdeck;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;

// The ListViewFragment is a fragment which holds a list. In the application, we used to load the full set of magic cards into it, as well as the deck.
// In this navigationDrawer implementation, we will not be using this listView_Fragment to load the complete list of magic cards.
public class ListViewFragment
        extends Fragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener {
    ArrayAdapter<Card> adapter;
    List<Card> cards;
    Context context;
    ListView listView;
    OnCardView mListener;
    OnCardView sListener;
    private Spinner sortSetSpinner;
    private String[] cardSet_array;
    private Spinner sortRaritySpinner;
    private String[] rarity_array;

    ArrayAdapter<String> adapterforSetArray;
    ArrayAdapter<String> adapterforRarityArray;
   // public int spinnerposition;
    private AdapterView.OnItemSelectedListener listener;

    public ListViewFragment(List<Card> card)
    {
        this.cards = card;
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.context = activity;
        if ((activity instanceof OnCardView))
        {
            this.mListener = ((OnCardView)activity);
            this.sListener = ((OnCardView)activity);
            return;
        }
        throw new ClassCastException(activity.toString() + " is lame!");
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        View localView = paramLayoutInflater.inflate(R.layout.fragment_main, paramViewGroup, false);
        this.sortSetSpinner = (Spinner) localView.findViewById(R.id.filterSetSpinner);
        cardSet_array = getResources().getStringArray(R.array.setNames);
        //cardSetCode_array = getResources().getStringArray(R.array.sets);
        adapterforSetArray = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, cardSet_array);
        this.sortSetSpinner.setAdapter(adapterforSetArray);
        listener = this;
        this.sortSetSpinner.post(new Runnable() {
            public void run() {
                sortSetSpinner.setOnItemSelectedListener(listener);
                int initialposition = (adapterforSetArray.getCount()-1);
                int spinnerposition = mListener.getSpinnerPosition();
                //if(mListener.getSpinnerPosition() = initialposition) { spinnerposition = initialposition; }
                sortSetSpinner.setSelection(spinnerposition);
            }
        });

        //this.sortSetSpinner.setOnItemSelectedListener(this);


        this.sortRaritySpinner = (Spinner) localView.findViewById(R.id.sortRaritySpinner);
        rarity_array = getResources().getStringArray(R.array.rarity);
        //cardSetCode_array = getResources().getStringArray(R.array.sets);
        adapterforRarityArray = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, rarity_array);
        this.sortRaritySpinner.setAdapter(adapterforRarityArray);
        this.sortRaritySpinner.setOnItemSelectedListener(this);

        this.listView = ((ListView)localView.findViewById(R.id.listView));
        this.adapter = new CardListAdapter(this.context, R.layout.card_list_row, cards);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);
        return localView;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong)
    {
        if(!(cards.get(0).getImageName().equals("Null"))){
            String calledBy = "set";
            this.mListener.onCardImageViewUpdate(position, calledBy);
        }
        //this.mListener.showCardInfo(position);
    }

    public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        if(!(cards.get(0).getImageName().equals("Null"))){
            this.mListener.addCardToDeck(paramInt);
        Toast.makeText(this.context, "added ".concat(((Card)this.cards.get(paramInt)).getName()), 0);
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            this.sListener.spinnerItemSelected(position, parent.getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void refresh(){
        this.listView.invalidateViews();
    }

    public static abstract interface OnCardView
    {
        public abstract void addCardToDeck(int position);
        public abstract void onCardImageViewUpdate(int paramInt, String calledBy);
        public abstract void spinnerItemSelected(int position, int id);
        public abstract int getSpinnerPosition();
        public abstract void showCardInfo(int position);
    }
}
