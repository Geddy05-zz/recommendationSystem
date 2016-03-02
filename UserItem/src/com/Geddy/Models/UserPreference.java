package com.Geddy.Models;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by geddy on 9-2-2016.
 */
public class UserPreference {
    HashMap<Integer,Double> ratings = new HashMap<Integer,Double>();
    private int UserId;

    public UserPreference(int userId,int id, double rating){
        this.UserId = userId;
        ratings.put(id,rating);
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getUserId() {
        return UserId;
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
