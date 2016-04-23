//imports the necesary classes for the GUI program
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class MainGUI
{
   private JPanel northPanel;
   private JLabel header;
   
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
   
   public MainGUI()
   {
      JFrame frame = new JFrame();
      frame.setSize(800, 800);
      frame.setTitle("Family Feud");
      frame.setLocationRelativeTo(null);
      
      northPanel = new JPanel();
      header = new JLabel("FAMILY FEUD");
      header.setFont(new Font("Arial", Font.BOLD, 75));
      header.setForeground(Color.YELLOW);
      northPanel.add(header);  
      northPanel.setBackground(Color.BLUE);
      frame.add(northPanel, BorderLayout.NORTH);     
      
      centerPanel = new JPanel(new GridLayout(4,2));
      answerOne = new JButton("1");
      centerPanel.add(answerOne);
      answerTwo = new JButton("2");
      centerPanel.add(answerTwo);
      answerThree = new JButton("3");
      centerPanel.add(answerThree);
      answerFour = new JButton("4");
      centerPanel.add(answerFour);
      answerFive = new JButton("5");
      centerPanel.add(answerFive);
      answerSix = new JButton("6");
      centerPanel.add(answerSix);
      answerSeven = new JButton("7");
      centerPanel.add(answerSeven);
      answerEight = new JButton("8");
      centerPanel.add(answerEight);
      frame.add(centerPanel, BorderLayout.CENTER);
      
      southPanel = new JPanel();
      answer = new JTextField(20);
      southPanel.add(answer);
      sendAnswer = new JButton("Send");
      southPanel.add(sendAnswer);
      frame.add(southPanel, BorderLayout.SOUTH);
      
      frame.setVisible(true);
   }
   
   public static void main(String [] args)
   {
      MainGUI game = new MainGUI();
   }
}