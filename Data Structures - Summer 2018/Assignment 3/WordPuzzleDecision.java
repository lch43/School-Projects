//Modified by Landon Higinbotham

public class WordPuzzleDecision implements DecisionInterface {

	private int currentDecision = 0;
	private int currentOption = 0;
	String word[] = null;
	private int rows = 0;
	private int cols = 0;

	public WordPuzzleDecision(String[] w, int decision, int row, int col)
	{
		word = w;
		currentDecision = decision;
		currentOption = -1;
		rows = row;
		cols = col;
	}

	public  boolean isTerminalDecision() //Checks to see if there are any other words after.
	{
		if(currentDecision>=word.length)
		{
			return true;
		}
		return false;
	}
	public  boolean hasNextOption() /*Checks to see if it is the first word, if it isn't it will iterate through the whole grid by changing 
	the Option number*/
	{
		if(currentOption<4)
		{
			return true;
		}
		else if(currentDecision==0&&currentOption<(rows*cols*4)) /*It calculates the possible Options with every single item on grid,
		but if it goes over then there is no next option*/
		{
			return true;
		}
		return false;
	}

	//return a new Decision object without changing the current decision
	public  DecisionInterface getNextDecision()
	{
		DecisionInterface returnInterface = new WordPuzzleDecision(word, currentDecision+1, rows, cols);
		return returnInterface;
	}

	//advance to next option
	public void nextOption()
	{
		currentOption++;
	}

	//getters
	public int getCurrentDecision()
	{
		return currentDecision;
	}

	public int getCurrentOption()
	{
		return currentOption;
	}
}
