//Landon Higinbotham

import java.util.*;
import java.io.*;

public class Assig4
{
	
	static long[]aAverage;
	static long[]bAverage;
	static long[]cAverage;
	static long[]dAverage;
	static long[]eAverage;

	public static void main(String[]args)
	{
		Scanner inScan = new Scanner(System.in);
		Scanner fileScan = new Scanner(System.in);
		int arraySize = 0;
		int trials = 0;
		String fileName;

		System.out.println("Enter array size:");
		arraySize = inScan.nextInt();

		System.out.println("Enter number of trials:");
		trials = inScan.nextInt();
		
		System.out.println("Enter file name:");
		fileName = fileScan.nextLine();

		inScan.close();
		fileScan.close();
		
		aAverage = new long[trials*3];	//These arrays are made to keep track of the times it takes for each trial
		bAverage = new long[trials*3];
		cAverage = new long[trials*3];
		dAverage = new long[trials*3];
		eAverage = new long[trials*3];

		randomTest(arraySize, trials);
		sortedTest(arraySize, trials);
		reverseSortedTest(arraySize, trials);
		export(fileName, arraySize, trials);
	}

	public static void randomTest(int size, int trials) //Tests each of the five algorithms against a Random Array.
	{

		Integer[]compare = new Integer[size];
		Integer[]a = new Integer[size];
		Integer[]b = new Integer[size];
		Integer[]c = new Integer[size];
		Integer[]d = new Integer[size];
		Integer[]e = new Integer[size];

		Random randInt = new Random();
		for(int curr = 0; curr<trials; curr++) //Iterates through the number of trials
		{
			for(int i = 0; i<size;i++) //Fills each of the arrays with the same random numbers
			{
				int number  = randInt.nextInt(size+1);
				compare[i] = number;
				a[i] = number;
				b[i] = number;
				c[i] = number;
				d[i] = number;
				e[i] = number;
			}

			long start = System.nanoTime();
			TextMergeQuick.quickSort(a, size);
			long end = System.nanoTime();
			aAverage[curr] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Median of Three (20)");
				System.out.println(size);
				System.out.println("Random");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(a[j]+" ");
				}
				System.out.println();
			}
			
			
			start = System.nanoTime();
			TextMergeQuick.quickSortHundred(b, size);
			end = System.nanoTime();
			bAverage[curr] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Median of Three (100)");
				System.out.println(size);
				System.out.println("Random");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(b[j]+" ");
				}
				System.out.println();
			}
			
			
			
			start = System.nanoTime();
			TextMergeQuick.randomQuickSort(c, size);
			end = System.nanoTime();
			cAverage[curr] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Random Pivot Quick Sort");
				System.out.println(size);
				System.out.println("Random");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(c[j]+" ");
				}
				System.out.println();
			}
			
			
			
			start = System.nanoTime();
			TextMergeQuick.mergeSort(d, size);
			end = System.nanoTime();
			dAverage[curr] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Recursive Merge Sort");
				System.out.println(size);
				System.out.println("Random");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(d[j]+" ");
				}
				System.out.println();
			}
			
			
			
			start = System.nanoTime();
			TextMergeQuick.iterativeMergeSort(e, size);
			end = System.nanoTime();
			eAverage[curr] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Iterative Merge Sort");
				System.out.println(size);
				System.out.println("Random");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(e[j]+" ");
				}
				System.out.println();
			}
			
			
		}
	}

	public static void sortedTest(int size, int trials) //Same as randomTest, but instead of choosing random numbers it puts in sorted numbers
	{

		Integer[]compare = new Integer[size];
		Integer[]a = new Integer[size];
		Integer[]b = new Integer[size];
		Integer[]c = new Integer[size];
		Integer[]d = new Integer[size];
		Integer[]e = new Integer[size];

		Random randInt = new Random();
		for(int curr = 0; curr<trials; curr++)
		{
			for(int i = 0; i<size;i++)
			{
				compare[i] = i;
				a[i] = i;
				b[i] = i;
				c[i] = i;
				d[i] = i;
				e[i] = i;
			}

			long start = System.nanoTime();
			TextMergeQuick.quickSort(a, size);
			long end = System.nanoTime();
			aAverage[curr+trials] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Median of Three (20)");
				System.out.println(size);
				System.out.println("Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(a[j]+" ");
				}
				System.out.println();
			}
			
			
			start = System.nanoTime();
			TextMergeQuick.quickSortHundred(b, size);
			end = System.nanoTime();
			bAverage[curr+trials] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Median of Three (100)");
				System.out.println(size);
				System.out.println("Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(b[j]+" ");
				}
				System.out.println();
			}
			
			
			
			start = System.nanoTime();
			TextMergeQuick.randomQuickSort(c, size);
			end = System.nanoTime();
			cAverage[curr+trials] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Random Pivot Quick Sort");
				System.out.println(size);
				System.out.println("Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(c[j]+" ");
				}
				System.out.println();
			}
			
			
			
			start = System.nanoTime();
			TextMergeQuick.mergeSort(d, size);
			end = System.nanoTime();
			dAverage[curr+trials] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Recursive Merge Sort");
				System.out.println(size);
				System.out.println("Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(d[j]+" ");
				}
				System.out.println();
			}
			
			
			
			start = System.nanoTime();
			TextMergeQuick.iterativeMergeSort(e, size);
			end = System.nanoTime();
			eAverage[curr+trials] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Iterative Merge Sort");
				System.out.println(size);
				System.out.println("Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(e[j]+" ");
				}
				System.out.println();
			}
			
			
		}
	}

	public static void reverseSortedTest(int size, int trials) //Same as randomTest, but puts in numbers from greatest to least.
	{

		Integer[]compare = new Integer[size];
		Integer[]a = new Integer[size];
		Integer[]b = new Integer[size];
		Integer[]c = new Integer[size];
		Integer[]d = new Integer[size];
		Integer[]e = new Integer[size];

		Random randInt = new Random();
		for(int curr = 0; curr<trials; curr++)
		{
			for(int i = 0; i<size;i++)
			{
				int j = size-i;
				compare[i] = j;
				a[i] = j;
				b[i] = j;
				c[i] = j;
				d[i] = j;
				e[i] = j;
			}

			long start = System.nanoTime();
			TextMergeQuick.quickSort(a, size);
			long end = System.nanoTime();
			aAverage[curr+trials*2] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Median of Three (20)");
				System.out.println(size);
				System.out.println("Reverse Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(a[j]+" ");
				}
				System.out.println();
			}
			
			
			start = System.nanoTime();
			TextMergeQuick.quickSortHundred(b, size);
			end = System.nanoTime();
			bAverage[curr+trials*2] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Median of Three (100)");
				System.out.println(size);
				System.out.println("Reverse Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(b[j]+" ");
				}
				System.out.println();
			}
			
			
			
			start = System.nanoTime();
			TextMergeQuick.randomQuickSort(c, size);
			end = System.nanoTime();
			cAverage[curr+trials*2] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Random Pivot Quick Sort");
				System.out.println(size);
				System.out.println("Reverse Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(c[j]+" ");
				}
				System.out.println();
			}
			
			
			
			start = System.nanoTime();
			TextMergeQuick.mergeSort(d, size);
			end = System.nanoTime();
			dAverage[curr+trials*2] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Recursive Merge Sort");
				System.out.println(size);
				System.out.println("Reverse Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(d[j]+" ");
				}
				System.out.println();
			}
			
			
			
			start = System.nanoTime();
			TextMergeQuick.iterativeMergeSort(e, size);
			end = System.nanoTime();
			eAverage[curr+trials*2] = end-start;

			if(size<=20) //If there is less than 20 entries, enter Trace Output Mode
			{
				System.out.println();
				System.out.println("Algorithm: Iterative Merge Sort");
				System.out.println(size);
				System.out.println("Reverse Sorted");
				for (int j = 0; j<size; j++)
				{
					System.out.print(compare[j]+" ");
				}
				System.out.println();
				for (int j = 0; j<size; j++)
				{
					System.out.print(e[j]+" ");
				}
				System.out.println();
			}
		}
	}

	public static void export(String fileName, int size, int trials)
	{
		File saveTo;
		PrintWriter writer = null;
		try
		{
			saveTo = new File(fileName); // Creates a new file
			writer = new PrintWriter(saveTo);
		}
		catch(FileNotFoundException ex)
		{
		}

		for (int i = 0; i<3; i++) //Fills in the file with the correct information for each algorihm and sort type
		{
			for (int j = 0; j<5; j++)
			{
				String nameAlg = "";
				String sortType = "";

				if (j==0)
				{
					nameAlg = "Median of Three (20)";
				}
				else if (j==1)
				{
					nameAlg = "Median of Three (100)";
				}
				else if (j==2)
				{
					nameAlg = "Random Pivot Quick Sort";
				}
				else if (j==3)
				{
					nameAlg = "Recursive Merge Sort";
				}
				else if (j==4)
				{
					nameAlg = "Iterative Merge Sort";
				}
				
				if (i==0)
				{
					sortType = "Random";
				}
				else if (i==1)
				{
					sortType = "Sorted";
				}
				else if (i==2)
				{
					sortType = "Reverse Sorted";
				}

				writer.println("Algorithm: "+nameAlg); //Prints the output to the file
				writer.println("Array Size: "+size);
				writer.println("Order: "+sortType);
				writer.println("Number of trials: "+trials);
				
				if (j==0) //Depending on which algorithm, we calculate the average for each sort type
				{
					long average = 0;
					long sum = 0;

					for (int k = 0; k<trials; k++)
					{
						sum = sum + aAverage[k+trials*i];
					}

					average = sum/trials;
					double seconds = (double) average/1000000000;
					writer.println("Average Time: "+seconds+" sec");
					
				}
				else if (j==1)
				{
					long average = 0;
					long sum = 0;

					for (int k = 0; k<trials; k++)
					{
						sum = sum + bAverage[k+trials*i];
					}

					average = sum/trials;
					double seconds = (double) average/1000000000;
					writer.println("Average Time: "+seconds+" sec");
					
				}
				else if (j==2)
				{
					long average = 0;
					long sum = 0;

					for (int k = 0; k<trials; k++)
					{
						sum = sum + cAverage[k+trials*i];
					}

					average = sum/trials;
					double seconds = (double) average/1000000000;
					writer.println("Average Time: "+seconds+" sec");
					
				}
				else if (j==3)
				{
					long average = 0;
					long sum = 0;

					for (int k = 0; k<trials; k++)
					{
						sum = sum + dAverage[k+trials*i];
					}

					average = sum/trials;
					double seconds = (double) average/1000000000;
					writer.println("Average Time: "+seconds+" sec");
					
				}
				else if (j==4)
				{
					long average = 0;
					long sum = 0;

					for (int k = 0; k<trials; k++)
					{
						sum = sum + eAverage[k+trials*i];
					}

					average = sum/trials;
					double seconds = (double) average/1000000000;
					writer.println("Average Time: "+seconds+" sec");
					
				}
				writer.println();
			}
		}
		writer.close(); //Saves the file
	}
}