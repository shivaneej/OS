#include <stdio.h>
#include <sys/types.h>
#include <fcntl.h> //file control
#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <time.h>
#include <errno.h>


void main()
{
    pid_t cid;
    cid = fork();
    if (cid >= 0)
    {
   	 if(cid == 0)
   	 {
   		 //execute child process
   		 printf("\nChild process executing");
   		 printf("\nProcess ID of child process is %d",getpid());
   		 printf("\nProcess ID of parent process is %d\n",getppid());
   		 int file = open("shivanee.txt",O_RDONLY);
   		 int size;
   		 char *c = (char *) calloc(100, sizeof(char));
 
   		 if(file == -1)
   		 {   	 
   			 printf("\nPermisson Denied");
   			 exit(0);
   		 }
   		 size = read(file,c,20);
   		 c[size]='\0';
   		 printf("%s\n", c);
     		 close(file);
		printf("\nChild process completed");

   	 }
   	 else
   	 {
   		 //execute parent process
   		 printf("\nParent process executing");
   		 printf("\nProcess ID of current process is %d",getpid());
		 wait(NULL);
   		 int x = 5,i;
   		 for(i=1;i<=x;i++)
   		 printf("\n %d",i);
		printf("\nParent process completed");

   	 }
    }
    else
    {
   	 perror("Error");
   	 exit(0);
    }




}
