package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import java.sql.SQLException;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import uk.ac.soton.comp2211.runwayredeclaration.Calculator.RunwayCalculator;
import uk.ac.soton.comp2211.runwayredeclaration.Component.*;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import uk.ac.soton.comp2211.runwayredeclaration.XMLloader.xmFileLoader;



import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A Base Scene used in the game. Handles common functionality between all scenes.
 */
public abstract class BaseScene {

    protected final HomeWindow homeWindow;

    protected HomePane root;
    protected Scene scene;

    protected ArrayList<Airport> airportList;
    protected Airport currentAirport;
    protected Runway currentRunway;
    protected SubRunway subRunway1;
    protected SubRunway subRunway2;
    protected SubRunway currentSubRunway;
    protected Obstacle currentObstacle;

    protected DoubleProperty runwayLength = new SimpleDoubleProperty();
    protected DoubleProperty stopWayLength1 = new SimpleDoubleProperty();
    protected DoubleProperty stopWayLength2 = new SimpleDoubleProperty();
    protected DoubleProperty clearWayLength1 = new SimpleDoubleProperty();
    protected DoubleProperty clearWayLength2 = new SimpleDoubleProperty();
    protected DoubleProperty displayStopWayLength = new SimpleDoubleProperty(60); // Need to be rescaled
    protected DoubleProperty displayClearWayLength = new SimpleDoubleProperty(80); // Need to be rescaled
    protected DoubleProperty displayRunwayLength = new SimpleDoubleProperty(550); // FIXED 550



    // Take Off Parameters
    protected DoubleProperty TORA = new SimpleDoubleProperty();
    protected DoubleProperty displayTORA = new SimpleDoubleProperty(displayRunwayLength.getValue());
    protected DoubleProperty displayBorderToTORA = new SimpleDoubleProperty();

    protected DoubleProperty TODA = new SimpleDoubleProperty();
    protected DoubleProperty displayTODA = new SimpleDoubleProperty(displayRunwayLength.getValue()  + displayClearWayLength.getValue() );
    protected DoubleProperty displayBorderToTODA = new SimpleDoubleProperty();

    protected DoubleProperty ASDA = new SimpleDoubleProperty();
    protected DoubleProperty displayASDA = new SimpleDoubleProperty(displayRunwayLength.getValue()  + displayStopWayLength.getValue() );
    protected DoubleProperty displayBorderToASDA = new SimpleDoubleProperty();

    protected DoubleProperty LDA = new SimpleDoubleProperty();
    protected DoubleProperty displayLDA = new SimpleDoubleProperty(displayRunwayLength.getValue() );
    protected DoubleProperty displayBorderToLDA = new SimpleDoubleProperty();


    // The blast protection is fixed to be 60m
    protected DoubleProperty blastAllowance = new SimpleDoubleProperty(60);
    protected DoubleProperty displayBlastAllowance = new SimpleDoubleProperty(50);
    protected DoubleProperty RESA = new SimpleDoubleProperty();
    protected DoubleProperty displayRESA = new SimpleDoubleProperty(100);
    protected DoubleProperty displayBorderToRESA = new SimpleDoubleProperty(0);
    protected DoubleProperty displayDisplacedThreshold = new SimpleDoubleProperty(30);

    protected DoubleProperty displayRunwayToPlane = new SimpleDoubleProperty(0);
    protected DoubleProperty displayPlaneToObstacle = new SimpleDoubleProperty(400);

    protected DoubleProperty displayBorderToRunway = new SimpleDoubleProperty();
    protected DoubleProperty displayBorderToStopway = new SimpleDoubleProperty();
    protected DoubleProperty displayPlaneWidth = new SimpleDoubleProperty(60);


    protected HBox toraBox;
    protected HBox todaBox;
    protected HBox ldaBox;
    protected HBox asdaBox;
    protected HBox blastAllowanceBox;
    protected HBox resaBox;
    protected HBox stripEndBox;
    protected HBox displacedThresholdBox = new HBox();

    // Obstacles
    protected ArrayList<Obstacle> predefinedObstacles = new ArrayList<>();

    public double obstacleHeightD;
    public double obstacleWidthD;
    public double distanceD;

    public String operationSelected = null;
    public SimpleStringProperty directionSelected = new SimpleStringProperty("(...)");

    public ToggleGroup operationButtons = new ToggleGroup();
    public ToggleGroup directionButtons = new ToggleGroup();
    private Text clearwayLengthDisplay;
    private Text stopwayLengthDisplay;
    private Text thresholdLengthDisplay;


    ToggleGroup group = new ToggleGroup();
    RadioButton defaultOption = new RadioButton("Default (Blue/Green)");
    RadioButton option1 = new RadioButton("Blue/Yellow");
    RadioButton option2 = new RadioButton("Magenta/Lime Green");
    RadioButton option3 = new RadioButton("Cyan/Deep Purple");

    protected CurrentState currentState = new CurrentState();

    protected HBox obstacleBox = new HBox();
    protected HBox planeBox = new HBox();
    protected StackPane indicatorsSubRunway1 = new StackPane();
    protected StackPane indicatorsSubRunway2 = new StackPane();
    protected RadioButton firstDirectionButton;
    protected RadioButton secondDirectionButton;


    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     * @param homeWindow the home window
     */
    public BaseScene(HomeWindow homeWindow) {
        this.homeWindow = homeWindow;



        // Load Airports
        this.airportList = xmFileLoader.loadAirports();


        currentAirport = airportList.get(0);
        currentRunway = currentAirport.getRunways().get(0);


        subRunway1 = new SubRunway(currentRunway.getSubRunways().get(0));
        subRunway2 = new SubRunway(currentRunway.getSubRunways().get(1));

        stopWayLength1.bind(subRunway1.getStopwayLength());
        stopWayLength2.bind(subRunway2.getStopwayLength());

        clearWayLength1.bind(subRunway1.getClearwayLength());
        clearWayLength2.bind(subRunway2.getClearwayLength());


        // Set the runway length
        displayBorderToRunway.setValue((homeWindow.getWidth() - 600 - displayRunwayLength.getValue()) / 2);
        displayBorderToStopway.setValue((homeWindow.getWidth() - 600 - displayRunwayLength.getValue() - displayStopWayLength.getValue() * 2) / 2);


        Obstacle Boeing_777 = new Obstacle("Boeing 777", 18.6, 64.8, 63.7);
        Obstacle Boeing_737 = new Obstacle("Boeing 737", 12.42, 35.91, 39.52);
        Obstacle Luggage_Car = new Obstacle("Luggage Car", 1.7, 5.1, 4.6);
        predefinedObstacles.addAll(new ArrayList<Obstacle>(List.of(Boeing_777, Boeing_737, Luggage_Car)));



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
        Label runway1 = new Label();
        runway1.textProperty().bind(subRunway1.getDesignator());
        runway1.getStyleClass().add("grid-pane-label");
        gpanething.add(runway1, 0, 2);

        Label tora_original1 = new Label();
        tora_original1.textProperty().bind(subRunway1.getOriginalTORA().asString());
        tora_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_original1, 1, 2);

        Label toda_original1 = new Label();
        toda_original1.textProperty().bind(subRunway1.getOriginalTODA().asString());
        toda_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_original1, 2, 2);

        Label lda_original1 = new Label();
        lda_original1.textProperty().bind(subRunway1.getOriginalLDA().asString());
        lda_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_original1, 3, 2);

        Label asda_original1 = new Label();
        asda_original1.textProperty().bind(subRunway1.getOriginalASDA().asString());
        asda_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_original1, 4, 2);


        Label runway2 = new Label();
        runway2.textProperty().bind(subRunway2.getDesignator());
        runway2.getStyleClass().add("grid-pane-label");
        gpanething.add(runway2, 0, 3);

        Label tora_original2 = new Label();
        tora_original2.textProperty().bind(subRunway2.getOriginalTORA().asString());
        tora_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_original2, 1, 3);

        Label toda_original2 = new Label();
        toda_original2.textProperty().bind(subRunway2.getOriginalTODA().asString());
        toda_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_original2, 2, 3);

        Label lda_original2= new Label();
        lda_original2.textProperty().bind(subRunway2.getOriginalLDA().asString());
        lda_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_original2, 3, 3);

        Label asda_original2 = new Label();
        asda_original2.textProperty().bind(subRunway2.getOriginalASDA().asString());
        asda_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_original2, 4, 3);


        Label lblRecalculated = new Label("New Values");
        lblRecalculated.setMaxWidth(Double.MAX_VALUE);
        lblRecalculated.getStyleClass().add("center-label");
        GridPane.setColumnSpan(lblRecalculated, GridPane.REMAINING);
        GridPane.setHalignment(lblRecalculated, HPos.CENTER);
        gpanething.add(lblRecalculated, 0, 4);

        // Recalculated Values:
        Label runway1_recalculated = new Label();
        runway1_recalculated.textProperty().bind(subRunway1.getDesignator());
        runway1_recalculated.getStyleClass().add("grid-pane-label");
        gpanething.add(runway1_recalculated, 0, 5);

        Label tora_recalculated1 = new Label();
        tora_recalculated1.textProperty().bind(subRunway1.getTORA().asString());
        tora_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_recalculated1, 1, 5);

        Label toda_recalculated1 = new Label();
        toda_recalculated1.textProperty().bind(subRunway1.getTODA().asString());
        toda_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_recalculated1, 2, 5);

        Label lda_recalculated1 = new Label();
        lda_recalculated1.textProperty().bind(subRunway1.getLDA().asString());
        lda_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_recalculated1, 3, 5);

        Label asda_recalculated1 = new Label();
        asda_recalculated1.textProperty().bind(subRunway1.getASDA().asString());
        asda_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_recalculated1, 4, 5);

        Label runway2_recalculated = new Label();
        runway2_recalculated.textProperty().bind(subRunway2.getDesignator());
        runway2_recalculated.getStyleClass().add("grid-pane-label");
        gpanething.add(runway2_recalculated, 0, 6);

        Label tora_recalculated2 = new Label();
        tora_recalculated2.textProperty().bind(subRunway2.getTORA().asString());
        tora_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_recalculated2, 1, 6);

        Label toda_recalculated2 = new Label();
        toda_recalculated2.textProperty().bind(subRunway2.getTODA().asString());
        toda_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_recalculated2, 2, 6);

        Label lda_recalculated2 = new Label();
        lda_recalculated2.textProperty().bind(subRunway2.getLDA().asString());
        lda_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_recalculated2, 3, 6);

        Label asda_recalculated2 = new Label();
        asda_recalculated2.textProperty().bind(subRunway2.getASDA().asString());
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
                indicatorsSubRunway1.getChildren().clear();
                indicatorsSubRunway2.getChildren().clear();
                indicatorsSubRunway1.getChildren().add(toraBox);
                indicatorsSubRunway2.getChildren().add(toraBox);

            }
            else if (selectedButton.getText().equals("TODA")){
                indicatorsSubRunway1.getChildren().clear();
                indicatorsSubRunway2.getChildren().clear();
                indicatorsSubRunway1.getChildren().add(todaBox);
                indicatorsSubRunway2.getChildren().add(todaBox);

            }
            else if (selectedButton.getText().equals("ASDA")){
                indicatorsSubRunway1.getChildren().clear();
                indicatorsSubRunway2.getChildren().clear();
                indicatorsSubRunway1.getChildren().add(asdaBox);
                indicatorsSubRunway2.getChildren().add(asdaBox);

            }
            else if (selectedButton.getText().equals("LDA")){
                indicatorsSubRunway1.getChildren().clear();
                indicatorsSubRunway2.getChildren().clear();
                indicatorsSubRunway1.getChildren().add(ldaBox);
                indicatorsSubRunway2.getChildren().add(ldaBox);
            }


        } else {
            displayArea.clear();
            // If it was already selected, clear the text area as the button is deselected

            indicatorsSubRunway1.getChildren().clear();
            indicatorsSubRunway1.getChildren().addAll(toraBox, todaBox, ldaBox, asdaBox, blastAllowanceBox, resaBox);

            indicatorsSubRunway2.getChildren().clear();
            indicatorsSubRunway2.getChildren().addAll(toraBox, todaBox, ldaBox, asdaBox, blastAllowanceBox, resaBox);


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

        ComboBox<String> comboRunways = new ComboBox<>();
        for (Runway runway : currentAirport.getRunways()){
            comboRunways.getItems().add(runway.getName());
        }
        comboRunways.setValue(currentRunway.getName());
        // If the runway is changed, update the subRunway indicators
        comboRunways.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                for (Runway runway : currentAirport.getRunways()){

                    if (runway.getName().equals(newValue)){

                        clearAllButtons();
                        planeBox.getChildren().clear();
                        obstacleBox.getChildren().clear();
                        currentRunway = runway;


                        subRunway1.update(currentRunway.getSubRunways().get(0));
                        subRunway2.update(currentRunway.getSubRunways().get(1));

                        // Make displaced threshold indicator
                        displacedThresholdBox.getChildren().clear();
                        displacedThresholdBox.setAlignment(Pos.CENTER_LEFT);
                        double ratio1 = (subRunway1.getDisplacedThreshold().get() / subRunway1.getOriginalTORA().get());
                        double displayDisplacedThreshold1 = displayRunwayLength.getValue() * ratio1;

                        double ratio2 = (subRunway2.getDisplacedThreshold().get() / subRunway2.getOriginalTORA().get());
                        double displayDisplacedThreshold2 = displayRunwayLength.getValue() * ratio2;

                        HBox borderToDisplacedThreshold1 = new HBox();
                        borderToDisplacedThreshold1.getStyleClass().add("empty");
                        borderToDisplacedThreshold1.prefWidthProperty().bind(Bindings.add(displayBorderToRunway, displayDisplacedThreshold1));

                        DashedLine thresholdLine1 = new DashedLine(0.1, 500, "red");
                        thresholdLine1.visibleProperty().set(ratio1 != 0);

                        HBox emptyBox1 = new HBox();
                        emptyBox1.getStyleClass().add("empty");
                        HBox.setHgrow(emptyBox1, Priority.ALWAYS);

                        DashedLine thresholdLine2 = new DashedLine(0.1, 500);
                        thresholdLine2.visibleProperty().set(ratio2 != 0);

                        HBox borderToDisplacedThreshold2 = new HBox();
                        borderToDisplacedThreshold2.getStyleClass().add("empty");
                        borderToDisplacedThreshold2.prefWidthProperty().bind(Bindings.add(displayBorderToRunway, displayDisplacedThreshold2));


                        displacedThresholdBox.getChildren().addAll(borderToDisplacedThreshold1, thresholdLine1, emptyBox1, thresholdLine2, borderToDisplacedThreshold2);
                        stopwayLengthDisplay.setText("(Select designator to show)");
                        clearwayLengthDisplay.setText("(Select designator to show)");
                        thresholdLengthDisplay.setText("(Select designator to show)");


                    }
                }
            }
        });

        ComboBox<String> comboAirports = new ComboBox<>();
        for (Airport airport : airportList){
            comboAirports.getItems().add(airport.getName());
        }

        comboAirports.setValue(currentAirport.getName());
        comboAirports.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                for (Airport airport : airportList){
                    if (airport.getName().equals(newValue)){
                        clearAllButtons();
                        planeBox.getChildren().clear();
                        obstacleBox.getChildren().clear();

                        // Hide all indicators

                        indicatorsSubRunway1.getChildren().clear();
                        indicatorsSubRunway2.getChildren().clear();

                        currentAirport = airport;
                        currentRunway = currentAirport.getRunways().get(0);

                        comboRunways.getItems().clear();
                        for (Runway runway : currentAirport.getRunways()){
                            comboRunways.getItems().add(runway.getName());
                        }
                        comboRunways.setValue(currentRunway.getName());

                        subRunway1.update(currentRunway.getSubRunways().get(0));
                        subRunway2.update(currentRunway.getSubRunways().get(1));

                        stopwayLengthDisplay.setText("(Select designator to show)");
                        clearwayLengthDisplay.setText("(Select designator to show)");
                        thresholdLengthDisplay.setText("(Select designator to show)");





                    }
                }


            }
        });




        airportGrid.addRow(0, new Label("Airport:"), comboAirports);
        airportGrid.addRow(1, new Label("Runway:"), comboRunways);

        thresholdLengthDisplay = new Text("Select designator to show");
        airportGrid.addRow(2, new Label("Threshold:"), thresholdLengthDisplay);
        clearwayLengthDisplay = new Text("Select designator to show");
        airportGrid.addRow(3, new Label("Clearway:"), clearwayLengthDisplay);
        stopwayLengthDisplay = new Text("Select designator to show");
        airportGrid.addRow(4, new Label("Stopway:"), stopwayLengthDisplay);

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

        obstacleGrid.setPrefWidth(300);
        TitledPane obstacleTPane = new TitledPane();
        obstacleTPane.setText("Add Obstacles:");
        obstacleTPane.setCollapsible(true);

        ComboBox<Obstacle> obstacle = new ComboBox<>();
        for (Obstacle o : predefinedObstacles){
            obstacle.getItems().add(o);
        }



        VBox obstacleBox = new VBox(5);

        TextField obstacleHeight = new TextField();
        obstacleBox.getChildren().addAll(new Label("Height (metres):"), obstacleHeight);


        TextField obstacleToCentreLine = new TextField();
        obstacleBox.getChildren().addAll(new Label("Distance from centreline (metres):"), obstacleToCentreLine);

        TextField distanceFromThreshold1 = new TextField();
        TextField distanceFromThreshold2 = new TextField();

        Label distanceFromLabel1 = new Label();
        distanceFromLabel1.textProperty().bind(Bindings.concat("Distance from ", subRunway1.getDesignator() , " threshold (meters):"));

        Label distanceFromLabel2 = new Label();
        distanceFromLabel2.textProperty().bind(Bindings.concat("Distance from ", subRunway2.getDesignator() , " threshold (meters):"));

        obstacleBox.getChildren().addAll(distanceFromLabel1, distanceFromThreshold1, distanceFromLabel2, distanceFromThreshold2);

        obstacle.setOnAction(e -> {

            currentObstacle = obstacle.getValue();
            obstacleHeight.setText(String.valueOf(currentObstacle.getHeight()));

            subRunway1.setTORA(subRunway1.getOriginalTORA().get());
            subRunway1.setTODA(subRunway1.getOriginalTODA().get());
            subRunway1.setASDA(subRunway1.getOriginalASDA().get());
            subRunway1.setLDA(subRunway1.getOriginalLDA().get());

            subRunway2.setTORA(subRunway2.getOriginalTORA().get());
            subRunway2.setTODA(subRunway2.getOriginalTODA().get());
            subRunway2.setASDA(subRunway2.getOriginalASDA().get());
            subRunway2.setLDA(subRunway2.getOriginalLDA().get());


        });

        // Recalculate Button
        Button recalculateB = new Button("Recalculate");
        recalculateB.setMaxWidth(Double.MAX_VALUE);
        recalculateB.setOnAction(e -> {
            String heightText = obstacleHeight.getText();
            String distanceToCentreline = obstacleToCentreLine.getText();
            String distanceTextD1 = distanceFromThreshold1.getText();
            String distanceTextD2 = distanceFromThreshold2.getText();

            double height;
            double distanceToCentrelineD;
            double distance1;
            double distance2;

            try {
                // Parse values to doubles
                height = Double.parseDouble(heightText);
                distanceToCentrelineD = Double.parseDouble(distanceToCentreline);
                distance1 = Double.parseDouble(distanceTextD1);
                distance2 = Double.parseDouble(distanceTextD2);

                // Check if the obstacle values are valid
                String errorMessage = handleObstacle(height, distanceToCentrelineD, distance1, distance2);

                // If there is an error, display the appropriate error message(s) and clear the corresponding input field(s)
                if (errorMessage != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText(errorMessage);
                    alert.showAndWait();

                    if (errorMessage.contains("Invalid height")) {
                        obstacleHeight.clear();
                    }
                    if (errorMessage.contains("Invalid width")) {
                        obstacleToCentreLine.clear();
                    }
                    if (errorMessage.contains("Invalid distance")) {
                        distanceFromThreshold1.clear();
                    }
                    if (errorMessage.contains("Invalid distance")) {
                        distanceFromThreshold2.clear();
                    }

                }
                currentObstacle = new Obstacle(obstacle.getValue().getName());
                currentObstacle.setHeight(height);

                subRunway1.setObstacle(currentObstacle, distance1);
                subRunway2.setObstacle(currentObstacle, distance2);


                subRunway1.setTORA(RunwayCalculator.calculateTORA(subRunway1, currentObstacle, distance1));
                subRunway1.setTODA(RunwayCalculator.calculateTODA(subRunway1, currentObstacle, distance1));
                subRunway1.setASDA(RunwayCalculator.calculateASDA(subRunway1, currentObstacle, distance1));
                subRunway1.setLDA(RunwayCalculator.calculateLDA(subRunway1, currentObstacle, distance1));


                subRunway2.setTORA(RunwayCalculator.calculateTORA(subRunway2, currentObstacle, distance2));
                subRunway2.setTODA(RunwayCalculator.calculateTODA(subRunway2, currentObstacle, distance2));
                subRunway2.setASDA(RunwayCalculator.calculateASDA(subRunway2, currentObstacle, distance2));
                subRunway2.setLDA(RunwayCalculator.calculateLDA(subRunway2, currentObstacle, distance2));



                this.obstacleBox.getChildren().clear();
                this.obstacleBox.setAlignment(Pos.BOTTOM_LEFT);
                if (subRunway1.getObstacle() != null){
                    double distance = subRunway1.getObstacleDistance();
                    double ratio = distance / subRunway1.getOriginalTORA().get();
                    double displayObstacleToThreshold;
                    if (ratio < 0){// If negative then the obstacle is before the threshold
                        displayObstacleToThreshold = ratio * displayRunwayLength.get() - 30;
                    }
                    else{
                        displayObstacleToThreshold = ratio * displayRunwayLength.get();
                    }



                    HBox borderToObstacleBox = new HBox();
                    borderToObstacleBox.getStyleClass().add("empty");
                    borderToObstacleBox.prefWidthProperty().bind( Bindings.add(displayBorderToRunway, displayObstacleToThreshold));


                    // Obstacle Image
                    Image obstacleImage = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
                    ImageView obstacleImageView = new ImageView(obstacleImage);
                    obstacleImageView.setPreserveRatio(true);
                    obstacleImageView.setFitWidth(30);

                    this.obstacleBox.getChildren().addAll(borderToObstacleBox, obstacleImageView);

                }



                if (firstDirectionButton.selectedProperty().get()){
                    System.out.println("First direction selected fired");
                    firstDirectionButton.setSelected(false);
                    firstDirectionButton.fire();

                }
                else if (secondDirectionButton.selectedProperty().get()) {
                    System.out.println("Second direction selected fired");
                    secondDirectionButton.setSelected(false);
                    secondDirectionButton.fire();
                }





            } catch (NumberFormatException ex) {
                // If parsing fails (e.g., input is not a valid double), show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter only numeric values in all sections");
                alert.showAndWait();

                // Clear all fields if parsing fails
                try {
                    height = Double.parseDouble(heightText);
                } catch (NumberFormatException f) {
                    obstacleHeight.clear();
                }

                try {
                    distanceToCentrelineD = Double.parseDouble(distanceToCentreline);
                } catch (NumberFormatException f) {
                    obstacleToCentreLine.clear();
                }

                try {
                    distance1 = Double.parseDouble(distanceTextD1);
                } catch (NumberFormatException f) {
                    distanceFromThreshold1.clear();
                }

                try {
                    distance2 = Double.parseDouble(distanceTextD2);
                } catch (NumberFormatException f) {
                    distanceFromThreshold2.clear();
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }



        });


        HBox obstacleLabel = new HBox(30, new Label("Obstacle: "), obstacle);

        obstacleGrid.addRow(0, obstacleLabel);
        obstacleGrid.addRow(1, obstacleBox);
        obstacleGrid.addRow(2, recalculateB);


        obstacleGrid.prefWidthProperty().bind(obstacleTPane.widthProperty());
        obstacleTPane.setContent(obstacleGrid);

        return obstacleTPane;
    }







    public TitledPane makeAirplaneTPane(){


        VBox airplaneStuff = new VBox(3);




        RadioButton takeOffRadioButton = new RadioButton("Taking Off");
        takeOffRadioButton.setUserData("takeoff");

        RadioButton landingRadioButton = new RadioButton("Landing");
        landingRadioButton.setUserData("landing");

        //ToggleGroup operationButtons = new ToggleGroup();
        takeOffRadioButton.setToggleGroup(operationButtons);
        landingRadioButton.setToggleGroup(operationButtons);
        operationButtons.selectToggle(null);

        firstDirectionButton = new RadioButton();
        firstDirectionButton.textProperty().bind(subRunway1.getDesignator());
        firstDirectionButton.setUserData(subRunway1.getDesignator());
        firstDirectionButton.setOnAction(e -> {
            System.out.println("First direction selected");
            clearwayLengthDisplay.setText(subRunway1.getClearwayLength().get() + "m");
            stopwayLengthDisplay.setText(subRunway1.getStopwayLength().get() + "m");
            thresholdLengthDisplay.setText(subRunway1.getDisplacedThreshold().get() + "m");
            currentSubRunway = subRunway1;

            indicatorsSubRunway1.getChildren().clear();
            indicatorsSubRunway1.getChildren().addAll(toraBox, todaBox, ldaBox, asdaBox, blastAllowanceBox, resaBox);

            SimpleDoubleProperty displayBorderToObstacle = new SimpleDoubleProperty();
            if ( obstacleBox.getChildren().isEmpty() ){ // If there is no obstacle on the runway
                indicatorsSubRunway1.getChildren().remove(resaBox);
                indicatorsSubRunway1.getChildren().remove(blastAllowanceBox);

            }
            else{ // If there is an obstacle on the runway

                displayBorderToObstacle.bind( ((HBox) obstacleBox.getChildren().get(0)).prefWidthProperty() );
                // Check whether the obstacle is close or further away from the threshold
                if (displayBorderToObstacle.get() > (displayBorderToRunway.get() + (displayRunwayLength.get() / 2)) ){
                    // If the obstacle is further away
                    displayBorderToRESA.bind( Bindings.subtract( displayBorderToObstacle, displayRESA));
                    resaBox.visibleProperty().set(true);




                }
                else{
                    // If the obstacle is closer
                    resaBox.visibleProperty().set(false);
                }

            }

            System.out.println("Border to TODA : " + displayBorderToTODA.getValue());
            System.out.println("Border to TORA : " + displayBorderToTORA.getValue());
            System.out.println("Border to ASDA : " + displayBorderToASDA.getValue());



        });



        secondDirectionButton = new RadioButton();
        secondDirectionButton.textProperty().bind(subRunway2.getDesignator());
        secondDirectionButton.setUserData(subRunway2.getDesignator());
        secondDirectionButton.setOnAction(e -> {
            System.out.println("Second direction selected");
            clearwayLengthDisplay.setText(subRunway2.getClearwayLength().get() + "m");
            stopwayLengthDisplay.setText(subRunway2.getStopwayLength().get() + "m");
            thresholdLengthDisplay.setText(subRunway2.getDisplacedThreshold().get() + "m");
            currentSubRunway = subRunway2;


        });

        //ToggleGroup directionButtons = new ToggleGroup();
        firstDirectionButton.setToggleGroup(directionButtons);
        secondDirectionButton.setToggleGroup(directionButtons);

        directionButtons.selectToggle(null);

        indicatorsSubRunway1.visibleProperty().bind(firstDirectionButton.selectedProperty());
        indicatorsSubRunway2.visibleProperty().bind(secondDirectionButton.selectedProperty());



        operationButtons.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            // Update the global variable based on the new selection
            if (newValue != null) {
                operationSelected = newValue.getUserData().toString();
            } else {
                // If no RadioButton is selected (e.g., initial state or all selections are cleared)
                operationSelected = null;
            }
            //System.out.println("Current operation: " + operationSelected);
        });

        directionButtons.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            // Update the global variable based on the new selection
            if (newValue != null) {
                directionSelected.set( ((RadioButton) newValue).getText());

            } else {
                // If no RadioButton is selected (e.g., initial state or all selections are cleared)
                directionSelected.set("(...)");
            }

        });




        airplaneStuff.getChildren().add(new Label("Operation:"));
        airplaneStuff.getChildren().add(takeOffRadioButton);
        airplaneStuff.getChildren().add(landingRadioButton);

        airplaneStuff.getChildren().add(new Label("Designator: "));
        airplaneStuff.getChildren().add(firstDirectionButton);
        airplaneStuff.getChildren().add(secondDirectionButton);


        TitledPane airplaneTPane = new TitledPane();
        airplaneTPane.setText("Add Airplane:");
        airplaneTPane.setCollapsible(true);
        airplaneTPane.setContent(airplaneStuff);


        return airplaneTPane;
    }

    public void clearAllButtons(){
        operationButtons.selectToggle(null);
        directionButtons.selectToggle(null);
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

    Checkers checker = new Checkers(obstacleHeightD, obstacleWidthD, distanceD);
    public String handleObstacle(double h, double w, double d1, double d2) throws SQLException {
        return checker.obstacleChecker(h, w, d1, d2);
    }




}
