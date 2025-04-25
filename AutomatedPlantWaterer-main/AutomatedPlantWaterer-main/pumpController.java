package eecs1021;

//imports
import org.firmata4j.Pin;

import java.io.IOException;

//Creating pumpController Class
public class pumpController {
    private static int pumpState;
    public static void updatePump(Pin myPump)
            throws IOException, InterruptedException {

        //Accessing the moisture level from the Oled Class
        int mLevelInt = OledUpdater.getmLevelInt();

        //If, else-if, else block to turn on/off pump
        if (mLevelInt > 65){
            pumpState = 1;
            myPump.setValue(1);
            Thread.sleep(3500);
            myPump.setValue(0);
        }
        else if (mLevelInt >= 57 && mLevelInt < 65) {
            pumpState = 1;
            myPump.setValue(1);
            Thread.sleep(2000);
            myPump.setValue(0);
        } else{
            pumpState = 0;
        }
    }

    //Getter method
    public static int getPumpState(){
        return pumpState;

    }
}

