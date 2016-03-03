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
    public int methodnr = 3;
    public UserPreference targetUser;
    public double threshold = 0.35;
    public int amountOfNeighbours = 3;

    public void runProgram() {


        // get input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select targetUSer ");
        targetUserKey = scanner.next();

        System.out.println("1   =>  Cosine");
        System.out.println("2   =>  Euclidean");
        System.out.println("3   =>  Pearson");
        System.out.println("Select method number");
        methodnr = scanner.nextInt();

        // MiningData
        MiningData miningdata = new MiningData();
        this.tmap=miningdata.readData();

        // Get Neighbors Users
        nearestNeighbour();
        System.out.println("\n\n");
        System.out.println("____________________Neighbours____________________");

        String format = "%-40s%s%n";

        for (Neighbour n : neighbours){
            System.out.printf(format," Distance: " + decimal.format(n.getDistance()),"   User:"+ n.getUser().getUserId());
        }

//        HashMap<Integer,Double> recommendations = calculateWeight();
        HashMap<Integer,Double> recommendations = recommend();



        for (Map.Entry<Integer, Double> entry : recommendations.entrySet()){
            System.out.printf(format," Article: " + entry.getKey(),"   Rating:"+ entry.getValue());
        }

    }

    public void nearestNeighbour() {
        UserPreference target = tmap.get(targetUserKey);
        Double lowestInList = 1.0;
        Neighbour lowestNeighbour = null;

        Context context = CreateContext();

        for (Map.Entry<String, UserPreference> entry : tmap.entrySet()) {
            String key = entry.getKey();
            UserPreference user = entry.getValue();
            if (!key.equals(targetUserKey)){

                double distance =  context.calculateDistance(target,entry.getValue());

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

    // create right method
    public Context CreateContext(){
        switch (methodnr){
            case 1 :
                return  new Context(new Cosine());
            case 2 :
                return  new Context(new Euclidean());
            default :
                return new Context(new Pearson());
        }
    }

    // Check if we can use this user
    public boolean haveMoreRatings(UserPreference target, UserPreference user){
        for (Map.Entry<Integer, Double> entry : user.getRatings().entrySet()) {
            if(!target.getRatings().containsKey(entry.getKey())){
                return true;
            }
        }
        return false;
    }

    // TODO: If a article is rated by more then 1 user create the Weight of all the neighbors who rated the article

    public HashMap<Integer,Double> recommend(){
        HashMap<Integer,Double> recommendations = new HashMap<Integer, Double>();

        ArrayList<Integer> articles = new ArrayList<Integer>();
        for (Neighbour n : neighbours){
            for(Map.Entry<Integer, Double> entry : n.getUser().getRatings().entrySet()) {
                if (!targetUser.getRatings().containsKey(entry.getKey())) {
                    articles.add(entry.getKey());
                    recommendations.put(entry.getKey(),0.0);
                }
            }
        }

        for (Integer article : articles ){
            HashMap<Double,Double> tempRecommendation = new HashMap<Double, Double>();
            for (Neighbour n : neighbours){
                if(n.getUser().getRatings().containsKey(article)){
                    tempRecommendation.put(n.getUser().getRating(article),n.getDistance());
                }
            }

            Double ratingSum = 0.0;
            Double weightSum = 0.0;

            for(Map.Entry<Double, Double> entry : tempRecommendation.entrySet()){
                ratingSum += entry.getKey() * entry.getValue();
                weightSum += entry.getValue();
            }

            recommendations.put(article,(ratingSum/weightSum));
        }

        return  recommendations;
    }

//    public HashMap<Integer,Double> calculateWeight(){
//        Double sumDistance = 0.0;
//        HashMap<Integer,Double> recommendations = new HashMap<Integer, Double>();
//
//        for (Neighbour n : neighbours){
//            sumDistance += n.getDistance();
//        }
//
//        for (Neighbour n : neighbours){
//            n.setWeight(n.getDistance() / sumDistance);
//
//            HashMap<Integer,Double> nRatings = n.getUser().getRatings();
//
//            for(Map.Entry<Integer, Double> entry : nRatings.entrySet()){
//                if (!targetUser.getRatings().containsKey(entry.getKey())){
//                    Double nRating = entry.getValue() * n.getWeight();
//                    Integer key = entry.getKey();
//                    if(recommendations.containsKey(key)){
//                        Double rRating = recommendations.get(key);
//                        recommendations.put(key,(rRating + nRating));
//                    }else{
//                        recommendations.put(key,nRating);
//                    }
//                }
//            }
//        }
//        return  recommendations;
//    }
}

class neighbourComparator implements Comparator<Neighbour> {
    @Override
    public int compare(Neighbour a, Neighbour b) {
        return a.getDistance() > b.getDistance() ? -1 : a.getDistance().equals(b.getDistance()) ? 0 : 1;
    }
}
