package uk.ac.soton.comp2211.runwayredeclaration.Component;

public class Checkers {

    private double height;
    private double width;
    private double distance;

    public Checkers(double height, double width, double distance){
        this.height = height;
        this.width = width;
        this.distance = distance;
    }


    public boolean obstacleChecker(double height, double width, double length){
        if (height < 0 || width < 0 || length < 0){
            return false;
        }
        return true;
    }
}

