package com.micahgemmell.mtgdeck;

public class Card
{
    public String cardtext = "cardtext";
    public String color = "color";
    public String flavortext = "flavortext";
    public String imageName = "iname";
    public int manacost = 0;
    public String name = "name";
    public String set = "THS";
    public String subtype = "stype";
    public String type = "type";

    public Card() {}

    public Card(String set, String name, String type, String stype, String color, int paramInt, String cardtext, String flavortext, String imageName)
    {
        this.set = set;
        this.name = name;
        this.imageName = imageName;
        this.type = type;
        this.color = color;
        this.subtype = stype;
        this.manacost = paramInt;
        this.cardtext = cardtext;
        this.flavortext = flavortext;
    }

    public String getCardtext()
    {
        return this.cardtext;
    }

    public String getColor()
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

    public void setColor(String color)
    {
        this.color = color;
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
        return this.name + ", " + this.type + ", " + this.subtype + ", " + this.color + ", " + this.manacost + ", " + this.cardtext + "," + this.flavortext + ", ";
    }
}
