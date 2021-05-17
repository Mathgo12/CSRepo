package ml.ml;
import java.util.*;  //Iterator, ArrayList, HashMap;

public class Practice {

	public static void main(String[] args) {
		
		ArrayList<Integer> numbers = new ArrayList<Integer>();
	    numbers.add(12);
	    numbers.add(8);
	    numbers.add(2);
	    numbers.add(23);
	   
	    Iterator<Integer> iterator = numbers.iterator();
	    
	    while(iterator.hasNext())
	    {
	    	Integer element = iterator.next();
	    	if(element > 10) iterator.remove();

	    }
	    
	    System.out.println(numbers);
	    
	}

}




















































































































