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
        int n_gram = 5;
        String method;
        String[] data;
        HashMap<String,ArrayList<Integer>>[] TN_Table = new HashMap[n_gram];
        HashMap<String,Integer>[] nGramTable = new HashMap[n_gram];
        ArrayList<String> corpus = Parse.readCorpus(args[0]);
        data = readData(args[1]);
        method = data[0];
        N = readNumberOfWords(corpus);
        if (method=="wb"){
            nGramTable = readNGT(args[1]);
        }else{
            TN_Table = readTNT(args[1]);
        }
        for (String line: corpus){
            if (method=="wb") {
                Perplexity += evalLS(line, nGramTable);
            }else {
                Perplexity += evalWB(line, TN_Table);
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

    private static double evalLS(String line, HashMap<String,Integer>[] nGramTable){
        //TODO write the function.
        return 0;
    }

    private static double evalWB(String line, HashMap<String,ArrayList<Integer>>[] TN_Table){
        //TODO write the function.
        return 0;
    }
/*
    Read T-N gram table.
 */
    private static HashMap<String,ArrayList<Integer>>[] readTNT(String path) {
        //TODO write the function.
        return null;
    }
/*
    read n gram table.
 */
    private static HashMap<String,Integer>[] readNGT(String path) {
        BufferedReader br = null;
        String sCurrentLine;
        String line;
        try {
            br = new BufferedReader(new FileReader(path));
            while ((sCurrentLine = br.readLine()) != null) {

            }
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
