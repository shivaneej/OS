#include<stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <errno.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <time.h>

void main()
{
clock_t start, end;
double time_elapsed;
printf ("The contents of the file are\n");
start= clock();
FILE *ptr;
ptr = fopen("Hello1.txt","r");
if (ptr == NULL)
{
printf ("File error");
}
else
{
char c= fgetc (ptr);
while (c!= EOF)
{
  printf ("%c",c);
  c= fgetc (ptr);
}
}
fclose (ptr);
end = clock();
 time_elapsed= end - start;
   printf ("Child process ends\n");
   printf ("Child process execution time is %f\n",time_elapsed);
}
