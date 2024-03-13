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


    public String obstacleChecker(double height, double width, double distance) {
        if (height <= 0 && width <= 0 && distance < 0) {
            return "Invalid height, width and distance(values are negative)";
        } else if (height <= 0 && distance < 0){
            return "Invalid height and distance(values are negative)";
        } else if (height <= 0 && width <= 0){
            return "Invalid height and width(values are negative)";
        } else if (width <= 0 && distance < 0){
            return "Invalid width and distance(values are negative)";
        } else if (height <= 0) {
            return "Invalid height(values are negative)";
        } else if (width <= 0) {
            return "Invalid width(values are negative)";
        } else if (distance < 0) {
            return "Invalid distance(values are negative)";
        }
        return null; // No error
    }
}

