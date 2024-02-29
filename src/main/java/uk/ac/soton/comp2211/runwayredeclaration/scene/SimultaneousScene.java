package uk.ac.soton.comp2211.runwayredeclaration.scene;

import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimultaneousScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(SimultaneousScene.class);

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
    }
}
