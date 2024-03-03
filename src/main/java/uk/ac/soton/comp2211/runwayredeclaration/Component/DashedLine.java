package uk.ac.soton.comp2211.runwayredeclaration.Component;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DashedLine extends Rectangle {
    public DashedLine(double width, double height) {
        super(width, height);
        setFill(Color.TRANSPARENT);
        setStroke(Color.web("#4472C4"));
        setStrokeWidth(2);
        getStrokeDashArray().addAll(10d, 5d);
    }

}
