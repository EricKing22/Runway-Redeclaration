package uk.ac.soton.comp2211.runwayredeclaration.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomePane extends StackPane {

    private static final Logger logger = LogManager.getLogger(HomePane.class);

    private final int width;
    private final int height;
    private double scalar = 1;
    private final boolean autoScale = true;

    /**
     * Create a new scalable GamePane with the given drawing width and height.
     * @param width width
     * @param height height
     */
    public HomePane(int width, int height) {
        super();
        this.width = width;
        this.height = height;

        getStyleClass().add("pane");
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Update the scalar being used by this draw pane
     * @param scalar scalar
     */
    protected void setScalar(double scalar) {
        this.scalar = scalar;
    }

    /**
     * Use a Graphics Transformation to scale everything inside this pane. Padding is added to the edges to maintain
     * the correct aspect ratio and keep the display centred.
     */
    @Override
    public void layoutChildren() {
        super.layoutChildren();

        if(!autoScale) {
            return;
        }

        //Work out the scale factor height and width
        var scaleFactorHeight = getHeight() / height;
        var scaleFactorWidth = getWidth() / width;

        //Work out whether to scale by width or height
        if (scaleFactorHeight > scaleFactorWidth) {
            setScalar(scaleFactorWidth);
        } else {
            setScalar(scaleFactorHeight);
        }

        //Set up the scale
        Scale scale = new Scale(scalar,scalar);

        //Get the parent width and height
        var parentWidth = getWidth();
        var parentHeight = getHeight();

        //Get the padding needed on the top and left
        var paddingLeft = (parentWidth - (width * scalar)) / 2.0;
        var paddingTop = (parentHeight - (height * scalar)) / 2.0;

        //Perform the transformation
        Translate translate = new Translate(paddingLeft, paddingTop);
        scale.setPivotX(0);
        scale.setPivotY(0);
        getTransforms().setAll(translate, scale);
    }


}
