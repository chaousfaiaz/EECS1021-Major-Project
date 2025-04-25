package eecs1021;

import org.firmata4j.IODevice;
import org.firmata4j.firmata.FirmataDevice;
import org.junit.Test;
import java.util.Random;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class theUnitTest {
    final int MAXINPUT = 1023;
    final int MININPUT = 0;
    final int ERRORVALUE = -1000;

    final int MINRESULT = 0;
    final int MAXRESULT = 100;


    @Test
    public void testNormalizeYValue() {
        // Test to make sure that -1000 is returned for values above 1023 ...
        System.out.println("Testing against too positive input...that it returns an error value of " + ERRORVALUE);
        String errorMessage0 = "Error: wrong value returned for an input above 1023.";
        assertEquals(errorMessage0, ERRORVALUE, OledUpdater.normalizeYValue(MAXINPUT + 1));
        assertEquals(errorMessage0, ERRORVALUE, OledUpdater.normalizeYValue(MAXINPUT * 1000));

        // ... or below 0.
        System.out.println("Testing against too negative input... that it returns an error value of " + ERRORVALUE);
        String errorMessage1 = "Error: wrong value returned for an input below 0.";
        assertEquals(errorMessage1, ERRORVALUE, OledUpdater.normalizeYValue(MININPUT - 1));
        assertEquals(errorMessage1, ERRORVALUE, OledUpdater.normalizeYValue((MININPUT - 1) * 1000));

        // Test to see how it behaves for values from 0 to 1023
        // If there's a problem, print a message saying so.
        System.out.println("Testing against values from " + MININPUT + " to " + MAXINPUT);
        int result;
        result = OledUpdater.normalizeYValue(MININPUT);
        String errorMessage2 = "Error: The result shouldn't be below 0";  // show this if the test fails.
        assertTrue(errorMessage2, result >= MINRESULT);

        result = OledUpdater.normalizeYValue(MAXINPUT + 1);
        String errorMessage3 = "Error: The result shouldn't be above 100"; // show this if the test fails.
        assertTrue(errorMessage3, result <= MAXRESULT);

        // Throw at it a thousand random numbers, bounded from min to max values, to see if it fails...
        for (int i = 0; i < 1000; i++) {
            System.out.print(".");
            int randValue = (int) (Math.random() * (MAXINPUT + 1)); // Generate random value between 0 and MAXINPUT
            result = OledUpdater.normalizeYValue(randValue);
            String errorMessage4 = "Error: The result is _not_ between 0 and 100";
            assertTrue(errorMessage4, result >= MINRESULT && result <= MAXRESULT);
        }
    }
    @Test
    // Test pump controller based on different moisture levels
    public void testPumpController() throws IOException, InterruptedException {

        //Setting up Arduino
        String myPort = "/dev/cu.usbserial-0001";
        IODevice myGroveBoard = new FirmataDevice(myPort);
        myGroveBoard.start();
        myGroveBoard.ensureInitializationIsDone();
        var myPump = myGroveBoard.getPin(7);

        //Random number generator
        Random random = new Random();

        // Test with moisture level above 65
        int above65 = random.nextInt(100 - 65 + 1) + 65;
        OledUpdater.setmLevelInt(above65);
        pumpController.updatePump(myPump);
        assertEquals("Pump should be turned on for moisture level above 65", 1, pumpController.getPumpState());

        // Test with moisture level between 57 and 65
        int between55And65 = random.nextInt(65 - 57 + 1) + 57;
        OledUpdater.setmLevelInt(between55And65);
        pumpController.updatePump(myPump);
        assertEquals("Pump should be turned on for moisture level between 57 and 65", 1, pumpController.getPumpState());

        // Test with moisture level below 57
        int below55 = random.nextInt(57 - 0 + 1) + 0;
        OledUpdater.setmLevelInt(below55);
        pumpController.updatePump(myPump);
        assertEquals("Pump should be turned off for moisture level below 57", 0, pumpController.getPumpState());
    }
}



