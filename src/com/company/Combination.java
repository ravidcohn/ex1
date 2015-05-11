package com.company;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pinhas on 05/05/2015.
 */


public class Combination {
    public static ArrayList<ArrayList<Double>> CreateLmbdaList(int n_gram) {

        // Create an alphabet to work with
        char[] alphabet = new char[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u'};
        HashMap<Character,Double> vals = new HashMap<>();
        vals.put('a',0.0);
        vals.put('b',0.05);
        vals.put('c',0.1);
        vals.put('d',0.15);
        vals.put('e',0.2);
        vals.put('f',0.25);
        vals.put('g',0.3);
        vals.put('h',0.35);
        vals.put('i',0.4);
        vals.put('j',0.45);
        vals.put('k',0.5);
        vals.put('l',0.55);
        vals.put('m',0.6);
        vals.put('n',0.65);
        vals.put('o',0.7);
        vals.put('p',0.75);
        vals.put('q',0.8);
        vals.put('r',0.85);
        vals.put('s',0.9);
        vals.put('t',0.95);
        vals.put('u',1.0);
        // Find all possible combinations of this alphabet in the string size of 3
        ArrayList<ArrayList<Double>> allLmbda = new ArrayList<>();
        possibleLmbda(n_gram, alphabet,"",allLmbda,vals);

        return allLmbda;
    }


    private static void possibleLmbda(int maxLength, char[] alphabet, String curr,ArrayList<ArrayList<Double>> allLmbda,HashMap<Character,Double> vals) {

        // If the current string has reached it's maximum length
        if (curr.length() == maxLength) {
            ArrayList<Double> list = new ArrayList<>();
            double sum = 0;
            for (int i = 0; i < maxLength; i++) {
                sum += vals.get(curr.charAt(i));
                list.add(vals.get(curr.charAt(i)));
            }
            if (sum == 1) {
                allLmbda.add(list);
            }
            // Else add each letter from the alphabet to new strings and process these new strings again
        } else {
            for (int i = 0; i < alphabet.length; i++) {
                String oldCurr = curr;
                curr += alphabet[i];
                possibleLmbda(maxLength, alphabet, curr, allLmbda, vals);
                curr = oldCurr;
            }
        }
    }
}