import java.util.HashMap;
import java.util.Map;

/**
 * Created by geddy on 15-3-2016.
 */

public class Program {

    HashMap<String, UserPreference> userItem;
    public void runProgram() {
        MiningData miningData = new MiningData();
        userItem = miningData.readData();

        SlopeOne so = new SlopeOne();
        HashMap<Integer, HashMap<Integer,Deviations>>  a = so.calculate(userItem);

        for(Map.Entry<Integer, HashMap<Integer,Deviations>> entry : a.entrySet()) {
            Integer key = entry.getKey();
            HashMap<Integer,Deviations> differents = entry.getValue();
            System.out.println(" ");
            System.out.print(key + "  => ");
            for(Map.Entry<Integer,Deviations> dev : differents.entrySet()) {
                System.out.print(dev.getKey() + " = " + dev.getValue().getRating() + ", ");
            }
        }
    }
}
