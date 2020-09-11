//Modified by Landon Higinbotham

public class WordPuzzleSolution implements SolutionInterface {

	private char[][] board = null;
	private String[] word = null;
	private StringBuilder[] wordOut = null;
	private int currRow = 0;
	private int currCol = 0;

	WordPuzzleSolution(char[][] b, String[] w)
	{
		currRow = 0;	//Sets the ints to keep track of where we are on grid.
		currCol = 0;
		board = new char[b.length][b[0].length];
		for (int i = 0; i<b.length; i++)	//Fills in board with letters from the array we got from the file.
		{
			for (int j = 0; j<b[i].length; j++)
			{
				board[i][j] = b[i][j];
			}
		}
		
		word = w;
		wordOut = new StringBuilder[word.length];	//Sets up the array that we will use to output points
	}


	public void applyOption(DecisionInterface decision) //This section is responsible for Capitalizing our decisions in the direction of our Option value
	{
		int currOption = decision.getCurrentOption();

		while (decision.getCurrentDecision()==0&&currOption>3)	/*Since we are using options greater than 3 to shift our location, we must remove 4
		from currOption until we have a value less than 3, to give us the correct direction.*/
		{
			currOption = currOption - 4;
		}

		if (currOption==0)//Right
		{
			if(decision.getCurrentDecision()==0)	/*The first decision behaves differently than the others, so that is taken in account
			 whenever we keep track of our grids.*/
			{
				wordOut[decision.getCurrentDecision()] = new StringBuilder(word[decision.getCurrentDecision()]+": ("+currRow+","+currCol+") to ");
			}
			else
			{
				wordOut[decision.getCurrentDecision()] = new StringBuilder(word[decision.getCurrentDecision()]+": ("+currRow+","+(currCol+1)+") to ");
			}
			for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) //Goes through and upper cases the word we found.
			{
				if(Character.isUpperCase(board[currRow][currCol]))
				{
					currCol++;
				}
				board[currRow][currCol] = Character.toUpperCase(board[currRow][currCol]);
			}
			wordOut[decision.getCurrentDecision()].append("("+currRow+","+currCol+")"); //Appends the last coordinate for the word
		}
		else if (currOption==1)//Down
		{
			if(decision.getCurrentDecision()==0)	/*The first decision behaves differently than the others, so that is taken in account
			whenever we keep track of our grids.*/
			{
				wordOut[decision.getCurrentDecision()] = new StringBuilder(word[decision.getCurrentDecision()]+": ("+currRow+","+currCol+") to ");
			}
			else
			{
				wordOut[decision.getCurrentDecision()] = new StringBuilder(word[decision.getCurrentDecision()]+": ("+(currRow+1)+","+currCol+") to ");
			}
			for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) //Goes and upper cases the word we found
			{
				if(Character.isUpperCase(board[currRow][currCol]))
				{
					currRow++;
				}
				board[currRow][currCol] = Character.toUpperCase(board[currRow][currCol]);
			}
			wordOut[decision.getCurrentDecision()].append("("+currRow+","+currCol+")"); //Appends the last coordinate for the word
		}
		else if (currOption==2)//Left
		{
			if(decision.getCurrentDecision()==0)	/*The first decision behaves differently than the others, so that is taken in account
			whenever we keep track of our grids.*/
			{
				wordOut[decision.getCurrentDecision()] = new StringBuilder(word[decision.getCurrentDecision()]+": ("+currRow+","+currCol+") to ");
			}
			else
			{
				wordOut[decision.getCurrentDecision()] = new StringBuilder(word[decision.getCurrentDecision()]+": ("+currRow+","+(currCol-1)+") to ");
			}
			for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) //Goes and upper cases the word we found
			{
				if(Character.isUpperCase(board[currRow][currCol]))
				{
					currCol--;
				}
				board[currRow][currCol] = Character.toUpperCase(board[currRow][currCol]);
			}
			wordOut[decision.getCurrentDecision()].append("("+currRow+","+currCol+")"); //Appends the last coordinate for the word
		}
		else if (currOption==3)//Up
		{
			if(decision.getCurrentDecision()==0)/*The first decision behaves differently than the others, so that is taken in account
			whenever we keep track of our grids.*/
			{
				wordOut[decision.getCurrentDecision()] = new StringBuilder(word[decision.getCurrentDecision()]+": ("+currRow+","+currCol+") to ");
			}
			else
			{
				wordOut[decision.getCurrentDecision()] = new StringBuilder(word[decision.getCurrentDecision()]+": ("+(currRow-1)+","+currCol+") to ");
			}
			for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) //Goes and upper cases the word we found
			{
				if(Character.isUpperCase(board[currRow][currCol]))
				{
					currRow--;
				}
				board[currRow][currCol] = Character.toUpperCase(board[currRow][currCol]);
			}
			wordOut[decision.getCurrentDecision()].append("("+currRow+","+currCol+")"); //Appends the last coordinate for the word
		}
	}
	//undo an option
	public void undoOption(DecisionInterface decision)//Goes through and reads the diretion that the word was in, and lowercases it in reverse
	{
		int currOption = decision.getCurrentOption();
		while (decision.getCurrentDecision()==0&&currOption>3) //Again the first word must have its currOption lowered to a value that we can read
		{
			currOption = currOption - 4;
		}
		if (currOption==0)//Right
		{
			for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) //Iterates from back to front of the word
			{
				if(Character.isUpperCase(board[currRow][currCol]))
				{
					board[currRow][currCol] = Character.toLowerCase(board[currRow][currCol]);
					currCol--;
				}
			}
		}
		else if (currOption==1)//Down
		{
			for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) //Iterates from back to front of the word
			{
				if(Character.isUpperCase(board[currRow][currCol]))
				{
					board[currRow][currCol] = Character.toLowerCase(board[currRow][currCol]);
					currRow--;
				}
			}
		}
		else if (currOption==2)//Left
		{
			for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) //Iterates from back to front of the word
			{
				if(Character.isUpperCase(board[currRow][currCol]))
				{
					board[currRow][currCol] = Character.toLowerCase(board[currRow][currCol]);
					currCol++;
				}
			}
		}
		else if (currOption==3)//Up
		{
			for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) //Iterates from back to front of the word
			{
				if(Character.isUpperCase(board[currRow][currCol]))
				{
					board[currRow][currCol] = Character.toLowerCase(board[currRow][currCol]);
					currRow++;
				}
			}
		}

	}
	//check if an option is feasible
	public boolean isFeasible(DecisionInterface decision) //Checsk to see if the decision can be used based on available options
	{
		int currOption = decision.getCurrentOption();
		int wordLength = word[decision.getCurrentDecision()].length();
		if (decision.getCurrentDecision()==0)	/*If we are on the first decision, we will reset the locator ints each time we check its
		feasibility*/
		{
			currCol = 0;
			currRow = 0;
			wordLength--;
		}
		while (decision.getCurrentDecision()==0&&currOption>3) /* Each time we can remove 4 from the currOption, we move to the next point in the
		grid, going from left to right first then going down a line all the way to the end. */
		{
			currOption = currOption - 4;
			if(currCol<board[currRow].length-1)
			{
				currCol++;
			}
			else if(currRow<board.length-1)
			{
				currCol = 0;
				currRow++;
			}
		}
		if (currOption==0)//Right
		{
			if (currCol+wordLength<board[currRow].length) //Sees if the word can even fit, given its location.
			{
				int checkCol = currCol;
				if (decision.getCurrentDecision()!=0) //Checks to see if its the first decision, again it behaves differently than other decisions
				{
				checkCol = currCol+1;
				}
				for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) /* Iterates through the word, checking to make sure
				the letter has not already been used and making sure that it is the same letter as the one needed in the word. */
				{
					if (Character.isUpperCase(board[currRow][checkCol+i])||board[currRow][checkCol+i]!=word[decision.getCurrentDecision()].charAt(i))
					{
						return false; //If there is a letter than can not work, it returns false
					}
				}
				return true;
			}
		}
		else if (currOption==1)//Down
		{
			if (currRow+wordLength<board.length)//Sees if the word can even fit, given its location.
			{
				int checkRow = currRow;
				if (decision.getCurrentDecision()!=0) //Checks to see if its the first decision, again it behaves differently than other decisions
				{
				checkRow = currRow+1;
				}
				for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) /* Iterates through the word, checking to make sure
				the letter has not already been used and making sure that it is the same letter as the one needed in the word. */
				{
					if (Character.isUpperCase(board[checkRow+i][currCol])||board[checkRow+i][currCol]!=word[decision.getCurrentDecision()].charAt(i))
					{
						return false; //If there is a letter than can not work, it returns false
					}
				}
				return true;
			}
		}
		else if (currOption==2)//Left
		{
			if (currCol-wordLength>=0)//Sees if the word can even fit, given its location.
			{
				int checkCol = currCol;
				if (decision.getCurrentDecision()!=0) //Checks to see if its the first decision, again it behaves differently than other decisions
				{
				checkCol = currCol-1;
				}
				for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) /* Iterates through the word, checking to make sure
				the letter has not already been used and making sure that it is the same letter as the one needed in the word. */
				{
					if (Character.isUpperCase(board[currRow][checkCol-i])||board[currRow][checkCol-i]!=word[decision.getCurrentDecision()].charAt(i))
					{
						return false; //If there is a letter than can not work, it returns false
					}
				}
				return true;
			}
		}
		else if (currOption==3)//Up
		{
			if (currRow-wordLength>=0)//Sees if the word can even fit, given its location.
			{
				int checkRow = currRow;
				if (decision.getCurrentDecision()!=0) //Checks to see if its the first decision, again it behaves differently than other decisions
				{
				checkRow = currRow-1;
				}
				for(int i = 0; i<word[decision.getCurrentDecision()].length(); i++) /* Iterates through the word, checking to make sure
				the letter has not already been used and making sure that it is the same letter as the one needed in the word. */
				{
					if (Character.isUpperCase(board[checkRow-i][currCol])||board[checkRow-i][currCol]!=word[decision.getCurrentDecision()].charAt(i))
					{
						return false; //If there is a letter than can not work, it returns false
					}
				}
				return true;
			}
		}
		return false;
	}

	public String toString()	//Converts the findings into the string, with format required.
	{
		StringBuilder wordOutput = new StringBuilder("The phrase:");
		for(int i=0;i<word.length;i++) //Adds the phrases that were found
		{
			wordOutput.append(" "+word[i]);
		}
		wordOutput.append(System.getProperty("line.separator"));
		wordOutput.append("was found:");
		for(int i=0;i<wordOut.length;i++) //Adds the locations of where the words were found
		{
			wordOutput.append(System.getProperty("line.separator"));
			wordOutput.append(wordOut[i]);
		}
		wordOutput.append(System.getProperty("line.separator"));

		for (int i = 0; i < board.length; i++)	//Adds the board into the output
		{
			for (int j = 0; j < board[i].length; j++)
			{
				wordOutput.append(board[i][j] + " ");
			}
			wordOutput.append(System.getProperty("line.separator"));
		}
		return wordOutput.toString();
	}
}
