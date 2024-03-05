package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Airport;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Runway;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Obstacle;


/**
 * A Base Scene used in the game. Handles common functionality between all scenes.
 */
public abstract class BaseScene {

    protected final HomeWindow homeWindow;

    protected HomePane root;
    protected Scene scene;

    protected Runway currentRunway1;
    protected Runway currentRunway2;
    protected Obstacle currentObstacle;

    protected DoubleProperty runwayLength = new SimpleDoubleProperty();
    protected DoubleProperty stopWayLength = new SimpleDoubleProperty();
    protected DoubleProperty clearWayLength = new SimpleDoubleProperty();
    protected DoubleProperty displayStopWayLength = new SimpleDoubleProperty(50); // Need to be rescaled
    protected DoubleProperty displayClearWayLength = new SimpleDoubleProperty(80); // Need to be rescaled
    protected DoubleProperty displayRunwayLength = new SimpleDoubleProperty(650); // FIXED


    protected DoubleProperty distBetweenPlaneObstacle = new SimpleDoubleProperty(10);


    // Take Off Parameters
    protected DoubleProperty TORA = new SimpleDoubleProperty();
    protected DoubleProperty displayTORA = new SimpleDoubleProperty(displayRunwayLength.getValue() - 2 * displayStopWayLength.getValue());
    protected DoubleProperty TODA = new SimpleDoubleProperty();
    protected DoubleProperty displayTODA = new SimpleDoubleProperty(displayRunwayLength.getValue() - 2 * displayStopWayLength.getValue() + displayClearWayLength.getValue() - 2);
    protected DoubleProperty ASDA = new SimpleDoubleProperty();
    protected DoubleProperty displayASDA = new SimpleDoubleProperty(displayRunwayLength.getValue() - 2 * displayStopWayLength.getValue() + displayStopWayLength.getValue() - 2);


    protected DoubleProperty LDA = new SimpleDoubleProperty();
    protected DoubleProperty displayLDA = new SimpleDoubleProperty(displayRunwayLength.getValue() - 2 * displayStopWayLength.getValue());

    protected DoubleProperty displayBorderToRunway = new SimpleDoubleProperty(75);


    protected StackPane takeOffIndicators;
    protected HBox toraBox;
    protected HBox todaBox;
    protected HBox ldaBox;
    protected HBox asdaBox;


    public double calculateThreshold(){
        return currentObstacle.getPositionOnRunway() - currentRunway1.getDisplacedThreshold();
    }

    public double calculateTORA(){
        return currentRunway1.getOriginalTORA().get() - currentRunway1.getBlastProtection() - calculateThreshold() - currentRunway1.getDisplacedThreshold();
    }

    public double calculateASDA(){
       return calculateTORA() + currentRunway1.getStopwayLength();
    }

    public double calculateTODA(){
       return calculateTORA() + currentRunway1.getClearwayLength();
    }

//    private static double calculateLDA() {
//        return originalLDA - distanceFromThreshold - stripEnd - (currentObstacle.getHeight() * distanceFromThresholdForLDA);
//    }


    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     * @param homeWindow the home window
     */
    public BaseScene(HomeWindow homeWindow) {
        this.homeWindow = homeWindow;


        // For demo
        this.currentRunway1 = new Runway("09L27R", 3902.0, 3902.0, 3902.0, 3595.0, 9, 0.0, "L");
        this.currentRunway2 = new Runway("09L27R", 3884.0, 3862.0, 3884.0, 3884.0, 27, 0.0, "R");
    }

    /**
     * Initialise this scene. Called after creation
     */
    public abstract void initialise();

    /**
     * Build the layout of the scene
     */
    public abstract void build();


    /**
     * Create the results Titled Pane, This is the top box in the left box
     * @return TitledPane the results Titled Pane
     */
    public TitledPane makeResultsTPane(){
        TitledPane tpane = new TitledPane();
        tpane.setText("Results:");
        tpane.setCollapsible(true);



        GridPane gpanething = new GridPane();
        gpanething.setPadding(new Insets(5,0,5,0));
        gpanething.setHgap(2);
        gpanething.setVgap(10);
        gpanething.getStyleClass().add("gridPane");
        String[] headers = {"Runway", "TORA", "TODA", "LDA", "ASDA"};

        // Set column constraints for each header, ensuring they grow to fill space and content is centered
        for (int i = 0; i < headers.length; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS); // Allow column to grow
            columnConstraints.setHalignment(HPos.CENTER); // Center align content
            gpanething.getColumnConstraints().add(columnConstraints); // Add constraints to column
        }

        for (int i = 0; i < headers.length; i++) {
            Label label = new Label(headers[i]);
            label.getStyleClass().add("header-label");
            gpanething.add(label, i, 0);
            GridPane.setHalignment(label, HPos.CENTER); // For horizontal alignment
            GridPane.setValignment(label, VPos.CENTER); // For vertical alignment
        }

        Label lblOriginal = new Label("Original Values");
        lblOriginal.setMaxWidth(Double.MAX_VALUE);
        lblOriginal.getStyleClass().add("center-label");
        GridPane.setColumnSpan(lblOriginal, GridPane.REMAINING);
        GridPane.setHalignment(lblOriginal, HPos.CENTER);
        gpanething.add(lblOriginal, 0, 1);

        // Original Values:
        Label runway1 = new Label("09L");
        runway1.getStyleClass().add("grid-pane-label");
        gpanething.add(runway1, 0, 2);

        Label tora_original = new Label();
        tora_original.textProperty().bind(currentRunway1.getOriginalTORA().asString());
        tora_original.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_original, 1, 2);

        Label toda_original = new Label();
        toda_original.textProperty().bind(currentRunway1.getOriginalTODA().asString());
        toda_original.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_original, 2, 2);

        Label lda_original = new Label();
        lda_original.textProperty().bind(currentRunway1.getOriginalLDA().asString());
        lda_original.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_original, 3, 2);

        Label asda_original = new Label();
        asda_original.textProperty().bind(currentRunway1.getOriginalASDA().asString());
        asda_original.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_original, 4, 2);


        Label runway2 = new Label("27R");
        runway2.getStyleClass().add("grid-pane-label");
        gpanething.add(runway2, 0, 3);

        Label tora_new = new Label();
        tora_new.textProperty().bind(currentRunway2.getOriginalTORA().asString());
        tora_new.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_new, 1, 3);

        Label toda_new = new Label();
        toda_new.textProperty().bind(currentRunway2.getOriginalTODA().asString());
        toda_new.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_new, 2, 3);

        Label lda_new = new Label();
        lda_new.textProperty().bind(currentRunway2.getOriginalLDA().asString());
        lda_new.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_new, 3, 3);

        Label asda_new = new Label();
        asda_new.textProperty().bind(currentRunway2.getOriginalASDA().asString());
        asda_new.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_new, 4, 3);






//        for (int row = 2; row < 4; row++) {
//            for (int col = 0; col < headers.length; col++) {
//                Label label = new Label("1000m");
//                label.getStyleClass().add("grid-pane-label");
//                GridPane.setHalignment(label, HPos.CENTER); // For horizontal alignment
//                GridPane.setValignment(label, VPos.CENTER); // For vertical alignment
//                gpanething.add(label, col, row);
//            }
//        }

        Label lblRecalculated = new Label("New Values");
        lblRecalculated.setMaxWidth(Double.MAX_VALUE);
        lblRecalculated.getStyleClass().add("center-label");
        GridPane.setColumnSpan(lblRecalculated, GridPane.REMAINING);
        GridPane.setHalignment(lblRecalculated, HPos.CENTER);
        gpanething.add(lblRecalculated, 0, 4);

        // Adding two rows of arbitrary values after "Re-Calculated Values"
        for (int row = 5; row < 7; row++) {
            for (int col = 0; col < headers.length; col++) {
                Label label = new Label("xxxx.0");
                label.getStyleClass().add("grid-pane-label");
                GridPane.setHalignment(label, HPos.CENTER); // For horizontal alignment
                GridPane.setValignment(label, VPos.CENTER); // For vertical alignment
                gpanething.add(label, col, row);
            }
        }


        tpane.setContent(gpanething);
        return tpane;
    }

    /**
     * Create the calculation breakdown Titled Pane, This is the bottom box in the left box
     * @return TitledPane the calculation breakdown Titled Pane
     */
    public TitledPane makeCalcBreakTPane(){
        TitledPane tpane2 = new TitledPane();
        tpane2.setText("Calculation Breakdown:");
        tpane2.setCollapsible(true);

        TextArea displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setPrefHeight(600);

        // Create Buttons
        Button buttonTORA = new Button("TORA");
        Button buttonTODA = new Button("TODA");
        Button buttonLDA = new Button("LDA");
        Button buttonASDA = new Button("ASDA");

        // Apply the normal style to all buttons initially
        buttonTORA.getStyleClass().add("button");
        buttonTODA.getStyleClass().add("button");
        buttonLDA.getStyleClass().add("button");
        buttonASDA.getStyleClass().add("button");

        // Button Actions
        Button[] allButtons = new Button[]{buttonTORA, buttonTODA, buttonLDA, buttonASDA};

        // Button Actions
        for (Button button : allButtons) {
            button.setOnAction(e -> {
                updateButtonStyles(button, allButtons, displayArea);
            });
        }
        HBox layout = new HBox(5);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(buttonTORA, buttonTODA, buttonLDA, buttonASDA);

        VBox vbox = new VBox(layout, displayArea);
        tpane2.setContent(vbox);

        return tpane2;
    }

    private void updateButtonStyles(Button selectedButton, Button[] allButtons, TextArea displayArea) {
        boolean isSelected = selectedButton.getStyleClass().contains("button-selected");
        for (Button btn : allButtons) {
            btn.getStyleClass().remove("button-selected");
        }

        // If the selected button was not already highlighted, highlight it


        if (!isSelected) {
            selectedButton.getStyleClass().add("button-selected");
            displayArea.setText("Content for " + selectedButton.getText());

            if (this instanceof SimultaneousScene){
                return;
            }


            if (selectedButton.getText().equals("TORA")){
                takeOffIndicators.getChildren().clear();
                takeOffIndicators.getChildren().add(toraBox);
            }
            else if (selectedButton.getText().equals("TODA")){
                takeOffIndicators.getChildren().clear();
                takeOffIndicators.getChildren().add(todaBox);
            }
            else if (selectedButton.getText().equals("ASDA")){
                takeOffIndicators.getChildren().clear();
                takeOffIndicators.getChildren().add(asdaBox);
            }
            else {
                takeOffIndicators.getChildren().clear();
            }
            //else if()


        } else {
            displayArea.clear();
            if (this instanceof SimultaneousScene){
                return;
            }
            // If it was already selected, clear the text area as the button is deselected
            takeOffIndicators.getChildren().clear();
            takeOffIndicators.getChildren().add(asdaBox);
            takeOffIndicators.getChildren().add(todaBox);
            takeOffIndicators.getChildren().add(toraBox);

        }

    }




    /**
     * Create the airport Titled Pane, This is the top box in the right box
     * @return TitledPane the airport Titled Pane
     */
    public TitledPane makeAirportTPane(){
        GridPane airportGrid = new GridPane();
        airportGrid.getStyleClass().add("full-width-grid");

        airportGrid.setVgap(5);
        airportGrid.setHgap(25);

        TitledPane airportTPane = new TitledPane();
        airportTPane.setText("Airport:");
        airportTPane.setCollapsible(true);


        ComboBox<String> airports = new ComboBox<>();
        airports.getItems().addAll("Heathrow (LHR)", "Gatwick (LGW)", "Luton (LTN)", "Stansted (STN)", "City (LCY)");
        airports.setValue("Heathrow (LHR)");

        ComboBox<String> runways = new ComboBox<>();
        runways.getItems().addAll("27R/09L");
        runways.setValue("27R/09L");


        airportGrid.addRow(0, new Label("Airport:"), airports);
        airportGrid.addRow(1, new Label("Runway:"), runways);
        airportGrid.addRow(2, new Label("Length:"),  new Text("xxxx.0m"));

        airportGrid.addRow(3, new Label("Threshold:"), new Text("xxxx.0m"));
        airportGrid.addRow(4, new Label("Clearway:"), new Text("xxxx.0m"));
        airportGrid.addRow(5, new Label("Stopway:"), new Text("xxxx.0m"));

        airportTPane.setContent(airportGrid);


        return airportTPane;
    }

    /**
     * Create the obstacle Titled Pane, This is the bottom box in the right box
     * @return TitledPane the obstacle Titled Pane
     */
    public TitledPane makeObstacleTPane(){
        GridPane obstacleGrid = new GridPane();
        obstacleGrid.setVgap(5);
        obstacleGrid.setHgap(10);

        TitledPane obstacleTPane = new TitledPane();
        obstacleTPane.setText("Obstacle:");
        obstacleTPane.setCollapsible(true);

        ComboBox<String> obstacle = new ComboBox<>();
        obstacle.getItems().addAll("XXXX Plane", "Car", "Boxes");
        obstacle.setValue("XXXX Plane");

        VBox obstacleBox = new VBox(5);


        //obstacleBox.getChildren().add((new Label("Obstacle:"), obstacle);
        //obstacleBox.getChildren().add(obstacle);
        obstacleBox.getChildren().addAll(new Label("Height (metres):"), new TextField());
        obstacleBox.getChildren().addAll(new Label("Width (metres):"), new TextField());
        obstacleBox.getChildren().addAll(new Label("Distance from left stopway (metres):"), new TextField());
        //obstacleBox.getChildren().addAll(new Label("Distance from centerline (metres):"), new TextField());

        HBox buttons = new HBox(5);
        buttons.getChildren().add(new Button("Edit"));
        buttons.getChildren().add(new Button("Add"));
        obstacleBox.getChildren().add(buttons);



        Button recalculateB = new Button("Recalculate");
        recalculateB.setMaxWidth(Double.MAX_VALUE);

        HBox something = new HBox(30, new Label("Obstacle: "), obstacle);

        obstacleGrid.addRow(0, something);
        obstacleGrid.addRow(1, obstacleBox);
        obstacleGrid.addRow(2, buttons);
        obstacleGrid.addRow(3, recalculateB);

        obstacleTPane.setContent(obstacleGrid);

        return obstacleTPane;
    }

    /**
     * Create a new JavaFX scene using the root contained within this scene
     * @return JavaFX scene
     */
    public Scene setScene() {
        var previous = homeWindow.getScene();
        Scene scene = new Scene(root, previous.getWidth(), previous.getHeight(), Color.BLACK);
        scene.getStylesheets().add(getClass().getResource("/style/all.css").toExternalForm());
        this.scene = scene;
        return scene;
    }

    /**
     * Get the JavaFX scene contained inside
     * @return JavaFX scene
     */
    public Scene getScene() {
        return this.scene;
    }

}
