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

        for (Map.Entry<String,UserPreference> user : userRatings.entrySet()) {

            for(Map.Entry<Integer,Double> targetRating : user.getValue().getRatings().entrySet()){
                for(Map.Entry<Integer,Double> compareRating : user.getValue().getRatings().entrySet()){
                    if(targetRating != compareRating){

                            HashMap<Integer, Integer> temp;
                            HashMap<Integer, Deviations> tempDev;
                            if (freg.size() == 0 || !freg.containsKey(targetRating.getKey())) {
                                temp = new HashMap<>();
                                tempDev = new HashMap<>();
                            }else{
                                temp = freg.get(targetRating.getKey());
                                tempDev = dev.get(targetRating.getKey());
                            }

                            Double rating = targetRating.getValue() - compareRating.getValue();
                            temp.put(compareRating.getKey(),1);
                            Deviations deviation = new Deviations(compareRating.getKey(),rating);
                            tempDev.put(compareRating.getKey(),deviation);
                            dev.put(targetRating.getKey(),tempDev);
                            freg.put(targetRating.getKey(),temp);

//                        }
                    }
                }
            }
        }
        return dev;
    }

    public void predictRating(UserPreference targetUser,HashMap<Integer, HashMap<Integer,Double>> slopeOne ){
        double numerator , denominator = 0.0;

    }
}
