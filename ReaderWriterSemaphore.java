import java.util.*;
import java.lang.*;
import java.io.*;

class Semaphore
{
    int flag;
    static Semaphore x=new Semaphore(1);
    static Semaphore wsem=new Semaphore(1);
    static Semaphore readcount=new Semaphore(0);
    public Semaphore(int x)
    {
      flag = x;
    }
    
    public void semWait()
    {
      while(flag!=1){}
      flag=0;
    }
    public void semSignal()
    {
      flag=1;
    }
}

class Reader extends Thread
{
  public Reader(String name)
  {
    super(name);
  }

  public void run()
  {
    while(true)
    {
      try
      {
        Random r = new Random();
        Thread.sleep(r.nextInt(600));
      }
      catch(InterruptedException e){}

      (Semaphore.x).semWait();
      Semaphore.readcount.flag++;
      if(Semaphore.readcount.flag==1)
      (Semaphore.wsem).semWait();
      (Semaphore.x).semSignal();

      System.out.println("Reader "+Thread.currentThread().getName()+ " is reading");
      try
      {
        File f =new File("hello.txt");
        BufferedReader br=new BufferedReader(new FileReader(f));
        String s;
        while((s=br.readLine())!=null)
            System.out.println(s);
      }
      catch(Exception e) {}

      (Semaphore.x).semWait();
      System.out.println("Reader "+Thread.currentThread().getName()+" has stopped reading");
      Semaphore.readcount.flag--;
      if(Semaphore.readcount.flag==0)
          (Semaphore.wsem).semSignal();
      (Semaphore.x).semSignal();
    }
  }
}
class Writer extends Thread
{
  Writer(String name)
  {
      super(name);
  }
  public void run()
  {
      while(true)
      {
        (Semaphore.wsem).semWait();
        System.out.println("Writer "+Thread.currentThread().getName()+" is writing");
        try
        {
          File f =new File("hello.txt");  
          String str=" 1";
          BufferedWriter bw =new BufferedWriter(new FileWriter(f,true));
          bw.append(' ');
          bw.append(str);
          bw.close();
        }
        catch(Exception e){}
        System.out.println("Writer "+Thread.currentThread().getName()+ " has finished writing");
        (Semaphore.wsem).semSignal();

        try
      {
        Random r = new Random();
        Thread.sleep(r.nextInt(900));
      }
      catch(InterruptedException e){}
      
      }
  }
}
class Main
{
    // Semaphore x=new Semaphore(1);
    // Semaphore wsem=new Semaphore(1);
    // Semaphore readcount=new Semaphore(0);
    public static void main(String args[])
    {
        Scanner sc =new Scanner(System.in);
        System.out.println("Enter the number of readers");
        int r=sc.nextInt();
        System.out.println("Enter the number of writers");
        int w =sc.nextInt();
        for(int i=1;i<=r;i++)
        {
            Reader rw =new Reader(Integer.toString(i));
            rw.start();
        }
        for(int i=1;i<=w;i++)
        {
            Writer wr=new Writer(Integer.toString(i));
            wr.start();
        }
    }
}
