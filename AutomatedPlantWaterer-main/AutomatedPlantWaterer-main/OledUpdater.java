package eecs1021;

//imports
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

//Creating class & method
public class OledUpdater {
    private static int mLevelInt;

    public static void updateDisplay(SSD1306 display, Pin mySensor){

        //Calculating moisture Level
        long mLevel = mySensor.getValue();
        mLevelInt = (int) ((mLevel * 100) / 1023);
        String mLevelString = String.valueOf(mLevelInt);
        String state = "";

        //If, else-if, else block to determine state of machine
        if (mLevelInt > 65){
            state = "Need Water";
        }
        else if (mLevelInt >= 57 && mLevelInt < 65) {
            state = "A little more water";
        } else{
            state = "Enough water";
        }

        //Printing the moisture level on console & Oled
        System.out.println("Moisture Level: " + mLevelString + " - " + state);
        display.clear();
        display.getCanvas().setTextsize(2);
        display.getCanvas().drawString(0, 0,"MLevel: " + mLevelString);
        display.getCanvas().drawString(0,16,state);
        display.display();
    }
    //Getter method
    public static int getmLevelInt(){
        return mLevelInt;
    }

    //Setter method
    public static void setmLevelInt(int newInt){
        mLevelInt = newInt;
    }

    // Method to display "Terminated" on the OLED
    public static void displayTerminated(SSD1306 display) {
        display.clear();
        display.getCanvas().setTextsize(2);
        display.getCanvas().drawString(0, 0, "Terminated");
        display.display();
    }

    //Method used in unitTest
    static public int normalizeYValue(int originalValue){

        final float MAXVALUE = 1023.0F;
        final float MINVALUE = 0.0F;
        final int ERRORVALUE = -1000;


        int theReturnValue;

        if((originalValue > MAXVALUE)){
            theReturnValue = ERRORVALUE;
        }
        else if ( (originalValue < MINVALUE) ){
            theReturnValue = ERRORVALUE;
        }
        else{
            theReturnValue = (int)(100.0*((float)(originalValue)*((1.0)/(MAXVALUE-MINVALUE))));
        }
        return theReturnValue;
    }
}
