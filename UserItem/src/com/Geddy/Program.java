package com.Geddy;

import com.Geddy.Models.UserPreference;
import com.Geddy.Similarity.Cosine;
import com.Geddy.Similarity.Euclidean;
import com.Geddy.Similarity.Pearson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by geddy on 16-2-2016.
 */
public class Program {

    public HashMap<String, UserPreference> tmap = new HashMap<String, UserPreference>();
    public HashMap<Double, UserPreference> neighbours = new HashMap<Double, UserPreference>();

    public String targetUser = "7";
    public double threshold = 0.35;
    public int amountOfNeighbours = 3;

    public void runProgram() {

        MiningData miningdata = new MiningData();
        this.tmap=miningdata.readData();

//        Context euclidean = new Context(new Euclidean());
//        float distance = euclidean.calculateDistance(tmap.get("1"),tmap.get("3"));
//        System.out.println(distance);
//
//        Context pearson = new Context(new Pearson());
//        float distancePearson = pearson.calculateDistance(tmap.get("1"),tmap.get("3"));
//        System.out.println(distancePearson);
//
//        Context cosine = new Context(new Cosine());
//        float distanceCosine = cosine.calculateDistance(tmap.get("1"),tmap.get("3"));
//        System.out.println(distanceCosine);

        nearestNeighbour();
        System.out.println("\n\n");
        System.out.println("____________________Neighbours____________________");
        DecimalFormat decimal = new DecimalFormat("#.##");

        String format = "%-40s%s%n";
        for (Map.Entry<Double, UserPreference> entry : neighbours.entrySet()) {
            System.out.printf(format," Distance: " + decimal.format(entry.getKey()) ,"   User:"+ entry.getValue().getUserId());
        }
    }

    public void nearestNeighbour() {
        UserPreference target = tmap.get(targetUser);
        Double lowestInList = 1.0;
        for (Map.Entry<String, UserPreference> entry : tmap.entrySet()) {
            String key = entry.getKey();
            UserPreference user = entry.getValue();
            if (!key.equals(targetUser)){

                Context pearson = new Context(new Euclidean());
                double distance = (double) pearson.calculateDistance(target,entry.getValue());
                if(distance > threshold && haveMoreRatings(target,user)){
                    neighbours.put(distance,user);

                    if(neighbours.size() > amountOfNeighbours){
                        if(distance < lowestInList){
                            lowestInList = distance;
                        }
                        threshold = lowestInList;
                        neighbours.remove(lowestInList);
                    }
                }
            }
        }
    }

    public boolean haveMoreRatings(UserPreference target, UserPreference user){
        for (Map.Entry<Integer, Double> entry : user.getRatings().entrySet()) {
            if(!target.getRatings().containsKey(entry.getKey())){
                return true;
            }
        }
        return false;
    }
}
