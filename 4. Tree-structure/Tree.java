 /**
 * @author Ann-Marie A.F. Revillard og Emil Orvik Olsson
 */

import java.lang.Math;
import java.util.ArrayList;

public class Tree {
    TreNode rot;

    /**
     * Constructor
     */
    public Tree(){
        rot = null;
    }

    /**
     * Returns true or false if it is empty or not
     * @return boolean
     */
    public boolean tomt(){
        return rot == null;
    }

    /**
     * Finds the depth of a node
     * @param n TreNode
     * @return int
     */
    private int finnDybde(TreNode n){
        int d = -1;
        while (n!=null){
            d++;
            n = n.forelder;
        }
        return d;
    }

    /**
     * Finds the height of a node
     * @param n TreNode
     * @return int
     */
    private int finnHoyde(TreNode n){
        if(n == null) return -1;
        else {
            int vh = finnHoyde(n.venstre);
            int hh = finnHoyde(n.hoyre);
            if(vh >= hh) return vh + 1;
            else return hh + 1;
        }
    }

    /**
     * Finds the height of the root
     * @return int
     */
    public int finnHoyde(){
        return finnHoyde(rot);
    }

    /**
     * Prints out the nodes by the preorder method
     * @param n TreNode
     */
    private void skrivPreorden(TreNode n){
        if(n != null){
            System.out.print(n.element + " ");
            skrivPreorden(n.venstre);
            skrivPreorden(n.hoyre);
        }
    }

    /**
     * Prints out the nodes by the preorder method
     */
    public void skrivPreorden(){
        skrivPreorden(rot);
    }

    /**
     * Adds a node to the tree
     * @param s String
     */
    public void settInn(String s){

        String nokkel = s;
        TreNode denViSerPa = rot;
        if (rot == null){
            rot = new TreNode(s, null, null, null);
            return;
        }
        String sml="";
        TreNode f = null;
        while(denViSerPa != null){
            f = denViSerPa;
            sml = f.finnNokkel();
            if(nokkel.compareTo(sml) < 0){
                denViSerPa = denViSerPa.venstre;
            } else denViSerPa = denViSerPa.hoyre;
        }
        if(nokkel.compareTo(sml) < 0) f.venstre = new TreNode(s,null,null,f);
        else f.hoyre = new TreNode(s,null,null,f);
    }

    /**
     * Returns the nodes by the preorder method in a list
     * @param n TreNode
     * @param spaces int
     * @param liste ArrayList
     * @param hoyde int
     * @return ArrayList
     */
    private ArrayList printTre(TreNode n, int spaces, ArrayList liste, int hoyde){
        hoyde--;
        int sp = spaces;
        if(n != null){
            int sideSpace = (sp - n.element.toString().length())/2;
            spaces /= 2;
            liste.add(" ".repeat(sideSpace) + n.element + " ".repeat(sideSpace));

            liste = printTre(n.venstre, spaces, liste, hoyde);
            liste = printTre(n.hoyre, spaces, liste, hoyde);
        } else if(hoyde == 0){
            int sideSpace = ((sp - 2)/2);
            liste.add(" ".repeat(sideSpace) + "  " + " ".repeat(sideSpace));
        }
        else if (hoyde > 0){
            liste = ptZ(spaces, liste, hoyde+1);
        }
        return liste;
    }

    /**
     * Returns the nodes by the preorder method
     */
    public ArrayList printTre(int spaces, ArrayList liste, int hoyde){
        return printTre(rot, spaces, liste, hoyde);
    }

    /**
     * Method to return empty spaces in the tree to fix the spaces
     * @param spaces int
     * @param liste ArrayList
     * @param hoyde int
     * @return ArrayList
     */
    private ArrayList ptZ(int spaces, ArrayList liste, int hoyde){

        hoyde--;
        int sp = spaces;
        if(hoyde >= 0){

            int sideSpace = (sp - 2)/2;
            spaces /= 2;

            liste.add(" ".repeat(sideSpace) + "  " + " ".repeat(sideSpace));

            liste = ptZ(spaces, liste, hoyde);
            liste = ptZ(spaces, liste, hoyde);
        }
        return liste;
    }
/**
 * Class for the nodes
 */
public class TreNode {
    Object element;
    TreNode venstre;
    TreNode hoyre;
    TreNode forelder;

    /**
     * Constructor
     * @param e Object
     * @param v TreNode
     * @param h TreNode
     * @param f TreNode
     */
    public TreNode(Object e, TreNode v, TreNode h, TreNode f){
        element = e;
        venstre = v;
        hoyre = h;
        forelder = f;
    }

    /**
     * Returns the key/element of the node
     * @return String
     */
    public String finnNokkel() {
        return (String) element;
    }
}

/**
 * Class for the tree and its methods
 */





    public static void main(String[] args) {
        Tree tre = new Tree();

        tre.settInn("hode");
        tre.settInn("bein");
        tre.settInn("legg");
        tre.settInn("albue");
        tre.settInn("hake");
        tre.settInn("arm");
        tre.settInn("tann");
        tre.settInn("tå");

        int h = tre.finnHoyde();
        ArrayList liste = new ArrayList();
        ArrayList nyliste = tre.printTre(128, liste, h + 1);

        ArrayList result = new ArrayList();
        result.add(nyliste.get(0).toString());

        for (int i = 0; i < h; i++) {
            String temporary = "";
            for (int j = 1; j < nyliste.size(); j++) {

                int divide = (int) (64/ (Math.pow(2,i)));

                if(nyliste.get(j).toString().length() == divide){
                    temporary += nyliste.get(j).toString();
                } else if(nyliste.get(j).toString().length() == divide -1){
                    temporary += nyliste.get(j).toString() + " ";
                }
            }
            result.add(temporary);
        }
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i).toString());
        }

    }

}

