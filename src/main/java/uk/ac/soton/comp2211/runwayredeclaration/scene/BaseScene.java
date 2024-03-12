package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Airport;
import uk.ac.soton.comp2211.runwayredeclaration.Component.SubRunway;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Runway;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Obstacle;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A Base Scene used in the game. Handles common functionality between all scenes.
 */
public abstract class BaseScene {

    protected final HomeWindow homeWindow;

    protected HomePane root;
    protected Scene scene;

    protected SubRunway subRunway1;
    protected SubRunway subRunway2;
    protected Obstacle currentObstacle;

    protected DoubleProperty runwayLength = new SimpleDoubleProperty();
    protected DoubleProperty stopWayLength = new SimpleDoubleProperty();
    protected DoubleProperty clearWayLength = new SimpleDoubleProperty();
    protected DoubleProperty displayStopWayLength = new SimpleDoubleProperty(0); // Need to be rescaled
    protected DoubleProperty displayClearWayLength = new SimpleDoubleProperty(0); // Need to be rescaled
    protected DoubleProperty displayRunwayLength = new SimpleDoubleProperty(550); // FIXED 550



    // Take Off Parameters
    protected DoubleProperty TORA = new SimpleDoubleProperty();
    protected DoubleProperty displayTORA = new SimpleDoubleProperty(displayRunwayLength.getValue());
    protected DoubleProperty TODA = new SimpleDoubleProperty();
    protected DoubleProperty displayTODA = new SimpleDoubleProperty(displayRunwayLength.getValue()  + displayClearWayLength.getValue() );
    protected DoubleProperty ASDA = new SimpleDoubleProperty();
    protected DoubleProperty displayASDA = new SimpleDoubleProperty(displayRunwayLength.getValue()  + displayStopWayLength.getValue() );
    protected DoubleProperty LDA = new SimpleDoubleProperty();
    protected DoubleProperty displayLDA = new SimpleDoubleProperty(displayRunwayLength.getValue() );
    // The blast protection is fixed to be 60m
    protected DoubleProperty blastAllowance = new SimpleDoubleProperty(60);
    protected DoubleProperty displayBlastAllowance = new SimpleDoubleProperty(50);
    protected DoubleProperty RESA = new SimpleDoubleProperty();
    protected DoubleProperty displayRESA = new SimpleDoubleProperty(100);

    protected DoubleProperty displayRunwayToPlane = new SimpleDoubleProperty(0);
    protected DoubleProperty displayPlaneToObstacle = new SimpleDoubleProperty(400);

    protected DoubleProperty displayBorderToRunway = new SimpleDoubleProperty();


    protected StackPane takeOffIndicators;
    protected HBox toraBox;
    protected HBox todaBox;
    protected HBox ldaBox;
    protected HBox asdaBox;
    protected HBox blastAllowanceBox;
    protected HBox resaBox;

    // Obstacles
    protected ArrayList<Obstacle> obstacles = new ArrayList<>();


    public double calculateThreshold(){
        return currentObstacle.getPositionOnRunway() - subRunway1.getDisplacedThreshold();
    }

    public double calculateTORA(){
        return subRunway1.getOriginalTORA() - subRunway1.getBlastProtection() - calculateThreshold() - subRunway1.getDisplacedThreshold();
    }

    public double calculateASDA(){
       return calculateTORA() + subRunway1.getStopwayLength();
    }

    public double calculateTODA(){
       return calculateTORA() + subRunway1.getClearwayLength();
    }


    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     * @param homeWindow the home window
     */
    public BaseScene(HomeWindow homeWindow) {
        this.homeWindow = homeWindow;


        // For demo
        this.subRunway1 = new SubRunway("09L", 3902.0, 3902.0, 3902.0, 3595.0, 0, 0, 0, 60, 300);
        this.subRunway2 = new SubRunway("27R", 3884.0, 3862.0, 3884.0, 3884.0, 0, 0, 0, 60, 300);

        displayBorderToRunway.setValue((homeWindow.getWidth() - 600 - displayRunwayLength.getValue() - displayStopWayLength.getValue() * 2) / 2);



        Obstacle Boeing_777 = new Obstacle("Boeing 777", 18.6, 64.8, 63.7, 2973);
        Obstacle Boeing_737 = new Obstacle("Boeing 737", 12.42, 35.91, 39.52, 2973);
        Obstacle Luggage_Car = new Obstacle("Luggage Car", 1.7, 5.1, 4.6, 2973);
        obstacles.addAll(new ArrayList<Obstacle>(List.of(Boeing_777, Boeing_737, Luggage_Car)));


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
        Label runway1 = new Label(subRunway1.getDesignator());
        runway1.getStyleClass().add("grid-pane-label");
        gpanething.add(runway1, 0, 2);

        Label tora_original1 = new Label();
        tora_original1.textProperty().set(String.valueOf(subRunway1.getOriginalTORA()));
        tora_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_original1, 1, 2);

        Label toda_original1 = new Label();
        toda_original1.textProperty().set(String.valueOf(subRunway1.getOriginalTODA()));
        toda_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_original1, 2, 2);

        Label lda_original1 = new Label();
        lda_original1.textProperty().set(String.valueOf(subRunway1.getOriginalLDA()));
        lda_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_original1, 3, 2);

        Label asda_original1 = new Label();
        asda_original1.textProperty().set(String.valueOf(subRunway1.getOriginalASDA()));
        asda_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_original1, 4, 2);


        Label runway2 = new Label("27R");
        runway2.getStyleClass().add("grid-pane-label");
        gpanething.add(runway2, 0, 3);

        Label tora_original2 = new Label();
        tora_original2.textProperty().set(String.valueOf(subRunway2.getOriginalTORA()));
        tora_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_original2, 1, 3);

        Label toda_original2 = new Label();
        toda_original2.textProperty().set(String.valueOf(subRunway2.getOriginalTODA()));
        toda_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_original2, 2, 3);

        Label lda_original2= new Label();
        lda_original2.textProperty().set(String.valueOf(subRunway2.getOriginalLDA()));
        lda_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_original2, 3, 3);

        Label asda_original2 = new Label();
        asda_original2.textProperty().set(String.valueOf(subRunway2.getOriginalASDA()));
        asda_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_original2, 4, 3);


        Label lblRecalculated = new Label("New Values");
        lblRecalculated.setMaxWidth(Double.MAX_VALUE);
        lblRecalculated.getStyleClass().add("center-label");
        GridPane.setColumnSpan(lblRecalculated, GridPane.REMAINING);
        GridPane.setHalignment(lblRecalculated, HPos.CENTER);
        gpanething.add(lblRecalculated, 0, 4);

        // Recalculated Values:
        Label runway1_recalculated = new Label(subRunway1.getDesignator());
        runway1_recalculated.getStyleClass().add("grid-pane-label");
        gpanething.add(runway1_recalculated, 0, 5);

        Label tora_recalculated1 = new Label();
        tora_recalculated1.textProperty().set(String.valueOf(subRunway1.TORA));
        tora_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_recalculated1, 1, 5);

        Label toda_recalculated1 = new Label();
        toda_recalculated1.textProperty().set(String.valueOf(subRunway1.TODA));
        toda_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_recalculated1, 2, 5);

        Label lda_recalculated1 = new Label();
        lda_recalculated1.textProperty().set(String.valueOf(subRunway1.LDA));
        lda_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_recalculated1, 3, 5);

        Label asda_recalculated1 = new Label();
        asda_recalculated1.textProperty().set(String.valueOf(subRunway1.ASDA));
        asda_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_recalculated1, 4, 5);

        Label runway2_recalculated = new Label(subRunway2.getDesignator());
        runway2_recalculated.getStyleClass().add("grid-pane-label");
        gpanething.add(runway2_recalculated, 0, 6);

        Label tora_recalculated2 = new Label();
        tora_recalculated2.textProperty().set(String.valueOf(subRunway2.TORA));
        tora_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_recalculated2, 1, 6);

        Label toda_recalculated2 = new Label();
        toda_recalculated2.textProperty().set(String.valueOf(subRunway2.TODA));
        toda_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_recalculated2, 2, 6);

        Label lda_recalculated2 = new Label();
        lda_recalculated2.textProperty().set(String.valueOf(subRunway2.LDA));
        lda_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_recalculated2, 3, 6);

        Label asda_recalculated2 = new Label();
        asda_recalculated2.textProperty().set(String.valueOf(subRunway2.ASDA));
        asda_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_recalculated2, 4, 6);



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
        HBox buttonLayout = new HBox(5);
        buttonLayout.setPadding(new Insets(5));
        buttonLayout.getChildren().addAll(buttonTORA, buttonTODA, buttonLDA, buttonASDA);

        VBox vbox = new VBox(buttonLayout, displayArea);
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
            takeOffIndicators.getChildren().add(blastAllowanceBox);
            takeOffIndicators.getChildren().add(resaBox);

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
        obstacleTPane.setText("Add Obstacles:");
        obstacleTPane.setCollapsible(true);

        ComboBox<Obstacle> obstacle = new ComboBox<>();
        for (Obstacle o : obstacles){
            obstacle.getItems().add(o);
        }



        VBox obstacleBox = new VBox(5);

        TextField obstacleHeight = new TextField();
        obstacleBox.getChildren().addAll(new Label("Height (metres):"), obstacleHeight);

        TextField obstacleWidth = new TextField();
        obstacleBox.getChildren().addAll(new Label("Width (metres):"),obstacleWidth);

        TextField distanceFromStopway = new TextField();
        obstacleBox.getChildren().addAll(new Label("Distance from left stopway (metres):"), distanceFromStopway);

        obstacle.setOnAction(e -> {
            currentObstacle = obstacle.getValue();
            obstacleHeight.setText(String.valueOf(currentObstacle.getHeight()));
            obstacleWidth.setText(String.valueOf(currentObstacle.getWidth()));
            distanceFromStopway.setText(String.valueOf(currentObstacle.getPositionOnRunway()));

        });

        HBox buttons = new HBox(5);
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {});

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
        });

        buttons.getChildren().add(editButton);

        buttons.getChildren().add(addButton);
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
    /*
    public TitledPane makeAirplaneTPane(){


        VBox airplaneStuff = new VBox();
        airplaneStuff.add(new JLabel("Operation:"));

        JRadioButton takeOffRadioButton = new JRadioButton("Taking Off");
        JRadioButton landingRadioButton = new JRadioButton("Landing");
        ButtonGroup operationGroup1 = new ButtonGroup();
        operationGroup1.add(takeOffRadioButton);
        operationGroup1.add(landingRadioButton);

        airplaneStuff.add(takeOffRadioButton);
        airplaneStuff.add(landingRadioButton);

        airplaneStuff.add(Box.createVerticalStrut(10));

        airplaneStuff.add(new JLabel("Direction: "));
        JRadioButton firstDirectionButton = new JRadioButton("Left");
        JRadioButton secondDirectionButton = new JRadioButton("Right");
        ButtonGroup operationGroup2 = new ButtonGroup();
        operationGroup2.add(firstDirectionButton);
        operationGroup2.add(secondDirectionButton);

        airplaneStuff.add(firstDirectionButton);
        airplaneStuff.add(secondDirectionButton);

        landingRadioButton.addActionListener(e -> {});
        takeOffRadioButton.addActionListener(e -> {});

        firstDirectionButton.addActionListener((e -> {}));
        secondDirectionButton.addActionListener((e -> {}));


        TitledPane airplaneTPane = new TitledPane();
        airplaneTPane.setText("Add Airplane:");
        airplaneTPane.setCollapsible(true);
        airplaneTPane.setContent(airplaneStuff);
        airplaneTPane.add(airplaneStuff);

        return airplaneTPane;
    }

*/





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
