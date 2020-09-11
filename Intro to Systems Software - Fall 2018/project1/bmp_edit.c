//Landon Higinbotham
#pragma pack(1)

#include <stdio.h>
#include <string.h>
#include <math.h>

int edit(int editType, FILE* picture)
{
	int returnVal = 0;
	struct header	//Creates the first header strcut
	{
		char formIdent1;
		char formIdent2;
		int fileSize;
		short reserve1;
		short reserve2;
		int offset;
	} picHeader;

	fread(&picHeader, sizeof(picHeader), 1, picture);	//Puts the first header into the first header struct

	struct dibHeader	//Creates the second header
	{
		int size;
		int width;
		int height;
		short colorPlanes;
		short bpp;
		int compression;
		int sizeInBytes;
		int horizontal;
		int vertical;
		int palette;
		int impColors;
	} picDIB;

	fread(&picDIB, sizeof(picDIB), 1, picture);	//Puts the second header into the second header struct

	if (picHeader.formIdent1 == 'B' && picHeader.formIdent2 == 'M' && picDIB.size == 40 && picDIB.bpp == 24)	//Makes sure the format identifier is BM, the size is 40, and the bits per pixel is 24
	{
		struct pixel	//Creates a struct for each pixel
		{
			unsigned char blue;
			unsigned char green;
			unsigned char red;
		} editPixel;

		fseek(picture, picHeader.offset, SEEK_SET);	//Stores RGB info into the pixel struct

		int height = picDIB.height;
		int width = picDIB.width;
		int padding = (4 - ((width * 3) % 4)) % 4;
		int check = fread(&editPixel, sizeof(editPixel), 1, picture);
		while ( check == 1 && height > 0)	//Reads the pixel info into the pixel struct and makes sure there are still pixels that need to be read.
		{
			if (width > 0)	//Checks to see if we are not at the end of the row.
			{
				width--;	//Subtracts from width to make it easier to see if we are near the end of the row.
				fseek(picture, -3, SEEK_CUR);	//Sets the cursor back to before the pixel we just read.

				if (editType == 1)	//If the user entered -invert then we will invert it.
				{
					editPixel.blue = ~editPixel.blue;
					editPixel.green = ~editPixel.green;
					editPixel.red = ~editPixel.red;
				}
				else if (editType == 2)	//If the user entered -grayscale then we will convert it to grayscale.
				{
					float R = editPixel.red/255.00f;
					float G = editPixel.green/255.00f;
					float B = editPixel.blue/255.00f;
					float Y = 0.2126 * R + 0.7152 * G + 0.0722 * B;

					if (Y <= .0031308)
					{
						R = G = B = 12.92 * Y;
					}
					else
					{
						R = G = B = 1.055 * pow(Y, 1/2.4) - 0.055;
					}

					editPixel.blue = (unsigned char) (B*255);
					editPixel.green = (unsigned char) (G*255);
					editPixel.red = (unsigned char) (R*255);
				}

				fwrite(&editPixel, sizeof(struct pixel), 1, picture);	//We now write the new pixel back to the file.
				fseek(picture, 0, SEEK_CUR);	//The step may be unnecessary, but I saw that it could help in the hints section.

			}
			else	//If we are at the end of the row we will move to the next row.
			{
				width = picDIB.width;
				height--;

				fseek(picture, -3, SEEK_CUR);
				fseek(picture, padding, SEEK_CUR);
			}

			check = fread(&editPixel, sizeof(editPixel), 1, picture);
		}
		if (height == 1 && width == 0)	//If we end the loop and the width is 0 and height is 1, then all must have went well.
		{
			height = 0; //Job complete
		}
		else if (!(height <= 0))	//If there are still rows that we haven't reached then something has went wrong.
		{
			printf("\nHeight %d    Width %d\n", height, width);
			printf("Error. Height not met.\n");
			returnVal = -4; //Error has occurred. We will return this value in attempts to let the user debug.
		}

	}
	else
	{
		printf("Unsupported file format.\n");
		returnVal = -3; //Error has occurred. We will return this value in attempts to let the user debug.
	}

	fclose(picture);
	return returnVal;
}


int  main(int argc, char** argv)
{
	if (argc >= 3)
	{
		FILE* picture = fopen(argv[2], "r+b");

		if (strcmp(argv[1], "-invert") == 0)
		{
			edit(1, picture);	//Calls edit, passing through the editType to invert the picture.
		}
		else if (strcmp(argv[1], "-grayscale") == 0)
		{
			edit(2, picture);	//Calls edit, passing through the editType to grayscale the picture.
		}
		else
		{
			printf("Incorrect arguments.\n");
			return -1; //Arguments read, but not correct.
		}
	}
	else
	{
		printf("Invalid arguments.\n");
		return -2; //Invalid arguments.
	}


	return 0;
}
