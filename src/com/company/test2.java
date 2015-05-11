package com.company;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
        }else if(method.equals("mixed")){
            mixed();
        }//
    }

    private static void mixed() {
        String[]args_Lm_En = new String[4];
        String[]args_Lm_Es = new String[4];
        String[]args_Lm_Ca = new String[4];

        ArrayList<String> mixed_corpus = new ArrayList<>();
        ArrayList<String> mixed_key = new ArrayList<>();
        Integer[] key_val = new Integer[600];
        Integer[] prediced = new Integer[600];

        Double Perplexity_en, Perplexity_es, Perplexity_ca;
        int n_gram_en = 0; int n_gram_es = 0; int n_gram_ca = 0;

        HashMap<String,ArrayList<Double>>[] Table_en = new HashMap[n_gram_en];
        HashMap<String,ArrayList<Double>>[] Table_es = new HashMap[n_gram_es];
        HashMap<String,ArrayList<Double>>[] Table_ca = new HashMap[n_gram_ca];

        int sum = 0;
        double suc = 0;

        args_Lm_En[0] = "-n";
        args_Lm_En[1] = ""+n_gram_en;
        args_Lm_En[2] = "-i";
        args_Lm_En[3] = "src/en_text.corp";
        args_Lm_En[4] = "-o";
        args_Lm_En[5] = "src/en.model";

        args_Lm_Es[0] = "-n";
        args_Lm_Es[1] = ""+n_gram_es;
        args_Lm_Es[2] = "-i";
        args_Lm_Es[3] = "src/es_text.corp";
        args_Lm_Es[4] = "-o";
        args_Lm_Es[5] = "src/es.model";

        args_Lm_Ca[0] = "-n";
        args_Lm_Ca[1] = ""+n_gram_ca;
        args_Lm_Ca[2] = "-i";
        args_Lm_Ca[3] = "src/ca_text.corp";
        args_Lm_Ca[4] = "-o";
        args_Lm_Ca[5] = "src/ca.model";

        mixed_corpus = Parse.readCorpus("src/mixed.test");
        mixed_key = Parse.readCorpus("mixed.key");
        int i = 0;
        for (String str:mixed_key){
            key_val[i] = (Integer.parseInt(str));
            i++;
        }

        i = 0;
        for (String line:mixed_corpus){
            Perplexity_en = eval.evalLS(Table_en,line, n_gram_en);
            Perplexity_es = eval.evalLS(Table_es,line, n_gram_es);
            Perplexity_ca = eval.evalLS(Table_ca,line, n_gram_ca);
            if(Perplexity_en<Perplexity_es && Perplexity_en<Perplexity_ca){
                prediced[i] = 0;
            }else if(Perplexity_es<Perplexity_ca){
                prediced[i] =1;
            }else{
                prediced[i] = 2;
            }
            i++;
        }

        for (i=0;i<prediced.length;i++){
            if (prediced[i]==key_val[i]){
                sum += 1;
            }
        }
        suc = sum/600;

        System.out.println("the suc ratio is - " + suc);
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
        for (int i = 2; i < 5; i++) {
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
        double N = eval.readNumberOfWords(test_corpus, n_gram);
        ArrayList<ArrayList<Double>> lambda = Combination.CreateLmbdaList(n_gram);
        for (ArrayList<Double> temp_lambda : lambda) {
            for (String line : test_corpus) {
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
