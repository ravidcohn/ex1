package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class Pr_Methods {
    private ArrayList<String>[] lmData;
    private String outPath ="";

    public Pr_Methods(HashMap<String,Integer>[] nGramTable,String method,double lmbda,String outPath){
        this.outPath = outPath;
        lmData = new ArrayList[nGramTable.length];
        if(method.equals("wb")) {
            WittenBell(nGramTable);
        }
        else{
            LidstonesLaw(nGramTable,lmbda);
        }
    }

    public void LidstonesLaw(HashMap<String,Integer>[] nGramTable, double lmbda){
        double[] N = new double[nGramTable.length];
        for (int i = 0;i <  nGramTable.length;i++) {
            for (double n : nGramTable[i].values()) {
                N[i] += n;
            }
        }

        for (int i = 0; i < nGramTable.length; i++) {
            for (String wi : nGramTable[i].keySet()) {
                double lmbdaB = lmbda*Math.pow(nGramTable[i].keySet().size(),(i+1));
                double pr = (double)nGramTable[i].get(wi) + lmbda;
                pr /= (lmbdaB + N[i]);
                pr = Math.log10(pr);
                lmData[i].add(pr + " " +wi);
            }
        }
    }

    public void WittenBell(HashMap<String,Integer>[] nGramTable){

    }

    public void saveLM(){
        try{
            File file = new File(outPath);
            //if file doesnt exists, then create it
            file.createNewFile();


            //true = append file
            FileWriter fileWritter = new FileWriter(outPath, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write("\\data\\");
            bufferWritter.newLine();
            //TODO: Complete writing to the file
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
