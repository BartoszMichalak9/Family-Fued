/*
 * @authors Tanner Glantz, Brett Phillips, Rosalee Hacker, and Alejandro Garzon
 * @version 05/14/2016
 * Description: The MainServer class sets up the server that will be used
 * by every client and its chat feature.    
 * Course: ISTE-121
 */

//imports the necesary classes for the GUI program
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class MainServer
{  
   /*
   *@param ArrayList<String> questionList creates ArrayList object where the questions will be stored
   *@param ArrayList<String> answerList creates ArrayList object where the answers will be stored
   *@param Vector<PrintWriter> printers creates Vector that stores the printwriters to be used for each client
   *@param int printNumber stores the printer number and is set to -1 by default
   *@param int questionCount stores the amount of questions in order to keep track of them
   *@param int[] playerScore array that will store 4 player scores
   *@param String[] playerName array that will store 4 player names
   *@param int playerCount stores player count and is set to -1 by default
   *@param int index stores the index
   *@param int countQuestionSent stores the amount of questions sent
   *@param int boardCount stores the count of the board
   *@param JFrame frame creates a frame for the server that will contain a text area that will print out information from the clients
   *@param JTextArea serverInfo Print out messages to the server with information about the game being played
   */ 
   private ArrayList<String> questionsList = new ArrayList<String>();
   private ArrayList<String> answersList = new ArrayList<String>();
   private Vector<PrintWriter> printers = new Vector<PrintWriter>();
   private int printerNumber = -1;
   private int questionCount = 1;
   private int[] playerScore = new  int[4];
   private String[] playerName = new String[4];
   private int playerCount = -1;
   private int index = 0;
   private int countQuestionsSent = 0;
   private int boardCount = 0;
   private JFrame frame;
   private JTextArea serverInfo;
   
   
   /*
   *Main constructor for MainServer. Instantiates the necessary components
   *to start the server such as the ServerSocket and Socket.
   */
   public MainServer(){
   
      //Sets information for the frame
      frame = new JFrame("Family Feud - Server");
      frame.setLocationRelativeTo(null);
      frame.setSize(700,700);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);
      //Creates a textarea and adds a scroll pane to it
      serverInfo = new JTextArea();
      serverInfo.setEditable(false);
      frame.add(new JScrollPane(serverInfo));
      
      //sets the frame to visible
      frame.setVisible(true);
   
   
   
      ServerSocket ss;
      try
      {
         ss = new ServerSocket(16100);
         Socket s = null;
         int threadCount = 0;
         //Always looking for a connection
         while(true)
         {
            s = ss.accept();
            //Creates an instance of MainServerInner class and starts it.
            Thread th = new MainServerInner(s, threadCount+"");
            th.start();
            threadCount++;
         }         
      }
      catch(IOException ioe){
      }                   
   }
   
   /*
   *Inner clas that extends Thread. It creates and instantiates 
   *necessary components in order to handle multiple clients
   */
   class MainServerInner extends Thread
   {
      /*
      *@param Socket s creates socket
      *@param String outputMessage stores the message that will be sent
      *@param HashSet<Integer> numsCalled creates Hashset object to store the amount of numbers called
      *@param Random generate creates Random object
      *@param String theadName stores the name of the thread
      */
      
      Socket s;
      String outputMessage = "";     
      HashSet<Integer> numsCalled = new HashSet<Integer>();
      Random generate = new Random();
      String threadName = ""; 
      
      /*
       *Constructor that takes in these variables and sets them to the ones in the 
       *inner class
       *@param Socket _s
       *@param String _name sets the name of the thread
      */
      public MainServerInner(Socket _s, String _name){
         s = _s;
         threadName = _name;
      }
      
      /*
      *Accessor for threadName
      *@return threadName returns threadName
      */
      public String getThreadName(){
         return threadName;
      }
      
      /*
      *Accessor for makeQuestion that randomly generates question via generate.
      *@return questionsList returns the question list of that specified index
      */
      public String makeQuestion(){
         
         index = generate.nextInt(25);
         while(numsCalled.contains(index)){
            index = generate.nextInt(25); 
            
         }
         numsCalled.add(index);
         
         return questionsList.get(index);
      }
      
      /*
      *Accessor for makeAnswer
      *@return answerList returns the answer from the specified index
      */
      public String makeAnswer(){
         return answersList.get(index);
      }
      
      /*
      *Method that prints out to each client depending on the condition
      */
      public void broadcast(String msg){
         for(PrintWriter p: printers)
         {
            //Decrements playerCount and breaks out of statement if msg equals DISCONNECT
            //Prints to the client if it isn't.
            if(msg.contains("DISCONNECT: ")){
               serverInfo.append(msg.substring(12) + "\n");
               playerCount--;
               break;
            }
            else{                
               p.println(msg);
               p.flush();
            }
         }
      }
      
      /*
      *Method that prints out to the client to unlock depending on the 
      *condition
      */
      public void unlockNextClient(){
         if(printerNumber == -1){
            printerNumber++;
            printers.get(printerNumber).println("UNLOCK");
            printers.get(printerNumber).flush();
         }
         else if(printerNumber>= 0 && printerNumber<3){
            printerNumber++;
            printers.get(printerNumber).println("UNLOCK");
            printers.get(printerNumber).flush();
         }        
         else{
            printerNumber = 0;
            printers.get(printerNumber).println("UNLOCK");
            printers.get(printerNumber).flush();
            //set the unlock to printer zero
         }
      
      }
      /*
      *Method that calls upon the broadcast method to "LOCK"
      */
      public void lockClients(){
         broadcast("LOCK");
      }
      /*
      *Method that prints out to unlock the current client depending on the
      *printer number.
      */
      public void unlockCurrentClient(){
         printers.get(printerNumber).println("UNLOCK");
         printers.get(printerNumber).flush();
      }
      
      /*
      *Method that prints unlock to the first client in the index. 
      */
      public void firstClientUnlock()
      {
         printers.get(0).println("UNLOCK");
         printers.get(0).flush();
      }
      
      /*
      *Thread's run method. Instantiates both BufferedReader and PrintWriter and 
      *adds the PrintWriter to the Vector list. It also deals with multiple conditons
      *depending on the condition from the received message from the BufferedReader
      */
      public void run()
      {
         BufferedReader brRun = null;
         PrintWriter pwRun = null;          
         
         try{        
            brRun = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pwRun = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            printers.add(pwRun);
            addAnswers();
            addQuestions();
            
            //Always reading messages received.
            while(true){
               outputMessage = brRun.readLine();
               
               /*
               *Increments playerCount and stores the outputMessage in playerName 
               *if the outputMessage contains player name and does not contain Message and ready 
               */                
               if(outputMessage.contains("Player Name:") && !outputMessage.contains("Message:") && !outputMessage.contains("READY!!!")){
                  if(playerCount<3){
                     playerCount++;                  
                     serverInfo.append("Player Number: "+(playerCount+1) + "\n");
                     playerName[playerCount] = outputMessage.substring(12);
                     serverInfo.append("Name entered: "+playerName[playerCount]+"\n");
                     serverInfo.append("Player: "+playerName[playerCount]+ " connected.\n\n\n");   
                     
                  } 
                  
                  //cycles through playerName and adds the list to teamMemberNames    
                  if(playerCount == 3 ){                                             
                        
                     String teamMemberNames = "NameHeader, ";
                                      
                     for(String list: playerName){
                        teamMemberNames +=(list+", ");
                     }
                     //this is for the list of players that are connected
                     broadcast("NameHeader: "+teamMemberNames);
                     unlockNextClient();
                     playerCount++;
                  }  
                  
                  //Prints to the client spectator if the playercount is greater than 4 players                      
                  else if(playerCount>=4){
                     printers.get(playerCount).println("SPECTATOR: ");
                     printers.get(playerCount).flush();
                     playerCount++;

                  }
                     
                                      
               }          
               
               //If message received is ready, multiple conditions will be checked.               
               else if(outputMessage.contains("READY!!!")){
                  //Increments questionCount if it's less than 4
                  if(questionCount < 4){
                     questionCount++;
                  }
                  /*
                  *Increments countQuestionSent if the questionCount is 4, and checks
                  *will send to the clients "END" if the countQuestionsSent is 6
                  */
                  else if(questionCount == 4){
                     countQuestionsSent++;
                     if(countQuestionsSent == 6)
                     {
                        broadcast("END");
                     }
                     //Resets if it's not 6
                     else
                     {                                          
                        questionCount = 1;
                        broadcast("RESET");
                        outputMessage = "Question: "+ makeQuestion();
                        lockClients();
                        broadcast(outputMessage);
                        firstClientUnlock();
                     } 
                  }  
                                
               }
               
               /*
               *If the output is Answer, sets wrongs to false and gets the threadName
               *in order to store in printerNumbers. It will then lock the clients and cycle
               *through input.
               */   
               else if(outputMessage.contains("Answer: ")){
                  boolean wrongs = false;
                  printerNumber = Integer.parseInt(getThreadName());
                  lockClients();
                  String temp = answersList.get(index);
                  String[] input = temp.split(",");
                   
                  //Cycles through input and broadcasts the correct answer.   
                  for(String list : input){
                     
                     serverInfo.append("Answer Submitted: "+outputMessage+"\n");
                        
                     if(list.contains(outputMessage.substring(8))){
                        
                        serverInfo.append("Correct: " + list+"\n\n");
                        broadcast("Correct: " + list);
                        unlockCurrentClient();
                        boardCount++;
                        wrongs = false;
                        break;                           
                     }
                     else if(!list.contains(outputMessage.substring(8))){
                        wrongs = true;
                     }
                     
                  }
                  //sends wrong to client and sets wrongs to false and allows the next client to go
                  if(wrongs){
                     broadcast("WRONG!!!");
                     serverInfo.append("The answer submitted is wrong\n");
                     unlockNextClient();
                     wrongs = false;
                  
                  }
                  //Checks to see that the boardcount is full
                  if(boardCount == 8)
                  {
                     broadcast("FULL");
                     boardCount = 0;
                  }
               }              
               broadcast(outputMessage);
            }
         }              
            
         catch(NullPointerException npe)
         {
         }        
         catch(IOException ioe){
         }
      
      }
     
      
      /*
      *Method that reads the answers from a CSV file using FileReader, which is then
      *passed to a BufferedReader and stored.
      */
      public void addAnswers(){
         FileReader frTwo = null;
         BufferedReader brTwo = null;      
         String line;      
         try{
            frTwo = new FileReader("FAMILY FEUD ANSWERS.csv");
            brTwo = new BufferedReader(frTwo);
         }
         catch(FileNotFoundException fnfe){
         }
         
         //reads through the answers csv file until its null.
         try{
            line = brTwo.readLine();         
         
            while(!line.equals(null)){
               answersList.add(line);        
               line = brTwo.readLine();
            }
         }
         catch(IOException ioe){
         } 
         catch(NullPointerException npe){
         }   
      }
      
      /*
      *Method that reads the questions from a CSV file using FileReader, which is then
      *passed to a BufferedReader and stored.
      */
      public void addQuestions()
      {
         FileReader fr = null;
         BufferedReader br = null;      
         String line;
      
         try
         {
            fr = new FileReader("FAMILY FEUD QUESTIONS.csv");
            br = new BufferedReader(fr);
         }
         catch(FileNotFoundException fnfe)
         {
         }
         
         //reads through the answers csv file until its null.
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
      }
   
   }
   public static void main(String [] args)
   {
      MainServer mainServer = new MainServer();
   }

}