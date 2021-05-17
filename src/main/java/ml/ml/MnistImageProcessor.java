package ml.ml;

//IO, Image, File handling imports
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.datavec.image.loader.ImageLoader;


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
	    RecordReader rr = new ImageRecordReader(28,28,1); //28x28 RGB images
	    rr.initialize(new FileSplit(new File("C:\\Users\\prsnb\\MnistData\\mnist_png\\testing")));

	    DataSetIterator iter = new RecordReaderDataSetIterator.Builder(rr, 32)
	         .classification(1, 10)
	         .preProcessor(new ImagePreProcessingScaler())    
	         .build();

	    
	    
	    //recordReader.initialize(new FileSplit(file));   
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











































































































