package com.Geddy.Similarity;

import com.Geddy.Models.UserPreference;

import java.util.HashMap;
import java.util.Map;

public class Cosine implements Distance {
    public float calculate(UserPreference targetUser ,UserPreference user){
        float xDotY = 0;
        float x = 0;
        float y = 0;

        HashMap<Integer,Double> items = new HashMap<Integer,Double>();

        // put all articles in a list
        for (Map.Entry<Integer,Double> entry : targetUser.getRatings().entrySet()) {
            if(items.size() == 0 || !items.containsKey(entry.getKey())) {
                items.put(entry.getKey(),entry.getValue());
            }
        }

        for (Map.Entry<Integer,Double> entry : user.getRatings().entrySet()) {
            if(items.size() == 0 || !items.containsKey(entry.getKey())) {
                items.put(entry.getKey(),entry.getValue());
            }
        }

        // the Cosine formule
        for (Map.Entry<Integer,Double> entry : items.entrySet()) {
            float tempX = 0;
            float tempY = 0;
            if(targetUser.getRatings().containsKey(entry.getKey())){
                tempX = (float) targetUser.getRating(entry.getKey());
            }
            if(user.getRatings().containsKey(entry.getKey())){
                tempY = (float) user.getRating(entry.getKey());
            }
            xDotY += tempX * tempY;
            x += Math.pow(tempX, 2);
            y += Math.pow(tempY, 2);
        }

        float answer = xDotY / (float)(Math.sqrt(x) * Math.sqrt(y));
        return answer;
    }
}
