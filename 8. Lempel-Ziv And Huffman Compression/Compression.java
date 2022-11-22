package Ferdig.Komprimering;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Compression class
 */
public class Compression {

    private Tree tree;
    private PriorityQueue<TreeNode> pQueue = new PriorityQueue<TreeNode>(Comparator.comparing(TreeNode::findFrequency));
    private int[] frequencyTable = new int[256];
    private String[] bitTable = new String[256];
    private ArrayList<Character> theFile = new ArrayList<>();
    private String treeTable = "";

    /**
     * Compression method
     * @param fileName String
     * @throws IOException IOException
     */
    public void compressFile(String fileName) throws IOException {
        fileToNodes(fileName);
        makeNodes();
        tree = putIn();
        makeTreeTable(tree.root, "");
        makeByteArray(compress());
    }

    /**
     * Method who assert the different bytes from the file into a frequency table at their ascii value. If an 'a' exists
     * three times in the original file, then the number at frequencyTable[97] will be 3. The characters are also added to an
     * ArrayList to compress later.
     * @param fileName String
     * @throws IOException IOException
     */
    public void fileToNodes(String fileName) throws IOException {
        DataInputStream file = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));//uncompressed.txt
        while (file.available() > 0){
            int asciiValues = file.readUnsignedByte();
            frequencyTable[asciiValues]++;
            theFile.add((char)asciiValues);
        }
    }

    /**
     * Makes nodes with the ascii value as the element of the leaf node and the element at the table as the frequency
     */
    public void makeNodes(){
        for(int i = 0; i < frequencyTable.length; i++){//TODO endre i
            if(frequencyTable[i] != 0){
                char letter = (char) i;
                pQueue.add(new Leaf(letter, frequencyTable[i], null, null, null));
            }
        }
    }

    /**
     * Adds the two nodes with the lowest frequency under one node. Then it removes the two nodes and adds the new node to
     * the priority queue
     * @return Tree
     */
    public Tree putIn(){
        while(pQueue.size() > 1){
            TreeNode t1 = pQueue.poll();
            TreeNode t2 = pQueue.poll();

            TreeNode tN = new TreeNode(t1.frequency + t2.frequency, t1, t2, null);
            pQueue.add(tN);

            t1.setParent(tN);
            t2.setParent(tN);
        }
        return new Tree(pQueue.poll());
    }

    /**
     * Makes a sting of the table to decompress later
     * @param currentPosition TreeNode
     * @param bits String
     */
    public void makeTreeTable(TreeNode currentPosition, String bits){
        if(!(currentPosition instanceof Leaf)){
            treeTable += "0";
            makeTreeTable(currentPosition.left, bits + "0");
            makeTreeTable(currentPosition.right, bits + "1");
        } else {
            int a = ((Leaf) currentPosition).element;
            bitTable[a] = bits;

            String binarSt = Integer.toBinaryString(a);
            while(binarSt.length() != 8){
                binarSt = "0" + binarSt;
            }

            treeTable += "1" + binarSt;
        }
    }

    /**
     * Makes a string of the compressed file
     * @return String
     */
    public String compress(){;
        String st = "";
        for (int i = 0; i < theFile.size(); i++) {
            int position = theFile.get(i);
            st += bitTable[position];
        }
        return st;
    }

    /**
     * Method who makes the bytes and the file
     * To have whole bytes the last byte gets padded starting with an 1 og then zeros
     * @param compressed String
     */
    public void makeByteArray(String compressed) throws IOException {
        String compr = treeTable + compressed;

        int l = compr.length()/8;
        int l2 = compr.length()%8;

        byte[] data = new byte[l+1];

        StringBuffer sb = new StringBuffer(compr);
        for (int i = 0; i < l; i++) {
            short a = Short.parseShort(compr.substring(i*8, i*8+8), 2);
            data[i] = (byte) a;
        }

        if(l2 == 0){
            data[l] = (byte)0b10000000;
        }else{
            String sub = compr.substring(compr.length()-l2);
            for (int i = 0; i < 8-l2; i++) {
                if(i == 0){
                    sub += "1";
                } else{
                    sub+= "0";
                }
            }
            short a = Short.parseShort(sub, 2);

            data[l] = (byte) a;
        }

        File here = new File("compressed.txt");
        DataOutputStream utfil = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(here)));
        utfil.write(data);
        utfil.close();
    }
}
