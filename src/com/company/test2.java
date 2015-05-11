package com.company;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;

/**
 * Created by Ravid on 09/05/2015.
 */
public class test2 {

    public static void main(String[] args){
//        ArrayList<Point2D.Double[]>allVals = new ArrayList<>();
        String method = "wb";//wb\ls.
        if (method.equals("ls")) {
            testLS();
        }else if (method.equals("wb")){
            testWB();
        }
    }

    public static void testLS(){
        for (int i = 2; i < 5; i++) {
            //          Point2D.Double[] vals = lmbdaTestForLidstone("src/en_text.corp","src/en.test",i);
            //   Point2D.Double[] vals = lmbdaTestForLidstone("src/trainLs","src/testLs",i);
            Point2D.Double[] vals = lmbdaTestForLidstone("src/ca_text.corp", "src/ca.test", i);
            //            Point2D.Double[] vals = lmbdaTestForLidstone("src/es_text.corp","src/es.test",i);
            //          allVals.add(vals);
            System.out.println("n-gram: " + i);
            GraphPanel.plot(vals);
            for (int j = 0; j < vals.length; j++) {
                System.out.println("lmbda: " + vals[j].getX() + "   ,Perplexity: " + vals[j].getY());
            }
        }
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
        HashMap<String,ArrayList<Double>>[] Table = new HashMap[n_gram];
        HashMap<String,ArrayList<Integer>>[] TN_Table =  new HashMap[n_gram-1];
        HashMap<String,Integer>[] nGramTable = Parse.createNGramTable(corpus, n_gram, method, TN_Table);
        nGramTable = Parse.addUnknown(nGramTable);
        Pr_Methods pr = null;
        ArrayList<String> test_corpus = Parse.readCorpus(test_inPath);

        double N = eval.readNumberOfWords(test_corpus, n_gram);
        double[] lmbda = new double[]{0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1};
        for (int count =0; count < lmbda.length; count++) {
            Table = new HashMap[n_gram];
            double Perplexity = 0;
            String outP = out+lmbda[count]+".txt";
            pr = new Pr_Methods(nGramTable,method,lmbda[count],outP,TN_Table);
            eval.readLS(Table,outP);
            for (String line: test_corpus) {
                Perplexity += eval.evalLS(Table,line, n_gram);
            }
            Perplexity = Math.pow(10,Perplexity/N);
            Perplexity = 1/Perplexity;
            lmbdaXY[count] = new Point2D.Double(lmbda[count],Perplexity);
        }

        return lmbdaXY;
    }

    public static void testWB() {
        ArrayList<Double>[] best_lambda = new ArrayList[4];
        for (int i = 3; i < 5; i++) {
            System.out.println("n-gram: " + i);
//           best_lambda[i-1]  = lmbdaTestForWB("src/en_text.corp","src/en.test",i);
                best_lambda[i-1]  = lmbdaTestForWB("src/trainLs","src/testLs",i);
//            best_lambda[i-1] = lmbdaTestForWB("src/ca_text.corp", "src/ca.test", i);
//             best_lambda[i-1]  = lmbdaTestForWB("src/es_text.corp","src/es.test",i);

        }
    }

    private static  ArrayList<Double> lmbdaTestForWB(String lm_inPath,String test_inPath,int n_gram) {
        HashMap<String,ArrayList<Double>>[] Table = new HashMap[n_gram];
        ArrayList<String> corpus = Parse.readCorpus(lm_inPath);
        String out = "src/WBLmbdaTest" + n_gram + ".txt";
        String method = "wb";
        ArrayList<Double> best_lambda = new ArrayList<>();

        HashMap<String,ArrayList<Integer>>[] TN_Table =  new HashMap[n_gram];
        HashMap<String,Integer>[] nGramTable = Parse.createNGramTable(corpus, n_gram, method, TN_Table);
        nGramTable = Parse.addUnknown(nGramTable);
        Pr_Methods pr = new Pr_Methods(nGramTable,method,0.5,out,TN_Table);
        ArrayList<String> test_corpus = Parse.readCorpus(test_inPath);
        eval.readWB(Table, out);

        double Perplexity = 0;
        double Best_Perplexity = Double.POSITIVE_INFINITY;
        double N = eval.readNumberOfWords(corpus, n_gram);
        ArrayList<ArrayList<Double>> lambda = Combination.CreateLmbdaList(n_gram);
        for (ArrayList<Double> temp_lambda : lambda) {
            for (String line : corpus) {
                Perplexity += eval.evalWB(Table, line, temp_lambda);
            }
            Perplexity = Math.pow(10, Perplexity / N);
            Perplexity = 1 / Perplexity;
            if (Perplexity < Best_Perplexity) {
                Best_Perplexity = Perplexity;
                best_lambda = temp_lambda;
            }
            Perplexity = 0;
        }

        System.out.println("Best_Perplexity: " + Best_Perplexity);
        System.out.println("best_lambda: " + print_lambda(best_lambda));

        return best_lambda;
    }

    public static String print_lambda(ArrayList<Double> best_lambda) {
        String str = "";
        for (double lamda:best_lambda){
            str  += lamda+ " ";
        }
        return str;
    }


}
