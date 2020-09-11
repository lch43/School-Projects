#include <stdio.h>

int main()
{

double weight;

printf("Please enter the weight you'd like to convert:");

scanf("%lf", &weight);

printf("\nHere is your weight on other planets:\n\n");
printf("Mercury\t%lf\n", weight*0.38);
printf("Venus\t%lf\n", weight*0.91);
printf("Mars\t%lf\n", weight*0.38);
printf("Jupiter\t%lf\n", weight*2.54);
printf("Saturn\t%lf\n", weight*1.08);
printf("Uranus\t%lf\n", weight*0.91);
printf("Neptune\t%lf\n", weight*1.19);
printf("Pluto\t%lf\n", weight*0.06);


}
