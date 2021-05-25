package mldigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class GUI  {
    private int i;
    private JButton button;
    private JButton button2;
    private JLabel label;
    private ScribblePane scribblePane;
    private JPanel contentPane;
    private BufferedImage bufferImage;
    private MultiLayerNetwork model;
   
    

    public GUI(){
    	
    	try {
    	
	        /** initializes JFrame and sets attributes */ 
		JFrame frame = new JFrame();
	
	        frame.setSize(600,600);
	        frame.setResizable(false);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setTitle("Hand-Written Digit Recognition: numbers 0-9");
		
	        //???
	        File path = new File("modelSave\\savedModel.zip");
	        model = ModelSerializer.restoreMultiLayerNetwork(path);
	
	        /** initializes JPanels on JFrame */
		JPanel panel = new JPanel();
	        JPanel panel2 = new JPanel();
	        
		/** initliazes and centers label that outputs predicted value */
		label = new JLabel("PREDICTED VALUE HERE",SwingConstants.CENTER);
	        
		/** increases fontsize */
		Font labelFont = label.getFont();
	        label.setFont(new Font(labelFont.getName(), Font.PLAIN, 15));
		
		
		/** intializes buttons for classifying and erasing the drawn images */
	        button = new JButton("Classify Image");
	        button2 = new JButton("Erase Image");
	        
		/** adds listeners to buttons */
	        button.addActionListener(new ButtonListeners());
	        button2.addActionListener(new ButtonListeners());

	        /** initalizes scribblePane JPanel, sets border */
	        scribblePane = new ScribblePane();
	        scribblePane.setBorder(new BevelBorder(BevelBorder.LOWERED);
	
		/** initializes GridBagLayout */
	        GridBagLayout layout = new GridBagLayout();
		
		
	        /** sets borders to JPanels on JFrame */
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
	        panel2.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
	
	        /** sets panel's layout as GridBagLayout */
		panel.setLayout(layout);
		
		/** adds label to panel2 with BorderLayout */
	        panel2.setLayout(new BorderLayout());
	        panel2.add(label, BorderLayout.SOUTH);
		
		
	        /** intializes constants for the GridBagLayout */
		GridBagConstraints gbc = new GridBagConstraints();
	        
		/** fills horizontally if display area is larger than requested size */		       
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		/** sets scribblePane to row 0, column 0 */
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	       
		panel.add(scribblePane, gbc);
		
		
	        /** sets panel2 to row 1, column 0 */
	        gbc.gridx = 1;
	        gbc.gridy = 0;
	        
		panel.add(panel2,gbc);
	
		
		/** sets button to row 0, columumn 1 */
	        gbc.gridx = 0;
	        gbc.gridy = 2;
	        
	        panel.add(button, gbc);
	
		/** sets button2 to row 1, column 1 */
	        gbc.gridx = 1;
	        gbc.gridy = 1;
	
	        panel.add(button2, gbc);
	
	
	        /** adds panel to the JFrame and makes it visible */
		
	        frame.add(panel);
	        frame.setVisible(true);
	
	

    	}
    	
    	catch (Exception e) {
    		e.printStackTrace();
    	}


    }

	/** defines actionListeners for the buttons */
	    class ButtonListeners implements ActionListener{
	
	        public void actionPerformed(ActionEvent e){
	        	
	        	/** actions exectued when "Classify Image" button is clicked */ 
			if(e.getSource() == button) {
	            	try {
		                /** increments i, the number in saved file name */
				i++;
				
				/** saves current image on scribblePane with number i */
		                saveDrawing(i);
				
	                                /** loads the recently saved file */
		        		File imagePath = new File("drawnImages\\save" + i + ".png");
				        
				        /** stores prediction from model into output **/
		        		int outputVal = Classifier.prediction(imagePath, model);
	
		                /** sets label text to predictedvalue from model */
				label.setText("PREDICTED VALUE: " + outputVal);
	            	}
	            	
	            	catch(IOException exception) {
	            		exception.printStackTrace();
	            	}
	        	
	        	}
	        	
			/** actions executed when "Erase" button is clicked */
	        	
			else if(e.getSource() == button2) {
	                       
				/** erases image on scribblePane */
				scribblePane.erase();
				
				/** deletes saved file of the image */
	                        deleteFile("save"+i+".png");
			        
				/** clears the predicted value from the label */
	                        label.setText("");
	        		
	        	}
	        }
         }
 
    /** saves the image on ScribbelPane */	
    public void saveDrawing(int i){

        /** creates local buffer image, sets to image on scribblePane using getter method */
	BufferedImage imagebuf = null;
        imagebuf = scribblePane.getImage();
        
        try {
	    /** saves imagebuf as a file in folder drawnImages with number i */
            ImageIO.write(imagebuf, "png", new File("drawnImages\\save" + i +".png"));
        
	} catch (Exception e) {
           e.printStackTrace();
        }
    }

    /** deletes the file of number i */
    public void deleteFile (String fileName) {
        File myObj = new File("drawnImages\\save" + i + ".png");
        myObj.delete();
    }

    /** returns downscaled buffered image on ScribblePane with a new width and height */	
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        
	/** creates an image object with new dimensions and scaling option of SCALE_SMOOTH */
	Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	
	/** creates new buffered image with the given dimensions */
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	/** draws the image on the new buffered image */
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }


}


/** JPanel on which digit is drawn */

class ScribblePane extends JPanel {

    private BufferedImage bufferImage;
    private Graphics2D g;
    private int s;
    private Color color = Color.white;


    public ScribblePane() {
	    
	/** sets dimensions of JPanel to be sxs  */ 
        s = 200;
        this.setPreferredSize(new Dimension(s, s));
	    
	/** creates a buffered image on top of the JPanel with same dimensions and fills it with black */    
        bufferImage = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) bufferImage.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,s,s);
	    
	    
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                moveto(e.getX(), e.getY()); // Move to click position
                requestFocus(); // Take keyboard focus
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                lineto(e.getX(), e.getY()); // Draw to mouse position
            }
        });

    }

    /* These are the coordinates of the the previous mouse position */
    protected int last_x, last_y;

    /** Remember the specified point */
    public void moveto(int x, int y) {
        last_x = x;
        last_y = y;
    }

    /** Draw from the last point to this point, then remember new point */
    public void lineto(int x, int y) {

	/**updates the scribblePane JPanel */ 
        this.clear();
        
	/** draws the line */
	
	g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(20));
        g.drawLine(last_x, last_y, x, y);
        moveto(x, y);
	    
	/** draws the line on the bufferImage */ 

        g.drawImage(bufferImage,0,0, null);

    }
	
    /** draws the buffered image on scribblePane */
    public void paintComponent(Graphics g){

        g.drawImage(bufferImage,0,0, null);

    }
	
    /** returns the a downscaled version of the buffered image */
    public BufferedImage getImage (){
        BufferedImage scaledImage = GUI.resize(bufferImage, 28,28);
        return scaledImage;


    }

    /** repaints scribblePane JPanel */
    public void clear() {
        repaint();
    }
    
    /** clears the bufferedImage */
    public void erase(){
          
	/** clears the grahics panel */
        g.clearRect(0,0,s,s);
	    
	/** creates new local bufferedImage */
        BufferedImage newBufferImage = new BufferedImage(s, s,
                BufferedImage.TYPE_INT_ARGB);
        
	/** sets it equal to global buffered image reference to clear the screen */
        bufferImage = newBufferImage;
        g = (Graphics2D) bufferImage.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,s,s);
        this.clear();


    }
 
 

}


