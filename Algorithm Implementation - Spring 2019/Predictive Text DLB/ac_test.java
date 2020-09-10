//Landon Higinbotham
//LCH43

import java.io.*;
import java.util.*;

public class ac_test
{

    public static void main(String[] args)
    {
        DLB dictionary = loadDictionary();
        WordBook historicalDatabase = new WordBook();
        boolean done = false;
        Scanner input = new Scanner(System.in);
        double timeTotal = 0;
        double numTimes = 0;

        while (!done)
        {
            dictionary.resetCurrent();
            boolean reset = false;
            boolean firstChar = true;
            String[] suggestions = new String[5];
            String substring = "";
            while(!reset)
            {

                if (firstChar)
                {
                    System.out.print("Enter your first character: ");
                    firstChar = false;
                }
                else
                {
                    System.out.print("Enter your next character: ");
                }
                
                
                String in = input.nextLine();

                System.out.println();
                


                
                //----------------Check Input----------------------------------------------------------

                char character;
                if (in.length() == 0)
                {
                    character = '0';
                }
                else
                {
                    character = in.charAt(0);
                }

                if(character == '!')
                {
                    if (numTimes > 0)
                    {
                        System.out.printf("Average time:    %f\n", (timeTotal/numTimes));
                    }
                    System.out.println("Bye!");
                    
                    done = true;
                    reset = true;
                }
                else if(character == '$')
                {
                    System.out.println(" WORD COMPLETED:    "+substring);
                    historicalDatabase.addWord(substring, 1);
                    reset = true;
                }
                else if(character >= 49 && character <= 53)
                {
                    int index = (((int) character) - 48) - 1;
                    if (index < suggestions.length && suggestions[index] != "" && suggestions[index] != null)
                    {
                        System.out.println(" WORD COMPLETED:    "+suggestions[index]);
                        historicalDatabase.addWord(suggestions[index], 1);
                        reset = true;
                    }
                }
                //-------------------------------------------------------------------------------------
                
                if (!done && !reset && character != '0')
                {
                    substring = substring+character;
                    //----------------Search Word and Gather Suggestions-------------------------------
                    suggestions = new String[5];
                    String[] dictSugg = new String[5];

                    long start = System.nanoTime();

                    String[] history = historicalDatabase.getSuggestions(substring);


                    if (dictionary.searchFromCurrent(character))
                    {
                        dictSugg = dictionary.getSuggestions(5);
                    }
                    long end = System.nanoTime();
                    long total = end - start;
                    double totalSeconds = ((double)total/1000000000.0);
                    System.out.printf("(%f s)\n", totalSeconds);

                    timeTotal = timeTotal + totalSeconds;
                    numTimes = numTimes + 1;
                    //---------------------------------------------------------------------------------


                    //----------------Suggestion Merging----------------------------------------------

                    int indexFromHistory = 0;
                    while (indexFromHistory < history.length && history[indexFromHistory] != null && history[indexFromHistory] != "" )
                    {
                        suggestions[indexFromHistory] = history[indexFromHistory];
                        indexFromHistory++;
                    }

                    int pickUp = indexFromHistory;
                    int dictNum = 0;

                    if (pickUp < 4)
                    {
                        while(dictNum < 5 && pickUp < 5 && dictSugg[dictNum] != null && dictSugg[dictNum] != "")
                        {
                            boolean canAdd = true;
                            int historyCheck = 0;
                            while (canAdd && historyCheck < indexFromHistory)
                            {
                                if (dictSugg[dictNum].equals(suggestions[historyCheck]))
                                {
                                    canAdd = false;
                                }
                                historyCheck++;
                            }
                            if (canAdd == true)
                            {
                                suggestions[pickUp] = dictSugg[dictNum];
                                pickUp++;
                            }
                            dictNum++;
                        }
                    }
                    
                    //---------------------------------------------------------------------------------


                    //----------------Suggestion Printing----------------------------------------------

                    if (suggestions[0] != null && suggestions[0] != "")
                    {
                        System.out.println("Predictions:");
                    }
                    else
                    {
                        System.out.println("No predictions were found.");
                    }

                    for (int i = 0; i < suggestions.length; i++)
                    {
                        if (suggestions[i] != "" && suggestions[i] != null)
                        {
                            if (i != suggestions.length-1 && suggestions[i+1] != "" && suggestions[i+1] != null)
                            {
                                System.out.print((i+1)+") "+suggestions[i]+" ");
                            }
                            else
                            {
                                System.out.println((i+1)+") "+suggestions[i]+" ");
                            }
                        }
                    }
                    //--------------------------------------------------------------------------------
                }
                System.out.println();
            }
        }
        historicalDatabase.save();
        input.close();
    }

    private static DLB loadDictionary()
    {
        DLB dictionary = new DLB();
        File words;
        Scanner scan = null;
        try
        {
            words = new File("dictionary.txt");
            scan = new Scanner(words);
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }

        while (scan.hasNextLine())
        {
            char esc = '!';
            String newWord = scan.nextLine()+esc;
            dictionary.addString(newWord);
        }
        return dictionary;
    }
}




//----------------DLB Class----------------------------------------------


class DLB
{
    private Node root;
    private Node current;
    private Node currentSlider;
    private Node currentParent;
    private String currentString;
    private Boolean cancelSearch = false;
    private String[] suggestions = new String[5];

    DLB()
    {
    }
    //----------------Adding Words----------------------------------------------

    void addString(String newString)
    {
        current = root;
        for (int i = 0; i < newString.length(); i++)
        {
            addNode(newString.charAt(i));
        }
        current = root;
        currentParent = null;
        currentSlider = null;
    }

    void addNode(char character)
    {
        if (current == null)
        {
            if (root == null)
            {
                Node node = new Node(character);
                root = node;
                current = node.getDown();
                currentParent = node;
            }
            else if(currentParent != null)
            {
                Node node = new Node(character);
                currentParent.setDown(node);
                current = node.getDown();
                currentParent = node;
            }
        }
        else
        {
            currentSlider = current;
            boolean placed = false;
            while (currentSlider != null && placed == false)
            {
                if (currentSlider.getData() == character)
                {
                    currentParent = currentSlider;
                    current = currentSlider.getDown();
                    placed = true;
                }
                else if(currentSlider.getData() > character)
                {
                    if (currentSlider == current)
                    {
                        Node node = new Node(character);
                        node.setRight(currentSlider);
                        if (currentParent != null)
                        {
                            currentParent.setDown(node);
                        }
                        current = node.getDown();
                        currentParent = node;
                        placed = true;
                    }
                    else
                    {
                        Node node = new Node(character);
                        node.setRight(currentSlider);
                        current.setRight(node);
                        current = node.getDown();
                        currentParent = node;
                        placed = true;
                    }
                }

                if (placed == false)
                {
                    current = currentSlider;
                    currentSlider = currentSlider.getRight();
                }
            }

            if (placed == false)
            {
                Node node = new Node(character);
                current.setRight(node);
                current = node.getDown();
                currentParent = node;
                placed = true;
            }
        }
    }

    //--------------------------------------------------------------------------------

    void resetCurrent()
    {
        current = root;
        currentString = "";
        /*for (int i = 1; i < suggestions.length; i++)
        {
            suggestions[i] = "";
        }*/
        currentParent = null;
        currentSlider = null;
        cancelSearch = false;
    }

    //----------------Search for letter----------------------------------------------

    boolean searchFromCurrent(char character)
    {  
        if (cancelSearch == false)
        {
            boolean found = false;
            if (current == null)
            {
                cancelSearch = true;
            }

            while (found == false && cancelSearch == false)
            {
                if (current.getData() == character)
                {
                    currentParent = current;
                    currentString = currentString+current.getData();
                    current = current.getDown();
                    found = true;
                }
                else if (current.getData() > character)
                {
                    cancelSearch = true;
                }

                if (found == false && cancelSearch == false)
                {
                    current = current.getRight();
                    if (current == null)
                    {
                        cancelSearch = true;
                    }
                }
            }
        }

        return !cancelSearch;
    }

    
    //-------------------------------------------------------------------------------

    
    //--------------------Search for Suggestions-------------------------------------

    String returnCurrent()
    {
        return currentString;
    }

    String[] getSuggestions(int needed)
    {
        suggestions = new String[5];
        recursiveGetSuggestions(needed, 0, current, currentString);
        
        return suggestions;
    }

    int recursiveGetSuggestions(int needed, int count, Node recurCurrent, String suggest)
    {
        while (count < needed && recurCurrent != null)
        {
            if (recurCurrent.getData() == '!')
            {
                suggestions[count] = suggest;
                count++;
            }
            else
            {
                count = recursiveGetSuggestions(needed, count, recurCurrent.getDown(), suggest+recurCurrent.getData());
            }

            recurCurrent = recurCurrent.getRight();
        }

        return count;
    }

    //-------------------------------------------------------------------------------

    //----------------------Testing Stuff--------------------------------------------

    /*void checkBuild()
    {
        checkBuildRecursive(root, "");
    }

    void checkBuildRecursive(Node node, String string)
    {
        while (node != null)
        {
            if (node.getData() == '!')
            {
                System.out.println(string);
            }
            else
            {
                checkBuildRecursive(node.getDown(), string+node.getData());
            }

            node = node.getRight();
        }
    }*/

    //-------------------------------------------------------------------------------

    class Node
        {
            private char data;
            private Node down;
            private Node right;
    
            Node(char dat)
            {
                data = dat;
            }

            void setDown(Node below)
            {
                down = below;
            }

            void setRight(Node side)
            {
                right = side;
            }

            char getData()
            {
                return data;
            }

            Node getRight()
            {
                return right;
            }

            Node getDown()
            {
                return down;
            }
        }
    
    
}
//--------------------------------------------------------------------------------

//---------------------------Word Book--------------------------------------------
class WordBook
{
    File historytext;

    Chapter rootChapter = null;

    WordBook()
    {
        Scanner scan = null;
        try
        {
            historytext = new File("user_history.txt");
            historytext.createNewFile();
            scan = new Scanner(historytext);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        while (scan.hasNextLine())
        {
            String line = scan.nextLine();
            String[] split = line.split(":");
            String word = split[0];
            String frequency = split[1];

            addWord(word, Integer.parseInt(frequency));
        }
    }

    void addWord(String word, int frequency)
    {
        Chapter chosenChapter = null;
        if (rootChapter == null)
        {
            rootChapter = new Chapter(word.charAt(0));
            chosenChapter = rootChapter;
        }
        else
        {
            Chapter current = rootChapter;
            Chapter previous = null;
            boolean found = false;
            while (!found && current != null)
            {
                if (current.getLetter() == word.charAt(0))
                {
                    chosenChapter = current;
                    found = true;
                }
                else if(current.getLetter() > word.charAt(0))
                {
                    if (current == rootChapter)
                    {
                        Chapter chapter = new Chapter(word.charAt(0));
                        chapter.setNext(rootChapter);
                        rootChapter = chapter;
                        chosenChapter = rootChapter;
                        found = true;
                    }
                    else
                    {
                        Chapter chapter = new Chapter(word.charAt(0));
                        chapter.setNext(current);
                        previous.setNext(chapter);
                        chosenChapter = chapter;
                        found = true;
                    }
                }
                if (!found)
                {
                    previous = current;
                    current = current.getNext();
                }
            }
            if (!found)
            {
                Chapter chapter = new Chapter(word.charAt(0));
                previous.setNext(chapter);
                chosenChapter = chapter;
                found = true;
            }
        }
        if (chosenChapter.getFirst() == null)
        {
            Page page = new Page(word, frequency);
            chosenChapter.setFirst(page);
        }
        else
        {
            Page current = chosenChapter.getFirst();
            Page previous = null;
            boolean found = false;
            while (!found && current != null)
            {
                if (current.getWord().equals(word))
                {
                    current.setFreq(current.getFreq()+frequency);;
                    found = true;
                }
                else
                {
                    boolean similar = true;
                    int i = 0;
                    if (current.getWord().length() > word.length())
                    {
                        while (similar && i < word.length())
                        {
                            if (current.getWord().charAt(i) != word.charAt(i))
                            {
                                similar = false;
                            }

                            if (current.getWord().charAt(i) > word.charAt(i))
                            {
                                found = true;
                            }

                            i++;
                        }
                        if (similar)
                        {
                            found = true;
                        }
                    }
                    else if(current.getWord().length() < word.length())
                    {
                        while (similar && i > word.length())
                        {
                            if (current.getWord().charAt(i) != word.charAt(i))
                            {
                                similar = false;
                            }

                            if (current.getWord().charAt(i) > word.charAt(i))
                            {
                                found = true;
                            }

                            i++;
                        }
                    }
                    else if (current.getWord().length() == word.length())
                    {
                        while (similar && i < word.length())
                        {
                            if (current.getWord().charAt(i) != word.charAt(i))
                            {
                                similar = false;
                            }
                            

                            if (current.getWord().charAt(i) > word.charAt(i))
                            {
                                found = true;
                            }

                            i++;
                        }
                    }

                    if (found)
                    {
                        Page page = new Page(word, frequency);
                        page.setNext(current);

                        if (current == chosenChapter.getFirst())
                        {
                            chosenChapter.setFirst(page);
                        }
                    }
                }
                if (!found)
                {
                    previous = current;
                    current = current.getNext();
                }
            }
            if (!found)
            {
                Page page = new Page(word, frequency);
                previous.setNext(page);
                found = true;
            }
        }

    }

    String[] getSuggestions(String substring)
    {
        Page[] suggestionRank = new Page[5];
        String[] suggestions = new String[5];
        if (substring != null && substring != "")
        {
            char character = substring.charAt(0);
            Chapter current = rootChapter;
            boolean found = false;
            while (current != null && !found)
            {
                if (current.getLetter() > character)
                {
                    current = null;
                }
                else if(current.getLetter() == character)
                {
                    found = true;
                }

                if (current != null && !found)
                {
                    current = current.getNext();
                }
            }

            if (current != null)
            {
                Page currentPage = current.getFirst();
                while (currentPage != null)
                {
                    boolean fit = true;
                    int i = 0;

                    if (currentPage.getWord().length() == substring.length())
                    {
                        while (i < substring.length() && fit)
                        {
                            if (substring.charAt(i) > currentPage.getWord().charAt(i))
                            {
                                currentPage = null;
                            }

                            if (currentPage == null || substring.charAt(i) != currentPage.getWord().charAt(i))
                            {
                                fit = false;
                            }
                            i++;
                        }
                    }
                    else if (currentPage.getWord().length() > substring.length())
                    {
                        while (i < substring.length() && fit)
                        {
                            if (substring.charAt(i) > currentPage.getWord().charAt(i))
                            {
                                currentPage = null;
                            }

                            if (currentPage == null || substring.charAt(i) != currentPage.getWord().charAt(i))
                            {
                                fit = false;
                            }
                            i++;
                        }
                    }
                    else if (currentPage == null || currentPage.getWord().length() < substring.length())
                    {
                        fit = false;
                    }

                    if (fit && currentPage != null)
                    {
                        for (int j=0; j < suggestionRank.length; j++)
                        {
                            if (suggestionRank[j] != null)
                            {
                                if (suggestionRank[j].getFreq() < currentPage.getFreq())
                                {
                                    for (int k=suggestionRank.length - 2; k > j; k--)
                                    {
                                        suggestionRank[k+1] = suggestionRank[k];
                                    }
                                    suggestionRank[j] = currentPage;
                                    break;
                                }
                                i++;
                            }
                            else
                            {
                                suggestionRank[j] = currentPage;
                                break;
                            }
                        }
                    }
                    if (currentPage != null)
                    {
                        currentPage = currentPage.getNext();
                    }
                }
            }
        }

        for (int i = 0; i < suggestions.length; i++)
        {
            if (suggestionRank[i] != null)
            {
                suggestions[i] = suggestionRank[i].getWord();
            }
        }

        return suggestions;
    }

    void save()
    {
        PrintWriter printer = null;
        try
        {
            printer = new PrintWriter(historytext);
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }

        Chapter currentChapter = rootChapter;
        while (currentChapter != null)
        {
            Page currentPage = currentChapter.getFirst();

            while(currentPage != null)
            {
                printer.println(currentPage.getWord()+":"+currentPage.getFreq());
                currentPage = currentPage.getNext();
            }
            currentChapter = currentChapter.getNext();
        }
        printer.close();
    }

    class Chapter
    {
        private char letter;
        private Chapter next;
        private Page first;

        Chapter(char character)
        {
            letter = character;
        }

        void setLetter(char character) {
            letter = character;
        }

        void setFirst(Page page) {
            first = page;
        }

        void setNext(Chapter chapter) {
            next = chapter;
        }
        
        Page getFirst() {
            return first;
        }
        
        char getLetter() {
            return letter;
        }
        
        Chapter getNext() {
            return next;
        }
    }

    class Page
    {
        private String word;
        private int freq;
        private Page next;

        Page(String entry, int frequency)
        {
            word = entry;
            freq = frequency;
        }

        void setFreq(int frequency) {
            freq = frequency;
        }

        void setNext(Page page) {
            next = page;
        }

        String getWord() {
            return word;
        }

        int getFreq() {
            return freq;
        }

        Page getNext() {
            return next;
        }
    }
}
//--------------------------------------------------------------------------------