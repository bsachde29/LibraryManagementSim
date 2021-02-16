import java.util.*;
public class Book implements Comparable<Book>{
   private String name="";
   private int copies=0;
   private String author="";
   private int id=0;
   private int timesIs=0;
   private Queue<Integer> inQ= new LinkedList<Integer>();
   
   public Book(String n, String au, int ident){
      name=n;
      copies=1;
      author= au;
      id=ident;
   }
   public Book(String n, String au, int c, int ident)
   {
      name=n;
      copies=1;
      author=au;
      id=ident;
   }
   
   public String getName(){
      return name;
   }
   public String toString(){
      return name;
   }
   
   public String getAuthor(){
      return author;
   }
   public int getID(){
      return id;
   }
   public int getCopies(){
      return copies;
   }
   
   public void setCopies(int c){
      copies=c;
   }
   
   public void addToQ(int n){
      inQ.add(n);
   }
   public void incIs()
   {
      timesIs++;}
   public int timesIssued()
   {
      return timesIs;
   }
   public int removeQ(){
      return inQ.remove();
   }
   public boolean emptyQ(){
      return inQ.isEmpty();
   }
   
   @Override public int compareTo(Book b){
      if(this.name.compareTo(b.getName())>0)
         return -1;
      else
         return 1;
   }
}