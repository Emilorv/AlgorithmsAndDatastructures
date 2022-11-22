import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class WeightedGraphAndDijkstra {
    public static void main(String[] args) throws IOException {
        String filnavn= "vg5.txt";
        int startNode =  2;

        Graf graf = new Graf();
        graf.vektetGrafFraFil(new BufferedReader(new FileReader(filnavn)));
        graf.dijkstra(graf.noder[startNode]);
        System.out.println("Ferdig.Node - Ferdig.Forgjenger - distanse");
        for (int i = 0; i <graf.antNoder ; i++) {
                if (((Forgjenger) graf.noder[i].d).dist != Forgjenger.uendelig) {
                    String fra = (graf.noder[i].node == startNode) ? "Start" : String.valueOf(((Forgjenger) graf.noder[i].d).forgjenger.node);
                    System.out.println(graf.noder[i].node + " - " + fra + " - " + ((Forgjenger) graf.noder[i].d).dist);
                } else {
                    System.out.println(graf.noder[i].node + " - " + " - " + "Nåes ikke");
                }
        }
    }
}

class Graf{
    int antNoder, antKanter;
    Node[] noder;
    PriorityQueue<Node> prioKo;

    public void vektetGrafFraFil(BufferedReader br)throws IOException {
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
            int vekt = Integer.parseInt(st.nextToken());
            Vkant k = new Vkant(noder[til], (Vkant)noder[fra].kant, vekt);
            noder[fra].kant=k;
        }
    }

    public void initforgj(Node s ){
        for (int i =antNoder; --i>=0;) {
            noder[i].d = new Forgjenger();
        }
        ((Forgjenger)s.d).dist = 0;
    }

    void forkort(Node n, Vkant k){
        Forgjenger nd = (Forgjenger)n.d, md = (Forgjenger)k.til.d;
        if(md.dist>nd.dist+k.vekt){
            md.dist= nd.dist+k.vekt;
            md.forgjenger = n;
            prioKo.remove(k.til);
            prioKo.add(k.til);
        }
    }

    void dijkstra(Node s){
        initforgj(s);
        lag_prioko();
        for (int i = antNoder; i-->0;) {
            Node n = prioKo.poll();
            for(Vkant k = (Vkant)n.kant; k!=null; k = (Vkant) k.neste)
                forkort(n,k);
        }
    }


    public void lag_prioko() {
        this.prioKo = new PriorityQueue<>(antNoder, Comparator.comparingInt(node -> ((Forgjenger) node.d).dist));
        for (int i = 0; i < antNoder; i++) {
            prioKo.add(noder[i]);
        }
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

class Vkant extends Kant{
    int vekt;
    public Vkant(Node n, Vkant neste, int vekt){
        super(n, neste);
        this.vekt = vekt;
    }
}

class Node {
    int node;
    Kant kant;
    Object d;

    public Node() {

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

