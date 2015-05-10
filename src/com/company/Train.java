package com.company;

import com.sun.org.apache.xerces.internal.xs.StringList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Train {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        String path = "src/en_text.corp";
      //  String path = "src/very_small_input";
        ArrayList<String> corpus = Parse.readCorpus(path);
        String out = "src/out_test.txt";
        String method = "wb";
        int n_gram = 5;
        double lmbda = 0.5;
        HashMap<String,ArrayList<Integer>>[] TN_Table =  new HashMap[n_gram-1];
        HashMap<String,Integer>[] nGramTable = Parse.createNGramTable(corpus, n_gram, method, TN_Table);
        nGramTable = Parse.addUnknown(nGramTable);
        Pr_Methods pr = new Pr_Methods(nGramTable,method,lmbda,out,TN_Table);
        time = System.currentTimeMillis() - time;
        double sec = ((double)time)/1000;
        System.out.println("Run time: " + sec + " sec");
    }



}
