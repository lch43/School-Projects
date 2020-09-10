//LCH43
/*************************************************************************
 *  Compilation:  javac MyLZW.java
 *  Execution:    java LZW - r < input.txt > output.lzw   (compress reset mode)
 *  Execution:    java LZW - m < input.txt > output.lzw   (compress monitor mode)
 *  Execution:    java LZW - n < input.txt > output.lzw   (compress do nothing mode)
 *  Execution:    java LZW + < output.lzw > inputCopy.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java Queue.java StdIn.java StdOut.java TST.java
 *
 *  Compress or expand binary input from standard input using MyLZW.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static final int MIN = 9;                  // 
    private static final int MAX = 16;                 // 
    private static int W = MIN;                        // codeword width
    private static int L = (int) Math.pow(2, W);       // number of codewords = 2^W
    private static char cmode = 'n';
    private static int touched = 0;
    private static int untouched = 0;
    private static double oldc = 0.0;
    private static double newc = 0.0;

    public static void compress() { 
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
        {
            st.put("" + (char) i, i);
        }
        int code = R+1;  // R is codeword for EOF



        BinaryStdOut.write(cmode); //Write the code out for whenever we want to expand



        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            touched = touched + W;
            untouched = t * 8;
            if (t < input.length())    // Add s to symbol table.
            {
                if (code < L)
                {
                    st.put(input.substring(0, t + 1), code++);
                    oldc = (double) untouched/touched;
                }
                else
                {
                    if (W < MAX)
                    {
                        W++;
                        L = (int) Math.pow(2,W);
                        st.put(input.substring(0, t + 1), code++);
                        oldc = (double) untouched/touched;
                    }
                    else if (cmode == 'r') // We will reset once the number of codewords has been maxxed and the width has been maxxed and we want to add a new codeword.
                    {
                        W = MIN;
                        L = (int) Math.pow(2,W);
                        st = new TST<Integer>();
                        for (int i = 0; i < R; i++)
                        {
                            st.put("" + (char) i, i);
                        }
                        code = R+1;
                        st.put(input.substring(0, t + 1), code++);
                    }
                    else if (cmode == 'm')
                    {
                        newc = (double) untouched/touched;
                        if ((double) oldc/newc > 1.1)
                        {
                            W = MIN;
                            L = (int) Math.pow(2,W);
                            st = new TST<Integer>();
                            for (int i = 0; i < R; i++)
                            {
                                st.put("" + (char) i, i);
                            }
                            code = R+1;
                            st.put(input.substring(0, t + 1), code++);
                        }
                    }
                    else //If this is do nothing mode then we will do nothing. Simple as that.
                    {

                    }
                }
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        String[] st = new String[(int) Math.pow(2, MAX)];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
        {
            st[i] = "" + (char) i;
        }
        st[i++] = "";                        // (unused) lookahead for EOF

        cmode = BinaryStdIn.readChar();
        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R)
        {
            return;           // expanded message is empty string
        }
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            touched = touched + W;
            untouched = val.length() * 8;
            if (i >= L)
            {
                if (W < MAX)
                {
                    W++;
                    L = (int) Math.pow(2, W);
                    
                    oldc = (double) untouched/touched;
                }
                else if (cmode == 'r')
                {
                    st = new String[(int) Math.pow(2, MAX)];
                    for (i = 0; i < R; i++)
                    {
                        st[i] = "" + (char) i;
                    }
                    st[i++] = "";                        // (unused) lookahead for EOF

                    W = MIN;
                    L = (int) Math.pow(2, W);
                }
                else if (cmode == 'm')
                {
                    newc = (double) untouched/touched;
                    if (oldc/newc > 1.1)
                    {
                        st = new String[(int) Math.pow(2, MAX)];
                        for (i = 0; i < R; i++)
                        {
                            st[i] = "" + (char) i;
                        }
                        st[i++] = "";                        // (unused) lookahead for EOF

                        W = MIN;
                        L = (int) Math.pow(2, W);
                        oldc = 0.0;
                        newc = 0.0;
                    }
                }
                else{}
            }
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R)
            {
                break;
            }
            String s = st[codeword];
            if (i == codeword)
            {
                s = val + val.charAt(0);   // special case hack
            } 
            if (i < L)
            {
                st[i++] = val + s.charAt(0);
                oldc = (double) untouched/touched;
            }
            val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if (args.length > 1)
        {
            if ((cmode == 'n' || cmode == 'r' || cmode == 'm') && args[1].length() > 0)
            {
                cmode = args[1].charAt(0);
            }
        }

        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
