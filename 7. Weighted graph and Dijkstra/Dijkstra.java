import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @Author Ann-Marie Revillard
 */
public class Dijkstra {
    private int distance[];
    public static String previous[];
    private Set<Integer> settled;
    private PriorityQueue<DNode> pQueue;
    private static int totalDNodes;
    private static int totalJEdges;
    List<List<DNode> > connections;
    private static String fileName = "";

    public Dijkstra() {
        totalDNodes = totalDNodes;
        distance = new int[totalDNodes];
        previous = new String[totalDNodes];
        settled = new HashSet<Integer>();
        pQueue = new PriorityQueue<DNode>(totalDNodes, new DNode());
    }

    public void dijkstra(List<List<DNode> > connections, int startNode) {
        this.connections = connections;
        for (int j = 0; j < totalDNodes; j++) {
            distance[j] = Integer.MAX_VALUE;
            previous[j] = "";
        }
        pQueue.add(new DNode(startNode, 0));
        distance[startNode] = 0;
        previous[startNode] = "start";

        while (settled.size() != totalDNodes) {
            if (pQueue.isEmpty()) {
                return;
            }
            int minimumNode = pQueue.remove().n;
            if (settled.contains(minimumNode)) {
                continue;
            }
            settled.add(minimumNode);
            neighbours(minimumNode);
        }
    }

    private void neighbours(int minimumNode) {
        int edgeDist = -1;
        int newDist = -1;

        for (int j = 0; j < connections.get(minimumNode).size(); j++) {
            DNode node = connections.get(minimumNode).get(j);

            if (!settled.contains(node.n)) {
                edgeDist = node.price;
                newDist = distance[minimumNode] + edgeDist;

                if (newDist < distance[node.n]) {
                    distance[node.n] = newDist;
                }

                pQueue.add(new DNode(node.n, distance[node.n]));
                previous[node.n] = minimumNode + "";
            }
        }
    }

    private static void chooseFile(){
        Scanner input = new Scanner(System.in);
        System.out.println("Choose file\nPress ...\n0 for vgSkandinavia.txt\n1 for vg1.txt\n2 for vg2.txt\n3 for vg3.txt\n4 for vg4.txt\n5 for vg5.txt");
        int choice = input.nextInt();

        switch (choice){
            case 0: fileName = "vgSkandinavia.txt"; break;
            case 1: fileName = "vg1.txt"; break;
            case 2: fileName = "vg2.txt"; break;
            case 3: fileName = "vg3.txt"; break;
            case 4: fileName = "vg4.txt"; break;
            case 5: fileName = "vg5.txt"; break;
            default: System.out.println("That was not an acceptable input"); chooseFile();
        }
    }

    private static int chooseStartNode(){
        Scanner input = new Scanner(System.in);
        System.out.println("Choose the starting node: ");
        int choice = input.nextInt();
        if(choice >= totalDNodes || choice < 0){
            System.out.println("This node does not exist");
            choice = chooseStartNode();
        }
        return choice;
    }

    private static List<List<DNode> > fileReader() throws IOException {
        BufferedReader bR = new BufferedReader((new FileReader(fileName)));
        StringTokenizer st = new StringTokenizer(bR.readLine());
        totalDNodes = Integer.parseInt(st.nextToken());
        totalJEdges = Integer.parseInt(st.nextToken());

        List<List<DNode> > connections = new ArrayList<List<DNode> >();

        for (int i = 0; i < totalDNodes; i++) {
            List<DNode> list = new ArrayList<DNode>();
            connections.add(list);
        }

        for (int i = 0; i < totalJEdges; i++) {
            st = new StringTokenizer(bR.readLine());
            connections.get(Integer.parseInt(st.nextToken())).add(new DNode(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
        }

        bR.close();

        return connections;
    }

    public static void main(String argvs[]) throws IOException {

        chooseFile();
        System.out.println("----------------");

        List<List<DNode>> connections = fileReader();

        int startNode = chooseStartNode();//= 1; //startnode
        System.out.println("----------------");

        Dijkstra obj = new Dijkstra();
        obj.dijkstra(connections, startNode);

        System.out.println("The shortest path from the Dnode :");

        for (int j = 0; j < obj.distance.length; j++) {
            if(obj.distance[j] == Integer.MAX_VALUE){
                System.out.println(j + " is unreachable from " + startNode);
            } else if(Objects.equals(previous[j], "start")){
                System.out.println(startNode + " is the startnode");
            } else{
                System.out.println(startNode + " to " + j + " by "+ previous[j] +" is " + obj.distance[j]);
            }
        }
        System.out.println("\nTotal number of nodes: "+ totalDNodes + " || Total number of edges: " + totalJEdges);

        /*System.out.println(startNode + " to " + 347370 + " is " + obj.distance[347370]);*/
        //oslo 143917
        // trd 347370
    }
}

class DNode implements Comparator<DNode> {
    public int n;
    public int price;
    public DNode() {}
    public DNode(int n, int price) {
        this.n = n;
        this.price = price;
    }
    @Override
    public int compare(DNode n1, DNode n2) {
        if (n1.price < n2.price) {
            return -1;
        }
        if (n1.price > n2.price) {
            return 1;
        }
        return 0;
    }
}