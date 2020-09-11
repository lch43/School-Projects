/** 
 *
 * A linked implementation of the PrimQ<T> and Reorder interfaces.
 *  This implementation is mostly taken from the Carrano LList.  It is modified
 *  for Assignment 2.  This code MUST BE USED AS IS for Assignment 2.
 * @author Sherif Khattab (Adapted  from Dr. John Ramirez's Spring 2017 CS 0445 Assignment 2 code)
 *
 * @param <T> The type of entries stored in the LinkedDS
 * 
 */

//Modified by Landon Higinbotham

/**
   A linked implementation of the ADT list.
   @author Frank M. Carrano
   @version 2.0
 */
import java.util.Random;
public class LinkedDS<T> implements PrimQ<T>, Reorder
{
	protected Node firstNode; // reference to first node
	protected int  numberOfEntries; 

	public LinkedDS()
	{
		initializeDataFields();
	} // end default constructor

	// Return the number of items currently in the PrimQ
	public int size(){
		return numberOfEntries;	
	}

	// Reset the PrimQ to empty status by reinitializing the variables
	// appropriately
	public final void clear() // note the final method
	{
		initializeDataFields();
	} // end clear

	private void initializeDataFields() {
		firstNode = null;
		numberOfEntries = 0;
	}

	@Override
	public T removeItem() throws EmptyQueueException {
		T result = null;                           // return value

		if(!isEmpty()){
			result = firstNode.getData();        // save entry to be removed 
			firstNode = firstNode.getNextNode();
			numberOfEntries--;
		} else {
			throw new EmptyQueueException(
					"Trying to remove from an empty PrimQ.");
		}		

		return result;    // return removed entry
	}

	// Copy constructor.  This is a "deepish" copy so it will make new
	// Node objects for all of the nodes in the old list.  However, it
	// is not totally deep since it does NOT make copies of the objects
	// within the Nodes -- rather it just copies the references.
	public LinkedDS(LinkedDS<T> oldList)
	{
		if (oldList.size() > 0)
		{
			// Special case for first Node since we need to set the
			// firstNode instance variable.
			Node temp = oldList.firstNode;		// front of old list
			Node newNode = new Node(temp.data);	// copy the data
			firstNode = newNode;				// set front of new list

			// Now we traverse the old list, appending a new Node with
			// the correct data to the end of the new list for each Node
			// in the old list.  Note how the loop is done and how the
			// Nodes are linked.
			Node currNode = firstNode;
			temp = temp.next;
			while (temp != null)
			{
				currNode.next = new Node(temp.data);
				temp = temp.next;
				currNode = currNode.next;
			}
			numberOfEntries = oldList.numberOfEntries;
		}			
	}

	// Make a StringBuilder then traverse the nodes of the list, appending the
	// toString() of the data for each node to the end of the StringBuilder.
	// Finally, return the StringBuilder as a String.
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		for (Node curr = firstNode; curr != null; curr = curr.next)
		{
			b.append(curr.data.toString());
			b.append(" ");
		}
		return b.toString();
	}


	// Returns a reference to the node at a given position.
	// Precondition: List is not empty;
	//               1 <= givenPosition <= numberOfEntries	   
	private Node getNodeAt(int givenPosition)
	{
		assert !isEmpty() && (1 <= givenPosition) && (givenPosition <= numberOfEntries);
		Node currentNode = firstNode;

		// traverse the list to locate the desired node
		for (int counter = 1; counter < givenPosition; counter++)
			currentNode = currentNode.getNextNode();

		assert currentNode != null;

		return currentNode;
	} // end getNodeAt

	@Override
	public void addItem(T item)  {
		Node newNode = new Node(item);

		if (isEmpty())
			firstNode = newNode;
		else  // add to end of non-empty list
		{
			Node lastNode = getNodeAt(numberOfEntries);
			lastNode.setNextNode(newNode); // make last node reference new node
		} // end if	

		numberOfEntries++;		
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		boolean result;

		if (numberOfEntries == 0)
		{
			assert firstNode == null;
			result = true;
		}
		else
		{
			assert firstNode != null;
			result = false;
		} // end if

		return result;
	}

	@Override
	public void reverse() {
		
		// TODO Auto-generated method stub
		if(!isEmpty())	//Reverses only if list is not empty
		{
			for(int i = 1; i<numberOfEntries;i++)	//Iterates through the full list moving the item that is after the original firstNode to the front.
			{
				Node previousFirstNode = getNodeAt(i);
				Node newFirstNode = previousFirstNode.getNextNode();
				previousFirstNode.setNextNode(newFirstNode.getNextNode());
				newFirstNode.setNextNode(firstNode);
				firstNode = newFirstNode;
			}
		}

	}

	@Override
	public void shiftRight() {
		getNodeAt(numberOfEntries-1).setNextNode(null);		//Grabs the last Node and sets it to null
		numberOfEntries--;		//Changed numberOfEntries appropriately
	}

	@Override
	public void shiftLeft() {
		firstNode = firstNode.getNextNode();	//Sets the firstNode to the second, removing the first.
		numberOfEntries--;	//Chaned numberOfEntries appropriately
	}

	@Override
	public void leftRotate(int num) {
		// TODO Auto-generated method stub
		if(num>0)
		{
			if(num>size())
			{
				num = num-size();	//If the number is greater than the size then we will lower it to use less iterations
			}
			for(int i=0;i<num;i++)	//If the number is positive, left rotate
			{
				if(!isEmpty())	//Ensures it isn't empty
				{
				Node originalLast = getNodeAt(numberOfEntries);
				originalLast.setNextNode(firstNode);	//Moves the original firstNode to the end
				firstNode = firstNode.getNextNode();	//Sets the second node as the first
				originalLast.getNextNode().setNextNode(null);	//Points the new last node's next node to null
				}
			}
		}
		if(num<0)	//If the number is negative, right rotate
		{
			rightRotate(num*-1);
		}
	}

	@Override
	public void rightRotate(int num) {
		// TODO Auto-generated method stub
		if(num>0)
		{
			if(num>size())	//If the number is greater than the size then we will lower it to use less iterations
			{
				num = num-size();
			}
			for(int i=0;i<num;i++)
			{
				if(!isEmpty())
				{
					Node newLastNode = getNodeAt(numberOfEntries-1);
					newLastNode.getNextNode().setNextNode(firstNode);	//Sets the last node's next node to the original first Node
					firstNode = newLastNode.getNextNode();	//Makes the last node become the first
					newLastNode.setNextNode(null);	//Cuts ties from the original last node and new last node
				}
			}
		}
		else if(num<0)	//If the number is negative, left rotate.
		{
			leftRotate(num*-1);
		}
	}

	@Override
	public T head() throws EmptyQueueException {
		// TODO Auto-generated method stub
		return firstNode.data;
	}
	
	@Override
	public void shuffle(int seed) {
		// TODO Auto-generated method stub
		Random random = new Random(seed);
		for(int i = numberOfEntries;i>1;i--) //Uses the seed and the Fisher-Yates shuffle Algorithm to iterate through the array, swapping each index with one lower than it.
		{
			int j = random.nextInt(i);
			Node data1 = getNodeAt(i);
			Node data2 = getNodeAt(j);
			T temp = getNodeAt(i).getData();
			data1.setData(data2.getData());
			data2.setData(temp);

		}
	}
	
	@Override
	public void leftShift(int num) {
		// TODO Auto-generated method stub
		while(num>numberOfEntries||num<numberOfEntries*-1)	//This loop gets rid of unnecessary shifts, making the rest of the method more efficient.
		{
			if (num>numberOfEntries)
			{
				num = num-numberOfEntries;
			}
			else
			{
				num = num+numberOfEntries;
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

	@Override
	public void rightShift(int num) {
		// TODO Auto-generated method stub
		while(num>numberOfEntries||num<numberOfEntries*-1) //This gets rid of any unnecessary shifts.
		{
			if (num>numberOfEntries)
			{
				num = num-numberOfEntries;
			}
			else
			{
				num = num+numberOfEntries;
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

	// Note that this class is protected so you can access it directly from
	// your LinkedDS and ReallyLongInt classes.  However, in case you
	// prefer using accessors and mutators, those are also provided here.
	protected class Node
	{
		private T data; 	// entry in list
		private Node next; 	// link to next node

		protected Node(T dataPortion)
		{
			this(dataPortion, null);
		} // end constructor

		protected Node(T dataPortion, Node nextNode)
		{
			data = dataPortion;
			next = nextNode;
		} // end constructor

		protected T getData()
		{
			return data;
		} // end getData

		protected void setData(T newData)
		{
			data = newData;
		} // end setData

		protected Node getNextNode()
		{
			return next;
		} // end getNextNode

		protected void setNextNode(Node nextNode)
		{
			next = nextNode;
		} // end setNextNode
	} // end Node
} // end LinkedDS