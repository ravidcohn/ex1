package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class Pr_Methods {
    private ArrayList<String>[] lmData;
    private String outPath ="";
    private FileWriter fileWritter;
    BufferedWriter bufferWritter;

    public Pr_Methods(HashMap<String,Integer>[] nGramTable,String method,
                      double lmbda,String outPath,HashMap<String,ArrayList<Integer>>[] TN_Table){
        this.outPath = outPath;
        File file = new File(outPath);
        int n_gram = nGramTable.length;
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            fileWritter = new FileWriter(outPath, false);
            bufferWritter = new BufferedWriter(fileWritter);

            if(method.equals("wb")) {
                saveLine("wb "+n_gram+"\n");
                bufferWritter.close();
                fileWritter.close();
                fileWritter = new FileWriter(outPath, true);
                bufferWritter = new BufferedWriter(fileWritter);
                WittenBell(nGramTable,TN_Table);
            }
            else{
                saveLine("ls "+n_gram+" "+lmbda+"\n");
                bufferWritter.close();
                fileWritter.close();
                fileWritter = new FileWriter(outPath, true);
                bufferWritter = new BufferedWriter(fileWritter);
                LidstonesLaw(nGramTable,lmbda);
            }
            bufferWritter.close();
            fileWritter.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }



/*
    private void LidstonesLaw(HashMap<String,Integer>[] nGramTable, double lmbda){
        double[] N = countN(nGramTable);
        String str = "";
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        double v = nGramTable[0].keySet().size();
        double pr = 0;
        for (int i = 0; i < nGramTable.length; i++) {
            saveLine("\\" + (i + 1) + "-gram:\n");
            double lmbdaBN = lmbda * Math.pow(v,(i+1)) +N[i];
            pr = lmbda / lmbdaBN + 0.00000000000001;
            if(i > 0) {
                saveLine(df.format(Math.log10(pr)) + " <unseen>:" + "\n");
            }
            for (String wi : nGramTable[i].keySet()) {
                pr = (double) nGramTable[i].get(wi) + lmbda;
                pr /= lmbdaBN ;
                str = df.format(Math.log10(pr)) + " " + wi+"\n";
                saveLine(str);
            }
            saveLine("\n");
        }
    }
   */
private void LidstonesLaw(HashMap<String,Integer>[] nGramTable, double lmbda){
    double[] N = countN(nGramTable);
    String str = "";
    double n = 0;
    DecimalFormat df = new DecimalFormat("#.####");
    df.setRoundingMode(RoundingMode.CEILING);
    double v = nGramTable[0].keySet().size();
    double unseen = 1.0 / v;
    double pr = 0;
    String startStr = "";
    String[] tokens;
    saveLine("\\" + (1) + "-gram:\n");

    for (String wi : nGramTable[0].keySet()) {
        pr = (double) nGramTable[0].get(wi);
        pr /= v;
        str = df.format(Math.log10(pr)) + " " + wi+" 1.0"+"\n";
        if(wi.equals("<UNK>")){
            str = df.format(Math.log10(pr)) + " " + wi+" "+df.format(Math.log10(unseen))+"\n";
        }
        saveLine(str);
    }
    saveLine("\n");
    double pr2 = 0;
    for (int i = 1; i < nGramTable.length; i++) {
       // double lmbdaBN = Math.pow(v,i+1)*lmbda +N[i];
        //unseen = lmbda / lmbdaBN + 0.000000000000000000000000001;

        saveLine("\\" + (i + 1) + "-gram:\n");
   //     saveLine(df.format(Math.log10(pr)) + " <unseen>:" + "\n");
        for (String wi : nGramTable[i].keySet()) {
            tokens = wi.split(" ");
            startStr = Parse.subTokens(tokens, tokens.length - 2, i - 1);
            n = nGramTable[i-1].get(startStr);
            pr = (double) nGramTable[i].get(wi) + lmbda;
            pr /= n + (v * lmbda);
            pr2 = lmbda/((double) nGramTable[i].get(wi) + v*lmbda);
            if(pr2 == 0){
                pr2+=0.0000000000000000000000000000001;
            }
            str = df.format(Math.log10(pr)) + " " + wi+df.format(Math.log10(pr2))+"\n";
            saveLine(str);
        }
        saveLine("\n");
    }
}

    private void WittenBell(HashMap<String,Integer>[] nGramTable,HashMap<String,ArrayList<Integer>>[] TN_Table){
        double[] N = countN(nGramTable);
        String str = "";
        String tokens[];
        String subStr;
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        double Z_i1 = 0;
        double pr2 = 0;
        for (int i = 0; i < nGramTable.length; i++) {
            saveLine("\\" + (i + 1) + "-gram:\n");
            for (String wi : nGramTable[i].keySet()) {
                double pr = (double) nGramTable[i].get(wi);
                if(i == 0){
                    pr = pr/(N[i]);
                    pr2 = TN_Table[i].get(wi).get(0)/(Z_i1*(TN_Table[i].get(wi).get(0)+TN_Table[i].get(wi).get(1)));
                    str = df.format(Math.log10(pr)) + " " + wi + " "+df.format(Math.log10(pr2)) + "\n";
                }
                else if (i < nGramTable.length - 1) {
                    //tokens = wi.split(" ");
                    //int end = (wi.length()-(tokens[tokens.length-1].length())-1);
                    //subStr = wi.substring(0, end);
                    pr /= (TN_Table[i].get(wi).get(0)+TN_Table[i].get(wi).get(1));
                    Z_i1 = N[i] - TN_Table[i].get(wi).get(0);

                    pr2 = TN_Table[i].get(wi).get(0)/(Z_i1*(TN_Table[i].get(wi).get(0)+TN_Table[i].get(wi).get(1)));
                    str = df.format(Math.log10(pr)) + " " + wi + " "+df.format(Math.log10(pr2)) + "\n";
                }else{
                    pr /= (TN_Table[i].get(wi).get(0)+TN_Table[i].get(wi).get(1));
                    str = df.format(Math.log10(pr)) + " " + wi + "\n";
                }
                saveLine(str);
            }
            saveLine("\n");
        }
    }
    private double[] countN(HashMap<String,Integer>[] nGramTable){
        double[] N = new double[nGramTable.length];
        saveLine("\\data\\\n");
        for (int i = 0;i <  nGramTable.length;i++) {
            for (double n : nGramTable[i].values()) {
                N[i] += n;
            }
            saveLine("ngram " + (i + 1) + "=" + (int) N[i] + "\n");
        }
        saveLine("\n");
        return N;
    }
    private void saveLine(String line){
        try{
            bufferWritter.write(line);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
