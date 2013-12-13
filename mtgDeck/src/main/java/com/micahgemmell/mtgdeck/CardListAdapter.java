package com.micahgemmell.mtgdeck;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CardListAdapter extends ArrayAdapter<Card> {
    public CardListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    private List<Card> cards;

    public CardListAdapter(Context context, int resource, List<Card> cards) {
        super(context, resource, cards);
        this.cards = cards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
       // View view = super.getView(position, convertView, parent);

        if (view == null) {

            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.card_list_row, null);

        }

        Card card = this.getItem(position);


        //Dealing with card color
        TextView text = (TextView) view.findViewById(R.id.cardName_tv);
        String cardColor = new String();
        text.setText(card.getName());
        if (card.getColor().size()>0) {
            cardColor = this.getItem(position).getColor().get(0);
        }
        else {
            cardColor = "Other";
        }

        if (cardColor.equals("Blue")) {
            text.setTextColor(Color.WHITE);
            text.setBackgroundColor(parent.getResources().getColor(R.color.blue));
        } else if (cardColor.equals("Green")){
            text.setTextColor(Color.WHITE);
            text.setBackgroundColor(parent.getResources().getColor(R.color.green));
        } else if (cardColor.equals("White")) {
            text.setBackgroundColor(Color.WHITE);
            text.setTextColor(Color.BLACK);
        } else if (cardColor.equals("Black")) {
            text.setBackgroundColor(Color.BLACK);
            text.setTextColor(Color.WHITE);
        } else if (cardColor.equals("Red")) {
            text.setBackgroundColor(parent.getResources().getColor(R.color.red));
            text.setTextColor(Color.WHITE);
        } else {
            text.setTextColor(Color.WHITE);
            text.setBackgroundColor(Color.GRAY);
        }

        String rarity = card.getRarity();
        TextView textRarity = (TextView) view.findViewById(R.id.rarity_tv);

        switch(rarity.charAt(0)){
            case 'C': //common - black
                textRarity.setText("C");
                textRarity.setTextColor(parent.getResources().getColor(R.color.gold));
                textRarity.setBackgroundColor(parent.getResources().getColor(R.color.black));
                break;
            case 'U': //uncommon - silver
                textRarity.setText("U");
                textRarity.setTextColor(parent.getResources().getColor(R.color.black));
                textRarity.setBackgroundColor(parent.getResources().getColor(R.color.silver));
                break;
            case 'R':
                textRarity.setText("R");
                textRarity.setTextColor(parent.getResources().getColor(R.color.black));
                textRarity.setBackgroundColor(parent.getResources().getColor(R.color.gold));
                break;
            case 'M':
                textRarity.setText("MR");
                textRarity.setTextColor(parent.getResources().getColor(R.color.silver));
                textRarity.setBackgroundColor(parent.getResources().getColor(R.color.orangered));
               break;
        }

        String type = card.getType();


            TextView subtypeText = (TextView) view.findViewById(R.id.subtype_tv);
            subtypeText.setText(type);

        return view;
    }

};
