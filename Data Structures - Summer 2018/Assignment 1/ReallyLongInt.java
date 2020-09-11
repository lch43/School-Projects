/** A partial implementation of the ReallyLongInt class.
 * @author Sherif Khattab (Adapted  from Dr. John Ramirez's Spring 2017 CS 0445 Assignment 2 code)
 * You need to complete the implementations of the remaining methods.  Also, for this class
 *  to work, you must complete the implementation of the ArrayDS class. See additional comments below.
 */
/**
 * @author Sherif Khattab
 *
 */
/**
 * @author Sherif Khattab
 *
 */


//Landon Higinbotham
//Added multiply function


public class ReallyLongInt extends ArrayDS<Integer> implements Comparable<ReallyLongInt>
{
	// Instance variables are inherited.  You may not add any new instance variables

	public ReallyLongInt(int size) {
		super(size);
		for (int i = 0; i < size; i++) {
			addItem(0);
		}
	}

	/**
	 * @param s a string representing the integer (e.g., "123456") with no leading zeros except for the special case "0"
	 * 	Note that we are adding the digits here to the END. This results in the 
	 * MOST significant digit first. It is assumed that String s is a valid representation of an
	 * unsigned integer with no leading zeros.
	 */
	public ReallyLongInt(String s)
	{
		super(s.length());
		char c;
		Integer digit;
		// Iterate through the String, getting each character and converting it into
		// an int.  Then add at the end.  Note that
		// the addItem() method (from ArrayDS) adds at the end.
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



	/** Simple call to super to copy the items from the argument ReallyLongInt into a new one.
	 * @param rightOp the object to copy
	 */
	public ReallyLongInt(ReallyLongInt rightOp)
	{
		super(rightOp);
	}

	/* (non-Javadoc)
	 * @see ArrayDS#toString()
	 * Method to put digits of number into a String.  Since the numbers are
	 * stored most significant digit first, we have to traverse the array from beginning to end.
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< count; i++) {
			sb.append(getItem(i));
		}
		return sb.toString();
	}

	// You must implement the methods below.  See the descriptions in the
	// assignment sheet

	public ReallyLongInt add(ReallyLongInt rightOp)
	{

		rightOp.removeZeros();	//Clears the array of extra zeros
		removeZeros();	//Clears the array of extra zeros
		ReallyLongInt larger = null;
		ReallyLongInt smaller = null;

		if (rightOp.size()>=size()) //Determines which ReallyLongInt is shorter and longer for easier addition.
		{
			larger = rightOp;
			smaller = this;
		} else {
			smaller = rightOp;
			larger = this;
		}

		int difference = larger.size()-smaller.size();	//Keeps track of how many more place values larger has than smaller
		ReallyLongInt temp = new ReallyLongInt(larger.size()+1);
		temp.clear();	//Clears the array for when we need to add the sum.
		boolean overTen = false;	//Allows us to see if we need to carry a one to the next place value

		
		for(int i=smaller.size()-1;i>=0;i--) //Iteratest through the array, adding until the smaller number has been fully added.
		{
			int sum = larger.getItem(i+difference)+smaller.getItem(i);
			if(overTen == true)
			{
				sum = sum+1;
				overTen = false;
			}
			if(sum>9)
			{
				sum = sum-10;
				overTen = true;
			}
			temp.addItem(sum);
		}

		for(int i=difference-1;i>=0;i--)	/*After the smaller number has been added, we will finish adding the
											numbers in the place values higher than what smaller had.*/
		{
			int valu = larger.getItem(i);
			if(overTen == true)
			{
				valu = valu+1;
				overTen = false;
			}
			if(valu>9)
			{
				valu = valu-10;
				overTen = true;
			}
			temp.addItem(valu);
		}

		if(overTen == true)	//Checks one last time for a one that needs to be carried.
		{
			temp.addItem(1);
		}
		temp.reverse();		//Since we added the item in reverse, we need to flip the array.
		return temp;

	}

	
	/** Remove leading zeros.
	 * 
	 */
	private void removeZeros()
	{
		while(count>1&&head()==0)	/*Checks to see if there is at least two items, if so it will remove any zeros before the first item.
									Rather than removing all leading zeros, we need to make sure we have one item left in the array
									ensuring we will show if a ReallyLongInt = 0, if need be.*/
		{
			removeItem();
		}
	}
	
	public ReallyLongInt subtract(ReallyLongInt rightOp)
	{	
		rightOp.removeZeros();	//Clears the array of extra zeros
		removeZeros();	//Clears the array of extra zeros

		int difference = size()-rightOp.size();
		ReallyLongInt temp = new ReallyLongInt(size());
		temp.clear();
		boolean underZero = false;

		if(compareTo(rightOp)==-1)	//Checks to see if the rightOp is negative, and if it is we will throw ArithmeticException
		{
			throw new ArithmeticException("Invalid Difference -- Negative Number");
		}
		else
		{
			for(int i=rightOp.size()-1;i>=0;i--) //The subtraction method acts similarly to the addition method.
			{
				int diff = getItem(i+difference)-rightOp.getItem(i);
				if(underZero == true)
				{
					diff = diff-1;
					underZero = false;
				}
				if(diff<0)
				{
					diff = diff+10;
					underZero = true;
				}
				temp.addItem(diff);
			}

			for(int i=difference-1;i>=0;i--)
			{
				int valu = getItem(i);
				if(underZero == true)
				{
					valu = valu-1;
					underZero = false;
				}
				if(valu<0)
				{
					valu = valu+10;
					underZero = true;
				}
			temp.addItem(valu);
			}
			temp.reverse();
			temp.removeZeros();
		}
		return temp;
		
	}

	public ReallyLongInt subtract(ReallyLongInt first, ReallyLongInt second) /*This method acts the same as the subtraction, though leaving
																			the option to decide the order of subtraction*/
	{	
		first.removeZeros();	//Clears the array of extra zeros
		second.removeZeros();	//Clears the array of extra zeros

		int difference = first.size()-second.size();
		ReallyLongInt temp = new ReallyLongInt(first.size());
		temp.clear();
		boolean underZero = false;

		if(first.compareTo(second)==-1)
		{
			throw new ArithmeticException("Invalid Difference -- Negative Number");
		}
		else
		{
			for(int i=second.size()-1;i>=0;i--)
		{
			int diff = first.getItem(i+difference)-second.getItem(i);
			if(underZero == true)
			{
				diff = diff-1;
				underZero = false;
			}
			if(diff<0)
			{
				diff = diff+10;
				underZero = true;
			}
			temp.addItem(diff);
		}

		for(int i=difference-1;i>=0;i--)
		{
			int valu = first.getItem(i);
			if(underZero == true)
			{
				valu = valu-1;
				underZero = false;
			}
			if(valu<0)
			{
				valu = valu+10;
				underZero = true;
			}
			temp.addItem(valu);
		}
		temp.reverse();
		temp.removeZeros();
		}
		return temp;
		
	}

	public int compareTo(ReallyLongInt rOp)
	{
		int result = 0;
		removeZeros();
		rOp.removeZeros();
		if(size()>rOp.size())	//If rOp has less items, it will have to be smaller
		{
			result = 1;
		}
		else if(size()<rOp.size())	//If rOp has more items, it will have to be larger
		{
			result = -1;
		}
		else	//Else we iterate through both to find which has a larger number occuring first
		{
			boolean found = false;
			for(int i=0;found==false&&i<count;i++)
			{
				if(getItem(i)>rOp.getItem(i))
				{
					result = 1;
					found = true;
				}
				else if(getItem(i)<rOp.getItem(i))
				{
					result = -1;
				}
			}
		}
		return result;
	}

	public boolean equals(Object rightOp)
	{
		return compareTo((ReallyLongInt) rightOp)==0;	//Using the compareTo we can easily determine if two ReallyLongInt's are equal.
	}

	public ReallyLongInt multTenToThe(int num)
	{
		ReallyLongInt temp = new ReallyLongInt(size()+num);	//Ensures the array is big enough.
		temp.clear();
		for(int i=0;i<count;i++)	//Iterates through and adds original numbers
		{
			temp.addItem(getItem(i));
		}
		for(int i=0;i<num;i++)		//Iterates through and adds num amount of zeros to the end, giving us 10^num times the original
		{
			temp.addItem(0);
		}
		return temp;
	}

	public ReallyLongInt divTenToThe(int num)	//Does the same as multTenToThe, except adds num items less, therefore dividing by 10^num
	{
		ReallyLongInt temp = new ReallyLongInt(size()-num);
		temp.clear();
		for(int i=0;i<count-num;i++)
		{
			temp.addItem(getItem(i));
		}
		return temp;
	}

	public ReallyLongInt multiply(ReallyLongInt multiple)	//Bonus: Use MultiplyTest.java to test.
	{
		multiple.removeZeros();	//Clears unwanted zeros from the beginning.
		removeZeros();
		ReallyLongInt larger = null;
		ReallyLongInt smaller = null;

		if (multiple.size()>=size())	//Checks to see which number is bigger to make it easier.
		{
			larger = multiple;
			smaller = this;
		} 
		else 
		{
			smaller = multiple;
			larger = this;
		}

		ReallyLongInt temp = new ReallyLongInt(larger.size()+smaller.size());	//Makes a temp array with the minimum required place values.
		temp.clear();
		int nextPlace = 0;
		int addZeros = 0;
		boolean started = false;
		
		for(int i=smaller.size()-1;i>=0;i--)	//Iterates the number being multiplied from the bottom.
		{
			ReallyLongInt temp2 = new ReallyLongInt(larger.size()+smaller.size());
			temp2.clear();
			for(int k=addZeros;k>0;k--)	//Adds zeros before higher place valued numbers are being multiplied on the bottom.
			{
				temp2.addItem(0);
			}
			for(int j=larger.size()-1;j>=0;j--)		//Iterates the number being multiplied from the top.
			{
				int product = larger.getItem(j)*smaller.getItem(i);
				product = product+nextPlace; 
				nextPlace = 0;
				while(product>9)
				{
					product = product-10;
					nextPlace++;
				}
				temp2.addItem(product);
			}
			if(nextPlace!=0)	//nextPlace is used to determine what numbers we need to carry over to the next iteration.
			{
				temp2.addItem(nextPlace);
				nextPlace = 0;
			}
			
			temp2.reverse();	//Since we add in the numbers backwards, we need to flip it around before adding it to temp.

			if(started==true)
			{
				temp = temp.add(temp2);
			}
			else
			{
				temp = temp2;	//Since we didn't have anything as temp before, we will make temp equal the first iteration 
			}
			started = true;		//Makes sure we don't make overwrite temp again after the first iteration.
			addZeros++;
		}
		temp.removeZeros();
		return temp;
	}
}
