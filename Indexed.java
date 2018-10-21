import java.util.*;
import java.lang.*;
//block size=256kb
//main memory size = 2048kb
//number of blocks in main memory = 8
class File
{
  String filename;
  int start,size,blocks;
  File(String filename,int size,int start,int blocks)
  {
    this.filename = filename;
    this.size = size;
    this.start = start;
    this.blocks = blocks;
  }
}

class Mem
{
  int status;
  //0: free
  //1: occupied and index block
  //2: occupied and data block
  Vector <Integer> index = new Vector <>();
  Mem(int status, Vector <Integer> index)
  {
    this.status=status;
    this.index=index;
  }
}
class Main
{
  static Mem mem[] = new Mem[8];
  static Vector <File> fat = new Vector <File> ();
  static String name;
  static final int blockSize = 256;
  static int avl = 8;
  static Vector <Integer> list = new Vector<> ();
  public static void main(String args[])
  {
    Scanner sc=new Scanner(System.in);
    int choice,i,newsize,search =0, loc=-1;
    String name2;
    for(i=0;i<8;i++)
    {
      Mem m =new Mem(0,new Vector <>());
      mem[i]=m;//true means free, false means occupied
      list.add(i);
    }
    while(true)
    {
      System.out.println("\n1. Create new file\n2. Delete existing file\n3. Print result\n4. Exit");
      choice = sc.nextInt();
      switch(choice)
      {
        case 1:
              create();
              break;
        case 2:
              System.out.println("Enter filename which you want to delete");
              name2 = sc.next();
              search =0;
              loc=-1;
              for(i=0;i<fat.size();i++)
              {
                if(name2.equals(fat.get(i).filename))
                {
                  search++;
                  loc = i;
                }
              }
              if(search == 0)
              System.out.println("File not found");
              else
                del(name2, loc);
              break;
        case 3:
              print();
              break;
        case 4: 
              System.exit(0);
              break;
        default:
              System.out.println("Wrong choice! Enter again");
              break;
      }
    } //while ends
  }

  public static void create()
  {
    Scanner sc = new Scanner(System.in);
    int start = -1, size,count = 0, i,block;
    String name;
    System.out.println("Enter file name and file size in kB:");
    name = sc.next();
    size = sc.nextInt(); 
    for(i=0;i<fat.size();i++)
    {
      if(name.equals(fat.get(i).filename))
      {
        System.out.println("File already exists!");
        return;
      }
    }
    double s = (double)size/blockSize;
    block = (int)(Math.ceil(s));
    block++;
    if(avl<block)
    {
      System.out.println("Error: Not enough memory available");
      return;
    }
    start = list.remove(0);
    mem[start].status=1;
    Vector <Integer> init = new Vector <>();
    for(i=1;i<block;i++)
    {
      int l = list.get(0);
      mem[l].status=2;
      init.add(list.remove(0));
    }
    mem[start].index = init;
    avl-=block;
    File f = new File(name,size,start,block);
    fat.add(f);
    System.out.println("File "+f.filename+" created successfully!");
  }

  

  public static void del(String name2, int loc)
  {
    int currStart;
    File deleted = fat.remove(loc);
    currStart = deleted.start;
    int t = currStart;
    Vector <Integer> tbd = new Vector<>();
    tbd = mem[currStart].index;
    mem[currStart].status=0;
    mem[currStart].index=new Vector <Integer>();
    for(int c = 0;c<tbd.size();c++)
    {
      t = tbd.get(c);
      mem[t].status=0;
      avl +=1;
      list.add(t);
      Collections.sort(list);
    }
    System.out.println("File "+name2+" deleted successfully!");
  }

  public static void print()
  {
    int i;
    System.out.println("\nFile Allocation Table is: "); 
    System.out.println("Name\t\tSize (kB)\tStart");
    for(i=0;i<fat.size();i++)
    {
      System.out.println(fat.get(i).filename+"\t\t"+fat.get(i).size+"\t\t"+fat.get(i).start);
    }

    System.out.println("\nMain memory is: (#D: Data block, *I: Index block)");
    for(i=0;i<8;i++)
    if(mem[i].status==0)
    System.out.println(i+" Free");
    else if (mem[i].status==2)
    System.out.println(i+" Occupied#D");
    else
    {
      System.out.println(i+" Occupied*I\t Index:");
      for(int j =0; j<mem[i].index.size();j++)
      {
        System.out.println("\t"+mem[i].index.get(j)+".........");
      }
    }
  }
}
