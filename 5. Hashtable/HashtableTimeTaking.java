package Ferdig;

import java.util.*;

public class HashtableTimeTaking {
    Integer[] hashTable;
    int m;
    int collisionCounter;
    int elements;

    public HashtableTimeTaking(int m) {
        this.hashTable = new Integer[m];
        this.m = m;
        this.collisionCounter = 0;
        this.elements = 0;
    }

    public static void main(String[] args) {
        double timeStop;
        int repeats = 0;
        ArrayList<Integer> randomNumberList = createRandomNumbers(10000000, 12000017);
        double timeStart = new Date().getTime();
        do{
            HashtableTimeTaking hashTable = new HashtableTimeTaking(12000017);
            hashTable.addListToHash(randomNumberList);
            timeStop = new Date().getTime();
            repeats++;
        } while ((timeStop-timeStart)<1000);
        System.out.println("Time placing 10.000.000 numbers in my hashTable: " + (timeStop-timeStart) /repeats);


        repeats = 0;
        timeStart = new Date().getTime();

        do{
            HashMap<Integer,Integer> hashMap = new HashMap();
            for (int i = 0; i < randomNumberList.size(); i++) {
                hashMap.put(randomNumberList.get(i),randomNumberList.get(i));
            }
            timeStop = new Date().getTime();
            repeats++;
        } while ((timeStop-timeStart)<1000);
        System.out.println("Time placing 10.000.000 numbers in java hashTable: " + (timeStop-timeStart) /repeats);


        HashtableTimeTaking hashTable = new HashtableTimeTaking(12000017);
        hashTable.addListToHash(createRandomNumbers(10000000, hashTable.m));

        System.out.println("Number of elements in hashtable: "+hashTable.NumberOfElements());
        System.out.println("LoadFactor: "+ hashTable.getLoadFactor());
        System.out.println("Number of collisions: " + hashTable.collisionCounter);
        System.out.println("Collisions pr element: " + hashTable.getCollisionsPrElement());
    }

    public int hashFunction1(Integer key){
        return key%m;
    }
    public int hashFunction2(Integer key){
        return key%(m-1)+1;
    }
    public void addListToHash(ArrayList<Integer> dataTable) {
        for (Integer integer : dataTable) {
            if(!addToHash(integer)){
                System.out.println(integer + "was not added");
            }
        }
    }

    public boolean addToHash(Integer key) {
        int tableLength = m;
        int h1 = hashFunction1(key);
        int h2 = hashFunction2(key);
        int pos = h1;
        if (hashTable[pos] == null) {
            elements++;
            hashTable[pos] = key;
            return true;
        }

        for (int i = 0; i < tableLength; i++) {
            collisionCounter++;
            pos += h2;
            pos=pos%m;
            if (hashTable[pos] == null) {
                elements++;
                hashTable[pos] = key;
                return true;
            }
        }
        return false;
    }

    public int NumberOfElements(){
        int n = 0;
        for (int i = 0; i < hashTable.length ; i++) {
            if(hashTable[i]!=null){
                n++;
            }
        }
        return n;
    }

    public int findPos(int key) {
        int tableLength = m;
        int h1 = hashFunction1(key);
        int h2 = hashFunction2(key);
        int pos = h1;

        if (hashTable[pos] == null) {
            return -1;
        }
        if(hashTable[pos]==key) return pos;

        for (int i = 0; i < tableLength; i++) {
            pos += h2;
            pos = pos%m;
            if (hashTable[pos] == null) {
                return -1;
            }
            if(hashTable[pos]==key) return pos;
        }
        return -1;
    }


    public double getLoadFactor() {
        double n = 0;
        for (int i = 0; i < m; i++) {
            if (hashTable[i] != null) {
                n++;
            }
        }
        return n / m;
    }
    public double getCollisionsPrElement() {
        return (double) collisionCounter / elements;
    }

    public static ArrayList<Integer> createRandomNumbers(int n, int m){
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i <n ; i++) {
            list.add((int) Math.round(Math.random()*10*m));
        }
        return list;
    }

}
