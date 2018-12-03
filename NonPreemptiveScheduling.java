import java.util.*;
import java.lang.*;

class Process{
  String Pname;
  int arrTime,sTime,wTime,tTime,prio,cTime,start;
  Boolean status; //status = true means placed in queue
  Process(String Pname, int arrTime, int sTime, int prio)
  {
    this.Pname=Pname;
    this.arrTime=arrTime;
    this.sTime=sTime;
    this.prio=prio;
    this.status = false;
  }
  Process(String Pname, int arrTime, int sTime)
  {
    this.Pname=Pname;
    this.arrTime=arrTime;
    this.sTime=sTime;
    this.status = false;
  }
}

class Main
{
  public static void main(String arg[])
  {
    int n,ch,i,arrival,service,priority;
    String name;
    Scanner sc=new Scanner(System.in);
    Vector <Process> list = new Vector<>();
    System.out.println("Enter number of processes");
    n=sc.nextInt();
    list.clear();
    System.out.println("Select scheduling algorithm:\n1.FCFS\n2.SJF\n3.Priority");
    ch=sc.nextInt();
    switch(ch)
    {
      case 1:
            System.out.println("Enter process details (Name, Arrival Time, Service Time)");
            for(i=0;i<n;i++)
            {
              name=sc.next();
              arrival=sc.nextInt();
              service=sc.nextInt();
              Process p = new Process(name,arrival,service);
              list.add(p);
            }
            fcfs(list);
            break;
      case 2:
            System.out.println("Enter process details (Name, Arrival Time, Service Time)");
            for(i=0;i<n;i++)
            {
              name=sc.next();
              arrival=sc.nextInt();
              service=sc.nextInt();
              Process p = new Process(name,arrival,service);
              list.add(p);
            }
            sjf(list);
            break;
      case 3:
            System.out.println("Enter process details (Name, Arrival Time, Service Time, Priority)");
            for(i=0;i<n;i++)
            {
              name=sc.next();
              arrival=sc.nextInt();
              service=sc.nextInt();
              priority=sc.nextInt();
              Process p = new Process(name,arrival,service,priority);
              list.add(p);
            }
            prioScheduling(list);
            break;
      default:
            System.out.println("Invalid");
            break;
    }
  }

  public static void fcfs(Vector <Process> list)
  {
    System.out.println("Scheduling by FCFS algorithm");
    int t=0;
    list=sortByArr(list);
    Vector <Process> seq = new Vector<>();
    seq.clear();
    int time = 0;
    while(t<list.size())
    {
        int x = list.get(t).arrTime;
        try 
        {
            if(time<x)
            {    
                Thread.sleep(x-time);
                time = x;
            }
            list.get(t).start = time;
            list.get(t).cTime = list.get(t).start + list.get(t).sTime;
            list.get(t).tTime = list.get(t).cTime - list.get(t).arrTime;
            list.get(t).wTime = list.get(t).tTime - list.get(t).sTime;
            seq.add(list.get(t));
            time = time + list.get(t).sTime;
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
        t++;
    } 
    print(seq);
  }


  public static void sjf(Vector <Process> list)
  {
    System.out.println("Scheduling by SJF algorithm");
    list=sortByArr(list);
    Vector <Process> seq = new Vector<>();
    Vector <Process> temp = new Vector<>();
    seq.clear();
    temp.clear();
    int time = 0,count=0;
    while(count < list.size())
    {
        int x = list.get(count).arrTime;
        try 
        {
            if(time<x)
            {    
                Thread.sleep(x-time);
                time = x;
            }
        }
        catch(InterruptedException e)
        {
           e.printStackTrace();
        }
        temp = schedule(list,temp,time,count,1);
        count++;
        Process obj = temp.remove(0);
        int index = list.indexOf(obj);
        list.get(index).start = time;
        list.get(index).cTime = list.get(index).start + list.get(index).sTime;
        list.get(index).tTime = list.get(index).cTime - list.get(index).arrTime;
        list.get(index).wTime = list.get(index).tTime - list.get(index).sTime;
        seq.add(list.get(index));
        time = time + list.get(index).sTime;
    } 
    print(seq);
  }


  public static void prioScheduling(Vector <Process> list)
  {
    System.out.println("Priority");
    list=sortByArr(list);
    Vector <Process> seq = new Vector<>();
    Vector <Process> temp = new Vector<>();
    seq.clear();
    temp.clear();
    int time = 0,count=0;
    while(count < list.size())
    {
        int x = list.get(count).arrTime;
        try 
        {
            if(time<x)
            {    
                Thread.sleep(x-time);
                time = x;
            }
        }
        catch(InterruptedException e)
        {
           e.printStackTrace();
        }
        temp = schedule(list,temp,time,count,2);
        count++;
        Process obj = temp.remove(0);
        int index = list.indexOf(obj);
        list.get(index).start = time;
        list.get(index).cTime = list.get(index).start + list.get(index).sTime;
        list.get(index).tTime = list.get(index).cTime - list.get(index).arrTime;
        list.get(index).wTime = list.get(index).tTime - list.get(index).sTime;
        seq.add(list.get(index));
        time = time + list.get(index).sTime;
    } 
    print(seq);
  }

  public static Vector <Process> schedule (Vector <Process> list,Vector <Process> seq,int time, int count,int k)
  {
        while(count<list.size())
        {
            int x = list.get(count).arrTime;
            if (x<=time && list.get(count).status==false)
            { 
                  list.get(count).status=true;
                  seq.add(list.get(count));
            }
            count++;
          
        }
        if(k==1)
            seq.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) 
            {
                return p1.sTime - p2.sTime;
            }
             });
        else if(k==2)
            seq.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) 
            {
                return p1.prio - p2.prio;
            }
            });
      return seq; 
  }


  public static Vector<Process> sortByArr(Vector <Process> list)
  {
     list.sort(new Comparator<Process>() {
        @Override
        public int compare(Process p1, Process p2) {
            return p1.arrTime - p2.arrTime;
        }
    });
    return list;
  }

  public static void print(Vector <Process> seq)
  {
     System.out.println("Name\tArr\tSer\tStart\tEnd\tTA\tWait");
    for(int i=0;i<seq.size();i++)
    {
      if(i>0)
      {
        if(seq.get(i-1).cTime!=seq.get(i).start)
          System.out.println(". . . . . . . . . . IDLE . . . . . . . . .");
      }
      if(i==0)
      {
        if(seq.get(i).start!=0)
        System.out.println(". . . . . . . . . . IDLE . . . . . . . . .");
      }
      System.out.println(seq.get(i).Pname+"\t"+seq.get(i).arrTime+"\t"+seq.get(i).sTime+"\t"+seq.get(i).start+"\t"+seq.get(i).cTime+"\t"+seq.get(i).tTime+"\t"+seq.get(i).wTime);
    }
    System.out.println("Average waiting time is ");
    float avg = (float) 0.0;
    float avgTurn = (float) 0.0;
    for(int i=0;i<seq.size();i++)
    {
      avg+=seq.get(i).wTime;
      avgTurn+=seq.get(i).tTime;
    }
    avg= avg/seq.size();
    avgTurn = avgTurn/seq.size();
    System.out.print(avg +" seconds\n");
    System.out.println("\nAverage Turnaround time is "+avgTurn+" seconds\n");
  }
}
