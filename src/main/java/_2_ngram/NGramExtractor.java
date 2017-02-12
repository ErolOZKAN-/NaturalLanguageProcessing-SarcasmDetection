package _2_ngram;


import utility.TokenDetails;
import utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 25.4.2016.
 */
public class NGramExtractor {

    private static final String INPUT_FILE = "files/step1/AllAppliedRules.txt";
    private static final String OUTPUT_FILE_UNIGRAMS = "files/step2/allUnigrams.txt";
    private static final String OUTPUT_FILE_BIGRAMS = "files/step2/allBigrams.txt";
    private static final String OUTPUT_FILE_TRIGRAMS = "files/step2/allTrigrams.txt";

    /**
     * returns unigram map
     * @param lines
     * @return
     */
    public HashMap<String, Integer> getUnigramMap(ArrayList<String> lines) {
        HashMap<String, Integer> unigramMap = new HashMap<String, Integer>();
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            line = Utility.processToken(line);
            line = Utility.removeExtraSpaces(line);
            sb.append(line).append(Utility.space);
        }

        String[] words = sb.toString().trim().split(Utility.space);
        for (int i = 0; i < words.length; i++) {
            if (words[i].indexOf("#sarcasm") > -1)
                continue;

            if (!words[i].trim().equalsIgnoreCase("")) {
                if (!unigramMap.containsKey(words[i]))
                    unigramMap.put(words[i], 1);
                else {
                    int count = unigramMap.get(words[i]);
                    unigramMap.put(words[i], count + 1);
                }
            }
        }
        return unigramMap;
    }

    /**
     * returns all ngrams except unigram
     * @param lines
     * @param n
     * @return
     */
    public HashMap<String, Integer> getNgramMap(ArrayList<String> lines, int n) {
        HashMap<String, Integer> ngramMap = new HashMap<String, Integer>();

        for (String line : lines) {
            line = line.trim();
            line = Utility.processToken(line);
            line = Utility.removeExtraSpaces(line);

            if (line == null)
                continue;
            String[] words = null;
            try {
                words = line.split(Utility.space);
            } catch (NullPointerException npe) {
                System.out.println("NPE for:" + line);
                System.exit(1);
            }

            String token = "";
            for (int i = 0; i < words.length - (n - 1); i++) {
                for (int j = 0; j < n; j++) {
                    if (j % n == (n - 1)) {
                        token = token + Utility.space + words[i + j];
                        if (!ngramMap.containsKey(token)) {
                            if (token.indexOf("#sarcasm") == -1) {
                                token = Utility.removeExtraSpaces(token);
                                ngramMap.put(token, 1);
                            }
                        } else {
                            if (token.indexOf("#sarcasm") == -1) {
                                int count = ngramMap.get(token);
                                token = Utility.removeExtraSpaces(token);
                                ngramMap.put(token, count + 1);
                            }
                        }
                        token = "";
                    } else if (j % n == 0) {
                        token = "" + words[i + j];
                    } else {
                        token = token + Utility.space + words[i + j];
                    }
                }
            }
        }
        return ngramMap;
    }

    /**
     * prints ngram to given file
     * @param file1
     * @param outputFile
     * @param mode
     */
    public void printNgrams(String file1, String outputFile, int mode) {
        ArrayList<String> lines = Utility.readFile(file1);
        if (mode == 1) {
            HashMap<String, Integer> unigrams = getUnigramMap(lines);
            HashMap<String, TokenDetails> unigramsTokenMap = Utility.convertMapToTokens(unigrams);
            Utility.printMap(unigramsTokenMap, 2, outputFile);
        } else {
            HashMap<String, Integer> ngrams = getNgramMap(lines, mode);
            HashMap<String, TokenDetails> ngramMap = Utility.convertMapToTokens(ngrams);
            Utility.printMap(ngramMap, 2, outputFile);
        }
    }

    public static void main(String[] args) {
        NGramExtractor ngram = new NGramExtractor();

        ngram.printNgrams(INPUT_FILE, OUTPUT_FILE_UNIGRAMS, 1);
        ngram.printNgrams(INPUT_FILE, OUTPUT_FILE_BIGRAMS, 2);
        ngram.printNgrams(INPUT_FILE, OUTPUT_FILE_TRIGRAMS, 3);

    }
}
