package _1_tweetfilter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 17.4.2016.
 */
public class PosPattern {
    /**
     * applies rule 1
     * @param tags
     * @return
     */
    public boolean isTweetGoodRule1(String tags)
    {
        ArrayList<String> regexList = new ArrayList<String>();
        regexList.add("V,*#*.*"); // for Noun
        return ruleMatcher(tags, regexList);
    }

    /**
     * applies rule 2
     * it will accept gerunds and adjective/past participle
     * @param tags
     * @return
     */
    public boolean isTweetGoodRule2(String tags)
    {
        ArrayList<String> regexList = new ArrayList<String>();
        regexList.add("VV,*#*.*"); // for gerunds past participle. extra Rules in corresponding functions.
        return ruleMatcher(tags, regexList);
    }

    /**
     * applies rule 3
     * @param tags
     * @return
     */
    public boolean isTweetGoodRule3(String tags)
    {
        ArrayList<String> regexList = new ArrayList<String>();
        regexList.add("N,*.*"); // for Noun
        //regexList.add("DN,*#*"); // for Det Noun
        return ruleMatcher(tags, regexList);
    }

    /**
     * applies rule 4
     * @param tags
     * @return
     */
    public boolean isTweetGoodRule4(String tags)
    {
        ArrayList<String> regexList = new ArrayList<String>();
        regexList.add("VA.*"); // for Noun
        return ruleMatcher(tags, regexList);
    }

    /**
     * applies rule 5
     * @param tags
     * @return
     */
    public boolean isTweetGoodRule5(String tags)
    {
        ArrayList<String> regexList = new ArrayList<String>();
        regexList.add("VN.*"); // for Noun
        //regexList.add("DN,*#*"); // for Det Noun
        return ruleMatcher(tags, regexList);
    }

    /**
     * applies rule 6
     * @param tags
     * @return
     */
    public boolean isTweetGoodRule6(String tags)
    {
        ArrayList<String> regexList = new ArrayList<String>();
        regexList.add("VP*D*N.*"); // for Noun
        return ruleMatcher(tags, regexList);
    }

    /**
     * matches rules to strings given a regex in rules
     * @param tags
     * @param regexList
     * @return
     */
    public boolean ruleMatcher(String tags, ArrayList<String> regexList)
    {
        tags = splitAndCombine(tags);
        for(String regex : regexList)
        {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(tags);
            //System.out.println(m.matches());
            if(m.matches())
                return true;
        }
        return false;
    }

    /**
     * deletes blackspaces in string
     * @param tagString
     * @return
     */
    public String splitAndCombine(String tagString)
    {
        String [] tags = tagString.split(" ");
        StringBuilder sb = new StringBuilder();
        for(String tag: tags)
            sb.append(tag);
        return sb.toString().trim();
    }
}
