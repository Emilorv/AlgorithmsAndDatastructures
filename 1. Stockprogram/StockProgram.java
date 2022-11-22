package Ferdig;

import java.util.Date;
import java.util.Random;

public class StockProgram {

    public static void main(String[] args) {
        //Use defined course
        int[] course = {-1,3,-9,2,2,-1,2,-1,-5};
        StockAlgorithm(course);

        //Makes random course
        int length = 10000, max= 10, min = -10;
        int[] course2 = new int [length];
        for (int i= 0; i<length; i++){
            course2[i] = new Random().nextInt((max-min)+1)+min;
        }
        StockAlgorithm(course2);
    }

    public static void StockAlgorithm(int[] course){
        long start = new Date().getTime();
        int buyValue = 0;
        int buyDate =0;
        int sellDate = 0;
        int bestDifference =0;

        for (int i=0; i<course.length; i++){
            int sellValue = 0;
            buyValue += course[i];
            for(int j=0; j<course.length; j++) {
                sellValue += course[j];
                if(sellValue-buyValue>bestDifference){
                    if( i<j) {
                        bestDifference = sellValue-buyValue;
                        buyDate = i;
                        sellDate = j;
                    }
                }
            }

        }

        long slutt = new Date().getTime();
        System.out.println("Buy date: " + buyDate);
        System.out.println("Sell date: " + sellDate);
        System.out.println( "Program execution time: " + (slutt-start) + " milliseconds");
    }
}


