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
         System.out.println("Client "+aPlayer.substring(12)+" disconnected.");
         try{
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
      connect = new JButton("Ready");      
      panelOneNorth.add(connect);
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
      messageArea = new JTextArea(16, 30);
      eastPanel.add(messageArea);
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
      insideMessage.add(sendAnswer);      
      centerPanel.add(insideMessage, BorderLayout.SOUTH);      

      frame.add(centerPanel, BorderLayout.CENTER);
      
      SenderGUI sender = new SenderGUI(answer, ipAddress, aPlayer, message, messageArea);
      sendAnswer.addActionListener(sender);
      sendMessage.addActionListener(sender);
      connect.addActionListener(sender);
      
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
         }
         catch(UnknownHostException uhe)
         {
            System.err.println("Cannot find the host");
         }
         catch(IOException ioe)
         {
            System.out.println("Lost connection with the server");
         }
      }
   
      public void sendTheAnswer()
      {
         try
         {
            pw.println("Answer: "+answer.getText());
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
         }
         else if(ae.getActionCommand().equals("Send Message"))
         {
            sendTheMessage();
         }
         else if(ae.getActionCommand().equals("Ready"))
         {
            connect();
         }
      }
   
      class Reader extends Thread
      {      
         String msg;
         
         public void run()
         {  
            try
            {
               while(true)
               {
                  msg = br.readLine();
                  if(msg.contains("NameHeader, ")){
                     String input = msg.substring(11);
                    
                     String[] output = input.split(",");
                      for(String x: output){
                        System.out.println("Testing...: "+x);
                     }
                     pOne.setText(pOne.getText()+"\t"+output[1]);
                     pTwo.setText(pTwo.getText()+"\t"+output[2]);
                     pThree.setText(pThree.getText()+"\t"+output[3]);
                     pFour.setText(pFour.getText()+"\t"+output[4]);
                  }
                  else if(msg.contains("Question: "))
                  {
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
                     }
                     else if(msg.contains("70"))
                     {
                        answerTwo.setText(msg);
                     }                     
                    else if(msg.contains("60"))
                     {
                        answerThree.setText(msg);
                     }
                    else if(msg.contains("50"))
                     {
                        answerFour.setText(msg);
                     }
                    else if(msg.contains("40"))
                     {
                        answerFive.setText(msg);
                     }
                    else if(msg.contains("30"))
                     {
                        answerSix.setText(msg);
                     }
                    else if(msg.contains("20"))
                     {
                        answerSeven.setText(msg);
                     }
                    else if(msg.contains("10"))
                     {
                        answerEight.setText(msg);
                     }
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
                     
                  }
                  else if (msg.contains("WRONG!!!")){
                     JOptionPane.showMessageDialog(null, msg.substring(8)+" "+aPlayer+" submitted was wrong.");
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










