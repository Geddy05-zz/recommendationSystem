package com.Geddy.Models;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by lord_ on 9-2-2016.
 */
public class UserPreference {
    HashMap<Integer,Double> ratings = new HashMap<Integer,Double>();

    public UserPreference(int id, double rating){
        ratings.put(id,rating);
    }

    public double getRating(int id) {
        return ratings.get(id);
    }

    public void setRating(int id, double rating){
        ratings.put(id,rating);
    }

    public HashMap<Integer,Double> getRatings() {
        return ratings;
    }
}
