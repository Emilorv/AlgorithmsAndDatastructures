package Ferdig.Komprimering;

import java.io.*;
import java.util.ArrayList;

public class LempelZivKomprimering {
    DataInputStream innfil;
    DataOutputStream utfil;
    int length;
    byte[] data;
    ArrayList<Byte> search_buffer = new ArrayList<>();
    ArrayList<Byte> checkArray = new ArrayList<>();
    int notEncoded;
    int index;

    public void initFiles(String innNavn, String utNavn) throws IOException {
        innfil = new DataInputStream(new BufferedInputStream(new FileInputStream(innNavn)));
        File sjekkefil = new File(utNavn);
        if (!sjekkefil.exists()) {
            sjekkefil.createNewFile();
        }
        utfil = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(utNavn)));
        length = innfil.available();
        data = new byte[length];
        innfil.readFully(data);
    }


    public void LempelZiv() throws IOException {
        index=0;
        notEncoded=0;
        for (int i = 0; i < this.length; i++) {
            checkArray.add(data[i]);
            if (elementsInSearchbuffer(checkArray, search_buffer) == -1 || i == data.length - 1) {
                if (checkArray.size() > 1) {
                    checkArrayMoreThanOne(i);
                }
                notEncoded++;
                index=i+1;
                search_buffer.addAll(checkArray);
                checkArray.clear();
            }
        }
        if(notEncoded>0){
            index=length;
            writeNotEncodedToken(index);
        }
        utfil.close();
    }

    public int elementsInSearchbuffer(ArrayList<Byte> checkArray, ArrayList<Byte> mainArray) {
        int offset = 0;
        //Iterates searchBuffer. If value in checkArray is equal to value in mainArray: Increase offset. If not, start from scratch.
        //If offset is equal or greater than size of checkArray, return the startPosition of the equal part.
        //If Equal part could not be found, return -1
            for (int i = 0; i < mainArray.size(); i++) {
                if (checkArray.size() <= offset) {
                    return i - checkArray.size();
                }if (checkArray.get(offset) == mainArray.get(i)) {
                    offset++;
                    if(checkArray.size()==mainArray.size() && i==mainArray.size()){
                        return 0;
                    }
                } else {
                    offset = 0;
                }
            }
        return -1;
    }


    public void checkArrayMoreThanOne(int i) throws IOException {
        //removes the last element in checkArray because it is not equal to a part in searchbuffer
        checkArray.remove(checkArray.size() - 1);
        String token = createToken(i);
        //If token is better than the original
        if (token.length() < checkArray.size()) {
            writeToFile(i);
        } else{
            notEncoded+=checkArray.size();
        }
        checkArray.add(data[i]);
    }
    public String createToken(int i) {
        int checkindex = elementsInSearchbuffer(checkArray, search_buffer);
        int checkoffset = i - checkindex - checkArray.size();
        int checklength = checkArray.size();
        String token = "[" + checkoffset + "," + checklength + "]";
        return token;
    }
    public void writeNotEncodedToken(int i) throws IOException {
        if(notEncoded>0){
            String notEncodedToken = "["+notEncoded+"]";
            utfil.write(notEncodedToken.getBytes());
            utfil.write(data, index-notEncoded, notEncoded);
        }
        notEncoded=0;

    }

    public void writeToFile(int i) throws IOException {
            writeNotEncodedToken(i);
            utfil.write(createToken(i).getBytes());
    }




}
