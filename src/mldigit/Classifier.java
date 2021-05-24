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

	private static String path = "C:\\Users\\prsnb\\MnistData\\mnist_png\\";
	private static int batches = 10;
	private static int epochs = 1;
	private static MultiLayerNetwork network;
	private static int randomSeed = 56543219;
	
	private static DataSetIterator dataProcessor(String addedPath, int numSamples){
	    try {
			ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
		    RecordReader recordReader = new ImageRecordReader(28,28, labelMaker); //28x28 RGB images
			recordReader.initialize(new FileSplit(new File(path + addedPath)));
			
			DataSetIterator tempDataset = new RecordReaderDataSetIterator.Builder(recordReader, numSamples)
		    		.classification(1,10) //10 outcomes
		    		.preProcessor(new ImagePreProcessingScaler())
		    		.build();
		    
		    DataSet train = tempDataset.next();
		    INDArray inputs = train.getFeatures().reshape(new int[]{numSamples, 784}); //28 * 28 = 784
		    INDArray outputs = train.getLabels();
		    
		    DataSet dataset = new DataSet(inputs, outputs);
		    dataset.shuffle();
		    DataSetIterator dataSetIterator = new ListDataSetIterator<DataSet>(dataset.asList(), batches);
		    
			return dataSetIterator;
			
		} catch (Exception e) {		
			e.printStackTrace();
		}
	    return null;
		
    } 
	
	public static void trainTestModel(DataSetIterator trainIterator, DataSetIterator testIterator) {
		
	    
        System.out.print("Build Model...");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()   //Building Neural Network Model
                .seed(randomSeed)
                .updater(new Nesterovs(0.006, 0.9)) //0.006 is the learning rate, 0.9 is Nesterov's momentum 
                //.l2(0.02) //L2 Regularization plus lambda (regularization coefficient)
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(784)
                        .nOut(1000) //1000 nodes in hidden layer
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.NORMAL)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(1000)
                        .nOut(10)
                        .activation(Activation.SOFTMAX) //turns neural network outputs to probabilities that the input data is a 0,1,2, etc.
                        .weightInit(WeightInit.NORMAL)
                        .build())
                .build();

        network = new MultiLayerNetwork(conf);
        network.init();
        //Print score every 500 iterations
        System.out.print("Train Model...");
        
        //evaluate model
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
		
        NativeImageLoader loader = new NativeImageLoader(28, 28, 1);
 
        INDArray image = loader.asMatrix(savedImagePath).reshape(new int[]{1, 784});
        DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
        scalar.transform(image);
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
		
        return bestValue;
        
		
	}
	
	public static void main(String[] args) throws IOException{
		
			DataSetIterator trainIterator = dataProcessor("training", 60000);
	        
	        DataSetIterator testIterator = dataProcessor("testing", 10000);
	       
	        trainTestModel(trainIterator, testIterator);
	        
	        //Export model as file
	        File ministModelPath = new File("C:\\Users\\prsnb\\MnistData\\Save\\savedModel.zip");
	        ModelSerializer.writeModel(network, ministModelPath, false);
	        
	       
	      
	}

}



































































































