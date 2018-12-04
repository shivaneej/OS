import java.util.*;

class Semaphore
{
  private int flag;
  Semaphore(int x)
  {
    flag = x;
  }
  public void semWait()
  {
    while(flag==0)
     Thread.yield();
    flag--;
  }
  public void semSignal()
  {
    flag++;
  }
}

class Container
{
  int capacity,qty;
  Container(int c)
  {
    capacity = c;
    qty = 0;
  }

  public void add()
  {
    if(capacity > qty)
    {
      qty++;
      System.out.println("Produced. Qty = "+qty);
    }
    else
    {
      System.out.println("Buffer full, cannot produce");
    }
  }
  public void remove()
  {
    if(qty>0)
    {
      qty--;
      System.out.println("Consumed. Qty = "+qty);
    }
    else
    {
      System.out.println("Buffer empty, cannot consume");
    }
  }
}

class Producer extends Thread
{
  Container c;
  Semaphore e,s,n;
  Producer(Container c,Semaphore s, Semaphore e, Semaphore n)
  {
    this.s = s;
    this.e = e;
    this.n = n;
    this.c=c;
  }
  public void run()
  {
    int i=0;
    Random r = new Random();
    for(i=0;i<10;i++)
    {
      try
      {
        Thread.sleep(r.nextInt(200));
      }
      catch (InterruptedException e)
      {

      }
      e.semWait();
      s.semWait();
      c.add();
      s.semSignal();
      n.semSignal();
    }
  }
}

class Consumer extends Thread
{
  Container c;
  Semaphore e,s,n;
  Consumer(Container c,Semaphore s, Semaphore e, Semaphore n)
  {
    this.s = s;
    this.e = e;
    this.n = n;
    this.c = c;
  }
  public void run()
  {
    int i=0;
    Random r = new Random();
    for(i=0;i<10;i++)
    {
      try
      {
        Thread.sleep(r.nextInt(200));
      }
      catch (InterruptedException e)
      {

      }
      n.semWait();
      s.semWait();
      c.remove();
      s.semSignal();
      e.semSignal();
    }
  }
}

class Main
{
  public static void main(String args[])
  {
    int size;
    Scanner sc = new Scanner (System.in);
    System.out.println("Enter capacity");
    size = sc.nextInt();
    Container ct = new Container(size);
    Semaphore s = new Semaphore (1);
    Semaphore e = new Semaphore (size);
    Semaphore n = new Semaphore (0);

    Producer p = new Producer (ct,s,e,n);
    Consumer c = new Consumer (ct,s,e,n);

    p.start();
    c.start();
  }
}
