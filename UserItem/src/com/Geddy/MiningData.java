package com.Geddy;

import com.Geddy.Models.UserPreference;

import java.io.FileReader;
import java.util.*;

/**
 * Created by lord_ on 16-2-2016.
 */
public class MiningData {

    public HashMap<String, UserPreference> readData(){
        HashMap<String, UserPreference> tmap = new HashMap<String,UserPreference>();

        try {
            final Scanner data = new Scanner(new FileReader("userItem.data"));
            while (data.hasNext()) {
                final String[] columns = data.next().split(",");
                if (tmap.containsKey(columns[0])){

                    UserPreference user = tmap.get(columns[0]);
                    user.setRating(Integer.parseInt(columns[1]),Double.parseDouble(columns[2]));
                    tmap.put(columns[0], user);
                }else {
                    UserPreference userPreference = new UserPreference(Integer.parseInt(columns[1]),Double.parseDouble(columns[2]));
                    tmap.put(columns[0], userPreference);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

        // Log treeMap
        for(Map.Entry<String,UserPreference> entry : tmap.entrySet()) {
            String key = entry.getKey();
            UserPreference preference = entry.getValue();
            System.out.println(key + "  => " + preference.getRatings());
        }

        return tmap;
    }

}
