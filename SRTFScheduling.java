import java.util.*;
import java.lang.*;

class Process
{
  String Pname;
  int arrTime,sTime,wTime,tTime,cTime,start,rem;
  Boolean status,arrived;
  Process(String Pname, int arrTime, int sTime)
  {
    this.Pname=Pname;
    this.arrTime=arrTime;
    this.sTime=sTime;
    this.rem=sTime;
    this.status=false; //status true means completed
    this.arrived=false;
  }
}

class Main
{
  public static void main(String arg[])
  {
    int n,i,arrival,service,t=0,complete=0,next;
    Process current,nextObj;
    String name;
    Scanner sc=new Scanner(System.in);
    Vector <Process> list = new Vector<>();
    System.out.println("Enter number of processes");
    n=sc.nextInt();
    list.clear();
    System.out.println("SRTF Scheduling");
    System.out.println("Enter process details (Name, Arrival Time, Service Time)");
    for(i=0;i<n;i++)
    {
      name=sc.next();
      arrival=sc.nextInt();
      service=sc.nextInt();
      Process p = new Process(name,arrival,service);
      list.add(p);
    }
    list.sort(new Comparator<Process>() {
        @Override
        public int compare(Process p1, Process p2) {
           return p1.arrTime - p2.arrTime;
        }
    });
    System.out.println("Sequence of process execution is\n");
    System.out.println("Process\tStart\tEnd");
    int time=0;
    Vector <Process> seq = new Vector<>();
    seq.clear();
    while(complete!=list.size())
    {
        if(seq.isEmpty())
        {
          int z=0;
          while(list.get(z).arrived==true)
          z++;
          int newTime = list.get(z).arrTime;
          if(newTime>time)
          {
            try
            {
              Thread.sleep(newTime-time);
              time = newTime;
              System.out.println("IDLE . . . .");
            }
          catch(InterruptedException e)
          {
            System.out.println(e);
          }
          }
        }
        seq=schedule(list,seq,time);
        current = seq.get(0);
        int index = list.indexOf(current);
        next=Integer.MAX_VALUE;
        if(index<n-1)
        {    nextObj = list.get(index+1);
              if(time<nextObj.arrTime)
                next = nextObj.arrTime;
        }
        if(list.get(index).rem==list.get(index).sTime)
        list.get(index).start=time;
        if(time+list.get(index).rem<=next && list.get(index).rem>0)
        {
            int st =time;
            time = time + list.get(index).rem;
            list.get(index).rem = 0;
            System.out.println(list.get(index).Pname+"\t"+st+"\t"+time);
            list.get(index).status = true;
            seq.remove(0);
            list.get(index).cTime = time;
            list.get(index).tTime = list.get(index).cTime - list.get(index).arrTime;
            list.get(index).wTime = list.get(index).tTime - list.get(index).sTime;
            complete++;
        }
        else
        {
            int part;
            if (next==Integer.MAX_VALUE)
              part=list.get(index).rem;
            else
              part = next - time;
            list.get(index).rem -= part; 
            int st=time;
            time = time + part;
            System.out.println(current.Pname+"\t"+st+"\t"+time);
        }
        
    } 

    System.out.println("Name\tArr\tSer\tStart\tEnd\tTA\tWait");
    for(i=0;i<list.size();i++)
    {
      System.out.println(list.get(i).Pname+"\t"+list.get(i).arrTime+"\t"+list.get(i).sTime+"\t"+list.get(i).start+"\t"+list.get(i).cTime+"\t"+list.get(i).tTime+"\t"+list.get(i).wTime);
    }
    System.out.println("Average waiting time is ");
    float avg = (float) 0.0;
    float avgTurn = (float) 0.0;
    for(i=0;i<list.size();i++)
    {
      avg+=list.get(i).wTime;
      avgTurn+=list.get(i).tTime;
    }
    avg= avg/list.size();
    avgTurn = avgTurn/list.size();
    System.out.print(avg +" seconds\n");
    System.out.println("\nAverage Turnaround time is "+avgTurn+" seconds\n");
  }
  
  public static Vector <Process> schedule(Vector <Process> list,Vector <Process> seq, int currentTime)
   {
      int t=0;
    	while(t<list.size())
    	{
    		int y=list.get(t).arrTime;
	    	if(y<=currentTime)
	    	{
	    		if(!seq.contains(list.get(t)) && list.get(t).rem!=0 && list.get(t).status==false)
	    		{
            list.get(t).arrived=true;
	    			seq.add(list.get(t));
	    		}	
        }
        t++;
      }           
       seq.sort(new Comparator<Process>() {
        @Override
        public int compare(Process p1, Process p2) {
           return p1.rem - p2.rem;
        }
    }); 
       return seq;
   }
}
