
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.runwayredeclaration.Calculator.RunwayCalculator;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Obstacle;
import uk.ac.soton.comp2211.runwayredeclaration.Component.SubRunway;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CalculatorTest {

    @Test
    public void testTORAs1() {

        SubRunway testRunway = new SubRunway("09L",3902,3902,3902,3595,306,60,300,240);
        Obstacle testObstacle = new Obstacle("plane", 10, 10, 10);

        double tora = RunwayCalculator.calculateTORA(testRunway, testObstacle, -50);

        System.out.println("Test tora is: " + tora);
        assertEquals(3346, tora);
    }
}
