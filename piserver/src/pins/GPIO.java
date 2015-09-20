package pins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GPIO {

	private static Logger logger = LogManager.getLogger(GPIO.class.getName());
	public final static int LCD_ROWS = 2;
	public final static int LCD_COLUMNS = 16;
	public final static int LCD_BITS = 4;
	boolean running = false;
	boolean[] checkButtons = { false, false };

	// create gpio controller
	final GpioController gpio = GpioFactory.getInstance();

	// provision gpio pin #01 as an output pin and turn on
	final GpioPinDigitalOutput pin12 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, PinState.LOW);
	final GpioPinDigitalOutput pin13 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, PinState.LOW);
	final GpioLcdDisplay lcd = new GpioLcdDisplay(LCD_ROWS, // number of row
															// supported by LCD
			LCD_COLUMNS, // number of columns supported by LCD
			RaspiPin.GPIO_11, // LCD RS pin
			RaspiPin.GPIO_10, // LCD strobe pin
			RaspiPin.GPIO_04, // LCD data bit 1
			RaspiPin.GPIO_05, // LCD data bit 2
			RaspiPin.GPIO_06, // LCD data bit 3
			RaspiPin.GPIO_07); // LCD data bit 4

	final GpioPinDigitalInput myButtons[] = {
			gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, "B1", PinPullResistance.PULL_DOWN),
			gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, "B2", PinPullResistance.PULL_DOWN) };

	// create gpio controller instance
	public void moveForward(boolean forward, boolean backward) throws InterruptedException {
		// initialize wiringPi library

		if (forward == true) {
			pin12.low();
			pin13.high();
			Thread.sleep(100);

			pin12.low();
			pin13.low();

			forward = false;
		} else if (backward == true) {
			pin12.high();
			pin13.low();
			Thread.sleep(100);

			pin12.low();
			pin13.low();

			backward = false;
		}
	}

	public void writeLineToLCD(int row, String line) {
		lcd.clear();
		lcd.write(row, line);
	}

	public void clearLineToLCD() {
		lcd.clear();
	}

	public boolean[] checkButtons() {
		// logger.debug("Button pressed = " + myButtons[0].getState());
		if (myButtons[0].getState() == PinState.HIGH)
			checkButtons[0] = true;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (myButtons[0].getState() == PinState.LOW) {
			checkButtons[0] = false;
		}
		if (myButtons[1].getState() == PinState.HIGH) {
			checkButtons[1] = true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (myButtons[1].getState() == PinState.LOW) {
			checkButtons[1] = false;
		}

		return checkButtons;
	}
}