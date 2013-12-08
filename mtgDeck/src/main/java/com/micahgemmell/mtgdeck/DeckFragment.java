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
import java.util.List;

public class DeckFragment
        extends Fragment
        implements AdapterView.OnItemClickListener
{
    ArrayAdapter<Card> adapter;
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
        this.adapter = new ArrayAdapter(this.context, android.R.layout.simple_list_item_1, this.deck);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this);
        return localView;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        String calledBy = "deck";
        this.mListener.onCardImageViewUpdate(paramInt, calledBy);
    }

    public static abstract interface OnCardView
    {
        public abstract void addCardToDeck(int paramInt, String calledBy);

        public abstract void onCardViewUpdate1(int paramInt);
    }
}
