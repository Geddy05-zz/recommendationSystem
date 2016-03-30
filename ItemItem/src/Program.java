import javafx.util.Pair;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by geddy on 15-3-2016.
 */

public class Program {

    HashMap<String, UserPreference> userItem;
    public String targetUserKey = "3";
    public int dataSetNumber = 1;
    public int numberOfRecommendations = 3;
    public HashMap<Integer,Item> movies = new HashMap<>();
    DecimalFormat decimalFormat = new DecimalFormat("#.##");


    public void runProgram() {

        getInputValues();

        MiningData miningData = new MiningData(dataSetNumber);
        userItem = miningData.readData();

        if(dataSetNumber > 2){
            HashMap<Integer,String> genre  = miningData.getGenreFromFile();
            this.movies = miningData.getMovieFromFile(genre);
        }

        SlopeOne so = new SlopeOne();
        HashMap<Integer, HashMap<Integer,Deviations>>  a = so.calculate(userItem);

//        System.out.println("======== Deviation ======== ");
        print(a);

//        a = so.update(a,new Pair(105,4.0),userItem.get("3"));
//        System.out.println("======== Deviation After update ======== ");
//        print(a);

        List<Recommendation> recommendations = so.predictRating(userItem.get(targetUserKey),a,numberOfRecommendations);
        printResults(recommendations);
    }

    public void getInputValues(){

        // get user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select target user ");
        this.targetUserKey = scanner.next();

        System.out.println("1   =>  Book");
        System.out.println("2   =>  Small set");
        System.out.println("3   =>  MovieLens 100k");
        System.out.println("Select a data set ");
        this.dataSetNumber = scanner.nextInt();

        System.out.println("Number of recommendations wanted ?");
        this.numberOfRecommendations = scanner.nextInt();

    }

    public void printResults(List<Recommendation> recommendations){
        // format is a layout for printing on the command line
        String format = "%-65s%-50s%-20s%-20s%n";

        System.out.println("\n\n");
        System.out.println("__________________  Recommendation  __________________");
        for (Recommendation r : recommendations){
            if(dataSetNumber > 2) {
                Item item = movies.get(r.getItemId());
                System.out.printf(format, " Item: " + item.getName(), item.getGenre(), item.getDate(),
                        "   Rating:" + decimalFormat.format(r.getRating()));
            }else{
                System.out.println(" Item: " + r.getItemId()+
                        "   Rating:" + decimalFormat.format(r.getRating()));
            }
        }
    }

    public void print(HashMap<Integer, HashMap<Integer,Deviations>> printMap){
        if (dataSetNumber < 3) {
            for (Map.Entry<Integer, HashMap<Integer, Deviations>> entry : printMap.entrySet()) {
                Integer key = entry.getKey();
                HashMap<Integer, Deviations> differents = entry.getValue();
                System.out.print(key + "  => ");

                for (Map.Entry<Integer, Deviations> dev : differents.entrySet()) {
                    System.out.print(dev.getKey() + " = " + dev.getValue().getRating() + "  " + dev.getValue().amountOfRatings + ", ");
                }
                System.out.println(" ");
            }
            System.out.println(" ");
        }
    }
}
