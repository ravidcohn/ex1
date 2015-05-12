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
        String method = "ls";//wb\ls.
        if (method.equals("ls")) {
            testLS();
        }else if (method.equals("wb")){
            testWB();
        }else if(method.equals("mixed")){
            mixed();
        }
    }

    private static void mixed() {
        String[]args_Lm_En = new String[6];
        String[]args_Lm_Es = new String[6];
        String[]args_Lm_Ca = new String[6];

        ArrayList<String> mixed_corpus = new ArrayList<>();
        ArrayList<String> mixed_key = new ArrayList<>();

        Double Perplexity_en, Perplexity_es, Perplexity_ca;
        int n_gram_en = 4; int n_gram_es = 4; int n_gram_ca = 4;

        HashMap<String,ArrayList<Double>>[] Table_en = new HashMap[n_gram_en];
        HashMap<String,ArrayList<Double>>[] Table_es = new HashMap[n_gram_es];
        HashMap<String,ArrayList<Double>>[] Table_ca = new HashMap[n_gram_ca];



        int sum = 0;
        double suc = 0;
        double en_lmbda = 0.01;
        String en_method = "wb";
        double es_lmbda = 0.01;
        String es_method = "wb";
        double ca_lmbda = 0.01;
        String ca_method = "wb";

        args_Lm_En[0] = ""+n_gram_en;
        args_Lm_En[1] = "src/en_text.corp";
        args_Lm_En[2] = "src/en.model";
        args_Lm_En[3] = ca_method;
        args_Lm_En[4] = ""+ca_lmbda;


        args_Lm_Es[0] = ""+n_gram_es;
        args_Lm_Es[1] = "src/es_text.corp";
        args_Lm_Es[2] = "src/es.model";
        args_Lm_Es[3] = ca_method;
        args_Lm_Es[4] = ""+ca_lmbda;

        args_Lm_Ca[0] = ""+n_gram_ca;
        args_Lm_Ca[1] = "src/ca_text.corp";
        args_Lm_Ca[2] = "src/ca.model";
        args_Lm_Ca[3] = ca_method;
        args_Lm_Ca[4] = ""+ca_lmbda;





        ArrayList<String> en_corpus = Parse.readCorpus(args_Lm_En[1]);
        HashMap<String,ArrayList<Integer>>[] en_TN_Table =  new HashMap[n_gram_en-1];
        HashMap<String,Integer>[] en_nGramTable = Parse.createNGramTable(en_corpus, n_gram_en, en_method, en_TN_Table);
        en_nGramTable = Parse.addUnknown(en_nGramTable);
        Pr_Methods en_pr = new Pr_Methods(en_nGramTable,en_method,en_lmbda,args_Lm_En[2],en_TN_Table);

        ArrayList<String> es_corpus = Parse.readCorpus(args_Lm_Es[1]);
        HashMap<String,ArrayList<Integer>>[] es_TN_Table =  new HashMap[n_gram_es-1];
        HashMap<String,Integer>[] es_nGramTable = Parse.createNGramTable(es_corpus, n_gram_es, es_method, es_TN_Table);
        es_nGramTable = Parse.addUnknown(es_nGramTable);
        Pr_Methods es_pr = new Pr_Methods(es_nGramTable,es_method,es_lmbda,args_Lm_Es[2],es_TN_Table);

        ArrayList<String> ca_corpus = Parse.readCorpus(args_Lm_Ca[1]);
        HashMap<String,ArrayList<Integer>>[] ca_TN_Table =  new HashMap[n_gram_ca-1];
        HashMap<String,Integer>[] ca_nGramTable = Parse.createNGramTable(ca_corpus, n_gram_ca, ca_method, ca_TN_Table);
        ca_nGramTable = Parse.addUnknown(ca_nGramTable);
        Pr_Methods ca_pr = new Pr_Methods(ca_nGramTable,ca_method,ca_lmbda,args_Lm_Ca[2],ca_TN_Table);

        if (en_method.equals("wb")){
            eval.readWB(Table_en, args_Lm_En[2]);
        }else if(en_method.equals("ls")){
            eval.readLS(Table_en, args_Lm_En[2]);
        }

        if (es_method.equals("wb")){
            eval.readWB(Table_es, args_Lm_Es[2]);
        }else if(es_method.equals("ls")){
            eval.readLS(Table_es, args_Lm_Es[2]);
        }

        if (ca_method.equals("wb")){
            eval.readWB(Table_ca, args_Lm_Ca[2]);
        }else if(ca_method.equals("ls")){
            eval.readLS(Table_ca, args_Lm_Ca[2]);
        }
        ArrayList<Integer> srcLine = new ArrayList<>();
        mixed_corpus = Parse.readCorpus2("src/mixed.test",srcLine);
        mixed_key = Parse.readCorpus("src/mixed.key");

        int[] key_val = new int[mixed_corpus.size()];
        int[] prediced = new int[mixed_corpus.size()];


        int i = 0;
        for (String str:mixed_key){
            String[] tokes = str.split(" ");
            key_val[i++] = (Integer.parseInt(tokes[1]));
        }

        i = 0;
        int j =0;

        for (int jPrev =j;jPrev <mixed_corpus.size();jPrev = j){
            Perplexity_en = Perplexity_es = Perplexity_ca = 0.0;
            while(j < mixed_corpus.size()&&srcLine.get(j++) == srcLine.get(jPrev)) {
                String line = mixed_corpus.get(jPrev);
                Perplexity_en += eval.evalLS(Table_en, line, n_gram_en);
                Perplexity_es += eval.evalLS(Table_es, line, n_gram_es);
                Perplexity_ca += eval.evalLS(Table_ca, line, n_gram_ca);
            }
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
        suc = sum/(double)key_val.length;

        System.out.println("the suc ratio is - " + suc);
    }

    public static void testLS(){
        for (int i = 2; i < 5; i++) {
            //          Point2D.Double[] vals = lmbdaTestForLidstone("src/en_text.corp","src/en.test",i);
            //   Point2D.Double[] vals = lmbdaTestForLidstone("src/trainLs","src/testLs",i);
            //Point2D.Double[] vals = lmbdaTestForLidstone("src/ca_text.corp", "src/ca.test", i);
                        Point2D.Double[] vals = lmbdaTestForLidstone("src/es_text.corp","src/es.test",i);
            //          allVals.add(vals);
            System.out.println("n-gram: " + i);
    //        GraphPanel.plot(vals);
            for (int j = 0; j < vals.length; j++) {
            //    System.out.println("lmbda: " + vals[j].getX() + "   ,Perplexity: " + vals[j].getY());
                System.out.println(vals[j].getY());
            }
        }
    }

    public static Point2D.Double[] lmbdaTestForLidstone(String lm_inPath,String test_inPath,int n_gram){

        double start = 0.000000000001;
        double end = 0.00001;
        double jump = 0.000001;
        int size = 101;
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
        double[] lmbda = new double[size];
        for (int i=0;i<=100;i++){
            lmbda[i] = 0.01*i;
        }
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
//                best_lambda[i-1]  = lmbdaTestForWB("src/trainLs","src/testLs",i);
//            best_lambda[i-1] = lmbdaTestForWB("src/ca_text.corp", "src/ca.test", i);
             best_lambda[i-1]  = lmbdaTestForWB("src/es_text.corp","src/es.test",i);

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
