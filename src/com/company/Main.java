package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        String path = "src/small_input_test.crop";
        String out = "out_test.txt";
        ArrayList<String> corpus = readCorpus(path);
        HashMap<String,Integer>[] nGramTable = createNGramTable(corpus, 5);
        Pr_Methods pr = new Pr_Methods(nGramTable,"lw",1,out);
        time = System.currentTimeMillis() - time;
        System.out.println("Run time: "+time);
    }

    public static ArrayList<String> readCorpus(String fileName){
        ArrayList<String> lines = new ArrayList<>();

        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(fileName));

            while ((sCurrentLine = br.readLine()) != null) {
                //TODO split "sCurrentLine" into sentences
                lines.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        return lines;
    }

    public static HashMap<String,Integer> countAllWords(ArrayList<String> lines, int n_gram){
        HashMap<String,Integer> wordCount = new HashMap<>();
        int j_border_end = n_gram-1;

        for (String line: lines) {
            String [] tokes = line.split(" ");
            int j_end = tokes.length - j_border_end;
            for (int j = 0; j < j_end; j++) {
                String str = "";
                for (int k = 0; k < n_gram; k++) {
                    str = str + tokes[j+k]+" ";
                }
                if(str.length() > 0){
                    if(wordCount.get(str) == null){
                        wordCount.put(str,0);
                    }
                    wordCount.put(str,wordCount.get(str)+1);
                }
            }
        }

        return wordCount;
    }
    public static HashMap<String,Integer>[] createNGramTable(ArrayList<String> lines, int n_gram){
        @SuppressWarnings("unchecked")
        HashMap<String,Integer>[] nGramTable = new HashMap[n_gram];
        for (int i = 0; i < n_gram; i++) {
            nGramTable[i] = countAllWords(lines,i+1);
        }
        return nGramTable;
    }
}
