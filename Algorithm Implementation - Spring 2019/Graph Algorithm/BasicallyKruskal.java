//Landon Higinbotham
//LCH43

import java.util.NoSuchElementException;
public class BasicallyKruskal
{
    IndexMinPQ<Edge> edges;
    Edge[] mst;
    int iterate = 0;

    public BasicallyKruskal(Edge[] G, int e)
    {
        //System.out.println(e);
        edges = new IndexMinPQ<Edge>(e*2);
        mst = new Edge[G.length-1];
        int key = 1;
        for (int i=0; i<G.length; i++)
        {
            Edge check = G[i];
            while(check != null)
            {
                //System.out.println(check.toString()+"        "+key);
                edges.insert(key++, check);
                check = check.next;
            }
        }

        while (iterate < mst.length)
        {
            try
            {
                Edge check = edges.minKey();
                if (check != null)
                {
                    //System.out.println(check.toString());
                    if (cycleCheck(check))
                    {
                        mst[iterate++] = check;
                    }
    
                    edges.delMin();
                }
            }
            catch (NoSuchElementException exc)
            {
                System.out.println("There are vertices that can not be reached.");
                break;
            }
            
        }
    }


    private boolean cycleCheck(Edge check)
    {
        boolean a = false;
        boolean b = false;
        for (int i = 0; i<iterate; i++)
        {
            int v1 = check.either();
            int v2 = mst[i].either();
            int w1 = check.other(v1);
            int w2 = mst[i].other(v2);

            if (v1 == v2 || v1 == w2)
            {
                a = true;
            }

            if (w1 == v2 || w1 == w2)
            {
                b = true;
            }

            if (a && b)
            {
                return false;
            }
        }
        return true;
    }

    public String toString()
    {
        int div = 0;
        double latency = 0;
        String edges = "";
        for (int i=0; i<iterate; i++)
        {
            latency = latency + mst[i].latency();
            div++;
            if (i == iterate - 1)
            {
                edges = edges+mst[i].toString();
            }
            else
            {
                edges = edges+mst[i].toString()+"\n";
            }
        }
        latency = latency/div;
        String s = "The Lowest Average Latency Spanning Tree has an average edge latency of "+latency+" and contains the edges:\n"+edges;
        return s;
    }
}