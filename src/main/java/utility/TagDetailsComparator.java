package utility;
import java.util.Comparator;

public class TagDetailsComparator implements Comparator<TagDetails>{

	public int compare(TagDetails t1, TagDetails t2)
	{
		return ((Integer)t2.getCount()).compareTo(t1.getCount()); 
	}
}