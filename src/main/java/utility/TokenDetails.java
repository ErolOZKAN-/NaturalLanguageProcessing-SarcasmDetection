package utility;

/**
 * Created by user on 17.4.2016.
 */
public class TokenDetails {

    String string;
    int count;
    float prob;
    float importanceScore;

    /**
     * returns importance score
     */
    public float getImportanceScore() {
        return importanceScore;
    }

    /**
     * sets importance score
     */
    public void setImportanceScore(float importanceScore) {
        this.importanceScore = importanceScore;
    }

    /**
     * returns string
     */
    public String getString() {
        return string;
    }

    /**
     * sets string
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * returns count
     */

    public int getCount() {
        return count;
    }

    /**
     * sets count
     */

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * returns probabilty
     */

    public float getProb() {
        return prob;
    }

    /**
     * sets prob
     */
    public void setProb(float prob) {
        this.prob = prob;
    }

    public boolean equals(Object o) {
        if (o instanceof TokenDetails && ((TokenDetails) o).getString().equalsIgnoreCase(this.getString()))
            return true;
        return false;
    }


    public void printTokenDetails() {
        System.out.print("\nStrng:" + this.getString() + " Count:" + this.getCount()
                + " Probability:" + this.getProb());

    }

    public String returnTokenDetails() {
        return ("Strng:" + this.getString() + Utility.delim + " Count:" + this.getCount()
                + Utility.delim + " Probability:" + this.getProb());
    }



    public int hashCode() {
        return this.getCount() * 31;
    }



}
