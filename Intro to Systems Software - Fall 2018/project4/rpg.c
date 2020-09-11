//Landon Higinbotham
//I could not get my program to read string inputs with spaces in them, so just type fight to fight a random enemy.
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#define pow power
#define random rando

char pname[60];
int parmor;
int pweapon;
int php;
int plevel;
int pxp;

char armor[5][20] = {"cloth","studded leather","ring mail","chain mail","plate"};
char weapon[5][20] = { "dagger","short sword","long sword","great sword","great axe"};
void fight();
struct NPC {
	char ename[60];
	int ehealth;
	int earmor;
	int eweapon;
	int elevel;
	int exp;
};
struct NPC world[10];

void save()
{
	FILE *save; 
	
	save = fopen("rpg.save", "w"); 

	int i;
	for (i = 0; i < 10; i++)
	{
        	fwrite (&world[i], sizeof(struct NPC), 1, save);
	}

        fwrite (&parmor, sizeof(int), 1, save);
        fwrite (&pweapon, sizeof(int), 1, save);
        fwrite (&php, sizeof(int), 1, save);
        fwrite (&plevel, sizeof(int), 1, save);
        fwrite (&pxp, sizeof(int), 1, save);
	fputs (pname, save);
	
	fclose(save);
}

void load()
{
	FILE *load; 
	load = fopen ("rpg.save", "r"); 
	
	int i;
	for (i = 0; i < 10; i++)
	{
		fread(&world[i], sizeof(struct NPC), 1, load);
	}
        fread (&parmor, sizeof(int), 1, load);
        fread (&pweapon, sizeof(int), 1, load);
        fread (&php, sizeof(int), 1, load);
        fread (&plevel, sizeof(int), 1, load);
        fread (&pxp, sizeof(int), 1, load);
	
	while(feof(load))
	{
     		pname[i++] = fgetc(load);
	}
	pname[i]='\0';

	fclose(load);

}

int power(int base, int exponent)
{
	int final = base;
	int i;
	for (i; i<exponent; i++)
	{
		final = final * base;
	}
	return final;

}

int rando(int sides) //For testing purposes
{
	return rand() % sides + 1;
}

/*int roll(int sides)
{
  int fd = open("/dev/dice", O_RDWR);
  read(fd, NULL, 1024);
  write(fd, NULL, 1024);
  return 0;
}*/

void newEnemies()
{
	int i = 0;
	struct NPC Sauron;
	world[i] = Sauron;
	strcpy(world[i].ename, "Sauron");
	world[i].elevel = 20;
	world[i].exp = 1000 * pow(2, world[i].elevel);
	world[i].ehealth = 20 + (world[i].elevel - 1) * 5;
	world[i].earmor = 4;
	world[i].eweapon = 4;
	for (i = 1; i <= 8; i++)
	{
	        struct NPC Orc;
	        world[i] = Orc;
		strcpy(world[i].ename, "Orc");
	        world[i].elevel = random(plevel);
	        world[i].exp = 1000 * pow(2, world[i].elevel);
	        world[i].ehealth = 20 + (world[i].elevel - 1) * 5;
	        world[i].earmor = random(5) - 1;
	        world[i].eweapon = random(5) - 1;
		
	}
        struct NPC Gollum;
        world[i] = Gollum;
        strcpy(world[i].ename, "Gollum");	
        world[i].elevel = 1;
        world[i].exp = 1000 * pow(2, world[i].elevel);
        world[i].ehealth = 10;
        world[i].earmor = 0;
        world[i].eweapon = 0;
}

void newPlayer()
{
	printf("What is your name?\n");
	scanf("%s", pname);
	plevel = 1;
	php = 20;
	pxp = 2000;
}

void playerStats();

void resetPlayer()
{
	printf("%sRespawning %s...", &pname);
	php = 20 + (plevel - 1) * 5;
	pxp = 1000 * pow(2, plevel);
	playerStats();
}

void playerStats()	//Prints out player's stats
{
	printf("[%s: hp=%d, armor=%s, weapon=%s, level=%d, xp=%d]\n", &pname, php, &armor[parmor], &weapon[pweapon], plevel, pxp);
}

void playerSetup()	//Allows users to choose items for Player1 and Player 2
{
	newPlayer();

	printf("\nList of available armors:\n");
	printf("0: cloth (AC=10)\n");
	printf("1: studded leather (AC=12)\n");
	printf("2: ring mail (AC=14)\n");
	printf("3: chain mail (AC=16)\n");
	printf("4: plate(AC = 18)\n\n");

	parmor = -1;
	while (parmor < 0 || parmor > 4)	//This loop makes sure the user inputs only the asked values.
	{
		printf("Choose %s's Armor (0~4):", &pname);
		if (scanf("%d", &parmor) < 1)
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

	pweapon = -1;
	while (pweapon < 0 || pweapon > 4)	//This loop makes sure the user inputs only the asked values.
	{
		printf("Choose %s's Weapon(0~4):", &pname);
		if (scanf("%d", &pweapon) < 1)
		{
			int t;
			while ((t = getchar()) != '\n');
		}
	}

	printf("\nPlayer setting complete:\n");
	playerStats();
}

void npcStats(int i)
{
	printf("%d: [%s: hp=%d, armor=%s, weapon=%s, level=%d, xp=%d]\n", i, &world[i].ename, world[i].ehealth, &armor[world[i].earmor], &weapon[world[i].eweapon], world[i].elevel, world[i].exp);
}

void respawn (i)
{
	if (i == 0)
	{
		struct NPC Sauron;
	        world[i] = Sauron;
	        strcpy(world[i].ename, "Sauron");
	        world[i].elevel = 20;
	        world[i].exp = 1000 * pow(2, world[i].elevel);
	        world[i].ehealth = 20 + (world[i].elevel - 1) * 5;
	        world[i].earmor = 4;
	        world[i].eweapon = 4;
	}
	else if (i == 9)
	{
		struct NPC Gollum;
	        world[i] = Gollum;
	        strcpy(world[i].ename, "Gollum");
	        world[i].elevel = 1;
	        world[i].exp = 1000 * pow(2, world[i].elevel);
	        world[i].ehealth = 10;
	        world[i].earmor = 0;
	        world[i].eweapon = 0;
	}
	else
	{
		struct NPC Orc;
                world[i] = Orc;
                strcpy(world[i].ename, "Orc");
                world[i].elevel = random(plevel);
                world[i].exp = 1000 * pow(2, world[i].elevel);
                world[i].ehealth = 20 + (world[i].elevel - 1) * 5;
                world[i].earmor = random(5) - 1;
                world[i].eweapon = random(5) - 1;
	}
}

void loot(int i)
{
	char a1[3];
	char a2[3];
	printf("Get %s's %s, exchanging %s's current armor %s (y/n)?", &world[i].ename, &armor[world[i].earmor], &pname, &armor[parmor]);
        scanf("%s", a1);
	printf("Get %s's %s, exchanging %s's current weapon %s (y/n)?", &world[i].ename, &weapon[world[i].eweapon], &pname, &weapon[pweapon]);
	scanf("%s", a2);

	if (strcmp(a1, "y") == 0)
	{
		parmor = world[i].earmor;
	}

	if (strcmp(a2, "y") == 0)
        {
                pweapon = world[i].eweapon;
        }
	respawn(i);	
}

void showWorld()
{
	printf("All is peaceful in the land of Mordor.\n");
	int i = 0;
	for (i; i < 10; i++)
	{
		npcStats(i);
	}
	printf("Also at the scene are some adventurers looking for trouble:\n");
        printf("0: [%s: hp=%d, armor=%s, weapon=%s, level=%d, xp=%d]\n", &pname, php, &armor[parmor], &weapon[pweapon], plevel, pxp);
}

void fightNPC(int E)
{
        int roll;
        int damage1 = 0;
        int damage2 = 0;

        roll = random(20);

        if (roll >= (10 + 2 * world[E].earmor)) //Checks to see if the roll can do damage
        {
                if (pweapon <= 2 && pweapon >= 0)       //Rolls for damage depending on which weapon
                {
                        damage1 = random(2*pweapon + 4);
                }
                else if (pweapon == 3)
                {
                        damage1 = random(6);
                        damage1 = damage1 + random(6);  //Since this weapon allows two rolls, we make sure to add both.
                }
                else if (pweapon == 4)
                {
                        damage1 = random(13);
                }
                world[E].ehealth = world[E].ehealth - damage1;  //Applies damage to Player2
                printf("%s hits %s for %d damage (attack roll %d)\n", &pname, &world[E].ename, damage1, roll);
        }
        else    //Player missed.
        {
                printf("%s misses %s (attack roll %d)\n", &pname, &world[E].ename, roll);
        }

        roll = random(20);      //This next portion is the same as the first portion, but Player2 is attacking Player1

        if (roll >= (10 + 2 * parmor))
        {
                if (world[E].eweapon <= 2 && world[E].eweapon >= 0)
                {
                        damage2 = random(2 * world[E].eweapon + 4);
                }
                else if (world[E].eweapon == 3)
                {
                        damage2 = random(6);
                        damage2 = damage2 + (random(6));
                }
                else if (world[E].eweapon == 4)
                {
                        damage2 = random(12);
                }
                php = php - damage2;
                printf("%s hits %s for %d damage (attack roll %d)\n", &world[E].ename, &pname, damage2, roll);
        }
        else
        {
                printf("%s misses %s (attack roll %d)\n", &world[E].ename, &pname, roll);
        }
        if (world[E].ehealth <= 0 && php > 0)
        {
                printf("\n%s is killed by %s\n", &world[E].ename, &pname);
                pxp = pxp + (world[E].elevel * 2000);
		while (pxp >  (1000 * pow(2, plevel)))
		{
			plevel = plevel + 1;
		}
		php = 20 + (plevel - 1) * 5;
		loot(E);
        }
        else if (php <= 0 && world[E].ehealth > 0)
        {
                printf("\n%s is killed by %s\n", &pname, &world[E].ename);
		resetPlayer();
        }
        else
        {
                fightNPC(E);
        }
}



int getInput()
{
	char input[60];
	scanf("%s", input);
	input[59] = 0;
	printf("We got %s!", input);
	if (strcmp(input, "quit") == 0)
	{
		save();
		return 0;
	}
	else if(strcmp(input, "stats") == 0)
	{
		playerStats();
	}
        else if(strcmp(input, "look") == 0)
        {
		showWorld();
        }
        else if (strcmp(input, "fight") == 0)
	{
		scanf("%s", input);
		printf("We got %s!", input);
		fightNPC(random(10)-1);
	}
	else
	{
		printf("Unknown command\n");
	}
	return 1;
}

void initializeNewWorld()
{
	playerSetup();
	newEnemies();	
}

int main()
{
	FILE *loader;  
	loader = fopen("rpg.save", "r"); 
	if (loader == NULL) 
	{ 
		initializeNewWorld();
	}
	else
	{
		char response[4];
		printf("Found save file. Continue where you left off (y/n)?");
		scanf("%s", response);
		if (strcmp(response, "y") == 0)
		{		
			load();
		}
		else
		{
			initializeNewWorld();
		}
	}
	srand((unsigned int)time(NULL));
	int cont = 1;
	while(cont == 1)
	{
		cont = getInput();
	}

	return 0;
}
