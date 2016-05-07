//imports the necesary classes for the GUI program
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class MainGUIClient
{
   private static String aPlayer;
   
   private JPanel panelOneNorth;
   private JLabel serverLabel;
   private JTextField ipAddress;
   private JTextField playerName;
   private JButton connect;
   
   private JPanel panelTwoNorth;
   private JLabel header;

   private JPanel northPanel;
   
   private JPanel eastPanel;
   
   private JPanel southPanel;
   private JTextField answer;
   private JButton sendAnswer;    
   
   private JPanel centerPanel;
   private JButton answerOne;
   private JButton answerTwo;
   private JButton answerThree;
   private JButton answerFour;
   private JButton answerFive;
   private JButton answerSix;
   private JButton answerSeven;
   private JButton answerEight;
   
   public MainGUIClient(String _aPlayer)
   {
      aPlayer = _aPlayer;
      JFrame frame = new JFrame();
      frame.setSize(800, 800);
      frame.setTitle("Family Feud");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      
      northPanel = new JPanel(new GridLayout(2, 1));
      
      panelOneNorth = new JPanel();
      serverLabel = new JLabel("Server: ");
      panelOneNorth.add(serverLabel);
      ipAddress = new JTextField(10);
      panelOneNorth.add(ipAddress);
      connect = new JButton("Connect");      
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
      
      centerPanel = new JPanel(new GridLayout(4,2));
      answerOne = new JButton("1");
      centerPanel.add(answerOne);
      answerOne.setEnabled(false);
      answerTwo = new JButton("2");
      centerPanel.add(answerTwo);
      answerTwo.setEnabled(false);
      answerThree = new JButton("3");
      centerPanel.add(answerThree);
      answerThree.setEnabled(false);
      answerFour = new JButton("4");
      centerPanel.add(answerFour);
      answerFour.setEnabled(false);
      answerFive = new JButton("5");
      centerPanel.add(answerFive);
      answerFive.setEnabled(false);
      answerSix = new JButton("6");
      centerPanel.add(answerSix);
      answerSix.setEnabled(false);
      answerSeven = new JButton("7");
      centerPanel.add(answerSeven);
      answerSeven.setEnabled(false);
      answerEight = new JButton("8");
      centerPanel.add(answerEight);
      answerEight.setEnabled(false);
      frame.add(centerPanel, BorderLayout.CENTER);
      
      southPanel = new JPanel();
      answer = new JTextField(20);
      southPanel.add(answer);
      sendAnswer = new JButton("Send");
      southPanel.add(sendAnswer);
      frame.add(southPanel, BorderLayout.SOUTH);
      
      SenderGUI sender = new SenderGUI(answer, ipAddress, aPlayer);
      sendAnswer.addActionListener(sender);
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
      PrintWriter pw = null;   
      JTextField answer;
      JTextField theAddresses;
      String playerName;
      
      public SenderGUI(JTextField _answer, JTextField _theAddresses, String _playerName)
      {
         answer = _answer;
         theAddresses = _theAddresses;
         playerName = _playerName;
      }
      
      public void connect()
      {
         try
         {
            System.out.println(theAddresses.getText());
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
            pw.println(answer.getText());
            pw.flush();
            answer.setText("");
         }
         catch(NullPointerException ioe)
         {
            System.err.println("Error, you must connect to the server before sending a message");
         } 
      }  
   
      public void actionPerformed(ActionEvent ae)
      {     
         if(ae.getActionCommand().equals("Send"))
         {
            sendTheAnswer();
         }
         else if(ae.getActionCommand().equals("Connect"))
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

                  if(msg.contains("Question: "))
                  {
                     header.setText(msg);
                     header.setFont(new Font("Arial", Font.BOLD, 18));
                  }
                  else
                  {
                     System.out.println(msg);
                  }
               }
            }
            catch(IOException ioe)
            {
            }
         }
      }
   }
}










