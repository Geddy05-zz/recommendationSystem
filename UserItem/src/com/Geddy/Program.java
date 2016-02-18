package com.Geddy;

import com.Geddy.Models.UserPreference;
import com.Geddy.Similarity.Cosine;
import com.Geddy.Similarity.Euclidean;
import com.Geddy.Similarity.Pearson;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by lord_ on 16-2-2016.
 */
public class Program {

    public HashMap<String, UserPreference> tmap = new HashMap<String, UserPreference>();

    public void runProgram() {

        MiningData miningdata = new MiningData();
        this.tmap=miningdata.readData();

        Context euclidean = new Context(new Euclidean());
        float distance = euclidean.calculateDistance(tmap.get("1"),tmap.get("3"));
        System.out.println(distance);

        Context pearson = new Context(new Pearson());
        float distancePearson = pearson.calculateDistance(tmap.get("1"),tmap.get("3"));
        System.out.println(distancePearson);

        Context cosine = new Context(new Cosine());
        float distanceCosine = cosine.calculateDistance(tmap.get("1"),tmap.get("3"));
        System.out.println(distanceCosine);
    }
}
