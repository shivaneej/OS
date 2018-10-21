import java.util.*;
import java.lang.*;

class Philosopher extends Thread
{
  int id,status;
  //status=0 idle
  //status=1 hungry
  //status=2 eating
  Philosopher(int id)
  {
    this.id=id;
    this.status=0;
  }

  public void run()
  {
    int x =this.id;
    int left, right;
    left = (Main.N+x)%Main.N;
    right = (Main.N+x+1)%Main.N;
    if(Main.fork.get(left) && Main.fork.get(right))
    {
      Main.fork.set(left,false);
      Main.fork.set(right,false);
      Main.v.get(x).status=2;
      System.out.println("Philosopher "+x+" started eating");
      try
      {
        Thread.sleep(500);
      }
      catch(InterruptedException e)
      {
        System.out.println(e);
      }
      System.out.println("Philosopher "+x+" finished eating");
      Main.fork.set(left,true);
      Main.fork.set(right,true);
       try
      {
        Thread.sleep(1500);
      }
      catch(InterruptedException e)
      {
        System.out.println(e);
      }
      Main.v.get(x).status=0;
    }
    else
    {
      System.out.println("Philosopher "+x+" is WAITING");
       try
      {
        Thread.sleep(200);
      }
      catch(InterruptedException e)
      {
        System.out.println(e);
      }
      run();
    }
  }
}

class Main 
{
  static int N;
  static Vector <Boolean> fork  = new Vector <>();
  static Vector <Philosopher> v = new Vector <>(); //list of philosophers
  static ArrayList <Integer> random = new ArrayList<Integer>();
  public static void main(String[] args) 
  {
    int i,num,x;
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter number of philosophers");
    N = sc.nextInt();
    for(i=0;i<N;i++)
    {
      fork.add(true);
      Philosopher p = new Philosopher(i);
      v.add(p);
      random.add(i);
    }
    Random r = new Random();
    outer:
    while(true)
    {
      num = r.nextInt(N) + 1;
      Collections.shuffle(random);
      ArrayList <Integer> current = new ArrayList <Integer> (random.subList(0,num));
      for(i=0;i<current.size();i++)
      {
        x = random.get(i);
        if(v.get(x).status==0 && v.get(x).isAlive()==false)
        {
          v.get(x).status=1;
          System.out.println("Philosopher "+x+" is hungry");
          try
          {
            Thread t = new Thread(v.get(x));
             t.start();
          }
          catch (IllegalThreadStateException e)
          {
            continue outer;
          }
         
        }
        else
        continue outer;
      }
    }

  }
}
