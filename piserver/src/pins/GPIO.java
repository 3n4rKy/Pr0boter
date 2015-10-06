package pins;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

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
	int[] PINS = { 12, 13, // Engine 1 FL 0,1
			14, 21, // Engine 2 FR 2,3
			22, 23, // Engine 3 BL 4,5
			24, 25 };// Engine 4 BR 6,7

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
			gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, "Skip Next", PinPullResistance.PULL_DOWN) };

	public GPIO() {
		buttonListener();
		enginePWM();
	}

	// Movements
	public void moveForward(int powerLevel) throws InterruptedException {
		// Engine 1
		allEnginesForward(powerLevel);
		Thread.sleep(100);

		// Engines off
		resetEngines();

	}

	public void moveBackward(int powerLevel) throws InterruptedException {
		// Engine 1
		allEnginesBackward(powerLevel);

		Thread.sleep(100);

		// Engines off
		resetEngines();
	}

	private void enginePWM() {
		Gpio.wiringPiSetup();

		for (int pin : PINS) {
			SoftPwm.softPwmCreate(pin, 0, 100);
		}
	}

	public void moveTurnLeft(int powerLevel) throws InterruptedException {
		rightSideForward(powerLevel);
		leftSideBackward(powerLevel);
		Thread.sleep(100);
		resetEngines();
	}

	public void moveTurnRight(int powerLevel) throws InterruptedException {
		rightSideBackward(powerLevel);
		leftSideForward(powerLevel);
		Thread.sleep(100);
		resetEngines();
	}

	public void moveStrafeLeft(int powerLevel) throws InterruptedException {
		SoftPwm.softPwmWrite(PINS[1], powerLevel);
		SoftPwm.softPwmWrite(PINS[2], powerLevel);
		SoftPwm.softPwmWrite(PINS[4], powerLevel);
		SoftPwm.softPwmWrite(PINS[7], powerLevel);
		Thread.sleep(100);
		resetEngines();
	}

	public void moveStrafeRight(int powerLevel) throws InterruptedException {
		SoftPwm.softPwmWrite(PINS[0], powerLevel);
		SoftPwm.softPwmWrite(PINS[3], powerLevel);
		SoftPwm.softPwmWrite(PINS[5], powerLevel);
		SoftPwm.softPwmWrite(PINS[6], powerLevel);
		Thread.sleep(100);
		resetEngines();
	}

	public void moveForwardLeft(int powerLevel) throws InterruptedException {
		SoftPwm.softPwmWrite(PINS[2], powerLevel);
		SoftPwm.softPwmWrite(PINS[4], powerLevel);
		Thread.sleep(100);
		resetEngines();
	}

	public void moveForwardRight(int powerLevel) throws InterruptedException {
		SoftPwm.softPwmWrite(PINS[0], powerLevel);
		SoftPwm.softPwmWrite(PINS[6], powerLevel);
		Thread.sleep(100);
		resetEngines();
	}

	public void moveBackwardLeft(int powerLevel) throws InterruptedException {
		SoftPwm.softPwmWrite(PINS[1], powerLevel);
		SoftPwm.softPwmWrite(PINS[7], powerLevel);
		Thread.sleep(100);
		resetEngines();
	}

	public void moveBackwardRight(int powerLevel) throws InterruptedException {
		SoftPwm.softPwmWrite(PINS[3], powerLevel);
		SoftPwm.softPwmWrite(PINS[5], powerLevel);
		Thread.sleep(100);
		resetEngines();
	}

	private void leftSideForward(int powerLevel) {
		SoftPwm.softPwmWrite(PINS[0], powerLevel);
		SoftPwm.softPwmWrite(PINS[4], powerLevel);
	}

	private void leftSideBackward(int powerLevel) {
		SoftPwm.softPwmWrite(PINS[1], powerLevel);
		SoftPwm.softPwmWrite(PINS[5], powerLevel);
	}

	private void rightSideForward(int powerLevel) {
		SoftPwm.softPwmWrite(PINS[2], powerLevel);
		SoftPwm.softPwmWrite(PINS[6], powerLevel);
	}

	private void rightSideBackward(int powerLevel) {
		SoftPwm.softPwmWrite(PINS[3], powerLevel);
		SoftPwm.softPwmWrite(PINS[7], powerLevel);
	}

	private void resetEngines() {
		for (int pin : PINS) {
			SoftPwm.softPwmWrite(pin, 0);
		}
	}

	private void allEnginesForward(int powerLevel) {
		for (int i = 0; i < PINS.length; i += 2) {
			SoftPwm.softPwmWrite(PINS[i], powerLevel);
		}

	}

	private void allEnginesBackward(int powerLevel) {
		for (int j = 1; j < PINS.length; j += 2) {
			SoftPwm.softPwmWrite(PINS[j], powerLevel);
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