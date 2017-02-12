package utility;
import java.util.Comparator;

public class TokenComparator implements Comparator<TokenDetails>
{

	public int compare(TokenDetails t1, TokenDetails t2)
	{
		if(t2.getProb() > t1.getProb())
			return 1;
		else if (t2.getProb() < t1.getProb())
			return -1;
		return 0;
	}
}
