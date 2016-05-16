/*
 * @authors Tanner Glantz, Brett Phillips, Rosalee Hacker, and Alejandro Garzon
 * @version 05/14/2016
 * Description: The MainGUIClient class creates and handles all the GUI visual components and
 * also creates the connection between the client and the server.   
 * Course: ISTE-121
 */

//imports the necesary classes for the GUI program
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class MainGUIClient
{
   /*
   *@param JFrame frame creates the frame object
   *@param int scoreOne player one score will be stored here
   *@param int scoreTwo player two score will be stored here
   *@param int scoreThree player three score will be stored here
   *@param int scoreFour player four score will be stored here
   *@param int checkPlayer compares the player's score
   *@param int questionCounter keeps count of the questions asked
   *@param static String aPlayer player's name stored here
   *@param JPanel chatPanel panel for the chat elements
   *@param JPanel chatSouth panel that has the chat elements in the south
   *@param JPanel playerPanel panel for player elements
   *@param JLabel pOne label that contains player one name
   *@param JLabel pTwo label that contains player two name
   *@param JLabel pThree label that contains player three name
   *@param JLabel pFour label that contains player four name
   *@param JPanel panelOneNorth panel that will be located north
   *@param JLabel serverLabel contains the serverLabel
   *@param JTextField ipAddress where the user enters the IP for the socket to use
   *@param JTextField playerName user enters their name
   *@param JButton connect connects the player to the server
   *@param JButton readyUp user will press once connected and waiting for others to ready up
   *@param JPanel panelTwoNorth contains elements for north side of JFrame
   *@param JLabel header the header of the frame
   *@param JPanel northPanel panel located in the north of frame
   *@param JPanel eastPanel panel located in the east side of frame
   *@param JTextArea messageArea area where chat messages are displayed
   *@param JTextField message field where users will type their chat messages
   *@param JButton sendMessage button used for sending messages
   *@param JPanel insideMessage panel that holds the JTextField for answer box and JButton
   *@param JTextField answer field where user will type the answer
   *@param JButton sendAnswer button used for sending the answer
   *@param JPanel centerPanel panel located in center of frame
   *@param JPanel insideCenterGrid holds all 1-8 JButtons where answers are displayed
   *@param JButton answerOne button where first answer is displayed
   *@param JButton answerTwo button where second answer is displayed
   *@param JButton answerThree button where third answer is displayed
   *@param JButton answerFour button where fourth answer is displayed
   *@param JButton answerFive button where fith answer is displayed
   *@param JButton answerSix button where sixth answer is displayed
   *@param JButton answerSeven button where seventh answer is displayed
   *@param JButton answerEight button where eighth answer is displayed
   *@param PrintWriter pw creates PrintWriter object
   */
   private JFrame frame;
   
   private int scoreOne = 0;
   private int scoreTwo = 0;
   private int scoreThree = 0;
   private int scoreFour = 0;
   private int checkPlayer = 0;
   private int questionCounter = 0;
   private int fullBoardCounter = 0;
     
   private static String aPlayer;
   
   private JPanel chatPanel;
   private JPanel chatSouth;
   private JPanel playerPanel;
   private JLabel pOne;
   private JLabel pTwo;
   private JLabel pThree;
   private JLabel pFour;   
   
   private JPanel panelOneNorth;
   private JLabel serverLabel;
   private JTextField ipAddress;
   private JTextField playerName;
   private JButton connect;
   private JButton readyUp;
   private JPanel panelTwoNorth;
   private JLabel header;

   private JPanel northPanel;
   
   private JPanel eastPanel;
   private JTextArea messageArea;
   private JTextField message;
   private JButton sendMessage;    
   
   private JPanel insideMessage;
   private JTextField answer;
   private JButton sendAnswer;
   
   private JPanel centerPanel;
   private JPanel insideCenterGrid;
   private JButton answerOne;
   private JButton answerTwo;
   private JButton answerThree;
   private JButton answerFour;
   private JButton answerFive;
   private JButton answerSix;
   private JButton answerSeven;
   private JButton answerEight;
   private PrintWriter pw = null;
   
   /*
    *Main constructor for the class. It instantiates and sets all the JFrame components
    *that will be used for the program
    *@param String _aPlayer takes in a player  
   */ 
   public MainGUIClient(String _aPlayer)
   {
      aPlayer = _aPlayer;
      frame = new JFrame();
      frame.setSize(1000, 800);
      frame.setTitle("Family Feud");
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      frame.setResizable(false);
      //Overides the default window closing
      WindowListener exitListener = new WindowAdapter() {  
         public void windowClosing(WindowEvent e) {         
            try{
               System.out.println("Client "+aPlayer.substring(12)+" disconnected.");
         
               pw.println("DISCONNECT: "+aPlayer.substring(12)+" disconnected.");
               pw.flush();
            }
            catch(Exception ee){
               System.out.println("Server is not connected.");
            }
         System.exit(0);
           }};
         frame.addWindowListener(exitListener);

      frame.setLocationRelativeTo(null);
      
      northPanel = new JPanel(new GridLayout(2, 1));
      
      panelOneNorth = new JPanel();
      serverLabel = new JLabel("Server: ");
      panelOneNorth.add(serverLabel);
      ipAddress = new JTextField(10);
      panelOneNorth.add(ipAddress);
      connect = new JButton("Connect");      
      readyUp = new JButton("Ready");
      panelOneNorth.add(connect);
      panelOneNorth.add(readyUp);
      readyUp.setEnabled(false);
      
      northPanel.add(panelOneNorth);
      
      panelTwoNorth = new JPanel();
      header = new JLabel("FAMILY FEUD");
      header.setFont(new Font("Arial", Font.BOLD, 75));
      header.setForeground(Color.YELLOW);
      panelTwoNorth.add(header);  
      panelTwoNorth.setBackground(Color.BLUE);
      northPanel.add(panelTwoNorth);
      
      frame.add(northPanel, BorderLayout.NORTH); 
      
      chatPanel = new JPanel(new BorderLayout());
     
      eastPanel = new JPanel(new GridLayout(2,2));
      messageArea = new JTextArea(11, 20);
      messageArea.setEditable(false);
      eastPanel.add(new JScrollPane(messageArea));
      chatPanel.add(eastPanel, BorderLayout.CENTER);
      
      playerPanel = new JPanel(new GridLayout(4,1, 20, 20));
      pOne = new JLabel("Player One: ");
      playerPanel.add(pOne);
      pTwo = new JLabel("Player Two: ");
      playerPanel.add(pTwo);
      pThree = new JLabel("Player Three: ");
      playerPanel.add(pThree);
      pFour = new JLabel("Player Four: ");
      playerPanel.add(pFour);
      chatPanel.add(playerPanel, BorderLayout.NORTH);
      
      chatSouth = new JPanel();
      message = new JTextField(10);
      chatSouth.add(message);
      sendMessage = new JButton("Send Message");
      chatSouth.add(sendMessage); 
      chatPanel.add(chatSouth, BorderLayout.SOUTH);
           
      frame.add(chatPanel, BorderLayout.EAST);   
      
      centerPanel = new JPanel(new BorderLayout());
      insideCenterGrid = new JPanel(new GridLayout(4,2));
      
      answerOne = new JButton("1");
      insideCenterGrid.add(answerOne);
      answerOne.setEnabled(false);
      answerTwo = new JButton("2");
      insideCenterGrid.add(answerTwo);
      answerTwo.setEnabled(false);
      answerThree = new JButton("3");
      insideCenterGrid.add(answerThree);
      answerThree.setEnabled(false);
      answerFour = new JButton("4");
      insideCenterGrid.add(answerFour);
      answerFour.setEnabled(false);
      answerFive = new JButton("5");
      insideCenterGrid.add(answerFive);
      answerFive.setEnabled(false);
      answerSix = new JButton("6");
      insideCenterGrid.add(answerSix);
      answerSix.setEnabled(false);
      answerSeven = new JButton("7");
      insideCenterGrid.add(answerSeven);
      answerSeven.setEnabled(false);
      answerEight = new JButton("8");
      insideCenterGrid.add(answerEight);
      answerEight.setEnabled(false);
      centerPanel.add(insideCenterGrid);
      
      insideMessage = new JPanel();
      answer = new JTextField(20);
      insideMessage.add(answer);
      sendAnswer = new JButton("Submit Answer");
      sendAnswer.setEnabled(false);
      insideMessage.add(sendAnswer);      
      centerPanel.add(insideMessage, BorderLayout.SOUTH);      

      frame.add(centerPanel, BorderLayout.CENTER);
      
      SenderGUI sender = new SenderGUI(answer, ipAddress, aPlayer, message, messageArea);
      sendAnswer.addActionListener(sender);
      sendMessage.addActionListener(sender);
      connect.addActionListener(sender);
      readyUp.addActionListener(sender);
      
      frame.setVisible(true);
   }
   
   //MainGUIClient's main method
   public static void main(String [] args)
   {
      MainGUIClient mainClient = new MainGUIClient(aPlayer);
   }
   
   /*
    *Inner class that deals with the connecton between the GUI and the server such as
    *instantiating sockets, using action events, and sending answers and messages.
    * 
   */
   class SenderGUI implements ActionListener
   {
   
   /*
   *@param Socket s creates socket object
   *@param BufferedReader br creates buffered reader object
   *@param JTextField answer field where answer wil be typed
   *@param JTextField theAddresses field where the adress will be typed
   *@param String playerName stores the player name
   *@param JTextField message field where message will be typed
   *@param JTextArea messageArea area where messages will be displayed
   */
      Socket s;
      BufferedReader br = null;   
      JTextField answer;
      JTextField theAddresses;
      String playerName;
      JTextField message;
      JTextArea messageArea;

      /*
       *Constructor that takes in these variables and sets them to the ones in the 
       *inner class
       *@param JTextField _answer answer field where answer wil be typed
       *@param JTextField _theAddresses field where the adress will be typed
       *@param String _playerName stores the player name
       *@param JTextField _message field where message will be typed
       *@param JTextArea messageArea area where messages will be displayed
      */
      public SenderGUI(JTextField _answer, JTextField _theAddresses, String _playerName, JTextField _message, JTextArea _messageArea)
      {
         answer = _answer;
         theAddresses = _theAddresses;
         playerName = _playerName;
         message = _message;
         messageArea = _messageArea;
      }
      
      /*
      *Method that will be used when the player presses connect. It creates the  
      *socket and the BufferedReader and PrintWriter, as well as changing certain
      *elements within the GUI in order for the user to know they have connected. 
      */
      public void connect()
      {
         try
         {
            System.out.println("IP Address: "+theAddresses.getText());
            s = new Socket(theAddresses.getText(), 16100);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            new Reader().start();
            pw.println(playerName);
            pw.flush();
            connect.setEnabled(false);
            header.setText("Press READY to begin the game");
            header.setFont(new Font("Arial", Font.BOLD, 45));            
         }
         //When UnknownException is catched, a message will be printed and the readyUp will be disabled
         catch(UnknownHostException uhe)
         {
            System.err.println("Cannot find the host");
            connect.setEnabled(true);
            readyUp.setEnabled(false);
         }
         //When IOExceptione is catched, a message will be printed and the readyUp will be disabled.
         catch(IOException ioe)
         {
            System.out.println("Lost connection with the server");
            connect.setEnabled(true);
            readyUp.setEnabled(false);
         }
      }
      
      /*
      *Method that will be used when the player presses readyUp. It prints to the server that
      *it is ready and changes some GUI components for the user to know they have ready'd up.
      */
      public void readyUp(){
         try
         {
            pw.println("READY!!!");
            pw.flush();
            header.setText("Waiting for other players...");
            header.setFont(new Font("Arial", Font.BOLD, 45));            
         }
         catch(NullPointerException npe)
         {
            System.err.println("You are not connected to the server or the server is off.");
         } 
      
      }
      
      /*
      *Method that sends the answer to the server
      */
      public void sendTheAnswer()
      {
         try
         {
            pw.println("Answer: "+answer.getText().toLowerCase());
            pw.flush();
            System.out.println("Answer: "+answer.getText());
            answer.setText("");
         }
         catch(NullPointerException ioe)
         {
            System.err.println("Error, you must connect to the server before sending a message");
         } 
      } 
      
      /*
      *Method that sends the chat message to the server
      */
      public void sendTheMessage()
      {
         try
         {
            pw.println("Message: " + aPlayer+": "+message.getText());
            pw.flush();
            message.setText("");
         }
         catch(NullPointerException ioe)
         {
            System.err.println("Error, you must connect to the server before sending a message");
         } 
      }      
      
      /*
      *Method that deals with all the actions performed depending on 
      *the condition's statement.
      *@param ActionEvent ae
      */
      public void actionPerformed(ActionEvent ae)
      {  
         //Calls the sendTheAnswer method if condition is met   
         if(ae.getActionCommand().equals("Submit Answer"))
         {
            sendTheAnswer();
            sendAnswer.setEnabled(false);
         }
         //Calls the sendTheMessage method if condition is met
         else if(ae.getActionCommand().equals("Send Message"))
         {
            sendTheMessage();
            
         }
         //Enables readyUp and calls the connect method if the condition is met
         else if(ae.getActionCommand().equals("Connect"))
         {
            readyUp.setEnabled(true);
            connect();
            
         }
         //Disables connect and calls readyUp method and then disables it if the condition is met 
         else if(ae.getActionCommand().equals("Ready")){
            connect.setEnabled(false);
            readyUp();
            readyUp.setEnabled(false);
         }
      }
      
      /*
      *Inner class that deals with multithreaded clients. It is always
      *waiting for a message and will act depending on what the server
      *writes back. 
      */
      class Reader extends Thread
      {      
         String msg;
         boolean gotHeader = false;
         //Run method
         public void run()
         {  
            try
            {
               /*Always running and stores what is recieved into 'msg',
               *which is then used to compare and meet conditions
               */
               while(true)
               {
                  msg = br.readLine();
                  //Unlocks sendAnswer if message received matches the condition.
                  if(msg.equals("UNLOCK")){
                     sendAnswer.setEnabled(true);
                     System.out.println("I was told to unlock");
                     
                  }  
                  //Locks sendAnswer if message received matches the condition                
                  else if(msg.equals("LOCK")){
                     sendAnswer.setEnabled(false);
                     System.out.println("I WAS TOLD TO LOCK");
                  }
                  //Sets all the player's text if message received matches the condition and sets gotHeader to true
                  else if(msg.contains("NameHeader, ")){
                    if(!gotHeader){
                        String input = msg.substring(11);
                    
                        String[] output = input.split(",");
                   
                        pOne.setText(pOne.getText()+"  \t"+output[1]+" " + "  \tScore:\t  ");
                        pTwo.setText(pTwo.getText()+"  \t"+output[2]+" "  + "  \tScore:\t  ");
                        pThree.setText(pThree.getText()+"  \t"+output[3]+" "  +  " \tScore:\t  ");
                        pFour.setText(pFour.getText()+"  \t"+output[4]+" "  + "  \tScore:\t  ");
                        gotHeader = true;
                        pOne.setForeground(Color.RED);
                    }
                    
                  }
                  /*Handles multiple conditons and sets the header depending on
                  *the questionCounter. After condition is met, the thread will sleep for
                  *two seconds and increment the questionCounter
                  */
                  else if(msg.contains("Question: "))
                  {
                     if(questionCounter == 1)
                     {
                        header.setText("ROUND 1");
                        header.setFont(new Font("Arial", Font.BOLD, 75));
                     }
                     else if(questionCounter == 3)
                     {
                        header.setText("ROUND 2");
                        header.setFont(new Font("Arial", Font.BOLD, 75));
                     }
                     else if(questionCounter == 4)
                     {
                        header.setText("ROUND 3");
                        header.setFont(new Font("Arial", Font.BOLD, 75));
                     }
                     else if(questionCounter == 6)
                     {
                        header.setText("ROUND 4");
                        header.setFont(new Font("Arial", Font.BOLD, 75));
                     } 
                     else if(questionCounter == 8)
                     {
                        header.setText("FINAL ROUND");
                        header.setFont(new Font("Arial", Font.BOLD, 75));
                     }                                                                                   
                     
                     try
                     {
                        Thread.sleep(2000);
                     }
                     catch(InterruptedException ie)
                     {
                     }
                     
                     questionCounter++;
                     
                     header.setText(msg);
                     header.setFont(new Font("Arial", Font.BOLD, 18));
                  }
                  //Sets the message in the message area if message received meets condition
                  else if(msg.contains("Message: "))
                  {
                     String temp = msg.substring(22);
                     messageArea.append(temp + "\n");
                  }
                  
                  /*
                  *Handles multiple conditions depending on the value
                  *of the correct answer. It will check the value of the question
                  *and see who answered it correctly and update their score.
                  */
                  else if(msg.contains("Correct: "))
                  {
                     if(msg.contains("80"))
                     {
                        answerOne.setText(msg);
                        System.out.println("right for 80");
                        System.out.println(checkPlayer);
                        if(checkPlayer == 0)
                        {
                        System.out.println("add 80");
                           scoreOne += 80;
                        }
                        else if(checkPlayer == 1)
                        {
                           scoreTwo += 80;
                        }
                        else if(checkPlayer == 2)
                        {
                           scoreThree += 80;
                        }                        
                        else if(checkPlayer == 3)
                        {
                           scoreFour += 80;
                        }                         
                     }
                     else if(msg.contains("70"))
                     {
                        answerTwo.setText(msg);
                        if(checkPlayer == 0)
                        {
                           scoreOne += 70;
                        }
                        else if(checkPlayer == 1)
                        {
                           scoreTwo += 70;
                        }
                        else if(checkPlayer == 2)
                        {
                           scoreThree += 70;
                        }                        
                        else if(checkPlayer == 3)
                        {
                           scoreFour += 70;
                        }                        
                     }                     
                    else if(msg.contains("60"))
                     {
                        answerThree.setText(msg);
                        if(checkPlayer == 0)
                        {
                           scoreOne += 60;
                        }
                        else if(checkPlayer == 1)
                        {
                           scoreTwo += 60;
                        }
                        else if(checkPlayer == 2)
                        {
                           scoreThree += 60;
                        }                        
                        else if(checkPlayer == 3)
                        {
                           scoreFour += 60;
                        }                         
                     }
                    else if(msg.contains("50"))
                     {
                        answerFour.setText(msg);
                        if(checkPlayer == 0)
                        {
                           scoreOne += 50;
                        }
                        else if(checkPlayer == 1)
                        {
                           scoreTwo += 50;
                        }
                        else if(checkPlayer == 2)
                        {
                           scoreThree += 50;
                        }                        
                        else if(checkPlayer == 3)
                        {
                           scoreFour += 50;
                        }                        
                     }
                    else if(msg.contains("40"))
                     {
                        answerFive.setText(msg);
                        if(checkPlayer == 0)
                        {
                           scoreOne += 40;
                        }
                        else if(checkPlayer == 1)
                        {
                           scoreTwo += 40;
                        }
                        else if(checkPlayer == 2)
                        {
                           scoreThree += 40;
                        }                        
                        else if(checkPlayer == 3)
                        {
                           scoreFour += 40;
                        }                        
                     }
                    else if(msg.contains("30"))
                     {
                        answerSix.setText(msg);
                        if(checkPlayer == 0)
                        {
                           scoreOne += 30;
                        }
                        else if(checkPlayer == 1)
                        {
                           scoreTwo += 30;
                        }
                        else if(checkPlayer == 2)
                        {
                           scoreThree += 30;
                        }                        
                        else if(checkPlayer == 3)
                        {
                           scoreFour += 30;
                        }                        
                     }
                    else if(msg.contains("20"))
                     {
                        answerSeven.setText(msg);
                        if(checkPlayer == 0)
                        {
                           scoreOne += 20;
                        }
                        else if(checkPlayer == 1)
                        {
                           scoreTwo += 20;
                        }
                        else if(checkPlayer == 2)
                        {
                           scoreThree += 20;
                        }                        
                        else if(checkPlayer == 3)
                        {
                           scoreFour += 20;
                        }                        
                     }
                    else if(msg.contains("10"))
                     {
                        answerEight.setText(msg);
                        if(checkPlayer == 0)
                        {
                           scoreOne += 10;
                        }
                        else if(checkPlayer == 1)
                        {
                           scoreTwo += 10;
                        }
                        else if(checkPlayer == 2)
                        {
                           scoreThree += 10;
                        }                        
                        else if(checkPlayer == 3)
                        {
                           scoreFour += 10;
                        }                        
                     }
                     System.out.println(scoreOne);
                     System.out.println(scoreTwo);
                     System.out.println(scoreThree);
                     System.out.println(scoreFour);
                  }
                  //Resets the board's questions and advance to the next question if message received meets condition
                  else if(msg.contains("RESET")){
                     answerOne.setText("1");
                     answerTwo.setText("2");
                     answerThree.setText("3");
                     answerFour.setText("4");
                     answerFive.setText("5");
                     answerSix.setText("6");
                     answerSeven.setText("7");
                     answerEight.setText("8");
                     String pOneNewText = pOne.getText();
                     String pTwoNewText = pTwo.getText();
                     String pThreeNewText = pThree.getText();
                     String pFourNewText = pFour.getText();
                     pOne.setText(pOne.getText().substring(0, pOneNewText.indexOf("Score") + 6)  + "\t" + scoreOne);
                     pTwo.setText(pTwo.getText().substring(0, pTwoNewText.indexOf("Score") + 6)  + "\t" + scoreTwo);
                     pThree.setText(pThree.getText().substring(0, pThreeNewText.indexOf("Score") + 6)  + "\t" + scoreThree);
                     pFour.setText(pFour.getText().substring(0, pFourNewText.indexOf("Score") + 6)  + "\t" + scoreFour);
                     checkPlayer = 0;
                     
                  }
                  /*Checks to see who has the highest score and determines who the
                  *winner is, which is then announced to every client. 
                  */
                  if(msg.equals("END")){
                  
                     String pOneNewText = pOne.getText();
                     String pTwoNewText = pTwo.getText();
                     String pThreeNewText = pThree.getText();
                     String pFourNewText = pFour.getText();
                     pOne.setText(pOne.getText().substring(0, pOneNewText.indexOf("Score") + 6)  + "\t" + scoreOne);
                     pTwo.setText(pTwo.getText().substring(0, pTwoNewText.indexOf("Score") + 6)  + "\t" + scoreTwo);
                     pThree.setText(pThree.getText().substring(0, pThreeNewText.indexOf("Score") + 6)  + "\t" + scoreThree);
                     pFour.setText(pFour.getText().substring(0, pFourNewText.indexOf("Score") + 6)  + "\t" + scoreFour);                  

                     int checkWinner = 0;
                     
                     for(int i = 0; i < 5; i++)
                     {
                        if(i == 0 && scoreOne >= checkWinner)
                        {
                           checkWinner = scoreOne;  
                        }
                        if(i == 1 && scoreTwo > checkWinner)
                        {
                           checkWinner = scoreTwo;  
                        }                        
                        if(i == 2 && scoreThree > checkWinner)
                        {
                           checkWinner = scoreThree;  
                        }                        
                        if(i == 3 && scoreFour > checkWinner)
                        {
                           checkWinner = scoreFour;  
                        }
                        if(i == 4)
                        {
                            pOneNewText = pOne.getText();
                            pTwoNewText = pTwo.getText();
                            pThreeNewText = pThree.getText();
                            pFourNewText = pFour.getText();                           
                           
                           if(scoreOne == checkWinner)
                           {
                              header.setText("Winner is: " + pOne.getText().substring(0, pOneNewText.indexOf("Score") + 6)  + "\t" + scoreOne);
                              header.setFont(new Font("Arial", Font.BOLD, 18));
                           }
                           else if(scoreTwo == checkWinner)
                           {
                              header.setText("Winner is: " + pTwo.getText().substring(0, pTwoNewText.indexOf("Score") + 6)  + "\t" + scoreTwo);
                              header.setFont(new Font("Arial", Font.BOLD, 18));
                           }
                           else if(scoreThree == checkWinner)
                           {
                              header.setText("Winner is: " + pThree.getText().substring(0, pThreeNewText.indexOf("Score") + 6)  + "\t" + scoreThree);
                              header.setFont(new Font("Arial", Font.BOLD, 18));
                           }
                           else if(scoreFour == checkWinner)
                           {
                              header.setText("Winner is: " + pFour.getText().substring(0, pFourNewText.indexOf("Score") + 6)  + "\t" + scoreFour);
                              header.setFont(new Font("Arial", Font.BOLD, 18));
                           }                         
                        }                        
                     }
                  }
                  /*
                  *Announces that the submitted answer is incorrect, and then 
                  *increment the checkPlayer. Once the checker reaches 4, it is reset
                  *back to 0
                  */                  
                  else if (msg.equals("WRONG!!!")){
                     
                     JOptionPane.showMessageDialog(null, "The answer submitted was wrong.");
                     checkPlayer++;
                     
                     pOne.setForeground(Color.BLACK);
                     pTwo.setForeground(Color.BLACK);
                     pThree.setForeground(Color.BLACK);
                     pFour.setForeground(Color.BLACK);
                     
                     if(checkPlayer == 4)
                     {
                        checkPlayer = 0;
                     }
                     
                     if(checkPlayer == 0)
                     {
                        pOne.setForeground(Color.RED);
                     }
                     else if(checkPlayer == 1)
                     {
                        pTwo.setForeground(Color.RED);
                     }
                     else if(checkPlayer == 2)
                     {
                        pThree.setForeground(Color.RED);
                     }
                     else if(checkPlayer == 3)
                     {
                        pFour.setForeground(Color.RED);
                     }
                     
                  }
                  /*
                  *Once 4 players are connected, it enables readyUp and changes
                  *the header for the user to know they are ready to ready up
                  */
                  else if(msg.equals("FULL"))
                  {
                     fullBoardCounter++;
                     readyUp.setEnabled(true);
                     if(fullBoardCounter < 5)
                     {
                        header.setText("Press READY to recieve the next question.");
                        header.setFont(new Font("Arial", Font.BOLD, 45));
                     }
                     else
                     {
                        header.setText("Press READY to see the winner.");
                        header.setFont(new Font("Arial", Font.BOLD, 45));
                     }
                  }
                  /*
                  *If a client tries to connect after 4 players have connected,
                  *they will have readyUp and connect disabled and will only be
                  *allowed to spectate the match
                  */
                  else if(msg.equals("SPECTATOR: "))
                  {
                     readyUp.setEnabled(false);
                     connect.setEnabled(false);
                     header.setText("You are a spectator, enjoy the game!");
                     header.setFont(new Font("Arial", Font.BOLD, 45));                     
                  }
               }
            }
            catch(NullPointerException npe)
            {
               System.err.println("Server has been shutdown. Thank you for playing.");
            }
            catch(IOException ioe)
            {
            }
         }
      }
   }
}