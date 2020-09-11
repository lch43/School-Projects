Hello! This is Landon Higinbotham here!
I included the original testing drivers, the only thing I changed was I added
#include "mymalloc.h"
in all of them. I also changed
#define MALLOC
#define FREE
#define DUMP_HEAP()
to
#define MALLOC my_malloc
#define FREE my_free
#define DUMP_HEAP() dump_heap()

To compile use
gcc mymalloc.c [INSERT DRIVER FILE] -o [INSERT OUTPUT FILE]
and switch "[INSERT DRIVER FILE]" with the name of the driver you want to use, and switch "[INSERT OUTPUT FILE]" with the filename you want to output it as.

Everything should work properly.
