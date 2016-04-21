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
 * chatRoomClient that contains
 * a main method, the constructor for the class
 * which builds and instance of the GUI. The buttons
 * of the GUI use actionPerformed to perform
 * commands that connect to a server with a given 
 * IP adress, and send the message to the users textarea
 * and the textarea of the 2nd user connected
 */
public class ChatRoomClient
{
/**
 * @param panel JPanel panel for the server label, jtextfield, and connect button
 * @param centerPanel JPanel panel for jtextfield 
 * @param panelTwo JPanel panel for jtextarea
 * @param panelThree JPanel panel for chat box and send button
 * @param serverLabel JLabel label for the server box
 * @param file JMenu File button
 * @param menuBar JMenuBar sets the menubar
 * @param exit JMenuItem item for exit button
 * @param connect JButton button for connecting to server
 * @param messageArea JTextArea dislpays the area when the chat shows
 * @param sendingText JTextField field area to enter chat
 * @param ipAddress JTextField field area to enter IP
 * @param send JButton sends the chat to the server
 */
   private JPanel panel;
   private JPanel centerPanel;
   private JPanel panelTwo;
   private JPanel panelThree;
   private JLabel serverLabel;
   private JMenu file;
   private JMenuBar menuBar;
   private JMenuItem exit;
   private JButton connect;
   private JTextArea messageArea;
   private JTextField sendingText;
   private JTextField ipAddress;
   private JButton send;   
   
   /**
    * main method that creates a new instance of ChatRoomClient
    * @param args String[]
    */
   public static void main(String [] args)
   {
      ChatRoomClient chatClient = new ChatRoomClient();
   }
   
    /**
    * constructor for ChatRoomClient class. Creates the GUI
    * and adds all the components to their relative components
    */
   public ChatRoomClient()
   {
      //creates frame and instances of the variables
      JFrame frame = new JFrame();
      frame.setSize(600, 700);
      frame.setTitle("Chat Room");
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      //Ovverides the default window closing
      WindowListener exitListener = new WindowAdapter() {  
         public void windowClosing(WindowEvent e) {         
         System.exit(0);
           }};
         frame.addWindowListener(exitListener);

      //Adds a menu bar which contains file -> exit
      menuBar = new JMenuBar();
      file = new JMenu("File");
      menuBar.add(file);
      exit = new JMenuItem("Exit");
      
      file.add(exit);  
      frame.add(menuBar, BorderLayout.NORTH);
      centerPanel = new JPanel();
      
      //Creates and adds the panel that contains the server label, textfield, and connect butotn
      panel = new JPanel();
      serverLabel = new JLabel("Server: ");
      panel.add(serverLabel);
      ipAddress = new JTextField(10);
      panel.add(ipAddress);
      connect = new JButton("Connect");
      
      panel.add(connect);
      centerPanel.add(panel, BorderLayout.NORTH);
      
      //Creates and adds the panel that contains the text area
      panelTwo = new JPanel();
      messageArea = new JTextArea(36, 50);
      panelTwo.add(new JScrollPane(messageArea));
      centerPanel.add(panelTwo, BorderLayout.CENTER);
      frame.add(centerPanel, BorderLayout.CENTER);        
      
      //Creates and adds the panel that will contain the sending textfield and send button
      panelThree = new JPanel();
      sendingText = new JTextField(20);
      panelThree.add(sendingText);
      send = new JButton("Send");
      
      //creates a new instance of class Sender and adds actionlisteners sender
      panelThree.add(send);
      
      Sender sender = new Sender(sendingText, messageArea, ipAddress);
      exit.addActionListener(sender);
      connect.addActionListener(sender);
      send.addActionListener(sender);
      frame.add(panelThree, BorderLayout.SOUTH);
      //displays the frame
      frame.setVisible(true);
   }
}
/**
 * Sender class that uses class ActionListenmer.
 * contains methods that allow the program to take in a message
 * and send it to another uses running the same server
 * @param s Socket variable for Socket
 * @param br BufferedReader reads the text
 * @param pw PrintWriter prints to the jtextarea
 * @param messageArea JTextArea displays chat text
 * @param sendingText JTextField textfield for the entering text
 * @param theAddresses JTextField textfield for entering IP
 */
class Sender implements ActionListener
{
   Socket s;
   BufferedReader br = null;
   PrintWriter pw = null;   
   JTextArea messageArea;
   JTextField sendingText;
   JTextField theAddresses;
   
   /**
    * Sender constructor that takes in 3 variables and sets
    * to later be used in the class
    * @param _sendingText JTextField
    * @param _messageArea JTextArea
    * @param _theAddresses JTextField
    */
   public Sender(JTextField _sendingText, JTextArea _messageArea, JTextField _theAddresses)
   {
      sendingText = _sendingText;
      messageArea = _messageArea;
      theAddresses = _theAddresses;
   }
   /**
    * connect method that prints the host and the IP address
    * and creates instances of the variables that were read
    */
   public void connect()
   {
      try
      {
         //prints user info
	 		System.out.println("Client IP Address: "+InetAddress.getLocalHost());					
         //Creates a new socket that will connect to the server	 
         s = new Socket(theAddresses.getText(), 16000);
         //Creates a bufferedreader and printwriter object
         br = new BufferedReader(new InputStreamReader(s.getInputStream()));
         pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
         //starts the thread
         new Reader().start();        
      }
      //prints error message if host is not found
      catch(UnknownHostException uhe)
      {
         System.err.println("Cannot find the host");
      }
      catch(IOException ioe)
      {
         System.out.println("Lost connection with the server");
         messageArea.setText("The Server has shutdown. Thank you for using Chat Room.");
      }
   }
   /**
    * sendTheText method that prints the message from
    * the printwriter and catches an error if a server
    * has not been connected
    */
   public void sendTheText()
   {
      try
      {
         pw.println(sendingText.getText());
         pw.flush();
         sendingText.setText("");
      }
      catch(NullPointerException ioe)
      {
         System.err.println("Error, you must connect to the server before sending a message");
      } 
   }  
   /**
    * actionPeformed() method that contains the action
    * listeners for all the buttons and holds the code
    * that performs those actions
    */
   public void actionPerformed(ActionEvent ae)
   {
      //connect button
      if(ae.getActionCommand().equals("Connect"))
      {
         connect();
      }
      //send button      
      else if(ae.getActionCommand().equals("Send"))
      {
         sendTheText();
      }
      //exit button with catch exceptions
      else if(ae.getActionCommand().equals("Exit"))
      {
         pw.println("CLOSE");
         pw.flush();

         System.exit(0);
      }
   }
   /**
    * class Reader which uses class Thread read
    * in message and print the text to the jtextarea
    * @param msg String string that holds the text for the message
    */
   class Reader extends Thread
   {      
      String msg;
      
      public void run()
      {  
         try
         {
            //while this is true, print the text to the jtextarea
            while(true)
            {
               msg = br.readLine();
               if(msg == null)
               {
                  msg = ("\nThe Server has shutdown. Thank you for using Chat Room.\n");
                  messageArea.append(msg + "\n");
                  break;
               }
               messageArea.append(msg + "\n");
            }
         }
         catch(IOException ioe)
         {
         }
      }
   }
}
