package mldigit;

import java.io.*;

import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

public class Main {
	
	public static void trainClassifier()
	{
		try {
			/**train data for fitting the model. Here, the method
			 * call is used to make the data in the right format. The "training" 
			 * refers to a file path where the training images are located, and 
			 * 60000 is the number of training images. 
			 * 
			 * The testIterator is for evaluating the model with test data 
			 * (for finding accuracy and other evaluation metrics). The method 
			 * call allows to process the data in the right format Similar to 
			 * before, 10000 refers to the number of test images.**/
			
			DataSetIterator trainIterator = Classifier.dataProcessor("training", 60000);
	        
	        DataSetIterator testIterator = Classifier.dataProcessor("testing", 10000);
	       
	        Classifier.trainTestModel(trainIterator, testIterator);
	        
	        //Export model as file
	        File ministModelPath = new File("modelSave\\savedModel.zip");
	        ModelSerializer.writeModel(Classifier.getModel(), ministModelPath, false);
	        
	        
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args){
		
		//trainClassifier();
		
		new GUI();
		
	}

	
}

































 
