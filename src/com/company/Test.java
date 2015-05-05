package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ravid on 04/05/2015.
 */
public class Test {
    public static void Eval(String[] args){
        long time = System.currentTimeMillis();
        double Perplexity = 0;
        double N;
        int n_gram;
        String method;
        String[] data;
        data = readData(args[1]);
        method = data[0];
        n_gram = Integer.parseInt(data[1]);
        HashMap<String,ArrayList<Double>>[] Table = new HashMap[n_gram];
        ArrayList<String> corpus = Parse.readCorpus(args[0]);
        N = readNumberOfWords(corpus);
        if (method=="wb"){
            Table = readWB(args[1]);
        }else if(method=="LS"){
            Table = readLS(args[1]);
        }
        for (String line: corpus){
            if (method=="wb") {
                Perplexity += evalLS(line, Table);
            }else if(method=="LS"){
                Perplexity += evalWB(line, Table);
            }
        }

        Perplexity = 1/Math.pow(Perplexity, -N);
        System.out.println(Perplexity);
        time = System.currentTimeMillis() - time;
        double sec = ((double)time)/1000;
        System.out.println("Run time: "+sec+" sec");
    }

    private static double readNumberOfWords(ArrayList<String> corpus) {
        double N = 0;
        String[] tokens;
        for (String line: corpus){
            tokens = line.split(" ");
            N +=tokens.length;
        }
        return N;
    }

    private static double evalLS(String line, HashMap<String,ArrayList<Double>>[] table){
        //TODO write the function.
        return 0;
    }

    private static double evalWB(String line, HashMap<String,ArrayList<Double>>[] table){
        //TODO write the function.
        return 0;
    }
/*
    Read T-N gram table.
 */
    private static HashMap<String,ArrayList<Double>>[] readLS(String path) {
        //TODO write the function.
        return null;
    }
/*
    read n gram table.
 */
    private static HashMap<String,ArrayList<Double>>[] readWB(String path) {
        BufferedReader br = null;
        String sCurrentLine;
        String line;
        String[] tokens;
        int n_gram = 0;
        String s;
        try {
            br = new BufferedReader(new FileReader(path));
            while ((sCurrentLine = br.readLine()) != null) {
                line = br.readLine();

                tokens = line.split(" ");

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
    }

    private static String[] readData(String path) {
        String[] data = null;
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(path));
            line = br.readLine();
            data = line.split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return data;
    }

}
