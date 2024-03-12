package uk.ac.soton.comp2211.runwayredeclaration.Component;



import javafx.beans.property.SimpleDoubleProperty;

import javax.print.SimpleDoc;
import java.util.ArrayList;

public class SubRunway {
    public final String designator;




    public ArrayList<Double> oldParameters;
    public ArrayList<Double> newParameters;

    public SimpleDoubleProperty originalTORA = new SimpleDoubleProperty();
    public SimpleDoubleProperty originalTODA = new SimpleDoubleProperty();
    public SimpleDoubleProperty originalASDA = new SimpleDoubleProperty();
    public SimpleDoubleProperty originalLDA = new SimpleDoubleProperty();


    public SimpleDoubleProperty TORA = new SimpleDoubleProperty();
    public SimpleDoubleProperty TODA = new SimpleDoubleProperty();
    public SimpleDoubleProperty ASDA = new SimpleDoubleProperty();
    public SimpleDoubleProperty LDA = new SimpleDoubleProperty();
    public SimpleDoubleProperty RESA = new SimpleDoubleProperty();
    public SimpleDoubleProperty clearwayLength = new SimpleDoubleProperty();
    public SimpleDoubleProperty stopwayLength = new SimpleDoubleProperty();
    public SimpleDoubleProperty stripEndLength = new SimpleDoubleProperty();
    public SimpleDoubleProperty blastProtection = new SimpleDoubleProperty();
    public SimpleDoubleProperty displacedThreshold = new SimpleDoubleProperty();
    public Obstacle obstacle;
    // Distance from the left start of the runway (not include stop way) to the obstacle
    public double obstacleDistance;



    public SubRunway(String designator, double tora, double toda, double asda, double lda, double displacedThreshold, double clearwayLength, double stopwayLength, double stripEndLength, double blastProtection) {
        this.designator = designator;

        this.oldParameters = new ArrayList<>();
        this.newParameters = new ArrayList<>();

        this.originalTORA.set(tora);
        this.originalTODA.set(toda);
        this.originalASDA.set(asda);
        this.originalLDA.set(lda);

        this.TORA.set(tora);
        this.TODA.set(toda);
        this.ASDA.set(asda);
        this.LDA.set(lda);
        this.clearwayLength.set(clearwayLength);
        this.stopwayLength.set(stopwayLength);


        this.displacedThreshold.set(displacedThreshold);

        this.stripEndLength.set(stripEndLength);
        this.blastProtection.set(blastProtection);

    }



    /**
     * Get the designator of the runway
     * @return the name of the runway
     */
    public String getDesignator(){
        return designator;
    }

    public SimpleDoubleProperty getOriginalTORA(){
        return this.TORA;
    }

    public SimpleDoubleProperty getOriginalTODA(){
        return this.originalTODA;
    }

    public SimpleDoubleProperty getOriginalASDA(){
        return this.originalASDA;
    }

    public SimpleDoubleProperty getOriginalLDA(){
        return this.originalLDA;
    }

    //Recalculated values
    public SimpleDoubleProperty getTORA(){
        return this.TORA;
    }

    public SimpleDoubleProperty getTODA(){
        return this.TODA;
    }

    public SimpleDoubleProperty getASDA(){
        return this.ASDA;
    }

    public SimpleDoubleProperty getLDA(){
        return this.LDA;
    }

    public SimpleDoubleProperty getStripEndLength(){
        return this.stripEndLength;
    }
    public SimpleDoubleProperty getBlastProtection(){
        return this.blastProtection;
    }

    public SimpleDoubleProperty getDisplacedThreshold(){
        return this.displacedThreshold;
    }

    public SimpleDoubleProperty getRESA(){
        return this.RESA;
    }

    public SimpleDoubleProperty getClearwayLength(){
        return this.clearwayLength;
    }

    public SimpleDoubleProperty getStopwayLength(){
        return this.stopwayLength;
    }

    public void setObstacle(Obstacle obstacle, double distance){
        this.obstacle = obstacle;
        this.obstacleDistance = distance;
    }

    public Obstacle getObstacle(){
        return this.obstacle;
    }

    public double getObstacleDistance(){
        return this.obstacleDistance;
    }

}
