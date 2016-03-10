package com.Geddy.Similarity;

import com.Geddy.Models.UserPreference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Geddy on 18-2-2016.
 */
public class Pearson implements Distance {
    public float calculate(UserPreference targetUser , UserPreference user){
        int count = 0;
        int sumX = 0;
        int sumY = 0;
        int sumXY = 0;
        int sumXPower = 0;
        int sumYPower = 0;

        // Pearson Formule
        for (Map.Entry<Integer, Double> entry : targetUser.getRatings().entrySet()) {
            int key = entry.getKey();
            double value = entry.getValue();
            if (user.getRatings().containsKey(key)) {
                count++;
                int x = (int) value;
                int y = (int) user.getRating(key);
                sumX += x;
                sumY += y;
                sumXPower += Math.pow(x, 2);
                sumYPower += Math.pow(y, 2);
                sumXY = sumXY + (x * y);
            }
        }
        double sumPartOne = (count*sumXY) - (sumX * sumY);
        double sumPartTwo = (Math.sqrt((count*sumXPower)- Math.pow(sumX,2)) * Math.sqrt((count*sumYPower)- (Math.pow(sumY,2))));
        float answer = (float)sumPartOne / (float)sumPartTwo;
        return answer;
    }
}
