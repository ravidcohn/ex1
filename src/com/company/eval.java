package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pinhas on 11/05/2015.
 */
public class eval {

    public static void main(String[] args) {
        if(args.length != 4){
            System.out.println("This program can only run in the following way:");
            System.out.println("eval -i <input file> -m <model file>");
        }
        else{
            String inPath = args[1];
            String modelPath = args[3];
            double Perplexity = 0;

            int n_gram;
            String method;
            String[] data = readData(modelPath);
            method = data[0];
            n_gram = Integer.parseInt(data[1]);
            HashMap<String,ArrayList<Double>>[] Table = new HashMap[n_gram];
            ArrayList<String> corpus = Parse.readCorpus(inPath);
            double N = readNumberOfWords(corpus, n_gram);
            ArrayList<Double>[] lambda = computeLmabdaWB();
            if (method.equals("wb")){
                readWB(Table, modelPath);
            }else if(method.equals("ls")){
                readLS(Table, modelPath);
            }
            if (method.equals("wb")) {
                for (String line: corpus) {
                    Perplexity += evalWB(Table, line, lambda[n_gram]);
                }
            }else if(method.equals("ls")){
                for (String line: corpus) {
                    Perplexity += evalLS(Table, line, n_gram);
                }

            }
            Perplexity = Math.pow(10,Perplexity/N);
            Perplexity = 1/Perplexity;
            System.out.println("Perplexity: " + Perplexity);
        }
    }

    public static ArrayList<Double>[] computeLmabdaWB() {
        ArrayList<Double>[] lambda = new ArrayList[5];
        for (int i = 0; i < lambda.length; i++) {
            lambda[i] = new ArrayList<>();
        }


        lambda[1].add(0, 0.5);
        lambda[1].add(1, 0.5);

        lambda[2].add(0, 0.5);
        lambda[2].add(1, 0.5);
        lambda[2].add(2, 0.5);

        lambda[3].add(0, 0.5);
        lambda[3].add(1, 0.5);
        lambda[3].add(2, 0.5);
        lambda[3].add(3, 0.5);

        lambda[4].add(0, 0.5);
        lambda[4].add(1, 0.5);
        lambda[4].add(2, 0.5);
        lambda[4].add(3, 0.5);
        lambda[4].add(4, 0.5);

        return lambda;
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

    public static double evalLS(HashMap<String,ArrayList<Double>>[] Table,String line,int n_gram){
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
                    double shit = Table[n_gram - 2].get(sub).get(1);
                }
            }
            else{
                pr += Table[n_gram-1].get(tLine).get(0);
            }
        }
        return pr;
    }

    public static double evalWB(HashMap<String,ArrayList<Double>>[] Table,String line, ArrayList<Double> lambda){
        //TODO write the function.
        int n_gram = lambda.size();
        String[] tokens;
        String[][] subStr = new String[n_gram][2];
        double PP = 0;
        double PP_temp = 0;
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
                    PP_temp += lambda.get(j) * Math.pow(10,Table[j].get(subStr[j][0]).get(0));
                }else if(j==0) {
                    PP_temp += lambda.get(j) * Math.pow(10,Table[j].get("<UNK> ").get(0));
                }else  if(Table[j-1].get(subStr[j][1]) != null) {
                    PP_temp += lambda.get(j) * Math.pow(10, Table[j-1].get(subStr[j][1]).get(1));
                }
            }
            PP_temp = Math.log10(PP_temp);
            PP += PP_temp;
            PP_temp = 0;

        }

        if (PP==0) {
            return 0;
        }else {
            return PP;
        }
    }
    /*
        Read T-N gram table.
     */
    public static void readLS(HashMap<String,ArrayList<Double>>[] Table,String path) {
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
    public static void readWB(HashMap<String,ArrayList<Double>>[] Table,String path) {
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
                        arr.add(1, Double.parseDouble(tokens[tokens.length-1]));
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
