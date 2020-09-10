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
 *************************************************************************

The files I created or modified are MyLZW.java and results.txt