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
    private static HashMap<String,ArrayList<Double>>[] Table;

    public static void main(String[] args){
        ArrayList<Point2D.Double[]>allVals = new ArrayList<>();
        for (int i = 2; i < 5; i++) {
            Point2D.Double[] vals = lmbdaTestForLidstone("src/en_text.corp","src/en.test",i);
            allVals.add(vals);
            System.out.println("n-gram: " + i);
            for (int j = 0; j < vals.length; j++) {
                System.out.println("lmbda: "+vals[j].getX()+"   ,Perplexity: "+vals[j].getY());
            }
        }

    }

/*    public static void main(String[] args){
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
*/
    private static String print_lambda(ArrayList<Double> best_lambda) {
        String str = "";
        for (double lamda:best_lambda){
            str  += lamda+ " ";
        }
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
                tLine += tokes[i+j] + " ";
            }
            if(Table[n_gram-1].get(tLine) == null || Table[n_gram-1].get(tLine).get(0) == null){
                if(i > 0) {
                    pr = Table[n_gram - 1].get("<unseen>:").get(0);
                }
                else{
                    pr = Table[0].get("<UNK>").get(0);
                }
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
                    PP += lambda.get(j) * Math.pow(10,Table[j].get("<UNK>").get(0));
                }else if(j==1 && Table[j-1].get(subStr[j][1]) != null){
                    PP += lambda.get(j) * Math.pow(10, Table[j-1].get(subStr[j][1]).get(0));
                }else if(j==1){
                    PP += lambda.get(j) * Math.pow(10,Table[j-1].get("<UNK>").get(0));
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

    public static Point2D.Double[] lmbdaTestForLidstone(String lm_inPath,String test_inPath,int n_gram){

        double start = 0;
        double end = 1;
        double jump = 0.1;
        int size =(int)((end - start)/jump) + 1;
        Point2D.Double[] lmbdaXY = new Point2D.Double[size];


        ArrayList<String> corpus = Parse.readCorpus(lm_inPath);
        String out = "src/lidstonLmbdaTest_";
        String method = "ls";

        HashMap<String,ArrayList<Integer>>[] TN_Table =  new HashMap[n_gram-1];
        HashMap<String,Integer>[] nGramTable = Parse.createNGramTable(corpus, n_gram, method, TN_Table);
        nGramTable = Parse.addUnknown(nGramTable);
        Pr_Methods pr = null;
        ArrayList<String> test_corpus = Parse.readCorpus(test_inPath);

        double[] lmbda = new double[]{0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1};
        for (int count =0; count < lmbda.length; count++) {
            Table = new HashMap[n_gram];
            double Perplexity = 0;
            String outP = out+lmbda[count]+".txt";
            pr = new Pr_Methods(nGramTable,method,lmbda[count],outP,TN_Table);
            readLS(outP);
            for (String line: test_corpus) {
                Perplexity += evalLS(line, n_gram);
            }
            lmbdaXY[count] = new Point2D.Double(lmbda[count],Perplexity);
        }

        return lmbdaXY;
    }


}
