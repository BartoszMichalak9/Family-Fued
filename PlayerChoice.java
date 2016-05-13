/*
 * @authors Tanner Glantz, Brett Phillips, Rosalee Hacker, and Alejandro Garzon
 * @version 05/14/2016
 * Description: The PlayerChoice class creates a window for the user to enter their name before starting the game.
 * Course: ISTE-121
 */

//imports the necesary classes for the GUI program
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/*
 * The PlayerChoice class creates a GUI for the users to enter their names into the game. After the user enters their name,
 *    it creates a MainGUI object in order to start the game.
 */
public class PlayerChoice extends JFrame implements ActionListener
{
   /*
    * @param JFrame frame Creates a JFrame object that will show the window that contains all of the panels.
    * @param JLabel playerOneLabel Creates a JLabel object that that will let the user know what they need to type in the textbox.
    * @param JTextField playerOneText Creates a JTextField object where the user will be able to enter his or her name as a player of the game.
    * @param JButton ok Creates a JButton object that the user will press to add the players to the game and also start the game.
    * @param JPanel panel Creates a JPanel object that will be in GridLayout and contain the JLabel and JTextField for the player input.
    * @param JPanel panelTwo Creates a JPanel object that will be in FlowLayout and contain the JButton ok.
    * @param String playerOneEntered Gets assigned the text that was put in the textField for the player.
    */    
   private JFrame frame;
   private JLabel playerOneLabel = null;
   private JTextField playerOneText = null;
   private JButton ok = null;
   private JPanel panel = null;
   private JPanel panelTwo = null;
   private String playerOneEntered;
   
   //PlayerChoice's main method
   public static void main(String [] args)
   {
      //Creates a new PlayerChoice object
      PlayerChoice pC = new PlayerChoice();
   } 
     
   /*
    * The default constructor creates the GUI for the PlayerChoice class. It will create a frame that will
    * have two panels added to it. These panels will contain JLabel and TextField objects that will allow
    * the user to input their name and a JButton that will allow the user 
    * accept the player, enter them, and start the game. 
    */  
   public PlayerChoice()
   {
      //Creates a JFrame object called frame
      frame = new JFrame();
      //Sets the title of the frame
      frame.setTitle("Family Feud");
      //Allows the user to close hte window and terminate the program by clicking the red X
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //Sets the size of the frame
      frame.setSize(600, 200);
      
      //Creates a new JPanel object called panel that is set to GridLayout
      panel = new JPanel(new GridLayout(2, 1));
      
      //Creates a new JLabel object called playerOneLabel that takes in a String
      playerOneLabel = new JLabel("Player Name:");
      //Adds playerOneLabel to panel
      panel.add(playerOneLabel);
      //Creates a new JTextField object called playerOneText where the user will be able to enter his or her own name
      playerOneText = new JTextField();
      //Adds playerOneText to panel
      panel.add(playerOneText);
            
      //Creates a second JPanel called panelTwo which is defaultly FlowLayout
      panelTwo = new JPanel();
      
      //Creates a new JButton object called ok that takes in a String
      ok = new JButton("OK");
      //Adds the JButton ok to panelTwo
      panelTwo.add(ok);
      //Adds an action to the button
      ok.addActionListener(this);
      
      //Adds panelTwo to the frame and sets it to BorderLayout to the south of the frame
      frame.add(panelTwo, BorderLayout.SOUTH);
      //Adds panel to the frame and sets it to BorderLayout to the north of the frame
      frame.add(panel, BorderLayout.CENTER);
      //Sets the location of the frame to be in the middle of the page
      frame.setLocationRelativeTo(null);
      //Sets the frame to be visible by the user
      frame.setVisible(true);
   } 
   
   /*
    * The actionPerformed() method listens for a call to the JButton Action Listener, then in return, performs
    *       a certain action.
    * @param ActionEvent ae Allows the action command to be performed 
    */   
   public void actionPerformed(ActionEvent ae)
   {
      //If the command equals OK, then it will perform the following action 
      if(ae.getActionCommand().equals("OK"));
      {
         //Gets the text that was enterened in the TextField and assigns it to the String playerOneEntered
         playerOneEntered = playerOneText.getText();
         //Creates a new MainGUIClient object and gives it a header of "player name: " and also takes in the playerOneEntered variable
         new MainGUIClient("Player Name: "+playerOneEntered);
         //Sets the frame to false
         frame.setVisible(false);
      }
   }   
}