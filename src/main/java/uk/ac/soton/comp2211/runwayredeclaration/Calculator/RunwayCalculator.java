package uk.ac.soton.comp2211.runwayredeclaration.Calculator;

import uk.ac.soton.comp2211.runwayredeclaration.Component.Obstacle;
import uk.ac.soton.comp2211.runwayredeclaration.Component.SubRunway;

import java.util.concurrent.TimeUnit;

public class RunwayCalculator {



    /**
     * Calculate the new TORA
     * @param subRunway the subRunway that is focused
     * @param obstacle the obstacle on the runway
     * @param distance the distance between the obstacle and the starting threshold (taking into account of displaced threshold)
     * @return tora the new TORA
     */
    public static double calculateTORA(SubRunway subRunway, Obstacle obstacle, double distance) {
        double originalTORA = subRunway.getOriginalTORA().get();
        double blastProtection = subRunway.getBlastProtection().get();
        double displacedThreshold = subRunway.getDisplacedThreshold().get();
        double stripEnd = subRunway.getStripEndLength().get();
        double RESA = subRunway.getRESA().get();
        double obstacleHeight = obstacle.getHeight();


        double tora;
        // Take off away
        // If the obstacle is located closer to the threshold
        // then the operation is taking off away from the obstacle
        if (distance < originalTORA / 2){

            tora = originalTORA - blastProtection - distance - displacedThreshold;
        }


        //Take off towards
        // if the obstacle is located far from the threshold
        // then the operation is taking off towards the obstacle
        else {
            double slopCalculation = 50 * obstacleHeight;
            // Check if the RESA is greater than the slop calculation
            if (RESA < slopCalculation){ // if the RESA is smaller than the slop calculation
                tora = distance + displacedThreshold - slopCalculation - stripEnd;
            }
            else{ // if the RESA is greater than the slop calculation
                tora = distance + displacedThreshold - RESA - stripEnd;
            }

        }



        return tora;

    }

    /**
     * Calculate the new TODA (TORA + Clearway)
     * @param subRunway the subRunway that is focused
     * @param obstacle the obstacle on the runway
     * @param distance the distance between the obstacle and the starting threshold (taking into account of displaced threshold)
     * @return toda the new TODA
     */
    public static double calculateTODA (SubRunway subRunway, Obstacle obstacle, double distance){
        // Check whether the plane in take off towards or away
        double original_TORA = subRunway.getOriginalTORA().get();
        double clearwayLength = subRunway.getClearwayLength().get();
        double toda;
        // The obstacle is closer to the threshold
        // Take off away
        if (distance < original_TORA / 2){
            toda = RunwayCalculator.calculateTORA(subRunway, obstacle, distance) + clearwayLength;
        }

        // the obstacle is far from the threshold
        // Take off towards
        else{
            toda = RunwayCalculator.calculateTORA(subRunway, obstacle, distance);
        }

        return toda;

    }

    /**
     * Calculate the new ASDA (TORA + Stopway)
     * @param subRunway the subRunway that is focused
     * @param obstacle the obstacle on the runway
     * @param distance distance between the obstacle to the starting threshold
     * @return asda the new ASDA
     */
    public static double calculateASDA (SubRunway subRunway, Obstacle obstacle, double distance){
        double original_ASDA = subRunway.getOriginalASDA().get();
        double stopwayLength = subRunway.getStopwayLength().get();
        double asda;


        // Take off away
        if (distance < original_ASDA / 2){// The obstacle is closer to the threshold
            asda = RunwayCalculator.calculateTORA(subRunway, obstacle, distance) + stopwayLength;
        }


        // Take off towards
        else{// the obstacle is far from the threshold
            asda = RunwayCalculator.calculateTORA(subRunway, obstacle, distance);
        }
        return asda;
    }

    /**
     * Calculate the new LDA
     * @param subRunway the subRunway that is focused
     * @param obstacle the obstacle on the runway
     * @param distance distance between the obstacle to the starting threshold
     * @return lda the new LDA
     */
    public static double calculateLDA (SubRunway subRunway, Obstacle obstacle, double distance){
        double original_LDA = subRunway.getOriginalLDA().get();
        double stripEnd = subRunway.getStripEndLength().get();
        double RESA = subRunway.getRESA().get();
        double blastProtection = subRunway.getBlastProtection().get();


        double lda;

        // Check whether the obstacle is located close or far from

        // landing over
        if (distance < original_LDA / 2){ // if the obstacle is closer to the threshold


            double slopCalculation = obstacle.getHeight() * 50;


            // Check if slopCalculation greater than RESA
            if (slopCalculation > RESA){

                // If slop calculation is greater than RESA
                if (stripEnd + slopCalculation > blastProtection){
                    lda = original_LDA - distance - stripEnd - slopCalculation;
                }
                else{
                    lda = original_LDA - distance - blastProtection;
                }

            }

            else{ // If slop calculation is less than RESA


                if (stripEnd + RESA > blastProtection){
                    lda = original_LDA - distance - stripEnd - RESA;
                }
                else{
                    lda = original_LDA - distance - blastProtection;
                }
            }


        }

        // landing towards
        else {

            lda = distance - stripEnd - RESA;

        }

        return lda;
    }




}
