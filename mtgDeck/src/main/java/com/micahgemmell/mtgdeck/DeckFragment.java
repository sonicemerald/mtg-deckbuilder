package com.micahgemmell.mtgdeck;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.micahgemmell.mtgdeck.Card.Card;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class DeckFragment
        extends Fragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    ArrayAdapter<Card> adapter;
    SwipeDismissAdapter sAdapter;
    Context context;
    List<Card> deck;
    ListView listView;
    ListViewFragment.OnCardView mListener;

    public DeckFragment(List<Card> paramList)
    {
        this.deck = paramList;
    }

    public void onAttach(Activity paramActivity)
    {
        super.onAttach(paramActivity);
        this.context = paramActivity;
        if ((paramActivity instanceof ListViewFragment.OnCardView))
        {
            this.mListener = ((ListViewFragment.OnCardView)paramActivity);
            return;
        }
        throw new ClassCastException(paramActivity.toString() + " is lame!");
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        View localView = paramLayoutInflater.inflate(R.layout.fragment_main, paramViewGroup, false);
        this.listView = ((ListView)localView.findViewById(R.id.listView));
        this.adapter = new CardListAdapter(this.context, R.layout.card_list_row, deck);
       if(deck.isEmpty()){
            Card welcome = new Card();
                    welcome.setName("Longpress a card to add it");
                    welcome.setType("Swipe to delete this.");
                    welcome.setImageName("Null");
            try {
                welcome.setColor(new JSONArray("[Blue]"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            deck.add(welcome);

        }
        //this.adapter = new ArrayAdapter(this.context, android.R.layout.simple_list_item_1, this.deck);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);

        OnDismissCallback oDC = new OnDismissCallback() {
            @Override
            public void onDismiss(AbsListView absListView, int[] ints) {
                try{
                for (int position : ints) {

                        adapter.remove(deck.get(position));
                    Toast.makeText(context, "Deleted ".concat(deck.get(position).getName()), Toast.LENGTH_SHORT).show();
                  }
                } catch (IndexOutOfBoundsException i){

                }

            }
        };
        // Somewhere in your adapter creation code
        sAdapter = new SwipeDismissAdapter(adapter, oDC);
        sAdapter.setAbsListView(listView);
        listView.setAdapter(sAdapter);
        return localView;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        if(!(deck.get(0).getImageName().equals("Null"))){
        String calledBy = "deck";
        this.mListener.onCardImageViewUpdate(paramInt, calledBy);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        try{
        this.adapter.remove(deck.get(position));
        Toast.makeText(this.context, "Deleted ".concat(deck.get(position).getName()), Toast.LENGTH_SHORT).show();
        } catch (IndexOutOfBoundsException i){}
        return true;
    }

    public static abstract interface OnCardView
    {
        public abstract void addCardToDeck(int paramInt, String calledBy);

        public abstract void onCardViewUpdate1(int paramInt);
    }
}
