package com.Geddy.Models;

import com.Geddy.Similarity.Distance;

/**
 * Created by geddy on 02/03/16.
 */
public class Neighbour {

    private UserPreference user;
    private Double distance;
//    private Double weight;

    public Neighbour(UserPreference user, Double distance){
        this.user = user;
        this.distance = distance;
    }

//    public void setWeight(Double weight) {
//        this.weight = weight;
//    }
//
//    public Double getWeight() {
//        return weight;
//    }

    public Double getDistance() {
        return distance;
    }

    public UserPreference getUser() {
        return user;
    }
}
