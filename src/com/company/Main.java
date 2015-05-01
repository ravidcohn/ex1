package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {


    }

    public static ArrayList<String> readCorpus(String fileName){
        ArrayList<String> lines = new ArrayList<>();

        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(fileName));

            while ((sCurrentLine = br.readLine()) != null) {
                //TODO
                lines.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        return lines;
    }

    public static HashMap<String,Integer> countAllWords(ArrayList<String> lines, int n_gram){
        //TODO
        return null;
    }

}
