package uk.ac.soton.comp2211.runwayredeclaration.Component;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Pair;

import java.util.ArrayList;

public class Runway {

    private String designator;
    private double length;
    private double width;
    private double displacedThreshold;
    private double stopwayLength;
    private double clearwayLength;
    private double blastProtection;
    private SimpleDoubleProperty TORA;
    private SimpleDoubleProperty TODA;
    private SimpleDoubleProperty ASDA;
    private SimpleDoubleProperty LDA;
    private double RESA;
    private ArrayList< Pair<Obstacle, Double> > obstacleDistanceList;




    public Runway (String name){
        this.designator = name;
    }

//    public Runway (String name, double length, double width, double stopwayLength, double clearwayLength, double blastProtection){
//        this.designator = name;
//        this.length = length;
//        this.width = width;
//        this.displacedThreshold = 0;
//        this.stopwayLength = stopwayLength;
//        this.clearwayLength = clearwayLength;
//        this.TORA = new SimpleDoubleProperty(length);
//        this.TODA = new SimpleDoubleProperty(TORA.getValue() + clearwayLength) ;
//        this.blastProtection = blastProtection;
//
//    }

    public Runway (String name, double tora, double toda, double asda, double lda, int heading, double displacedThreshold, String position){
        this.designator = name;
        this.TORA = new SimpleDoubleProperty(tora);
        this.TODA = new SimpleDoubleProperty(toda);
        this.ASDA = new SimpleDoubleProperty(asda);
        this.LDA = new SimpleDoubleProperty(lda);
        this.displacedThreshold = displacedThreshold;
        this.obstacleDistanceList = new ArrayList<>();
        this.blastProtection = 300;
    }

    /**
     * Add an obstacle to the runway
     * @param obstacle The Obstacle object
     * @param distance The distance from the left start of the runway (not include stop way) to the obstacle
     */
    public void addObstacle(Obstacle obstacle, Double distance){
        obstacleDistanceList.add((new Pair<>(obstacle, distance)));
    }

    /**
     * Get an obstacle from the runway
     * @param obstacleName the name of the obstacle
     * @return the obstacle and the distance from the left start of the runway (not include stop way) to the obstacle
     */
    public Pair<Obstacle, Double> getObstacles(String obstacleName){
        for (Pair<Obstacle, Double> pair : obstacleDistanceList){
            if (pair.getKey().getName().equals(obstacleName)){
                return pair;
            }
        }

        return null;
    }


    /**
     * Set the designator of the runway
     * @param designator the name of the runway
     */
    public void setDesignator(String designator){
        this.designator = designator;
    }

    /**
     * Get the designator of the runway
     * @return the name of the runway
     */
    public String getDesignator(){
        return designator;
    }

    public void setLength(double length){
        this.length = length;
    }

    public double getLength(){
        return length;
    }

    public double getBlastProtection(){
        return this.blastProtection;
    }

    public void setOriginalTORA(double TORA){
        this.TORA.set(TORA);
    }

    public SimpleDoubleProperty getOriginalTORA(){
        return this.TORA;
    }

    public SimpleDoubleProperty getOriginalTODA(){
        return this.TODA;
    }

    public SimpleDoubleProperty getOriginalASDA(){
        return this.ASDA;
    }

    public SimpleDoubleProperty getOriginalLDA(){
        return this.LDA;
    }



    public void setWidth(double width){
        this.width = width;
    }

    public double getWidth(){
        return width;
    }

    public void setDisplacedThreshold(double displacedThreshold){
        this.displacedThreshold = displacedThreshold;
    }

    public double getDisplacedThreshold(){
        return displacedThreshold;
    }

    public void setStopwayLength(double stopwayLength){
        this.stopwayLength = stopwayLength;
    }

    public double getStopwayLength(){
        return stopwayLength;
    }

    public void setClearwayLength(double clearwayLength){
        this.clearwayLength = clearwayLength;
    }

    public double getClearwayLength(){
        return clearwayLength;
    }


}
