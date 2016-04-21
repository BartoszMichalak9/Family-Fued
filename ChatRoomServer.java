/**
 * @author Brett Phillips
 * @author Tanner Glantz
 * @author Rosalee Hacker
 * @author Alejandro Garzon
 * @version 4/1/16
 * Description: A Java GUI that runs a client of the program
 * when a server is running. Users can send a message
 * to each other when connected over the same server
 */
 
//imported classes
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

/**
 * ChatRoomServer class that creates a server and uses
 * the IP and name of the users machine to open a connection
 * between 2 machines, which allow them to send messages between
 * through a chatroom
 * @param printers Vector<PrintWriter> array that holds printers
 */
public class ChatRoomServer
{
   private Vector<PrintWriter> printers = new Vector<PrintWriter>();
   private int counter = 0;
   
   /**
    * main method which creates a new program of ChatRoomServer
    */
   public static void main(String [] args)
   {
      ChatRoomServer chatServer = new ChatRoomServer();
   }
   /**
    * ChatRoomServer method which print the users information, create
    * new sockets from port 16000, and sends the message over the server
    * to the other users machine
    * @param ss ServerSocket creates a new ServerSocket
    */
   public ChatRoomServer()
   {
      ServerSocket ss;
      try
      {
         //prints users information and creates new socket
         ss = new ServerSocket(16000);
         Socket s = null;
         
         //while the loop runs, it successfully creates a connection between client and server and creates multiple threads for each instance of a client
         while(true)
         {
            s = ss.accept();
            Thread th = new ServerInnerClass(s);
            th.start();
         }         
      }
      catch(IOException ioe)
      {
      }
   } 
   /**
    * ServerInnerClass class that uses Threads to send messages
    * to the other connected users machines using file IO
    * @param s Socket new socket
    * @param outputMessage String string that holds the message
    */
   class ServerInnerClass extends Thread
   {
      Socket s;
      String outputMessage;     
    /**
     * parameterized constructor for ServerInnerClass that uses 
     * socket variables to instaniate and use later in the program
     * @param _s Socket
     */
      public ServerInnerClass(Socket _s)
      {
         s = _s;
      }
      /**
       * run method which runs the server once it has been started
       * @param br BufferedReader reader that is set to null
       * @param pw PrinterWriter writer that is set to null
       */
      public void run()
      {
         BufferedReader br = null;
         PrintWriter pw = null;          
         
         try
         {
            //opens new streams and adds to printers array
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            printers.add(pw);
            
            //while loop is true, reads lines, adds to array and prints to 
            //correct output
            while(true)
            {
               outputMessage = br.readLine();
              
               try
               {
                  //For loop that will print to every client that is connected to the server
                  for(PrintWriter p: printers)
                  {  
                      try
                     { 
                       //Lets the server know if someone disconnects from the server using file -> exit
                       if(outputMessage.equals("CLOSE"))
                        {
                           outputMessage = "\nA client has diconnected from the chat.\n";
                           System.out.println(outputMessage);
                        }                    
                        
                        p.println(outputMessage);
                        p.flush();
                     }
                     //Lets clients know if someone disconnects from the server using the [X] button on the window
                     catch(NullPointerException npe)
                     {
                        if(counter == 0)
                        {
                           p.println("\nA client has diconnected from the chat.\n");
                           p.flush();
                           counter++; 
                           System.out.println("\nA client has diconnected from the chat.\n");
                        }
                     }
                  }
               }
               catch(ConcurrentModificationException cme)
               {
               }
            }
         }
         catch(IOException ioe)
         {
         }
      }
   }  
}