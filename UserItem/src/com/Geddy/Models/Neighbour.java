package com.Geddy.Models;

public class Neighbour {

    private UserPreference user;
    private Double distance;

    public Neighbour(UserPreference user, Double distance){
        this.user = user;
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public UserPreference getUser() {
        return user;
    }
}
