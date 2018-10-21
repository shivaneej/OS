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
  boolean status;
  int pointer;
  Mem(boolean status, int pointer)
  {
    this.status=status;
    this.pointer=pointer;
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
      Mem m =new Mem(true,-1);
      mem[i]=m;//true means free, false means occupied
      list.add(i);
    }
    while(true)
    {
      System.out.println("\n1. Create new file\n2. Update existing file\n3. Delete existing file\n4. Print result\n5. Exit");
      choice = sc.nextInt();
      switch(choice)
      {
        case 1:
              create();
              break;
        case 2:
              System.out.println("Enter filename which you want to update");
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
              {
                System.out.println("Enter new file size in kB");
                newsize = sc.nextInt();
                update(name2,newsize,loc);
              }
              break;
        case 3:
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
        case 4:
              print();
              break;
        case 5: 
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
    if(avl<block)
    {
      System.out.println("Error: Not enough memory available");
      return;
    }
    start = list.get(0);
    for(i=0;i<block;i++)
    {
      int l =list.get(0);
      mem[l].status=false;
      if(i==(block-1))
      mem[l].pointer=-1;
      else
      mem[l].pointer=list.get(1);
      list.remove(0);
    }
    avl-=block;
    File f = new File(name,size,start,block);
    fat.add(f);
    System.out.println("File "+f.filename+" created successfully!");
  }

  public static void update(String name2, int newSize, int loc)
  {
    int i,currSize,currStart,currBlocks, newBlocks,end,eblocks;
    currSize = fat.get(loc).size;
    currStart = fat.get(loc).start;
    currBlocks = fat.get(loc).blocks;
    if(currSize>newSize)
    {
      double s = (double)newSize/blockSize;
      newBlocks = (int)(Math.ceil(s));
      end = currStart+newBlocks;
      for(i=end;i<(currStart+currBlocks);i++)
      {
        mem[end].status=true;
        int next = mem[end].pointer;
        mem[end].pointer=-1;
        avl +=1;
        list.add(end);
        Collections.sort(list);
        end=next;
      }
      File nf = new File(name2, newSize, currStart, newBlocks);
      fat.set(loc,nf);
      System.out.println("File "+name2+" updated successfully!");
    }
    else if(currSize < newSize)
    {
      int diff = newSize - currSize, empty = (currBlocks*256) - currSize;
      if(diff<empty)
      {
        File nf = new File(name2, newSize, currStart, currBlocks);
        fat.set(loc,nf);
        System.out.println("File "+name2+" updated successfully!");
      }
      else
      {
        int extra = diff - empty, x = currStart+currBlocks;
        double s = (double)extra/blockSize;
        eblocks = (int)(Math.ceil(s));
        end = currStart+currBlocks;
        if(eblocks>avl)
        {
          System.out.println("Error: Failed to update file since memory is unavailable!");
          return;
        }
        mem[end-1].pointer = list.get(0);
        for(i=0;i<eblocks;i++)
        {
          int l =list.get(0);
          mem[l].status=false;
          if(i==(eblocks-1))
          mem[l].pointer=-1;
          else
          mem[l].pointer=list.get(1);
          list.remove(0);
        }
        avl-=eblocks;
        File nf = new File(name2,newSize,currStart,(currBlocks+eblocks));
        fat.set(loc,nf);
        System.out.println("File "+name2+" updated successfully!");
      }
    }
  }

  public static void del(String name2, int loc)
  {
    int currStart,currBlocks;
    File deleted = fat.remove(loc);
    currStart = deleted.start;
    currBlocks = deleted.blocks;
    int t = currStart;
    while(currBlocks!=0)
    {
      mem[t].status=true;
      int next = mem[t].pointer;
      mem[t].pointer=-1;
      currBlocks -=1;
      avl +=1;
      list.add(t);
      Collections.sort(list);
      t=next;
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

    System.out.println("\nMain memory is: ");
    for(i=0;i<8;i++)
    if(mem[i].status==true)
    System.out.println(i+" Free");
    else
    System.out.println(i+" Occupied. Next: "+mem[i].pointer);
  }
}
