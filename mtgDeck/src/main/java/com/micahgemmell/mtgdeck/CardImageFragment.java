package com.micahgemmell.mtgdeck;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CardImageFragment
        extends Fragment
{
    private Card cards;
    private Bitmap itemBitmap;
    private ImageView itemImageView;

    public CardImageFragment(Card paramCard)
    {
        this.cards = paramCard;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        View localView = paramLayoutInflater.inflate(R.layout.cardview, paramViewGroup, false);
        this.itemImageView = ((ImageView)localView.findViewById(R.id.imageView));
        if (this.itemBitmap != null) {
            this.itemImageView.setImageBitmap(this.itemBitmap);
        }
        return localView;
    }

    public void setImageView(Bitmap paramBitmap)
    {
        this.itemBitmap = paramBitmap;
        this.itemImageView.setImageBitmap(paramBitmap);
    }
}
