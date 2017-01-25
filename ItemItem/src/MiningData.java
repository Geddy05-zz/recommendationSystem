import Models.Item;
import Models.UserPreference;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by geddy on 17-3-2016.
 */
public class MiningData {
    private int dataSetNumber;
    public MiningData(int dataSetNumber){
        this.dataSetNumber = dataSetNumber;
    }

    public HashMap<String, UserPreference> readData(){
        HashMap<String, UserPreference> userItem = new HashMap<String,UserPreference>();

        try {
            final Scanner data;
            // select right dataSet
            if(dataSetNumber == 1) {
                data = new Scanner(new FileReader("test.data"));
            }else if (dataSetNumber == 2){
                data = new Scanner(new FileReader("userItem.data"));
            }else if (dataSetNumber == 3){
                data = new Scanner(new FileReader("u.data"));
            }else{
                data = new Scanner(new FileReader("ratings.dat"));
            }
            while (data.hasNext()) {
                final String[] columns;
                if(dataSetNumber == 1) {
                    columns = data.next().split(",");
                }else if (dataSetNumber == 2){
                    columns = data.next().split(",");
                }else if (dataSetNumber == 3){
                    columns = data.nextLine().split("\\t");
                }else{
                    columns = data.nextLine().split("::");
                }
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
            System.out.println("readData" +e);
        }

        // Log treeMap
        if(Program.allowPrinting) {
            for (Map.Entry<String, UserPreference> entry : userItem.entrySet()) {
                String key = entry.getKey();
                UserPreference preference = entry.getValue();
                System.out.println(key + "  => " + preference.getRatings());
            }
            System.out.println(" ");
        }
        System.out.println("Done Loading Ratings");
        return userItem;
    }

    public HashMap<Integer,String> getGenreFromFile(){
        HashMap<Integer,String> genre = new HashMap<Integer, String>();

        try{
            final Scanner genreData = new Scanner(new FileReader("u.genre"));
            while (genreData.hasNext()){
                final String[] columns = genreData.next().split("[|]");
                genre.put(Integer.parseInt(columns[1]),columns[0]);
            }

        }catch(Exception e){
            System.out.println("Genre: "+e);
        }
        if(Program.allowPrinting) {
            for(Map.Entry<Integer,String> entry : genre.entrySet()) {
                System.out.println(entry.getKey() + "  => " + entry.getValue());
            }
        }
        return genre;

    }

    public HashMap<Integer, Item>getMovieFromFile(HashMap<Integer,String> genres){
        HashMap<Integer,Item> movies = new HashMap<Integer, Item>();

        try{
            final Scanner genreData = new Scanner(new FileReader("u.item"));
            while (genreData.hasNext()){
                final String[] columns = genreData.nextLine().split("\\|");
                int id = Integer.parseInt(columns[0]);
                String name = columns[1];
                String date = columns[2];
                String imdb = columns[4];
                ArrayList<String> genre = new ArrayList<String>();

                int count = 5;
                for(int i = 0; i < columns.length - 5; i++){
                    if( Integer.parseInt(columns[count]) == 1 ) {
                        genre.add(genres.get(i));
                    }
                    count++;
                }
                Item item = new Item(id,name,date,imdb,genre);
                movies.put(Integer.parseInt(columns[0]),item);
            }

        }catch(Exception e){
            System.out.println(" movies"+e);
        }

        if(Program.allowPrinting) {
            for(Map.Entry<Integer,Item> entry : movies.entrySet()) {
                System.out.println(entry.getKey() + "  => " + entry.getValue().getName() + " / " + entry.getValue().getGenre() );
            }
        }
        return movies;
    }

    public HashMap<Integer, Item>getMovieFromFile(){
        HashMap<Integer,Item> movies = new HashMap<Integer, Item>();

        try{
            final Scanner genreData = new Scanner(new FileReader("movies.dat"));
            while (genreData.hasNext()){
                final String[] columns = genreData.nextLine().split("::");
                int id = Integer.parseInt(columns[0]);
                String name = columns[1];
                String imdb = columns[2];

                Item item = new Item(id,name,imdb);
                movies.put(Integer.parseInt(columns[0]),item);
            }

        }catch(Exception e){
            System.out.println(e);
        }

        if(Program.allowPrinting) {
            for(Map.Entry<Integer,Item> entry : movies.entrySet()) {
                System.out.println(entry.getKey() + "  => " + entry.getValue().getName() + " / " + entry.getValue().getImdb() );
            }
        }
        System.out.println("Done Loading movies");
        return movies;
    }
}
