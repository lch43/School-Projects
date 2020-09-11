//Landon Higinbotham
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>




int armor1;
int armor2;
int weapon1;
int weapon2;
int hp1;
int hp2;
int rounds;
char armor[5][20] = {"cloth","studded leather","ring mail","chain mail","plate"};
char weapon[5][20] = { "dagger","short sword","long sword","great sword","great axe"};
void fight();

void resetHP()
{
	hp1 = 20;
	hp2 = 20;
}

void playerStats()	//Prints out player's stats
{
	printf("[Player 1: hp=%d, armor=%s, weapon=%s]\n", hp1, &armor[armor1][0], &weapon[weapon1][0]);
	printf("[Player 2: hp=%d, armor=%s, weapon=%s]\n", hp2, &armor[armor2][0], &weapon[weapon2][0]);
}

void playerSetup()	//Allows users to choose items for Player1 and Player 2
{
	resetHP();

	printf("List of available armours:\n");
	printf("0: cloth (AC=10)\n");
	printf("1: studded leather (AC=12)\n");
	printf("2: ring mail (AC=14)\n");
	printf("3: chain mail (AC=16)\n");
	printf("4: plate(AC = 18)\n\n");

	armor1 = -1;
	while (armor1 < 0 || armor1>4)	//This loop makes sure the user inputs only the asked values.
	{
		printf("Choose Player 1 Armor (0~4):");
		if (scanf("%d", &armor1) < 1)
		{
			int t;
			while ((t = getchar()) != '\n');
		}
	}
	armor2 = -1;
	while (armor2 < 0 || armor2>4)	//This loop makes sure the user inputs only the asked values.
	{
		printf("Choose Player 2 Armor (0~4):");
		if (scanf("%d", &armor2) < 1)
		{
			int t;
			while ((t = getchar()) != '\n');
		}
	}

	printf("\nList of available weapons:\n");
	printf("0: dagger (damage=1d4)\n");
	printf("1: short sword (damage=1d6)\n");
	printf("2: long sword (damage=1d8)\n");
	printf("3: great sword (damage=2d6)\n");
	printf("4: great axe (damage = 1d12)\n\n");

	weapon1 = -1;
	while (weapon1 < 0 || weapon1>4)	//This loop makes sure the user inputs only the asked values.
	{
		printf("Choose Player 1 Weapon(0~4):");
		if (scanf("%d", &weapon1) < 1)
		{
			int t;
			while ((t = getchar()) != '\n');
		}
	}
	weapon2 = -1;
	while (weapon2 < 0 || weapon2>4)	//This loop makes sure the user inputs only the asked values.
	{
		printf("Choose Player 2 Weapon(0~4):");
		if (scanf("%d", &weapon2) < 1)
		{
			int t;
			while ((t = getchar()) != '\n');
		}
	}

	printf("\nPlayer setting complete:\n");
	playerStats();
}

void fightStage()	//Stage that simulates the fight calculating the hits and damage done
{
	int roll;
	int damage1 = 0;
	int damage2 = 0;
	rounds++;

	printf("----Round %d-----\n", rounds);

	roll = rand() % (20 - 0 + 1);

	if (roll >= (10 + 2 * armor2))	//Checks to see if the roll can do damage
	{
		if (weapon1 <= 2 && weapon1 >= 0)	//Rolls for damage depending on which weapon
		{
			damage1 = rand() % (2*weapon1 + 5);
		}
		else if (weapon1 == 3)
		{
			damage1 = rand() % 7;
			damage1 = damage1 + (rand() % 7);	//Since this weapon allows two rolls, we make sure to add both.
		}
		else if (weapon1 == 4)
		{
			damage1 = rand() % 13;
		}
		hp2 = hp2 - damage1;	//Applies damage to Player2
		printf("Player 1 hits Player 2 for %d damage (attack roll %d)\n", damage1, roll);
	}
	else	//Player missed.
	{
		printf("Player 1 misses Player 2 (attack roll %d)\n", roll);
	}

	roll = rand() % (20 - 0 + 1);	//This next portion is the same as the first portion, but Player2 is attacking Player1

	if (roll >= (10 + 2 * armor1))
	{
		if (weapon2 <= 2 && weapon2 >= 0)
		{
			damage2 = rand() % (2 * weapon2 + 5);
		}
		else if (weapon2 == 3)
		{
			damage2 = rand() % 7;
			damage2 = damage2 + (rand() % 7);
		}
		else if (weapon2 == 4)
		{
			damage2 = rand() % 13;
		}
		hp1 = hp1 - damage2;
		printf("Player 2 hits Player 1 for %d damage (attack roll %d)\n", damage2, roll);
	}
	else
	{
		printf("Player 2 misses Player 1 (attack roll %d)\n", roll);
	}

	playerStats();	//Displays the player's stats.
}

void fight()
{

	char choice[30];
	do
	{
		do
		{
			printf("\n");
			printf("Fight? (y/n):\n");
			scanf("%s", &choice);
		} while ((strcmp(choice, "Y") != 0) && (strcmp(choice, "y") != 0) && (strcmp(choice, "N") != 0) && (strcmp(choice, "n") != 0));	//Makes sure the player either enters y or n (not case sensitive)

		if ((strcmp(choice, "Y") == 0) || (strcmp(choice, "y") == 0))	//If the player replied y we will enter the fight simulation.
		{
			resetHP();	//Reset the player's stats.
			rounds = 0;
			printf("\n");

			while (hp1 > 0 && hp2 > 0)	//Loop until a player loses.
			{
				fightStage();
				if (hp1 <= 0 || hp2 <= 0)	//If a player died...
				{
					if (hp1 <= 0 && hp2 <= 0)	//See if they had a draw.
					{
						printf("\nDRAW!\n");
					}
					else if (hp2 <= 0)	//See if Player1 won
					{
						printf("\nPlayer 1 WINS!\n");
					}
					else	//See if Player2 won
					{
						printf("\nPlayer 2 WINS!\n");
					}
				}
			}
		}
	} while ((strcmp(choice, "Y") == 0) || (strcmp(choice, "y") == 0));	//Loops until the user does not want to simulate any more battles.
}

int main()
{
	srand((unsigned int)time(NULL));
	playerSetup();
	fight();

	return 0;
}
