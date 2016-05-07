//imports the necesary classes for the GUI program
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class MainServer
{
   private FileReader fr = null;
   private BufferedReader br = null;
   private FileReader frTwo = null;
   private BufferedReader brTwo = null;   
   private ArrayList<String> questionsList = new ArrayList<String>();
   private ArrayList<String> answersList = new ArrayList<String>();
   private String line;
   private Vector<PrintWriter> printers = new Vector<PrintWriter>();
   private int[] playerScore = new  int[4];
   private String[] playerName = new String[4];
   private int playerCount = -1;
   public MainServer()
   {
      ServerSocket ss;
      try
      {
         ss = new ServerSocket(16100);
         Socket s = null;
         
         while(true)
         {
            s = ss.accept();
            Thread th = new MainServerInner(s);
            th.start();
         }         
      }
      catch(IOException ioe)
      {
      }      

      try
      {
         fr = new FileReader("FAMILY FEUD QUESTIONS.csv");
         br = new BufferedReader(fr);
      }
      catch(FileNotFoundException fnfe)
      {
      }
      
      try
      {
         line = br.readLine();
         
         while(!line.equals(null))
         {
            questionsList.add(line);
            line = br.readLine();
         } 
      }
      catch(IOException ioe)
      {
      }
      catch(NullPointerException npe)
      {
      }
      
      try
      {
         frTwo = new FileReader("FAMILY FEUD ANSWERS.csv");
         brTwo = new BufferedReader(frTwo);
      }
      catch(FileNotFoundException fnfe)
      {
      }
      
      try
      {
         line = brTwo.readLine();         
         
         while(!line.equals(null))
         {
            answersList.add(line);        
            line = brTwo.readLine();
         }
      }
      catch(IOException ioe)
      {
      } 
      catch(NullPointerException npe)
      {
      }             
   }
   
   class MainServerInner extends Thread
   {
      Socket s;
      String outputMessage = "";     
      HashSet<Integer> numsCalled = new HashSet<Integer>();
      Random generate = new Random();
            
      public MainServerInner(Socket _s)
      {
         s = _s;
      }
      
      public int makeQuestion(){
      //this makes it from 1 to 25, not inlcuding zero
         boolean check = false;
         int index = generate.nextInt(25)+1; 
         while(!check){
         if(!numsCalled.contains(index)){
            numsCalled.add(index);
            check = true;
            return index;
         }
         else
            index = generate.nextInt(25)+1; 
      }
      return -1;
      }

      public void run()
      {
         BufferedReader brRun = null;
         PrintWriter pwRun = null;          
         
         try
         {        
            brRun = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pwRun = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            printers.add(pwRun);
            
            while(playerCount<4){
               outputMessage = brRun.readLine();
               if(outputMessage.contains("Player Name:")){
                 playerCount++;
                 /* if(playerCount<4){
                  playerCount++;
                  }This will be for limiting the player count*/
                 playerName[playerCount] = outputMessage.substring(12);
                 System.out.println("Name entered: "+playerName[playerCount]);
                 System.out.println("Player: "+playerName[playerCount]+ " connected.");

                }
            }
            
            while(true)
            {
              outputMessage = brRun.readLine();
              
               for(PrintWriter p: printers)
               {                  
                  p.println(outputMessage);
                  p.flush();
               }
            }
         }
         catch(IOException ioe)
         {
         }
      }
   }   
   
   
   
   public static void main(String [] args)
   {
      MainServer mainServer = new MainServer();
   }
}