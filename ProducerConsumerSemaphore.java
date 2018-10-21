import java.util.*;
import java.lang.*;
//flag==true : can execute
//flag==false: cannot execute
class Semaphore extends Thread
{
  static boolean flag = true;
  static Semaphore s=new Semaphore();
  public static void Pwait()
  {
    String now="x";
    while(!flag)
    {
      now = Main.queue.get(0);
    }    
    Main.queue.remove(0);
    if(now.equals("c"))
      {
        new Consumer().run();
      }
      else if(now.equals("p"))
      {
        new Producer().run();
      }
  }
}
class Producer extends Thread
{
  static int i=0;
  public void run()
  {
    while(i<5)
    {
      if(Semaphore.flag==true)
      {
        if(Main.m<Main.N)
        {
          Semaphore.flag = false;
          System.out.println("Producing");
          try
          {
            Thread.sleep(400);
          }
          catch(InterruptedException e)
          {
            System.out.println(e);
          }
          Main.m++;
          System.out.println("Produced! Quantity: "+Main.m+"\n");
          i++;
          Semaphore.flag = true;
        }
        else
        {
          System.out.println("Cannot produce, container full");
          Main.queue.add("p");
          Semaphore.Pwait();
        }
        //Semaphore.flag = true;
      }
      else
      {
        System.out.println("Producer Waiting");
        Main.queue.add("p");
        Semaphore.Pwait();
      }
      try
      {
          Thread.sleep(100);
      }
      catch(InterruptedException e)
      {
        System.out.println(e);
      }
      //i++;
    }
  }
}
class Consumer extends Thread
{
  static int j=0;
  public void run()
  {
    while(j<5 || Main.m>0)
    {
      if(Semaphore.flag==true)
      {
        if(Main.m>0)
        {
          Semaphore.flag = false;
          System.out.println("Consuming");
          try
          {
            Thread.sleep(400);
          }
          catch(InterruptedException e)
          {
            System.out.println(e);
          }
          Main.m--;
          System.out.println("Consumed! Quantity: "+Main.m+"\n");
          j++;
          Semaphore.flag = true;
        }
        else
        {
          System.out.println("Cannot consume, container empty");
          Main.queue.add("c");
          Semaphore.Pwait();
        }
        //Semaphore.flag = true;
      }
      else
      {
        System.out.println("Consumer Waiting");
        Main.queue.add("c");
        Semaphore.Pwait();
      }
      try
      {
          Thread.sleep(600);
      }
      catch(InterruptedException e)
      {
        System.out.println(e);
      }
      //j++;
    } 
  }
}
public class Main
{
  static int m=0,N;
  static Vector <String> queue = new Vector<String>();
  public static void main(String arg[])
  {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter capacity");
    N=sc.nextInt();
    System.out.println("Inital quantity: "+m);
    Producer p = new Producer();
    Consumer c = new Consumer();
    p.start();
    c.start();
  }
}

