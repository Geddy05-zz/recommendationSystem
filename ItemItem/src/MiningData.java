import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by geddy on 17-3-2016.
 */
public class MiningData {

    public HashMap<String, UserPreference> readData(){
        HashMap<String, UserPreference> userItem = new HashMap<String,UserPreference>();

        try {
//            final Scanner data = new Scanner(new FileReader("userItem.data"));
            final Scanner data = new Scanner(new FileReader("test.data"));
            while (data.hasNext()) {
                final String[] columns = data.next().split(",");
                if (userItem.containsKey(columns[0])){
                    UserPreference user = userItem.get(columns[0]);
                    user.setRating(Integer.parseInt(columns[1]),Double.parseDouble(columns[2]));
                    userItem.put(columns[0], user);
                }else {
                    UserPreference userPreference = new UserPreference(Integer.parseInt(columns[0]), Integer.parseInt(columns[1]),Double.parseDouble(columns[2]));
                    userItem.put(columns[0], userPreference);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

        // Log treeMap
        for(Map.Entry<String,UserPreference> entry : userItem.entrySet()) {
            String key = entry.getKey();
            UserPreference preference = entry.getValue();
            System.out.println(key + "  => " + preference.getRatings());
        }

        return userItem;
    }
}
