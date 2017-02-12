package _3_probabilty;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 30.4.2016.
 */
public class FileWalker {

    public static String INPUT_FILE = "files/input/AllTweets.txt";
    List<String> stringList = new ArrayList<String>();


    public FileWalker() {

        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(INPUT_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                stringList.add(strLine);
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Prob calculateProbabilty(String sentence) {

        Double totalOccurence = 0.00000000001;
        Double totalSarcasticOccurence = 0.0000000000001;

        for (int i = 0; i < stringList.size(); i++) {
            String strLine = stringList.get(i);
            if (strLine.toLowerCase().contains(sentence.toLowerCase())) {
                totalOccurence++;
                if (strLine.toLowerCase().contains("sarcasm")) {
                    totalSarcasticOccurence++;
                }
            }
        }

        Double probabilty = totalSarcasticOccurence / totalOccurence;
      //  System.out.println(totalSarcasticOccurence+ " - "+ totalOccurence);
        return new Prob(totalOccurence,probabilty);
    }
}
