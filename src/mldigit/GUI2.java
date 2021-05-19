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



public class GUI2  {
    
    private static  JButton button;
    private static JButton button2;
    private ScribblePane2 scribblePane;
    
    public static void main(String[] args){
    
        new GUI2();
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
        
        
        Container contentPane = frame.getContentPane();

        // Specify a layout manager for the content pane
        contentPane.setLayout(new BorderLayout());
    
        // Create the main scribble pane component, give it a border, and
        // a background color, and add it to the content pane
        scribblePane = new ScribblePane2();
        scribblePane.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scribblePane.setBackground(Color.white);
        contentPane.add(scribblePane, BorderLayout.CENTER);
    
        // Create a menubar and add it to this window. Note that JFrame
        // handles menus specially and has a special method for adding them
        // outside of the content pane.
        JMenuBar menubar = new JMenuBar(); // Create a menubar
        frame.setJMenuBar(menubar); // Display it in the JFrame
    
        // Create menus and add to the menubar
        JMenu filemenu = new JMenu("File");
        JMenu colormenu = new JMenu("Color");
        menubar.add(filemenu);
        menubar.add(colormenu);
  
        frame.setTitle("GUI");
        frame.pack();
        frame.setVisible(true);
        
        
    
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
    class ClearAction extends AbstractAction {
    public ClearAction() {
      super("Clear"); // Specify the name of the action
    }

    public void actionPerformed(ActionEvent e) {
      scribblePane.clear();
    }
  }

  /** This inner class defines the "quit" action to quit the program */
  //class QuitAction extends AbstractAction {
   // public QuitAction() {
     // super("Quit");
    }

    //public void actionPerformed(ActionEvent e) {
      // Use JOptionPane to confirm that the user really wants to quit
     // int response = JOptionPane.showConfirmDialog(Scribble.this,
//          "Really Quit?");
     // if (response == JOptionPane.YES_OPTION)
        //System.exit(0);
   // }
 //}
//}
  /**
   * This inner class defines an Action that sets the current drawing color of
   * the ScribblePane2 component. Note that actions of this type have icons
   * rather than labels
   */
  class ColorAction extends AbstractAction {
    Color color;

    public ColorAction(Color color) {
      this.color = color;
      putValue(Action.SMALL_ICON, new ColorIcon(color)); // specify icon
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Xodjs::");
      //scribblePane.setColor(color); // Set current drawing color
    }
    
    
}
/**
 * A simple JPanel subclass that uses event listeners to allow the user to
 * scribble with the mouse. Note that scribbles are not saved or redrawn.
 */

class ScribblePane2 extends JPanel {
  public ScribblePane2() {
    // Give the component a preferred size
    setPreferredSize(new Dimension(450, 200));

    // Register a mouse event handler defined as an inner class
    // Note the call to requestFocus(). This is required in order for
    // the component to receive key events.
    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        moveto(e.getX(), e.getY()); // Move to click position
        requestFocus(); // Take keyboard focus
      }
    });

    // Register a mouse motion event handler defined as an inner class
    // By subclassing MouseMotionAdapter rather than implementing
    // MouseMotionListener, we only override the method we're interested
    // in and inherit default (empty) implementations of the other methods.
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        lineto(e.getX(), e.getY()); // Draw to mouse position
      }
    });

    // Add a keyboard event handler to clear the screen on key 'C'
    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C)
          clear();
      }
    });
  }

  /** These are the coordinates of the the previous mouse position */
  protected int last_x, last_y;

  /** Remember the specified point */
  public void moveto(int x, int y) {
    last_x = x;
    last_y = y;
  }

  /** Draw from the last point to this point, then remember new point */
  public void lineto(int x, int y) {
    Graphics g = getGraphics(); // Get the object to draw with
    g.setColor(color); // Tell it what color to use
    g.drawLine(last_x, last_y, x, y); // Tell it what to draw
    moveto(x, y); // Save the current point
  }

  /**
   * Clear the drawing area, using the component background color. This method
   * works by requesting that the component be redrawn. Since this component
   * does not have a paintComponent() method, nothing will be drawn. However,
   * other parts of the component, such as borders or sub-components will be
   * drawn correctly.
   */
  public void clear() {
    repaint();
  }

  /** This field holds the current drawing color property */
  Color color = Color.black;

  /** This is the property "setter" method for the color property */
  public void setColor(Color color) {
    this.color = color;
  }

  /** This is the property "getter" method for the color property */
  public Color getColor() {
    return color;
  }

}

