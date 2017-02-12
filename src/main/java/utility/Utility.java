package utility;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static final String space = " ";
    public static final String delim = "<count>";
    public static final String positive = "+";
    public static final String negative = "-";
    public static Tagger tagger;
    public static ArrayList<String> tabooWords = new ArrayList<String>();
    public static ArrayList<String> usedConcepts = new ArrayList<String>();
    public static HashMap<String, String> specialSymbolsMap = new HashMap<String, String>();

    static {
        init();
    }

    public static void callLoadModel() {
        try {
            tagger.loadModel("files/postaggermodelforenglish");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void init() {
        tagger = new Tagger();
        callLoadModel();
        tabooWords = readFile("files/step6/auxilaries.txt");
        usedConcepts = readFile("files/step6/usedConcepts.txt");

        specialSymbolsMap.put(":)", positive);
        specialSymbolsMap.put(":')", positive);
        specialSymbolsMap.put(":]", positive);
        specialSymbolsMap.put(":=)", positive);
        specialSymbolsMap.put(":-)", positive);
        specialSymbolsMap.put(";)", positive);
        specialSymbolsMap.put(";-)", positive);
        specialSymbolsMap.put("<3", positive);
        specialSymbolsMap.put(":D", positive);
        specialSymbolsMap.put(":d", positive);
        specialSymbolsMap.put(":-D", positive);
        specialSymbolsMap.put("-_-", positive);
        specialSymbolsMap.put("=D", positive);
        specialSymbolsMap.put(":-*", positive);
        specialSymbolsMap.put(":*", positive);
        specialSymbolsMap.put(":p", positive);
        specialSymbolsMap.put(":P", positive);
        specialSymbolsMap.put(":-p", positive);
        specialSymbolsMap.put(":-P", positive);
        specialSymbolsMap.put(";p", positive);
        specialSymbolsMap.put("[P", positive);
        specialSymbolsMap.put(";-p", positive);
        specialSymbolsMap.put(";-P", positive);
        specialSymbolsMap.put("\\m/", positive);
        specialSymbolsMap.put("o_v_oxo", positive);

        specialSymbolsMap.put(":/", negative);
        specialSymbolsMap.put(":-/", negative);
        specialSymbolsMap.put(">:o", negative);
        specialSymbolsMap.put(">:-o", negative);
        specialSymbolsMap.put(":-(", negative);
        specialSymbolsMap.put(":(", negative);
        specialSymbolsMap.put(":=(", negative);
        specialSymbolsMap.put(":[", negative);

    }

    public static String processToken(String token) {
        if (token == null)
            return null;
        token = token.toLowerCase();

        if (specialSymbolsMap.containsKey(token))
            return token;

        if (token.startsWith("http:") || token.startsWith("@") || token.equalsIgnoreCase(""))
            return null;

        StringBuilder sb = new StringBuilder();
        char[] letters = token.toCharArray();

        sb = new StringBuilder();
        boolean charActive = false;
        for (Character c : letters) {
            if (!isSymbol(c)) {
                sb.append(c);
                charActive = true;
            } else {
                if (charActive)
                    sb.append(" ");
                charActive = false;
            }
        }

        token = Utility.processForNumericValue(sb.toString().trim());
        return token;
    }


    public static boolean isSymbol(char c) {
        //following symbols will be ignored
        if (c == '"' || c == '?' || c == '.' || c == ',' || c == '!' || c == ':' || c == ';' || c == '-' || c == '(' || c == ')' || c == '/'
                || c == '!' || c == '=' || c == '$' || c == '%' || c == '|' || c == '[' || c == ']' || c == '*' || c == '+' || c == '_' || c == '<' || c == '>')
            return true;
        return false;
    }

    public static ArrayList<String> readFile(String name) {

        ArrayList<String> sentences = new ArrayList<String>();
        ;
        StringBuffer sentence = new StringBuffer();

        try {
            FileInputStream fileStream = new FileInputStream(name);
            DataInputStream inputStream = new DataInputStream(fileStream);
            BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            sentences.clear();

            while ((line = bufferdReader.readLine()) != null) {
                sentences.add(line);
            }
            inputStream.close();
        } catch (Exception e) {
            System.err.print("Could not read file specified:" + name);
            System.exit(1);
        }
        return sentences;
    }


    public static String getCurrentExecutionPath() {
        return System.getProperty("user.dir");
    }


    public static String removeExtraSpaces(String t) {
        if (t == null)
            return null;

        String words[] = t.split(space);

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            if (!words[i].equalsIgnoreCase(""))
                list.add(words[i]);
        }

        int size = list.size();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++)
            sb.append(list.get(i)).append(space);
        return sb.toString().trim();
    }


    public static String returnFirstNWords(String t, int n) {
        if (t == null)
            return null;

        String words[] = t.split(space);

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            if (!words[i].equalsIgnoreCase(""))
                list.add(words[i]);
        }

        int size = list.size();

        if (size < n)
            return null;
        else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++)
                sb.append(list.get(i)).append(space);
            return sb.toString().trim();
        }
    }


    public static int getHighestNumerIndex(float one, float two, float three) {
        if (one >= two) {
            if (one >= three)
                return 1;
            return 3;
        }
        if (two >= three)
            return 2;
        return 3;
    }


    public static ArrayList<TokenDetails> copyList(ArrayList<TokenDetails> src, ArrayList<TokenDetails> dest) {
        for (TokenDetails td : src)
            dest.add(td);
        return dest;
    }

    public static String processForNumericValue(String word) {
        String regex = "[0-9]+";
        String numberReplacement = "${Number}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(word);

        StringBuilder sb = new StringBuilder();
        char[] chars = word.toCharArray();
        int i = 0;
        boolean found = false;
        while (m.find()) {
            found = true;
            int start = m.start();
            int end = m.end();
            //System.out.println(m.group()+","+m.start()+","+m.end());
            while (i < start) {
                sb.append(chars[i]);
                i = i + 1;
            }
            i = end;
            sb.append(numberReplacement);
        }

        if (!found)
            return word;
        return sb.toString().trim();
    }


    public static void printList(ArrayList<TokenDetails> tdList, String fileName) {
        try {
            // Create file
            fileName = getCurrentExecutionPath() + "/" + fileName;
            System.out.print("\nPrinting to File:" + fileName);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            int size = tdList.size();
            for (int i = 0; i < size; i++) {
                TokenDetails td = tdList.get(i);
                if (i < size - 1)
                    out.write(td.returnTokenDetails() + "\n");
                else
                    out.write(td.returnTokenDetails());
            }
            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void printMap(HashMap<String, TokenDetails> map, int mode, String filename) {
        Iterator<Entry<String, TokenDetails>> it = map.entrySet().iterator();
        ArrayList<TokenDetails> tdList = new ArrayList<TokenDetails>();

        while (it.hasNext()) {
            Entry<String, TokenDetails> pair = (Entry<String, TokenDetails>) it.next();
            TokenDetails td = pair.getValue();
            System.out.print("\n" + td.getString());
            tdList.add(td);
        }

        if (mode == 1) {
            TokenComparator tc = new TokenComparator();
            Collections.sort(tdList, tc);
        } else if (mode == 2) {
            TokenComparatorCount tc = new TokenComparatorCount();
            Collections.sort(tdList, tc);
        }
        printList(tdList, filename);
    }

    public static HashMap<String, TokenDetails> convertMapToTokens(HashMap<String, Integer> map) {
        Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
        HashMap<String, TokenDetails> tokenMap = new HashMap<String, TokenDetails>();
        while (it.hasNext()) {
            Entry<String, Integer> pair = it.next();
            String token = pair.getKey();
            int count = pair.getValue();

            TokenDetails t = new TokenDetails();
            t.setCount(count);
            t.setString(token);
            tokenMap.put(token, t);
        }
        return tokenMap;
    }


    public static boolean containsSymbol(String s, String symbol) {
        Iterator<Entry<String, String>> it = specialSymbolsMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> pair = (Entry<String, String>) it.next();
            String key = pair.getKey();
            if (s.contains(key)) {
                String polarity = specialSymbolsMap.get(key);
                if (polarity.equalsIgnoreCase(symbol))
                    return true;
            }
        }
        return false;
    }


    public static boolean isVerb(String ngram) {
        List<TaggedToken> tokens = tagger.tokenizeAndTag(ngram);
        int size = tokens.size();
        if (size > 0) {
            for (TaggedToken token : tokens) {
                if (token.tag.equalsIgnoreCase("v"))
                    return true;
            }
        }
        return false;
    }


}
