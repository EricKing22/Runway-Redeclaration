package uk.ac.soton.comp2211.runwayredeclaration.Component;
import javafx.scene.control.Alert;
import java.sql.SQLException;

public class Obstacle {

    private String name;

    private double height;
    private double width;
    private double length;


    public Obstacle(String name){
        this.name = name;
    }
    public Obstacle(String name, double height, double width, double length){
        this.name = name;
        this.height = height;
        this.width = width;
        this.length = length;

    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }



    public void setHeight(double height){
        this.height = height;
    }

    public double getHeight(){
        return height;
    }

    public void setWidth(double width){
        this.width = width;
    }

    public double getWidth(){
        return width;
    }

    public void setLength(double length){
        this.length = length;
    }

    public double getLength(){
        return length;
    }

    public String toString(){
        return name;
    }


}
