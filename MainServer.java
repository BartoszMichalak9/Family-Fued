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

      /*try
      {
         fr = new FileReader("FAMILY-FEUD-QUESTIONS-Sheet1.csv");
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
         frTwo = new FileReader("FAMILY-FEUD-ANSWERS-Sheet1-1.csv");
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
            questionsList.add(line);        
            line = brTwo.readLine();
         }
      }
      catch(IOException ioe)
      {
      } 
      catch(NullPointerException npe)
      {
      }*/             
   }
   
   class MainServerInner extends Thread
   {
      Socket s;
      String outputMessage;     

      public MainServerInner(Socket _s)
      {
         s = _s;
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
            
            while(true)
            {
               outputMessage = brRun.readLine();

               System.out.println(outputMessage);
      
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