import java.util.Map;

/**
 * Created by geddy on 17-3-2016.
 */
public class SlopeOne {

    public double calculate(UserPreference targetItem ,UserPreference item){

        double deviation = 0.0;
        int count = 0;
        for (Map.Entry<Integer,Double> entry : targetItem.getRatings().entrySet()) {
            if (item.getRatings().containsKey(entry.getKey())){
                deviation += entry.getValue() - item.getRating(entry.getKey());
                count++;
            }
        }
        return (double) deviation/count;
    }
}
