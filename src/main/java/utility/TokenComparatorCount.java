package utility;
import java.util.Comparator;

public class TokenComparatorCount implements Comparator<TokenDetails>
{
	public int compare(TokenDetails t1, TokenDetails t2)
	{
		if(t2.getCount() > t1.getCount())
			return 1;
		else if (t2.getCount() < t1.getCount())
			return -1;
		return 0;
	}
}
