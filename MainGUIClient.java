//imports the necesary classes for the GUI program
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class MainGUIClient
{
   private JFrame frame;
   
   private int scoreOne = 0;
   private int scoreTwo = 0;
   private int scoreThree = 0;
   private int scoreFour = 0;
   private int checkPlayer = 0;
   private int questionCounter = 0;
     
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
   
   public MainGUIClient(String _aPlayer)
   {
      aPlayer = _aPlayer;
      frame = new JFrame();
      frame.setSize(1000, 800);
      frame.setTitle("Family Feud");
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

   public static void main(String [] args)
   {
      MainGUIClient mainClient = new MainGUIClient(aPlayer);
   }

   class SenderGUI implements ActionListener
   {
      Socket s;
      BufferedReader br = null;   
      JTextField answer;
      JTextField theAddresses;
      String playerName;
      JTextField message;
      JTextArea messageArea;

      
      public SenderGUI(JTextField _answer, JTextField _theAddresses, String _playerName, JTextField _message, JTextArea _messageArea)
      {
         answer = _answer;
         theAddresses = _theAddresses;
         playerName = _playerName;
         message = _message;
         messageArea = _messageArea;
      }
      
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
         catch(UnknownHostException uhe)
         {
            System.err.println("Cannot find the host");
            connect.setEnabled(true);
            readyUp.setEnabled(false);
         }
         catch(IOException ioe)
         {
            System.out.println("Lost connection with the server");
            connect.setEnabled(true);
            readyUp.setEnabled(false);
         }
      }
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
      
      public void actionPerformed(ActionEvent ae)
      {     
         if(ae.getActionCommand().equals("Submit Answer"))
         {
            sendTheAnswer();
            sendAnswer.setEnabled(false);
         }
         else if(ae.getActionCommand().equals("Send Message"))
         {
            sendTheMessage();
            
         }
         else if(ae.getActionCommand().equals("Connect"))
         {
            readyUp.setEnabled(true);
            connect();
            
         }
         else if(ae.getActionCommand().equals("Ready")){
            connect.setEnabled(false);
            readyUp();
            readyUp.setEnabled(false);
         }
      }
   
      class Reader extends Thread
      {      
         String msg;
         boolean gotHeader = false;
         public void run()
         {  
            try
            {
               while(true)
               {
                  msg = br.readLine();
                  if(msg.equals("UNLOCK")){
                     sendAnswer.setEnabled(true);
                     System.out.println("I was told to unlock");

                  }                  
                  else if(msg.equals("LOCK")){
                     sendAnswer.setEnabled(false);
                     System.out.println("I WAS TOLD TO LOCK");
                  }
                  else if(msg.contains("NameHeader, ")){
                    if(!gotHeader){
                        String input = msg.substring(11);
                    
                        String[] output = input.split(",");
                   
                        pOne.setText(pOne.getText()+"\t"+output[1] + "\tScore:\t");
                        pTwo.setText(pTwo.getText()+"\t"+output[2]  + "\tScore:\t");
                        pThree.setText(pThree.getText()+"\t"+output[3]  + "\tScore:\t");
                        pFour.setText(pFour.getText()+"\t"+output[4]  + "\tScore:\t");
                        gotHeader = true;
                    }
                    
                  }
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
                  else if(msg.contains("Message: "))
                  {
                     String temp = msg.substring(22);
                     messageArea.append(temp + "\n");
                  }
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
                  if(msg.equals("END")){
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
                           String pOneNewText = pOne.getText();
                           String pTwoNewText = pOne.getText();
                           String pThreeNewText = pOne.getText();
                           String pFourNewText = pOne.getText();                           
                           
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
                  else if (msg.equals("WRONG!!!")){
                     
                     JOptionPane.showMessageDialog(null, "The answer submitted was wrong.");
                     checkPlayer++;
                     if(checkPlayer == 4)
                     {
                        checkPlayer = 0;
                     }
                  }
                  else if(msg.equals("FULL"))
                  {
                     readyUp.setEnabled(true);
                     header.setText("Press READY to recieve the next question.");
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