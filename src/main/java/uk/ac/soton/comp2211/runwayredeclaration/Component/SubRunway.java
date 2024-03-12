package uk.ac.soton.comp2211.runwayredeclaration.Component;



import java.util.ArrayList;

public class SubRunway {
    public final String designator;

    public double displacedThreshold;


    public ArrayList<Double> oldParameters;
    public ArrayList<Double> newParameters;

    public double originalTORA;
    public double originalTODA;
    public double originalASDA;
    public double originalLDA;


    public double TORA;
    public double TODA;
    public double ASDA;
    public double LDA;
    public double RESA;
    public double clearwayLength;
    public double stopwayLength;
    public double stripEndLength;
    public double blastProtection;



    public SubRunway(String designator, double tora, double toda, double asda, double lda, double displacedThreshold, double clearwayLength, double stopwayLength, double stripEndLength, double blastProtection) {
        this.designator = designator;

        this.oldParameters = new ArrayList<>();
        this.newParameters = new ArrayList<>();

        this.originalTORA = tora;
        this.originalTODA = toda;
        this.originalASDA = asda;
        this.originalLDA = lda;

        this.TORA = tora;
        this.TODA = toda;
        this.ASDA = asda;
        this.LDA = lda;
        this.displacedThreshold = displacedThreshold;

        this.stripEndLength = stripEndLength;
        this.blastProtection = blastProtection;

    }



    /**
     * Get the designator of the runway
     * @return the name of the runway
     */
    public String getDesignator(){
        return designator;
    }

    public void setOriginalTORA(double TORA){
        this.TORA = TORA;
    }

    public double getOriginalTORA(){
        return this.TORA;
    }

    public double getOriginalTODA(){
        return this.originalTODA;
    }

    public double getOriginalASDA(){
        return this.originalASDA;
    }

    public double getOriginalLDA(){
        return this.originalLDA;
    }

    public double getBlastProtection(){
        return this.blastProtection;
    }

    public double getDisplacedThreshold(){
        return this.displacedThreshold;
    }

    public double getRESA(){
        return this.RESA;
    }

    public double getClearwayLength(){
        return this.clearwayLength;
    }

    public double getStopwayLength(){
        return this.stopwayLength;
    }

}
