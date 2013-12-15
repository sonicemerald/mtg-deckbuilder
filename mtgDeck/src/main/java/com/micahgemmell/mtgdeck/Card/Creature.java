package com.micahgemmell.mtgdeck.Card;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by MXPS on 12/14/13.
 */
public class Creature extends Card {
        public String power;
        public String toughness;

        public Creature() {}

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getToughness() {
        return toughness;
    }

    public void setToughness(String toughness) {
        this.toughness = toughness;
    }
}