package com.company;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pinhas on 11/05/2015.
 */
public class lm {
    public static void main(String [] args){
        int n_gram = 0;
        String inPath = "";
        String outPath = "";
        String model = "";
        double lmbda = 1;
        for (int i = 0; i < args.length; i+=2) {
            if (args[i].equals("-n")){
                n_gram = Integer.parseInt(args[i+1]);
            }else if(args[i].equals("-i")){
                inPath = args[i+1];
            }
            else if(args[i].equals("-o")){
                outPath = args[i+1];
            }
            else if(args[i].equals("-s")){
                model = args[i+1];
            }
            else if(args[i].equals("-lmbd")){
                lmbda = Double.parseDouble(args[i+1]);
            }
        }
        if(n_gram < 1 || n_gram > 5 || inPath.length() == 0
                || outPath.length() == 0|| model.length() == 0 ||(!model.equals("wb")&&!model.equals("ls")) ||lmbda < 0|| lmbda > 1){
            System.out.println("The program can only run in the following way:");
            System.out.println("lm -n <1-5 gram> -i <input corpus file> -o <output model file>  <options>");
            System.out.println("<options> =  [-s <wb,ls> , -lmbd <lmbda>]");
            System.out.println("0 <= lmbda <= 1");
        }
        else{
            ArrayList<String> corpus = Parse.readCorpus(inPath);
            HashMap<String,ArrayList<Integer>>[] TN_Table =  new HashMap[n_gram-1];
            HashMap<String,Integer>[] nGramTable = Parse.createNGramTable(corpus, n_gram, model, TN_Table);
            nGramTable = Parse.addUnknown(nGramTable);
            Pr_Methods pr = new Pr_Methods(nGramTable,model,lmbda,outPath,TN_Table);
        }
    }
}
