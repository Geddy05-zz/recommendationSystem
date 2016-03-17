import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * Created by geddy on 17-3-2016.
 */
public class SlopeOne {

    public HashMap<Integer, HashMap<Integer,Double>>  calculate(HashMap<String, UserPreference> userRatings){

        HashMap<Integer, HashMap<Integer,Integer>> freg = new HashMap<>();
        HashMap<Integer, HashMap<Integer,Double>> dev = new HashMap<>();
        double deviation = 0.0;

        for (Map.Entry<String,UserPreference> user : userRatings.entrySet()) {

            for(Map.Entry<Integer,Double> targetRating : user.getValue().getRatings().entrySet()){
                for(Map.Entry<Integer,Double> compareRating : user.getValue().getRatings().entrySet()){
                    if(targetRating != compareRating){
//                        if(freg.containsKey(targetRating.getKey()) && freg.get(targetRating.getKey()).containsKey(compareRating.getKey())){
//                            HashMap<Integer,Integer> temp = freg.get(targetRating.getKey());
//                            HashMap<Integer,Double> tempDev = dev.get(targetRating.getKey());
//
//                            Double rating = tempDev.get(targetRating.getKey());
//                            Integer count = temp.get(targetRating.getKey());
//
//                            count += 1;
//                            rating += targetRating.getValue() - compareRating.getValue();
//
//                            temp.put(compareRating.getKey(),count);
//                            tempDev.put(compareRating.getKey(),rating);
//
//                            freg.put(targetRating.getKey(),temp);
//                            dev.put(targetRating.getKey(),tempDev);
//                        }else{
                            HashMap<Integer, Integer> temp;
                            HashMap<Integer, Double> tempDev;
                            if (freg.size() == 0 || !freg.containsKey(targetRating.getKey())) {
                                temp = new HashMap<>();
                                tempDev = new HashMap<>();
                            }else{
                                temp = freg.get(targetRating.getKey());
                                tempDev = dev.get(targetRating.getKey());
                            }

                            Double rating = targetRating.getValue() - compareRating.getValue();
                            temp.put(compareRating.getKey(),1);
                            tempDev.put(compareRating.getKey(),rating);
                            dev.put(targetRating.getKey(),tempDev);
                            freg.put(targetRating.getKey(),temp);

//                        }
                    }
                }
            }

//            if (item.getRatings().containsKey(entry.getKey())){
//                deviation += entry.getValue() - item.getRating(entry.getKey());
//                count++;
//            }
        }
        return dev;
    }
}
