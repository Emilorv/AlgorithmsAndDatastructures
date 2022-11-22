package Ferdig;

import java.util.Date;

public class RecursionAndMathPow {
    public static void main(String[] args) {
        System.out.println("test power: " + Power(3,14));
        System.out.println("test power: " + Power(2,12));
        System.out.println("test power2; "+ Power2(3,14));
        System.out.println("test power2; "+ Power2(2,12));

        int exp = 4000;

        double timeStart = new Date().getTime();
        double timeStop = 0;
        int repeats = 0;
        double task1;
        do{
            task1 = Power(1.001,exp);
            timeStop = new Date().getTime();
            repeats++;
        } while ((timeStop-timeStart)<1000);
        System.out.println("Task 1: " + task1);
        System.out.println("Time task 1: " + (timeStop-timeStart)/repeats);

        timeStart = new Date().getTime();
        repeats = 0;

        double task2;
        do{
            task2 = Power2(1.001,exp);
            timeStop = new Date().getTime();
            repeats++;
        } while((timeStop-timeStart)<1000);
        System.out.println("Task 2: " + task2);
        System.out.println("Time task 2 " + (timeStop-timeStart)/repeats);

        timeStart = new Date().getTime();
        repeats = 0;

        double task3;
        do{
            task3 = Math.pow(1.001,exp);
            timeStop = new Date().getTime();
            repeats++;
        } while((timeStop-timeStart)<1000);
        System.out.println("Task 3: " + task3);
        System.out.println("Time task 3: " +(timeStop-timeStart)/repeats);
    }

    public static double Power(double x, int n){
        if(n==0)return 1;
        else return x * Power(x,n-1);
    }

    public static double Power2(double x, int n) {
        if (n == 0) return 1;
        else if (n % 2 == 1) return x * Power2(x * x, (n - 1) / 2);
        else return Power2(x * x, n / 2);
    }
}
