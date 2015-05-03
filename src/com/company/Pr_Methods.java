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

    public Pr_Methods(HashMap<String,Integer>[] nGramTable,String method,double lmbda,String outPath){
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
                WittenBell(nGramTable);
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




    public void LidstonesLaw(HashMap<String,Integer>[] nGramTable, double lmbda){
        double[] N = new double[nGramTable.length];
        saveLM("\\data\\\n");
        for (int i = 0;i <  nGramTable.length;i++) {
            for (double n : nGramTable[i].values()) {
                N[i] += n;
            }
            saveLM("ngram "+(i+1)+"="+(int)N[i]+"\n");
        }

        saveLM("\n");
        String str = "";
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        for (int i = 0; i < nGramTable.length; i++) {
            saveLM("\\"+(i+1)+"-gram:\n");
            double lmbdaBN = lmbda * Math.pow(nGramTable[i].keySet().size(), (i + 1)) + N[i];
            for (String wi : nGramTable[i].keySet()) {
                double pr = (double) nGramTable[i].get(wi) + lmbda;
                pr /= lmbdaBN ;
                str = df.format(Math.log10(pr)) + " " + wi+"\n";
                saveLM(str);
            }
            saveLM("\n");
        }
    }
    //TODO: write the function
    public void WittenBell(HashMap<String,Integer>[] nGramTable){

    }

    public void saveLM(String line){
        try{
            bufferWritter.write(line);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
