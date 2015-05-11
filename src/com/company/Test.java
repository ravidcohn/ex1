package com.company;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ravid on 04/05/2015.
 */
public class Test {
    public static HashMap<String,ArrayList<Double>>[] Table;

    public static void main(String[] args){
        long time = System.currentTimeMillis();
        double Perplexity = 0;
        double Best_Perplexity = Double.POSITIVE_INFINITY;
        double N;
        int n_gram;
        String method;
        String[] data;
        data = readData(args[1]);
        method = data[0];
        n_gram = Integer.parseInt(data[1]);
        Table = new HashMap[n_gram];
        ArrayList<Double> best_lambda = new ArrayList<>();
        ArrayList<ArrayList<Double>> lambda = new ArrayList<>();
        ArrayList<String> corpus = Parse.readCorpus(args[0]);
        N = readNumberOfWords(corpus, n_gram);
        if (method.equals("wb")){
            readWB(args[1]);
        }else if(method.equals("ls")){
            readLS(args[1]);
        }
        lambda = Combination.CreateLmbdaList(n_gram);
        if (method.equals("wb")) {
            for (ArrayList<Double> temp_lambda:lambda) {
                for (String line: corpus) {
                    Perplexity += evalWB(line, temp_lambda);
                }
                Perplexity = Math.pow(10,Perplexity/N);
                Perplexity = 1/Perplexity;
                if (Perplexity<Best_Perplexity){
                    Best_Perplexity = Perplexity;
                    best_lambda = temp_lambda;
                }
                Perplexity = 0;
            }
            }else if(method.equals("ls")){
                for (String line: corpus) {
                    Perplexity += evalLS(line, n_gram);
                 }
            Perplexity = Math.pow(10,Perplexity/N);
            Perplexity = 1/Perplexity;
            System.out.println(Perplexity);
         }

        Perplexity = 1/Math.pow(Perplexity, -N);
        System.out.println("Best_Perplexity: " + Best_Perplexity);
        System.out.println("best_lambda: " + print_lambda(best_lambda));
        System.out.println("Perplexity: " + Perplexity);
        time = System.currentTimeMillis() - time;
        double sec = ((double)time)/1000;
        System.out.println("Run time: "+sec+" sec");
    }

    public static String print_lambda(ArrayList<Double> best_lambda) {
        String str = "";
        for (double lamda:best_lambda){
            str  += lamda+ " ";
        }
        return str;
    }

    public static ArrayList<ArrayList<Double>> interpolation(int n_gram) {
        return new ArrayList<>();
    }

    public static double readNumberOfWords(ArrayList<String> corpus,int n_gram) {
        double N = 0;
        String[] tokens;
        for (String line: corpus){
            tokens = line.split(" ");
                N+= tokens.length - n_gram + 1;
        }
        return N;
    }

    public static double evalLS(String line,int n_gram){
        double pr = 0;
        String[] tokes = line.split(" ");
        String tLine = "";
        int end = tokes.length-n_gram;
        for (int i = 0; i <= end; i++) {
            tLine = "";
            for (int j = 0; j < n_gram ; j++) {
                tLine += tokes[i+j] + " ";
            }
            if(Table[n_gram-1].get(tLine) == null){
                String sub = tLine.substring(0, tLine.length()-tokes[i+n_gram-1].length()-1);
                if(Table[n_gram-2].get(sub) == null){
                    pr += Table[0].get("<UNK> ").get(1);
                }
                else{
                    pr += Table[n_gram - 2].get(sub).get(1);
                }
            }
            else{
                pr += Table[n_gram-1].get(tLine).get(0);
            }
        }
        return pr;
    }

    public static double evalWB(String line, ArrayList<Double> lambda){
        //TODO write the function.
        int n_gram = lambda.size();
        String[] tokens;
        String[][] subStr = new String[n_gram][2];
        double PP = 0;
        tokens = line.split(" ");
        for (int i=n_gram;i<=tokens.length;i++){
            for(int j=0;j<n_gram;j++){
                subStr[j][0] = Parse.subTokens(tokens, i-1, j);
                if (j>0) {
                    subStr[j][1] = Parse.subTokens(tokens, i - 2, j - 1);
                }
            }
            for(int j=0;j<n_gram;j++) {
                if (Table[j].get(subStr[j][0]) != null) {
                    PP += lambda.get(j) * Math.pow(10,Table[j].get(subStr[j][0]).get(0));
                } else if(j==0) {
                    PP += lambda.get(j) * Math.pow(10,Table[j].get("<UNK> ").get(0));
                }else if(j==1 && Table[j-1].get(subStr[j][1]) != null){
                    PP += lambda.get(j) * Math.pow(10, Table[j-1].get(subStr[j][1]).get(0));
                }else if(j==1){
                    PP += lambda.get(j) * Math.pow(10,Table[j-1].get("<UNK> ").get(0));
                }else  if(Table[j-1].get(subStr[j][1]) != null) {
                    PP += lambda.get(j) * Math.pow(10, Table[j-1].get(subStr[j][1]).get(1));
                }
            }

        }

        if (PP==0) {
            return 0;
        }else {
            return Math.log10(PP);
        }
    }
/*
    Read T-N gram table.
 */
    public static void readLS(String path) {
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
            br.readLine();
            while ((line = br.readLine()) != null) {
                //line = br.readLine();
                if(line.length() > 0){
                    if(line.charAt(0) == '\\'){
                        count++;
                        if(count>=0){
                            Table[count] = new HashMap<>();
                        }                    }
                    else if(count >= 0) {
                        tokens = line.split(" ");
                        list = new ArrayList<>();
                        list.add(Double.parseDouble(tokens[0]));
                        list.add(Double.parseDouble(tokens[tokens.length-1]));
                        Table[count].put(line.substring(
                                tokens[0].length()+1,line.length()-tokens[tokens.length-1].length()), list);
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
    public static void readWB(String path) {
        BufferedReader br = null;
        String sCurrentLine;
        String line;
        String[] tokens;
        int n_gram = -2;
        String Start = "\\";
        ArrayList<Double> arr = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                tokens = line.split(" ");
                if (tokens[0].length()>0){
                    if (tokens[0].substring(0,1).equals(Start)){
                        n_gram ++;
                        if(n_gram>=0){
                            Table[n_gram] = new HashMap<>();
                        }
                    }else if(n_gram>=0){
                        arr.add(0, Double.parseDouble(tokens[0]));
                        if(n_gram>0) {
                            arr.add(1, Double.parseDouble(tokens[tokens.length-1]));
                        }
                        Table[n_gram].put(Parse.subTokens(tokens, 1 + n_gram, n_gram), arr);
                        arr = new ArrayList<>();
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

    public static String[] readData(String path) {
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
