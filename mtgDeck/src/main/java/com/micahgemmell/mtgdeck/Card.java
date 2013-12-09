package com.micahgemmell.mtgdeck;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Card
{
    public String cardtext = "cardtext";
    public ArrayList<String> color;
    public String flavortext = "flavortext";
    public String imageName = "iname";
    public int manacost = 0;
    public String name = "name";
    public String set = "set";
    public String subtype = "stype";
    public String type = "type";
    public String rarity = "rarity";

    public Card() {}

    public Card(String set, String name, String type, String stype, ArrayList<String> color, int paramInt, String cardtext, String flavortext, String imageName, String rarity)
    {
        this.set = set;
        this.name = name;
        this.imageName = imageName;
        this.type = type;
        //this.color = color;
        if (this.color != color)
        {
        this.color.clear();
        for (int i =0; i<color.size(); i++)
            this.color.set(i, color.get(i));
        }
            this.subtype = stype;
        this.manacost = paramInt;
        this.cardtext = cardtext;
        this.flavortext = flavortext;
        this.rarity = rarity;

    }

    public String getCardtext()
    {
        return this.cardtext;
    }

    public ArrayList<String> getColor()
    {
        return this.color;
    }

    public String getFlavortext()
    {
        return this.flavortext;
    }

    public String getImageName()
    {
        return this.imageName;
    }

    public int getManacost()
    {
        return this.manacost;
    }

    public String getName()
    {
        return this.name;
    }

    public String getSet()
    {
        return this.set;
    }

    public String getSubtype()
    {
        return this.subtype;
    }

    public String getType()
    {
        return this.type;
    }

    public void setCardtext(String cardtext)
    {
        this.cardtext = cardtext;
    }

    public void setColor(JSONArray color)
    {
        if(this.color != null)
        this.color.clear();
        this.color = new ArrayList<String>();
        if (!color.isNull(0)){
        for (int i =0; i<color.length(); i++){
            try {
               // System.out.println("THIS DIDN'T WORK");
                this.color.add(color.getString(i));
                //System.out.println("THIS DIDN'T WORK2");
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        }
    }

    public void setFlavortext(String paramString)
    {
        this.flavortext = paramString;
    }

    public void setImageName(String paramString)
    {
        this.imageName = paramString;
    }

    public void setManacost(int paramInt)
    {
        this.manacost = paramInt;
    }

    public void setName(String paramString)
    {
        this.name = paramString;
    }

    public void setSet(String paramString)
    {
        this.set = paramString;
    }

    public void setSubtype(String paramString)
    {
        this.subtype = paramString;
    }

    public void setType(String paramString)
    {
        this.type = paramString;
    }

    public String toString()
    {
        return this.name;// + ", " + this.type + ", " + this.subtype + ", " + this.color + ", " + this.manacost + ", " + this.cardtext + "," + this.flavortext + ", ";
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }


}
