//Name: Bharat Sachdev
//Project: ATCS Final

import java.io.PrintWriter;
import java.util.*;
import java.io.*;
public class LibraryDriver //driver class
{
   //throughout this class, there are a few methods that have 2 versions. the ones labeled (sim) are used for simulation purposes and the ones labeled (controlled) are controlled by either admin or user running the program
   private static Map<String, Book> booklist= new TreeMap<String, Book>(); //map of books
   private static Map<Integer, User> userlist= new TreeMap<Integer, User>();  //map of users
   private static Map<String, Integer> authorlist= new HashMap<String, Integer>(); //map of authors
   private static int inU=1; //current user index which is incremented as users are added
   private static int bid=1; //book id which is also incremented as books are added
   private static Scanner sc; //scanner which is used as input stream throughout the program
   private static PrintWriter pw; //writes the simulation to text of your choice
   private static String wF=""; //file name
   private static int days=0; //number of days you want to simulate
   private static double isProb=0.5;//probability of simulated user issuing book
   private static double isRet=0.5;//probability of simulated user returning a book
   private static int uID=0; //current user id that is carried through multiple methods, easier to make it instance variable than input in every method
   private static int mBooks=0; //books issued in a day
   private static int mBooksF=0; //max books through sim
   private static int [] bpd; //keeps track of books issued per day
   public static void main(String []args)
   {
      populateBook();
      populateUser();
      sc= new Scanner(System.in);
      System.out.println("Welcome to the Library. Please enter the number of days to simulate. (Enter days > 0)");
      days= sc.nextInt();
      sc.nextLine();
      bpd= new int[days];
      if(days<=0){
         System.out.println("Understood. Shutting down."); System.exit(0);}
      System.out.println("Enter text file to store simulation logs in:");
      wF=sc.next();
      try{
         pw= new PrintWriter(new File(wF));
      }
      catch(Exception e){
         System.out.println("Something went wrong.");
      }
      System.out.println("Enter probability for user to issue book in simulation (between 0 and 1):");
      isProb=sc.nextDouble();
      sc.nextLine();
      if(isProb<0 || isProb>1){
         System.out.println("Understood. Shutting down."); System.exit(0);}
      System.out.println("Enter probability for user to return book in simulation (between 0 and 1):");
      isRet=sc.nextDouble();
      sc.nextLine();
      if(isRet<0 || isRet>1){
         System.out.println("Understood. Shutting down."); System.exit(0);}
      runSim();
      System.out.println("Simulation completed! Now, onto a few final steps");
      analysis();
      pw.close();
   }
   private static void populateUser()//populates user map
   {
      try{
         sc= new Scanner(new File("Names.txt"));
      }
      catch(Exception e){
         System.out.println("Please download Names.txt and restart");
         System.exit(0);
      }
      
      while(sc.hasNextLine())
      {
         String s=sc.nextLine();
         User u= new User(inU, s);
         userlist.put(inU, u);
         inU++;
      }
   
   }
   private static void populateBook()//populates book map and author map
   {
      try{
         sc= new Scanner(new File("Books.txt"));
      }
      catch(Exception e){
         System.out.println("Please download Books.txt and restart");
         System.exit(0);
      }
      
      while(sc.hasNextLine())
      {
         String s=sc.nextLine();
         String name= s.substring(s.indexOf("-")+1);
         name.trim();
         String author=s.substring(0,s.indexOf("-"));
         author.trim();
         Book b= new Book(name, author, bid);
         bid++;
         if(booklist.containsKey(name))
            booklist.get(name).setCopies(booklist.get(name).getCopies()+1);
         else
            booklist.put(name,b);
         if(!authorlist.containsKey(author))
            authorlist.put(author,0);
      }
   }

         
   private static void runSim()//start of simulatiom
   {
   
      for(int i=1;i<=days;i++)
      {
         mBooks=0;
         System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
         System.out.println("                         DAY "+i);
         System.out.println();
         System.out.println();
         
         pw.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
         pw.println("                         DAY "+i);
         pw.println();
         pw.println();
         disMenu();//calls main menu
         bpd[i-1]=mBooks;
         if(mBooks>mBooksF)
            mBooksF=mBooks;
      }
   }
   
   private static void disMenu()//main menu
   {
      System.out.println("--------------------------------------------------------------");
      System.out.println("                       MAIN MENU");
      System.out.println("--------------------------------------------------------------");
      System.out.println();
      System.out.println();
      System.out.println("1. Login as Admin");
      System.out.println("2. Login as a User");
      System.out.println("3. View book catalog");
      System.out.println("4. Start daily simulation");
      System.out.println();
      System.out.println();
      System.out.println("--------------------------------------------------------------");
      daily();//calls the daily actions that can be carried out
   }
   
   private static void daily()//carries out options listed in main menu
   {
      sc=new Scanner(System.in);
      int choice=sc.nextInt();
      sc.nextLine();
      switch (choice){
         case 1://admin login
            adminAct();
            break;
         case 2://user login
            System.out.println("Howdy user. What's your id?");
            uID= sc.nextInt();
            sc.nextLine();
            if(userlist.containsKey(uID))
               userAct();
            else{
               System.out.println("Invalid user ID. Going back to main menu.");
               disMenu();}
            break;
         case 3://catalog
            System.out.println("Chose a filter if you want:\n  1. Filter by author.\n  2. Filter by currently availible.\n  3. Any other number or 3 for show all books");
            int cho=sc.nextInt();
            sc.nextLine();
            switch(cho){
               case 1:
                  System.out.println("Enter author name:");
                  String a=sc.nextLine();
                  filtBookAuthDis(a);
                  disMenu();
                  break;
               case 2:
                  filtBookAvai();
                  disMenu();
                  break;
               default:
                  disCat();}
            disMenu();
            break;
         default://daily simulation
            simulation();
            break;
      }
   }
   private static void adminAct()//admin actions
   {
      System.out.println();
      System.out.println();
      System.out.println("Howdy admin. What you wanna do? (Enter the index choice)");
      System.out.println("1. Add a book");
      System.out.println("2. Delete a book");
      System.out.println("3. Return a book");
      System.out.println("4. Display all users");
      System.out.println("5. Add a user");
      System.out.println("6. Inspect user");
      System.out.println("7. Delete a user");
      System.out.println("8. Edit checkout limit");
      System.out.println("9. Any other number to logout (return to main menu)");
      sc= new Scanner(System.in);
      int choice= sc.nextInt();
      sc.nextLine();
      String n=""; int count=0;int id=0;
      switch (choice){
      
         case 1:
         
            System.out.println();
            System.out.println();
            System.out.println("Sure thing partner. Enter the name:");
            n=sc.nextLine();
            System.out.println("Now, enter the author:");
            String au=sc.nextLine();
            System.out.println("How many ya got?");
            count=sc.nextInt();
            sc.nextLine();
            if(booklist.containsKey(n))
               booklist.get(n).setCopies(booklist.get(n).getCopies()+count);
            else
            {
               Book b= new Book(n, au, bid);
               booklist.put(n,b);
            }
            if(!authorlist.containsKey(au))
            authorlist.put(au,0);
            System.out.println("Done!");
            adminAct(); 
            break;
         
         case 2:
         
            System.out.println();
            System.out.println();
            System.out.println("Sure thing partner. Enter the name:");
            n=sc.nextLine();
            System.out.println("How many ya want gone?");
            count=sc.nextInt();
            sc.nextLine();
            if(booklist.containsKey(n))
            {
               if(booklist.get(n).getCopies()<=count)
                  booklist.remove(n);
               else
                  booklist.get(n).setCopies(booklist.get(n).getCopies()-count);
               System.out.println("Done!");
            }
            else
            {
               System.out.println("Book not found in collection!");
            }
            adminAct(); 
            break;
         
         case 3:
         
            System.out.println();
            System.out.println();
            System.out.println("Sure thing partner. Enter the name:");
            n=sc.nextLine();
            System.out.println("How many ya wanna return?");
            count=sc.nextInt();
            sc.nextLine();
            if(booklist.containsKey(n))
            {
               booklist.get(n).setCopies(booklist.get(n).getCopies()+count);
               checkQ(n);
               System.out.println("Done!");
            }
            else
            {
               System.out.println("Book not found in collection! Choose add a book instead.");
            }
            adminAct();
            break;
         
         case 4:
            
            displayUsers();
            adminAct();
            break;
            
         case 5:
         
            System.out.println();
            System.out.println();
            System.out.println("Sure thing partner. Enter the name:");
            n=sc.nextLine();
            User u= new User(inU, n);
            userlist.put(inU, u);
            inU++;
            adminAct();
            break;
         
         case 6:
            System.out.println();
            System.out.println();
            System.out.println("Sure thing partner. Enter the id:");
            id=sc.nextInt();
            sc.nextLine();
            if(userlist.containsKey(id))
            {
               System.out.println("User info:\n   ID: "+id+" Name: "+userlist.get(id).getName());
               System.out.println("What do you want to inspect?\n1. Books checked out\n2. Limit remaining");
               int ch=sc.nextInt();
               sc.nextLine();
               switch (ch){
                  case 1:
                     userlist.get(id).getBookList();
                     break;
                  case 2:
                     System.out.println("Limit remaining: "+(userlist.get(id).getLimit()-userlist.get(id).getIssued()));
                     break;
                  default:
                     System.out.println("Do not try to cheat the system.");
                     System.exit(0);
               }
            }
            else
               System.out.println("User id not found!");
            adminAct();
            break;
         
         case 7:
            System.out.println();
            System.out.println();
            System.out.println("Sure thing partner. Enter the id:");
            id=sc.nextInt();
            sc.nextLine();
            if(userlist.containsKey(id))
            {
               userlist.remove(id);
            }
            else
               System.out.println("User id not found!");
            adminAct();
            break;
         
         case 8:
            System.out.println();
            System.out.println();
            System.out.println("Sure thing partner. Enter new limit:");
            int lim=sc.nextInt();
            sc.nextLine();
            for(int i:userlist.keySet())
            {
               userlist.get(i).setLimit(lim);
            }
            System.out.println("Done!");
            adminAct();
            break;
         
         default:
            disMenu();
         
      }
   }

   private static void userAct()//user actions
   {
      sc= new Scanner(System.in);
      System.out.println();
      System.out.println();
      System.out.println("1. Issue a book");
      System.out.println("2. Return a book");
      System.out.println("3. Delete account");
      System.out.println("4. Check limit");
      System.out.println("5. Any other number to logout (return to main menu)");
      int id=uID;
      int choice= sc.nextInt();
      sc.nextLine();
      String n=""; int count=0;
      switch (choice){
         case 1:
            System.out.println("Sure thing. Enter the name of the book you want to issue.");
            System.out.println();
            n=sc.nextLine();
            if(booklist.containsKey(n)){
               issueBook2(id,n);
               mBooks++;
            }
            else
               System.out.println("Invalid book name.");
            userAct();
            break;
         case 2:
            if(userlist.get(id).getIssued()!=0)
            {
               System.out.println("Sure thing. Pick a book (index) you want to return.");
               System.out.println();
               userlist.get(id).getBookList();
               int c=sc.nextInt();
               sc.nextLine();
               if(c>0 && c<userlist.get(id).getIssued()+2){
                  returnBook2(id, c-1);
                  userAct();
                  break;}
               else{
                  System.out.println("Incorrect index entered. Returning back to user menu.");
                  System.out.println();
                  userAct();
                  break;}
            }
            else
               System.out.println("No books currently issued.");
            System.out.println();
            userAct();
            break;
         case 3:
            System.out.println("Deleting acccount. Returning to main menu.");
            System.out.println();
            userlist.remove(id);
            disMenu();
            break;
         case 4:
            System.out.println("Overall limit: "+userlist.get(id).getLimit()+", remaining: "+(userlist.get(id).getLimit()-userlist.get(id).getIssued()));
            System.out.println();
            userAct();
            break;
         case 5:
            System.out.println("Logging out. Returning to main menu.");
            System.out.println();
            disMenu();
            break;
            
      }
   
   }
   private static void filtBookAuthDis(String auth)//filter 1
   {
      System.out.println();
      System.out.println();
      System.out.println("--------------------------------------------------------------");
      System.out.println("             BOOKS BY "+auth.toUpperCase() );
      System.out.println();
      System.out.println();
      Set <String> books= booklist.keySet();
      int i=1;int k=0;
      for(String s:books)
      {
         if(booklist.get(s).getAuthor().equals(auth)){
            System.out.println(i+". "+s);
            i++;k=1;
         }
      }
      if(k==0)
         System.out.println("No books found!");
      System.out.println();
      System.out.println();
      
   }
   private static void filtBookAvai()//filter 2
   {
      System.out.println();
      System.out.println();
      System.out.println("--------------------------------------------------------------");
      System.out.println("                      BOOKS AVAILABLE");
      System.out.println();
      System.out.println();
      Set <String> books= booklist.keySet();
      int i=1;int k=0;
      for(String s:books)
      {
         if(booklist.get(s).getCopies()>0){
            System.out.println(i+". "+s);
            i++;k=1;
         }
      }
      if(k==0)
         System.out.println("No available books found!");
      System.out.println();
      System.out.println();
      
   }
   private static void disCat()//no filter
   {
      System.out.println();
      System.out.println();
      System.out.println("--------------------------------------------------------------");
      System.out.println("                         ALL BOOKS");
      System.out.println();
      System.out.println();
      Set <String> books= booklist.keySet();
      int i=1;
      for(String s:books)
      {
         System.out.println(i+". "+s);
         i++;
      }
      System.out.println();
      System.out.println();
      
   }

   private static void simulation()//simulation method
   {
      Set <Integer> ks= userlist.keySet();
      Iterator i=ks.iterator();
      while(i.hasNext())
      {
         int m=(int)(i.next());
         if(Math.random()<isProb)
         {
            int bookid=(int)( Math.random()*booklist.size());
            issueBook(m,bookid);
            mBooks++;
         }
         if(Math.random()<isRet)
         {
            if(userlist.get(m).getIssued()>0)
            {
               returnBook(m, (int)(Math.random()*userlist.get(m).getIssued()));
            }
         }
      }
   }
   private static void returnBook(int i, int re)//returns random book held by user (sim)
   {
      String s=userlist.get(i).reBook(re);
      booklist.get(s).setCopies(booklist.get(s).getCopies()+1);
      pw.println("--> User ID: "+i+", Name: "+userlist.get(i).getName()+" returned book "+s);
      pw.println();
      checkQ(s);
   }
   private static void returnBook2(int i, int re)//returns book (controlled)
   {
      String s=userlist.get(i).reBook(re);
      booklist.get(s).setCopies(booklist.get(s).getCopies()+1);
      System.out.println("Successfully returned book "+s);
      System.out.println();
      checkQ(s);
   }
   private static void checkQ(String s)//called whenever book is returned. If someone is in queue for returned book, book is issued to next in queue
   {
      if(booklist.get(s).emptyQ()==false)
      {
         int i=booklist.get(s).removeQ();
         userlist.get(i).issBook(s);
         booklist.get(s).setCopies(booklist.get(s).getCopies()-1);
         booklist.get(s).incIs();
         authorlist.replace(booklist.get(s).getAuthor(),authorlist.get(booklist.get(s).getAuthor())+1);
         pw.println("--> User ID: "+i+", Name: "+userlist.get(i).getName()+" was issued book "+s+" and removed from queue.");
         mBooks++;
         pw.println();
      }
   }
   private static void issueBook(int i, int bookid)//issues random book from lib to a user (sim)
   {
      Iterator it=booklist.keySet().iterator();String s="";
      for(int k=0;k<bookid;k++)
      {  String a=(String)(it.next());
         if(a!=null)
            s=a;
      }
      if(userlist.get(i).getIssued()<userlist.get(i).getLimit()){
         if(booklist.get(s).getCopies()>0){
            userlist.get(i).issBook(s);
            booklist.get(s).setCopies(booklist.get(s).getCopies()-1);
            booklist.get(s).incIs();
            authorlist.replace(booklist.get(s).getAuthor(),authorlist.get(booklist.get(s).getAuthor())+1);
            pw.println("--> User ID: "+i+", Name: "+userlist.get(i).getName()+" successfully issued "+s);
            pw.println();
         }
         else{
            booklist.get(s).addToQ(i);
            pw.println("--> User ID: "+i+", Name: "+userlist.get(i).getName()+" has been added to the queue for "+s);
            pw.println();}
      }
      else{
         pw.println("--> User ID: "+i+", Name: "+userlist.get(i).getName()+" failed to issue "+s+" because user reached max limit.");
         pw.println();}
            
   }
   private static void issueBook2(int i, String s)//issues book to current user (controlled)
   {
      if(booklist.keySet().contains(s)){
         if(userlist.get(i).getIssued()<userlist.get(i).getLimit()){
            if(booklist.get(s).getCopies()>0){
               userlist.get(i).issBook(s);
               booklist.get(s).setCopies(booklist.get(s).getCopies()-1);
               booklist.get(s).incIs();
               authorlist.replace(booklist.get(s).getAuthor(),authorlist.get(booklist.get(s).getAuthor())+1);
               System.out.println("Successfully issued book "+s);
               System.out.println();
            }
            else{
               booklist.get(s).addToQ(i);
               System.out.println("Added to the queue for "+s);
               System.out.println();}
         }
         else{
            System.out.println("Max limit reached, book "+s+" not issued!");
            System.out.println();}
      }
      else
         System.out.println("Book "+s+" not found in library.");
   
   }
   
   private static void displayUsers()//displays all users. used by admin
   {
      System.out.println();
      System.out.println();
      System.out.println("--------------------------------------------------------------");
      System.out.println("                              USERS");
      System.out.println();
      System.out.println();
      if(!userlist.isEmpty())
      {
         for(int i:userlist.keySet())
         {
            System.out.println("ID: "+i+", Name: "+userlist.get(i).getName());
         }
      }
      else
         System.out.println("No users found!");
      System.out.println();
      System.out.println();
   
   }
   private static void analysis()//conducts analysis once simulation has ended
   {
      sc=new Scanner (System.in);
      System.out.println();
      System.out.println();
      System.out.println("Select analysis type: ");
      System.out.println("1. Day on which max books were issued");
      System.out.println("2. Number of books issued on each day");
      System.out.println("3. Book that was issued most number of times");
      System.out.println("4. Most read author");
      System.out.println("5. Exit");
      System.out.println();
      System.out.println();
      int choice= sc.nextInt();
      sc.nextLine();
      switch (choice)
      {
         case 1:
            for(int i=0; i<bpd.length;i++)
            {
               if(bpd[i]==mBooksF)
               {
                  System.out.println("The most number of books ("+mBooksF+") were issued on day number "+(i+1));
               }
            }
            analysis();
            break;
         case 2:
            System.out.println("Number of books issued on each day are: ");
            for(int i=0;i<bpd.length;i++)
               System.out.println("Day "+(i+1)+". Books issued: "+bpd[i]);
            System.out.println();
            System.out.println();
            analysis();
            break;
         case 3:
            String a="";int issued=0;
            for(String s:booklist.keySet())
            {
               if(booklist.get(s).timesIssued()>issued)
               {
                  a=s;
                  issued=booklist.get(s).timesIssued();
               }
            }
            System.out.println("The book issued the most ("+issued+" times) is "+a);
            analysis();
            break;
         case 4:
            String au="";int iss=0;
            for(String s:authorlist.keySet())
            {
               if(authorlist.get(s)>iss){
                  au=s;
                  iss=authorlist.get(s);
               }
            }
            System.out.println("The author whose books were issued the most ("+iss+" times) is "+au);
            analysis();
            break;
         default:
            pw.close();
            System.exit(0);
      }
   }

}