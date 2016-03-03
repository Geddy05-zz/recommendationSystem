package com.Geddy;

import com.Geddy.Models.Neighbour;
import com.Geddy.Models.UserPreference;
import com.Geddy.Similarity.Cosine;
import com.Geddy.Similarity.Euclidean;
import com.Geddy.Similarity.Pearson;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by geddy on 16-2-2016.
 */
public class Program {

    public HashMap<String, UserPreference> tmap = new HashMap<String, UserPreference>();
    ArrayList<Neighbour> neighbours = new ArrayList<Neighbour>();
    DecimalFormat decimal = new DecimalFormat("#.##");


    public String targetUserKey = "7";
    public UserPreference targetUser;
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

        String format = "%-40s%s%n";

        for (Neighbour n : neighbours){
            System.out.printf(format," Distance: " + decimal.format(n.getDistance()),"   User:"+ n.getUser().getUserId());
        }

        HashMap<Integer,Double> recommendations = calculateWeight();


        for (Map.Entry<Integer, Double> entry : recommendations.entrySet()){
            System.out.printf(format," Article: " + entry.getKey(),"   Rating:"+ entry.getValue());
        }

    }

    public void nearestNeighbour() {
        UserPreference target = tmap.get(targetUserKey);
        Double lowestInList = 1.0;
        Neighbour lowestNeighbour = null;

        for (Map.Entry<String, UserPreference> entry : tmap.entrySet()) {
            String key = entry.getKey();
            UserPreference user = entry.getValue();
            if (!key.equals(targetUserKey)){

                Context pearson = new Context(new Pearson());
                double distance =  pearson.calculateDistance(target,entry.getValue());

                if(distance > threshold && haveMoreRatings(target,user)){
                    Neighbour neighbour = new Neighbour(user,distance);
                    neighbours.add(neighbour);

                    if(neighbours.size() > amountOfNeighbours){
                        if(distance < lowestInList){
                            lowestInList = distance;
                            lowestNeighbour = neighbour;
                        }
                        threshold = lowestInList;

                        neighbours.remove(lowestNeighbour);
                    }
                }
            }else{
                targetUser = entry.getValue();
            }
        }
        neighbours.sort(new neighbourComparator());
    }

    public boolean haveMoreRatings(UserPreference target, UserPreference user){
        for (Map.Entry<Integer, Double> entry : user.getRatings().entrySet()) {
            if(!target.getRatings().containsKey(entry.getKey())){
                return true;
            }
        }
        return false;
    }

    public HashMap<Integer,Double> calculateWeight(){
        Double sumDistance = 0.0;
        HashMap<Integer,Double> recommandations = new HashMap<Integer, Double>();

        for (Neighbour n : neighbours){
            sumDistance += n.getDistance();
        }

        for (Neighbour n : neighbours){
            n.setWeight(n.getDistance() / sumDistance);

            HashMap<Integer,Double> nRatings = n.getUser().getRatings();

            for(Map.Entry<Integer, Double> entry : nRatings.entrySet()){
                if (!targetUser.getRatings().containsKey(entry.getKey())){
                    Double nRating = entry.getValue() * n.getWeight();
                    Integer key = entry.getKey();
                    if(recommandations.containsKey(key)){
                        Double rRating = recommandations.get(key);
                        recommandations.put(key,(rRating + nRating));
                    }else{
                        recommandations.put(key,nRating);
                    }
                }
            }
        }
        return  recommandations;
    }
}

class neighbourComparator implements Comparator<Neighbour> {
    @Override
    public int compare(Neighbour a, Neighbour b) {
        return a.getDistance() > b.getDistance() ? -1 : a.getDistance() == b.getDistance() ? 0 : 1;
    }
}
