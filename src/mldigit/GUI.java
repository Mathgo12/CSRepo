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
    	
	        JFrame frame = new JFrame();
	
	        frame.setSize(600,600);
	        frame.setResizable(false);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setTitle("Hand-Written Digit Recognition: numbers 0-9");
	  
	        File path = new File("modelSave\\savedModel.zip");
	        model = ModelSerializer.restoreMultiLayerNetwork(path);
	
	        JPanel panel = new JPanel();
	        JPanel panel2 = new JPanel();
	        label = new JLabel("PREDICTED VALUE HERE",SwingConstants.CENTER);
	        Font labelFont = label.getFont();
	        label.setFont(new Font(labelFont.getName(), Font.PLAIN, 15));
	
	        //contentPane = new JPanel();
	        button = new JButton("Classify Image");
	        button2 = new JButton("Erase Image");
	        button.addActionListener(new ButtonListeners());
	        button2.addActionListener(new ButtonListeners());
	
	       // contentPane.setLayout(new BorderLayout());
	        scribblePane = new ScribblePane();
	        scribblePane.setBorder(new BevelBorder(BevelBorder.LOWERED));
	        //contentPane.add(scribblePane, BorderLayout.CENTER);
	
	        GridBagLayout layout = new GridBagLayout();
	        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
	        panel2.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
	
	        panel.setLayout(layout);
	        panel2.setLayout(new BorderLayout());
	        panel2.add(label, BorderLayout.SOUTH);
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        panel.add(scribblePane, gbc);
	
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
	        frame.setVisible(true);
	
	

    	}
    	
    	catch (Exception e) {
    		e.printStackTrace();
    	}


    }

	    class ButtonListeners implements ActionListener{
	
	        public void actionPerformed(ActionEvent e){
	        	
	        	if(e.getSource() == button) {
	            	try {
		                i++;
		                saveDrawing(i);
	
		        		File imagePath = new File("drawnImages\\save" + i + ".png");
		        		int outputVal = Classifier.prediction(imagePath, model);
	
		                label.setText("PREDICTED VALUE: " + outputVal);
	            	}
	            	
	            	catch(IOException exception) {
	            		exception.printStackTrace();
	            	}
	        	
	        	}
	        	
	        	else if(e.getSource() == button2) {
	                scribblePane.erase();
	                deleteFile("save"+i+".png");
	                label.setText("");
	        		
	        	}
	        }
         }

    public void saveDrawing(int i){

        BufferedImage imagebuf = null;
        imagebuf = scribblePane.getImage();
        
        try {
            ImageIO.write(imagebuf, "png", new File("drawnImages\\save" + i +".png"));
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public void deleteFile (String fileName) {
        File myObj = new File("drawnImages\\save" + i + ".png");
        myObj.delete();
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        
	Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }


}



class ScribblePane extends JPanel {

    private BufferedImage bufferImage;
    private Graphics2D g;
    private int s;
    private Color color = Color.white;


    public ScribblePane() {
        s = 200;
        this.setPreferredSize(new Dimension(s, s));
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

    /** These are the coordinates of the the previous mouse position */
    protected int last_x, last_y;

    /** Remember the specified point */
    public void moveto(int x, int y) {
        last_x = x;
        last_y = y;
    }

    /** Draw from the last point to this point, then remember new point */
    public void lineto(int x, int y) {

        this.clear();
        g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(20));
        g.drawLine(last_x, last_y, x, y);
        moveto(x, y);

        g.drawImage(bufferImage,0,0, null);

    }
    
    public void paintComponent(Graphics g){

        g.drawImage(bufferImage,0,0, null);

    }

    public BufferedImage getImage (){
        BufferedImage scaledImage = GUI.resize(bufferImage, 28,28);
        return scaledImage;


    }

    public void clear() {
        repaint();
    }
    

    public void erase(){

        g.clearRect(0,0,s,s);
        BufferedImage newBufferImage = new BufferedImage(s, s,
                BufferedImage.TYPE_INT_ARGB);
        bufferImage = newBufferImage;
        g = (Graphics2D) bufferImage.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,s,s);
        this.clear();


    }
 
 

}


