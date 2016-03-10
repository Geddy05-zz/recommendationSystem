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

    public HashMap<String, UserPreference> userRatings = new HashMap<String, UserPreference>();
//    public HashMap<Integer,String> genre = new HashMap<Integer, String>();
    public HashMap<Integer,Item> movies = new HashMap<Integer, Item>();
    ArrayList<Neighbour> neighbours = new ArrayList<Neighbour>();
    List<Recommendation> recommendations;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");


    public String targetUserKey = "7";
    public int methodnr = 3;
    public UserPreference targetUser;
    public double threshold = 0.35;
    public int amountOfNeighbours = 25;
    public int numberOfRecommendations = 10;

    public void runProgram() {


        // get input
        getInputValues();

        // MiningData
        MiningData miningdata = new MiningData();
        this.userRatings = miningdata.readData();
        HashMap<Integer,String> genre  = miningdata.getGenreFromFile();
        this.movies = miningdata.getMovieFromFile(genre);

        this.targetUser = this.userRatings.get(targetUserKey);

        // Get Neighbors Users
        nearestNeighbour();

        // get recommended items
        recommendations = recommend();

        printResults();
    }

    public void getInputValues(){

        // get user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select target user ");
        this.targetUserKey = scanner.next();

        System.out.println("1   =>  Cosine");
        System.out.println("2   =>  Euclidean");
        System.out.println("3   =>  Pearson");
        System.out.println("Select method number");
        this.methodnr = scanner.nextInt();

        System.out.println("Number of recommendations wanted ?");
        this.numberOfRecommendations = scanner.nextInt();

    }

    public void printResults(){
        // format is a layout for printing on the command line
        String format = "%-40s%s%n";

        System.out.println("\n\n");
        System.out.println("____________________  Neighbours  ____________________");

        for (Neighbour n : neighbours){
            System.out.printf(format," Distance: " + decimalFormat.format(n.getDistance()),"   User:"+ n.getUser().getUserId());
        }

        // format is a layout for printing on the command line
        format = "%-65s%-50s%-20s%-20s%n";

        System.out.println("\n\n");
        System.out.println("__________________  Recommendation  __________________");
        for (Recommendation r : recommendations){
            Item item = movies.get(r.getItemId());
            System.out.printf(format," Item: " + item.getName() ,item.getGenre(), item.getDate(),
                    "   Rating:"+ decimalFormat.format(r.getRating()));
        }
    }

    // return nearest Neighbors depending on method the user choice
    public void nearestNeighbour() {
        UserPreference target = userRatings.get(targetUserKey);
        Double lowestInList = 2.0;

        Context context = CreateContext();

        int count = 0;
        for (Map.Entry<String, UserPreference> entry : userRatings.entrySet()) {
            String key = entry.getKey();
            UserPreference user = entry.getValue();

            // if user in the list isn't the target user
            if (!key.equals(targetUserKey)){
                double distance =  context.calculateDistance(target,entry.getValue());

                // Check if user distance is greater then threshold and user have another item rated
                if(distance > threshold && haveMoreRatings(target,user)){
                    Neighbour neighbour = new Neighbour(user,distance);
                    if(distance < lowestInList){
                        lowestInList = distance;
                    }

                    // Check if neighbours list isn't bigger then we want to.
                    // if it is bigger remove the lowest neighbour and change
                    // and change the threshold to lowest item.

                    if(count >= amountOfNeighbours){
                        if(distance >lowestInList){
                            // we add the neighbour before we check the lowest
                            neighbours.add(neighbour);

                            Neighbour lowestNeighbour = null;

                            // check for lowest neighbour
                            for (Neighbour n : neighbours){
                                if(lowestNeighbour == null){
                                    lowestNeighbour = n;
                                }else if (lowestNeighbour.getDistance() > n.getDistance() ){
                                    lowestNeighbour = n;
                                }
                            }
                            // remove the lowest
                            neighbours.remove(lowestNeighbour);
                        }
                        // change the threshold
                        threshold = lowestInList;
                    }else{
                        neighbours.add(neighbour);
                        count++;
                    }
                }
            }
//            else{
//                targetUser = entry.getValue();
//            }
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

    // Return a list with top N recommendations.
    public List<Recommendation> recommend(){
        ArrayList<Recommendation> recommendations = new ArrayList<Recommendation>();

        // get articles from the neighbours that the target user didn't rate
        ArrayList<Integer> articles = new ArrayList<Integer>();
        for (Neighbour n : neighbours){
            for(Map.Entry<Integer, Double> entry : n.getUser().getRatings().entrySet()) {
                if (!targetUser.getRatings().containsKey(entry.getKey()) && !articles.contains(entry.getKey())) {
                    articles.add(entry.getKey());
                }
            }
        }

        // Loop the list of articles for calculate the rate for the target user.
        for (Integer article : articles ){
            HashMap<Double,Double> tempRecommendation = new HashMap<Double, Double>();

            // get the ratings of the neighbours and put them in the hash map.
            // The key is the rating and value is the distance
            for (Neighbour n : neighbours){
                if(n.getUser().getRatings().containsKey(article)){
                    tempRecommendation.put(n.getUser().getRating(article),n.getDistance());
                }
            }

            Double ratingSum = 0.0;
            Double weightSum = 0.0;

            // calculate the rating for a article. and add the article if there a more then one rating
            int amountOrates = 0;
            for(Map.Entry<Double, Double> entry : tempRecommendation.entrySet()){
                ratingSum += entry.getKey() * entry.getValue();
                weightSum += entry.getValue();
                amountOrates++;

            }

            // if there a 3 or more rates add to recommendation
            if (amountOrates >= 3) {
                Recommendation rec = new Recommendation(article, (ratingSum / weightSum));
                recommendations.add(rec);
            }
        }

        // sort the list
        recommendations.sort(new recommendationComparator());
        return  recommendations.subList(0,numberOfRecommendations);
    }
}

// Sort function/ class for sorting the neighbours
class neighbourComparator implements Comparator<Neighbour> {
    @Override
    public int compare(Neighbour a, Neighbour b) {
        return a.getDistance() > b.getDistance() ? -1 : a.getDistance().equals(b.getDistance()) ? 0 : 1;
    }
}

// Sort function/ class for sorting the recommendations
class recommendationComparator implements Comparator<Recommendation> {
    @Override
    public int compare(Recommendation a, Recommendation b) {
        return a.getRating() > b.getRating() ? -1 : a.getRating() == (b.getRating()) ? 0 : 1;
    }
}
