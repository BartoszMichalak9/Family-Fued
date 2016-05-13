//imports the necesary classes for the GUI program
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class MainServer
{   
   private ArrayList<String> questionsList = new ArrayList<String>();
   private ArrayList<String> answersList = new ArrayList<String>();
   private Vector<PrintWriter> printers = new Vector<PrintWriter>();
   private int[] playerScore = new  int[4];
   private String[] playerName = new String[4];
   private int playerCount = -1;
   private int readyCount = 0;
   private int index = 0;
   
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
      catch(IOException ioe){
      }                   
   }
   
   class MainServerInner extends Thread
   {
      Socket s;
      String outputMessage = "";     
      HashSet<Integer> numsCalled = new HashSet<Integer>();
      Random generate = new Random();
            
      public MainServerInner(Socket _s){
         s = _s;
      }
      
      public String makeQuestion(){
         boolean check = false;
         index = generate.nextInt(25);
         String output = "";
         while(!check){
         if(!numsCalled.contains(index)){
            numsCalled.add(index);
            check = true;
            output = questionsList.get(index);
            return output;
         }
         else{
            index = generate.nextInt(25);             
         }
      }
         return "error";
      }
      
      public String makeAnswer(){
         return answersList.get(index);
      }
      
      public void broadcast(String msg){
         for(PrintWriter p: printers)
               {
                  if(msg.contains("DISCONNECT: ")){
                     System.out.println(msg.substring(12));
                     playerCount--;
                     break;
                  }
                  else{                
                     p.println(msg);
                     p.flush();
                  }
               }
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
            addAnswers();
            addQuestions();
            
       try{
            while(true)
            {
               outputMessage = brRun.readLine();
               
                  if(playerCount<3){
                     if(outputMessage.contains("Player Name:") && !outputMessage.contains("Message:")){
                           playerCount++;
                           System.out.println("Player Number: "+(playerCount+1));
                           playerName[playerCount] = outputMessage.substring(12);
                           System.out.println("Name entered: "+playerName[playerCount]);
                           System.out.println("Player: "+playerName[playerCount]+ " connected.\n#########\n");
      
                      }                   
                  }               
                                
                 if(playerCount==3){
                     String teamMemberNames = "NameHeader, ";
                     for(String list: playerName){
                     teamMemberNames +=(list+", ");
                     }
                     broadcast("NameHeader: "+teamMemberNames);
                     outputMessage = "Question: "+ makeQuestion();
                     broadcast(outputMessage);
                     playerCount = -1;
                     broadcast("RESET");             
                 }
                 if(outputMessage.contains("Answer: ")){
                   
                   String temp = answersList.get(index);
                   String[] x = temp.split(",");
                   for(String list : x){
                    
                     System.out.println("Database Answers: "+list);
                     System.out.println(outputMessage);
                     if(list.contains(outputMessage.substring(8))){
                        System.out.println("Correct: " + list+"\n");
                        broadcast("Correct: " + list);
                     }
                     else{
                        //broadcast("WRONG!!!");
                     }
                   }
                 }              
                               
                 broadcast(outputMessage);
              
            }
           }
           catch(NullPointerException npe)
           {
               System.err.println("Client has disconnected");
           }
         }
         catch(IOException ioe){
         }
      }
   }   
   
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

   public static void main(String [] args)
   {
      MainServer mainServer = new MainServer();
   }
}