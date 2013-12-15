package com.micahgemmell.mtgdeck.Card;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Card implements Cloneable
{
    public String cardtext = "cardtext";
    public ArrayList<String> color;
    public String flavortext = "flavortext";
    public String imageName = "iname";
    public int manacost = 0;
    public String name = "name";
    public String set = "set";
    public String subtype = "stype";
   //public ArrayList<String> subtype;
    public String type = "type";
    public ArrayList<String> types;
    public String rarity = "rarity";

    public Card() {}

    public void Clone(Card card) {
        card.set = this.set;
        card.name = this.name;
        card.imageName = this.imageName;
        card.type = this.type;
        card.color = this.color;
        card.cardtext = this.cardtext;
        card.flavortext = this.flavortext;
        card.cardtext = this.cardtext;
        card.rarity = this.rarity;
    }



//    @Override
//    protected Enchantment clone() throws CloneNotSupportedException {
//        return (Enchantment) super.clone();
//        //return (Card)super.clone();
//
//    }

    public Card(String set, String name, String type, String stype,  ArrayList<String> types, ArrayList<String> color, int paramInt, String cardtext, String flavortext, String imageName, String rarity)
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

        if (this.types != color)
        {
            this.types.clear();
            for (int i =0; i< types.size(); i++)
                this.types.set(i, types.get(i));
        }

        this.subtype = stype;
/*        if (this.subtype != stype){
            this.subtype.clear();
            for(int i =0; i < stype.size(); i++)
                this.subtype.set(i, stype.get(i));
        }*/

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
    public ArrayList<String> getTypes () {return this.types; }

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

    public void setSubtype(String stype)
    {
        this.subtype = stype;
        /*
        if(this.subtype != null)
            this.subtype.clear();
        this.subtype = new ArrayList<String>();
        if (!stype.isNull(0)){
            for (int i =0; i<stype.length(); i++){
                try {
                    // System.out.println("THIS DIDN'T WORK");
                    this.subtype.add(stype.getString(i));
                    //System.out.println("THIS DIDN'T WORK2");
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }*/
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



    public void setType(String paramString)
    {
        this.type = paramString;
    }

    public void setTypes(JSONArray types){

        if(this.types != null)
            this.types.clear();
        this.types = new ArrayList<String>();
        if (!types.isNull(0)){
            for (int i =0; i < types.length(); i++){
                try {
                    // System.out.println("THIS DIDN'T WORK");
                    this.types.add(types.getString(i));
                    //System.out.println("THIS DIDN'T WORK2");
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }
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
