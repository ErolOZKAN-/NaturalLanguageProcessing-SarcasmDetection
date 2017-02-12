package _2_ngram;

import utility.TokenDetails;
import utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 26.4.2016.
 */
public class NGramExtractorAfterLove {

    private static final String INPUT_FILE = "files/step1/AllAppliedRules.txt";
    private static final String UNIGRAM_FILE_NAME_OUTPUT = "files/step2/loveSarcasmUnigrams.txt";
    private static final String BIGRAM_FILE_NAME_OUTPUT = "files/step2/loveSarcasmBigrams.txt";
    private static final String TRIGRAM_FILE_NAME_OUTPUT = "files/step2/loveSarcasmTrigrams.txt";
    private static final String X = "---------------------------------------------";
    private static final String UNIGRAMS_AFTER_LOVE = "\t\t\tUNIGRAMS AFTER LOVE";
    private static final String BIGRAMS_AFTER_LOVE = "\t\t\tBIGRAMS_AFTER_LOVE";
    private static final String TRIGRAMS_AFTER_LOVE = "\t\t\tTRIGRAMS_AFTER_LOVE";

    private String space = " ";
    private ArrayList<String> tweets = new ArrayList<String>();
    private HashMap<String, Integer> unigramMap = new HashMap<String, Integer>();
    private HashMap<String, Integer> bigramMap = new HashMap<String, Integer>();
    private HashMap<String, Integer> trigramMap = new HashMap<String, Integer>();

    private ArrayList<TokenDetails> unigrams = new ArrayList<TokenDetails>();
    private ArrayList<TokenDetails> bigrams = new ArrayList<TokenDetails>();
    private ArrayList<TokenDetails> trigrams = new ArrayList<TokenDetails>();
    private String searchCriteria = " love ";

    /**
     * constructor
     */
    NGramExtractorAfterLove() {
        init();
    }

    /**
     * init method, read input file and checks if the tweet containts the word love
     */
    public void init() {
        tweets = Utility.readFile(INPUT_FILE);
        int size = tweets.size();
        ArrayList<String> loveTweets = new ArrayList<String>();

        for (int i = 0; i < size; i++) {
            String tweet = tweets.get(i);
            if (tweet.contains(searchCriteria)) {
                loveTweets.add(tweet);
            }
        }
        tweets = loveTweets;

    }

    /**
     * returns the string after "love" keyword
     *
     * @param txt
     * @return
     */
    public String getStringAfterLove(String txt) {
        if (txt == null)
            return null;

        if (!txt.startsWith("love ")) {
            String[] tokens = txt.split(searchCriteria);
            if (tokens.length > 1)
                return tokens[1];
        } else {
            String[] tokens = txt.split("love ");
            if (tokens.length > 1)
                return tokens[1];
        }
        return null;
    }

    /**
     * build a map for ngram
     *
     * @param n
     * @return
     */
    public HashMap<String, Integer> fillNgramMap(int n) {
        int size = tweets.size();
        HashMap<String, Integer> ngramMap = new HashMap<String, Integer>();

        for (int i = 0; i < size; i++) {
            String data = tweets.get(i);
            data = Utility.processToken(data);
            data = getStringAfterLove(data);
            data = Utility.returnFirstNWords(data, n);
            if (data != null) {
                if (ngramMap.containsKey(data)) {
                    int count = ngramMap.get(data);
                    count = count + 1;
                    ngramMap.put(data, count);
                } else
                    ngramMap.put(data, 1);
            }
        }
        return ngramMap;
    }


    /**
     * build ngram map
     */
    public void getNgrams() {
        HashMap<String, Integer> unigramMap = fillNgramMap(1);
        HashMap<String, TokenDetails> unigramsTokenMap = Utility.convertMapToTokens(unigramMap);
        System.out.println();
        System.out.println(UNIGRAMS_AFTER_LOVE);
        System.out.println(X);
        Utility.printMap(unigramsTokenMap, 2, UNIGRAM_FILE_NAME_OUTPUT);

        HashMap<String, Integer> bigramMap = fillNgramMap(2);
        HashMap<String, TokenDetails> bigramsTokenMap = Utility.convertMapToTokens(bigramMap);
        System.out.println(BIGRAMS_AFTER_LOVE);
        System.out.println(X);
        Utility.printMap(bigramsTokenMap, 2, BIGRAM_FILE_NAME_OUTPUT);

        HashMap<String, Integer> trigramMap = fillNgramMap(3);
        HashMap<String, TokenDetails> trigramsTokenMap = Utility.convertMapToTokens(trigramMap);
        System.out.println(TRIGRAMS_AFTER_LOVE);
        System.out.println(X);
        Utility.printMap(trigramsTokenMap, 2, TRIGRAM_FILE_NAME_OUTPUT);

    }

    public static void main(String[] args) {
        NGramExtractorAfterLove ngram = new NGramExtractorAfterLove();
        ngram.getNgrams();
    }
}
