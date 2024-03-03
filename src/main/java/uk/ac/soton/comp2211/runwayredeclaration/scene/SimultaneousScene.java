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
        MenuItem sideView = new MenuItem("Side View");
        sideView.setOnAction(e -> homeWindow.startSideView());
        // Add menu items
        viewMenu.getItems().addAll(topView, sideView);


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

        // Direction Pane
        BorderPane directionPane = new BorderPane();
        displayStackPane.getChildren().add(directionPane);

        // Landing Direction
        Image arrow1 = new Image(getClass().getResource("/images/Arrow2.png").toExternalForm());
        ImageView landingArrow = new ImageView(arrow1);
        landingArrow.setPreserveRatio(true);
        landingArrow.setFitWidth(100);
        landingArrow.setRotate(180);
        VBox landingArrowBox = new VBox();
        landingArrowBox.setAlignment(Pos.CENTER);
        VBox arrowEmpty1 = new VBox();
        arrowEmpty1.getStyleClass().add("empty");
        VBox.setVgrow(arrowEmpty1, Priority.ALWAYS);
        Text landingText = new Text("Landing");
        landingText.getStyleClass().add("arrow-text");
        landingArrowBox.getChildren().addAll(landingArrow, landingText, arrowEmpty1);
        directionPane.setRight(landingArrowBox);

        // Takeoff Direction
        Image arrow2 = new Image(getClass().getResource("/images/Arrow1.png").toExternalForm());
        ImageView takeoffArrow = new ImageView(arrow2);
        takeoffArrow.setPreserveRatio(true);
        takeoffArrow.setFitWidth(100);
        VBox takeoffArrowBox = new VBox();
        takeoffArrowBox.setAlignment(Pos.CENTER);
        VBox arrowEmpty2 = new VBox();
        arrowEmpty2.getStyleClass().add("empty");
        VBox.setVgrow(arrowEmpty2, Priority.ALWAYS);
        Text takeoffText = new Text("Take off");
        takeoffText.getStyleClass().add("arrow-text");
        takeoffArrowBox.getChildren().addAll(takeoffArrow, takeoffText, arrowEmpty2);
        directionPane.setLeft(takeoffArrowBox);


        // Top-View Part
        StackPane topViewPane = new StackPane();
        topViewPane.getStyleClass().add("topView-background");
        displayBorderPane.setTop(topViewPane);
        topViewPane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(2));
        Image toprunwayImage = new Image(getClass().getResource("/images/Runway1.png").toExternalForm());
        ImageView toprunwayImageView = new ImageView(toprunwayImage);
        toprunwayImageView.setFitHeight(50);
        toprunwayImageView.setFitWidth(500);
        // Graded Area
        Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
        ImageView gradeAreaImageView = new ImageView(gradeArea);
        gradeAreaImageView.setFitHeight(200);
        gradeAreaImageView.setFitWidth(700);
        // Border for the views
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






}
