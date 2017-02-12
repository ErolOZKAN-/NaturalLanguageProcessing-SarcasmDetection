package utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class TagDetails {

	String tags;
	String tagDescription;
	int count;
	float prob;
	ArrayList<String> tweets = new ArrayList<String>();

	/**
	 * returns tweets
	 * @return
	 */
	public ArrayList<String> getTweets() {
		return tweets;
	}

	/**
	 * sets tweets
	 * @param tweets
	 */
	public void setTweets(ArrayList<String> tweets) {
		this.tweets = tweets;
	}

	/**
	 * returns TagDescription
	 * @return
	 */
	public String getTagDescription() {
		return tagDescription;
	}

	/**
	 * sets TagDescription
	 * @param tagDescription
	 */
	public void setTagDescription(String tagDescription) {
		this.tagDescription = tagDescription;
	}

	/**
	 * returns tags
	 * @return
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * sets tags
	 * @param tags
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * returns count
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * sets count
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * returns probabilty
	 * @return
	 */
	public float getProb() {
		return prob;
	}

	/**
	 * sets probabilty
	 * @param prob
	 */
	public void setProb(float prob) {
		this.prob = prob;
	}

	/**
	 * prints tag details
	 */
	public void printTagDetails()
	{
		System.out.print("\nStrng:"+this.getTagDescription()+ Utility.delim+" Count:"+this.getCount()+ Utility.delim+" prob:"+this.getProb());
	}

	/**
	 * prints tag deatails in deatail
	 */
	public void printTagDetailsInDetail()
	{

		System.out.print("\n"+this.getTags());
		System.out.print("\n"+this.getTagDescription());
		for(String tweet: this.tweets)
			System.out.println("\n\t "+tweet);
	}


	/**
     * Created by Erol Özkan on 24-Apr-16.
     */
    public static class MyFileBuilder {

        /**
         * takes a list and writes them to a given file.
         * @param fileName
         * @param lineList
         */
        public static void writeLinesToFile(String fileName, List<String> lineList) {

            try {

                PrintStream out = new PrintStream(new FileOutputStream(fileName));

                for (String line : lineList) {
                    String s = new String(line);
                    out.println(s);
                }

                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
