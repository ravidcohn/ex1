package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravid on 09/05/2015.
 */
public class test2 {
    public static void main(String[] args){
        ArrayList<Double> scores = new ArrayList<>();
        scores.add((double) 34);
        scores.add((double) 56);
        scores.add((double) 34);
        scores.add((double) 100);
        scores.add((double) 3);
        scores.add((double) 56);
        GraphPanel.createAndShowGui(scores);
    }
}
