import java.util.*;
import java.lang.*;

public class Main
{
  public static void main(String args[])
  {
    int r,p,i,j,k=0,index;
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter number of processes");
    p = sc.nextInt();
    System.out.println("Enter number of types of resources");
    r = sc.nextInt();
    int resource[] = new int[r];
    int available[] = new int[r];
    int claim[][] = new int[p][r];
    int allocation[][] = new int[p][r];
    for(i=0;i<r;i++)
    {
      System.out.println("Enter number of resources of type R"+(i+1));
      resource[i] = sc.nextInt();
    }
    System.out.println("Enter claim and allocation matrix");
    for(i=0;i<p;i++)
    {
      System.out.println("For process P"+(i+1));
      for(j=0;j<r;j++)
      {
        System.out.println("\tFor resource R"+(j+1));
        claim[i][j] = sc.nextInt();
        allocation[i][j] = sc.nextInt();
        if(allocation[i][j]>claim[i][j])
        {
          System.out.println("Allocated resources cannot be greater than claim");
          System.exit(-1);
        }
      }
    }
    for(i=0;i<r;i++)
    {
      available[i]= resource[i] - sum(allocation,i);
    }
    if(validate(resource,available,claim,allocation)==1)
    {
      System.out.println("Resources insufficient");
      System.exit(-1);
    }
    else if(validate(resource,available,claim,allocation)==2)
    {
      System.out.println("All resources should be available or allocated");
      System.exit(-1);
    }
    while(k<p)
    {
      index = check(claim,allocation,available);
      if(index==-1)
      {
        System.out.println("System is in deadlock");
        System.exit(-1);
      }
      else
      {
        System.out.println("\nProcess P"+(index+1)+" can be run to completion");
        for(j=0;j<r;j++)
        {
          available[j]+=allocation[index][j];
          allocation[index][j] = 0;
          claim[index][j] = 0;
        }
        k++;
        printRes(claim,allocation,available);
      }
    }
  }

  public static int sum(int allocation[][],int i)
  {
    int sum=0,j;
    for(j=0;j<allocation.length;j++)
      sum=sum + allocation[j][i];
    return sum;
  }

  public static int validate(int r[],int avl[],int c[][],int all[][])
  {
    int i,j;
    for(i=0;i<r.length;i++)
    {
      if(r[i]<sum(c,i))
      return 1;
      else if (r[i]!=(avl[i]+sum(all,i)))
      return 2;
    }
    return 0;
  }
  public static int check(int claim[][], int allocation[][], int available[])
  {
    int index=-1,i,j,count;
    outer:
    for(i=0;i<claim.length;i++)
    {
      count=0;
      for(j=0;j<claim[0].length;j++)
      {
        if(claim[i][j]==0)
          count++;
        if((claim[i][j]-allocation[i][j])>available[j])
        continue outer;
      }
      if(count == claim[0].length)
        continue outer;
      else
        return i;
    }
    return index;
  }

  public static void printRes(int claim[][], int allocation[][], int available[])
  {
    int i,j;
    System.out.println("\nClaim matrix:\n");
    for(i=0;i<claim.length;i++)
    {
      for(j=0;j<claim[0].length;j++)
        System.out.print(claim[i][j]+"\t");
      System.out.print("\n");
    }
    System.out.print("\n");
    System.out.println("Allocation matrix:\n");
    for(i=0;i<allocation.length;i++)
    {
      for(j=0;j<allocation[0].length;j++)
        System.out.print(allocation[i][j]+"\t");
      System.out.print("\n");
    }
    System.out.print("\n");
    System.out.println("Available vector:\n");
    for(i=0;i<available.length;i++)
    {
        System.out.print(available[i]+"\t");
    }
  }
}
