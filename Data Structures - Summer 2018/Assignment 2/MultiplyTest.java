/*
 * This program must work as is with your ReallyLongInt class.  Look carefully at all of the method calls so that
 *  you create your ReallyLongInt methods correctly.  For example, note the constructor calls and the toString() method call. 
 *  The output should be identical to my sample output. This will be verified by running the diff program (fc on Windows) between
 *  the provided RLITest.txt and the output of running this driver using your ReallyLongInt implementation. The diff program shouldn't 
 *  give any output.
 */
import java.util.*;

public class MultiplyTest
{
	public static void main(String[]args)
	{
		int a, b;
		System.out.println("Testing Multiply");

		a = 0;
		b = 20;
		System.out.println("Expected Outcome: "+a+"*"+b+" = "+a*b);
		ReallyLongInt aInt1 = new ReallyLongInt(""+a);
		ReallyLongInt bInt1 = new ReallyLongInt(""+b);
		System.out.println("Results achieved: "+aInt1.toString()+"*"+bInt1.toString()+" = "+aInt1.multiply(bInt1));
		System.out.println();

		
		a = 9;
		b = 9;
		System.out.println("Expected Outcome: "+a+"*"+b+" = "+a*b);
		ReallyLongInt aInt2 = new ReallyLongInt(""+a);
		ReallyLongInt bInt2 = new ReallyLongInt(""+b);
		System.out.println("Results achieved: "+aInt2.toString()+"*"+bInt2.toString()+" = "+aInt2.multiply(bInt2));
		System.out.println();

		a = 237;
		b = 99;
		System.out.println("Expected Outcome: "+a+"*"+b+" = "+a*b);
		ReallyLongInt aInt5 = new ReallyLongInt(""+a);
		ReallyLongInt bInt5 = new ReallyLongInt(""+b);
		System.out.println("Results achieved: "+aInt5.toString()+"*"+bInt5.toString()+" = "+aInt5.multiply(bInt5));
		System.out.println();

		
		String ab = "9999999999999999";
		String ba = "1";
		System.out.println("Expected Outcome: "+ab+"*"+ba+" = 9999999999999999");
		ReallyLongInt aInt3 = new ReallyLongInt(""+ab);
		ReallyLongInt bInt3 = new ReallyLongInt(""+ba);
		System.out.println("Results achieved: "+aInt3.toString()+"*"+bInt3.toString()+" = "+aInt3.multiply(bInt3));
		System.out.println();

		ab = "123456789";
		ba = "987654321";
		System.out.println("Expected Outcome: "+ab+"*"+ba+" = 121932631112635269");
		ReallyLongInt aInt4 = new ReallyLongInt(""+ab);
		ReallyLongInt bInt4 = new ReallyLongInt(""+ba);
		System.out.println("Results achieved: "+aInt4.toString()+"*"+bInt4.toString()+" = "+aInt4.multiply(bInt4));
		System.out.println();

	}
}
