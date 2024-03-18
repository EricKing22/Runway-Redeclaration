package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.runwayredeclaration.Component.*;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


public class SideViewScene extends BaseScene{

    private VBox left_box;
    private VBox right_box;

    private HBox menuBox;

    private StackPane middleDisplayBox;




    private BorderPane mainPane = new BorderPane();

    private BorderPane bluePane = new BorderPane();

    private BorderPane groundPane = new BorderPane();

    private StackPane displayStackPaneTop;



    /**
     * Create a new side view scene
     * @param homeWindow the home Window this will be displayed in
     */
    public SideViewScene(HomeWindow homeWindow) {
        super(homeWindow);
    }


    @Override
    public void initialise() {

    }

    /**
     * Build the layout of the scene
     */
    @Override
    public void build() {

        root = new HomePane(homeWindow.getWidth(),homeWindow.getHeight());

        var sideViewPane = new StackPane();
        sideViewPane.setMaxWidth(homeWindow.getWidth());
        sideViewPane.setMaxHeight(homeWindow.getHeight());

        root.getChildren().add(sideViewPane);


        mainPane.getStyleClass().add("sideView-background");
        sideViewPane.getChildren().add(mainPane);


        // Left Box Set-up
        left_box = new VBox(2, makeResultsTPane(), makeCalcBreakTPane());
        left_box.getStyleClass().add("left-box");
        mainPane.setLeft(left_box);
        BorderPane.setAlignment(left_box, Pos.CENTER);


        // Right Box Set-up
        right_box = new VBox(2, makeAirportTPane(), makeAirplaneTPane(), makeObstacleTPane());
        right_box.getStyleClass().add("right-box");
        mainPane.setRight(right_box);
        BorderPane.setAlignment(right_box, Pos.CENTER);

        // Menu Set-up
        menuBox = new HBox(makeMenuBox());
        menuBox.getStyleClass().add("menu-box");
        BorderPane.setAlignment(menuBox, Pos.TOP_CENTER);
        mainPane.setTop(menuBox);

        // Middle Display Box Set-up
        middleDisplayBox = new StackPane(makeSideViewMiddleDisplayBox(), makeDirectionPane());
        middleDisplayBox.getStyleClass().add("sideView-background");
        BorderPane.setAlignment(middleDisplayBox, Pos.CENTER);
        mainPane.setCenter(middleDisplayBox);


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

        this.initialise();
    }

    /**
     * Add the direction arrows
     * @return borderPane the direction pane
     */
    public BorderPane makeDirectionPane(){
        BorderPane directionPane = new BorderPane();

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
        landingArrowBox.getChildren().addAll(landingArrow, arrowEmpty1);
        landingArrowBox.setAlignment(Pos.CENTER_RIGHT);
        directionPane.setRight(landingArrowBox);

        // Takeoff Direction
        Image arrow2 = new Image(getClass().getResource("/images/Arrow1.png").toExternalForm());
        ImageView takeoffArrow = new ImageView(arrow2);
        takeoffArrow.setPreserveRatio(true);
        takeoffArrow.setFitWidth(100);
        VBox takeoffArrowBox = new VBox();
        takeoffArrowBox.setAlignment(Pos.CENTER_LEFT);
        VBox arrowEmpty2 = new VBox();
        arrowEmpty2.getStyleClass().add("empty");
        VBox.setVgrow(arrowEmpty2, Priority.ALWAYS);
        Text takeoffText = new Text("Take off");
        takeoffText.getStyleClass().add("arrow-text");
        takeoffArrowBox.getChildren().addAll(takeoffArrow, arrowEmpty2);
        directionPane.setLeft(takeoffArrowBox);


        return directionPane;
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

        // Switch to Side View
        MenuItem sideView = new MenuItem("Side View");


        // Switch to Top view
        MenuItem topView = new MenuItem("Top View");

        // Switch to simultaneous view
        MenuItem simultaneous = new MenuItem("Simultaneous");


        sideView.setOnAction(e -> {
            middleDisplayBox.getChildren().clear();
            obstacleBox.setAlignment(Pos.BOTTOM_LEFT);
            middleDisplayBox.getChildren().addAll(makeSideViewMiddleDisplayBox(), makeDirectionPane());
            viewMenu.getItems().clear();
            viewMenu.getItems().addAll(topView, simultaneous);
        });

        topView.setOnAction(e -> {
            middleDisplayBox.getChildren().clear();
            obstacleBox.setAlignment(Pos.CENTER_LEFT);
            middleDisplayBox.getChildren().addAll(makeTopViewMiddleDisplayBox(), makeDirectionPane());
            viewMenu.getItems().clear();
            viewMenu.getItems().addAll(sideView, simultaneous);
        });

        simultaneous.setOnAction(e -> {
            middleDisplayBox.getChildren().clear();
            middleDisplayBox.getChildren().addAll(makeSimultaneousMiddleDisplayBox(), makeDirectionPane());
            viewMenu.getItems().clear();
            viewMenu.getItems().addAll(sideView, topView);
        });

        // Add menu items
        viewMenu.getItems().addAll(topView, simultaneous);


        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem colourSettings = new MenuItem("Colour Schemes");
        colourSettings.setOnAction(e -> makeColourSettingPage());
        helpMenu.getItems().addAll(new MenuItem("About"), new MenuItem("Contact"), colourSettings);

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


    public StackPane makeDisplacedThreshold (){
        StackPane displacedThresholdStackPane = new StackPane();
        displacedThresholdStackPane.getStyleClass().add("empty");
        displacedThresholdStackPane.setAlignment(Pos.CENTER_LEFT);


        displacedThresholdStackPane.getChildren().add(displacedThresholdBox);

        return displacedThresholdStackPane;

    }

    /**
     * StackPane for all the indicators
     * @return StackPane the indicators
     */
    public StackPane makeIndicators(){


        // TORA HBox
        toraBox = new HBox();
        toraBox.setMinWidth(125 * 2 + 550);
        toraBox.getStyleClass().add("empty");
        toraBox.setAlignment(Pos.CENTER_LEFT);
        HBox borderToTORA = new HBox();
        borderToTORA.getStyleClass().add("empty");
        displayBorderToTORA.bind(displayBorderToRunway);
        borderToTORA.prefWidthProperty().bind(displayBorderToTORA);
        DashedLine toraStart = new DashedLine(0.1, 500);
        DashedLine toraEnd = new DashedLine(0.1, 500);

        // TORA Distance Box (Line + Text)
        VBox toraDistanceBox = new VBox();
        toraDistanceBox.getStyleClass().add("empty");
        toraDistanceBox.prefWidthProperty().bind(displayTORA);
        toraDistanceBox.setMaxHeight(500);
        toraDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine toraArrow = new DashedLine(displayTORA.get(), 0.1, false);
        Text toraText = new Text("TORA");
        toraText.getStyleClass().add("arrow-text");
        toraDistanceBox.getChildren().addAll(toraArrow, toraText, new EmptyVBox(0.1, 50));

        toraBox.getChildren().addAll(borderToTORA, toraStart, toraDistanceBox, toraEnd);



        // TODA HBox
        todaBox = new HBox();
        todaBox.setMinWidth(125 * 2 + 550);
        todaBox.getStyleClass().add("empty");
        todaBox.setAlignment(Pos.CENTER_LEFT);
        DashedLine todaStart = new DashedLine(0.1, 500);
        DashedLine todaEnd = new DashedLine(0.1, 500);

        VBox todaDistanceBox = new VBox();
        todaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        todaDistanceBox.setMaxHeight(500);
        todaDistanceBox.getStyleClass().add("empty");



        displayTODA.bind(Bindings.when(
                        subRunway1.getClearwayLength().isNotEqualTo(0).and(subRunway1.getTORA().isNotEqualTo(subRunway1.getTODA())))
                .then(Bindings.add(displayTORA, displayClearWayLength))
                .otherwise(displayTORA));

        todaDistanceBox.prefWidthProperty().bindBidirectional(displayTODA);
        DashedLine todaArrow = new DashedLine(displayTODA.get(), 0.1, false);
        Text todaText = new Text("TODA");
        todaText.getStyleClass().add("arrow-text");
        todaDistanceBox.getChildren().addAll(todaArrow, todaText, new EmptyVBox(0.1, 100));

        HBox borderToTODA = new HBox();
        borderToTODA.getStyleClass().add("empty");
        displayBorderToTODA.bind(displayBorderToRunway);
        borderToTODA.prefWidthProperty().bind(displayBorderToTODA);


        todaBox.getChildren().addAll(borderToTODA, todaStart, todaDistanceBox, todaEnd);


        // ASDA HBox
        asdaBox = new HBox();
        asdaBox.setMinWidth(125 * 2 + 550);
        asdaBox.getStyleClass().add("empty");
        asdaBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine asdaStart = new DashedLine(0.1, 500);
        DashedLine asdaEnd = new DashedLine(0.1, 500);

        VBox asdaDistanceBox = new VBox();
        asdaDistanceBox.getStyleClass().add("empty");
        displayASDA.bind(Bindings.when(
                        subRunway1.getStopwayLength().isNotEqualTo(0).and(subRunway1.getTORA().isNotEqualTo(subRunway1.getASDA())))
                .then(Bindings.add(displayTORA, displayStopWayLength))
                .otherwise(displayTORA));
        asdaDistanceBox.prefWidthProperty().bindBidirectional(displayASDA);
        asdaDistanceBox.setMaxHeight(500);
        asdaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine asdaArrow = new DashedLine(displayASDA.get(), 0.1, false);
        Text asdaText = new Text("ASDA");
        asdaText.getStyleClass().add("arrow-text");
        asdaDistanceBox.getChildren().addAll(asdaArrow, asdaText, new EmptyVBox(0.1, 150));


        HBox borderToASDA = new HBox();
        borderToASDA.getStyleClass().add("empty");
        displayBorderToASDA.bind(displayBorderToRunway);
        borderToASDA.prefWidthProperty().bind(displayBorderToASDA);

        asdaBox.getChildren().addAll(borderToASDA, asdaStart, asdaDistanceBox, asdaEnd);

        // RESA HBox
        resaBox = new HBox();
        resaBox.getStyleClass().add("empty");
        resaBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine resaStart = new DashedLine(0.1, 150);
        DashedLine resaEnd = new DashedLine(0.1, 150);

        VBox resaDistanceBox = new VBox();
        resaDistanceBox.getStyleClass().add("empty");
        resaDistanceBox.prefWidthProperty().bindBidirectional(displayRESA);
        resaDistanceBox.setMaxHeight(500);
        resaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine resaArrow = new DashedLine(displayRESA.get(), 0.1, false);
        Text resaText = new Text("RESA");
        resaText.getStyleClass().add("arrow-text");
        resaText.setStyle("-fx-font-size: 10");
        resaDistanceBox.getChildren().addAll(resaArrow, resaText, new EmptyVBox(0.1, 200));

        HBox displayBorderToRESABox = new HBox();
        displayBorderToRESABox.getStyleClass().add("empty");
        displayBorderToRESABox.prefWidthProperty().bind(displayBorderToRESA);
        resaBox.getChildren().addAll(displayBorderToRESABox, resaStart, resaDistanceBox, resaEnd);


        // Blast Allowance HBox
        blastAllowanceBox = new HBox();
        blastAllowanceBox.getStyleClass().add("empty");
        blastAllowanceBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine blastAllowanceStart = new DashedLine(0.1, 150);
        DashedLine blastAllowanceEnd = new DashedLine(0.1, 150);

        VBox blastAllowanceDistanceBox = new VBox();
        blastAllowanceDistanceBox.getStyleClass().add("empty");
        blastAllowanceDistanceBox.prefWidthProperty().bindBidirectional(displayBlastAllowance);
        blastAllowanceDistanceBox.setMaxHeight(500);
        blastAllowanceDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine blastAllowanceArrow = new DashedLine(displayBlastAllowance.get(), 0.1, false);
        Text blastAllowanceText = new Text("Blast\nAllowance");
        blastAllowanceText.getStyleClass().add("arrow-text");
        blastAllowanceText.setStyle("-fx-font-size: 10");
        blastAllowanceDistanceBox.getChildren().addAll(blastAllowanceArrow, blastAllowanceText, new EmptyVBox(0.1, 200));

        HBox borderToBlastAllowance = new HBox();
        borderToBlastAllowance.getStyleClass().add("empty");
        borderToBlastAllowance.prefWidthProperty().bind(Bindings.add(displayBorderToRunway, Bindings.add(displayRunwayToPlane, displayPlaneWidth)));
        blastAllowanceBox.getChildren().addAll(borderToBlastAllowance, blastAllowanceStart, blastAllowanceDistanceBox, blastAllowanceEnd);





        // LDA HBox
        ldaBox = new HBox();
        ldaBox.getStyleClass().add("empty");
        ldaBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine ldaStart = new DashedLine(0.1, 500);
        DashedLine ldaEnd = new DashedLine(0.1, 500);

        VBox ldaDistanceBox = new VBox();
        ldaDistanceBox.getStyleClass().add("empty");
        ldaDistanceBox.prefWidthProperty().bindBidirectional(displayLDA);
        ldaDistanceBox.setMaxHeight(500);
        ldaDistanceBox.setAlignment(Pos.TOP_CENTER);
        DashedLine ldaArrow = new DashedLine(displayLDA.get(), 0.1, false);
        Text ldaText = new Text("LDA");
        ldaText.getStyleClass().add("arrow-text");
        ldaDistanceBox.getChildren().addAll(new EmptyVBox(0.1, 150), ldaArrow, ldaText);

        HBox borderToLDA = new HBox();
        borderToLDA.getStyleClass().add("empty");
        borderToLDA.prefWidthProperty().bind(displayBorderToLDA);
        ldaBox.getChildren().addAll(borderToLDA, ldaStart, ldaDistanceBox, ldaEnd);


        indicatorsSubRunway1.getChildren().addAll(toraBox, todaBox, asdaBox, resaBox, blastAllowanceBox, ldaBox);






        return indicatorsSubRunway1;
    }



    /**
     * Create the middle display box
     * @return StackPane Middle display box
     */
    public StackPane makeSideViewMiddleDisplayBox(){
        StackPane displayStackPane = new StackPane();
        BorderPane displayBorderPane = new BorderPane();

        displayStackPane.getChildren().add(displayBorderPane);


        // Sky Part

        bluePane.getStyleClass().add("sideView-background");
        bluePane.prefHeightProperty().bind(displayStackPane.heightProperty().divide(2));
        displayBorderPane.setTop(bluePane);


        // Plane and Obstacle Pane
        StackPane planeObstaclePane = new StackPane();

        obstacleBox.setAlignment(Pos.BOTTOM_LEFT);
        planeBox.setAlignment(Pos.BOTTOM_LEFT);

        // Plane Image
        Image planeImage = new Image(getClass().getResource("/images/Plane1.png").toExternalForm());
        ImageView planeImageView = new ImageView(planeImage);
        planeImageView.setPreserveRatio(true);
        planeImageView.setFitWidth(displayPlaneWidth.getValue());



        HBox borderToPlane = new HBox();
        borderToPlane.getStyleClass().add("empty");

        SimpleDoubleProperty displayBorderToPlaneTail = new SimpleDoubleProperty();
        SimpleDoubleProperty displayBorderToPlaneNose = new SimpleDoubleProperty();

        displayBorderToPlaneTail.bind(Bindings.subtract( (Bindings.add(displayBorderToRunway, displayRunwayToPlane) ), displayPlaneWidth));
        displayBorderToPlaneNose.bind(Bindings.add(displayBorderToRunway, displayRunwayToPlane));
        borderToPlane.prefWidthProperty().bind(displayBorderToPlaneTail);


        planeBox.getChildren().clear();
        planeBox.getStyleClass().add("empty");
        planeBox.setAlignment(Pos.CENTER_LEFT);
        planeBox.getChildren().addAll(borderToPlane, planeImageView);



        planeObstaclePane.getChildren().addAll(planeBox, obstacleBox);

        bluePane.setBottom(planeObstaclePane);


        // Ground Part

        groundPane.setBackground(new Background(new BackgroundFill(Color.web("#A9D18E"), CornerRadii.EMPTY, Insets.EMPTY)));
        groundPane.prefHeightProperty().bind(displayStackPane.heightProperty().divide(2));

        displayBorderPane.setBottom(groundPane);


        // Runway Image
        Image runwayImage = new Image(getClass().getResource("/images/Runway2.png").toExternalForm());
        ImageView runwayImageView = new ImageView(runwayImage);
        runwayImageView.setPreserveRatio(true);
        runwayImageView.setFitWidth(displayRunwayLength.getValue());
        runwayImageView.setFitHeight(100);


        // Runway Pane
        HBox runwayBox = new HBox();
        runwayBox.getStyleClass().add("empty");
        runwayBox.setAlignment(Pos.CENTER_LEFT);


        // Stop Ways
        HBox stopWay1 = new HBox();
        stopWay1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay1.prefWidthProperty().bind(displayStopWayLength);
        stopWay1.visibleProperty().bind(stopWayLength2.greaterThan(0));
        stopWay1.setAlignment(Pos.CENTER_LEFT);
        HBox stopWay2 = new HBox();
        stopWay2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay2.prefWidthProperty().bind(displayStopWayLength);
        stopWay2.setPrefHeight(runwayImageView.getFitHeight());
        stopWay2.visibleProperty().bind(stopWayLength1.greaterThan(0));
        stopWay2.setAlignment(Pos.CENTER_LEFT);

        HBox stopWayBox = new HBox();
        HBox emptyBoxBetweenStopWays = new HBox();
        emptyBoxBetweenStopWays.getStyleClass().add("empty");
        emptyBoxBetweenStopWays.setPrefHeight(displayStackPane.getHeight());
        emptyBoxBetweenStopWays.setPrefWidth(displayRunwayLength.get());
        stopWayBox.getChildren().addAll(stopWay1, emptyBoxBetweenStopWays, stopWay2);
        stopWayBox.setAlignment(Pos.CENTER);

        runwayBox.getChildren().add(runwayImageView);


        // Empty boxes to push the runway to the center
        HBox borderToRunway1 = new HBox();
        borderToRunway1.getStyleClass().add("empty");
        borderToRunway1.setPrefWidth(displayBorderToRunway.getValue());
        HBox borderToRunway2 = new HBox();
        borderToRunway2.getStyleClass().add("empty");
        borderToRunway2.setPrefWidth(displayBorderToRunway.getValue());
        HBox runwayPaneBox = new HBox(borderToRunway1, runwayBox, borderToRunway2);
        runwayPaneBox.setAlignment(Pos.CENTER_LEFT);


        StackPane runwayPane = new StackPane();
        runwayPane.getStyleClass().add("empty");
        runwayPane.setAlignment(Pos.CENTER_LEFT);
        runwayPane.getChildren().add(runwayPaneBox);
        runwayPane.getChildren().add(stopWayBox);
        runwayPane.setPrefHeight(Region.USE_PREF_SIZE);

        groundPane.setTop(runwayPane);

        // Clearways
        HBox clearWayBox = new HBox();
        clearWayBox.getStyleClass().add("empty");
        clearWayBox.setAlignment(Pos.CENTER);

        HBox clearWay1 = new HBox();
        clearWay1.getStyleClass().add("clearway-box");
        clearWay1.visibleProperty().bind(clearWayLength2.greaterThan(0));
        clearWay1.prefWidthProperty().bind(displayClearWayLength);

        HBox emptyBoxBetweenClearWay = new HBox();
        emptyBoxBetweenClearWay.getStyleClass().add("empty");
        emptyBoxBetweenClearWay.setPrefWidth(displayRunwayLength.get());

        HBox clearWay2 = new HBox();
        clearWay2.getStyleClass().add("clearway-box");
        clearWay2.visibleProperty().bind(clearWayLength1.greaterThan(0));
        clearWay2.prefWidthProperty().bind(displayClearWayLength);



        clearWayBox.getChildren().addAll(clearWay1, emptyBoxBetweenClearWay, clearWay2);
        displayStackPane.getChildren().add(clearWayBox);

        // Designator Display
        Text designator1 = new Text();
        designator1.getStyleClass().add("display-designator-text");
        designator1.textProperty().bind(subRunway1.getDesignator());
        Text designator2 = new Text();
        designator2.getStyleClass().add("display-designator-text");
        designator2.textProperty().bind(subRunway2.getDesignator());

        HBox emptyHbox = new HBox();
        emptyHbox.getStyleClass().add("empty");
        HBox.setHgrow(emptyHbox, Priority.ALWAYS);


        HBox designatorBox = new HBox();
        designatorBox.getStyleClass().add("empty");
        designatorBox.setAlignment(Pos.CENTER);
        designatorBox.getChildren().addAll(designator1, emptyHbox, designator2);

       displayStackPane.getChildren().add(designatorBox);

       displayStackPane.getChildren().add(makeDisplacedThreshold());

       displayStackPane.getChildren().add(makeIndicators());


        return displayStackPane;
    }

    /**
     * Create the top view middle display box
     * @return StackPane Middle display box
     */
    public StackPane makeTopViewMiddleDisplayBox(){

        displayStackPaneTop = new StackPane();
        BorderPane displayBorderPane = new BorderPane();
        displayStackPaneTop.getChildren().add(displayBorderPane);
        changeColourSchemeTop();

        // Top-View Runway
        Image runwayImage = new Image(getClass().getResource("/images/Runway1.png").toExternalForm());
        ImageView runwayImageView = new ImageView(runwayImage);
        runwayImageView.setPreserveRatio(true);
        runwayImageView.setFitWidth(displayRunwayLength.getValue());
        // Graded Area
        Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
        ImageView gradeAreaImageView = new ImageView(gradeArea);
        gradeAreaImageView.setPreserveRatio(true);
        gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);

        // Runway HBox
        HBox runwayBox = new HBox();
        runwayBox.getStyleClass().add("empty");
        runwayBox.setAlignment(Pos.CENTER_LEFT);


        // Stop Ways
        HBox stopWay1 = new HBox();
        stopWay1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay1.prefWidthProperty().bind(displayStopWayLength);
        stopWay1.prefHeightProperty().bind(runwayImageView.fitHeightProperty());
        stopWay1.visibleProperty().bind(stopWayLength2.greaterThan(0));
        HBox stopWay2 = new HBox();
        stopWay2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay2.prefWidthProperty().bind(displayStopWayLength);
        stopWay1.prefHeightProperty().bind(runwayImageView.fitHeightProperty());
        stopWay2.visibleProperty().bind(stopWayLength1.greaterThan(0));


        stopWay1.setMaxHeight(runwayImageView.getFitWidth() / runwayImage.getWidth() * runwayImage.getHeight());
        stopWay2.setMaxHeight(runwayImageView.getFitWidth() / runwayImage.getWidth() * runwayImage.getHeight());

        HBox stopWayBox = new HBox();
        HBox emptyBoxBetweenStopWays = new HBox();
        emptyBoxBetweenStopWays.getStyleClass().add("empty");
        emptyBoxBetweenStopWays.setPrefHeight(displayStackPaneTop.getHeight());
        emptyBoxBetweenStopWays.setPrefWidth(displayRunwayLength.get());
        stopWayBox.getChildren().addAll(stopWay1, emptyBoxBetweenStopWays, stopWay2);
        stopWayBox.setAlignment(Pos.CENTER);

        stopWayBox.setPrefHeight(100);



        runwayBox.getChildren().add(runwayImageView);


        // Empty boxes to push the runway to the center
        HBox borderToRunway1 = new HBox();
        borderToRunway1.getStyleClass().add("empty");
        borderToRunway1.setPrefWidth(displayBorderToRunway.getValue());
        HBox borderToRunway2 = new HBox();
        borderToRunway2.getStyleClass().add("empty");
        borderToRunway2.setPrefWidth(displayBorderToRunway.getValue());

        HBox runwayPaneBox = new HBox(borderToRunway1, runwayBox, borderToRunway2);
        runwayPaneBox.setAlignment(Pos.CENTER_LEFT);



        // Plane Images
        Image planeImage = new Image(getClass().getResource("/images/Plane-TopView1.png").toExternalForm());
        ImageView planeImageView = new ImageView(planeImage);
        planeImageView.setPreserveRatio(true);
        planeImageView.setFitWidth(60);




        if (planeBox.getChildren().size() != 0){
            planeBox.getChildren().remove(1);
            planeBox.getChildren().add(planeImageView);
        }



        // Clearways
        HBox clearWayBox = new HBox();
        clearWayBox.getStyleClass().add("empty");
        clearWayBox.setAlignment(Pos.CENTER);

        HBox clearWay1 = new HBox();
        clearWay1.getStyleClass().add("clearway-box");
        clearWay1.visibleProperty().bind(clearWayLength2.greaterThan(0));
        clearWay1.prefWidthProperty().bind(displayClearWayLength);

        HBox distanceBetweenStopways = new HBox();
        distanceBetweenStopways.getStyleClass().add("empty");
        distanceBetweenStopways.setPrefWidth(displayRunwayLength.get());

        HBox clearWay2 = new HBox();
        clearWay2.getStyleClass().add("clearway-box");
        clearWay2.visibleProperty().bind(clearWayLength1.greaterThan(0));
        clearWay2.prefWidthProperty().bind(displayClearWayLength);


        clearWayBox.getChildren().addAll(clearWay1, distanceBetweenStopways, clearWay2);


        obstacleBox.setAlignment(Pos.CENTER_LEFT);


        planeBox.setAlignment(Pos.CENTER_LEFT);
        displayStackPaneTop.getChildren().addAll(gradeAreaImageView, runwayPaneBox, stopWayBox, clearWayBox, obstacleBox, planeBox);

        // Designator Display
        Text designator1 = new Text();
        designator1.getStyleClass().add("display-designator-text");
        designator1.textProperty().bind(subRunway1.getDesignator());
        Text designator2 = new Text();
        designator2.getStyleClass().add("display-designator-text");
        designator2.textProperty().bind(subRunway2.getDesignator());

        HBox emptyHbox = new HBox();
        emptyHbox.getStyleClass().add("empty");
        HBox.setHgrow(emptyHbox, Priority.ALWAYS);


        HBox designatorBox = new HBox();
        designatorBox.getStyleClass().add("empty");
        designatorBox.setAlignment(Pos.CENTER);
        designatorBox.getChildren().addAll(designator1, emptyHbox, designator2);

        displayStackPaneTop.getChildren().add(designatorBox);

        displayStackPaneTop.getChildren().add(makeDisplacedThreshold());



        return displayStackPaneTop;



    }

    /**
     * Create the simultaneous middle display box
     * @return StackPane Middle display box
     */
    public StackPane makeSimultaneousMiddleDisplayBox(){
        StackPane displayStackPane = new StackPane();
        BorderPane displayBorderPane = new BorderPane();
        displayStackPane.getChildren().add(displayBorderPane);


        // Top-View Part
        StackPane topViewPane = new StackPane();


        topViewPane.getStyleClass().add("topView-background");
        displayBorderPane.setTop(topViewPane);
        topViewPane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(2));


        // Top-View Runway Image
        Image toprunwayImage = new Image(getClass().getResource("/images/Runway1.png").toExternalForm());
        ImageView toprunwayImageView = new ImageView(toprunwayImage);
        toprunwayImageView.setFitHeight(50);
        toprunwayImageView.setFitWidth(displayRunwayLength.getValue());


        // Stop Ways
        HBox stopWayTop1 = new HBox();
        stopWayTop1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWayTop1.prefWidthProperty().bind(displayStopWayLength);
        stopWayTop1.setAlignment(Pos.CENTER_LEFT);
        HBox stopWayTop2 = new HBox();
        stopWayTop2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWayTop2.prefWidthProperty().bind(displayStopWayLength);
        stopWayTop2.setAlignment(Pos.CENTER_LEFT);

        stopWayTop2.visibleProperty().bind(stopWayLength1.greaterThan(0));
        stopWayTop1.visibleProperty().bind(stopWayLength2.greaterThan(0));

        // Clearways
        HBox topClearWayBox = new HBox();
        topClearWayBox.getStyleClass().add("empty");
        topClearWayBox.setAlignment(Pos.CENTER);

        HBox topclearWay1 = new HBox();
        topclearWay1.getStyleClass().add("clearway-box");
        topclearWay1.visibleProperty().bind(clearWayLength2.greaterThan(0));
        topclearWay1.prefWidthProperty().bind(displayClearWayLength);

        HBox distanceBetweenStopways1 = new HBox();
        distanceBetweenStopways1.getStyleClass().add("empty");
        distanceBetweenStopways1.setPrefWidth(displayRunwayLength.get());

        HBox topclearWay2 = new HBox();
        topclearWay2.getStyleClass().add("clearway-box");
        topclearWay2.visibleProperty().bind(clearWayLength1.greaterThan(0));
        topclearWay2.prefWidthProperty().bind(displayClearWayLength);


        topClearWayBox.getChildren().addAll(topclearWay1, distanceBetweenStopways1, topclearWay2);




        // Add stopways and runway to the top view pane
        HBox runwayTopBox = new HBox();
        runwayTopBox.getStyleClass().add("empty");
        runwayTopBox.setAlignment(Pos.CENTER_LEFT);
        runwayTopBox.setMaxHeight(Region.USE_PREF_SIZE);
        runwayTopBox.getChildren().addAll(stopWayTop1, toprunwayImageView, stopWayTop2);

        // Empty boxes to push the runway to the center
        HBox borderToTopRunway1 = new HBox();
        borderToTopRunway1.getStyleClass().add("empty");
        borderToTopRunway1.setPrefWidth(displayBorderToRunway.getValue());
        HBox borderToTopRunway2 = new HBox();
        borderToTopRunway2.getStyleClass().add("empty");
        borderToTopRunway2.setPrefWidth(displayBorderToRunway.getValue());

        HBox runwayTopPaneBox = new HBox(borderToTopRunway1, runwayTopBox, borderToTopRunway2);
        runwayTopPaneBox.setAlignment(Pos.CENTER_LEFT);


        // Plane Image
        Image planeImageTop = new Image(getClass().getResource("/images/Plane-TopView1.png").toExternalForm());
        ImageView planeImageViewTop = new ImageView(planeImageTop);
        planeImageViewTop.setFitWidth(50);
        planeImageViewTop.setPreserveRatio(true);


        // Empty boxes to push the plane to the center
        HBox frontPlaneTopEmpty = new HBox();
        frontPlaneTopEmpty.getStyleClass().add("empty");
        frontPlaneTopEmpty.prefWidthProperty().bind(Bindings.add(displayBorderToRunway,displayRunwayToPlane));


        // HBox distance between the plane and obstacle
        HBox planeObstacleTopDistanceBox = new HBox();
        planeObstacleTopDistanceBox.getStyleClass().add("empty");
        planeObstacleTopDistanceBox.setPrefWidth(displayPlaneToObstacle.getValue());

        // Obstacle Image
        Image obstacleImageTop = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
        ImageView obstacleImageViewTop = new ImageView(obstacleImageTop);
        obstacleImageViewTop.setFitWidth(30);
        obstacleImageViewTop.setPreserveRatio(true);

        // Plane & Obstacle HBox
        HBox planeObstacleTopBox = new HBox(frontPlaneTopEmpty ,planeImageViewTop, planeObstacleTopDistanceBox, obstacleImageViewTop);
        planeObstacleTopBox.setAlignment(Pos.CENTER_LEFT);




        // Graded Area
        Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
        ImageView gradeAreaImageView = new ImageView(gradeArea);
        gradeAreaImageView.setFitHeight(200);
        gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
        // Border for the views
        topViewPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2, 0))));

        //Top view label
        VBox topViewLabelBox = new VBox();
        topViewLabelBox.setAlignment(Pos.BOTTOM_CENTER);
        VBox topViewEmpty = new VBox();
        topViewEmpty.getStyleClass().add("empty");
        VBox.setVgrow(topViewEmpty, Priority.ALWAYS);
        Label topViewLabel = new Label("Top View");
        topViewLabel.getStyleClass().add("view-label");
        topViewLabelBox.getChildren().addAll(topViewLabel, topViewEmpty);


        // Add the runway, plane, obstacle and graded area to the top view pane
        topViewPane.getChildren().addAll(gradeAreaImageView, runwayTopPaneBox, planeObstacleTopBox, topViewLabelBox, topClearWayBox);

        // Designator Display
        Text designator1 = new Text();
        designator1.getStyleClass().add("display-designator-text");
        designator1.textProperty().bind(subRunway1.getDesignator());
        Text designator2 = new Text();
        designator2.getStyleClass().add("display-designator-text");
        designator2.textProperty().bind(subRunway2.getDesignator());

        HBox emptyHbox = new HBox();
        emptyHbox.getStyleClass().add("empty");
        HBox.setHgrow(emptyHbox, Priority.ALWAYS);


        HBox designatorBox = new HBox();
        designatorBox.getStyleClass().add("empty");
        designatorBox.setAlignment(Pos.CENTER);
        designatorBox.getChildren().addAll(designator1, emptyHbox, designator2);

        topViewPane.getChildren().add(designatorBox);


        // Side-View Part
        StackPane sideViewStackPane = new StackPane();
        BorderPane sideViewPane = new BorderPane();
        sideViewStackPane.getChildren().add(sideViewPane);
        displayBorderPane.setBottom(sideViewStackPane);

        // Sky Part
        BorderPane bluePane = new BorderPane();
        bluePane.getStyleClass().add("sideView-background");
        bluePane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(4));
        sideViewPane.setTop(bluePane);



        HBox sideViewEmpty1 = new HBox();
        sideViewEmpty1.getStyleClass().add("empty");
        HBox.setHgrow(sideViewEmpty1, Priority.ALWAYS);
        HBox sideViewEmpty2 = new HBox();
        sideViewEmpty2.getStyleClass().add("empty");
        HBox.setHgrow(sideViewEmpty2, Priority.ALWAYS);

        Label sideViewLabel = new Label("Side View");
        sideViewLabel.setAlignment(Pos.CENTER);
        sideViewLabel.getStyleClass().add("view-label");


        bluePane.setTop(new HBox(sideViewEmpty1, sideViewLabel, sideViewEmpty2));



        // Plane Image
        Image planeImageSide = new Image(getClass().getResource("/images/Plane1.png").toExternalForm());
        ImageView planeImageViewSide = new ImageView(planeImageSide);
        planeImageViewSide.setPreserveRatio(true);
        planeImageViewSide.setFitWidth(50);
        // HBox distance between the plane and obstacle
        HBox planeObstacleSideDistance = new HBox();
        planeObstacleSideDistance.getStyleClass().add("empty");
        planeObstacleSideDistance.setPrefWidth(displayPlaneToObstacle.getValue());
        // Obstacle Image
        Image obstacleImageSide = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
        ImageView obstacleImageViewSide = new ImageView(obstacleImageSide);
        obstacleImageViewSide.setPreserveRatio(true);
        obstacleImageViewSide.setFitWidth(30);




        // Plane & Obstacle Pane (Might change)
        HBox planeObstacleSideBox = new HBox();
        planeObstacleSideBox.setAlignment(Pos.BOTTOM_LEFT);
        HBox frontPlaneEmpty = new HBox();
        frontPlaneEmpty.getStyleClass().add("empty");
        frontPlaneEmpty.prefWidthProperty().bind(Bindings.add(displayBorderToRunway,displayRunwayToPlane));


        //planeObstacleSideBox.setMaxWidth(displayRunwayLength.getValue());
        planeObstacleSideBox.getChildren().addAll(frontPlaneEmpty, planeImageViewSide, planeObstacleSideDistance, obstacleImageViewSide);

        bluePane.setBottom(planeObstacleSideBox);


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
        siderunwayImageView.setFitWidth(displayRunwayLength.getValue());


        HBox runwayBox = new HBox();
        runwayBox.getStyleClass().add("empty");
        runwayBox.setAlignment(Pos.CENTER_LEFT);



        // Stop ways
        HBox stopWay1 = new HBox();
        stopWay1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay1.setMaxHeight(Region.USE_COMPUTED_SIZE);
        stopWay1.prefWidthProperty().bind(displayStopWayLength);
        stopWay1.setAlignment(Pos.CENTER_LEFT);
        HBox stopWay2 = new HBox();
        stopWay2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay2.prefWidthProperty().bind(displayStopWayLength);
        stopWay2.setAlignment(Pos.CENTER_RIGHT);
        stopWay2.setMaxHeight(Region.USE_COMPUTED_SIZE);

        stopWay2.visibleProperty().bind(stopWayLength1.greaterThan(0));
        stopWay1.visibleProperty().bind(stopWayLength2.greaterThan(0));


        runwayBox.getChildren().addAll(stopWay1, siderunwayImageView, stopWay2);

        // Empty boxes to push the runway to the center
        HBox borderToRunway1 = new HBox();
        borderToRunway1.getStyleClass().add("empty");
        borderToRunway1.setPrefWidth(displayBorderToRunway.getValue());
        HBox borderToRunway2 = new HBox();
        borderToRunway2.getStyleClass().add("empty");
        borderToRunway2.setPrefWidth(displayBorderToRunway.getValue());
        HBox runwayPaneBox = new HBox(borderToRunway1, runwayBox, borderToRunway2);
        runwayPaneBox.setAlignment(Pos.CENTER_LEFT);



        // Clearways
        HBox clearWayBox = new HBox();
        clearWayBox.getStyleClass().add("empty");
        clearWayBox.setAlignment(Pos.CENTER);

        HBox clearWay1 = new HBox();
        clearWay1.getStyleClass().add("clearway-box");
        clearWay1.visibleProperty().bind(clearWayLength2.greaterThan(0));
        clearWay1.prefWidthProperty().bind(displayClearWayLength);

        HBox distanceBetweenStopways = new HBox();
        distanceBetweenStopways.getStyleClass().add("empty");
        distanceBetweenStopways.setPrefWidth(displayRunwayLength.get());

        HBox clearWay2 = new HBox();
        clearWay2.getStyleClass().add("clearway-box");
        clearWay2.visibleProperty().bind(clearWayLength1.greaterThan(0));
        clearWay2.prefWidthProperty().bind(displayClearWayLength);


        clearWayBox.getChildren().addAll(clearWay1, distanceBetweenStopways, clearWay2);

        sideViewStackPane.getChildren().add(clearWayBox);



        groundPane.setTop(runwayPaneBox);


        // Designator Display
        Text designator3 = new Text();
        designator3.getStyleClass().add("display-designator-text");
        designator3.textProperty().bind(subRunway1.getDesignator());
        Text designator4 = new Text();
        designator4.getStyleClass().add("display-designator-text");
        designator4.textProperty().bind(subRunway2.getDesignator());

        HBox emptyHbox2 = new HBox();
        emptyHbox2.getStyleClass().add("empty");
        HBox.setHgrow(emptyHbox2, Priority.ALWAYS);


        HBox designatorBox2 = new HBox();
        designatorBox2.getStyleClass().add("empty");
        designatorBox2.setAlignment(Pos.CENTER);
        designatorBox2.getChildren().addAll(designator3, emptyHbox2, designator4);

        sideViewStackPane.getChildren().add(designatorBox2);


        return displayStackPane;
    }

    public void changeColourScheme(){
        //System.out.println(currentState.getColourSettting());
        if (currentState.getColourSettting() == "Default (Blue/Green)"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box");
                bluePane.getStyleClass().clear();
                bluePane.getStyleClass().add("sideView-background");
                groundPane.getStyleClass().clear();
                groundPane.getStyleClass().add("sideView-ground");
            });



        }else if (currentState.getColourSettting() == "Blue/Yellow"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box");
                bluePane.getStyleClass().clear();
                bluePane.getStyleClass().add("sideView-background-Deuteranopia");
                groundPane.getStyleClass().clear();
                groundPane.getStyleClass().add("sideView-ground-Deuteranopia");
            });
        }else if (currentState.getColourSettting() == "Magenta/Lime Green"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-limegreen");
                bluePane.getStyleClass().clear();
                bluePane.getStyleClass().add("sideView-background-Tritanopia");
                groundPane.getStyleClass().clear();
                groundPane.getStyleClass().add("sideView-ground-Tritanopia");
            });

        }else if (currentState.getColourSettting() == "Cyan/Deep Purple"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-purple");
                bluePane.getStyleClass().clear();
                bluePane.getStyleClass().add("sideView-background-Protanopia");
                groundPane.getStyleClass().clear();
                groundPane.getStyleClass().add("sideView-ground-Protanopia");
            });
        }
    }

    public void makeColourSettingPage(){
        Stage colourSetting = new Stage();
        colourSetting.initModality(Modality.WINDOW_MODAL);


//        ToggleGroup group = new ToggleGroup();
//        RadioButton defaultOption = new RadioButton("Default");
        defaultOption.setToggleGroup(group);
//        RadioButton option1 = new RadioButton("Option 1");
        option1.setToggleGroup(group);
//        RadioButton option2 = new RadioButton("Option 2");
        option2.setToggleGroup(group);
//        RadioButton option3 = new RadioButton("Option 3");
        option3.setToggleGroup(group);

        if (currentState.getColourSettting() == "Default (Blue/Green)"){
            defaultOption.setSelected(true);
        } else if (currentState.getColourSettting() == "Blue/Yellow"){
            option1.setSelected(true);
        } else if (currentState.getColourSettting() == "Magenta/Lime Green"){
            option2.setSelected(true);
        } else if (currentState.getColourSettting() == "Cyan/Deep Purple"){
            option3.setSelected(true);
        }

        RadioButton initiallySelected = (RadioButton) group.getSelectedToggle();

        Button applyButton = new Button("Exit");
        applyButton.setOnAction(e -> colourSetting.close());

        applyButton.setOnAction(e -> {
                    currentState.setColourSettting(((RadioButton) group.getSelectedToggle()).getText());
                    colourSetting.close();
                    changeColourScheme();
                    changeColourSchemeTop();

                    //here there needs to be an update style section. not really sure how to do it

                }


        );

        // Listen for changes in selection
        group.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != initiallySelected) {
                applyButton.setText("Apply");
            } else {
                applyButton.setText("Exit");
            }
        });

        VBox layout = new VBox(10, new Label("Please select the colour scheme you would like:"), defaultOption, option1, option2, option3, applyButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);
        colourSetting.setScene(scene);
        colourSetting.show();
    }


    public void changeColourSchemeTop(){
        //System.out.println(currentState.getColourSettting());
        if (currentState.getColourSettting() == "Default (Blue/Green)"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box");

                displayStackPaneTop.getStyleClass().clear();
                displayStackPaneTop.getStyleClass().add("topView-background");
            });



        }else if (currentState.getColourSettting() == "Blue/Yellow"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box");

                displayStackPaneTop.getStyleClass().clear();
                displayStackPaneTop.getStyleClass().add("topView-background-Deuteranopia");
            });
        }else if (currentState.getColourSettting() == "Magenta/Lime Green"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-limegreen");

                displayStackPaneTop.getStyleClass().clear();
                displayStackPaneTop.getStyleClass().add("topView-background-Tritanopia");
            });

        }else if (currentState.getColourSettting() == "Cyan/Deep Purple"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-purple");

                displayStackPaneTop.getStyleClass().clear();
                displayStackPaneTop.getStyleClass().add("topView-background-Protanopia");
            });
        }
    }











}
