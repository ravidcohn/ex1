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
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            fileWritter = new FileWriter(outPath, true);
            bufferWritter = new BufferedWriter(fileWritter);

            if(method.equals("wb")) {
                WittenBell(nGramTable,TN_Table);
            }
            else{
                LidstonesLaw(nGramTable,lmbda);
            }
            bufferWritter.close();
            fileWritter.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }




    private void LidstonesLaw(HashMap<String,Integer>[] nGramTable, double lmbda){
        double[] N = countN(nGramTable);
        String str = "";
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        for (int i = 0; i < nGramTable.length; i++) {
            saveLine("\\" + (i + 1) + "-gram:\n");
            double lmbdaBN = lmbda * Math.pow(nGramTable[i].keySet().size(), (i + 1)) + N[i];
            for (String wi : nGramTable[i].keySet()) {
                double pr = (double) nGramTable[i].get(wi) + lmbda;
                pr /= lmbdaBN ;
                str = df.format(Math.log10(pr)) + " " + wi+"\n";
                saveLine(str);
            }
            saveLine("\n");
        }
    }

    private void WittenBell(HashMap<String,Integer>[] nGramTable,HashMap<String,ArrayList<Integer>>[] TN_Table){
        double[] N = countN(nGramTable);
        String str = "";
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        saveLine("\\1-gram:\n");
        for (String wi : nGramTable[0].keySet()) {
            double pr = (double) nGramTable[0].get(wi)/(N[0]);
            str = df.format(Math.log10(pr)) + " " + wi+"\n";
            saveLine(str);
        }
        saveLine("\n");


        for (int i = 1; i < nGramTable.length; i++) {
            saveLine("\\" + (i + 1) + "-gram:\n");
            for (String wi : nGramTable[i].keySet()) {
                double pr = (double) nGramTable[i].get(wi);
                int end = wi.length() - 2;
                while(str.charAt(end)!=' '){
                    end--;
                }
                String subStr = str.substring(0,(end+1));
                pr /=(double)(TN_Table[i-1].get(subStr).get(0)+TN_Table[i-1].get(subStr).get(1));
                str = df.format(Math.log10(pr)) + " " + wi+"\n";
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
