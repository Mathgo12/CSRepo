package ml.ml;

import java.awt.image.BufferedImage;
import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.datavec.image.loader.ImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;




public class Main {

	public static void getDocuments(File path) throws IOException
	{
		
		FileInputStream input = new FileInputStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = reader.readLine();
		
		while(line != null)
		{
			System.out.println(line);
			line = reader.readLine();
		}
		
		
		
	}
	
	
	public static void main(String[] args) throws IOException 
	{
		//Model Testing
		File path = new File("C:\\Users\\prsnb\\MnistData\\Save\\trained_mnist_model.zip");
		MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(path);
		
		File imagePath = new File("C:\\Users\\prsnb\\MnistData\\Save\\test2.png");
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
        	System.out.println(i + ": " + output.getFloat(i));
        	if(output.getFloat(i) > highest) { 
        		highest = output.getFloat(i);
        		bestValue = i;
        	}
        }
		
        System.out.println("Best Prediction: " + bestValue);
	}
	
}

































 
