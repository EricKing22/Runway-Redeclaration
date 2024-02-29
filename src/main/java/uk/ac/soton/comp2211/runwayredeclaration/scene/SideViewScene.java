package uk.ac.soton.comp2211.runwayredeclaration.scene;

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

        left_box = new VBox(makeResultsBox());
        left_box.getStyleClass().add("left-box");
        mainPane.setLeft(left_box);
        BorderPane.setAlignment(left_box, Pos.CENTER);



        VBox resultsBox = makeResultsBox();
        TableView resultsTable = new TableView();
        resultsTable.setEditable(true);
        TableColumn tableRunway = new TableColumn("Runway");
        TableColumn tableTORA = new TableColumn("TORA");
        TableColumn tableTODA = new TableColumn("TODA");
        TableColumn tableLSA = new TableColumn("LSA");
        TableColumn tableASDA = new TableColumn("ASDA");




        right_box = new VBox();
        right_box.getStyleClass().add("right-box");
        mainPane.setRight(right_box);
        BorderPane.setAlignment(right_box, Pos.CENTER);

        HBox menuHBox = new HBox();

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

        // Add MenuBar and Logout button to the HBox
        menuHBox.getChildren().addAll(menuBar, empty, logoutButton);
        HBox.setHgrow(empty, Priority.ALWAYS); // This will push the logout button to the right

        // Set the style class for the HBox
        menuHBox.getStyleClass().add("menu-hbox");
        BorderPane.setAlignment(menuHBox, Pos.CENTER);
        mainPane.setTop(menuHBox);




        StackPane displayStackPane = new StackPane();
        mainPane.setCenter(displayStackPane);

        BorderPane displayBorderPane = new BorderPane();
        displayStackPane.getChildren().add(displayBorderPane);

        Pane bluePane = new Pane();
        bluePane.setMaxWidth(Double.MAX_VALUE);
        bluePane.setMaxHeight(Double.MAX_VALUE);
        bluePane.getStyleClass().add("sideView-background");
        displayBorderPane.setTop(bluePane);

        Pane groundPane = new Pane();
        groundPane.setMaxWidth(Double.MAX_VALUE);
        groundPane.setMaxHeight(Double.MAX_VALUE);
        groundPane.setBackground(new Background(new BackgroundFill(Color.web("#A9D18E"), CornerRadii.EMPTY, Insets.EMPTY)));
        displayBorderPane.setBottom(groundPane);






        this.initialise();
    }





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
