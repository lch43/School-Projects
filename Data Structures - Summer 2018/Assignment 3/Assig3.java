//Landon Higinbotham

import java.util.*;
import java.io.*;

public class Assig3
{

	public static void main(String[]args)
	{
		start();
	}

	// Constructor to set things up and make the initial search call.
	public static void start()
	{
		Scanner inScan = new Scanner(System.in);
		Scanner fReader;
		File fName;
		String fString = "", word = "";
		
			// Make sure the file name is valid
		while (true)
		{
			try
			{
				System.out.println("Please enter grid filename:");
				fString = inScan.nextLine();
				fName = new File(fString);
				fReader = new Scanner(fName);
				
				break;
			}
			catch (IOException e)
			{
				System.out.println("Problem: " + e);
			}
		}

		// Parse input file to create 2-d grid of characters
		String [] dims = (fReader.nextLine()).split(" ");
		int rows = Integer.parseInt(dims[0]);
		int cols = Integer.parseInt(dims[1]);
		
		char [][] theBoard = new char[rows][cols];

		for (int i = 0; i < rows; i++)
		{
			String rowString = fReader.nextLine();
			for (int j = 0; j < rowString.length(); j++)
			{
				theBoard[i][j] = Character.toLowerCase(rowString.charAt(j));
			}
		}

		// Show user the grid
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				System.out.print(theBoard[i][j] + " ");
			}
			System.out.println();
		}
		boolean repeat = true;
		System.out.println();
		while (repeat)
		{
			System.out.println("Please enter phrase (sep. by single spaces):");
			word = (inScan.nextLine()).toLowerCase();
			String wordArray[] = word.split(" ");
			if(wordArray[0].length()==0)	//If the user does not input then close program.
			{
				break;
			}
			StringBuilder output = new StringBuilder("Looking for:");
			for(int i=0;i<wordArray.length;i++)
			{
				output.append(" "+wordArray[i]);
			}
			output.append(System.getProperty("line.separator"));
			output.append("containing "+wordArray.length+" words");
			System.out.println(output.toString());
			BackTrackingSolver bts = new BackTrackingSolver(new WordPuzzleSolution(theBoard, wordArray),new WordPuzzleDecision(wordArray, 0, rows, cols));
			
			SolutionInterface finalOutput = bts.solve();
			if(finalOutput == null)	//If the final answer isn't found, adjust output
			{
				StringBuilder thePhrase = new StringBuilder("The phrase:");
				for(int i=0;i<wordArray.length;i++)
				{
					thePhrase.append(" "+wordArray[i]);
				}
				System.out.println(thePhrase.toString());
				System.out.println("was not found");
				System.out.println();
			}
			else
			{
				System.out.println(finalOutput);
			}
		}
	}

}