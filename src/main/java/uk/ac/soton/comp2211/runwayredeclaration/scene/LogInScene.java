package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.runwayredeclaration.Component.LogInVBox;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;

public class LogInScene extends BaseScene{
    public LogInVBox logInVBox;
    public Button logIn;
    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param homeWindow the home window
     */
    public LogInScene(HomeWindow homeWindow) {
        super(homeWindow);
        logIn = new Button();
        logIn.setOnAction(e -> {
            homeWindow.startHome(logInVBox.getUser());
        });
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        root = new HomePane(homeWindow.getWidth(),homeWindow.getHeight());


        var logInPane = new StackPane();
        logInPane.setMaxWidth(homeWindow.getWidth());
        logInPane.setMaxHeight(homeWindow.getHeight());

        root.getChildren().add(logInPane);

        logInVBox = new LogInVBox(logIn);
        logInPane.getChildren().add(logInVBox);



    }
}
