package uk.ac.soton.comp2211.runwayredeclaration.Component;


import java.util.ArrayList;

public class Runway {

    private String name;

    private Obstacle obstacle;
    private ArrayList<SubRunway> subRunways;


    public Runway (String name){
        this.name = name;
        this.subRunways = new ArrayList<>();
    }


    /**
     * Add a sub runway to the runway
     * @param subRunway the subRunway to be added
     */
    public void addSubRunway(SubRunway subRunway){
        subRunways.add(subRunway);
    }

    /**
     * Get the sub runways of the runway
     * @return subRunways the list of stored subRunways
     */
    public ArrayList<SubRunway> getSubRunways(){
        return subRunways;
    }

    /**
     * Add a sub runway to the runway
     * @param obstacle the obstacle to be added
     * @param distance the distance from the start of the runway to the obstacle
     */
    public void addObstacle(Obstacle obstacle, double distance){
        this.obstacle = obstacle;
        for (SubRunway subRunway : subRunways) {
            subRunway.setObstacle(obstacle, distance);
        }
    }

    /**
     * Get the obstacle of the runway
     * @return the obstacle on the runway
     */
    public Obstacle getObstacle(){
        return obstacle;
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


}
