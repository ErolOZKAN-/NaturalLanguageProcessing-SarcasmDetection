package _4_classify;

/**
 * Created by Erol Özkan on 15-May-16.
 */
class Tweet {

    private String tweetText;
    private boolean isSarcastic;

    /**
     * returns tweet text
     * @return
     */
    public String getTweetText() {
        return tweetText;
    }

    /**
     * sets tweet text
     * @param tweetText
     */
    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    /**
     * return true if tweet is sarcastic
     * @return
     */
    public boolean isSarcastic() {
        return isSarcastic;
    }

    /**
     * sets tweet as sarcastic
     * @param isSarcastic
     */
    public void setSarcastic(boolean isSarcastic) {
        this.isSarcastic = isSarcastic;
    }
}
