package _4_classify;

import utility.Utility;

import java.util.ArrayList;

/**
 * Created by user on 17.5.2016.
 */
public class SarcasmClassifier {

    private static final String FILE_POSITIVE_INPUT = "files/positive.txt";
    private static final String FILE_NEGATIVE_INPUT = "files/negative.txt";

    private static final String FILES_TEST_TWEETS = "files/input/tweetsForTest.txt";
    private static final String FILES_TEST_TWEETS_ANNOTATION = "files/input/annotation.txt";

    private static final String ONLY_POSITIVE_TEST = "ONLY POSITIVE TEST:\n";
    private static final String ONLY_NEGATIVE_TEST = "ONLY NEGATIVE TEST:\n";
    private static final String PRINTING_STATS_FOR_LOVE_NEG_TEST = "LOVE WORD + NEG: \n";
    private static final String PRINTING_STATS_FOR_POS_NEG_IN_ANY_ORDER_TEST = "POS AND NEG IN ANY ORDER:\n";
    private static final String PRINTING_STATS_FOR_POS_NEG_TEST = "POS FOLLOWED BY NEG:\n";

    private static final String CORRECT_PREDICTIONS_FOR_TWEETS = "Correct Predictions for tweets:";
    private static final String TOTAL_TWEETS_CREATED = "Total Tweets Read:";
    private static final String TOTAL_NEGATIVE_CONCEPTS_LEARNED = "Total negative words learned:";
    public static final String TOTAL_POSITIVE_CONCEPTS_LEARNED = "Total positive words learned:";
    private static final String POSITIVE_TAGGED_TOTAL_TWEET_COUNT_BY_METHOD = "Positive Tagged Total Tweet Count By Method:";
    private static final String SARCASM_TAG_IN_ANNOTATION = "SARCASM";

    private static final String PRECISION = "Precision:";
    private static final String RECALL = "Recall:";
    private static final String F_SCORE = "F-score:";

    private static final String X = "\n-------------------------------------\n";


    private ArrayList<Tweet> tweetsList = new ArrayList<Tweet>();
    private ArrayList<String> negativeConcepts = new ArrayList<String>();
    private ArrayList<String> positiveConcepts = new ArrayList<String>();
    private ArrayList<String> neutralConcepts = new ArrayList<String>();
    private int sarcasticTweets;

    /**
     * initiliez the class
     */
    public void construct() {
        positiveConcepts = Utility.readFile(FILE_POSITIVE_INPUT);
        negativeConcepts = Utility.readFile(FILE_NEGATIVE_INPUT);

        ArrayList<String> tweetLines = Utility.readFile(FILES_TEST_TWEETS);
        ArrayList<String> annotations = Utility.readFile(FILES_TEST_TWEETS_ANNOTATION);

        initializeSarcamsTag(tweetLines, annotations);

        System.out.println();
        System.out.println(TOTAL_TWEETS_CREATED + tweetsList.size());
        System.out.println(TOTAL_NEGATIVE_CONCEPTS_LEARNED + negativeConcepts.size());
        System.out.println(TOTAL_POSITIVE_CONCEPTS_LEARNED + positiveConcepts.size());

        System.out.println();
        System.out.println();
    }

    /**
     * initiliez the sarcasm list, tags tweets according to the annotation.txt
     *
     * @param tweetLines
     * @param annotations
     */
    private void initializeSarcamsTag(ArrayList<String> tweetLines, ArrayList<String> annotations) {
        int size = tweetLines.size();
        for (int i = 0; i < size; i++) {
            Tweet t = new Tweet();
            t.setTweetText(tweetLines.get(i));
            if (annotations.get(i).equalsIgnoreCase(SARCASM_TAG_IN_ANNOTATION)) {
                t.setSarcastic(true);
                sarcasticTweets = sarcasticTweets + 1;
            } else {
                t.setSarcastic(false);
            }
            tweetsList.add(t);
        }
    }

    /**
     * just test if the tweet contains positive sentiment
     */
    public void onlyPositiveTest() {
        int size = tweetsList.size();
        ArrayList<Tweet> positive = new ArrayList<Tweet>();
        ArrayList<Tweet> negative = new ArrayList<Tweet>();

        for (int i = 0; i < size; i++) {
            Tweet t = tweetsList.get(i);
            if (containsPositiveConcept(t.getTweetText()))
                positive.add(t);
            else
                negative.add(t);
        }

        int correctAnswer = 0;
        size = positive.size();
        for (int i = 0; i < size; i++) {
            Tweet t = positive.get(i);
            if (t.isSarcastic())
                correctAnswer = correctAnswer + 1;
        }

        System.out.println(ONLY_POSITIVE_TEST);
        printDetails(correctAnswer, positive.size());

    }

    /**
     * returns true if negative situation is in the tweet otherwise returns false
     *
     * @param t
     * @return
     */
    public boolean containsNegativeConcept(String t) {
        int size = negativeConcepts.size();
        for (int i = 0; i < size; i++) {
            String concept = negativeConcepts.get(i);
            if (t.contains(concept)) {
                return true;
            }

        }
        return false;
    }


    /**
     * returns true if positive sentiment is in the tweet otherwise returns false
     *
     * @param t
     * @return
     */
    public boolean containsPositiveConcept(String t) {
        int size = positiveConcepts.size();
        for (int i = 0; i < size; i++) {
            String concept = positiveConcepts.get(i);
            if (t.contains(concept)) {
                return true;
            }

        }
        return false;
    }

    /**
     * just test if the tweet contains negative situation
     */
    public void onlyNegativeTest() {
        int size = tweetsList.size();
        ArrayList<Tweet> positive = new ArrayList<Tweet>();
        ArrayList<Tweet> negative = new ArrayList<Tweet>();

        for (int i = 0; i < size; i++) {
            Tweet t = tweetsList.get(i);
            String tweetText = t.getTweetText().toLowerCase();
            if (containsNegativeConcept(tweetText)) {
                positive.add(t);
            } else
                negative.add(t);
        }

        int correctAnswer = 0;
        size = positive.size();
        for (int i = 0; i < size; i++) {
            Tweet t = positive.get(i);
            if (t.isSarcastic())
                correctAnswer = correctAnswer + 1;
        }
        System.out.println(ONLY_NEGATIVE_TEST);
        printDetails(correctAnswer, positive.size());

    }

    /**
     * returns the start position of negative situation
     *
     * @param tweetText
     * @param list
     * @return
     */
    public int getTermStartIndex(String tweetText, ArrayList<String> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            String concept = list.get(i);
            int index = tweetText.indexOf(concept);
            if (index > -1) {
                return index;
            }
        }

        return -1;
    }

    /**
     * returns the end position of positive sentiment
     *
     * @param tweetText
     * @param list
     * @return
     */
    public int getTermEndIndex(String tweetText, ArrayList<String> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            String concept = list.get(i);
            int index = tweetText.indexOf(concept);
            if (index > -1) {
                return index + concept.length();
            }
        }

        return -1;
    }

    /**
     * test if the tweet contains positive sentiment FOLLOWED BY negative situation
     */
    public void posNegTest() {
        String love = "love";
        int size = tweetsList.size();
        ArrayList<Tweet> positive = new ArrayList<Tweet>();
        ArrayList<Tweet> negative = new ArrayList<Tweet>();


        for (int i = 0; i < size; i++) {
            Tweet t = tweetsList.get(i);
            String tweetText = t.getTweetText().toLowerCase();

            boolean containsPositiveSymbol = Utility.containsSymbol(tweetText, Utility.positive);
            boolean containsNegativeSymbol = Utility.containsSymbol(tweetText, Utility.negative);


            //if((containsPositiveConcept(tweetText) || containsPositiveSymbol) && (containsNegativeConcept(tweetText) || containsNegativeSymbol))
            //if((containsPositiveConcept(tweetText) && containsNegativeConcept(tweetText)) || (containsNeutralConcept(tweetText)) && (containsPositiveConcept(tweetText) ||containsNegativeConcept(tweetText)))

            int positiveIndex = getTermEndIndex(tweetText, positiveConcepts);
            int negativeIndex = getTermStartIndex(tweetText, negativeConcepts);

            //if((positiveIndex > -1 && negativeIndex > -1) && (negativeIndex - positiveIndex < 6))
            if ((positiveIndex > -1 && negativeIndex > -1) && (negativeIndex > positiveIndex))
                positive.add(t);
            else
                negative.add(t);
        }

        int correctAnswer = 0;
        size = positive.size();
        for (int i = 0; i < size; i++) {
            Tweet t = positive.get(i);
            if (t.isSarcastic())
                correctAnswer = correctAnswer + 1;
        }

        System.out.println(PRINTING_STATS_FOR_POS_NEG_TEST);
        printDetails(correctAnswer, positive.size());

    }

    /**
     * test if the tweet contains positive sentiment with negative situation, NO ORDER
     */
    public void posNegTestInAnyOrderTest() {
        String love = "love";
        int size = tweetsList.size();
        ArrayList<Tweet> positive = new ArrayList<Tweet>();
        ArrayList<Tweet> negative = new ArrayList<Tweet>();


        for (int i = 0; i < size; i++) {
            Tweet t = tweetsList.get(i);
            String tweetText = t.getTweetText().toLowerCase();


            if (containsPositiveConcept(tweetText) && containsNegativeConcept(tweetText))
                positive.add(t);
            else
                negative.add(t);
        }

        int correctAnswer = 0;
        size = positive.size();
        for (int i = 0; i < size; i++) {
            Tweet t = positive.get(i);
            if (t.isSarcastic())
                correctAnswer = correctAnswer + 1;
        }


        System.out.println(PRINTING_STATS_FOR_POS_NEG_IN_ANY_ORDER_TEST);
        printDetails(correctAnswer, positive.size());


    }

    public void loveAndNegativeTest() {
        String love = "love";
        int size = tweetsList.size();
        ArrayList<Tweet> positive = new ArrayList<Tweet>();
        ArrayList<Tweet> negative = new ArrayList<Tweet>();

        for (int i = 0; i < size; i++) {
            Tweet t = tweetsList.get(i);
            String tweetText = t.getTweetText().toLowerCase();
            if ((tweetText.contains("love"))
                    && containsNegativeConcept(tweetText))
                positive.add(t);
            else
                negative.add(t);
        }

        int correctAnswer = 0;
        size = positive.size();
        for (int i = 0; i < size; i++) {
            Tweet t = positive.get(i);
            if (t.isSarcastic())
                correctAnswer = correctAnswer + 1;
        }

        System.out.println(PRINTING_STATS_FOR_LOVE_NEG_TEST);
        printDetails(correctAnswer, positive.size());
    }


    public void printDetails(int correctAnswers, int predictedAnswers) {
        int totalTweetsSize = tweetsList.size();
        float precision = correctAnswers / (float) predictedAnswers;
        float recall = correctAnswers / (float) sarcasticTweets;
        float f = 2 * (precision * recall) / (precision + recall);
        System.out.println(POSITIVE_TAGGED_TOTAL_TWEET_COUNT_BY_METHOD + predictedAnswers);
        System.out.println(CORRECT_PREDICTIONS_FOR_TWEETS + correctAnswers);
        System.out.println(PRECISION + precision + " (" + correctAnswers + "/" + predictedAnswers + ")");
        System.out.println(RECALL + recall + " (" + correctAnswers + "/" + sarcasticTweets + ")");
        System.out.println(F_SCORE + f);
        System.out.println(X);

    }

    public static void main(String[] args) {
        SarcasmClassifier p = new SarcasmClassifier();
        p.construct();

        p.onlyPositiveTest();
        p.onlyNegativeTest();
        p.loveAndNegativeTest();
        p.posNegTestInAnyOrderTest();
        p.posNegTest();

    }

}

