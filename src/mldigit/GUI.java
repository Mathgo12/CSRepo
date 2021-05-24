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
    private static int i;
    private static  JButton button;
    private static JButton button2;
    private static JLabel label;
    private static ScribblePane scribblePane;
    private static JPanel contentPane;




    public GUI(){
        
	JFrame frame = new JFrame();

        frame.setSize(600,600);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
	    
	GridBagLayout layout = new GridBagLayout();


        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        
	label = new JLabel("",SwingConstants.CENTER);
        Font labelFont = label.getFont();
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, 15));

        JLabel label2 = new JLabel("PREDICTED NUMBER",SwingConstants.CENTER);
        label2.setFont(new Font(labelFont.getName(), Font.PLAIN, 12));
        
	contentPane = new JPanel();
        contentPane.add(label2);
        
	button = new JButton("Classify Image");
        button2 = new JButton("Erase Image");
        button.addActionListener(new ButtonListeners());
        button2.addActionListener(new ButtonListeners());

        contentPane.setLayout(new BorderLayout());
        scribblePane = new ScribblePane();
        scribblePane.setBorder(new BevelBorder(BevelBorder.LOWERED));
    

        contentPane.add(scribblePane, BorderLayout.CENTER);
	    
	  


        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel2.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));



        panel.setLayout(layout);
        
	panel2.setLayout(new BorderLayout());
        panel2.add(label, BorderLayout.SOUTH);
        panel2.add(label2, BorderLayout.NORTH);
        
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
        
        panel.add(button, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;

        panel.add(button2, gbc);



        frame.add(panel);
        frame.setTitle("HandDrawn Digit Recognition");
        frame.setVisible(true);


	    

    }

    class ButtonListeners implements ActionListener{

        public void actionPerformed(ActionEvent e){
        	
        	if(e.getSource() == button) {
            	try {
	                System.out.println("save");
	                i++;
	                saveDrawing(i);
	                
	                File path = new File("modelSave\\savedModel.zip");
	        		MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(path);
	        		File imagePath = new File("drawnImages\\save" + i + ".png");
	        		int outputVal = Classifier.prediction(imagePath, model);
	              
	                
	                
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

    public static void saveDrawing(int i){

        BufferedImage imagebuf = null;
        imagebuf = scribblePane.getImage();
        
        try {
            ImageIO.write(imagebuf, "png", new File("drawnImages\\save" + i +".png"));
            System.out.println("image saved");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error");
        }
    }

    public static void deleteFile (String fileName) {
        File myObj = new File("drawnImages\\save" + i + ".png");
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
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
        ((Graphics2D) g).setStroke(new BasicStroke(18));
        g.drawLine(last_x, last_y, x, y);
        moveto(x, y);

        g.drawImage(bufferImage,0,0, null);

    }
    
    public void paintComponent(Graphics g){

        g.drawImage(bufferImage,0,0, null);

    }

    public BufferedImage getImage (){
        BufferedImage scaledImage = GUI2.resize(bufferImage, 28,28);
        return scaledImage;


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

        g.clearRect(0,0,s,s);
        BufferedImage newBufferImage = new BufferedImage(s, s,
                BufferedImage.TYPE_INT_ARGB);
        bufferImage = newBufferImage;
        g = (Graphics2D) bufferImage.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,s,s);
        this.clear();


    }
 
    /** This is the property "setter" method for the color property */
    public void setColor(Color color) {
        this.color = color;
    }

    /** This is the property "getter" method for the color property */
    public Color getColor() {
        return color;
    }

}


