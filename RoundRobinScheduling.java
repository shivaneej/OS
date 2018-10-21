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
    this.status=false; //scheduled in queue or not for the first time 
    this.arrived=false;
  }
}

class Main
{
  public static void main(String arg[])
  {
    int n,i,arrival,service,q,t=0,complete=0;
    String name;
    Scanner sc=new Scanner(System.in);
    Vector <Process> list = new Vector<>();
    System.out.println("Enter number of processes");
    n=sc.nextInt();
    list.clear();
    System.out.println("Round Robin Scheduling");
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
    System.out.println("Enter time slice");
    q=sc.nextInt();
    System.out.println("Sequence of process execution is\n");
    System.out.println("Process\tStart\tEnd");
    int time=0;
    ArrayDeque <Process> seq = new ArrayDeque<>();
    seq.clear();
    while(complete!=n)
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
        if(!seq.isEmpty())
        {  
            if(seq.getFirst().rem == seq.getFirst().sTime)
          {
            seq.getFirst().start = time;
          }
          if(seq.getFirst().rem>q)
          {
            //run for q duration and update rem = rem -q
            seq.getFirst().rem-=q;
            int st=time;
            time = time + q;
            seq=schedule(list,seq,time);
            System.out.println(seq.getFirst().Pname+"\t"+st+"\t"+time);
            Process obj = seq.pollFirst();
            seq.addLast(obj);

          }
          else //if(seq.getFirst().rem<=q)
          {
                //run for rem duration and go to next
            int st=time;
            time = time +seq.getFirst().rem;
            seq=schedule(list,seq,time);
            seq.getFirst().rem = 0;
            System.out.println(seq.getFirst().Pname+"\t"+st+"\t"+time);
            Process obj = seq.pollFirst();
            int index = list.indexOf(obj);
            list.get(index).cTime = time;
            list.get(index).tTime = list.get(index).cTime - list.get(index).arrTime;
            list.get(index).wTime = list.get(index).tTime - list.get(index).sTime;
            complete++;
                  
          } 
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
  
  public static ArrayDeque <Process> schedule(Vector <Process> list,ArrayDeque <Process> seq, int currentTime)
   {
       int t=0;
    	while(t<list.size())
    	{
    		int y=list.get(t).arrTime;
	    	if(y<=currentTime)
	    	{
	    		if(!seq.contains(list.get(t)) && list.get(t).status==false)
	    		{
            list.get(t).status=true;
            list.get(t).arrived=true;
	    			seq.addLast(list.get(t));
	    		}	
                }
                t++;
        }           
       return seq;
   }
}
