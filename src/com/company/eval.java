package com.company;

/**
 * Created by pinhas on 11/05/2015.
 */
public class eval {

    public static void main(String[] args) {
        if(args.length != 4){
            System.out.println("The program can only run in the following way:");
            System.out.println("eval -i <input file> -m <model file>");
        }
        else{
            String inPath = args[1];
            String modelPath = args[3];
            double Perplexity = 0;

        }
    }
}
