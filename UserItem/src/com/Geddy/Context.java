package com.Geddy;

import com.Geddy.Models.UserPreference;
import com.Geddy.Models.UserPreference;
import com.Geddy.Similarity.Distance;

import java.util.HashMap;

/**
 * Created by lord_ on 16-2-2016.
 */
public class Context {

    private Distance strategy;

    public Context(Distance strategy){
        this.strategy = strategy;
    }

    public float calculateDistance(UserPreference targetUser,UserPreference User){
        return strategy.calculate(targetUser, User);
    }

}
