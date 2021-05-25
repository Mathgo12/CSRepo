package mldigit;

import org.deeplearning4j.nn.conf.layers.* ; //{DenseLayer, GravesLSTM, OutputLayer, RnnOutputLayer};

import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.learning.config.Nesterovs;

import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;

import java.io.*;

public class Classifier {
	 															
	private static String path = "mnist_png\\";  //path for loading data
	private static int batches = 10;                    
	private static int epochs = 1;
	private static MultiLayerNetwork network;   //Neural network 
	private static int randomSeed = 56543219;    
	
	public static DataSetIterator dataProcessor(String addedPath, int numSamples){
		
		//An exception may be thrown when loading an image given a path.
	    try {
			ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator(); //takes name of folder and sets it to the label
			
			/** This RecordReader objects converts images into a series of Record objects. 
			 *  Record objects are single vectors. Each element contains a pixel value. **/
		    RecordReader recordReader = new ImageRecordReader(28,28, labelMaker); //28x28 RGB images
			recordReader.initialize(new FileSplit(new File(path + addedPath)));
			
			
			/**A DataSet object holds data. The input features are stored as a matrix, and the output labels are 
			 * stored as a different matrix. Each associate with one another so that each training example (a vector of pixel values)
			 * is linked to a single output (label).
			 * 
			 *  A DataSet object holds a single batch of data. A DataSetIterator allows to iterate across multiple DataSet objects.
			 *  In the first DataSetIterator, we put all the training data into a single DataSet which is why the batch size is 
			 *  numSamples. This way, we can easily make any modifications using this DataSet object.**/
			
			DataSetIterator tempDataset = new RecordReaderDataSetIterator.Builder(recordReader, numSamples)  
		    		.classification(1,10) //We want a classification iterator. 1 refers to the index of the labels (index 0 is all the features) and 10 is the 10 outputs 0-9.
		    		.preProcessor(new ImagePreProcessingScaler()) //Its easier to work with pixel values 0-1 instead of 0-255, so that values are scaled down.
		    		.build();
		    
			/** There's only 1 DataSet object since we used all the training examples for the batch size. **/
		    DataSet data = tempDataset.next();
		    
		    /**An INDArray is a more advanced version of the general java array that has a variety of methods to work with for data manipulation.
		     * The 'inputs' variable includes the features from the DataSet object. The model can only work with 2D data, so the inputs are scaled
		     * to be a 2-D matrix. 784 represents the total number of pixels per image. NumSamples is the total number of images being used.**/
		    
		    INDArray inputs = data.getFeatures().reshape(new int[]{numSamples, 784}); 
		    INDArray outputs = data.getLabels();
		    
		    DataSet dataset = new DataSet(inputs, outputs); //Create new DataSet object
		    
		    /**Randomize the order of each training example and label. We need to do this so that 
		     * the model can train properly without seeing the same type of data multiple times. **/
		    dataset.shuffle(); 
		    
		    //DataSetIterator object which the model can work with, split into more batches
		    DataSetIterator dataSetIterator = new ListDataSetIterator<DataSet>(dataset.asList(), batches); //DataSetIterator object which the model can work with, split into more batches
		    
			return dataSetIterator;
			
		} catch (Exception e) {		
			e.printStackTrace();
		}
	    
	    return null;  
		
    } 
	
	/** Method for both training and testing the model **/
	public static void trainTestModel(DataSetIterator trainIterator, DataSetIterator testIterator) {
		
		System.out.print("Build Model...");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()   //Building Neural Network Model
                .seed(randomSeed) 
                .updater(new Nesterovs(0.006, 0.9)) //0.006 is the learning rate, 0.9 is Nesterov's momentum 
                .l2(0.001) //L2 Regularization plus lambda (regularization coefficient)
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(784)   //Each input image is interpreted as a row of pixel values, and there are 784 pixels
                        .nOut(1000) //1000 nodes in hidden layer
                        .activation(Activation.RELU)  //Activation function (eliminates possible negative pixel values)
                        .weightInit(WeightInit.UNIFORM) //Internal model parameters are randomized uniformly
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD) //The loss function for assessing how well the model is predicting for each training example.
                        .nIn(1000)
                        .nOut(10)
                        .activation(Activation.SOFTMAX) //turns neural network outputs to probabilities that the input data is a 0,1,2, etc.
                        .weightInit(WeightInit.UNIFORM) 
                        .build())
                .build();

        network = new MultiLayerNetwork(conf); //Apply the neural network configuration to the model
        network.init();  //Initialize the model before fitting the training data
        //Print score every 500 iterations
        System.out.print("Train Model...");
        
        //evaluate model for each epoch
        for (int i = 0; i < epochs; i++) {
            network.fit(trainIterator);
            System.out.println("Completed epoch " + i);
            Evaluation eval = network.evaluate(testIterator);
            System.out.println(eval.stats());
            trainIterator.reset();
            testIterator.reset();
        }
		
	}
	
	public static int prediction(File savedImagePath, MultiLayerNetwork model) throws IOException{
		
        NativeImageLoader loader = new NativeImageLoader(28, 28, 1);  //Loads images and allows for obtaining matrices. Larger images are compressed.
 
        INDArray image = loader.asMatrix(savedImagePath).reshape(new int[]{1, 784});  //One image is a single array with 784 pixel values 
        DataNormalization scalar = new ImagePreProcessingScaler();  //Each image pixel is scaled to a number from 0 to 1.
        scalar.transform(image); //applies the scaler
        INDArray output = model.output(image);
        double highest = output.getFloat(0); 
        int bestValue = 0;
        
        /** This code computes the largest value in the output array, which includes decimal values for how confident 
         * the model is for each prediction (0,1,2, ...). The highest value is likely to be associated with the **/
        for(int i = 0; i<output.length(); i++)  // Prediction confidences (all add up to 1). The value closest to 1 is predicted. 
        {
        	if(output.getFloat(i) > highest) { 
        		highest = output.getFloat(i);
        		bestValue = i;
        	}
        }
		
        return bestValue;
        
		
	}
	
	public static MultiLayerNetwork getModel()
	{
		return network;
	}

}



































































































