// CS 0445 Summer 2018
// Author's code for MergeSort and QuickSort
// Note that this code is designed for readability and modularity.  It is
// not necessarily the most efficient way of implementing these algorithms.
import java.util.*;

public class TextMergeQuick
{
	public static final int MIN_SIZE = 5; // for quick sort
	
	// MERGE SORT
	public static <T extends Comparable<? super T>>
	       void mergeSort(T[] a, int n)
	{
		mergeSort(a, 0, n - 1);
	} // end mergeSort

	public static <T extends Comparable<? super T>>
	       void mergeSort(T[] a, int first, int last)
	{
	  @SuppressWarnings("unchecked")	
	  T[] tempArray = (T[])new Comparable<?>[a.length];
	  mergeSort(a, tempArray, first, last);
	} // end mergeSort
	
	private static <T extends Comparable<? super T>>
	        void mergeSort(T[] a, T[] tempArray, int first, int last)
	{
	   if (first < last)
	   {  // sort each half
	      int mid = (first + last)/2;// index of midpoint
	      mergeSort(a, tempArray, first, mid);  // sort left half array[first..mid]
	      mergeSort(a, tempArray, mid + 1, last); // sort right half array[mid+1..last]

			if (a[mid].compareTo(a[mid + 1]) > 0)      // Question 2, Chapter 9
	     	 	merge(a, tempArray, first, mid, last); // merge the two halves
	   //	else skip merge step
	   }  // end if
	}  // end mergeSort
	
	private static <T extends Comparable<? super T>> 
	        void merge(T[] a, T[] tempArray, int first, int mid, int last)
	{
		// Two adjacent subarrays are a[beginHalf1..endHalf1] and a[beginHalf2..endHalf2].
		int beginHalf1 = first;
		int endHalf1 = mid;
		int beginHalf2 = mid + 1;
		int endHalf2 = last;

		// while both subarrays are not empty, copy the
	   // smaller item into the temporary array
		int index = beginHalf1; // next available location in
								            // tempArray
		for (; (beginHalf1 <= endHalf1) && (beginHalf2 <= endHalf2); index++)
	   {  // Invariant: tempArray[beginHalf1..index-1] is in order
	   
	      if (a[beginHalf1].compareTo(a[beginHalf2]) <= 0)
	      {  
	      	tempArray[index] = a[beginHalf1];
	        beginHalf1++;
	      }
	      else
	      {  
	      	tempArray[index] = a[beginHalf2];
	        beginHalf2++;
	      }  // end if
	   }  // end for

	   // finish off the nonempty subarray

	   // finish off the first subarray, if necessary
	   for (; beginHalf1 <= endHalf1; beginHalf1++, index++)
	      // Invariant: tempArray[beginHalf1..index-1] is in order
	      tempArray[index] = a[beginHalf1];

	   // finish off the second subarray, if necessary
		for (; beginHalf2 <= endHalf2; beginHalf2++, index++)
	      // Invariant: tempa[beginHalf1..index-1] is in order
	      tempArray[index] = a[beginHalf2];
		
	   // copy the result back into the original array
	   for (index = first; index <= last; index++)
	      a[index] = tempArray[index];
	}  // end merge

    public static <T extends Comparable<? super T>> 
	       void iterativeMergeSort(T[] a, int n)
	{
      // The cast is safe because the new array contains null entries
      @SuppressWarnings("unchecked")
      T[] tempArray = (T[])new Comparable<?>[a.length]; // unchecked cast
		int beginLeftovers = n;
		
		for (int segmentLength = 1; segmentLength <= n/2; segmentLength = 2*segmentLength)
		{
			beginLeftovers = mergeSegmentPairs(a, tempArray, n, segmentLength);

			// Two full segments do not remain at end; the following are possibilites:
			//   a. one full segment and a partial second segment
			//   b. one full segment only
			//   c. one partial segment
			//   d. nothing is left at end
			int endSegment = beginLeftovers + segmentLength - 1;
			
			if (endSegment < n - 1)
			// Case a: one full segment and a partial second segment exist, so merge them
				merge(a, tempArray, beginLeftovers, endSegment, n-1);

			// else Cases b, c, or d: only one full or partial segment is left (leave it in place)
			//                        or nothing is left
		} // end for

		// merge the sorted leftovers, if any, with the rest of the sorted array

		if (beginLeftovers < n)
			merge(a, tempArray, 0, beginLeftovers-1, n-1);
	} // end iterativeMergeSort

	// Merges pairs of segments of a given length within an array 
	// and returns the index after the last merged pair.
	private static <T extends Comparable<? super T>> 
	        int mergeSegmentPairs(T[] a, T[] tempArray, int n, int segmentLength)
	{
		int mergedPairLength = 2 * segmentLength; // Length of two merged segments
		int numberOfPairs = n / mergedPairLength;
		
		int beginSegment1 = 0;
		
		for (int count = 1; count <= numberOfPairs; count++)
		{
			int endSegment1 = beginSegment1 + segmentLength - 1;

			int beginSegment2 = endSegment1 + 1;
			int endSegment2 = beginSegment2 + segmentLength - 1;
			
			merge(a, tempArray, beginSegment1, endSegment1, endSegment2);
			
			beginSegment1 = endSegment2 + 1;
		} // end for
		
		return beginSegment1; // Return index of element after last merged pair
	}  // end mergeSegmentPairs

// -------------------------------------------------------------------------------

// QUICK SORT 20
	public static <T extends Comparable<? super T>>
		   void quickSort(T[] array, int n)
	{
		quickSort(array, 0, n-1);
	} // end quickSort
	
	/** Sorts an array into ascending order. Uses quick sort with
	 *  median-of-three pivot selection for arrays of at least 
	 *  MIN_SIZE elements, and uses insertion sort for other arrays. */
	public static <T extends Comparable<? super T>>
	       void quickSort(T[] a, int first, int last)
	{
	  if (last - first + 1 < 20)
	  {
	    insertionSort(a, first, last);
	  }
	  else
	  {
	    // create the partition: Smaller | Pivot | Larger
	    int pivotIndex = partition(a, first, last);
	    
	    // sort subarrays Smaller and Larger
	    quickSort(a, first, pivotIndex - 1);
	    quickSort(a, pivotIndex + 1, last);
	  } // end if
	} // end quickSort


	
	// QUICK SORT

	public static <T extends Comparable<? super T>>
		   void quickSortHundred(T[] array, int n)
	{
		quickSortHundred(array, 0, n-1);
	} // end quickSort
	
	/** Sorts an array into ascending order. Uses quick sort with
	 *  median-of-three pivot selection for arrays of at least 
	 *  MIN_SIZE elements, and uses insertion sort for other arrays. */
	public static <T extends Comparable<? super T>>
	       void quickSortHundred(T[] a, int first, int last)
	{
	  if (last - first + 1 < 100)
	  {
	    insertionSort(a, first, last);
	  }
	  else
	  {
	    // create the partition: Smaller | Pivot | Larger
	    int pivotIndex = partition(a, first, last);
	    
	    // sort subarrays Smaller and Larger
	    quickSort(a, first, pivotIndex - 1);
	    quickSort(a, pivotIndex + 1, last);
	  } // end if
	} // end quickSort

	// 12.17
	/** Task: Partitions an array as part of quick sort into two subarrays
	 *        called Smaller and Larger that are separated by a single
	 *        element called the pivot. 
	 *        Elements in Smaller are <= pivot and appear before the 
	 *        pivot in the array.
	 *        Elements in Larger are >= pivot and appear after the 
	 *        pivot in the array.
	 *  @param a      an array of Comparable objects
	 *  @param first  the integer index of the first array element; 
	 *                first >= 0 and < a.length 
	 *  @param last   the integer index of the last array element; 
	 *                last - first >= 3; last < a.length
	 *  @return the index of the pivot */
	private static <T extends Comparable<? super T>>
	        int partition(T[] a, int first, int last)
	{
	  int mid = (first + last)/2;
	  sortFirstMiddleLast(a, first, mid, last);
	  
	  // Assertion: The pivot is a[mid]; a[first] <= pivot and 
	  // a[last] >= pivot, so do not compare these two array elements
	  // with pivot.
	  
	  // move pivot to next-to-last position in array
	  swap(a, mid, last - 1);
	  int pivotIndex = last - 1;
	  T pivot = a[pivotIndex];
	  
	  // determine subarrays Smaller = a[first..endSmaller]
	  // and                 Larger  = a[endSmaller+1..last-1]
	  // such that elements in Smaller are <= pivot and 
	  // elements in Larger are >= pivot; initially, these subarrays are empty

	  int indexFromLeft = first + 1; 
	  int indexFromRight = last - 2; 
	  boolean done = false;
	  while (!done)
	  {
	    // starting at beginning of array, leave elements that are < pivot;
	    // locate first element that is >= pivot; you will find one,
	    // since last element is >= pivot
	    while (a[indexFromLeft].compareTo(pivot) < 0)
	      indexFromLeft++;
	      
	    // starting at end of array, leave elements that are > pivot; 
	    // locate first element that is <= pivot; you will find one, 
	    // since first element is <= pivot
	    while (a[indexFromRight].compareTo(pivot) > 0)
	      indexFromRight--;
	      
	    assert a[indexFromLeft].compareTo(pivot) >= 0 && 
	           a[indexFromRight].compareTo(pivot) <= 0;
	           
	    if (indexFromLeft < indexFromRight)
	    {
	      swap(a, indexFromLeft, indexFromRight);
	      indexFromLeft++;
	      indexFromRight--;
	    }
	    else 
	      done = true;
	  } // end while
	  
	  // place pivot between Smaller and Larger subarrays
	  swap(a, pivotIndex, indexFromLeft);
	  pivotIndex = indexFromLeft;
	  
	  // Assertion:
	  //   Smaller = a[first..pivotIndex-1]
	  //   Pivot = a[pivotIndex]
	  //   Larger = a[pivotIndex+1..last]
	  
	  return pivotIndex; 
	} // end partition

	// 12.16
	/** Task: Sorts the first, middle, and last elements of an 
	 *        array into ascending order.
	 *  @param a      an array of Comparable objects
	 *  @param first  the integer index of the first array element; 
	 *                first >= 0 and < a.length 
	 *  @param mid    the integer index of the middle array element
	 *  @param last   the integer index of the last array element; 
	 *                last - first >= 2, last < a.length */
	private static <T extends Comparable<? super T>>
	        void sortFirstMiddleLast(T[] a, int first, int mid, int last)
	{
	  order(a, first, mid); // make a[first] <= a[mid]
	  order(a, mid, last);  // make a[mid] <= a[last]
	  order(a, first, mid); // make a[first] <= a[mid]
	} // end sortFirstMiddleLast

	/** Task: Orders two given array elements into ascending order
	 *        so that a[i] <= a[j].
	 *  @param a  an array of Comparable objects
	 *  @param i  an integer >= 0 and < array.length
	 *  @param j  an integer >= 0 and < array.length */
	private static <T extends Comparable<? super T>>
	        void order(T[] a, int i, int j)
	{
	  if (a[i].compareTo(a[j]) > 0)
	    swap(a, i, j);
	} // end order

  /** Task: Swaps the array elements a[i] and a[j].
   *  @param a  an array of objects
   *  @param i  an integer >= 0 and < a.length
   *  @param j  an integer >= 0 and < a.length */
  private static void swap(Object[] a, int i, int j)
  {
    Object temp = a[i];
    a[i] = a[j];
    a[j] = temp; 
  } // end swap

  public static <T extends Comparable<? super T>> 
	       void insertionSort(T[] a, int n)
	{
		insertionSort(a, 0, n - 1);
	} // end insertionSort

  public static <T extends Comparable<? super T>> 
	       void insertionSort(T[] a, int first, int last)
	{
		int unsorted, index;
		
		for (unsorted = first + 1; unsorted <= last; unsorted++)
		{   // Assertion: a[first] <= a[first + 1] <= ... <= a[unsorted - 1]
		
			T firstUnsorted = a[unsorted];
			
			insertInOrder(firstUnsorted, a, first, unsorted - 1);
		} // end for
	} // end insertionSort

  private static <T extends Comparable<? super T>> 
	        void insertInOrder(T element, T[] a, int begin, int end)
	{
		int index;
		
		for (index = end; (index >= begin) && (element.compareTo(a[index]) < 0); index--)
		{
			a[index + 1] = a[index]; // make room
		} // end for
		
		// Assertion: a[index + 1] is available
		a[index + 1] = element;  // insert
	} // end insertInOrder


	public static <T extends Comparable<? super T>>
	void randomQuickSort(T[] array, int n)
	{
 		randomQuickSort(array, 0, n-1);
	} // end quickSort

	public static <T extends Comparable<? super T>>
	void randomQuickSort(T[] a, int first, int last)
	{
		if (last - first + 1 < MIN_SIZE)
		{
			insertionSort(a, first, last);
		}
		else
		{
		// create the partition: Smaller | Pivot | Larger
		int pivotIndex = randPartition(a, first, last);
	
		// sort subarrays Smaller and Larger
		randomQuickSort(a, first, pivotIndex - 1);
		randomQuickSort(a, pivotIndex + 1, last);
		} // end if
	} // end randomQuickSort

	private static <T extends Comparable<? super T>>
	int randPartition(T[] a, int first, int last)
	{
		Random randNum = new Random();
		int pivotIndex = randNum.nextInt(last-first+1)+first; //Selects a random pivot point
		swap(a, pivotIndex, last); //Puts the random pivot point last
		pivotIndex = last;
		T pivot = a[pivotIndex];
		
		for(int i = first; i<pivotIndex; i++) //Any time a number is greater than the pivot it gets moved to the other side of the pivot
		{
			if(i<pivotIndex)
				while(a[i].compareTo(pivot)>0&&i<pivotIndex)
				{
					swap(a, i, pivotIndex-1);
					swap(a, pivotIndex-1, pivotIndex);
					pivotIndex--;
				}
		}

		return pivotIndex;
	} // end partition
}