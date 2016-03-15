package com.Geddy.Similarity;

//import com.Geddy.Models.Articles;

import com.Geddy.Models.UserPreference;

import java.util.HashMap;
import java.util.Map;

public class Euclidean implements Distance {
    public float calculate(UserPreference targetUser ,UserPreference user){

        Double sum = 0.0;
        for (Map.Entry<Integer, Double> entry : targetUser.getRatings().entrySet()) {
            Integer key = entry.getKey();
            Double value = entry.getValue();
            double userRating ;

            // if the user we compare with rate the item calculate the sum to the power of 2.
            if(user.getRatings().containsKey(key)) {
                userRating = user.getRating(key);
                sum += Math.pow((value - userRating), 2);
            }
        }

        // sigmoid method returns a value between 0 and 1;
        return 1/ (1 +((float)Math.sqrt(sum)));
    }
}
