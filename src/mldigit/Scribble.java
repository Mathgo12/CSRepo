  
/*
 * Copyright (c) 2000 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 2nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book (recommended),
 * visit http://www.davidflanagan.com/javaexamples2.
 */

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

/**
 * This JFrame subclass is a simple "paint" application.
 */
public class Scribble extends JFrame {
  /**
   * The main method instantiates an instance of the class, sets it size, and
   * makes it visible on the screen
   */
  public static void main(String[] args) {
    Scribble scribble = new Scribble();
    scribble.setSize(500, 300);
    scribble.setVisible(true);
  }

  // The scribble application relies on the ScribblePane2 component developed
  // earlier. This field holds the ScribblePane2 instance it uses.
  ScribblePane2 scribblePane;

  /**
   * This constructor creates the GUI for this application.
   */
  public Scribble() {
    super("Scribble"); // Call superclass constructor and set window title

    // Handle window close requests
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    // All content of a JFrame (except for the menubar) goes in the
    // Frame's internal "content pane", not in the frame itself.
    // The same is true for JDialog and similar top-level containers.
    Container contentPane = this.getContentPane();

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
    this.setJMenuBar(menubar); // Display it in the JFrame

    // Create menus and add to the menubar
    JMenu filemenu = new JMenu("File");
    JMenu colormenu = new JMenu("Color");
    menubar.add(filemenu);
    menubar.add(colormenu);

    // Create some Action objects for use in the menus and toolbars.
    // An Action combines a menu title and/or icon with an ActionListener.
    // These Action classes are defined as inner classes below.
    Action clear = new ClearAction();
    Action quit = new QuitAction();
    Action black = new ColorAction(Color.black);
    Action red = new ColorAction(Color.red);
    Action blue = new ColorAction(Color.blue);
    Action select = new SelectColorAction();

    // Populate the menus using Action objects
    filemenu.add(clear);
    filemenu.add(quit);
    colormenu.add(black);
    colormenu.add(red);
    colormenu.add(blue);
    colormenu.add(select);

    // Now create a toolbar, add actions to it, and add it to the
    // top of the frame (where it appears underneath the menubar)
    JToolBar toolbar = new JToolBar();
    toolbar.add(clear);
    toolbar.add(select);
    toolbar.add(quit);
    contentPane.add(toolbar, BorderLayout.NORTH);

    // Create another toolbar for use as a color palette and add to
    // the left side of the window.
    JToolBar palette = new JToolBar();
    palette.add(black);
  
    palette.setOrientation(SwingConstants.VERTICAL);
    contentPane.add(palette, BorderLayout.WEST);
  }

  /** This inner class defines the "clear" action that clears the scribble */
  class ClearAction extends AbstractAction {
    public ClearAction() {
      super("Clear"); // Specify the name of the action
    }

    public void actionPerformed(ActionEvent e) {
      scribblePane.clear();
    }
  }

  /** This inner class defines the "quit" action to quit the program */
  class QuitAction extends AbstractAction {
    public QuitAction() {
      super("Quit");
    }

    public void actionPerformed(ActionEvent e) {
      // Use JOptionPane to confirm that the user really wants to quit
      int response = JOptionPane.showConfirmDialog(Scribble.this,
          "Really Quit?");
      if (response == JOptionPane.YES_OPTION)
        System.exit(0);
    }
  }

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
      scribblePane.setColor(color); // Set current drawing color
    }
  }

  /**
   * This inner class implements Icon to draw a solid 16x16 block of the
   * specified color. Most icons are instances of ImageIcon, but since we're
   * only using solid colors here, it is easier to implement this custom Icon
   * type
   */
  static class ColorIcon implements Icon {
    Color color;

    public ColorIcon(Color color) {
      this.color = color;
    }

    // These two methods specify the size of the icon
    public int getIconHeight() {
      return 16;
    }

    public int getIconWidth() {
      return 16;
    }

    // This method draws the icon
    public void paintIcon(Component c, Graphics g, int x, int y) {
      g.setColor(color);
      g.fillRect(x, y, 16, 16);
    }
  }

  /**
   * This inner class defines an Action that uses JColorChooser to allow the
   * user to select a drawing color
   */
  class SelectColorAction extends AbstractAction {
    public SelectColorAction() {
      super("Select Color...");
    }

    public void actionPerformed(ActionEvent e) {
      Color color = JColorChooser.showDialog(Scribble.this,
          "Select Drawing Color", scribblePane.getColor());
      if (color != null)
        scribblePane.setColor(color);
    }
  }
}

/*
 * Copyright (c) 2000 David Flanagan. All rights reserved. This code is from the
 * book Java Examples in a Nutshell, 2nd Edition. It is provided AS-IS, WITHOUT
 * ANY WARRANTY either expressed or implied. You may study, use, and modify it
 * for any non-commercial purpose. You may distribute it non-commercially as
 * long as you retain this notice. For a commercial use license, or to purchase
 * the book (recommended), visit http://www.davidflanagan.com/javaexamples2.
 */

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