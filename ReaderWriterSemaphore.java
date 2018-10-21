import java.util.*;
import java.lang.*;
import java.io.*;

class Semaphore extends Thread
{
  static boolean canWrite= true;
  static boolean canRead = true; 
  public static void Pwait()
  {
    String now=" ";
    while(canRead == false)
    {
     //do nothing
    }
    if(Main.queue.isEmpty() == false)
        now = Main.queue.get(0);
    if(now.equals("r"))
    {
      int occurrences = Collections.frequency(Main.queue, "r");
        for(int k=0;k<occurrences;k++)
        {
            new Reader().run();
            Main.queue.remove("r");
        }
    }
    else if(canWrite==true && now.equals("w"))
    {
      Main.queue.remove(0);
    }
  }
}

class Reader extends Thread
{
  static int i=0;
  public void run()
  {
    while(i<5)
    {
      if(Semaphore.canRead)
      {
        Semaphore.canRead=true;
        Semaphore.canWrite=false;
        System.out.println("Reading started");
        //display text read
        String filename="shivanee.txt";
        String line;
        try 
        {
          FileReader fileReader = new FileReader(filename);
          BufferedReader br = new BufferedReader(fileReader);
          line=br.readLine();
          while(line!=null) 
          {
            System.out.println(line);
            line=br.readLine();
          }   
          br.close();         
        }
        catch(FileNotFoundException e) 
        {
          System.out.println("Unable to open file"); 
        }
        catch(IOException e) 
        {
          System.out.println("Error reading file");  
        }
        i++;
        System.out.println("Reading completed");
        Semaphore.canRead=true;
        Semaphore.canWrite=true;
      }
      else
      {
        System.out.println("Waiting to read");
        Main.queue.add("r");
        Semaphore.Pwait();
      }
      Semaphore.Pwait();
       /*try
        {
          Thread.sleep(100);
        }
        catch(InterruptedException e)
        {
          System.out.println(e);
        }*/
    }
  }
}
class Writer extends Thread
{
  static int j=0;
  public void run()
  {
    while(j<5)
    {
      if(Semaphore.canWrite)
      {
        Semaphore.canRead=false;
        Semaphore.canWrite=false;
        System.out.println("Writing started\nEnter text:");
        String filename = "shivanee.txt";
        try 
        {
          FileWriter fileWriter=new FileWriter(filename);
          BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
          Scanner sc=new Scanner(System.in);
          String ip=sc.next();
          bufferedWriter.write(ip);
          bufferedWriter.close();
        }
        catch(FileNotFoundException e) 
        {
          System.out.println("Unable to open file"); 
        }
        catch(IOException e) 
        {
          System.out.println("Error writing file");  
        }
        j++;
        System.out.println("Writing completed");
        Semaphore.canRead=true;
        Semaphore.canWrite=true;
      }
      else
      {
        System.out.println("Waiting to write");
        Main.queue.add("r");
        Semaphore.Pwait();
      }
      Semaphore.Pwait();
       /*try
        {
          Thread.sleep(100);
        }
        catch(InterruptedException e)
        {
          System.out.println(e);
        }*/
    }
  }
} 
class Main
{
  static Vector <String> queue = new Vector<String>();
  public static void main(String args[])
  {
      Reader r=new Reader();
      Writer w=new Writer();
      r.start();
      w.start();
  }
}
