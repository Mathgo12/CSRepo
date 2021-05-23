package mldigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GUI  {
    private static int i;
    private static  JButton button;
    private static JButton button2;
    private static JLabel label;
    private static JLabel label2;
    private static ScribblePane scribblePane;
    private static JPanel contentPane;
    //private Bitmap  mBitmap;
    BufferedImage bufferImage;

    public static void main(String[] args){

        new GUI();
        
    }




    public GUI(){
        JFrame frame = new JFrame();

        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout layout = new GridBagLayout();


        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        label = new JLabel("number");
        label2 = new JLabel();
        contentPane = new JPanel();
        contentPane.add(label2);
        button = new JButton("Classify Image");
        button2 = new JButton("Erase Image");

        button.addActionListener(new ButtonListeners());
        button2.addActionListener(new ButtonListeners());

        contentPane.setLayout(new BorderLayout());
        scribblePane = new ScribblePane();
        scribblePane.setBorder(new BevelBorder(BevelBorder.LOWERED));
       // scribblePane.setBackground(Color.black);

        contentPane.add(scribblePane, BorderLayout.CENTER);


        // Create a menubar and add it to this window. Note that JFrame
        // handles menus specially and has a special method for adding them
        // outside of the content pane.
        JMenuBar menubar = new JMenuBar(); // Create a menubar
        //frame.setJMenuBar(menubar); // Display it in the JFrame

        // Create menus and add to the menubar
        JMenu filemenu = new JMenu("File");
        menubar.add(filemenu);
        JToolBar palette = new JToolBar();
      
        palette.setOrientation(SwingConstants.VERTICAL);
       // contentPane.add(palette, BorderLayout.WEST);

        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));



        panel.setLayout(layout);
        panel2.setLayout(new BorderLayout());
        panel2.add(label, BorderLayout.WEST);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(contentPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(panel2,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        //gbc.gridwidth = 2;
        panel.add(button, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;

        panel.add(button2, gbc);

        
        frame.add(panel);
        frame.setTitle("GUI");
        frame.pack();
        frame.setVisible(true);


    }


    //Start Listeners

    class ButtonListeners implements ActionListener{

            public void actionPerformed(ActionEvent e){
            	
            	if(e.getSource() == button) {
	            	try {
		                System.out.println("save");
		                saveDrawing(i);
		                
		                File path = new File("C:\\Users\\prsnb\\MnistData\\Save\\savedModel.zip");
		        		MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(path);
		        		File imagePath = new File("C:\\eclpise\\eclipse-workspace\\ml\\drawnImages\\save" + i + ".png");
		        		int outputVal = Classifier.prediction(imagePath, model);
		              
		                
		                i++;
		                label.setText("Predicted Value: " + outputVal);
	                
	            	}
	            	
	            	catch(IOException exception) {
	            		exception.printStackTrace();
	            	}
            	
            	}
            	
            	else if(e.getSource() == button2) {
            		
            		System.out.println("erase");
                    scribblePane.erase();
                    deleteFile("save"+i+".png");
                    label.setText("");
            		
            	}
            }


        
        

    }
    //End Listeners

    public static void saveDrawing(int i) {

            //throws AWTException {
        BufferedImage imagebuf = null;

        imagebuf = scribblePane.getImage();
        Graphics2D graphics2D = imagebuf.createGraphics();
        scribblePane.paint(graphics2D);
        try {
            ImageIO.write(imagebuf, "png", new File("drawnImages\\save" + i +".png"));
            System.out.println("image saved");
        } catch (Exception e) {
           
            System.out.println("error");
        }
    }

    public static void deleteFile (String fileName) {
            File myObj = new File("C:\\eclpise\\eclipse-workspace\\ml\\drawnImages\\save"+i+".png");
            if (myObj.delete()) {
                System.out.println("Deleted the file: " + myObj.getName());
            } else {
                System.out.println("Failed to delete the file.");
            }
        }



    /** This inner class defines the "clear" action that clears the scribble */


    /** This inner class defines the "quit" action to quit the program */


    /**
     * This inner class defines an Action that sets the current drawing color of
     * the ScribblePane2 component. Note that actions of this type have icons
     * rather than labels
     */
}

class ScribblePane extends JPanel {

    BufferedImage bufferImage;
    Graphics2D g;
    public ScribblePane() {
        // Give the component a preferred size
        setPreferredSize(new Dimension(200, 200));
        bufferImage = new BufferedImage(200, 200,
                BufferedImage.TYPE_INT_ARGB);

        g = (Graphics2D) bufferImage.getGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0,200,200);

        //this.paint(g);
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                moveto(e.getX(), e.getY());
                requestFocus();
            }
        });   

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                lineto(e.getX(), e.getY());
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


        //Graphics g = getGraphics();
        //BufferedImage newBufferedImage = new BufferedImage(200,200,
              // BufferedImage.TYPE_INT_ARGB);

        // Get the object to draw with
        this.clear();
        g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(18));// Tell it what color to use
        g.drawLine(last_x, last_y, x, y); // Tell it what to draw
        moveto(x, y);

        g.drawImage(bufferImage,0,0, null);

        //this.paint(g);
        //g.drawImage(bufferImage, x,y,null);
        //bufferImage = newBufferedImage;// Save the current point

    }
    public void paintComponent(Graphics g){

        g.drawImage(bufferImage,0,0, null);

    }

    public BufferedImage getImage (){
         return bufferImage;

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
    public void erase(){

        g.clearRect(0,0,200,200);
        BufferedImage newBufferImage = new BufferedImage(200, 200,
                BufferedImage.TYPE_INT_ARGB);
        bufferImage = newBufferImage;
        g = (Graphics2D) bufferImage.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,200,200);


    }

    /** This field holds the current drawing color property */
    Color color = Color.white;

    /** This is the property "setter" method for the color property */
    public void setColor(Color color) {
        this.color = color;
    }

    /** This is the property "getter" method for the color property */
    public Color getColor() {
        return color;
    }

}

