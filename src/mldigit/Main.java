package mldigit;

import java.io.*;

import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

public class Main {
	
	public static void trainClassifier()
	{
		try {
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
		
		trainClassifier();
		
		new GUI();
		
	}

	
}

































 
