package pins;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.PinEventType;
import com.pi4j.wiringpi.SoftPwm;

public class GPIO {
	boolean running = false;

	// create gpio controller instance
	public void runLED() throws InterruptedException {
		// initialize wiringPi library
		com.pi4j.wiringpi.Gpio.wiringPiSetup();

		// create soft-pwm pins (min=0 ; max=100)
		SoftPwm.softPwmCreate(1, 0, 100);

		if (running == false) {
			running = true;
			// fade LED to fully ON
			for (int i = 0; i <= 100;i=i+4) {
				SoftPwm.softPwmWrite(1, i);
				Thread.sleep(1);
				
			}

			// fade LED to fully OFF
			for (int i = 100; i >= 0; i=i-4) {
				SoftPwm.softPwmWrite(1, i);
				Thread.sleep(1);
			}
		running = false;
		}
	}
}