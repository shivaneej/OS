import java.util.*;
import java.lang.*;
//block size=256kb
//main memory size = 4096kb
//number of blocks in main memory = 16
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
class Main
{
  static boolean mem[] = new boolean[16];
  static Vector <File> fat = new Vector <File> ();
  static String name;
  static final int blockSize = 256;
  public static void main(String args[])
  {
    Scanner sc=new Scanner(System.in);
    int choice,i,newsize,search =0, loc=-1;
    String name2;
    for(i=0;i<16;i++)
    mem[i] = true; //true means free, false means occupied
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
    for(i=0;i<=(16-block);i++)
    {
      if(mem[i] == true)
      {
        count=0;
        for(int j=i;j<i+block;j++)
        {
          if(mem[j] == true)
          count++;
        }
        if(count==block)
        {
          start = i;
          for(int j=i;j<i+block;j++)
          mem[j]=false;
          File f = new File(name,size,start,block);
          fat.add(f);
          System.out.println("File "+f.filename+" created successfully!");
          break;
        }
      }
    }
    if(start==-1)
    System.out.println("Error: Not enough memory available");
  }

  public static void update(String name2, int newSize, int loc)
  {
    int i,currSize,currStart,currBlocks, newBlocks;
    currSize = fat.get(loc).size;
    currStart = fat.get(loc).start;
    currBlocks = fat.get(loc).blocks;
    if(currSize>newSize)
    {
      double s = (double)newSize/blockSize;
      newBlocks = (int)(Math.ceil(s));
      for(i=(currStart+newBlocks);i<(currStart+currBlocks);i++)
        mem[i] = true;
      File nf = new File(name2, newSize, currStart, newBlocks);
      fat.set(loc,nf);
      System.out.println("File "+name2+" updated successfully!");
    }
    else if(currSize < newSize)
    {
      int diff = newSize - currSize, avl = (currBlocks*256) - currSize;
      if(diff<avl)
      {
        File nf = new File(name2, newSize, currStart, currBlocks);
        fat.set(loc,nf);
        System.out.println("File "+name2+" updated successfully!");
      }
      else
      {
        int extra =diff - avl, eblocks, x = currStart+currBlocks, freeB = 0;
        double s = (double)extra/blockSize;
        eblocks = (int)(Math.ceil(s));
        if(x+eblocks>16)
        {
          System.out.println("Error: Failed to update file since memory is unavailable!");
          return;
        }
        for(i=x;i<(x+eblocks);i++)
        {
          if(mem[i] == true)
            freeB++;
        }
        if(freeB==eblocks)
        {
          for(int j=x;j<x+eblocks;j++)
            mem[j]=false;
          File nf = new File(name2,newSize,currStart,(currBlocks+eblocks));
          fat.set(loc,nf);
          System.out.println("File "+name2+" updated successfully!");
        }
        else
        {
          System.out.println("Error: Failed to update file since memory available is low!");
        }
      }
    }
  }

  public static void del(String name2, int loc)
  {
    int i, currStart, currBlocks;
    File deleted = fat.remove(loc);
    currStart = deleted.start;
    currBlocks = deleted.blocks;
    for(i=currStart;i<(currStart+currBlocks);i++)
      mem[i]=true;
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
    for(i=0;i<16;i++)
    if(mem[i]==true)
    System.out.println(i+" Free");
    else
    System.out.println(i+" Occupied");
  }
}
