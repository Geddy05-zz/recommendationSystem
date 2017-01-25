import Models.Deviations;
import Models.Item;
import Models.UserPreference;
import apple.laf.JRSUIConstants;
import javafx.util.Pair;

import java.net.PortUnreachableException;
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
    public static boolean allowPrinting = false;
    public String targetUserKey = "3";
    public int dataSetNumber = 1;
    public int numberOfRecommendations = 3;
    public int updateDeviation = 0;
    public HashMap<Integer, Item> movies = new HashMap<>();
    DecimalFormat decimalFormat = new DecimalFormat("#.##");


    public void runProgram() {

        getInputValues();

        System.out.println("Start slope one ");

        long startTime = System.currentTimeMillis();
        MiningData miningData = new MiningData(dataSetNumber);
        userItem = miningData.readData();

        if(dataSetNumber > 2){
            if(dataSetNumber == 3) {
                HashMap<Integer, String> genre = miningData.getGenreFromFile();
                this.movies = miningData.getMovieFromFile(genre);
            }else{
                this.movies = miningData.getMovieFromFile();
            }
        }

        long deviationTime = System.currentTimeMillis();
        SlopeOne so = new SlopeOne();
        HashMap<Integer, HashMap<Integer, Deviations>>  a = so.calculate(userItem);

        if(allowPrinting) {
            System.out.println("======== Deviation ======== ");
            print(a);
        }
        if (updateDeviation == 1) {

            a = so.update(a,new Pair(105,4.0),userItem.get("3"));

            if(allowPrinting) {
                System.out.println("======== Deviation After update ======== ");
                print(a);
            }
        }
        long computationTime = System.currentTimeMillis();


        List<Recommendation> recommendations = so.predictRating(userItem.get(targetUserKey),a,numberOfRecommendations);
        printResults(recommendations);

        long endTime = System.currentTimeMillis();

        System.out.println(" ");
        System.out.println("====== Benchmark =====");
        System.out.println("Total Slope One time: " + (endTime-deviationTime)/ 1000.0 + "sec");
        System.out.println("PredictRating time: " + (endTime-computationTime) / 1000.0 + "sec");
        System.out.println("Total time: " + (endTime-startTime) / 1000.0 + "sec");
    }

    public void getInputValues(){

        // get user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select target user ");
        this.targetUserKey = scanner.next();

        System.out.println("1   =>  Book");
        System.out.println("2   =>  Small set");
        System.out.println("3   =>  MovieLens 100k");
        System.out.println("4   =>  MovieLens 1 M");

        System.out.println("Select a data set ");
        this.dataSetNumber = scanner.nextInt();

        System.out.println("0   =>  no Update");
        System.out.println("1   =>  Update ");
        this.updateDeviation = scanner.nextInt();

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
                    System.out.print(dev.getKey() + " = " + dev.getValue().getRating() + "  " + dev.getValue().getAmountOfRatings() + ", ");
                }
                System.out.println(" ");
            }
            System.out.println(" ");
        }
    }
}
