package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
public class TopViewScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(TopViewScene.class);

    private VBox left_box;
    private VBox right_box;

    private HBox menuBox;

    private StackPane middleDisplayBox;

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param homeWindow the home window
     */
    public TopViewScene(HomeWindow homeWindow) {
        super(homeWindow);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new HomePane(homeWindow.getWidth(),homeWindow.getHeight());

        var topViewPane = new StackPane();
        topViewPane.setMaxWidth(homeWindow.getWidth());
        topViewPane.setMaxHeight(homeWindow.getHeight());

        root.getChildren().add(topViewPane);

        var mainPane = new BorderPane();
        mainPane.getStyleClass().add("topView-background");
        topViewPane.getChildren().add(mainPane);




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

        menuBox = new HBox(makeMenuBox());
        menuBox.getStyleClass().add("menu-box");
        mainPane.setTop(menuBox);
        BorderPane.setAlignment(menuBox, Pos.TOP_CENTER);

        // Middle Display Box Set-up
        middleDisplayBox = new StackPane(makeMiddleDisplayBox());
        middleDisplayBox.getStyleClass().add("sideView-background");
        BorderPane.setAlignment(middleDisplayBox, Pos.CENTER);
        mainPane.setCenter(middleDisplayBox);


        this.initialise();
    }

    /**
     * Create the results box
     * @return StackPane the middle display
     */
    private StackPane makeMiddleDisplayBox() {
        StackPane displayStackPane = new StackPane();



        Image runwayImage = new Image(getClass().getResource("/images/Runway1.png").toExternalForm());
        ImageView runwayImageView = new ImageView(runwayImage);

        runwayImageView.setPreserveRatio(true);
        runwayImageView.setFitWidth(500);


        Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
        ImageView gradeAreaImageView = new ImageView(gradeArea);

        gradeAreaImageView.setPreserveRatio(true);
        gradeAreaImageView.setFitWidth(600);


        displayStackPane.getChildren().addAll(gradeAreaImageView, runwayImageView);


        return displayStackPane;
    }

    /**
     * Create the menu box
     * @return the menu box
     */
    private HBox makeMenuBox() {
        // Create Menus
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(new MenuItem("Import XML"), new MenuItem("Export XML"), new MenuItem("Export Report"));

        // View Menu
        Menu viewMenu = new Menu("View");
        // Switch to side view
        MenuItem sideView = new MenuItem("Side View");
        sideView.setOnAction(e -> homeWindow.startSideView());
        // Switch to simultaneous view
        MenuItem simultaneous = new MenuItem("Simultaneous");
        simultaneous.setOnAction(e -> homeWindow.startSimultaneousView());
        // Add menu items
        viewMenu.getItems().addAll(sideView, simultaneous);

        // Help Menu
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(new MenuItem("About"), new MenuItem("Contact"));

        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);

        // Create Log out button
        Button logoutButton = new Button("Log out");
        //logoutButton.setOnAction(e -> homeWindow.startLogin());
        logoutButton.getStyleClass().add("logout-button");

        // Empty box to push the logout button to the right
        HBox empty = new HBox();
        empty.getStyleClass().add("empty");


        HBox.setHgrow(empty, Priority.ALWAYS); // This will push the logout button to the right

        HBox result = new HBox(menuBar, empty, logoutButton);
        HBox.setHgrow(result, Priority.ALWAYS);
        return (result);
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
