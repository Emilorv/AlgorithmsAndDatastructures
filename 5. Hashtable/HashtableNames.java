
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class HashtableNames {
    LinkedList[] hashTable;
    int m;
    int collisionCounter;
    int elements;

    public HashtableNames(int hashTableSize) {
        this.hashTable = new LinkedList[hashTableSize];
        this.m = hashTable.length;
        this.collisionCounter = 0;
        this.elements = 0;
    }

    public static void main(String[] args) {
        HashtableNames hashtabell = new HashtableNames(107);

        ArrayList<String> dataTable = ReadFromFile();
        hashtabell.addListToHash(dataTable);

        System.out.println("\n\n Search for data: " + hashtabell.searchForData("Emil Orvik Olsson"));


        System.out.println("Antall kollisjoner = " + hashtabell.collisionCounter);
        System.out.println("Lastfaktor = " + hashtabell.getLastFaktor());
        System.out.println("Kollisjoner per person: " + hashtabell.getCollisionsPrElement());

    }

    public int hashFunction(int key) {
        return key % m;
    }

    public void addListToHash(ArrayList<String> dataTable) {
        for (int i = 0; i < dataTable.size(); i++) {
            addToHash(dataTable.get(i));
        }
    }

    public void addToHash(String data) {
        int key = StringToKey(data);
        int hashedKey = hashFunction(key);
        if (hashTable[hashedKey] == null) {
            hashTable[hashedKey] = new LinkedList();
            hashTable[hashedKey].addFirst(data);
        } else {
            System.out.println(data + " collided with: " + hashTable[hashedKey]);
            hashTable[hashedKey].addLast(data);
            collisionCounter++;
        }
        elements++;
    }

    public int StringToKey(String data) {
        int hashedKey = 0;
        for (int i = 0; i < data.length(); i++) {
            int a = data.charAt(i);
         hashedKey += (a + (3) * i);
        }
        return hashedKey;
    }

    public Object searchForData(String data) {
        LinkedList listAtPosition = hashTable[getPositionOfData(data)];
        int position = listAtPosition.indexOf(data);
        if(position>=0) return listAtPosition.get(position);
        else return null;
    }

    public int getPositionOfData(String data) {
        int key = StringToKey(data);
        int position = hashFunction(key);
        return position;
    }

    public double getLastFaktor() {
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

    public static ArrayList<String> ReadFromFile() {
        ArrayList<String> dataList = new ArrayList<>();
        try {
            File myObj = new File("navn.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                dataList.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return dataList;
    }
}
