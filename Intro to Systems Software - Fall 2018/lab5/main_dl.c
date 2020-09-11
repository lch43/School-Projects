#include <stdlib.h>
#include <stdio.h>
#include <dlfcn.h>

int main() {
  void *handle;
  void (*my_strcat_copy)(char *, char *);
  void (*my_str_copy)(char *, char *); char *error;
  handle = dlopen("libmystr.so", RTLD_LAZY);
  if(!handle) { //handle == NULL
    printf("%s\n", dlerror()); //dlerror gives us a string with the error
    exit(1);
  }
  dlerror(); // Clear any existing error
  my_str_copy = dlsym(handle, "my_strcpy"); //lookup the function by name if ((error = dlerror()) != NULL) {
  if ((error = dlerror()) != NULL) {
    printf ("%s\n", error);
    exit(1);
}  
  dlerror(); // Clear any existing error
  my_strcat_copy = dlsym(handle, "my_strcat"); //lookup the function by name if ((error = dlerror()) != NULL) {
  if ((error = dlerror()) != NULL) {
    printf ("%s\n", error);
    exit(1);

  }
  //Let’s test it.
  char dest[1000];
  char *src = "Hello World!";
  my_str_copy(dest, src);
  printf ("%s\n", dest);
  my_strcat_copy(dest, " Hello again!");
  printf ("%s\n", dest);
  dlclose(handle);
  return 0;
}
