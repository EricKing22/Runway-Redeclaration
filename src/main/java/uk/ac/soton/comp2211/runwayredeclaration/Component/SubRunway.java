package uk.ac.soton.comp2211.runwayredeclaration.Component;



import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.print.SimpleDoc;
import java.util.ArrayList;

public class SubRunway {
    public SimpleStringProperty designator = new SimpleStringProperty();




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
        this.designator.set(designator);

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

    public SubRunway (SubRunway new_subRunway){
        this.designator.set(new_subRunway.getDesignator().get());
        this.oldParameters = new ArrayList<>();
        this.newParameters = new ArrayList<>();

        this.originalTORA.set(new_subRunway.getOriginalTORA().get());
        this.originalTODA.set(new_subRunway.getOriginalTODA().get());
        this.originalASDA.set(new_subRunway.getOriginalASDA().get());
        this.originalLDA.set(new_subRunway.getOriginalLDA().get());

        this.TORA.set(new_subRunway.getTORA().get());
        this.TODA.set(new_subRunway.getTODA().get());
        this.ASDA.set(new_subRunway.getASDA().get());
        this.LDA.set(new_subRunway.getLDA().get());
        this.clearwayLength.set(new_subRunway.getClearwayLength().get());
        this.stopwayLength.set(new_subRunway.getStopwayLength().get());
        this.displacedThreshold.set(new_subRunway.getDisplacedThreshold().get());
        this.stripEndLength.set(new_subRunway.getStripEndLength().get());
        this.blastProtection.set(new_subRunway.getBlastProtection().get());
    }


    public void update (SubRunway new_subRunway){
        this.designator.set(new_subRunway.getDesignator().get());
        this.oldParameters = new ArrayList<>();
        this.newParameters = new ArrayList<>();

        this.originalTORA.set(new_subRunway.getOriginalTORA().get());
        this.originalTODA.set(new_subRunway.getOriginalTODA().get());
        this.originalASDA.set(new_subRunway.getOriginalASDA().get());
        this.originalLDA.set(new_subRunway.getOriginalLDA().get());

        this.TORA.set(new_subRunway.getTORA().get());
        this.TODA.set(new_subRunway.getTODA().get());
        this.ASDA.set(new_subRunway.getASDA().get());
        this.LDA.set(new_subRunway.getLDA().get());
        this.clearwayLength.set(new_subRunway.getClearwayLength().get());
        this.stopwayLength.set(new_subRunway.getStopwayLength().get());
        this.displacedThreshold.set(new_subRunway.getDisplacedThreshold().get());
        this.stripEndLength.set(new_subRunway.getStripEndLength().get());
        this.blastProtection.set(new_subRunway.getBlastProtection().get());
    }


    /**
     * Get the designator of the runway
     * @return the name of the runway
     */
    public SimpleStringProperty getDesignator(){
        return designator;
    }

    public String returnDesignator() {
        return designator.getValue();
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
