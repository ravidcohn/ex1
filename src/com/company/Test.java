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
        ArrayList<ArrayList<Double>> lambda;
        ArrayList<Double> temp_lambda;
        ArrayList<String> corpus = Parse.readCorpus(args[0]);
        N = readNumberOfWords(corpus, n_gram);
        if (method=="wb"){
            readWB(args[1]);
        }else if(method=="ls"){
            readLS(args[1]);
        }
        lambda = interpolation(n_gram);
        if (method=="wb") {
            for (temp_lambda:lambda) {
                for (String line: corpus) {
                    Perplexity += evalLS(line, temp_lambda);
                }
                Perplexity = 1/Math.pow(Perplexity, -N);
                System.out.println(Perplexity);
            }
            }else if(method=="ls"){
                for (String line: corpus) {
                    Perplexity += evalWB(line, n_gram);
                 }
            Perplexity = 1/Math.pow(Perplexity, -N);
            System.out.println(Perplexity);
         }

        Perplexity = 1/Math.pow(Perplexity, -N);
        System.out.println(Perplexity);
        time = System.currentTimeMillis() - time;
        double sec = ((double)time)/1000;
        System.out.println("Run time: "+sec+" sec");
    }

    private static ArrayList<ArrayList<Double>> interpolation(int n_gram) {
        return null;
    }

    private static double readNumberOfWords(ArrayList<String> corpus,int n_gram) {
        //TODO n_gram
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

    private static double evalWB(String line, ArrayList<Double> lambda){
        //TODO write the function.
        int n_gram = lambda.size();
        String[] tokens;
        String[] subStr;
        double[] PP = new double[lambda.size()];
        tokens = line.split(" ");
        for (int i=0;i<=tokens.length-n_gram){
            for(int j=0;j<n_gram;j++){
                subStr = substring(tokens,);
            }


        }
            int end = (wi.length()-(tokens[tokens.length-1].length())-1);
            subStr = wi.substring(0, end);
        for (int i=0;i<PP.length;i++){
            PP[i] = Table[i].
        }
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
        String Start = "\\";
        ArrayList<Double> arr = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(path));
            while ((sCurrentLine = br.readLine()) != null) {
                line = br.readLine();

                tokens = line.split(" ");
                if (tokens.length>0){
                    if (tokens[0] =="Start"){
                        n_gram ++;
                    }else if(n_gram>0){
                        arr.add(0,Double.parseDouble(tokens[0]));
                        arr.add(1,Double.parseDouble(tokens[2]));
                        Table[n_gram].put(tokens[1],arr);
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
