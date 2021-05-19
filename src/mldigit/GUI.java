//package mldigit;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;



public class GUI  {
    
    private static  JButton button;
    private static JButton button2;
    //private ScribblePane2 scribblePane;
    
    public static void main(String[] args){
    
        new GUI();
        setUpButtonListeners();
        Scribble scribble = new Scribble();
        scribble.setSize(500, 300);
        scribble.setVisible(true);
    }
    
    
    
    
    public GUI(){
        JFrame frame = new JFrame();
        
        frame.setSize(300,300);
        
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JLabel label = new JLabel("number");
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        
        button = new JButton("Classify Image");
        button2 = new JButton("Erase Image");
     
        panel2.add(button);
        panel2.add(button2);
        panel2.add(label);
        frame.add(panel);
        frame.add(panel2);
        
        
        
        
        
    
    }
    
    public void saveImage(){
        int i =0;
    
    }
    
  
    
    public static void setUpButtonListeners(){
    
        ActionListener buttonListener = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            
               System.out.println("save");
            }
        
        
        };
        
        ActionListener buttonListener2 = new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
              System.out.println("erase");
            
            }
        
        
        };
        button.addActionListener(buttonListener);
        button2.addActionListener(buttonListener2);
    
    
    }
}

