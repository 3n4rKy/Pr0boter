package pins;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import display.IButtonStateChangedListener;

/**
 * Manages the pins on the raspi board
 * 
 * @author nrk
 *
 */
public class GPIO {

	private static Logger logger = LogManager.getLogger(GPIO.class.getName());
	public final static int LCD_ROWS = 2;
	public final static int LCD_COLUMNS = 16;
	public final static int LCD_BITS = 4;
	boolean running = false;
	boolean[] checkButtons = { false, false };
	private ArrayList<IButtonStateChangedListener> listeners = new ArrayList<IButtonStateChangedListener>();

	// create gpio controller
	final GpioController gpio = GpioFactory.getInstance();

	// Engines
	final GpioPinDigitalOutput engines[] = { 
			//Engine 1 FL
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, PinState.LOW),
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, PinState.LOW),
			//Engine 2 FR
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, PinState.LOW),
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.LOW),
			//Engine 3 BL
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, PinState.LOW),
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, PinState.LOW),
			//Engine 4 BR
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW),
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, PinState.LOW) 
			};
	
	final GpioLcdDisplay lcd = new GpioLcdDisplay(LCD_ROWS, // number of row
															// supported by LCD
			LCD_COLUMNS, // number of columns supported by LCD
			RaspiPin.GPIO_11, // LCD RS pin
			RaspiPin.GPIO_10, // LCD strobe pin
			RaspiPin.GPIO_04, // LCD data bit 1
			RaspiPin.GPIO_05, // LCD data bit 2
			RaspiPin.GPIO_06, // LCD data bit 3
			RaspiPin.GPIO_07); // LCD data bit 4

	final GpioPinDigitalInput displayButtons[] = {
			gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, "Skip Back", PinPullResistance.PULL_DOWN),
			gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, "Skip Next", PinPullResistance.PULL_DOWN) 
			};

	public GPIO() {
		buttonListener();
	}

	// Movements
	public void moveForward(boolean forward, boolean backward) throws InterruptedException {

		if (forward == true) {
			// Engine 1
			allEnginesForward();
			Thread.sleep(100);

			// Engines off
			resetEngines();

			forward = false;
		} else if (backward == true) {
			// Engine 1
			allEnginesBackward();

			Thread.sleep(100);

			// Engines off
			resetEngines();
			backward = false;
		}
	}
	
	public void turnLeft() throws InterruptedException {
		rightSideForward();
		leftSideBackward();
		Thread.sleep(100);
		resetEngines();
	}
	
	public void turnRight() throws InterruptedException {
		rightSideBackward();
		leftSideForward();
		Thread.sleep(100);
		resetEngines();
	}
	
	private void leftSideForward() {
		engines[0].high();
		engines[1].low();
		engines[4].high();
		engines[5].low();
	}
	
	private void leftSideBackward() {
		engines[0].low();
		engines[1].high();
		engines[4].low();
		engines[5].high();
	}
	private void rightSideForward() {
		engines[2].high();
		engines[3].low();
		engines[6].high();
		engines[7].low();
	}
	
	private void rightSideBackward() {
		engines[2].low();
		engines[3].high();
		engines[6].low();
		engines[7].high();
	}
	
	private void resetEngines() {
		for (GpioPinDigitalOutput engine : engines) {
			engine.low();
		}
	}
	
	private void allEnginesForward() {
		for(int i = 0; i < engines.length; i += 2) {
			engines[i].high();
		}
		for(int j = 1; j < engines.length; j += 2) {
			engines[j].low();
		}
	}
	
	private void allEnginesBackward() {
		for(int i = 0; i < engines.length; i += 2) {
			engines[i].low();
		}
		for(int j = 1; j < engines.length; j += 2) {
			engines[j].high();
		}
	}
	
	public void writeLineToLCD(int row, String line) {
		lcd.clear();
		lcd.write(row, line);
	}

	public void clearLineToLCD() {
		lcd.clear();
	}

	public void addButtonStateChangedListener(IButtonStateChangedListener listener) {
		listeners.add(listener);
	}

	private void buttonListener() {
		displayButtons[0].addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				// display pin state on console
				PinState state = event.getState();
				GpioPin pin = event.getPin();
				listeners.forEach(l -> l.stateChanged(pin, state));
				logger.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
			}
		});
		displayButtons[1].addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				// display pin state on console
				PinState state = event.getState();
				GpioPin pin = event.getPin();
				listeners.forEach(l -> l.stateChanged(pin, state));
				logger.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
			}
		});
	}
}