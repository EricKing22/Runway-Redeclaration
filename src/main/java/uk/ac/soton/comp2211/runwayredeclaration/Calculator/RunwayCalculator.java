package uk.ac.soton.comp2211.runwayredeclaration.Calculator;

import uk.ac.soton.comp2211.runwayredeclaration.Component.Obstacle;
import uk.ac.soton.comp2211.runwayredeclaration.Component.SubRunway;

public class RunwayCalculator {
    private SubRunway runway;
    private Obstacle obstacle;



    public static double calculateTORA(SubRunway subRunway, Obstacle obstacle, double distance) {



        return 0;

    }

    public double calculateThreshold(double obstacleDistance, double displacedThreshold) {
        return obstacleDistance - displacedThreshold;
    }

    public double calculateTORA(double originalTORA, double blastProtection, double threshold, double displacedThreshold) {
        return originalTORA - blastProtection - threshold - displacedThreshold;
    }

    public double calculateASDA(double TORA, double stopwayLength) {
        return TORA + stopwayLength;
    }

    public double calculateTODA(double TORA, double clearwayLength) {
        return TORA + clearwayLength;
    }


}
