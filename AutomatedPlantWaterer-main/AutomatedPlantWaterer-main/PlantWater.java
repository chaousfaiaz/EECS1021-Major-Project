package eecs1021;

//Imports
import org.firmata4j.I2CDevice;
import org.firmata4j.firmata.*;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import java.util.ArrayList;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

//Main Class
public class PlantWater {
    public static void main(String[] args)
            throws IOException, InterruptedException {

        //Initializing Grove Board
        String myPort = "/dev/cu.usbserial-0001";
        IODevice myGroveBoard = new FirmataDevice(myPort);
        myGroveBoard.start();
        myGroveBoard.ensureInitializationIsDone();

        //Setting up variables for arduino
        var myPump = myGroveBoard.getPin(7);
        var mySensor = myGroveBoard.getPin(15);
        var myButton = myGroveBoard.getPin(6);
        myPump.setMode(Pin.Mode.OUTPUT);
        myButton.setMode(Pin.Mode.INPUT);

        //Terminating Bool Variable
        AtomicBoolean isTerminating = new AtomicBoolean(false);

        //Setting up OLED display to be used
        I2CDevice i2cObject = myGroveBoard.getI2CDevice((byte) 0x3C); // Use 0x3C for the Grove OLED
        SSD1306 display = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64); // 128x64 OLED SSD1515

        //Button Listener
        ButtonListener buttonListener = new ButtonListener(myButton);
        myGroveBoard.addEventListener(buttonListener);

        //Termination Display
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            isTerminating.set(true);
            OledUpdater.displayTerminated(display);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        //Setting up ArrayList and graph plotter for grapherClass class
        ArrayList<Integer> moistureLevels = new ArrayList<>();
        grapherClass graphPlotter = new grapherClass();

        //While loop which nests the logic (overridden by termination)
        while(!isTerminating.get()){

            //Oled Update
            OledUpdater.updateDisplay(display,mySensor);

            //adds the moisture levels to the ArrayList
            int moistureLevel = OledUpdater.getmLevelInt();
            moistureLevels.add(moistureLevel);
            graphPlotter.plotGraph(moistureLevels);

            //Pump Controller
            pumpController.updatePump(myPump);

            //Sleeping 1 seconds per iteration
            //Allows for Oled Display to update
            Thread.sleep(1000);
        }
    }
}
