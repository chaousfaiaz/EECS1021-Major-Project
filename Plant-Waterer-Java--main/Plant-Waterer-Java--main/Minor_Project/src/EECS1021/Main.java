package EECS1021;

import org.firmata4j.I2CDevice;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.Pin; // Firmata
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.Timer; // Timer
import java.util.TimerTask;

class SoilSensorTest extends TimerTask {
    //Varibles Private to class
    private final Pin soilSensor;
    private final Pin waterPump;
    private final SSD1306 oled;

    //Class contructor
    public SoilSensorTest(Pin sensorPin, Pin pumpPin, SSD1306 oledPin) {
        this.soilSensor = sensorPin;
        this.waterPump = pumpPin;
        this.oled = oledPin;

    }

    @Override
    public void run() {

        //Get value of soil sensor and convert to voltage
        var soilSensorValue = soilSensor.getValue();
        double voltCalc = soilSensorValue*5;
        double voltage = (voltCalc/1023);

        //Clear OLED screen and write the new soil value and soil voltage
        oled.clear();
        oled.getCanvas().drawString(0,0, "Soil Value: " + soilSensorValue);
        oled.getCanvas().drawString(0,10,"Soil Voltage: " + String.format("%.2f",voltage) + " V");

        //Check if voltage is 2.6 or higher
        if (voltage >= 2.6) {

            //Display that Soil is dry on the OLED
            oled.getCanvas().drawString(0, 20, "Soil is Dry");
            oled.display();
        }
        //When voltage is lower than 2.6
        else if (voltage < 2.6) {

            //Display that soil is sufficiantly watered
            oled.getCanvas().drawString(0, 20, "Soil is sufficiantly watered");
            oled.display();
        }
        else {
        }
    }
}

class SoilSensor extends TimerTask {
    //Varibles Private to class
    private final Pin soilSensor;
    private final Pin waterPump;
    private final SSD1306 oled;


    //Class contructor
    public SoilSensor(Pin sensorPin, Pin pumpPin, SSD1306 oledPin) {
        this.soilSensor = sensorPin;
        this.waterPump = pumpPin;
        this.oled = oledPin;
    }

    @Override
    public void run() {

        //Get value of soil sensor and convert to voltage
        var soilSensorValue = soilSensor.getValue();
        double voltCalc = soilSensorValue*5;
        double voltage = (voltCalc/1023);

        //Clear OLED screen and write the new soil value and soil voltage
        oled.clear();
        oled.getCanvas().drawString(0,0, "Soil Value: " + soilSensorValue);
        oled.getCanvas().drawString(0,10,"Soil Voltage: " + String.format("%.2f",voltage) + " V");


        //Water plant if voltage is 2.6 or higher
        if (voltage >= 2.6) {

            //Display that Soil is dry on the OLED
            oled.getCanvas().drawString(0, 20, "Soil is Dry");
            oled.display();

            //Water plant for 3 seconds
            try {
                waterPump.setValue(1);
                Thread.sleep(3000);
                waterPump.setValue(0);
            } catch (Exception ex) {
                System.out.println("Error pumping water");
            }
        }
        //When voltage is lower than 2.6
        else if (voltage < 2.6) {

            //Display that soil is sufficiantly watered
            oled.getCanvas().drawString(0, 20, "Soil is sufficiantly watered");
            oled.display();
        }
        else {
        }
    }
}

public class Main {

    //Pin definitions
    static final int	A0	= 14;	// Soil Sensor
    static final int    D2 = 2;     // Water Pump
    static final byte I2C0 = 0x3C;	// OLED Display

    public static void main(String[] args)
            throws InterruptedException, IOException
    {
        //Get port name and create variable for arduino device
        String port = "COM3"; // The USB port name varies.
        var arduino = new FirmataDevice(port); // Board object, using the name of a port

        // Start and initialize arduino device
        arduino.start();
        arduino.ensureInitializationIsDone();

        //Variables for soil sensor and water pump
        var soilSensor = arduino.getPin(A0);
        var waterPump = arduino.getPin(D2);

        //Varible for OLED device
        I2CDevice i2cObject = arduino.getI2CDevice(I2C0); // Use 0x3C for the Grove OLED
        SSD1306 oled = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64); // 128x64 OLED SSD1515

        // Initialize the OLED
        oled.init();

        //Task to check soil voltage and water accordingly, runs every 10 seconds
        var task = new SoilSensor(soilSensor, waterPump, oled);
        new Timer().schedule(task, 0, 10000);

        //Test task to check soil voltage, runs every 10 seconds
//        var task2 = new SoilSensorTest(soilSensor, waterPump, oled,);
//        new Timer().schedule(task2, 0, 10000);

    }

}


