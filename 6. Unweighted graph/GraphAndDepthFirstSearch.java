import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class GraphAndDepthFirstSearch {

    public static void main(String[] args) throws IOException {
        String filnavn = "ø6g2.txt";
        BufferedReader br = new BufferedReader(new FileReader(filnavn));
        Graf graf = new Graf();
        graf.grafFraFil(br);
        graf.dfs_pa_Noder(false);
        System.out.println("------------");
        Graf transponertGraf = new Graf();
        br = new BufferedReader(new FileReader(filnavn));
        transponertGraf.grafFraFilT(br);
        graf.sortNodes();
        int ssk = transponertGraf.finnSterktSammenhengendeKomponenter(graf.getNodeOrder(), true);
        System.out.println("Antall sterkt sammenhengende komponenter: " + ssk );
        System.out.println("----------");
    }
}

class Graf{
    int antNoder, antKanter;
    Node[] noder;

    public void grafFraFil(BufferedReader br)throws IOException{
        StringTokenizer st = new StringTokenizer(br.readLine());
        antNoder =Integer.parseInt(st.nextToken());
        noder = new Node[antNoder];
        for (int i = 0; i < antNoder; i++){
            noder[i] = new Node();
            noder[i].node = i;
        }
        antKanter = Integer.parseInt(st.nextToken());
        for (int i = 0; i < antKanter; i++) {
            st = new StringTokenizer(br.readLine());
            int fra = Integer.parseInt(st.nextToken());
            int til = Integer.parseInt(st.nextToken());
            Kant k = new Kant(noder[til], noder[fra].kant);
            noder[fra].kant=k;
        }
    }

    public void grafFraFilT(BufferedReader br)throws IOException{
        StringTokenizer st = new StringTokenizer(br.readLine());
        antNoder =Integer.parseInt(st.nextToken());
        noder = new Node[antNoder];
        for (int i = 0; i < antNoder; i++){
            noder[i] = new Node();
            noder[i].node = i;
        }
        antKanter = Integer.parseInt(st.nextToken());
        for (int i = 0; i < antKanter; i++) {
            st = new StringTokenizer(br.readLine());
            int til = Integer.parseInt(st.nextToken());
            int fra = Integer.parseInt(st.nextToken());
            Kant k = new Kant(noder[til], noder[fra].kant);
            noder[fra].kant=k;
        }
    }
    public void dfs_init(){
        for (int i =antNoder; i-->0;) {
            noder[i].d = new DFS_forgjenger();
        }
        DFS_forgjenger.null_tid();
    }

    public void df_sok(Node n, boolean print){
        if(print){
            System.out.print(n+" ");
        }
        n.visited = true;
        DFS_forgjenger nd = (DFS_forgjenger)n.d;
        nd.funnet_tid = DFS_forgjenger.les_tid();
        for(Kant k = n.kant; k!=null; k=k.neste){
            DFS_forgjenger md = (DFS_forgjenger)k.til.d;
            if(md.funnet_tid==0){
                md.forgjenger = n;
                md.dist = nd.dist+1;
                if(!k.til.visited){
                    df_sok(k.til, print);
                }
            }
        }
        nd.ferdig_tid = DFS_forgjenger.les_tid();
    }

    public void dfs(Node s, boolean print){
        dfs_init();
        ((DFS_forgjenger)s.d).dist=0;
        if(!s.visited) {
            df_sok(s, print);
        }
    }

    public void dfs_pa_Noder(boolean print){
        dfs(noder[0], print);
        for (int i = 0; i <antNoder ; i++) {
            if(!noder[i].visited){
                df_sok(noder[i], print);
            }
        }
    }
    public void sortNodes(){
        for (int j = 1; j <this.antNoder; j++) {
            Node bytt = this.noder[j];
            int i = j-1;
            while(i>=0 && ((DFS_forgjenger) noder[i].d).ferdig_tid<((DFS_forgjenger)bytt.d).ferdig_tid){
                this.noder[i+1] = this.noder[i];
                i--;
            }
            noder[i+1]=bytt;
        }
    }

    public ArrayList getNodeOrder(){
        ArrayList list = new ArrayList<>();
        for (int i = 0; i <noder.length ; i++) {
            list.add(noder[i].node);
        }
        return list;
    }

    public int finnSterktSammenhengendeKomponenter(ArrayList<Integer> order, boolean print){
        int n = 1;
        System.out.print("Komponent " + n + ": ");
        dfs(noder[order.get(0)], print);
        System.out.println("");
        for (int indeks : order) {
            if(!noder[indeks].visited){
                n++;
                System.out.print("Komponent "+ n + ": ");
                df_sok(noder[indeks], print);
                if( print){
                    System.out.println(" ");
                }
            }
        }
        return  n;
    }
}

class Kant{
    Node til;
    Kant neste;


    public Kant(Node til, Kant neste){
        this.til = til;
        this.neste = neste;
    }
}

class Node{
    int node;
    Kant kant;
    Object d;
    boolean visited;

    public Node() {

    }

    @Override
    public String toString() {
        return node+"";
    }
}

class Forgjenger{
    int dist;
    Node forgjenger;
    static int uendelig = 10000000;

    public Forgjenger(){
        dist=uendelig;
    }

}

class DFS_forgjenger extends Forgjenger {
    int funnet_tid, ferdig_tid;
    static int tid;
    static void null_tid(){tid=0;}
    static int les_tid(){return tid++;}

}


