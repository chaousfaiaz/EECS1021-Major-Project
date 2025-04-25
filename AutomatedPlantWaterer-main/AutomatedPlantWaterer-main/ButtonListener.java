package eecs1021;

//Imports
import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;

//Button Listener method, to override entire plantWaterer classs
public class ButtonListener implements
        IODeviceEventListener {
    private final Pin buttonObject;

    ButtonListener(Pin buttonObject){
        this.buttonObject = buttonObject;
    }

    //If button is pressed, program is exited
    @Override
    public void onPinChange(IOEvent event) {
        if (event.getPin() == buttonObject){
            if(event.getValue() == 1){
                System.out.println("Program Terminated...");
                System.exit(0);
            }
        }
    }
    @Override
    public void onStart(IOEvent event) {}
    @Override
    public void onStop(IOEvent event) {}
    @Override
    public void onMessageReceive(IOEvent event, String message) {}

}
