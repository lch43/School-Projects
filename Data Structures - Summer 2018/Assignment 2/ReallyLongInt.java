
/** A partial implementation of the ReallyLongInt class.
 * @author Sherif Khattab (Adapted  from Dr. John Ramirez's Spring 2017 CS 0445 Assignment 2 code)
 * You need to complete the implementations of the remaining methods.  Also, for this class
 *  to work, you must complete the implementation of the ArrayDS class. See additional comments below.
 */

 //Modified by Landon Higinbotham

public class ReallyLongInt extends LinkedDS<Integer> 
implements Comparable<ReallyLongInt>
{
	// Instance variables are inherited.  You may not add any new instance variables

	public ReallyLongInt() {
		super();
	}

	/**
	 * @param s a string representing the integer (e.g., "123456") with no leading 
	 * zeros except for the special case "0".
	 * 	Note that we are adding the digits here to the END. This results in the 
	 * MOST significant digit first. It is assumed that String s is a valid representation
	 * of an unsigned integer with no leading zeros.
	 */
	public ReallyLongInt(String s)
	{
		super();
		char c;
		Integer digit;
		// Iterate through the String, getting each character and converting it into
		// an int.  Then add at the end.  Note that
		// the addItem() method (from LinkedDS) adds at the end.
		for (int i = 0; i < s.length(); i++)
		{
			c = s.charAt(i);
			if (('0' <= c) && (c <= '9'))
			{
				digit = c - '0';
				addItem(digit);
			}
			else 
				throw new NumberFormatException("Illegal digit " + c);
		}
	}
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (Node curr = firstNode; curr != null; curr = curr.getNextNode())
		{
			b.append(curr.getData().toString());
		}
		return b.toString();
	}

	/** Simple call to super to copy the items from the argument ReallyLongInt into a new one.
	 * @param rightOp the object to copy
	 */
	public ReallyLongInt(ReallyLongInt rightOp)
	{
		super(rightOp);
	}

	// You must implement the methods below.  See the descriptions in the
	// assignment sheet

	public ReallyLongInt add(ReallyLongInt rightOp) 	{	
		ReallyLongInt sum = new ReallyLongInt();
		ReallyLongInt larger, smaller;
		Boolean carryInt = false;

		if(compareTo(rightOp)<=0)	//Checks to see which value is larger
		{
			larger = new ReallyLongInt(rightOp);
			smaller = new ReallyLongInt(this);
		}
		else
		{
			larger = new ReallyLongInt(this);
			smaller = new ReallyLongInt(rightOp);
		}

		larger.reverse();
		smaller.reverse();

		while(larger.size()>0)	//Repeats as long as the larger value still has values
		{
			int sumInt = 0;

			if(0<smaller.size())	//Adds the smaller value to the larger if there is a smaller value
			{
				sumInt = larger.removeItem()+smaller.removeItem();
			}
			else	//If there isn't a smaller
			{
				sumInt = larger.removeItem();
			}

			if(carryInt)	//If at the point before there was a number greater than 9, we will carry the value overflow place value
			{
				sumInt++;
				carryInt = false;
			}

			if(sumInt>9)	//If we have overflow, we will take care of that here.
			{
				sumInt = sumInt-10;
				carryInt = true;
			}
			sum.addItem(sumInt);
		}

		if(carryInt)	//If we had overflow after the loop, we will add it here.
		{
			sum.addItem(1);
		}

		sum.reverse();	//Put the sum back in the right order.
		return sum;
	}

	public ReallyLongInt subtract(ReallyLongInt rightOp)
	{	
		ReallyLongInt difference = new ReallyLongInt();
		ReallyLongInt first, second;
		Boolean carryInt = false;
		first = new ReallyLongInt(this);
		second = new ReallyLongInt(rightOp);

		if(compareTo(rightOp)>=0)	//Checks to see if the rightOp is smaller or equal to this.
		{
			first.reverse();
			second.reverse();

			while(second.size()>0)	//While there are still values from righOp to subtract, we will subtract it from this.
			{
				int difInt = first.removeItem()-second.removeItem();
				
				if(carryInt)	//Checks to see if we need to carry any values from previous iterations.
				{
					difInt--;
					carryInt = false;
				}

				if(difInt<0)	//If a number is less than 0 then we need to borrow 10 from the higher place values
				{
					difInt = difInt+10;
					carryInt = true;
				}
				difference.addItem(difInt);
			}
			while(first.size()>0)	//After rightOp runs out, we will just carry over the rest of this.
			{
				int difInt = first.removeItem();
				
				if(carryInt)
				{
					difInt--;
					carryInt = false;
				}

				if(difInt<0)
				{
					difInt = difInt+10;
					carryInt = true;
				}
				difference.addItem(difInt);
			}

			difference.reverse();

			while(difference.size()>1&&difference.head()==0)	//We remove any header zeros here.
			{
				difference.removeItem();
			}
		}
		else	//If rightOp is greater than this, we ill throw an ArithmeticException
		{
			throw new ArithmeticException("Invalid Difference -- Negative Number");
		}
		

		return difference;
	}


	public int compareTo(ReallyLongInt rOp)
	{
		int equalityResult = 0;

		if(size()==rOp.size())	//If two ReallyLongInts are the same size, we will test to find which one is smaller, larger, or if they are equal.
		{
			boolean equality = true;
			ReallyLongInt first = new ReallyLongInt(this);
			ReallyLongInt second = new ReallyLongInt(rOp);

			while(equality&&first.size()>0&&second.size()>0)	//Iterates through the ReallyLongInts finding the first unequal value, if there is.
			{
				int firstInt = first.removeItem();
				int secondInt = second.removeItem();
				if(firstInt!=secondInt)	//If you find an unequal value, test which ReallyLongInt was lower.
				{
					equality = false;
					if(firstInt>secondInt)
					{
						equalityResult = 1;
					}
					else
					{
						equality = false;
						equalityResult = -1;
					}
				}
			}
			if(equality==true)
			{
				equalityResult = 0;
			}
		}
		else if(this.size()>rOp.size())	//If this is larger in size, then it is a larger number.
		{
			equalityResult = 1;
		}
		else	//Else it is a smaller number.
		{
			equalityResult = -1;
		}

		return equalityResult;
	}

	public boolean equals(Object rightOp)	//Checks with compareTo to see if two objects are equal.
	{
		return compareTo((ReallyLongInt)rightOp)==0;
	}

	public ReallyLongInt multTenToThe(int num)
	{
		ReallyLongInt mult = new ReallyLongInt(this);

		for(int i=num;i>0;i--)	//Adds num number of zeros to the end of the ReallyLongInt
		{
			mult.addItem(0);
		}

		return mult;
	}

	public ReallyLongInt divTenToThe(int num)
	{
		ReallyLongInt div = new ReallyLongInt(this);

		div.reverse();	//Flips beginning to end

		for(int i=num;i>0;i--)	//Removes the end numbers from the beginning
		{
			div.removeItem();
		}

		div.reverse();	//Flips back to original order

		return div;
	}

	public ReallyLongInt multiply(ReallyLongInt multiplier)	//Extra Credit-Use MultiplyTest.java to test!
	{
		ReallyLongInt product = new ReallyLongInt();
		ReallyLongInt temp = new ReallyLongInt();
		ReallyLongInt larger = null;
		ReallyLongInt smaller = null;
		boolean largerSmaller = false;
		
		if(compareTo(multiplier)>=0)	//Determines the greater between this and multiplier.
		{
			largerSmaller = true;
		}
		else
		{
			largerSmaller = false;
		}

		if(largerSmaller)	//Based on which is greater, it sets the values of larger and smaller.
		{
			larger = new ReallyLongInt(this);
			smaller = new ReallyLongInt(multiplier);
		}
		else
		{
			larger = new ReallyLongInt(multiplier);
			smaller = new ReallyLongInt(this);
		}

		int nextPlace = 0;
		int addZeros = 0;
		boolean started = false;

		for(int i=smaller.size();i>0;i--)	//Iterates through the smaller numbers
		{
			if(largerSmaller)	//Resets larger and smaller
			{
				larger = new ReallyLongInt(this);
				smaller = new ReallyLongInt(multiplier);
			}
			else
			{
				larger = new ReallyLongInt(multiplier);
				smaller = new ReallyLongInt(this);
			}
			
			larger.reverse();
			smaller.reverse();

			for(int k=addZeros;k>0;k--)	//Adds the appropriate amount of zeros, based on which place value you are multiplying from in smaller
			{
				temp.addItem(0);
				smaller.removeItem();
			}

			int smallerHold = smaller.removeItem();	//Keeps the smaller number since we are using .removeiTEM()

			for(int j=larger.size();j>0;j--)	//Multiplies through the larger number by smallerHold
			{
				int tempProduct = larger.removeItem()*smallerHold;
				tempProduct = tempProduct+nextPlace;
				nextPlace = 0;
				while(tempProduct>9)	//Checking to make sure we appropriately place the place values of the product.
				{
					tempProduct = tempProduct-10;
					nextPlace++;
				}
				temp.addItem(tempProduct);
			}
			if(nextPlace!=0)	//If we have any overflow place values that we haven't gotten to add, we will do it here.
			{
				temp.addItem(nextPlace);
				nextPlace = 0;
			}
			temp.reverse();
			if(started==true)	//If we already started we will add the temp to the prodcut.
			{
				product = product.add(temp);
			}
			else	//If its the first time, we will set product equal to temp to lay a foundation.
			{
				product = temp;
			}
			started = true;
			temp = new ReallyLongInt();
			addZeros++;
		}

		while(product.size()>1&&product.head()==0)	//We will remove any heading zeros, since we are adding the numbers backwards and flipping.
		{
			product.removeItem();
		}

		return product;
	}
}
