package _3_probabilty;

import utility.*;

import java.util.*;

/**
 * Created by user on 17.5.2016.
 */
public class ProbabilityCalculator {

    private static final String P_UNIGRAM_FILE_NAME_OUTPUT = "files/step2/loveSarcasmUnigramsWithProb.txt";
    private static final String P_BIGRAM_FILE_NAME_OUTPUT = "files/step2/loveSarcasmBigramsWithProb.txt";
    private static final String P_TRIGRAM_FILE_NAME_OUTPUT = "files/step2/loveSarcasmTrigramsWithProb.txt";

    public static final String UNIGRAM_FILE = "files/step2/loveSarcasmUnigrams.txt";
    public static final String BIGRAM_FILE = "files/step2/loveSarcasmBigrams.txt";
    public static final String TRIGRAM_FILE = "files/step2/loveSarcasmTrigrams.txt";
    public static final String FILES_STEP3_NEGATIVE_SITUATION_PHRASES_WITH_PROB_TXT = "files/step3/NegativeSituationPhrasesWithProb.txt";
    public static final String FILES_STEP3_NEGATIVE_SITUATION_PHRASES_TXT = "files/step3/NegativeSituationPhrases.txt";

    ArrayList<String> all = new ArrayList<String>();
    ArrayList<String> sarcastic = new ArrayList<String>();
    ArrayList<String> unigrams = new ArrayList<String>();
    ArrayList<String> bigrams = new ArrayList<String>();
    ArrayList<String> trigrams = new ArrayList<String>();

    HashMap<String, TokenDetails> sarcasticMap = new HashMap<String, TokenDetails>();
    HashMap<String, TokenDetails> allMap = new HashMap<String, TokenDetails>();

    FileWalker fileWalker = new FileWalker();


    public TokenDetails buildAndReturnTokenObject(String[] tokens) {
        int count = -1;

        float prob = 0.0f;
        try {
            count = Integer.parseInt(tokens[1]);
            prob = Float.parseFloat(tokens[2]);
        } catch (NumberFormatException n) {
            System.err.println("Number Format Error: Count:" + tokens[1] + "prob:" + tokens[2]);
            System.exit(1);
        }
        TokenDetails td = new TokenDetails();
        td.setString(tokens[0]);
        td.setCount(count);
        td.setProb(prob);
        return td;
    }

    public TokenDetails buildAndReturnTokenObjectWithProb(String[] tokens) {
        int count = -1;
        float prob = 0.0f;
        try {
            count = Integer.parseInt(tokens[1]);
            //prob = Float.parseFloat(tokens[2]);
        } catch (NumberFormatException n) {
            System.err.println("Number Format Error: Count:" + tokens[1] + "prob:" + tokens[2]);
            System.exit(1);
        }
        TokenDetails td = new TokenDetails();
        td.setString(tokens[0]);
        td.setCount(count);
        td.setProb(prob);
        return td;
    }

    public String[] getTwoTokensFromLine(String line) {
        String[] tokens = line.split(Utility.delim);
        String[] returnVals = new String[2];
        String token = tokens[0];
        String tag = token.substring(6);
        try {
            returnVals[0] = tag;
            token = tokens[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("" + line);
            System.exit(1);
        }
        token = tokens[1].trim();
        returnVals[1] = token.substring(6);
        return returnVals;
    }

    public String[] getThreeTokensFromLine(String line) {
        String[] tokens = line.split(Utility.delim);
        String[] returnVals = new String[3];
        String token = tokens[0];
        String tag = token.substring(6);
        try {
            returnVals[0] = tag;
            token = tokens[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("ArrayIndexException:" + line);
            System.exit(1);
        }
        token = tokens[1].trim();
        returnVals[1] = token.substring(6);
        token = tokens[2];
        String[] anotherTokens = token.split(":");
        returnVals[2] = anotherTokens[1].trim();
        return returnVals;
    }

    public HashMap<String, TokenDetails> buildMapByCalculatingProbs(ArrayList<String> list) {
        HashMap<String, TokenDetails> tmpMap = new HashMap<String, TokenDetails>();
        int size = list.size();

        for (int i = 0; i < size; i++) {
            String line = list.get(i);
            String[] tokens = getTwoTokensFromLine(line);
            TokenDetails td = buildAndReturnTokenObjectWithProb(tokens);
            tmpMap.put(td.getString(), td);
        }
        return tmpMap;
    }

    public HashMap<String, TokenDetails> buildMapByUsingProbs(ArrayList<String> list) {
        HashMap<String, TokenDetails> tmpMap = new HashMap<String, TokenDetails>();
        int size = list.size();

        for (int i = 0; i < size; i++) {
            String line = list.get(i);
            String[] tokens = getThreeTokensFromLine(line);
            TokenDetails td = buildAndReturnTokenObject(tokens);
            tmpMap.put(td.getString(), td);
        }
        return tmpMap;
    }

    public void getProbs() {
        Iterator<Map.Entry<String, TokenDetails>> it = sarcasticMap.entrySet().iterator();
        HashMap<String, TokenDetails> newMap = new HashMap<String, TokenDetails>();
        while (it.hasNext()) {
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it.next();
            String key = pair.getKey();
            TokenDetails tdSarcastic = pair.getValue();
            try {
                TokenDetails tokenAll = allMap.get(key);
                float prob = tdSarcastic.getCount() / (float) tokenAll.getCount();
                tdSarcastic.setProb(prob);
                newMap.put(key, tdSarcastic);
            } catch (NullPointerException npe) {
                System.err.println("ll" + key);
                System.exit(1);
            }
        }
        printMap(newMap, 1);
    }

    public boolean seenTheStartOfThisTokenBefore(HashMap<String, TokenDetails> highProbMap, String key) {
        Iterator<Map.Entry<String, TokenDetails>> it = highProbMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it.next();
            String candidate = pair.getKey();

            String[] keyTokens = key.split(Utility.space);
            String[] candidateTokens = candidate.split(Utility.space);


        }
        return false;
    }

    private void calculateProbabilities(HashMap<String, TokenDetails> unigramsMap, HashMap<String, TokenDetails> bigramsMap, HashMap<String, TokenDetails> trigramsMap) {
        Iterator<Map.Entry<String, TokenDetails>> it = trigramsMap.entrySet().iterator();

        int i = 0;
        while (it.hasNext()) {
            i++;
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it.next();
            String key = pair.getKey();
            pair.getValue().setProb(Float.parseFloat(fileWalker.calculateProbabilty(key).getProbabilty().toString()));
            if (i % 10 == 0) {
                System.out.print(".");
            }

        }
        Utility.printMap(trigramsMap, 2, P_TRIGRAM_FILE_NAME_OUTPUT);
        System.out.println();
        Iterator<Map.Entry<String, TokenDetails>> it2 = bigramsMap.entrySet().iterator();
        while (it2.hasNext()) {
            i++;
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it2.next();
            String key = pair.getKey();
            pair.getValue().setProb(Float.parseFloat(fileWalker.calculateProbabilty(key).getProbabilty().toString()));
            if (i % 10 == 0) {
                System.out.print(".");
            }
        }
        Utility.printMap(bigramsMap, 2, P_BIGRAM_FILE_NAME_OUTPUT);
        System.out.println();
        Iterator<Map.Entry<String, TokenDetails>> it3 = unigramsMap.entrySet().iterator();
        while (it3.hasNext()) {
            i++;
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it3.next();
            String key = pair.getKey();
            pair.getValue().setProb(Float.parseFloat(fileWalker.calculateProbabilty(key).getProbabilty().toString()));
            if (i % 10 == 0) {
                System.out.print(".");
            }
        }

        Utility.printMap(unigramsMap, 2, P_UNIGRAM_FILE_NAME_OUTPUT);


    }

    public HashMap<String, TokenDetails> compareProbs(HashMap<String, TokenDetails> unigramsMap, HashMap<String, TokenDetails> bigramsMap, HashMap<String, TokenDetails> trigramsMap) {
        Iterator<Map.Entry<String, TokenDetails>> it3 = trigramsMap.entrySet().iterator();

        HashMap<String, TokenDetails> highProbMap = new HashMap<String, TokenDetails>();
        List<String> ignoreMap = new ArrayList<String>();

        while (it3.hasNext()) {
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it3.next();
            if (pair.getValue().getCount() > 3) {
                if (pair.getValue().getProb() >= 0.7)
                    highProbMap.put(pair.getKey(), pair.getValue());
            }
        }


        Iterator<Map.Entry<String, TokenDetails>> it = bigramsMap.entrySet().iterator();


        while (it.hasNext()) {
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it.next();
            if (pair.getValue().getCount() > 5) {
                if (pair.getValue().getProb() >= 0.6)
                    highProbMap.put(pair.getKey(), pair.getValue());
            }
        }


        Iterator<Map.Entry<String, TokenDetails>> it2 = unigramsMap.entrySet().iterator();


        while (it2.hasNext()) {
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it2.next();
            if (pair.getValue().getCount() > 1) {
                if (pair.getValue().getProb() >= 0.4)
                    highProbMap.put(pair.getKey(), pair.getValue());
            }
        }


        return highProbMap;

    }


    public HashMap<String, TokenDetails> compareProbsByImportance(HashMap<String, TokenDetails> unigramsMap, HashMap<String, TokenDetails> bigramsMap, HashMap<String, TokenDetails> trigramsMap) {
        Iterator<Map.Entry<String, TokenDetails>> it = trigramsMap.entrySet().iterator();
        HashMap<String, TokenDetails> highProbMap = new HashMap<String, TokenDetails>();

        while (it.hasNext()) {
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it.next();
            String key = pair.getKey();
            TokenDetails triToken = pair.getValue();
            triToken.setImportanceScore(triToken.getProb() * triToken.getCount());

            String uni = Utility.returnFirstNWords(key, 1);
            String bi = Utility.returnFirstNWords(key, 2);

            try {
                TokenDetails uniToken = unigramsMap.get(uni);
                TokenDetails biToken = bigramsMap.get(bi);

                uniToken.setImportanceScore(uniToken.getProb() * uniToken.getCount());
                biToken.setImportanceScore(biToken.getProb() * biToken.getCount());


                int highestIndex = Utility.getHighestNumerIndex(uniToken.getImportanceScore(), biToken.getImportanceScore(), triToken.getImportanceScore());
                TokenDetails tokenToAdd = null;
                if (highestIndex == 1)
                    tokenToAdd = uniToken;
                else if (highestIndex == 2)
                    tokenToAdd = biToken;
                else
                    tokenToAdd = triToken;

                if (tokenToAdd.getCount() >= 7)
                    highProbMap.put(tokenToAdd.getString(), tokenToAdd);
            } catch (NullPointerException npe) {
                System.out.println("Tri:" + key);
                System.out.println("uni:[" + uni + "]");
                System.out.println("bi:[" + bi + "]");
                System.exit(1);
            }
        }
        return highProbMap;
    }

    public void printList(ArrayList<TokenDetails> tdList) {
        int size = tdList.size();
        System.out.println(size);
        List<String> linesToWrite = new ArrayList<String>();
        List<String> linesToWrite2 = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            TokenDetails td = tdList.get(i);
            td.printTokenDetails();
            linesToWrite.add(td.returnTokenDetails());
            linesToWrite2.add(td.getString());
        }

        TagDetails.MyFileBuilder.writeLinesToFile(FILES_STEP3_NEGATIVE_SITUATION_PHRASES_WITH_PROB_TXT, linesToWrite);
        TagDetails.MyFileBuilder.writeLinesToFile(FILES_STEP3_NEGATIVE_SITUATION_PHRASES_TXT, linesToWrite2);
    }

    public void printMap(HashMap<String, TokenDetails> map, int mode) {
        Iterator<Map.Entry<String, TokenDetails>> it = map.entrySet().iterator();
        ArrayList<TokenDetails> tdList = new ArrayList<TokenDetails>();

        while (it.hasNext()) {
            Map.Entry<String, TokenDetails> pair = (Map.Entry<String, TokenDetails>) it.next();
            TokenDetails td = pair.getValue();
            tdList.add(td);
        }

        TokenComparator tc = new TokenComparator();
        Collections.sort(tdList, tc);

        //if(mode ==2)
        sortOnCounts(tdList);
        // else
        // printList(tdList);
    }


    public void sortOnCounts(ArrayList<TokenDetails> tdList) {
        int size = tdList.size();

        float currentProb = tdList.get(0).getProb();
        ArrayList<TokenDetails> finalList = new ArrayList<TokenDetails>();
        ArrayList<TokenDetails> tmpList = new ArrayList<TokenDetails>();
        TokenComparatorCount tcc = new TokenComparatorCount();
        int i = 0;
        while (i < size) {
            float probToCompare = tdList.get(i).getProb();

            if (probToCompare == currentProb) {
                tmpList.add(tdList.get(i));
                i = i + 1;
            } else {
                Collections.sort(tmpList, tcc);
                finalList = Utility.copyList(tmpList, finalList);
                currentProb = tdList.get(i).getProb();
                tmpList.clear();
            }
        }
        printList(finalList);
    }


    public void getHighestProb() {
        init1();
        System.out.println("Building Map for Unigrams Tweets...");
        HashMap<String, TokenDetails> unigramsMap = new HashMap<String, TokenDetails>();
        unigramsMap = buildMapByUsingProbs(unigrams);
        System.out.println("Map for Unigrams Built...");

        System.out.println("Building Map for Bigrams Tweets...");
        HashMap<String, TokenDetails> bigramsMap = new HashMap<String, TokenDetails>();
        bigramsMap = buildMapByUsingProbs(bigrams);
        System.out.println("Map for Bigrams Built...");

        System.out.println("Building Map for Trigrams Tweets...");
        HashMap<String, TokenDetails> trigramsMap = new HashMap<String, TokenDetails>();
        trigramsMap = buildMapByUsingProbs(trigrams);
        System.out.println("Map for Trigrams Built...");

        // calculateProbabilities(unigramsMap, bigramsMap, trigramsMap);
        HashMap<String, TokenDetails> highProbMap = compareProbs(unigramsMap, bigramsMap, trigramsMap);
        printMap(highProbMap, 2);
        //printMap(bigramsMap);

        System.out.println("\n----------------------------------");
    }


    public void getProbabilities() {
        init();
        System.out.println("Building Map for Unigrams Tweets...");
        HashMap<String, TokenDetails> unigramsMap = new HashMap<String, TokenDetails>();
        unigramsMap = buildMapByUsingProbs(unigrams);
        System.out.println("Map for Unigrams Built...");

        System.out.println("Building Map for Bigrams Tweets...");
        HashMap<String, TokenDetails> bigramsMap = new HashMap<String, TokenDetails>();
        bigramsMap = buildMapByUsingProbs(bigrams);
        System.out.println("Map for Bigrams Built...");

        System.out.println("Building Map for Trigrams Tweets...");
        HashMap<String, TokenDetails> trigramsMap = new HashMap<String, TokenDetails>();
        trigramsMap = buildMapByUsingProbs(trigrams);
        System.out.println("Map for Trigrams Built...");

        calculateProbabilities(unigramsMap, bigramsMap, trigramsMap);


        System.out.println("\n----------------------------------");
    }


    public void init() {
        unigrams = Utility.readFile(UNIGRAM_FILE);
        bigrams = Utility.readFile(BIGRAM_FILE);
        trigrams = Utility.readFile(TRIGRAM_FILE);
    }


    public void init1() {
        unigrams = Utility.readFile(P_UNIGRAM_FILE_NAME_OUTPUT);
        bigrams = Utility.readFile(P_BIGRAM_FILE_NAME_OUTPUT);
        trigrams = Utility.readFile(P_TRIGRAM_FILE_NAME_OUTPUT);
    }


    public static void main(String... strings) {
        ProbabilityCalculator p = new ProbabilityCalculator();
        // p.getProbabilities();
        p.getHighestProb();
    }
}
