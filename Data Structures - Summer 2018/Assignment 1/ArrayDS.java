//Landon Higinbotham
//Responsible for adding function to skeletons

import java.util.Random;



public class ArrayDS<T> implements PrimQ<T>, Reorder
{
	final protected T[] array;
	protected int count = 0;

	public ArrayDS(int capacity)	//Creates an array with a capacity of the int passed through it.
	{
		@SuppressWarnings("unchecked")
		T[] tempArray = (T[]) new Object[capacity];
		array = tempArray;
	}

	public ArrayDS(ArrayDS<T> other)	//Creates a copy of an ArrayDS passed through it.
	{
		array = other.array;
		count = other.count;
	}

		/** Add a new Object to the ArrayDS<T> in the next available location. 
	 * @param item the item to be added.
	 * 	@throws OutOfRoomException if there is no room in the ArrayDS for the new item  (you should NOT resize it)
	 */
	public void addItem(T item) throws OutOfRoomException
	{
		if(count<array.length)
		{
			array[count] = item;
			count++;
		}
		else	//If the array is full it will throw the out of room exception
		{
			throw new OutOfRoomException("no room");
		}
	}
	
	/** Remove and return the "oldest" item in the ArrayDS.
	 * @return the "oldest" item in the ArrayDS<T> object
	 * @throws EmptyQueueException if the queue is empty
	 */
	public T removeItem() throws EmptyQueueException
	{
		T returnValue = array[0];
		if(count>0)	//Checks to see if we have at least one item in the array.
		{
			for(int i = 1; i<count;i++)
			{
				array[i-1] = array[i];
			}
			if(count>0)
			{
				array[count-1] = null;
				count--;
			}
		}
		else //If we don't have one item in the array then we will throw an EmptyQueueException
		{
			throw new EmptyQueueException("no room");
		}
		
		return returnValue;
	}
	
	/**  
	 * @return the "oldest" item in the ArrayDS.
	 * @throws EmptyQueueException if the queue is empty
	 */
	public T head() throws EmptyQueueException	
	{
		if(count<=0)	//Checks to see if array is empty, and if it is it will throw EmptyQueueException
		{
			throw new EmptyQueueException("no room");
		}
		return array[0];
	}
		
	/**
	 * @return true if the ArrayDS is full, and false otherwise
	 */
	public boolean isFull()
	{
		return count==array.length;
	}
	
	/**
	 * @return true if the ArrayDS is empty, and false otherwise
	 */
	public boolean isEmpty()
	{
		boolean Empty;
		if (count==0)
		{
			Empty = true;
		}
		else
		{
			Empty = false;
		}
		return Empty;
	}
	
	/**
	 * @return the number of items currently in the ArrayDS
	 */
	public int size()
	{
		return count;
	}

	/** Reset the ArrayDS to empty status by reinitializing the variables 
	 * appropriately
	 */
	public void clear()
	{
		for(int i = 0; i<count;i++)	//Iterates through the array, setting values to null.
		{
			array[i] = null;
		}
		count = 0;
	}

	/** Logically reverse the data in the Reorder object so that the item
	 * that was logically first will now be logically last and vice
	 * versa.  The physical implementation of this can be done in 
	 * many different ways, depending upon how you actually implemented
	 * your physical ArrayDS<T> class
	 */
	public void reverse()
	{
		int j = 0;
		for(int i=count-1;i>=0;i--) //Goes through the array starting from the back, moving the items to a new array in a reverse order.
		{
			if(i>=j)
			{
				T temp = array[i];
				array[i] = array[j];
				array[j] = temp; //Sets the array to the flipped array.
				j++;
			}
		}
	}

	/** Remove the logically last item and put it at the 
	 * front.  As with reverse(), this can be done physically in
	 * different ways depending on the underlying implementation. 
	 */
	public void shiftRight()
	{
		T temp = array[count-1]; //Keeps track of the last item.
		for(int i=count-1;i>0;i--) //Moves all items, except for the last, to the right.
		{
			array[i] = array[i-1];
		}
		array[0] = temp; //Moves original last item to the front.
	}

	/** Remove the logically first item and put it at the
	 * end.  As above, this can be done in different ways.
	 */
	public void shiftLeft()
	{
		T temp = array[0]; //Keeps track of first item
		for(int i = 0; i<count-1;i++) //Moves all items, except for the first, to the left.
		{
			array[i] = array[i+1];
		}
		array[count-1] = temp; //Moves original first item to the last.
	}
	
	/** Shift the contents of the Reorder object num places to 
	 * 	the left, but rather than removing items, simply change 
	 * their ordering in a cyclic way.  For example, if an object has 8 items in it 
	 * numbered from 1 to 8, a leftRotate of 3 would shift items 1, 2 and 3 to the
	 * end, so that the old item 4 would now be item 1, and the old items 
	 * 1, 2 and 3 would now be items 6, 7 and 8 (in that order).  The rotation should
	 * work modulo the size of the object, so, for example, if the object is of length 8 then
	 * a leftRotate of 10 should be equivalent to a leftRotate of 2.  If num < 0, the 
	 * rotation should still be done but it will in fact be a right rotation rather than
	 * a left rotation.
	 * @param num the number of places to rotate to the left
	 */
	public void leftRotate(int num)
	{
		while(num>count||num<count*-1)	//This loop gets rid of unnecessary shifts, making the rest of the method more efficient.
		{
			if (num>count)
			{
				num = num-count;
			}
			else
			{
				num = num+count;
			}
		}
		if(num>=0) //If num is positive or zero, it will do num amounts of shiftLeft()s
		{
			for(int i = num; i>0; i--)
			{
				shiftLeft();
			}
		}
		else //If num is negative, it will do num amounts of shiftRight()s
		{
			for(int i = num*-1; i>0; i--)
			{
				shiftRight();
			}
		}
	}

	/** Same idea as leftRotate above, but in the opposite direction.  For example, if an object 
	 * has 8 items in it (numbered from 1 to 8), a rightRotate of 3 would shift items 8, 7 and 
	 * 6 to the beginning, so that the old item 8 would now be item 3, the old item 
	 * 7 would now be item 2 and the old item 6 would now be item 1.  The behavior for num > the
	 * number of items and for num < 0 should be analogous to that described above for leftRotate.
	 * @param num
	 */
	public void rightRotate(int num)
	{
		while(num>count||num<count*-1) //This gets rid of any unnecessary shifts.
		{
			if (num>count)
			{
				num = num-count;
			}
			else
			{
				num = num+count;
			}
		}
		if(num>=0) //If num is greater positive or zero, it will do num amounts of shiftRight()s
		{
			for(int i = num; i>0; i--)
			{
				shiftRight();
			}
		}
		else //Otherwise it will do num amounts of shiftLeft()s
		{
			for(int i = num*-1; i>0; i--)
			{
				shiftLeft();
			}
		}

	}
	
	/** Reorganize the items in the object in a pseudo-random way such that all permutations are equally likely.  
	 * Use Fisher–Yates shuffle Algorithm (http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm).
	 * Use a Random object (see java.util.Random in the Java API) initialized with seed. Thus, after this operation the logically 
	 * first item could be arbitrary.
	 * @param seed the random seed to pass to Random().
	 */
	public void shuffle(int seed)
	{
		Random random = new Random(seed);
		for(int i = count-1;i>=1;i--) //Uses the seed and the Fisher-Yates shuffle Algorithm to iterate through the array, swapping each index with one lower than it.
		{
			int j = random.nextInt(i);
			T temp = array[j];
			array[j] = array[i];
			array[i] = temp;
		}
	}

	public String toString()
	{
		String toString = "Contents:"+System.lineSeparator();	//Used lineSeperator to add a new line after Contents:
		if(array[0]!=null)
		{
			toString = toString+array[0].toString();
			for(int i = 1;i<count;i++)	//Iterates through the array indexes after 0, if any, and appends them to the string.
			{
				if(array[i]!=null)
				{
					toString = toString+" "+array[i].toString();
				}
				else
				{
					System.out.println("Index "+i+" is null");
				}
			}
		}
		return toString;
	}

	protected T getItem(int i)
	{
		if (i >= count)
		throw new IndexOutOfBoundsException();
		return array[i];
	}
	protected void setItem(int i, T value) 
	{
		if (i >= count)
		throw new IndexOutOfBoundsException();
		array[i] = value;
	}
		
}