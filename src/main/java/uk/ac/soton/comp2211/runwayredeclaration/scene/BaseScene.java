package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;


/**
 * A Base Scene used in the game. Handles common functionality between all scenes.
 */
public abstract class BaseScene {

    protected final HomeWindow homeWindow;

    protected HomePane root;
    protected Scene scene;

    protected DoubleProperty stopWayLength = new SimpleDoubleProperty(40);

    protected DoubleProperty clearWayLength = new SimpleDoubleProperty(60);

    protected DoubleProperty distBetweenPlaneObstacle = new SimpleDoubleProperty(10);

    protected DoubleProperty displayRunwayLength = new SimpleDoubleProperty(650);

    protected DoubleProperty displayThreshold = new SimpleDoubleProperty();

    protected DoubleProperty TORA = new SimpleDoubleProperty(500);
    protected DoubleProperty TODA = new SimpleDoubleProperty(600);
    protected DoubleProperty ASDA = new SimpleDoubleProperty(700);
    protected DoubleProperty LDA = new SimpleDoubleProperty(800);


    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     * @param homeWindow the home window
     */
    public BaseScene(HomeWindow homeWindow) {
        this.homeWindow = homeWindow;
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

        for (int row = 2; row < 4; row++) {
            for (int col = 0; col < headers.length; col++) {
                Label label = new Label("1000m");
                label.getStyleClass().add("grid-pane-label");
                GridPane.setHalignment(label, HPos.CENTER); // For horizontal alignment
                GridPane.setValignment(label, VPos.CENTER); // For vertical alignment
                gpanething.add(label, col, row);
            }
        }

        Label lblRecalculated = new Label("New Values");
        lblRecalculated.setMaxWidth(Double.MAX_VALUE);
        lblRecalculated.getStyleClass().add("center-label");
        GridPane.setColumnSpan(lblRecalculated, GridPane.REMAINING);
        GridPane.setHalignment(lblRecalculated, HPos.CENTER);
        gpanething.add(lblRecalculated, 0, 4);

        // Adding two rows of arbitrary values after "Re-Calculated Values"
        for (int row = 5; row < 7; row++) {
            for (int col = 0; col < headers.length; col++) {
                Label label = new Label("750m");
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

        //part to create hbo for upper bit

        HBox tabs = new HBox(5);
        tabs.setPadding(new Insets(5));
        ToggleButton buttonTORA = new ToggleButton("TORA");
        ToggleButton buttonTODA = new ToggleButton("TODA");
        ToggleButton buttonLDA = new ToggleButton("LDA");
        ToggleButton buttonASDA = new ToggleButton("ASDA");

        ToggleGroup toggleGroup = new ToggleGroup();
        buttonTORA.setToggleGroup(toggleGroup);
        buttonTODA.setToggleGroup(toggleGroup);
        buttonLDA.setToggleGroup(toggleGroup);
        buttonASDA.setToggleGroup(toggleGroup);

        tabs.getChildren().addAll(buttonTORA, buttonTODA, buttonLDA, buttonASDA);

        // White box below the tabs
        StackPane whiteBox = new StackPane();
        whiteBox.setPrefSize(200, 375); // Set your preferred size
        whiteBox.setStyle("-fx-background-color: white; -fx-border-color: black;");

        // Adding tabs and white box to a VBox
        VBox vbox = new VBox(tabs, whiteBox);

        // Set the content of the TitledPane
        tpane2.setContent(vbox);

        // Apply CSS to the scene
        return tpane2;
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
        airportGrid.addRow(2, new Label("Length:"),  new Text("5555.0m"));

        airportGrid.addRow(3, new Label("Threshold:"), new Text("1111.0m"));
        airportGrid.addRow(4, new Label("Clearway:"), new Text("100.0m"));
        airportGrid.addRow(5, new Label("Stopway:"), new Text("80.0m"));

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
        obstacleBox.getChildren().addAll(new Label("Height:"), new TextField());
        obstacleBox.getChildren().addAll(new Label("Width:"), new TextField());
        obstacleBox.getChildren().addAll(new Label("Distance from plane:"), new TextField());
        obstacleBox.getChildren().addAll(new Label("Distance from centerline:"), new TextField());

        HBox buttons = new HBox(5);
        buttons.getChildren().add(new Button("Edit"));
        buttons.getChildren().add(new Button("Add"));
        obstacleBox.getChildren().add(buttons);



        Button recalculateB = new Button("Recalculate");
        recalculateB.setMaxWidth(Double.MAX_VALUE);

        obstacleGrid.addRow(0, new Label("Obstacle: "), obstacle);
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
