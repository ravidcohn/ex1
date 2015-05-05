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
    private static HashMap<String,ArrayList<Double>>[] Table;
    public static void main(String[] args){
        long time = System.currentTimeMillis();
        double Perplexity = 0;
        double N;
        int n_gram;
        String method;
        String[] data;
        data = readData(args[1]);
        method = data[0];
        n_gram = Integer.parseInt(data[1]);
        Table = new HashMap[n_gram];
        ArrayList<String> corpus = Parse.readCorpus(args[0]);
        N = readNumberOfWords(corpus);
        if (method=="wb"){
            readWB(args[1]);
        }else if(method=="ls"){
            readLS(args[1]);
        }
        for (String line: corpus){
            if (method=="wb") {
                Perplexity += evalLS(line,n_gram);
            }else if(method=="ls"){
                Perplexity += evalWB(line);
            }
        }

        Perplexity = 1/Math.pow(Perplexity, -N);
        System.out.println("Perplexity: "+Perplexity);
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

    private static double evalLS(String line,int n_gram){
        double pr = 0;
        String[] tokes = line.split(" ");
        String tLine = "";
        int end = tokes.length-n_gram;
        for (int i = 0; i <= end; i++) {
            tLine = "";
            for (int j = 0; j < n_gram ; j++) {
                tLine += tokes[i+j];
            }
            if(Table[n_gram-1].get(tLine).get(0) == null){
                pr = Table[n_gram-1].get("<unseen>:").get(0);
            }
            else{
                pr = Table[n_gram-1].get(tLine).get(0);
            }
        }
        return pr;
    }

    private static double evalWB(String line){
        //TODO write the function.
        return 0;
    }
/*
    Read T-N gram table.
 */
    private static void readLS(String path) {
        int count = -2;
        BufferedReader br = null;
        String sCurrentLine;
        String line;
        String[] tokens;
        int n_gram = 0;
        String s;
        ArrayList<Double> list;
        try {
            br = new BufferedReader(new FileReader(path));
            while ((sCurrentLine = br.readLine()) != null) {
                line = br.readLine();
                if(line.length() > 0){
                    if(line.charAt(0) == '\\'){
                        count++;
                    }
                    else if(count >= 0) {
                        tokens = line.split(" ");
                        list = new ArrayList<>();
                        list.add(Double.parseDouble(tokens[0]));
                        Table[count].put(line.substring(tokens[0].length()+1), list);
                    }
                }
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
/*
    read n gram table.
 */
    private static void readWB(String path) {
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
