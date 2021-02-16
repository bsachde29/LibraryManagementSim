import java.util.*;
import java.io.*;
public class User
{
   private int id=0;
   private String name= "";
   private ArrayList <String> books= new ArrayList<String>();
   private int limit=4;
   
   public User(int ident, String n){
      id= ident; 
      name=n;
   }
   
   public int getID(){ //returns id
      return id;
   }
   
   public String getName(){ //returns name
      return name;
   }
   
   public void getBookList(){ //returns currently issued books
   int i=1;
   if(books!=null)
   {
      for(String s:books)
      {
         System.out.println(i+". "+s);
      }
   }
   else
      System.out.println("No books issued yet.");
   }
   
   
   public int getLimit(){ //returns limit on books
      return limit;
   }
   
   public int getIssued(){ //returns number of books currently issued
      return books.size();
   }
   
   public void issBook(String s){ //adds book to issued books
   books.add(s);
   }
   
   public void setLimit(int l){ //sets new limit
      limit=l;
   }
   
   public String reBook(String b){ //return books
      for(int i=0;i<books.size();i++)
      {
         if(b.compareTo(books.get(i))==0)
         {
            String a=books.remove(i);
            return "--> User ID: "+id+", Name: "+name+" succesfully returned book "+a;
         }
      }
      return "--> User ID: "+id+", Name: "+name+" did not issue the book "+b;
   }
   public String reBook(int i){ //returns book v2 (when index is given) used for sim
      String a=books.remove(i);
      return a;  
   }
   public int compareTo(User u)
   {
      if(this.name.compareTo(u.name)>0)
         return -1;
      else
         return 1;
   }
   
}