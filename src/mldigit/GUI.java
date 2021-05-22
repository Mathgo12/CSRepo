package mldigit;

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

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class GUI  {
    private static int i;
    private static  JButton button;
    private static JButton button2;
    private static JLabel label;
    private static ScribblePane2 scribblePane;
    private static JPanel contentPane;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private Paint   mPaint;
    //private Bitmap  mBitmap;
    private Canvas  mCanvas;
    BufferedImage bufferImage;

    public static void main(String[] args){

        new GUI();
        setUpButtonListeners();
        // Scribble scribble = new Scribble();
        //scribble.setSize(500, 300);
        //scribble.setVisible(true);
    }




    public GUI(){
        JFrame frame = new JFrame();

        frame.setSize(300,300);

        GridBagLayout layout = new GridBagLayout();


        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        label = new JLabel("number");
        JLabel label2 = new JLabel("Hi I'm contentPane");
        contentPane = new JPanel();
        contentPane.add(label2);
        button = new JButton("Classify Image");
        button2 = new JButton("Erase Image");


        contentPane.setLayout(new BorderLayout());
        scribblePane = new ScribblePane2();
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
        JMenu colormenu = new JMenu("Color");
        menubar.add(filemenu);
       // menubar.add(colormenu);

        // Create some Action objects for use in the menus and toolbars.
        // An Action combines a menu title and/or icon with an ActionListener.
        // These Action classes are defined as inner classes below.


        Action black = new ColorAction(Color.black);
        Action red = new ColorAction(Color.red);
        Action blue = new ColorAction(Color.blue);


        // Populate the menus using Action objects


       // colormenu.add(black);



        // Now create a toolbar, add actions to it, and add it to the
        // top of the frame (where it appears underneath the menubar)
        JToolBar toolbar = new JToolBar();


       // contentPane.add(toolbar, BorderLayout.NORTH);

        // Create another toolbar for use as a color palette and add to
        // the left side of the window.
        JToolBar palette = new JToolBar();
        palette.add(black);

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




        //panel.setLayout(new BorderLayout());
        //panel.add(button, BorderLayout.WEST);
        //panel.add(button2, BorderLayout.EAST);
        //panel.add(label, BorderLayout.NORTH);
        //panel.add(contentPane, BorderLayout.SOUTH);
        frame.add(panel);



        frame.setTitle("GUI");
        frame.pack();
        frame.setVisible(true);







    }




    public static void setUpButtonListeners(){


        ActionListener buttonListener = new ActionListener(){
            //@Override
            public void actionPerformed(ActionEvent e){
            	
            	try {
                System.out.println("save");
                saveDrawing(i);
                
                int outputVal = 0;
              //Model Testing
        		File path = new File("C:\\Users\\prsnb\\MnistData\\Save\\savedModel.zip");
        		MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(path);
        		
        		File imagePath = new File("C:\\eclpise\\eclipse-workspace\\ml\\save" + i + ".png");
        		//BufferedImage img = ImageIO.read(imagePath);s

                //Use the nativeImageLoader to convert to numerical matrix
                NativeImageLoader loader = new NativeImageLoader(28, 28, 1);

                //put image into INDArray
                INDArray image = loader.asMatrix(imagePath).reshape(new int[]{1, 784});
                //System.out.println(image);
                //values need to be scaled
                DataNormalization scalar = new ImagePreProcessingScaler(0, 1);

                //then call that scalar on the image dataset
                scalar.transform(image);
                
                //pass through neural net and store it in output array
                INDArray output = model.output(image);
                double highest = output.getFloat(0);
                int bestValue = 0;
                
                for(int i = 0; i<output.length(); i++)  // Prediction confidences (all add up to 1). The value closest to 1 is predicted. 
                {
                	
                	if(output.getFloat(i) > highest) { 
                		highest = output.getFloat(i);
                		bestValue = i;
                	}
                }
        		
                System.out.println("Best Prediction: " + bestValue);
                outputVal = bestValue;
                
                i++;
                label.setText("" + outputVal);
                
            	}catch(IOException exception) {
            		exception.printStackTrace();
            	}
            	

            }


        };

        ActionListener buttonListener2 = new ActionListener(){
            //@Override
            public void actionPerformed(ActionEvent e){
                System.out.println("erase");
                scribblePane.erase();
                deleteFile("save"+i+".png");
                label.setText("");

            }


        };
        button.addActionListener(buttonListener);
        button2.addActionListener(buttonListener2);


    }

    public static void saveDrawing(int i) {

            //throws AWTException {
        BufferedImage imagebuf = null;
        //imagebuf = new Robot().createScreenCapture(scribblePane.getBounds());
        //imagebuf = new BufferedImage(scribblePane.WIDTH, scribblePane.HEIGHT,     BufferedImage.TYPE_INT_RGB);

        imagebuf = scribblePane.getImage();
        Graphics2D graphics2D = imagebuf.createGraphics();
        scribblePane.paint(graphics2D);
        try {
            ImageIO.write(imagebuf, "png", new File("save" + i +".png"));
            System.out.println("image saved");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error");
        }
    }

    public static void deleteFile (String fileName) {
            File myObj = new File("/Users/nargisjareen/Downloads/GUIProject/save"+i+".png");
            if (myObj.delete()) {
                System.out.println("Deleted the file: " + myObj.getName());
            } else {
                System.out.println("Failed to delete the file.");
            }
        }

    private void loadImageFromStorage(String path)
    {

        System.out.println("load");
        //use a fileInputStream to read the file in a try / catch block
       // try {
          //  File f=new File(path, "drawn_image.jpg");
         //   Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
          //  ImageView img=(ImageView)findViewById(R.id.outputView);
          //  img.setImageBitmap(b);
       // }
     //   catch (FileNotFoundException e)
       // {
      ///     e.printStackTrace();
      //  }

    }



    /** This inner class defines the "clear" action that clears the scribble */


    /** This inner class defines the "quit" action to quit the program */


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



}

class ScribblePane2 extends JPanel {

    BufferedImage bufferImage;
    Graphics2D g;
    public ScribblePane2() {
        // Give the component a preferred size
        setPreferredSize(new Dimension(200, 200));
        bufferImage = new BufferedImage(200, 200,
                BufferedImage.TYPE_INT_ARGB);

        g = (Graphics2D) bufferImage.getGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0,200,200);

        //this.paint(g);


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

