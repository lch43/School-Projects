//Landon Higinbotham
//LCH43

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;

public class NetworkAnalysis
{
    static File file;
    static Scanner fileScanner;
    static Scanner input;
    static int numV = 0;
    static Edge[] adjacencyList;
    static boolean copperTested = false;
    static boolean[] copper;
    static boolean containsoptic;
    static String copperlessvertices = "";
    static boolean lostvertex;
    static int[] lostvertices;
    static int nextlostvertexindex = 0;
    static int edges = 0;

    public static void main(String[]args)
    {
        input = new Scanner(System.in);
        if (args.length >= 1)
        {
            try
            {
                file = new File(args[0]);
                fileScanner = new Scanner(file);
                scanFile();
                prompt();
                fileScanner.close();
            }
            catch(FileNotFoundException c)
            {
                System.out.println("File could not be found");
            }
        }
        else
        {
            System.out.println("Please run the program again with the file name as the first and only command line argument.");
            System.out.println("Goodbye");
        }
        input.close();
    }

    public static void scanFile()
    {
        if (fileScanner.hasNext())
        {
            numV = Integer.parseInt(fileScanner.nextLine());
            adjacencyList = new Edge[numV];
        }
        while (fileScanner.hasNext())
        {
            edges = edges + 2;
            String[] edgeInfo = fileScanner.nextLine().split(" ");
            for (int i=0; i<2; i++)
            {
                if (adjacencyList[Integer.parseInt(edgeInfo[i])] == null)
                {
                    if (i == 1)
                    {
                        adjacencyList[Integer.parseInt(edgeInfo[i])] = new Edge(Integer.parseInt(edgeInfo[1]), Integer.parseInt(edgeInfo[0]), edgeInfo[2], Integer.parseInt(edgeInfo[3]), Integer.parseInt(edgeInfo[4]));
                    }
                    else
                    {
                        adjacencyList[Integer.parseInt(edgeInfo[i])] = new Edge(Integer.parseInt(edgeInfo[0]), Integer.parseInt(edgeInfo[1]), edgeInfo[2], Integer.parseInt(edgeInfo[3]), Integer.parseInt(edgeInfo[4]));
                    }
                }
                else
                {
                    Edge check = adjacencyList[Integer.parseInt(edgeInfo[i])];
                    while(check.next != null)
                    {
                        check = check.next;
                    }

                    if (i == 1)
                    {
                        check.next = new Edge(Integer.parseInt(edgeInfo[1]), Integer.parseInt(edgeInfo[0]), edgeInfo[2], Integer.parseInt(edgeInfo[3]), Integer.parseInt(edgeInfo[4]));
                    }
                    else
                    {
                        check.next = new Edge(Integer.parseInt(edgeInfo[0]), Integer.parseInt(edgeInfo[1]), edgeInfo[2], Integer.parseInt(edgeInfo[3]), Integer.parseInt(edgeInfo[4]));
                    }
                }
            }
        }

        /*for (int i=0; i<adjacencyList.length; i++)
        {
            Edge check = adjacencyList[i];
            while(check != null)
            {
                System.out.print(check.toString()+" ");
                check = check.next;
            }
            System.out.println();
        }*/
    }

    public static void prompt()
    {
        boolean continu = true;
        while (continu)
        {
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Please enter the number corresponding to your choice below.");
            System.out.println();
            System.out.println("1: Find the lowest latency path between any two points.");
            System.out.println("2: Determine whether or not the graph is copper-only connected, or whether it is connected considering only copper links.");
            System.out.println("3: Find the lowest average latency spanning tree for the graph.");
            System.out.println("4: Determine whether or not the graph would remain connected if any two vertices in the graph were to fail.");
            System.out.println("5: Quit the program.");
            String choice = input.nextLine();
            if (choice.equals("1"))
            {
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
                choice1();
            }
            else if(choice.equals("2"))
            {
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
                choice2();
            }
            else if(choice.equals("3"))
            {
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
                choice3();
            }
            else if(choice.equals("4"))
            {
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
                choice4();
            }
            else if(choice.equals("5"))
            {
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
                continu = false;
            }
        }
    }

    /*
        Find the lowest latency path between any two points, and give the bandwidth available along that path.

        First, your program should prompt the user for the two vertices that they wish to find the lowest latency path between.

        Then, your program should output the edges that comprise this lowest latency path in order from the first user-specified vertex to the second.

        You must find the path between these vertices that will require the least amount of time for a single data packet to travel. For this project,
            we will simply compute the time required to travel along a path through the graph as the sum of the times required to travel each edge, 
            where the time to travel each edge is computed as the length of the cable represented by that edge divided by the speed at which data can 
            be send along a connection of that type.

        A single data packet can be sent along a copper cable at a speed of 230,000,000 meters per second.

        A single data packet can be sent along a fiber optic cable at a speed of 200,000,000 meters per second.

        Your program should also output the bandwidth that is available along the resulting path (minimum bandwidth 
            of all edges in the path).
    */

    public static void choice1()
    {
        int vertex1 = -1;
        int vertex2 = -1;

        while (vertex1 < 0 || vertex1 > numV-1)
        {
            System.out.println("Please enter the number of the first vertex (0-"+(numV-1)+")");
            try
            {
                vertex1 = Integer.parseInt(input.nextLine());
            }
            catch(NumberFormatException c)
            {
                vertex1 = -1;
            }
        }
        while (vertex2 < 0 || vertex2 > numV-1)
        {
            System.out.println("Please enter the number of the second vertex (0-"+(numV-1)+")");try
            {
                vertex2 = Integer.parseInt(input.nextLine());
            }
            catch(NumberFormatException c)
            {
                vertex2 = -1;
            }
        }
        Dijkstra lowLatency = new Dijkstra(adjacencyList, vertex1);
        System.out.println(lowLatency.toString(vertex2));
    }

    /*
        Determine whether or not the graph is copper-only connected, or whether it is connected considering only copper links (i.e., ignoring fiber 
            optic cables).
    */

    public static void choice2()
    {
        if (copperTested == false)
        {
            copper = new boolean[adjacencyList.length];
            lostvertices = new int[adjacencyList.length];
            for (int i=0; i<adjacencyList.length; i++)
            {
                Edge check = adjacencyList[i];
                if (check == null)
                {
                    lostvertex = true;
                    lostvertices[nextlostvertexindex++] = i;
                }
                while (check != null)
                {
                    if (check.type().equals("copper"))
                    {
                        copper[i] = true;
                    }
                    else if(check.type().equals("optical"));
                    {
                        containsoptic = true;
                    }
                    check = check.next;
                }
            }
            for (int i=0; i<copper.length; i++)
            {
                if (copper[i] == false)
                {
                    if (copperlessvertices == "")
                    {
                        copperlessvertices = i+"";
                    }
                    else
                    {
                        copperlessvertices = copperlessvertices+", "+i;
                    }
                }
            }

            copperTested = true;
        }


        if(containsoptic)
        {
            System.out.println("Considering all possible edges, not only copper, there are fiber optic edges.");
        }

        if(lostvertex)
        {
            System.out.print("Vertex/Vertices without any edge:");

            for (int i = 0; i<nextlostvertexindex; i++)
            {
                if (i == nextlostvertexindex - 1)
                {
                    System.out.print(lostvertices[i]);
                }
                else
                {
                    System.out.print(lostvertices[i]+",");
                }
            }

            System.out.println();

            System.out.print("Ignoring vertices without any edge, the graph");

            if (copperlessvertices.equals(""))
            {
                System.out.println(" can be connected using only copper links.");
            }
            else
            {
                System.out.println(" can not be connected using only copper links.");
            }

        }
        else if (!copperlessvertices.equals(""))
        {
            System.out.println("The graph can not be connected using only copper links.");
        }
        else
        {
            System.out.println("The graph can be connected using only copper links.");
        }
    }

    /*
        Find the lowest average latency spanning tree for the graph (i.e., a spanning tree with the lowest average latency per edge).
    */

    public static void choice3()
    {
        BasicallyKruskal krusk = new BasicallyKruskal(adjacencyList, edges);
        System.out.println(krusk.toString());
    }

    /*
        Determine whether or not the graph would remain connected if any two vertices in the graph were to fail.

        Note that you are not prompting the users for two vertices that could fail, you will need to determine 
            whether the failure of any pair of vertices would cause the graph to become disconnected.
    */

    public static void choice4()
    {
        boolean willBreak = false;
        boolean[][] pairs = new boolean[adjacencyList.length][adjacencyList.length];
        for (int i = 0; i<adjacencyList.length; i++)
        {
            boolean[][] added = new boolean[adjacencyList.length-1][adjacencyList.length-1];
            Graph g = new Graph(adjacencyList.length-1);
            for (int j = 0; j<adjacencyList.length; j++)
            {
                if (j == i)
                {
                    j++;
                    if (j>=adjacencyList.length)
                    {
                        break;
                    }
                }

                Edge check = adjacencyList[j];

                while (check != null)
                {
                    int v1 = check.either();
                    int v2 = check.other(v1);

                    if (v1 != i && v2 != i)
                    {
                        if (v1>i)
                        {
                            v1--;
                        }
                        if (v2>i)
                        {
                            v2--;
                        }
                        int low = 0;
                        int high = 0;
                        if (v1 < v2)
                        {
                            low = v1;
                            high = v2;
                        }
                        else
                        {
                            low = v2;
                            high = v1;
                        }

                        if (!added[low][high])
                        {
                            g.addEdge(low, high);
                        }

                    }

                    check = check.next;
                }
            }
            Biconnected b = new Biconnected(g);
            if (b.hasArticulation)
            {
                willBreak = true;
                break;
            }
        }

        if (willBreak)
        {
            System.out.println("There EXISTS two vertices that if failed will disconnect the graph.");
        }
        else
        {
            System.out.println("There are NO two vertices that if failed will disconnecct the graph.");
        }
    }
}