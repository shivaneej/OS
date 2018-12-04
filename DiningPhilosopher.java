import java.util.*;
import java.lang.*;

class Sem
{
    int flag;
    static int num = Main.num;
    static Sem forks[]=new Sem[num];
    public Sem ()
    {
      this.flag = 1;
    }
    public void semWait()
    {
        while(flag!=1) {Thread.yield();}
        flag=0;
    }
    public void semSignal()
    {
        flag=1;
    }
}


class Phil extends Thread
{
  int n = Main.num;
  Phil(String name)
  {
      super(name);
  }
  public void run()
  {
      while(true)
      {
        int name=Integer.parseInt(Thread.currentThread().getName());
        if(name==n-1)
				{
          (Sem.forks[(name+1)%n]).semWait();
          System.out.println("Philosopher "+name+" has got right fork");
          (Sem.forks[name]).semWait();
          System.out.println("Philosopher "+name+" has got left fork");
        }
			  else
        {
          (Sem.forks[name]).semWait();
          System.out.println("Philosopher "+name+" has got left fork");
          (Sem.forks[(name+1)%n]).semWait();
          System.out.println("Philosopher "+name+" has got right fork");
        }
        System.out.println("Philosopher "+name+" is eating");
        
        try 
        {
            Random r = new Random();
            Thread.sleep(r.nextInt(500));
            
        }
        catch(Exception e) {}

        System.out.println("Philosopher "+name+" has released forks");
        (Sem.forks[name]).semSignal();
        (Sem.forks[(name+1)%n]).semSignal();
        try 
        {
          Random r = new Random();
          Thread.sleep(r.nextInt(900));
        }
        catch(Exception e) {}
      }
  }
}
class Main
{
    static int num;
    
    public static void main(String args[])
    {
        Scanner sc =new Scanner(System.in);
        System.out.println("Enter the number of philosophers");
        num=sc.nextInt();
        for(int i=0;i<num;i++)
        Sem.forks[i] = new Sem();
        for(int i=0;i<num;i++)
        {
            Phil p=new Phil(Integer.toString(i));
            p.start();
        }
    }
}
