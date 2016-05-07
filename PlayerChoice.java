//imports the necesary classes for the GUI program
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class PlayerChoice extends JFrame implements ActionListener
{
   private JLabel playerOneLabel = null;
   private JTextField playerOneText = null;
   private JButton ok = null;
   private JPanel panel = null;
   private JPanel panelTwo = null;
   private String playerOneEntered;
   private String playerTwoEntered;
   
   public static void main(String [] args)
   {
      PlayerChoice pC = new PlayerChoice();
   } 
     
   public PlayerChoice()
   {
      JFrame frame = new JFrame();
      frame.setTitle("Family Feud");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(600, 200);
      
      panel = new JPanel(new GridLayout(2, 1));
      
      playerOneLabel = new JLabel("Player Name:");
      panel.add(playerOneLabel);
      playerOneText = new JTextField();
      panel.add(playerOneText);
            
      panelTwo = new JPanel();
      
      ok = new JButton("OK");
      panelTwo.add(ok);
      ok.addActionListener(this);
      
      frame.add(panelTwo, BorderLayout.SOUTH);
      frame.add(panel, BorderLayout.CENTER);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   } 
   
   public void actionPerformed(ActionEvent ae)
   {
      if(ae.getActionCommand().equals("OK"));
      {
         playerOneEntered = playerOneText.getText();
         new MainGUIClient("Player Name:"+playerOneEntered);
      }
   }   
}