package com.Geddy;

import com.Geddy.Models.Item;
import com.Geddy.Models.Neighbour;
import com.Geddy.Models.Recommendation;
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
//    public HashMap<Integer,String> genre = new HashMap<Integer, String>();
    public HashMap<Integer,Item> items = new HashMap<Integer, Item>();
    ArrayList<Neighbour> neighbours = new ArrayList<Neighbour>();
    DecimalFormat decimal = new DecimalFormat("#.##");


    public String targetUserKey = "7";
    public int methodnr = 3;
    public UserPreference targetUser;
    public double threshold = 0.35;
    public int amountOfNeighbours = 25;
    public int numberOfRecommendations = 10;

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

        System.out.println("Number of recommendations wanted ?");
        numberOfRecommendations = scanner.nextInt();

        // MiningData
        MiningData miningdata = new MiningData();
        this.tmap = miningdata.readData();
        HashMap<Integer,String> genre  = miningdata.getGenreFromFile();
        this.items = miningdata.getMovieFromFile(genre);
        // make genre empty for saving memory;
        genre = null;

        // Get Neighbors Users
        nearestNeighbour();
        System.out.println("\n\n");
        System.out.println("____________________Neighbours____________________");

        String format = "%-40s%s%n";

        for (Neighbour n : neighbours){
            System.out.printf(format," Distance: " + decimal.format(n.getDistance()),"   User:"+ n.getUser().getUserId());
        }

        ArrayList<Recommendation> recommendations = recommend();

        System.out.println("\n\n");
        System.out.println("__________________Recommendation__________________");
        for (Recommendation r : recommendations.subList(0,numberOfRecommendations)){
            System.out.printf(format," Item: " + items.get(r.getItemId()).getName() ,"   Rating:"+ r.getRating());
        }
    }

    public void nearestNeighbour() {
        UserPreference target = tmap.get(targetUserKey);
        Double lowestInList = 2.0;

        Context context = CreateContext();

        int count = 0;
        for (Map.Entry<String, UserPreference> entry : tmap.entrySet()) {
            String key = entry.getKey();
            UserPreference user = entry.getValue();
            if (!key.equals(targetUserKey)){

                double distance =  context.calculateDistance(target,entry.getValue());

                if(distance > threshold && haveMoreRatings(target,user)){
                    Neighbour neighbour = new Neighbour(user,distance);

                    if(distance < lowestInList){
                        lowestInList = distance;
                    }

                    if(count >= amountOfNeighbours){
                        if(distance >lowestInList){
                            neighbours.add(neighbour);

                            Neighbour lowestNeighbour = null;
                            for (Neighbour n : neighbours){
                                if(lowestNeighbour == null){
                                    lowestNeighbour = n;
                                }else if (lowestNeighbour.getDistance() > n.getDistance() ){
                                    lowestNeighbour = n;
                                }
                            }
                            neighbours.remove(lowestNeighbour);
                        }
                        threshold = lowestInList;
                    }else{
                        neighbours.add(neighbour);
                        count++;
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

    public ArrayList<Recommendation> recommend(){
        ArrayList<Recommendation> recommendations = new ArrayList<Recommendation>();

        ArrayList<Integer> articles = new ArrayList<Integer>();
        for (Neighbour n : neighbours){
            for(Map.Entry<Integer, Double> entry : n.getUser().getRatings().entrySet()) {
                if (!targetUser.getRatings().containsKey(entry.getKey())) {
                    articles.add(entry.getKey());
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
            Recommendation rec = new Recommendation(article,(ratingSum/weightSum));
            recommendations.add(rec);
        }

        recommendations.sort(new recommandationComparator());
        return  recommendations;
    }
}

class neighbourComparator implements Comparator<Neighbour> {
    @Override
    public int compare(Neighbour a, Neighbour b) {
        return a.getDistance() > b.getDistance() ? -1 : a.getDistance().equals(b.getDistance()) ? 0 : 1;
    }
}

class recommandationComparator implements Comparator<Recommendation> {
    @Override
    public int compare(Recommendation a, Recommendation b) {
        return a.getRating() > b.getRating() ? -1 : a.getRating() == (b.getRating()) ? 0 : 1;
    }
}
