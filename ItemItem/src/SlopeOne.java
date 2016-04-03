import Models.Deviations;
import Models.UserPreference;
import javafx.util.Pair;

import java.util.*;

public class SlopeOne {

    public HashMap<Integer, HashMap<Integer, Deviations>>  calculate(HashMap<String, UserPreference> userRatings){

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

                        // create the hashmap or get the right hashmap
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

                        // create Deviations object
                        Deviations deviation = new Deviations(compareRating.getKey(),rate,amount);

                        // update the hashmap
                        tempDev.put(compareRating.getKey(),deviation);
                        dev.put(targetRating.getKey(),tempDev);
                        freg.put(targetRating.getKey(),temp);

//                      }
                    }
                }
            }
        }

        // loop trough the hashmap we just creates for calculating the right deviation
        // depending on the amount of ratings
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

                Deviations deviation = new Deviations(devItem.getKey(), a, devItem.getValue().getAmountOfRatings());
                tempDev.put(devItem.getKey(), deviation);
                deviationMap.put(deviationItem.getKey(), tempDev);
            }
        return deviationMap;
    }

    public List<Recommendation> predictRating(UserPreference targetUser,HashMap<Integer, HashMap<Integer,Deviations>> slopeOne ,
                                                   int numberOfRecommendations){
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
        ArrayList<Recommendation> recommendations = new ArrayList<>();
        for(Map.Entry<Integer,Double> rec : tempRecommendations.entrySet()) {
            double rating = rec.getValue() / amountOfRaters.get(rec.getKey());
            Recommendation recommendation = new Recommendation(rec.getKey(), rating);
            recommendations.add(recommendation);
        }

        recommendations.sort(new recommendationComparator());
        if(recommendations.size() > numberOfRecommendations){
            return  recommendations.subList(0,numberOfRecommendations);
        }
        return  recommendations;
    }

    public HashMap<Integer, HashMap<Integer,Deviations>> update(HashMap<Integer, HashMap<Integer,Deviations>> deviations,
                                                                Pair<Integer,Double> updateItem,
                                                                UserPreference targetUser){
        // loop trow the x as of items
        for (Map.Entry<Integer, HashMap<Integer,Deviations>> deviationItem : deviations.entrySet()) {

            if (updateItem.getKey() == deviationItem.getKey()) {
                // loop trough the y as
                for (Map.Entry<Integer, Deviations> devItem : deviationItem.getValue().entrySet()) {
                    if (updateItem.getKey() != devItem.getKey() && targetUser.getRatings().containsKey(devItem.getKey())){
                        Deviations dev = devItem.getValue();
                        Double newDev = ((dev.getAmountOfRatings()*dev.getRating())+(updateItem.getValue() - targetUser.getRating(devItem.getKey())))/ (dev.getAmountOfRatings() + 1);
                        deviationItem.getValue().put(devItem.getKey(),new Deviations(devItem.getKey(),newDev,dev.getAmountOfRatings() + 1));
                    }
                }
            }else{
                for (Map.Entry<Integer, Deviations> devItem : deviationItem.getValue().entrySet()) {
                    if (updateItem.getKey() == devItem.getKey() && targetUser.getRatings().containsKey(deviationItem.getKey())){
                        Deviations dev = devItem.getValue();
                        Double newDev = ((dev.getAmountOfRatings()*dev.getRating())+( targetUser.getRating(deviationItem.getKey()) - updateItem.getValue()))/ (dev.getAmountOfRatings() + 1);
                        deviationItem.getValue().put(devItem.getKey(),new Deviations(devItem.getKey(),newDev,dev.getAmountOfRatings() + 1));
                    }
                }
            }
        }
        return  deviations;
    }
}

class recommendationComparator implements Comparator<Recommendation> {
    @Override
    public int compare(Recommendation a, Recommendation b) {
        return a.getRating() > b.getRating() ? -1 : a.getRating() == (b.getRating()) ? 0 : 1;
    }
}
