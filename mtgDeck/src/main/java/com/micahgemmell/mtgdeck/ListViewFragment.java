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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

// The ListViewFragment is a fragment which holds a list. In the application, we used to load the full set of magic cards into it, as well as the deck.
// In this navigationDrawer implementation, we will not be using this listView_Fragment to load the complete list of magic cards.
public class ListViewFragment
        extends Fragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    ArrayAdapter<Card> adapter;
    List<Card> cards;
    Context context;
    ListView listView;
    OnCardView mListener;


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
            return;
        }
        throw new ClassCastException(paramActivity.toString() + " is lame!");
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        View localView = paramLayoutInflater.inflate(R.layout.fragment_main, paramViewGroup, false);
        this.listView = ((ListView)localView.findViewById(R.id.listView));
        this.adapter = new ArrayAdapter(this.context, android.R.layout.simple_list_item_1, this.cards);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);
        return localView;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        String calledBy = "set";
        this.mListener.onCardImageViewUpdate(paramInt, calledBy);
    }

    public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        this.mListener.addCardToDeck(paramInt);
        Toast.makeText(this.context, "added ".concat(((Card)this.cards.get(paramInt)).getName()), 0);
        return true;
    }

    public static abstract interface OnCardView
    {
        public abstract void addCardToDeck(int position);

        public abstract void onCardImageViewUpdate(int paramInt, String calledBy);

    }
}
