package display;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;
/**
 * Button listener interface
 * 
 * @author nrk
 * 
 *
 */
public interface IButtonStateChangedListener {
	/**
	 * Method waiting for state change 
	 * @param pin
	 * @param state
	 */
	void stateChanged(GpioPin pin, PinState state);
}

