import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * Created by geddy on 17-3-2016.
 */
public class SlopeOne {

    public HashMap<Integer, HashMap<Integer,Deviations>>  calculate(HashMap<String, UserPreference> userRatings){

        HashMap<Integer, HashMap<Integer,Integer>> freg = new HashMap<>();
        HashMap<Integer, HashMap<Integer,Deviations>> dev = new HashMap<>();

        // loop trough all the users
        for (Map.Entry<String,UserPreference> user : userRatings.entrySet()) {

            // loop trough all ratings of a user
            for(Map.Entry<Integer,Double> targetRating : user.getValue().getRatings().entrySet()){
                // loop trough all items again for compare them
                for(Map.Entry<Integer,Double> compareRating : user.getValue().getRatings().entrySet()){
                    // if rating != targetRating calculate slope one
                    if(targetRating != compareRating){

                        // create the hashmap
                        HashMap<Integer, Integer> temp;
                        HashMap<Integer, Deviations> tempDev;
                        int amount = 1;
                        double rate = 0.0;
                        if (freg.size() == 0 || !freg.containsKey(targetRating.getKey())) {
                            temp = new HashMap<>();
                            tempDev = new HashMap<>();
                        }else{
                            temp = freg.get(targetRating.getKey());
                            if(temp.containsKey(compareRating.getKey())) {
                                amount = temp.get(compareRating.getKey()) + 1;
                            }
                            tempDev = dev.get(targetRating.getKey());
                            if(tempDev.containsKey(compareRating.getKey())) {
                                rate = tempDev.get(compareRating.getKey()).getRating();
                            }
                        }

                        // Calculate the slope one ans update the hashmap
                        rate += targetRating.getValue() - compareRating.getValue();
                        temp.put(compareRating.getKey(),amount);
                        Deviations deviation = new Deviations(compareRating.getKey(),rate,amount);
                        tempDev.put(compareRating.getKey(),deviation);
                        dev.put(targetRating.getKey(),tempDev);
                        freg.put(targetRating.getKey(),temp);

//                      }
                    }
                }
            }
        }

        HashMap<Integer, HashMap<Integer,Deviations>> deviationMap = new HashMap<>();
        for (Map.Entry<Integer, HashMap<Integer,Deviations>> deviationItem : dev.entrySet())
            for (Map.Entry<Integer, Deviations> devItem : deviationItem.getValue().entrySet()) {
                HashMap<Integer, Deviations> tempDev;

                if (deviationMap.size() == 0 ||
                        !deviationMap.containsKey(deviationItem.getKey())) {
                    tempDev = new HashMap<>();
                }else {
                    tempDev = deviationMap.get(deviationItem.getKey());
                }

                double a = devItem.getValue().getRating() / freg.get(deviationItem.getKey()).get(devItem.getKey());

                Deviations deviation = new Deviations(devItem.getKey(), a, devItem.getValue().amountOfRatings);
                tempDev.put(devItem.getKey(), deviation);
                deviationMap.put(deviationItem.getKey(), tempDev);
            }
        return deviationMap;
    }

    public HashMap<Integer,Double> predictRating(UserPreference targetUser,HashMap<Integer, HashMap<Integer,Deviations>> slopeOne ){
        double numerator , denominator = 0.0;
        HashMap<Integer,Double> targetUserRatings = targetUser.getRatings();

        HashMap<Integer,Integer> amountOfRaters = new HashMap<>();
        HashMap<Integer,Double> tempRecommendations = new HashMap<>();

        for(Map.Entry<Integer,Double> targetUserRating : targetUserRatings.entrySet()){
            for (Map.Entry<Integer,HashMap<Integer,Deviations>> deviations : slopeOne.entrySet()){
                if(!targetUserRatings.containsKey(deviations.getKey()) && deviations.getValue().containsKey(targetUserRating.getKey())){
                    Deviations dev = deviations.getValue().get(targetUserRating.getKey());
                    int amoutOfRatings = 0;
                    if(amountOfRaters.containsKey(deviations.getKey())){
                        amoutOfRatings = amountOfRaters.get(deviations.getKey());
                    }
                    amoutOfRatings += dev.getAmountOfRatings();
                    amountOfRaters.put(deviations.getKey(),amoutOfRatings);
                    double rating = 0.0;
                    if(tempRecommendations.containsKey(deviations.getKey())){
                        rating = tempRecommendations.get(deviations.getKey());
                    }

                    rating += (dev.getRating() + targetUserRating.getValue()) * dev.getAmountOfRatings();
                    tempRecommendations.put(deviations.getKey(),rating);
                }

            }

        }
        HashMap<Integer,Double> recommendations = new HashMap<>();

        for(Map.Entry<Integer,Double> rec : tempRecommendations.entrySet()) {
            double rating = rec.getValue() / amountOfRaters.get(rec.getKey());
            recommendations.put(rec.getKey(),rating);
        }
        return  recommendations;
    }
}
