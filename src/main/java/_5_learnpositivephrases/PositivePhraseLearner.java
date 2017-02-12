package _5_learnpositivephrases;

import _2_ngram.NGramExtractor;
import cmu.arktweetnlp.Tagger;
import utility.Utility;
import utility.TagDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Erol Özkan on 19-May-16.
 */
public class PositivePhraseLearner {

    public static final String FILES_STEP4_OUTPUT1GRAM_TXT = "files/step4/output1gram.txt";
    public static final String FILES_STEP4_OUTPUT2GRAM_TXT = "files/step4/output2gram.txt";
    public static final String FILES_STEP4_1GRAM_OUTPUT_TXT = "files/step4/1gramOUTPUT.txt";
    public static final String FILES_STEP4_OUTPUT2GRAM_TXT_OUTPUT = "files/step4/2gramOUTPUT.txt";


    private static String INPUT_FILE = "files/step4/negatives_input.txt";
    private static String INPUT_FILE_SARCASTIC_TWEETS = "files/input/AllTweets.txt";
    private static String OUTPUT_FILE_WORDS = "files/step4/words.txt";
    private static Tagger tagger = new Tagger();

    private static ArrayList<String> negativeConcepts = new ArrayList<String>();
    private static ArrayList<String> sarcasticTwetets = new ArrayList<String>();
    private static List<String> learnedPositivePhrases = new ArrayList<String>();

    private static List<String> outputOneGramList = new ArrayList<String>();
    private static List<String> outputTwoGramList = new ArrayList<String>();

    public static void main(String[] args) {

        callLoadModel();
        negativeConcepts = Utility.readFile(INPUT_FILE);
        sarcasticTwetets = Utility.readFile(INPUT_FILE_SARCASTIC_TWEETS);


        for (int i = 0; i < sarcasticTwetets.size(); i++) {
            String tweet = sarcasticTwetets.get(i).toLowerCase();
            //    List<Tagger.TaggedToken> tokens = tagger.tokenizeAndTag(tweet);


            for (int j = 0; j < negativeConcepts.size(); j++) {
                String negative = negativeConcepts.get(j).toLowerCase();


                if (tweet.contains(negative) && !tweet.contains("love") && tweet.contains("sarcasm")) {

                    String sss = tweet;
                    tweet = tweet.split(negative)[0];
                    String[] parts = tweet.split(" ");
                    if (parts.length > 2) {

                        String s1 = parts[parts.length - 2].trim();
                        String s2 = parts[parts.length - 1].trim();


                        if (!outputOneGramList.contains(s1)) {
                            outputOneGramList.add(s1);
                        }
                        if (!outputTwoGramList.contains(s1 + " " + s2)) {
                            outputTwoGramList.add(s1 + " " + s2);
                        }

                    }
                    if (parts.length > 0 && parts.length < 2) {
                        if (!outputOneGramList.contains(parts[parts.length - 1].trim())) {
                            outputOneGramList.add(parts[parts.length - 1].trim());
                        }
                    }
                }
            }
        }

        TagDetails.MyFileBuilder.writeLinesToFile(FILES_STEP4_OUTPUT1GRAM_TXT, outputOneGramList);
        TagDetails.MyFileBuilder.writeLinesToFile(FILES_STEP4_OUTPUT2GRAM_TXT, outputTwoGramList);

        NGramExtractor ngram = new NGramExtractor();
        ngram.printNgrams(FILES_STEP4_OUTPUT1GRAM_TXT, FILES_STEP4_1GRAM_OUTPUT_TXT, 1);
        ngram.printNgrams(FILES_STEP4_OUTPUT2GRAM_TXT, FILES_STEP4_OUTPUT2GRAM_TXT_OUTPUT, 2);
    }

    public static void callLoadModel() {
        try {
            tagger.loadModel("files/postaggermodelforenglish");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
