package display;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

public interface IButtonStateChangedListener {
	void stateChanged(GpioPin pin, PinState state);
}

