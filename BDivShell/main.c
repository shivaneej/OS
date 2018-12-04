#include <unistd.h>
#include <sys/types.h>
#include <errno.h>
#include <stdio.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <time.h>

void main()
{
  int a[13]= {1,2,3,4,5,6,7,8,9,11,43,67,89};
  int n = fork();
  if (n>0)
  {
    clock_t start, end;
    float time_elapsed;
    printf ("Parent process has started\n");
    start = clock();
    printf ("Parent Process: %d\n", getpid());
    wait (NULL);
    printf ("Child process has ended\n");
    for (int i=0;i<13;i++)
    {
      if (a[i]%2==1)
     {
      printf ("%d\n", a[i]);
     }
    } 
    end = clock();
    time_elapsed= end - start;
    printf ("Parent process has ended\n");
    printf ("Parent process execution time is %f\n",time_elapsed);
  }
  else if (n==0)
  {
    printf ("Child process has started\n");
    printf ("Parent Process: %d\n Child process: %d\n", getppid(),getpid());
    char *args[] = {"Hello", "C", "Programming", NULL};
    execv("./child", args);
    
  }
  else 
  {
    printf ("Error");
  } 
}
