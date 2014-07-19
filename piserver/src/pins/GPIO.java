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
	// create gpio controller
    final GpioController gpio = GpioFactory.getInstance();
    
    // provision gpio pin #01 as an output pin and turn on
    final GpioPinDigitalOutput pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
    final GpioPinDigitalOutput pin4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
    final GpioPinDigitalOutput pin5 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
    
    
	public GPIO() {
		//	com.pi4j.wiringpi.Gpio.wiringPiSetup();

		// create soft-pwm pins (min=0 ; max=100)
		//SoftPwm.softPwmCreate(1, 0, 100);
	}
	// create gpio controller instance
	public void moveForward(boolean forward, boolean backward) throws InterruptedException {
		// initialize wiringPi library
		
//		pin4.high();
//	    pin5.low();
		
		
       
        if (forward == true) {
			// fade LED to fully ON
			//for (int i = 0; i <= 100;i=i+50) {
				pin4.low();
	    	    pin5.high();
				Thread.sleep(100);
				
				pin4.low();
	    	    pin5.low();
		//		Thread.sleep(1);
			//}
		
		forward = false;
		}
        else if (backward == true) {
			// fade LED to fully ON
			//for (int i = 0; i <= 100;i=i+50) {
				pin4.high();
	    	    pin5.low();
				Thread.sleep(100);
				
				pin4.low();
	    	    pin5.low();
		//		Thread.sleep(1);
			//}
		
		backward = false;
		}
		
		
		
		
		
//		if (running == false) {
//			running = true;
//			// fade LED to fully ON
//			for (int i = 0; i <= 100;i=i+25) {
//				SoftPwm.softPwmWrite(1, i);
//				Thread.sleep(20);
//				
//			}
//
//			// fade LED to fully OFF
//			for (int i = 100; i >= 0; i=i-25) {
//				SoftPwm.softPwmWrite(1, i);
//				Thread.sleep(20);
//			}
//		running = false;
//		}
	}
}