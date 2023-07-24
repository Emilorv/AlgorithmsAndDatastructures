import java.io.*;
import java.util.*;

public class Dijkstra_AStar {
    String nodesFile = "noder.txt";
    String edgesFile = "kanter.txt";
    String interestPointsFile = "interessepkt.txt";
    static int nodeCount, edgeCount;
    PriorityQueue<Node> pq;
    Node[] nodes;
    ArrayList<Landmark> landmarks = new ArrayList<>();
    HashMap<String, Integer> places = new HashMap<>();
    static boolean menu = true;

    /**
     * main
     * @param args String[]
     * @throws IOException IOException
     */
    public static void main(String[] args) throws IOException {
        Dijkstra_AStar graph = new Dijkstra_AStar(false);
        menu = true;
        while (menu) {
            System.out.print("-------------------------------\n\n");
            graph.menu();
            graph.reset();
        }

    }
    /**
     * Constructor
     * @param isTransposed boolean
     * @throws IOException IOException
     */
    public Dijkstra_AStar(boolean isTransposed) throws IOException {
        getNodes(nodesFile);
        if (isTransposed) {
            getTransposedEdges(edgesFile, this.nodes);
        } else {
            getEdges(edgesFile, this.nodes);
        }
        getInterestPoints(interestPointsFile);
        getLandmarks();

        for (int i=1; i<=landmarks.size(); i++) {
            readLandMarkFiles(landmarks.get(i-1),"landmarks"+i+".csv",true);
            readLandMarkFiles(landmarks.get(i-1),"landmarks-transposed"+i+".csv",false);
        }
    }
    /**
     * menu
     */
    public void menu() {
        boolean poi=false;
        boolean dijkstra = false;
        boolean aStar =false;
        boolean alt = false;
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose");
        System.out.println("1. Dijkstra");
        System.out.println("2. A*");
        System.out.println("3. ALT");
        System.out.println("4. ALl of Them, ALT!");
        System.out.println("5. Point of interest");
        System.out.println("6. Exit menu");
        System.out.println("Anything else: Quit ");
        int choice = Integer.parseInt(sc.nextLine());
        if(choice ==1){
            dijkstra = true;
        } else if(choice==2){
            aStar = true;
        } else if (choice==3) {
           alt=true;
        } else if (choice==4) {
            aStar = true;
            dijkstra = true;
            alt = true;
        } else if (choice==5) {
            poi = true;
        } else if (choice==6) {
            menu = false;
            return;
        } else{
            System.exit(0);
        }

        if(poi){
            System.out.println("Type location name: ");
            ArrayList<Node> poiNodes = new ArrayList<>();

            Node start = nodes[this.places.get('"' + sc.nextLine().toLowerCase() + '"')];
            System.out.println("Point-of-interest code: ");
            int poiCode = Integer.parseInt(sc.nextLine());
            System.out.println("Amount: ");
            int amount = Integer.parseInt(sc.nextLine());

            run_poi_dijkstra(poiNodes, start, poiCode, amount);
            reset();
        }else {
            System.out.println("Type start and end place");
            int start = this.places.get('"' + sc.nextLine().toLowerCase() + '"');
            int end = this.places.get('"' + sc.nextLine().toLowerCase() + '"');
            if (dijkstra) {
                run_dijkstra(this, start, end);
                System.out.println("*------------------*");
                reset();
            }
            if(aStar){
                run_astar(this,start,end);
                System.out.println("*------------------*");
                reset();
            }
            if(alt){
                run_alt(this,start,end);
                System.out.println("*------------------*");
                reset();
            }
        }
    }

    /************************
     * All algorithm functions
     ***********************/
    /**
     * Dijkstra method used for ALT
     * @param startNode Node
     * @param list Node
     * @return int[]
     */
    public int[] dijkstra(Node startNode, int[] list) {
        startNode.distance = 0;
        pq = new PriorityQueue<>(nodeCount, Comparator.comparingDouble(a -> a.distance));
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Node priorityNode = pq.poll();
            list[priorityNode.value] = (int)priorityNode.distance;
            for(Edge e = priorityNode.edge; e != null; e = e.next) {
                if (e.to.distance > priorityNode.distance + e.weight) {
                    e.to.distance = priorityNode.distance + e.weight;
                    e.to.pred = priorityNode;
                    pq.add(e.to);
                }
            }
        }

        return list;
    }
    /**
     * Dijkstra method used for finding interest points
     * @param startNode Node
     * @param poiCode int
     * @param amount int
     * @return ArrayList<Node
     */
    public ArrayList<Node> dijkstra(Node startNode, int poiCode, int amount) {
        ArrayList<Node> list = new ArrayList<>();
        startNode.distance = 0;
        pq = new PriorityQueue<>(nodeCount, Comparator.comparingDouble(a -> a.distance));
        pq.add(startNode);
        int count = 0;

        while (!pq.isEmpty() && count != amount) {
            Node priorityNode = pq.poll();
            if(checkNodeType(priorityNode.type, poiCode)){
                list.add(priorityNode);
                count++;
            }
            for(Edge e = priorityNode.edge; e != null; e = e.next) {
                if (e.to.distance > priorityNode.distance + e.weight) {
                    e.to.distance = priorityNode.distance + e.weight;
                    e.to.pred = priorityNode;
                    pq.add(e.to);
                }
            }
        }

        return list;
    }
    /**
     * Dijkstra method used for A star
     * @param startNode Node
     * @param endNode Node
     * @return int
     */
    public int dijkstra(Node startNode, Node endNode) {
        startNode.distance = 0;
        pq = new PriorityQueue<>(nodeCount, Comparator.comparingDouble(a -> a.distance));
        pq.add(startNode);
        int count = 0;
        endNode.end = true;

        while (!pq.isEmpty()) {
            Node priorityNode = pq.poll();
            count++;
            if (priorityNode.end) {
                return count;
            }

            for(Edge e = priorityNode.edge; e != null; e = e.next) {
                if (e.to.distance > priorityNode.distance + e.weight) {
                    e.to.distance = priorityNode.distance + e.weight;
                    e.to.pred = priorityNode;
                    pq.add(e.to);
                }
            }
        }
        return -1;
    }
    /**
     * A* method
     * @param startNode Node
     * @param endNode Node
     * @return int
     */
    public int AStar(Node startNode, Node endNode) {
        startNode.distance = 0;
        startNode.distToEnd = distance(startNode, endNode);
        startNode.fullDistance = startNode.distToEnd;
        endNode.end = true;
        pq = new PriorityQueue<>(nodeCount, Comparator.comparingDouble(a -> a.fullDistance));
        pq.add(startNode);
        int count = 0;

        while (!pq.isEmpty()) {
            Node priorityNode = pq.poll();
            count++;
            if (priorityNode.end) {
                return count;
            }

            for(Edge e = priorityNode.edge; e != null; e = e.next) {
                if (e.to.distance > priorityNode.distance + e.weight) {
                    if (e.to.distToEnd == -1) {
                        e.to.distToEnd = distance(e.to, endNode);
                    }
                    e.to.distance = priorityNode.distance + e.weight;
                    e.to.pred = priorityNode;
                    e.to.fullDistance = e.to.distance + e.to.distToEnd;
                    pq.remove(e.to);
                    pq.add(e.to);
                }
            }
        }
        return -1;
    }
    /**
     * Alt method
     * @param startNode Node
     * @param endNode Node
     * @return int
     */
    public int alt(Node startNode, Node endNode) {
        startNode.fullDistance = 0;
        startNode.distance = 0;
        endNode.end = true;
        pq = new PriorityQueue<>(nodeCount, Comparator.comparingInt(a -> (int)a.fullDistance));
        pq.add(startNode);
        int count = 0;

        while (!pq.isEmpty()) {
            Node priorityNode = pq.poll();
            count++;
            if (priorityNode.end) {
                return count;
            }

            for(Edge e = priorityNode.edge; e != null; e = e.next) {
                if (e.to.distance > priorityNode.distance + e.weight) {
                    int biggest = 0;
                    /*1. Slå opp avstand fra første landemerke til målet, og trekk fra avstand fra første landemerke til n.
                    Hvis tallet blir negativt, bruk 0 i stedet.*/
                    for (int i = 0; i <landmarks.size() ; i++) {
                        int landmarkToGoal = findDistance(endNode, i, true);
                        int landmarkToCurrentNode = findDistance(priorityNode, i, true);
                        int difference1 = landmarkToGoal - landmarkToCurrentNode;
                        if (difference1<0) difference1 = 0;

                        /*2. Slå deretter opp avstand fra n til første landemerke, og trekk fra avstanden fra målet til landemerket.
                        Det største av disse to tallene, er det beste estimatet første landemerke kan gi oss.*/
                        int nodeToLandmark = findDistance(priorityNode, i, false);
                        int goalToLandmark = findDistance(endNode, i, false);
                        int difference2 = nodeToLandmark - goalToLandmark;
                        if (difference2<0) difference2 = 0;

                        /*3. Gjør deretter samme beregning for de andre landemerkene. Det største av alle estimatene, er det
                        beste. Det bruker vi som estimat for nodens avstand til målet.*/
                        int biggest2 = Math.max(difference1, difference2);
                        if(biggest2 > biggest){
                            biggest = biggest2;
                        }
                    }

                    e.to.distance = priorityNode.distance + e.weight;
                    e.to.pred = priorityNode;
                    e.to.fullDistance = e.to.distance + biggest;
                    pq.remove(e.to);
                    pq.add(e.to);
                }
            }
        }
        return -1;
    }
    /**
     * Run dijkstra
     * @param g Dijkstra_AStar
     * @param start int
     * @param end int
     */
    public void run_dijkstra(Dijkstra_AStar g, int start, int end) {
        long timeStart = System.nanoTime();
        int m = g.dijkstra(g.nodes[start], g.nodes[end]);
        long timeEnd = System.nanoTime();

        long total = timeEnd-timeStart;
        System.out.println("Dijkstra time: " + (double)total/1000000000 +"s");
        Node n = g.nodes[end];
        int counter = 0;
        try {
            FileWriter os = new FileWriter("dijkstra.csv");
            System.out.println("Total time by driving: "+timeFormat((int)n.distance));

            while(n !=null){
                counter++;
                os.write(n.getCoordinates() +"\n");
                n = n.pred;
            }
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Antall noder sjekket: "+m);
        System.out.println("Antall noder på veien: "+counter);
    }
    /**
     * Run A*
     * @param g Dijkstra_AStar
     * @param start int
     * @param end int
     */
    public void run_astar(Dijkstra_AStar g, int start, int end) {
        long timeStart = System.nanoTime();
        int m = g.AStar(g.nodes[start], g.nodes[end]);
        long timeEnd = System.nanoTime();
        long total = timeEnd-timeStart;
        System.out.println("Astar time: " + (double)total/1000000000 +"s");
        Node n = g.nodes[end];
        int counter = 0;
        try {
            FileWriter os = new FileWriter("astar.csv");
            System.out.println("Total time by driving: "+timeFormat((int)n.distance)+" : ");
            while(n !=null){
                os.write(n.getCoordinates() +"\n");
                n = n.pred;
                counter++;
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Antall noder sjekket: "+m);
        System.out.println("Antall noder på veien: "+counter);
    }
    /**
     * Run point of interests
     * @param poiNodes ArrayList<Node>
     * @param start Node
     * @param poiCode int
     * @param amount int
     */
    public void run_poi_dijkstra(ArrayList<Node> poiNodes, Node start, int poiCode, int amount) {
        poiNodes = dijkstra(start,poiCode,amount);
        poiNodes.forEach(p -> System.out.println(p.toString()));

        try {
            FileWriter os = new FileWriter("poi.csv");

            for (Node node : poiNodes) {
                os.write(node.getCoordinates() +"\n");
            }

            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Run alt
     * @param g Dijkstra_AStar
     * @param start int
     * @param end int
     */
    public void run_alt(Dijkstra_AStar g, int start, int end) {
        long timeStart = System.nanoTime();
        int m = g.alt(g.nodes[start], g.nodes[end]);
        long timeEnd = System.nanoTime();
        long total = timeEnd-timeStart;
        System.out.println("ALT time: " + (double)total/1000000000 +"s");
        Node n = g.nodes[end];
        int counter = 0;
        try {
            FileWriter os = new FileWriter("alt.csv");
            System.out.println("Total time by driving: "+timeFormat((int)n.distance)+" : ");
            while(n !=null){
                os.write(n.getCoordinates() +"\n");
                n = n.pred;
                counter++;
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Antall noder sjekket: "+m);
        System.out.println("Antall noder på veien: "+counter);
    }

    /***************************
     * Preprocessing functions
     **************************/
    /**
     *
     * @param fileName String
     * @return Node[]
     * @throws IOException IOException
     */
    public Node[] getNodes(String fileName) throws IOException {
        BufferedReader br = new BufferedReader((new FileReader(fileName)));
        StringTokenizer st = new StringTokenizer(br.readLine());
        this.nodeCount = Integer.parseInt(st.nextToken());
        this.nodes = new Node[nodeCount];
        for (int i = 0; i < nodeCount; ++i) {
            st = new StringTokenizer(br.readLine());
            int index = Integer.parseInt(st.nextToken());
            double lat = Double.parseDouble(st.nextToken())*(Math.PI/180);
            double lon = Double.parseDouble(st.nextToken())*(Math.PI/180);
            nodes[index] = new Node(index, lat, lon);
        }

        return nodes;
    }
    /**
     * Reads the edges off the file
     * @param fileName String
     * @param nodes Node[]
     * @throws IOException IOException
     */
    public void getEdges(String fileName, Node[] nodes) throws IOException {
        BufferedReader br = new BufferedReader((new FileReader(fileName)));
        StringTokenizer st = new StringTokenizer(br.readLine());
        this.edgeCount = Integer.parseInt(st.nextToken());
        for (int i = 0; i < edgeCount; ++i) {
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            int length = Integer.parseInt(st.nextToken());
            int speedLimit = Integer.parseInt(st.nextToken());
            Edge e = new Edge(nodes[to], nodes[from].edge, weight, speedLimit, length);
            nodes[from].edge = e;
        }
    }
    /**
     * Reads the transposed edges off the file
     * @param fileName String
     * @param nodes Node[]
     * @throws IOException IOException
     */
    public void getTransposedEdges(String fileName, Node[] nodes) throws IOException {
        BufferedReader br = new BufferedReader((new FileReader(fileName)));
        StringTokenizer st = new StringTokenizer(br.readLine());
        this.edgeCount = Integer.parseInt(st.nextToken());
        for (int i = 0; i < edgeCount; ++i) {
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            int length = Integer.parseInt(st.nextToken());
            int speedLimit = Integer.parseInt(st.nextToken());
            Edge e = new Edge(nodes[from], nodes[to].edge, weight, speedLimit, length);
            nodes[to].edge = e;
        }
    }
    /**
     * Reads the interest points off the file
     * @param fileName String
     * @throws IOException IOException
     */
    public void getInterestPoints(String fileName) throws IOException {
        BufferedReader br = new BufferedReader((new FileReader(fileName)));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int m = Integer.parseInt(st.nextToken());
        for (int i = 0; i < m; ++i) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int type = Integer.parseInt(st.nextToken());
            String name = st.nextToken();
            while (st.hasMoreTokens()) {
                name += " " + st.nextToken();
            }

            this.nodes[n].type = type;
            this.nodes[n].name = name;
            this.places.put(name.toLowerCase(), n);
        }
    }
    /**
     * Sets the landmarks
     */
    public void getLandmarks() {
        Landmark landmark1 = new Landmark(nodes[3030649]);  //Florø -- West coast in Norway
        Landmark landmark2 = new Landmark(nodes[4606006]);  //Ilomantsi -- East side of Finland
        Landmark landmark3 = new Landmark(nodes[2552883]); //Odense -- South of Denmark
        Landmark landmark4 = new Landmark(nodes[2713398]); //Hammerfest -- North in Norway
        landmarks.add(landmark1);
        landmarks.add(landmark2);
        landmarks.add(landmark3);
        landmarks.add(landmark4);
    }

    /******************************
     * Important helping functions
     ****************************/
    /**
     * Method who reads the landmark files
     * @param landmark Landmark
     * @param filename String
     * @param from boolean
     * @throws IOException IOException
     */
    public void readLandMarkFiles(Landmark landmark, String filename, boolean from) throws IOException {
        Scanner sc = new Scanner(new File(filename));
        int counter = 0;
        if (from) {
            while (sc.hasNextInt()) {
                landmark.distanceFrom[counter] =  sc.nextInt();
                counter++;
            }
        } else {
            while (sc.hasNextInt()) {
                landmark.distanceTo[counter] =  sc.nextInt();
                counter++;
            }
        }
    }
    /**
     * The reset method
     */
    public void reset(){
        for (Node n : nodes) {
            n.pred = null;
            n.end = false;
            n.driveTime = Node.infinity;
            n.distance = Node.infinity;
            n.fullDistance = Node.infinity;
            n.distToEnd = -1;
        }
    }
    /**
     * Method who finds the distance between landmark and node
     * @param node Node
     * @param landmark int
     * @param fromLandmark boolean
     * @return int
     */
    public int findDistance(Node node, int landmark, boolean fromLandmark) {
        if (fromLandmark) {
            return landmarks.get(landmark).distanceFrom[node.value];
        }else{
            return landmarks.get(landmark).distanceTo[node.value];
        }
    }
    /**
     * Method who checks the position of interest
     * @param type int
     * @param poiCode int
     * @return boolean
     */
    public boolean checkNodeType(int type, int poiCode){
        if((type & 1) ==1 && poiCode == 1 ){
            return true;
        }
        if((type & 2) ==2 && poiCode == 2){
            return true;
        }
        if((type & 4) ==4 && poiCode == 4){
            return true;
        }
        if ((type & 8) == 8 && poiCode == 8){
            return true;
        }
        if((type & 16) ==16 && poiCode == 16){
            return true;
        }
        if((type & 32) == 32 && poiCode == 32){
            return true;
        }
        return false;
    }
    /**
     * Method who calculates distance with haversine
     * @param n1 Node
     * @param n2 Node
     * @return double
     */
    public double distance(Node n1, Node n2) {
        double sin_width = Math.sin((((n1.latitude * Math.PI) / 180)-((n2.latitude* Math.PI) / 180))/2.0);
        double sin_length = Math.sin((((n1.longitude * Math.PI) /180)-((n2.longitude * Math.PI) / 180))/2.0);
        return (41701090.90909090909090909091*Math.asin(Math.sqrt(
                sin_width*sin_width+Math.cos(n1.latitude)*Math.cos(n2.latitude)*sin_length*sin_length)));
    }
    /**
     * Method to format time
     * @param time time
     * @return String
     */
    public static String timeFormat(int time) {
        int hours = time/(60*60*100);
        time = time-(hours*60*60*100);
        int minutes = time/(60*100);
        time = time-(minutes*60*100);
        int seconds = time/(100);
        String timeString = hours + " hours, " + minutes + " minutes and  " + seconds + " seconds";
        return timeString;
    }

    
    /***********************************************
     * Used these methods to make the landmarks files
     ***********************************************/
    /**
     * Used to make the landmarks files
     * @param graph graph
     * @throws IOException IOException
     */
    public static void fileExists(Dijkstra_AStar graph) throws IOException {
        Dijkstra_AStar transposedGraph = new Dijkstra_AStar(true);
        graph.createTables("landmarks.csv");
        transposedGraph.createTables("landmarks-transposed.csv");
    }
    /**
     * Create tables with distances from landmarks to all nodes in the map
     * @param filename String
     */
    public void createTables(String filename) {
        ArrayList<String> filenames = new ArrayList<>();
        if(filename.equals("landmarks.csv")){
            filenames.add("landmarks1.csv");
            filenames.add("landmarks2.csv");
            filenames.add("landmarks3.csv");
            filenames.add("landmarks4.csv");
        } else{
            filenames.add("landmarks-transposed1.csv");
            filenames.add("landmarks-transposed2.csv");
            filenames.add("landmarks-transposed3.csv");
            filenames.add("landmarks-transposed4.csv");
        }

        for (int i = 0; i < 4; i++) {
            File landmarksT = new File(filenames.get(i));
        }

        int[] list1 = new int[nodeCount];
        int[] list2 = new int[nodeCount];
        int[] list3 = new int[nodeCount];
        int[] list4 = new int[nodeCount];

        for (int i = 0; i < nodeCount; i++) {
            list1[i] = Node.infinity;
            list2[i] = Node.infinity;
            list3[i] = Node.infinity;
            list4[i] = Node.infinity;
        }

        list1 = dijkstra(nodes[landmarks.get(0).node.value], list1);
        reset();
        list2 = dijkstra(nodes[landmarks.get(1).node.value], list2);
        reset();
        list3 = dijkstra(nodes[landmarks.get(2).node.value], list3);
        reset();
        list4 = dijkstra(nodes[landmarks.get(3).node.value], list4);
        reset();

        ArrayList<int[]> totalList = new ArrayList();

        totalList.add(list1);
        totalList.add(list2);
        totalList.add(list3);
        totalList.add(list4);

        for (int i = 0; i<4; i++) {
            try {
                BufferedWriter os = new BufferedWriter(new FileWriter(filenames.get(i)));
                    for (int j = 0; j<nodeCount; j++) {
                         os.write(totalList.get(i)[j]+" ");//\n
                    }
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}

/**
 * Landmark class
 */
class Landmark {
    Node node;
    int[] distanceFrom = new int[Dijkstra_AStar.nodeCount];
    int[] distanceTo = new int[Dijkstra_AStar.nodeCount];
    /**
     * Constructor
     * @param n Node
     */
    public Landmark(Node n){
        this.node  = n;
    }
}

/**
 * Node class
 */
class Node {
    Edge edge;
    int value;
    int driveTime;
    double distance;
    Node pred;
    static int infinity = 1000000000;
    double longitude;
    double latitude;
    int type;
    String name;
    double fullDistance;
    double distToEnd;
    boolean end;
    /**
     * Constructor
     * @param val int
     * @param longitude double
     * @param latitude double
     */
    public Node(int val, double longitude, double latitude) {
        value = val;
        driveTime = infinity;
        distance = infinity;
        fullDistance = infinity;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distToEnd = -1;
    }
    /**
     * Get coordinates
     * @return String
     */
    public String getCoordinates() {
        return this.longitude*(180/Math.PI)+","+this.latitude*(180/Math.PI);
    }
    /**
     * toString method
     * @return String
     */
    @Override
    public String toString() {
        return this.name+" "+this.type;
    }
}

/**
 * Edge class
 */
class Edge {
    Edge next;
    Node to;
    int weight;
    int speedLimit;
    int length;
    /**
     * Constructor
     * @param t Node
     * @param nxt Edge
     * @param w int
     * @param s int
     * @param l int
     */
    public Edge(Node t, Edge nxt, int w, int s, int l) {
        to = t;
        next = nxt;
        weight = w;
        this.speedLimit = s;
        this.length = l;
    }
    /**
     * toString method
     * @return String
     */
    @Override
    public String toString() {
        return to+ " Weight: "+weight;
    }
}


