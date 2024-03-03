package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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


        BorderPane displayBorderPane = new BorderPane();
        BorderPane directionPane = new BorderPane();
        displayStackPane.getChildren().add(displayBorderPane);
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


        // Top-View Runway
        Image runwayImage = new Image(getClass().getResource("/images/Runway1.png").toExternalForm());
        ImageView runwayImageView = new ImageView(runwayImage);
        runwayImageView.setPreserveRatio(true);
        runwayImageView.setFitWidth(displayRunwayLength.getValue());
        // Graded Area
        Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
        ImageView gradeAreaImageView = new ImageView(gradeArea);
        gradeAreaImageView.setPreserveRatio(true);
        gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 50);


        // Runway Pane
        StackPane runwayPane = new StackPane();
        runwayPane.getChildren().addAll(runwayImageView);




        // Stop Ways
        HBox stopWay1 = new HBox();
        stopWay1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay1.prefWidthProperty().bind(stopWayLength);
        stopWay1.setAlignment(Pos.CENTER_LEFT);
        HBox stopWay2 = new HBox();
        stopWay2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay2.prefWidthProperty().bind(stopWayLength);
        stopWay2.setAlignment(Pos.CENTER_RIGHT);

        HBox stopWayEmpty = new HBox();
        stopWayEmpty.getStyleClass().add("empty");
        HBox.setHgrow(stopWayEmpty, Priority.ALWAYS);
        HBox stopWayPane = new HBox(stopWay1, stopWayEmpty, stopWay2);
        stopWayPane.setMaxHeight(70);


        runwayPane.getChildren().add(stopWayPane);

        // Empty boxes to push the runwayPane to the center
        HBox empty1 = new HBox();
        empty1.getStyleClass().add("empty");
        HBox.setHgrow(empty1, Priority.ALWAYS);
        HBox empty2 = new HBox();
        empty2.getStyleClass().add("empty");
        HBox.setHgrow(empty2, Priority.ALWAYS);
        HBox runwayPaneBox = new HBox(empty1, runwayPane ,empty2);

        // Plane Images
        Image planeImage = new Image(getClass().getResource("/images/Plane-TopView1.png").toExternalForm());
        ImageView planeImageView = new ImageView(planeImage);
        planeImageView.setPreserveRatio(true);
        planeImageView.setFitWidth(50);
        // HBox distance between plane and obstacle
        HBox planeObstacleDistance = new HBox();
        planeObstacleDistance.getStyleClass().add("empty");
        planeObstacleDistance.setPrefWidth(distBetweenPlaneObstacle.getValue());
        // Obstacle Images
        Image obstacleImage = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
        ImageView obstacleImageView = new ImageView(obstacleImage);
        obstacleImageView.setPreserveRatio(true);
        obstacleImageView.setFitWidth(30);

        // Plane & Obstacle Pane (Might change)
        HBox planeObstacleBox = new HBox();
        planeObstacleBox.setAlignment(Pos.CENTER);
        HBox frontPlaneEmpty = new HBox();
        frontPlaneEmpty.getStyleClass().add("empty");
        HBox.setHgrow(frontPlaneEmpty, Priority.ALWAYS);
        HBox backPlaneEmpty = new HBox();
        backPlaneEmpty.getStyleClass().add("empty");
        HBox.setHgrow(backPlaneEmpty, Priority.ALWAYS);





        planeObstacleBox.getChildren().addAll(planeImageView, planeObstacleDistance, obstacleImageView);





        displayStackPane.getChildren().addAll(gradeAreaImageView, runwayPaneBox, planeObstacleBox);


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







}
