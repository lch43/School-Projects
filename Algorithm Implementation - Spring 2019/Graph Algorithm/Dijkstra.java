//Landon Higinbotham
//LCH43
    
public class Dijkstra {
    private Edge[] edgeTo; //Shortest edge from tree vertex to non-tree vertex
    private double[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;
    private int s;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    /*public Dijkstra(EdgeWeightedGraph G, int s) {
        this.s = s;
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        dijkstra(G, s);
    }*/

    public Dijkstra(Edge[] G, int s)
    {
        this.s = s;
        edgeTo = new Edge[G.length];
        distTo = new double[G.length];
        marked = new boolean[G.length];
        pq = new IndexMinPQ<Double>(G.length);
        for (int v = 0; v < G.length; v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        dijkstra(G, s);
    }

    // run Dijkstra's algorithm in graph G, starting from vertex s
    private void dijkstra(Edge[] G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    /*private void scan(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;         // v-w is obsolete edge
            if (distTo[v] + e.weight() < distTo[w]) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }*/

    private void scan(Edge[] G, int v) {
        //System.out.println("Scan "+v);
        marked[v] = true;
        Edge e = G[v];
        while (e != null)
        {
            //System.out.println(e.toString());
            int w = e.other(v);
            if (marked[w])
            {
                e = e.next;
                continue;         // v-w is obsolete edge
            }
            if (distTo[v] + e.latency() < distTo[w]) {
                distTo[w] = distTo[v] + e.latency();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
            e = e.next;
        }
    }


    public String toString(int d) {
        if (distTo[d] == Double.POSITIVE_INFINITY)
        {
            return "There is no possible path from "+s+" to "+d;
        }
        
        String st = "The lowest latency path from "+s+" to "+d+" is: ";
        int via = d;
        String[] path = new String[edgeTo.length];
        int i = edgeTo.length-1;
        int bandwidth = -1;
        while (via != s && i > 0) {
            int c = via;
            Edge e = edgeTo[via];
            if(e == null)continue;
            if (bandwidth == -1 || e.bandwidth() < bandwidth)
            {
                bandwidth = e.bandwidth();
            }
            int v1 = e.either();
            int v2 = e.other(v1);
            //System.out.println(v1+" "+v2);
            via = v1;
            if(v1 == c) {
                via = v2;
            }
            path[i] = "->"+c;
            //System.out.println(path[i]);
            i--;
        }
        path[0] = s+"";
        for (int j=0; j<path.length;j++)
        {
            if (path[j] == null || path[j] == "")
            {

            }
            else
            {
                st = st+path[j];
            }
        }
        double dist = distTo[d];
        st = st+"\nLatency: "+dist;
        if (bandwidth == -1 && s == d)
        {
            st = st+"\nBandwidth: Indeterminable due to the starting vertex being the ending vertex.";
        }
        else
        {
            st = st+"\nBandwidth: "+bandwidth;
        }
        return st;
    }

    /*public static void main(String[] args) {
        EdgeWeightedGraph graph = new EdgeWeightedGraph(5);
        graph.addEdge(new Edge(0, 1, 500));
        graph.addEdge(new Edge(0, 2, 3));
        graph.addEdge(new Edge(2, 4, 5));
        graph.addEdge(new Edge(3, 2, 1));
        graph.addEdge(new Edge(3, 1, 7));
        graph.addEdge(new Edge(0, 3, 4));
        Dijkstra p = new Dijkstra(graph, 0);
        System.out.println(p);
        System.out.println("------------");
        graph = new EdgeWeightedGraph(5);
        graph.addEdge(new Edge(0, 1, 1));
        graph.addEdge(new Edge(1, 2, 1));
        graph.addEdge(new Edge(2, 3, 1));
        graph.addEdge(new Edge(3, 4, 1));
        graph.addEdge(new Edge(0, 4, 2));
        p = new Dijkstra(graph, 0);
        System.out.println(p);
    }*/
}