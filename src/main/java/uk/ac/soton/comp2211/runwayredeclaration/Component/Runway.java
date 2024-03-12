package uk.ac.soton.comp2211.runwayredeclaration.Component;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Pair;

import java.util.ArrayList;

public class Runway {

    private String name;
    private double length;
    private double width;

    private ArrayList< Pair<Obstacle, Double> > obstacleDistanceList;
    private ArrayList<SubRunway> subRunways;


    public Runway (String name){
        this.name = name;
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
     * @param name the name of the runway
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Get the designator of the runway
     * @return the name of the runway
     */
    public String getName(){
        return name;
    }

    public void setLength(double length){
        this.length = length;
    }

    public double getLength(){
        return length;
    }


    public void setWidth(double width){
        this.width = width;
    }

    public double getWidth(){
        return width;
    }



}
