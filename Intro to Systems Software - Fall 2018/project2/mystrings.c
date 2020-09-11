//Landon Higinbotham

#include <stdio.h>
#include <string.h>

int printStrings(FILE* fileName)
{
	int returnVal = 0;
	char charArray[4];
	int index = 0;
	char charTest;

	while (fread(&charTest, sizeof(char), 1, fileName) != 0)	//Puts a byte in charTest and makes sure there is a byte to be read in
	{
		if (index < 4) // See if we do not already have a valid string...
		{
			if ((32 <= (int)charTest && (int)charTest <= 126) || (int)charTest == 9) // Test to see if it is a valid printable character
			{
				charArray[index] = charTest; // Fill in the array
				index++; //Increment our index

				if (index == 4) //If we get a valid string, lets print it!
				{
					int i = 0;
					for (i; i < 4; i++)
					{
						printf("%c", charArray[i]);
					}
				}
			}
			else // If we did not get a valid string, we will reset the array.
			{
				index = 0;
			}
		}
		else // If we already have a size 4 array, we will just append some extra good characters
		{
			if ((32 <= (int)charTest && (int)charTest <= 126) || (int)charTest == 9) // Test to see if it is a valid printable character
			{
				printf("%c", charTest);
			}
			else // If it isnt a printable character, we will end the array and end the line.
			{
				index = 0;
				printf("\n");
			}
		}
	}
	
	fclose(fileName);
	return returnVal;
}


int  main(int argc, char** argv)
{
	if (argc >= 2)
	{
		FILE* fileName = fopen(argv[1], "r+b");
		if (fileName != NULL)
		{
			
        	        printStrings(fileName);

		}
		else
		{
			return -2;//Invalid filename
		}
	}
	else
	{
		printf("Invalid arguments.\n");
		return -1; //Invalid arguments.
	}


	return 0;
}
