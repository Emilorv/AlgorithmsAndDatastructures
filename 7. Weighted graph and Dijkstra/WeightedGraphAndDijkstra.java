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

        WGraf graf = new WGraf();
        graf.vektetGrafFraFil(new BufferedReader(new FileReader(filnavn)));
        graf.dijkstra(graf.noder[startNode]);
        System.out.println("Ferdig.Node - Ferdig.Forgjenger - distanse");
        for (int i = 0; i <graf.antNoder ; i++) {
                if (((WForgjenger) graf.noder[i].d).dist != WForgjenger.uendelig) {
                    String fra = (graf.noder[i].node == startNode) ? "Start" : String.valueOf(((WForgjenger) graf.noder[i].d).forgjenger.node);
                    System.out.println(graf.noder[i].node + " - " + fra + " - " + ((WForgjenger) graf.noder[i].d).dist);
                } else {
                    System.out.println(graf.noder[i].node + " - " + " - " + "Nåes ikke");
                }
        }
    }
}

class WGraf {
    int antNoder, antKanter;
    WNode[] noder;
    PriorityQueue<WNode> prioKo;

    public void vektetGrafFraFil(BufferedReader br)throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        antNoder =Integer.parseInt(st.nextToken());
        noder = new WNode[antNoder];
        for (int i = 0; i < antNoder; i++){
            noder[i] = new WNode();
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

    public void initforgj(WNode s ){
        for (int i =antNoder; --i>=0;) {
            noder[i].d = new WForgjenger();
        }
        ((WForgjenger)s.d).dist = 0;
    }

    void forkort(WNode n, Vkant k){
        WForgjenger nd = (WForgjenger)n.d, md = (WForgjenger)k.til.d;
        if(md.dist>nd.dist+k.vekt){
            md.dist= nd.dist+k.vekt;
            md.forgjenger = n;
            prioKo.remove(k.til);
            prioKo.add(k.til);
        }
    }

    void dijkstra(WNode s){
        initforgj(s);
        lag_prioko();
        for (int i = antNoder; i-->0;) {
            WNode n = prioKo.poll();
            for(Vkant k = (Vkant)n.kant; k!=null; k = (Vkant) k.neste)
                forkort(n,k);
        }
    }


    public void lag_prioko() {
        this.prioKo = new PriorityQueue<>(antNoder, Comparator.comparingInt(node -> ((WForgjenger) node.d).dist));
        for (int i = 0; i < antNoder; i++) {
            prioKo.add(noder[i]);
        }
    }

}

class WKant {
    WNode til;
    WKant neste;


    public WKant(WNode til, WKant neste){
        this.til = til;
        this.neste = neste;
    }
}

class Vkant extends WKant {
    int vekt;
    public Vkant(WNode n, Vkant neste, int vekt){
        super(n, neste);
        this.vekt = vekt;
    }
}

class WNode {
    int node;
    WKant kant;
    Object d;

    public WNode() {

    }
}

class WForgjenger {
    int dist;
    WNode forgjenger;
    static int uendelig = 10000000;

    public WForgjenger(){
        dist=uendelig;
    }

}

