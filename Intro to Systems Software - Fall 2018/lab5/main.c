#include <stdio.h>

void my_strcpy(char *dest, char *src);
  
int main() {
  char dest[100];
  char *src = "Hello World!";

  my_strcpy(dest, src);

  printf ("%s\n", dest);

  return 0;
}
