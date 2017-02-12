package _3_probabilty;

/**
 * Created by Erol Özkan on 30-May-16.
 */
public class Prob {

   private Double totalOccurence;
    private  Double probabilty;

    public Prob(Double totalOccurence, Double probabilty) {
        this.totalOccurence = totalOccurence;
        this.probabilty = probabilty;
    }

    public Double getTotalOccurence() {
        return totalOccurence;
    }

    public void setTotalOccurence(Double totalOccurence) {
        this.totalOccurence = totalOccurence;
    }

    public Double getProbabilty() {
        return probabilty;
    }

    public void setProbabilty(Double probabilty) {
        this.probabilty = probabilty;
    }
}
