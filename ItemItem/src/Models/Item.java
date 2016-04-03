package Models;

import java.util.ArrayList;

public class Item {
    private int iD;
    private String name;
    private String date;
    private String imdb;
    private ArrayList<String> genre = new ArrayList<String>();

    public Item(int iD,String name,String date,String imdb,ArrayList<String> genre){
        this.iD = iD;
        this.name = name;
        this.date = date;
        this.imdb = imdb;
        this.genre = genre;
    }

    public int getiD() {
        return iD;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getImdb() {
        return imdb;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }
}