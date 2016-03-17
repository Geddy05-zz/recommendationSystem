import java.util.HashMap;

/**
 * Created by geddy on 15-3-2016.
 */

public class Program {

    HashMap<String, UserPreference> userItem;
    public void runProgram() {
        MiningData miningData = new MiningData();
        userItem = miningData.readData();

        SlopeOne so = new SlopeOne();
        double a = so.calculate(userItem.get("103"),userItem.get("104"));
        System.out.print(a);
    }
}
