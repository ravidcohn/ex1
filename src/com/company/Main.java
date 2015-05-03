package com.company;

import com.sun.org.apache.xerces.internal.xs.StringList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
//        String path = "src/small_input_test.crop";
//        ArrayList<String> corpus = readCorpus(path);
        String out = "src/out_test.txt";
        ArrayList<String> corpus = readCorpus(args[0]);
        HashMap<String,Integer>[] nGramTable = createNGramTable(corpus, 1);
        Pr_Methods pr = new Pr_Methods(nGramTable,"lw",1,out);
        time = System.currentTimeMillis() - time;
        double sec = ((double)time)/1000;
        System.out.println("Run time: "+sec+" sec");
    }

    public static ArrayList<String> readCorpus(String fileName){
        ArrayList<String> lines = new ArrayList<>();
        StringTokenizer st;
        BufferedReader br = null;
        String START = "<s> ";
        String END = " </s>";
        String DELIMS = ";:";
        String line = null;
        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(fileName));

            while ((sCurrentLine = br.readLine()) != null) {
                st = new StringTokenizer(sCurrentLine,DELIMS);
                while(st.hasMoreElements()){
                    line = (String) st.nextElement();
                    line = line.replaceAll(",", "");
                    line = line.replaceAll(":", "");
                    line = line.replaceAll(";", "");
                    line = line.replaceAll("\\(", "");
                    line = line.replaceAll("\\)", "");
                    line = line.replaceAll("\\?", "");
                    line = line.replaceAll("\\.","");
                    line = line.replaceAll("\"", "");
                    line = line.replaceAll("\\s+", " ");
                    line = line.trim();
                    line = START + line + END;
                    lines.add(line);
                }
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
                    str = str.trim();
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
