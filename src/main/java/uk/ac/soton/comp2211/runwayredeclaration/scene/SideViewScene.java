package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import javafx.scene.layout.*;
import javafx.application.Platform;
import javafx.scene.text.Text;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class SideViewScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(SideViewScene.class);


    private VBox left_box;
    private VBox right_box;

    private HBox menuBox;

    private StackPane middleDisplayBox;

    private DoubleProperty stopWay = new SimpleDoubleProperty(20);


    /**
     * Create a new side view scene
     * @param homeWindow the home Window this will be displayed in
     */
    public SideViewScene(HomeWindow homeWindow) {
        super(homeWindow);
        logger.info("Creating SideView Scene");
    }

    @Override
    public void initialise() {

    }

    /**
     * Build the layout of the scene
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new HomePane(homeWindow.getWidth(),homeWindow.getHeight());

        var sideViewPane = new StackPane();
        sideViewPane.setMaxWidth(homeWindow.getWidth());
        sideViewPane.setMaxHeight(homeWindow.getHeight());

        root.getChildren().add(sideViewPane);

        var mainPane = new BorderPane();
        mainPane.getStyleClass().add("sideView-background");
        sideViewPane.getChildren().add(mainPane);


        // Left Box Set-up
        left_box = new VBox(makeResultsBox());
        left_box.getStyleClass().add("left-box");
        mainPane.setLeft(left_box);
        BorderPane.setAlignment(left_box, Pos.CENTER);


        // Right Box Set-up
        right_box = new VBox();
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
     * Create the middle display box
     * @return StackPane Middle display box
     */

    public StackPane makeMiddleDisplayBox(){
        StackPane displayStackPane = new StackPane();
        BorderPane displayBorderPane = new BorderPane();
        displayStackPane.getChildren().add(displayBorderPane);

        // Sky Part
        GridPane bluePane = new GridPane();
        bluePane.getStyleClass().add("sideView-background");
        bluePane.prefHeightProperty().bind(displayStackPane.heightProperty().divide(2));
        displayBorderPane.setTop(bluePane);

        // Ground Part
        BorderPane groundPane = new BorderPane();
        groundPane.setBackground(new Background(new BackgroundFill(Color.web("#A9D18E"), CornerRadii.EMPTY, Insets.EMPTY)));
        groundPane.prefHeightProperty().bind(displayStackPane.heightProperty().divide(2));
        displayBorderPane.setBottom(groundPane);

        // Empty boxes to push the runway to the center
        HBox empty1 = new HBox();
        empty1.getStyleClass().add("empty");
        HBox.setHgrow(empty1, Priority.ALWAYS);
        HBox empty2 = new HBox();
        empty2.getStyleClass().add("empty");
        HBox.setHgrow(empty2, Priority.ALWAYS);
        // Runway Image
        Image runwayImage = new Image(getClass().getResource("/images/Runway2.png").toExternalForm());
        ImageView runwayImageView = new ImageView(runwayImage);
        runwayImageView.setPreserveRatio(true);
        runwayImageView.setFitWidth(500);


        StackPane runwayPane = new StackPane();
        runwayPane.getChildren().add(runwayImageView);
        runwayPane.setPrefWidth(runwayImageView.getFitWidth());
        runwayPane.setPrefHeight(runwayImageView.getFitHeight());


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

        HBox result = new HBox(menuBar, empty, logoutButton);
        HBox.setHgrow(result, Priority.ALWAYS);
        return (result);
    }

    /**
     * Create the results box
     * @return VBox
     */
    public VBox makeResultsBox(){
        VBox calculations = new VBox();


        Text justResults = new Text("Results");


        Image image = new Image(getClass().getResource("/images/foldButton.png").toExternalForm());
        ImageView foldButtonImage = new ImageView(image);
        foldButtonImage.setFitHeight(9);
        foldButtonImage.setFitWidth(9);
        Button foldButton = new Button();
        foldButton.setGraphic(foldButtonImage);

        HBox emptyR = new HBox();
        emptyR.getStyleClass().add("empty");
        HBox.setHgrow(emptyR, Priority.ALWAYS);

        HBox topResultsBar = new HBox(justResults, emptyR, foldButton);


        return new VBox(topResultsBar);
    }
}
