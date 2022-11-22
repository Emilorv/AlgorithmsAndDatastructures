
import java.io.*;
import java.util.ArrayList;

/**
 * Decompression class
 */
public class Decompression {

    private Tree tree;
    private String bits = "";
    private int[] compressedBitsTable;
    private int traversalThroughTable = 1;

    /**
     * Decompression method
     * @param fileName String
     * @throws IOException IOException
     */
    public void decompressFile(String fileName) throws IOException {
        fileToBinary(fileName);
        TreeNode tN = new TreeNode(0,null, null, null);
        tree = new Tree(tN);
        makeNewTree(tN);
        deCompress();
    }

    /**
     * Reads the bytes of the file and puts them into a string. It checks the last byte for the last '1'. It gets
     * removed along with other zeros behind it
     * @param fileName String
     * @throws IOException IOException
     */
    public void fileToBinary(String fileName) throws IOException {
        DataInputStream file = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        ArrayList<Integer> bytes = new ArrayList<>();
        while (file.available() > 0){
            bytes.add(file.readUnsignedByte());
        }

        for (int i = 0; i < bytes.size()-1; i++) {
            String binarSt = Integer.toBinaryString(bytes.get(i));
            while(binarSt.length() != 8){
                binarSt = "0" + binarSt;
            }
            bits += binarSt;
        }

        //the last bit
        String lastBits = Integer.toBinaryString(bytes.get(bytes.size()-1));
        while(lastBits.length() != 8){
            lastBits = "0" + lastBits;
        }
        boolean fixed = false;
        while (!fixed){
            if(lastBits.charAt(lastBits.length()-1) == '0'){
                StringBuffer sb = new StringBuffer(lastBits);
                lastBits = sb.deleteCharAt(lastBits.length()-1).toString();
            } else {
                StringBuffer sb = new StringBuffer(lastBits);
                lastBits = sb.deleteCharAt(lastBits.length()-1).toString();
                if(!lastBits.equals("")){
                    bits += lastBits;
                }
                fixed = true;
            }
        }

        compressedBitsTable = new int[bits.length()];
        for (int i = 0; i < bits.length(); i++) {
            if((int)bits.charAt(i) == 49){
                compressedBitsTable[i] = 1;
            }
        }
    }

    /**
     * Makes a new tree by using the table with the bits. If it meets an 1 it means a leaf node. Then it goes the the
     * makeLeaf method
     * @param currentPosition TreeNode
     */
    public void makeNewTree(TreeNode currentPosition){
        if(compressedBitsTable[traversalThroughTable] == 0){
            traversalThroughTable++;
            TreeNode left = new TreeNode(0, null,null,currentPosition);
            currentPosition.left = left;
            makeNewTree(left);

        } else{
            makeLeaf('l', currentPosition);
        }

        if(compressedBitsTable[traversalThroughTable] == 0){
            traversalThroughTable++;
            TreeNode right = new TreeNode(0, null,null,currentPosition);
            currentPosition.right = right;
            makeNewTree(right);
        } else{
            makeLeaf('r', currentPosition);
        }
    }

    /**
     * It takes the next 8 bits and finds the char and sets it as the element of the new leaf node
     * @param direction char
     * @param currentPosition TreeNode
     */
    public void makeLeaf(char direction, TreeNode currentPosition){
        traversalThroughTable++;
        String string = "";
        for (int i = 0; i < 8; i++) {
            string += compressedBitsTable[traversalThroughTable];
            traversalThroughTable++;
        }
        char e = (char)Integer.parseInt(string, 2);
        Leaf leaf = new Leaf(e,0, null, null, currentPosition);
        if(direction == 'l'){
            currentPosition.left = leaf;
        } else{
            currentPosition.right = leaf;
        }
    }

    /**
     * Decompresses the bits by traversing the new tree
     * @throws IOException IOException
     */
    public void deCompress() throws IOException {
        ArrayList<Byte> test = new ArrayList<>();

        TreeNode tN = tree.root;
        while(traversalThroughTable != compressedBitsTable.length){
            if(compressedBitsTable[traversalThroughTable] == 1){
                tN = tN.right;
            } else{
                tN = tN.left;
            }

            if(tN instanceof Leaf){
                test.add((byte) ((Leaf) tN).element);
                tN = tree.root;
            }

            traversalThroughTable++;
        }

        byte[] data = new byte[test.size()];
        for (int i = 0; i < test.size(); i++) {
            data[i] = test.get(i);
        }

        File here = new File("decompressed.txt");
        DataOutputStream utfil = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(here)));
        utfil.write(data);
        utfil.close();
    }
}