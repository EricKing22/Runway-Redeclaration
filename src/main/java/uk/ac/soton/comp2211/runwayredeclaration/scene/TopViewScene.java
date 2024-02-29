package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
public class TopViewScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(TopViewScene.class);

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


        //Awful title
        var title = new Text("Top View");


        VBox left_box = new VBox();
        left_box.getStyleClass().add("left-box");
        mainPane.setLeft(left_box);
        BorderPane.setAlignment(left_box, Pos.CENTER);

        VBox right_box = new VBox();
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
        viewMenu.getItems().addAll(new MenuItem("Side On"), new MenuItem("Top Down"), new MenuItem("Simultaneous"));

        // Help Menu
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(new MenuItem("About"), new MenuItem("Contact"));

        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);

        // Create Log out button
        Button logoutButton = new Button("Log out");
        logoutButton.getStyleClass().add("logout-button");

        // Add MenuBar and Logout button to the HBox
        menuHBox.getChildren().addAll(menuBar, logoutButton);
        HBox.setHgrow(logoutButton, Priority.ALWAYS); // This will push the logout button to the right

        // Set the style class for the HBox
        menuHBox.getStyleClass().add("menu-hbox");

        mainPane.setTop(menuHBox);
        BorderPane.setAlignment(menuHBox, Pos.CENTER);


        this.initialise();
    }
}
