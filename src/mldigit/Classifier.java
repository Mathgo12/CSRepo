package mldigit;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;

import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.* ; //{DenseLayer, GravesLSTM, OutputLayer, RnnOutputLayer};
import org.deeplearning4j.nn.conf.* ;//{ComputationGraphConfiguration, MultiLayerConfiguration, NeuralNetConfiguration, Updater};
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.AdaGrad;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.util.*;

public class Classifier {

	private static String path = "C:\\Users\\prsnb\\MnistData\\mnist_png\\";
	private static int batches = 10;
	private static int epochs = 5;
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
                .updater(new Nesterovs(0.006, 0.9))
                .l2(1e-4).list()
                .layer(new DenseLayer.Builder()
                        .nIn(784)
                        .nOut(1000)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(1000)
                        .nOut(10)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER).build())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        network.setListeners(new ScoreIterationListener(500));  //Print score every 500 iterations
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
	
	public static void main(String[] args) throws IOException{
		
		    
			DataSetIterator trainIterator = dataProcessor("training", 60000);
	        
	        DataSetIterator testIterator = dataProcessor("testing", 10000);
	       
	        trainTestModel(trainIterator, testIterator);
	        
	        //Export model as file
	        File ministModelPath = new File("C:\\Users\\prsnb\\MnistData\\savedModel.zip");
	        ModelSerializer.writeModel(network, ministModelPath, true);
	        
	       
	      
	}

}



































































































