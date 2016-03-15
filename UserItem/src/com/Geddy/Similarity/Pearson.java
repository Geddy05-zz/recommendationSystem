package com.Geddy.Similarity;

import com.Geddy.Models.UserPreference;

import java.util.HashMap;
import java.util.Map;

public class Pearson implements Distance {
    public float calculate(UserPreference targetUser , UserPreference user){
        int count = 0;
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumXPower = 0;
        double sumYPower = 0;

        // Pearson Formule
        for (Map.Entry<Integer, Double> entry : targetUser.getRatings().entrySet()) {
            int key = entry.getKey();
            double value = entry.getValue();
            if (user.getRatings().containsKey(key)) {
                count++;
                double x = (double) value;
                double y = (double) user.getRating(key);
                sumX += x;
                sumY += y;
                sumXPower += Math.pow(x, 2);
                sumYPower += Math.pow(y, 2);
                sumXY += (x * y);
            }
        }

        double sumPartOne = sumXY - ((sumX * sumY)/ count);
        double domination = (Math.sqrt(sumXPower - (Math.pow(sumX,2)/ count))) * (Math.sqrt(sumYPower - (Math.pow(sumY , 2) / count)));

        if (domination == 0){
            return 0;
        }

        return (float) (sumPartOne / domination);
    }
}
