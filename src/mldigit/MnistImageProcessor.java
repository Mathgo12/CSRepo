package mldigit;

//IO, Image, File handling imports
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.Record;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.datavec.image.recordreader.*;
import org.datavec.image.transform.ResizeImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.datavec.image.loader.ImageLoader;
import org.datavec.image.loader.NativeImageLoader;

import java.util.*;

public class MnistImageProcessor
{
	
	public static INDArray convertImageToMatrix(BufferedImage img, int channels) throws IOException
	{		
		ImageLoader loader = new ImageLoader(img.getHeight(), img.getWidth(), channels);
	    INDArray input = loader.asMatrix(img); //.reshape(1, 28, 28, 1);
		
	    return input;
	}
	
	
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		
		//File file = new File();
		ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
	    RecordReader recordReader = new ImageRecordReader(28,28, labelMaker); //28x28 RGB images
	    recordReader.initialize(new FileSplit(new File("C:\\Users\\prsnb\\MnistData\\mnist_png\\training")));
	      
	    
	    DataSetIterator trainIter = new RecordReaderDataSetIterator.Builder(recordReader, 60000)
	    		.classification(1,10)
	    		.preProcessor(new ImagePreProcessingScaler())
	    		.build();
	    
	    DataSet train = trainIter.next();
	    INDArray inputs = train.getFeatures().reshape(new int[]{60000, 784});
	    INDArray outputs = train.getLabels();
	    
	    DataSet dataset = new DataSet(inputs, outputs);
	    dataset.shuffle();
	    DataSetIterator dataSetIterator = new ListDataSetIterator<DataSet>(dataset.asList(), 10);
	    
	    
	    //System.out.println(Arrays.toString(inputs.shape()));
	    
//	    INDArray input = Nd4j.create(new int[]{60000, 784});
//      INDArray output = Nd4j.create(new int[]{60000, 10});
//	    		
//	    int n=0;
//	    while(trainIter.hasNext()) {
//		    DataSet dataset = trainIter.next();
//		    INDArray datasetArr = dataset.getFeatures().reshape(new int[]{1, 784});
//		    INDArray datasetLabels = dataset.getLabel();
//		    input.putRow(n, datasetArr);
//            output.put(n, datasetLabels);
//            
//            n++;
//		    
//	    }
//	    DataSet dataSet = new DataSet(input, output);
//        List<DataSet> listDataSet = dataSet.asList();
//        Collections.shuffle(listDataSet, new Random(System.currentTimeMillis()));
//	    DataSetIterator dsi = new ListDataSetIterator<DataSet>(listDataSet, 32);
//	    
//	    DataSet dataset = dsi.next();
//        System.out.println(Arrays.toString(dataset.getFeatures().shape()));
//	   

	    
	    //INDArray arr = dataset.getFeatures();
	    //System.out.println(Arrays.toString(arr.shape()));
        

	}
	
	
//	public static void testIterator()
//	{
//		Map<Integer, String> map = new HashMap<Integer,String>();
//		map.put(1,"Surya");
//		map.put(2,"Bob");
//		map.put(3, "Aditya");
//		
//		
//		Set<Map.Entry<Integer, String>> set = map.entrySet();
//		//System.out.println(set);
//		
//		for(Map.Entry x: set)
//		{
//			//System.out.println(x.getKey() + " " + x.getValue());
//		}
//		
//		
//		Set<Integer> myset = map.keySet();
//		Iterator<Integer> iter = myset.iterator();
//		
//		while(iter.hasNext())
//		{
//			iter.next();
//			//System.out.println(map.get(iter.next()));
//		}		
//		
//		List<Integer> lst = new ArrayList(Arrays.asList(2,5,3,7,8,4,3));
//		Collections.sort(lst);
//		
//		
//		//Set<Integer> testSet = new HashSet<Integer>(Arrays.asList(2,5,3,7,8,4,3));
//		//TreeSet<Integer> sorted = new TreeSet<Integer>(testSet);
//		
//		//System.out.println(sorted);
//		
//		
//	}


}











































































































