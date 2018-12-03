import java.util.*;
import java.lang.*;
class Track
{
  int pos,diff;
  Track(int pos)
  {
    this.pos=pos;
    this.diff=-1;
  }

}

class Main
{
  public static void main(String args[])
  {
    int n,m,head,i,choice;
    Vector <Track> ip = new Vector <>();
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter number of tracks present:"); 
    n = sc.nextInt();
    System.out.println("Enter number of tracks to be read:");
    m = sc.nextInt();
    System.out.println("Enter start position of head:");
    head = sc.nextInt();
    System.out.println("Enter position of tracks to be read:");
    for(i=0;i<m;i++)
    {
      Track t = new Track(sc.nextInt());
      ip.add(t);
    }
    System.out.println("Select algorithm:");
    System.out.println("1. LOOK\n2. CLOOK");
    choice= sc.nextInt();
    switch(choice)
    {
      case 1:
            look(ip,head,n);
            break;
      case 2:
            clook(ip,head,n);
            break;
      default:
            System.out.println("Invalid");
            break;
    }
  }

  public static void look(Vector <Track> ip,int head, int n)
  {
    int i,x=ip.size();
    Vector <Track> res = new Vector<>();
    int start = head;
    Track max = Collections.max(ip,new Comparator <Track> ()
      {
        public int compare (Track t1, Track t2)
        {
          return t1.pos - t2.pos;
        }
      });
      Track min = Collections.min(ip,new Comparator <Track> ()
      {
        public int compare (Track t1, Track t2)
        {
          return t1.pos - t2.pos;
        }
      });
      int up = max.pos;
      int low = min.pos;
    for(i=start;i<=up;i++)
    {
      Track current = new Track (i);
      if(contain(ip,i)!=-1)
      {
        ip.remove(contain(ip,i));
        current.diff = Math.abs(head - current.pos);
        res.add(current);
        head=current.pos; 
      }
    }
    if(ip.size()!=0)
    {
      for(i=head;i>=low;i--)
      {
        Track current = new Track (i);
        if(contain(ip,i)!=-1)
        {
          ip.remove(contain(ip,i));
          current.diff = Math.abs(head - current.pos);
          res.add(current);
          head=current.pos; 
        }
      }
    }
    printRes(res,x);
  }

  
  public static void clook(Vector <Track> ip,int head, int n)
  {
    int i,x=ip.size();
    Vector <Track> res = new Vector<>();
    int start = head;
     Track max = Collections.max(ip,new Comparator <Track> ()
      {
        public int compare (Track t1, Track t2)
        {
          return t1.pos - t2.pos;
        }
      });
      Track min = Collections.min(ip,new Comparator <Track> ()
      {
        public int compare (Track t1, Track t2)
        {
          return t1.pos - t2.pos;
        }
      });
      int up = max.pos;
      int low = min.pos;
    for(i=start;i<=up;i++)
    {
      Track current = new Track (i);
      if(contain(ip,i)!=-1)
      {
        ip.remove(contain(ip,i));
        current.diff = Math.abs(head - current.pos);
        res.add(current);
        head=current.pos; 
      }
    }
    if(ip.size()!=0)
    {
      Track buffer2 = new Track(low);
      buffer2.diff = Math.abs(head-low);
      res.add(buffer2);
      head = low;
      for(i=head+1;i<=start;i++)
      {
        Track current = new Track (i);
        if(contain(ip,i)!=-1)
        {
          ip.remove(contain(ip,i));
          current.diff = Math.abs(head - current.pos);
          res.add(current);
          head=current.pos; 
        }
      }
    }
    printRes(res,x);
  }

  public static void printRes(Vector <Track> res,int m)
  {
    int i,sum=0;
    double avg;
    System.out.println("The tracks are read in following order:");
    System.out.println("Track Position\tSeek Length");
    for(i=0;i<res.size();i++)
    {
      System.out.println(res.get(i).pos+"\t\t"+res.get(i).diff);
      sum= sum+res.get(i).diff;
    }
    avg=(double)sum/m;
    System.out.println("Average Seek Length is "+avg);
  }
 

 public static int contain(Vector <Track> ip, int i)
  {
    for(int x = 0;x<ip.size();x++)
    {
      if(ip.get(x).pos == i)
        return x;
    }
    return -1;
  }
}
