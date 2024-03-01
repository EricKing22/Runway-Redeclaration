package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimultaneousScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(SimultaneousScene.class);

    private VBox left_box;
    private VBox right_box;
    private HBox menuBox;

    private StackPane middleDisplayBox;

    private DoubleProperty stopWay = new SimpleDoubleProperty(20);

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param homeWindow the home window
     */
    public SimultaneousScene(HomeWindow homeWindow) {
        super(homeWindow);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new HomePane(homeWindow.getWidth(),homeWindow.getHeight());

        var simultaneousViewPane = new StackPane();
        simultaneousViewPane.setMaxWidth(homeWindow.getWidth());
        simultaneousViewPane.setMaxHeight(homeWindow.getHeight());

        root.getChildren().add(simultaneousViewPane);

        var mainPane = new BorderPane();
        simultaneousViewPane.getChildren().add(mainPane);
        mainPane.getStyleClass().add("simultaneous-background");

        // Left Box Set-up
        left_box = new VBox(2, makeResultsTPane(), makeCalcBreakTPane());
        left_box.getStyleClass().add("left-box");
        mainPane.setLeft(left_box);
        BorderPane.setAlignment(left_box, Pos.CENTER);


        // Right Box Set-up
        right_box = new VBox(2, makeAirportTPane(), makeObstacleTPane());
        right_box.getStyleClass().add("right-box");
        mainPane.setRight(right_box);
        BorderPane.setAlignment(right_box, Pos.CENTER);

        // Menu Set-up
        menuBox = new HBox(makeMenuBox());
        menuBox.getStyleClass().add("menu-box");
        BorderPane.setAlignment(menuBox, Pos.TOP_CENTER);
        mainPane.setTop(menuBox);

        // Middle Display Box Set-up
        middleDisplayBox = new StackPane(makeMiddleDisplayBox());
        middleDisplayBox.getStyleClass().add("sideView-background");
        BorderPane.setAlignment(middleDisplayBox, Pos.CENTER);
        mainPane.setCenter(middleDisplayBox);




        this.initialise();

    }

    /**
     * Create the menu box
     * @return HBox the Menu Bar
     */
    private HBox makeMenuBox(){
        // Create Menus
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(new MenuItem("Import XML"), new MenuItem("Export XML"), new MenuItem("Export Report"));

        // View Menu
        Menu viewMenu = new Menu("View");
        // Switch to side view
        MenuItem topView = new MenuItem("Top View");
        topView.setOnAction(e -> homeWindow.startTopView());
        // Switch to simultaneous view
        MenuItem simultaneous = new MenuItem("Simultaneous");
        simultaneous.setOnAction(e -> homeWindow.startSimultaneousView());
        // Add menu items
        viewMenu.getItems().addAll(topView, simultaneous);


        // Help Menu
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(new MenuItem("About"), new MenuItem("Contact"));

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);

        // Create Log out button
        Button logoutButton = new Button("Log out");
        //logoutButton.setOnAction(e -> homeWindow.startLogin());
        logoutButton.getStyleClass().add("logout-button");


        // Empty box to push the logout button to the right
        HBox empty = new HBox();
        empty.getStyleClass().add("empty");


        HBox.setHgrow(empty, Priority.ALWAYS); // This will push the logout button to the right

        HBox everything = new HBox(menuBar, empty, logoutButton);
        HBox.setHgrow(everything, Priority.ALWAYS);
        return (everything);
    }

    /**
     * Create the middle display box
     * @return StackPane Middle display box
     */

    public StackPane makeMiddleDisplayBox(){
        StackPane displayStackPane = new StackPane();
        BorderPane displayBorderPane = new BorderPane();
        displayStackPane.getChildren().add(displayBorderPane);


        // Top-View Part
        StackPane topViewPane = new StackPane();
        topViewPane.getStyleClass().add("topView-background");
        displayBorderPane.setTop(topViewPane);
        topViewPane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(2));
        Image toprunwayImage = new Image(getClass().getResource("/images/Runway1.png").toExternalForm());
        ImageView toprunwayImageView = new ImageView(toprunwayImage);

        toprunwayImageView.setFitHeight(50);
        toprunwayImageView.setFitWidth(500);


        Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
        ImageView gradeAreaImageView = new ImageView(gradeArea);

        gradeAreaImageView.setFitHeight(200);
        gradeAreaImageView.setFitWidth(700);


        topViewPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2, 0))));
        topViewPane.getChildren().addAll(gradeAreaImageView, toprunwayImageView);




        // Side-View Part
        BorderPane sideViewPane = new BorderPane();
        sideViewPane.setBackground(new Background(new BackgroundFill(Color.web("#F8FCEC"), CornerRadii.EMPTY, Insets.EMPTY)));
        displayBorderPane.setBottom(sideViewPane);



        // Sky Part
        StackPane bluePane = new StackPane();
        bluePane.getStyleClass().add("sideView-background");
        bluePane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(4));
        sideViewPane.setTop(bluePane);


        // Ground Part
        BorderPane groundPane = new BorderPane();
        groundPane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(4));
        groundPane.setBackground(new Background(new BackgroundFill(Color.web("#A9D18E"), CornerRadii.EMPTY, Insets.EMPTY)));
        sideViewPane.setBottom(groundPane);

        // Empty boxes to push the runway to the center
        HBox empty1 = new HBox();
        empty1.getStyleClass().add("empty");
        HBox.setHgrow(empty1, Priority.ALWAYS);
        HBox empty2 = new HBox();
        empty2.getStyleClass().add("empty");
        HBox.setHgrow(empty2, Priority.ALWAYS);
        // Runway Image
        Image siderunwayImage = new Image(getClass().getResource("/images/Runway2.png").toExternalForm());
        ImageView siderunwayImageView = new ImageView(siderunwayImage);
        siderunwayImageView.setPreserveRatio(true);
        siderunwayImageView.setFitWidth(500);


        StackPane runwayPane = new StackPane();
        runwayPane.getChildren().add(siderunwayImageView);
        runwayPane.setPrefWidth(siderunwayImageView.getFitWidth());
        runwayPane.setPrefHeight(siderunwayImageView.getFitHeight());


        // Stop ways
        HBox stopWay1 = new HBox();
        stopWay1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay1.prefWidthProperty().bind(stopWay);
        stopWay1.setAlignment(Pos.CENTER_LEFT);
        HBox stopWay2 = new HBox();
        stopWay2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay2.prefWidthProperty().bind(stopWay);
        stopWay2.setAlignment(Pos.CENTER_RIGHT);
        BorderPane stopWayPane = new BorderPane();
        stopWayPane.setLeft(stopWay1);
        stopWayPane.setRight(stopWay2);

        runwayPane.getChildren().add(stopWayPane);



        HBox hBox = new HBox(empty1, runwayPane ,empty2);
        hBox.setAlignment(Pos.TOP_CENTER);


        groundPane.setTop(hBox);



        return displayStackPane;
    }



    //all this below is for the left and right
    public TitledPane makeResultsTPane(){
        TitledPane tpane = new TitledPane();
        tpane.setText("Results:");
        tpane.setCollapsible(true);



        GridPane gpanething = new GridPane();
        gpanething.setPadding(new Insets(10));
        gpanething.setHgap(10);
        gpanething.setVgap(10);
        //gpanething. getStyleClass().add(getClass().getResource("/style/all.css").toExternalForm());
        String[] headers = {"Runway", "TORA", "TODA", "LDA", "ASDA"};
        for (int i = 0; i < headers.length; i++) {
            Label label = new Label(headers[i]);
            label.getStyleClass().add("header-label");
            gpanething.add(label, i, 0);
        }

        Label lblOriginal = new Label("Original Values");
        lblOriginal.setMaxWidth(Double.MAX_VALUE);
        lblOriginal.getStyleClass().add("center-label");
        GridPane.setColumnSpan(lblOriginal, GridPane.REMAINING);
        GridPane.setHalignment(lblOriginal, HPos.CENTER);
        gpanething.add(lblOriginal, 0, 1);

        for (int row = 2; row < 4; row++) {
            for (int col = 0; col < headers.length; col++) {
                gpanething.add(new Label("1000m"), col, row);
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
                gpanething.add(new Label("750m"), col, row);
            }
        }


        tpane.setContent(gpanething);
        return tpane;
    }



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
}
