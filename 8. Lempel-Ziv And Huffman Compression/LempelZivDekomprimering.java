package Ferdig.Komprimering;

import java.io.*;
import java.util.ArrayList;

public class LempelZivDekomprimering {
    DataInputStream innfil;
    DataOutputStream utfil;
    int length;
    byte[] data;
    int index;
    int i;
    ArrayList<Byte> characters;

    public void initFiles(String innNavn, String utNavn) throws IOException {
        innfil = new DataInputStream(new BufferedInputStream(new FileInputStream(innNavn)));
        File sjekkefil = new File(utNavn);
        if(!sjekkefil.exists()){
            sjekkefil.createNewFile();
        }
        utfil = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(utNavn)));
        length = innfil.available();
        data = new byte[length];
        innfil.readFully(data);
        characters = new ArrayList<>();
    }

    public void Dekomprimering() throws IOException {
        index = 0;
        for (i = 0; i <length; i++) {
            if(data[i]==91){
                boolean encodedToken = scanToken(i);
                if(!encodedToken){
                    addNotEncodedCharacters();
                    } else{
                    addEncodedCharacters();
                }
            } else{
                System.out.println("Error " + i);
            }
        }
        utfil.close();
    }
    public void addNotEncodedCharacters() throws IOException {
        String token = identifyToken(i);
        i += token.length();
        int notEncodedSteps = Integer.parseInt(token.substring(1, token.length() - 1));
        for (int j = 0; j < notEncodedSteps; j++) {
           writeCharacter(data[i]);
           if (j != notEncodedSteps - 1) {
                i++;
            }
        }
    }
    public void addEncodedCharacters() throws IOException {
        String token = identifyToken(i);
        int divider = token.indexOf(",");
        int offset = Integer.parseInt(token.substring(1,divider));
        int lengthOfRep = Integer.parseInt(token.substring(divider+1,token.length()-1));
        i+=token.length()-1;
        for (int j = 0; j <lengthOfRep; j++) {
            int copyIndex = index-offset;
            writeCharacter(characters.get(copyIndex));
        }
    }

    public boolean scanToken(int i){
        while (data[i++]!=93){
            if(data[i]==44){
                return true;
            }
        }
        return false;
    }

    public String identifyToken(int i){
        String token="";
            token+=(char)91;
            while (data[i++]!=93){
                token+=(char)data[i];
            }
        return token;
    }

    public void writeCharacter(byte data) throws IOException {
        utfil.write(data);
        characters.add(data);
        index++;
    }
}
