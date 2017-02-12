package _1_tweetfilter;

import cmu.arktweetnlp.Tagger;
import utility.Utility;
import utility.TagDetails;
import utility.TagDetailsComparator;

import java.io.IOException;
import java.util.*;

/**
 * Created by user on 18.4.2016.
 */
public class TweetFilter {

    public static final String FILE_TO_READ_AS_INPUT = "files/input/sarcastic_tweets.txt";
    //private static final String FILE_TO_READ_AS_INPUT = "files/input/tweetsForTest.txt";
    private static final String FILES_STEP1_APLIED_RULE_TWEET1_TXT = "files/step1/ApliedRuleTweet1.txt";
    private static final String FILES_STEP1_APLIED_RULE_TWEET2_TXT = "files/step1/ApliedRuleTweet2.txt";
    private static final String FILES_STEP1_APLIED_RULE_TWEET3_TXT = "files/step1/ApliedRuleTweet3.txt";
    private static final String FILES_STEP1_APLIED_RULE_TWEET4_TXT = "files/step1/ApliedRuleTweet4.txt";
    private static final String FILES_STEP1_APLIED_RULE_TWEET5_TXT = "files/step1/ApliedRuleTweet5.txt";
    private static final String CONTAINS_ALL_FILE_OUTPUT = "files/step1/AllAppliedRules.txt";

    private static final String NUMBER_OF_TWEETS_THAT_INPUT_FILE_CONTAINS = "NUMBER OF TWEETS THAT INPUT FILE CONTAINS : ";
    private static final String POSSIBLE_PHRASES = "\t\t\t\t POSSIBLE PHRASES FOR REGEX";
    private static final String TOTAL_KEYS = "TOTAL KEYS:";
    private static final String TOTAL_COUNT = "\t TOTAL COUNT:";
    private static final String X1 = "-----------------------------------------------------------------";

    private Tagger tagger;
    private HashMap<String, String> tagDescription = new HashMap<String, String>();
    private ArrayList<String> tweets;
    private HashMap<String, Integer> posTagsMap = new HashMap<String, Integer>();
    private HashMap<String, ArrayList<String>> posTagsTweetsMap = new HashMap<String, ArrayList<String>>();
    private ArrayList<TagDetails> tagsInfo = new ArrayList<TagDetails>();
    private PosPattern chunker;
    private HashMap<String, Integer> matchedTweetsMap = new HashMap<String, Integer>();
    private List<String> allLinestToWrite = new ArrayList<String>();

    /**
     * constructor
     */
    TweetFilter() {
        tagger = new Tagger();
        tweets = new ArrayList<String>();
        chunker = new PosPattern();
        buildMap();
        callLoadModel();
    }

    /**
     * fills the tag hash map
     */
    public void buildMap() {

        tagDescription.put("N", "CommonNoun");
        tagDescription.put("O", "Pronoun");
        tagDescription.put("^", "ProperNoun");
        tagDescription.put("S", "NominalPossessive");
        tagDescription.put("Z", "ProperPossessive");
        tagDescription.put("V", "VerbAuxillaries");
        tagDescription.put("A", "Adjective");
        tagDescription.put("R", "Adverb");
        tagDescription.put("!", "Interjection");
        tagDescription.put("D", "Determiner");
        tagDescription.put("P", "PrepositionOrConjuction");
        tagDescription.put("&", "CoConjunction");
        tagDescription.put("T", "VerbParticle");
        tagDescription.put("X", "ExistentialPredeterminers");
        tagDescription.put("#", "Hashtag");
        tagDescription.put("@", "At-Mention");
        tagDescription.put("~", "DiscourseMarker");
        tagDescription.put("E", "Emoticon");
        tagDescription.put("$", "Numeral");
        tagDescription.put(",", "Punctuation");
        tagDescription.put("G", "OtherAbbreviationsOrForeign Words");
        tagDescription.put("L", "NominalVerbalViceVersa");
        tagDescription.put("M", "ProperNounAndVerbal");
        tagDescription.put("Y", "ExistentialPredeterminersAndVerbal");
    }

    /**
     * returns tag description
     *
     * @param tag
     * @return
     */
    public String getTagDescription(String tag) {
        if (tagDescription.containsKey(tag))
            return tagDescription.get(tag);

        String[] tags = tag.split(" ");

        if (tags.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tags.length; i++) {
                if (i == tags.length - 1)
                    sb.append(getTagDescription(tags[i]));
                else
                    sb.append(getTagDescription(tags[i])).append(" ");
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * the english model for tagger
     */
    public void callLoadModel() {
        try {
            tagger.loadModel("files/postaggermodelforenglish");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void ruleFollowUp() {

        if (matchedTweetsMap.containsKey(""))
            matchedTweetsMap.remove("");

        Iterator<Map.Entry<String, Integer>> it = matchedTweetsMap.entrySet().iterator();
        ArrayList<TagDetails> tdList = new ArrayList<TagDetails>();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();
            if (pair.getKey().trim().equalsIgnoreCase(""))
                continue;

            TagDetails td = new TagDetails();
            td.setCount(pair.getValue());
            td.setTags(pair.getKey());
            tdList.add(td);
        }

        TagDetailsComparator tdc = new TagDetailsComparator();
        Collections.sort(tdList, tdc);
        int count = 0;
        printTagDetails(tdList, count);
    }

    /**
     * prints tag details.
     *
     * @param tdList
     * @param count
     */
    private void printTagDetails(ArrayList<TagDetails> tdList, int count) {
        System.out.println(X1);
        System.out.println();
        System.out.println(X1);
        System.out.println();
        System.out.println(POSSIBLE_PHRASES);

        for (TagDetails td : tdList) {
            count = count + td.getCount();
            System.out.println(td.getTags());
        }

        System.out.println();
        System.out.println(TOTAL_KEYS + tdList.size() + TOTAL_COUNT + count);
        System.out.println();
    }

    /**
     * filters the rule according to the regex
     */
    public void filterForRegex1() {
        List<String> linesToWrite = new ArrayList<String>();
        for (TagDetails tagObject : tagsInfo) {
            if (chunker.isTweetGoodRule1(tagObject.getTags())) {
                for (String tweet : tagObject.getTweets()) {
                    //System.out.println(tweet);
                    String[] tmpArray = tweet.split(" love ");
                    if (tmpArray.length < 2)
                        continue;
                    linesToWrite.add(tweet);
                    allLinestToWrite.add(tweet);
                    String tmp = tmpArray[1];
                    tmp = Utility.processToken(tmp);
                    tmp = Utility.returnFirstNWords(tmp, 1);

                    String[] words = tmp.split(" ");
                    if (words[0].endsWith("ing")) {
                        if (matchedTweetsMap.containsKey(tmp)) {
                            int count = matchedTweetsMap.get(tmp);
                            count = count + 1;
                            matchedTweetsMap.put(tmp, count);
                        } else {
                            matchedTweetsMap.put(tmp, 1);
                        }
                        //System.out.println(tweet);
                    }
                }
            }
        }
        TagDetails.MyFileBuilder.writeLinesToFile(FILES_STEP1_APLIED_RULE_TWEET1_TXT, linesToWrite);
        ruleFollowUp();
    }

    /**
     * filters the rule according to the regex
     */
    public void filterForRegex2() {
        List<String> linesToWrite = new ArrayList<String>();
        for (TagDetails tagObject : tagsInfo) {
            if (chunker.isTweetGoodRule2(tagObject.getTags())) {
                for (String tweet : tagObject.getTweets()) {
                    //System.out.println(tweet);
                    String[] tmpArray = tweet.split(" love ");
                    if (tmpArray.length < 2)
                        continue;
                    linesToWrite.add(tweet);
                    allLinestToWrite.add(tweet);
                    String tmp = tmpArray[1];
                    tmp = Utility.processToken(tmp);


                    tmp = Utility.returnFirstNWords(tmp, 2);


                    if (tmp == null)
                        continue;

                    String[] tokens = tmp.split(" ");
                    if (tokens[0].endsWith("ing") && (tokens[1].endsWith("ed") || tokens[1].endsWith("en"))) {

                        if (matchedTweetsMap.containsKey(tmp)) {
                            int count = matchedTweetsMap.get(tmp);
                            count = count + 1;
                            matchedTweetsMap.put(tmp, count);
                        } else {
                            matchedTweetsMap.put(tmp, 1);
                        }

//						/System.out.println(tweet);
                    }
                }
            }
        }
        TagDetails.MyFileBuilder.writeLinesToFile(FILES_STEP1_APLIED_RULE_TWEET2_TXT, linesToWrite);
        ruleFollowUp();
    }

    /**
     * filters the rule according to the regex
     */
    public void filterForRegex3() {
        List<String> linesToWrite = new ArrayList<String>();
        for (TagDetails tagObject : tagsInfo) {
            if (chunker.isTweetGoodRule3(tagObject.getTags())) {
                for (String tweet : tagObject.getTweets()) {
                    //System.out.println(tweet);
                    String[] tmpArray = tweet.split(" love ");
                    if (tmpArray.length < 2)
                        continue;
                    linesToWrite.add(tweet);
                    allLinestToWrite.add(tweet);
                    String tmp = tmpArray[1];
                    tmp = Utility.processToken(tmp);
                    tmp = Utility.returnFirstNWords(tmp, 1);

                    String[] tokens = tmp.split(" ");

                    if (matchedTweetsMap.containsKey(tmp)) {
                        int count = matchedTweetsMap.get(tmp);
                        count = count + 1;
                        matchedTweetsMap.put(tmp, count);
                    } else
                        matchedTweetsMap.put(tmp, 1);
                    //System.out.println(tweet);
                }
            }
        }

        TagDetails.MyFileBuilder.writeLinesToFile(FILES_STEP1_APLIED_RULE_TWEET3_TXT, linesToWrite);
        ruleFollowUp();
    }

    /**
     * filters the rule according to the regex
     */
    public void filterForRegex4() {
        List<String> linesToWrite = new ArrayList<String>();
        for (TagDetails tagObject : tagsInfo) {
            if (chunker.isTweetGoodRule4(tagObject.getTags())) {
                for (String tweet : tagObject.getTweets()) {
                    //System.out.println(tweet);
                    String[] tmpArray = tweet.split(" love ");
                    if (tmpArray.length < 2)
                        continue;
                    linesToWrite.add(tweet);
                    allLinestToWrite.add(tweet);

                    String tmp = tmpArray[1];
                    tmp = Utility.processToken(tmp);
                    tmp = Utility.returnFirstNWords(tmp, 2);

                    String[] tokens = tmp.split(" ");
                    if (tokens[0].endsWith("ing")) {
                        if (matchedTweetsMap.containsKey(tmp)) {
                            int count = matchedTweetsMap.get(tmp);
                            count = count + 1;
                            matchedTweetsMap.put(tmp, count);
                        } else
                            matchedTweetsMap.put(tmp, 1);

                        //System.out.println(tweet);
                    }
                }
            }
        }
        TagDetails.MyFileBuilder.writeLinesToFile(FILES_STEP1_APLIED_RULE_TWEET4_TXT, linesToWrite);
        ruleFollowUp();
    }

    /**
     * filters the rule according to the regex
     */
    public void filterForRegex5() {
        List<String> linesToWrite = new ArrayList<String>();
        for (TagDetails tagObject : tagsInfo) {
            if (chunker.isTweetGoodRule5(tagObject.getTags())) {
                for (String tweet : tagObject.getTweets()) {
                    //System.out.println(tweet);
                    String[] tmpArray = tweet.split(" love ");
                    if (tmpArray.length < 2)
                        continue;
                    linesToWrite.add(tweet);
                    allLinestToWrite.add(tweet);

                    String tmp = tmpArray[1];
                    tmp = Utility.processToken(tmp);
                    tmp = Utility.returnFirstNWords(tmp, 2);

                    if (tmp == null)
                        continue;

                    String[] words = tmp.split(" ");
                    if (words[0].endsWith("ing")) {
                        if (matchedTweetsMap.containsKey(tmp)) {
                            int count = matchedTweetsMap.get(tmp);
                            count = count + 1;
                            matchedTweetsMap.put(tmp, count);
                        } else {
                            matchedTweetsMap.put(tmp, 1);
                        }
                        //System.out.println(tweet);
                    }
                }
            }
        }
        TagDetails.MyFileBuilder.writeLinesToFile(FILES_STEP1_APLIED_RULE_TWEET5_TXT, linesToWrite);
        ruleFollowUp();
    }

	/*public void filterForRule6()
    {
    List<String> linesToWrite= new ArrayList<String>();
		for(TagDetails tagObject: tagsInfo)
		{
			if(chunker.isTweetGoodRule6(tagObject.getTags()))
			{
				for(String tweet: tagObject.getTweets())
				{
					//System.out.println(tweet);
					String [] tmpArray = tweet.split(" love ");
					if(tmpArray.length<2)
						continue;

					String tmp = tmpArray[1];
					tmp = MyUtil.cleanUp(tmp);
					tmp = MyUtil.RemoveHashTags(tmp);
					int index = getFirstNounAfterLove(tweet);
					tmp = MyUtil.returnFirstNWords(tmp, index);

					String [] words = tmp.split(" ");
					if(words[0].endsWith("ing"))
					{
						if(matchedTweetsMap.containsKey(tmp))
						{
							int count = matchedTweetsMap.get(tmp);
							count = count +1;
							matchedTweetsMap.put(tmp, count);
						}
						else
						{
							matchedTweetsMap.put(tmp, 1);
						}
						//System.out.println(tweet);
					}
				}
			}
		}
		ruleFollowUp();
	}*/


    /**
     * create tag and tweet map
     */
    public void createTagAndTweetMap() {
        Iterator<Map.Entry<String, Integer>> it = posTagsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();
            TagDetails td = new TagDetails();
            td.setTags(pair.getKey());
            td.setCount(pair.getValue());
            td.setTagDescription(getTagDescription(td.getTags()));

            if (posTagsTweetsMap.containsKey(pair.getKey())) {
                ArrayList<String> tmpList = posTagsTweetsMap.get(pair.getKey());
                td.setTweets(tmpList);
            } else
                td.setTweets(null);
            tagsInfo.add(td);
        }
        TagDetailsComparator tdc = new TagDetailsComparator();
        Collections.sort(tagsInfo, tdc);

        printTagDetails();
    }

    /**
     * prints tagDetailsList
     */
    private void printTagDetails() {
        for (TagDetails td : tagsInfo) {
            td.printTagDetailsInDetail();
        }
    }

    /**
     * ÝF TWEET CONTAINS LOVE KEYWORD, IT RETURNS THE TAGS AFTER LOVE
     *
     * @param tokens
     * @param tweet
     */
    public void buildPOSTagMapSpecific(List<Tagger.TaggedToken> tokens, String tweet) {
        boolean isLoveFound = false;
        int size = tokens.size();
        int loveIndex = -1;

        for (int i = 0; i < size; i++) {
            if (!isLoveFound) {
                if (tokens.get(i).token.toLowerCase().equalsIgnoreCase("love")) {
                    isLoveFound = true;
                    loveIndex = i;
                    break;
                }
            }
        }

        if (isLoveFound && loveIndex > -1) {
            StringBuilder sb = new StringBuilder();
            for (int i = loveIndex + 1; i < size; i++) {
                if (i == size - 1)
                    sb.append(tokens.get(i).tag);
                else
                    sb.append(tokens.get(i).tag).append(" ");
            }

            String tagKey = sb.toString();
            if (posTagsMap.containsKey(tagKey)) {
                int count = posTagsMap.get(tagKey);
                posTagsMap.put(tagKey, count + 1);
            } else {
                posTagsMap.put(tagKey, 1);
            }


            if (posTagsTweetsMap.containsKey(tagKey)) {
                ArrayList<String> tweetsList = posTagsTweetsMap.get(tagKey);
                tweetsList.add(tweet);

                posTagsTweetsMap.put(tagKey, tweetsList);
            } else {
                ArrayList<String> tmp = new ArrayList<String>();
                tmp.add(tweet);
                posTagsTweetsMap.put(tagKey, tmp);
            }
        }
    }

    /**
     * PRECPORCESS TWEETS, TAGS THEM
     */
    public void tagTweet() {

        tweets = Utility.readFile(FILE_TO_READ_AS_INPUT);
        System.out.println(NUMBER_OF_TWEETS_THAT_INPUT_FILE_CONTAINS + tweets.size());

        for (String tweet : tweets) {
            List<Tagger.TaggedToken> tokens = tagger.tokenizeAndTag(tweet);
            //printTaggingDetails(tokens);
            buildPOSTagMapSpecific(tokens, tweet);
        }
    }


    public void writeAll() {
        TagDetails.MyFileBuilder.writeLinesToFile(CONTAINS_ALL_FILE_OUTPUT, allLinestToWrite);
    }

    public static void main(String[] args) {
        TweetFilter t = new TweetFilter();
        t.tagTweet();
        t.createTagAndTweetMap();
        t.filterForRegex1();
        t.filterForRegex2();
        t.filterForRegex3();
        t.filterForRegex4();
        t.filterForRegex5();
        t.writeAll();


        //  t.predictState();
    }
}
