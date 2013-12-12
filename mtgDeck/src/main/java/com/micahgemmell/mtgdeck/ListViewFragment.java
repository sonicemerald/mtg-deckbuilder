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
    private Spinner addSetSpinner;
    private String[] cardSet_array;
    ArrayAdapter<String> adapterforStringArray;

    public ListViewFragment(List<Card> card)
    {
        this.cards = card;
    }

    public void onAttach(Activity paramActivity)
    {
        super.onAttach(paramActivity);
        this.context = paramActivity;
        if ((paramActivity instanceof OnCardView))
        {
            this.mListener = ((OnCardView)paramActivity);
            this.sListener = ((OnCardView)paramActivity);
            return;
        }
        throw new ClassCastException(paramActivity.toString() + " is lame!");
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        View localView = paramLayoutInflater.inflate(R.layout.fragment_main, paramViewGroup, false);
        this.addSetSpinner = (Spinner) localView.findViewById(R.id.filterSetSpinner);
        cardSet_array = getResources().getStringArray(R.array.setNames);
        //cardSetCode_array = getResources().getStringArray(R.array.sets);
        adapterforStringArray = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, cardSet_array);
        this.addSetSpinner.setAdapter(adapterforStringArray);
        this.addSetSpinner.setOnItemSelectedListener(this);
        this.listView = ((ListView)localView.findViewById(R.id.listView));
        this.adapter = new CardListAdapter(this.context, R.layout.card_list_row, cards);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);
        return localView;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong)
    {
        String calledBy = "set";
        this.mListener.onCardImageViewUpdate(position, calledBy);
        //this.mListener.showCardInfo(position);
    }

    public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        this.mListener.addCardToDeck(paramInt);
        Toast.makeText(this.context, "added ".concat(((Card)this.cards.get(paramInt)).getName()), 0);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.sListener.spinnerItemSelected(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static abstract interface OnCardView
    {
        public abstract void addCardToDeck(int position);
        public abstract void onCardImageViewUpdate(int paramInt, String calledBy);
        public abstract void spinnerItemSelected(int position);
        public abstract void showCardInfo(int position);
    }
}
