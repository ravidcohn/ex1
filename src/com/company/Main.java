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
        String path = "src/small_input_test.crop";
        ArrayList<String> corpus = readCorpus(path);
        String out = "src/out_test.txt";
        String method = "lw2";
        int n_gram = 5;
        HashMap<String,ArrayList<Integer>>[] TN_Table =  new HashMap[n_gram-1];;
        HashMap<String,Integer>[] nGramTable = createNGramTable(corpus,n_gram,method,TN_Table);
        Pr_Methods pr = new Pr_Methods(nGramTable,method,1,out,TN_Table);
        time = System.currentTimeMillis() - time;
        double sec = ((double)time)/1000;
        System.out.println("Run time: "+sec+" sec");
    }
    /*
    Input: text file.
    Output: Separate the file into lines by DELIMS.
    Wrap each line with START & END symbol.
     */
    public static ArrayList<String> readCorpus(String fileName){
        ArrayList<String> lines = new ArrayList<>();
        StringTokenizer st;
        BufferedReader br = null;
        String START = "<s> ";
        String END = " </s>";
        //Which symbol to use for separating the lines.
        //TODO insert special cases, like "S.K.".
        String DELIMS = ";:\"";
        String line = null;
        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(fileName));

            while ((sCurrentLine = br.readLine()) != null) {
                st = new StringTokenizer(sCurrentLine,DELIMS);
                while(st.hasMoreElements()){
                    line = (String) st.nextElement();
                    line = line.replaceAll(",", " ");
                    line = line.replaceAll(":", " ");
                    line = line.replaceAll(";", " ");
                    line = line.replaceAll("\\(", " ");
                    line = line.replaceAll("\\)", " ");
                    line = line.replaceAll("\\?", " ");
                    line = line.replaceAll("\\."," ");
                    line = line.replaceAll("\"", " ");
                    line = line.replaceAll("\\s+", " ");
                    line = line.trim();
                    if (line.length()>0) {
                        line = START + line + END;
                        lines.add(line);
                    }
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
                    if(wordCount.get(str) == null) {
                        wordCount.put(str, 0);
                    }
                    wordCount.put(str,wordCount.get(str)+1);
                }
            }
        }

        return wordCount;
    }
    public static HashMap<String,Integer> countAllWords2(ArrayList<String> lines
            , int n_gram, HashMap<String,ArrayList<Integer>>[] TN_Table){
        HashMap<String,Integer> wordCount = new HashMap<>();
        TN_Table[n_gram-2] = new HashMap<>();
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
                    int end = (str.length()-(tokes[j+n_gram-1].length()+1));
                    String subStr = str.substring(0, end);
                    if(wordCount.get(str) == null) {
                        wordCount.put(str, 0);
                        if(TN_Table[n_gram-2].get(subStr) == null){
                            ArrayList<Integer> arr = new ArrayList<>(2);
                            arr.add(0);
                            arr.add(0);
                            TN_Table[n_gram-2].put(subStr, arr);
                        }
                        TN_Table[n_gram-2].get(subStr).set(0,TN_Table[n_gram-2].get(subStr).get(0)+1);
                    }
                    wordCount.put(str, wordCount.get(str) + 1);
                    TN_Table[n_gram-2].get(subStr).set(1, TN_Table[n_gram - 2].get(subStr).get(1) + 1);
                }
            }
        }

        return wordCount;
    }


    public static HashMap<String,Integer>[] createNGramTable(ArrayList<String> lines, int n_gram
            ,String method,HashMap<String,ArrayList<Integer>>[] TN_Table){
        @SuppressWarnings("unchecked")
        HashMap<String,Integer>[] nGramTable = new HashMap[n_gram];
        if(method.equals("lw")) {
            for (int i = 0; i < n_gram; i++) {
                nGramTable[i] = countAllWords(lines, i + 1);
            }
        }
        else{
            nGramTable[0] = countAllWords(lines, 1);
            for (int i = 1; i < n_gram; i++) {
                nGramTable[i] = countAllWords2(lines, i + 1,TN_Table);
            }
        }
        return nGramTable;
    }


}
