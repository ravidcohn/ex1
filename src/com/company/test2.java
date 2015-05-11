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

public static void testWB() {
    ArrayList<Double>[] best_lambda = new ArrayList[4];
    ArrayList<ArrayList<Double>> lambda;
    double Perplexity = 0;
    /*double N = Test.readNumberOfWords(corpus, n_gram);
    for (int i = 2; i < 5; i++) {
//          Point2D.Double[] vals = lmbdaTestForWB("src/en_text.corp","src/en.test",i);
        //   Point2D.Double[] vals = lmbdaTestForWB("src/trainLs","src/testLs",i);
        Point2D.Double[] vals = lmbdaTestForWB("src/ca_text.corp", "src/ca.test", i);
//            Point2D.Double[] vals = lmbdaTestForWB("src/es_text.corp","src/es.test",i);
        //          allVals.add(vals);
        System.out.println("n-gram: " + i);
        GraphPanel.plot(vals);
        for (int j = 0; j < vals.length; j++) {
            System.out.println("lmbda: " + vals[j].getX() + "   ,Perplexity: " + vals[j].getY());
        }
        lambda = Combination.CreateLmbdaList(i);
        for (ArrayList<Double> temp_lambda : lambda) {
            for (String line : corpus) {
                Perplexity += Test.evalWB(line, temp_lambda);
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
        System.out.println("Perplexity: " + Perplexity);
    }*/
}
    private static Point2D.Double[] lmbdaTestForWB(String s, String s1, int i) {
        return new Point2D.Double[0];
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

        double N = Test.readNumberOfWords(test_corpus, n_gram);
        double[] lmbda = new double[]{0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1};
        for (int count =0; count < lmbda.length; count++) {
            Table = new HashMap[n_gram];
            double Perplexity = 0;
            String outP = out+lmbda[count]+".txt";
            pr = new Pr_Methods(nGramTable,method,lmbda[count],outP,TN_Table);
            Test.readLS(Table,outP);
            for (String line: test_corpus) {
                Perplexity += Test.evalLS(Table,line, n_gram);
            }
            Perplexity = Math.pow(10,Perplexity/N);
            Perplexity = 1/Perplexity;
            lmbdaXY[count] = new Point2D.Double(lmbda[count],Perplexity);
        }

        return lmbdaXY;
    }


}
