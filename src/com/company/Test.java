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

        //ArrayList<ArrayList<Double>> allLmbda =  Combination.CreateLmbdaList(2);


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
        if (method=="wb"){
            readWB(args[1]);
        }else if(method=="ls"){
            readLS(args[1]);
        }
        lambda = Combination.CreateLmbdaList(n_gram);
        if (method=="wb") {
            for (ArrayList<Double> temp_lambda:lambda) {
                for (String line: corpus) {
                    Perplexity += evalWB(line, temp_lambda);
                }
                Perplexity = 1/Math.pow(Perplexity, -N);
                if (Perplexity<Best_Perplexity){
                    Best_Perplexity = Perplexity;
                    best_lambda = temp_lambda;
                }
                System.out.println(Perplexity);
            }
            }else if(method=="ls"){
                for (String line: corpus) {
                    Perplexity += evalLS(line, n_gram);
                 }
            Perplexity = 1/Math.pow(Perplexity, -N);
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

    private static String print_lambda(ArrayList<Double> best_lambda) {
        String str = "";
        for (double lamda:best_lambda){
            str  = lamda+ " ";
        }//j
        return str;
    }

    private static ArrayList<ArrayList<Double>> interpolation(int n_gram) {
        return new ArrayList<>();
    }

    private static double readNumberOfWords(ArrayList<String> corpus,int n_gram) {
        double N = 0;
        String[] tokens;
        for (String line: corpus){
            tokens = line.split(" ");
                N+= tokens.length - n_gram + 1;
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
        String[][] subStr = new String[n_gram][2];
        double PP = 0;
        tokens = line.split(" ");
        for (int i=n_gram;i<tokens.length;i++){
            for(int j=0;j<n_gram;j++){
                subStr[j][0] = Parse.subTokens(tokens, i, j);
                if (j>0) {
                    subStr[j][1] = Parse.subTokens(tokens, i - 1, j - 1);
                }
            }
            for(int j=0;j<n_gram;j++) {
                if (Table[j].get(subStr[j][0]) != null) {
                    PP += lambda.get(j) * Table[j].get(subStr[j][0]).get(0);
                } else if (Table[j].get(subStr[j][1]) != null) {
                    PP += lambda.get(j) * Table[j].get(subStr[j][1]).get(1);
                }
            }

        }


        return PP;
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
