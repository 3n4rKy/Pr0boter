package display;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;

import pins.GPIO;
import pins.GPIOFactory;

/**
 * Responsible for printing Strings on LCDisplay and uses array list for
 * skipping between messages via forward and backward button
 * 
 * @author nrk
 *
 */
public class LCD implements IButtonStateChangedListener {
	private static Logger logger = LogManager.getLogger(LCD.class.getName());
	public final static int LCD_ROW_1 = 0;
	public final static int LCD_ROW_2 = 1;
	final GPIO gp = GPIOFactory.getInstance();
	public String persistentLCDLine = "";
	ArrayList<String> lineList = new ArrayList<>();
	int lineListSize = 0;

	public LCD() {
		gp.addButtonStateChangedListener(this);
	}

	private void writeLineSingleLine(String line, boolean temporary) {
		if (lineList.size() > 100) {
			lineList.remove(0);
		}
		if (temporary) {
			try {
				gp.writeLineToLCD(LCD_ROW_1, line);
				Thread.sleep(500);
				if (lineList.size() > 0) {
					if (!lineList.get(lineList.size() - 1).equals(line)) {
						lineList.add(line);
					}

				} else {
					lineList.add(line);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			gp.writeLineToLCD(LCD_ROW_1, line);
			persistentLCDLine = line;
			lineList.add(line);
		}
		lineListSize = lineList.size()-1;
		logger.debug("lineList.size() = " + lineList.size());
		logger.debug("lineListSize = " + lineListSize);
	}

	public void writeLine(String line) {
		writeLineSingleLine(line, false);
	}

	public void writeLineTemporary(String line) {
		writeLineSingleLine(line, true);
	}

	@Override
	public void stateChanged(GpioPin pin, PinState state) {
		// Button 1 "Skip Back"
		if (pin.getName() == "Skip Back" && state == PinState.HIGH) {
			logger.debug("lineList.size() = " + lineList.size());
			logger.debug("lineListSize = " + lineListSize);
			if (lineListSize > 0) {
				gp.writeLineToLCD(LCD_ROW_1, lineList.get(lineListSize - 1));
				lineListSize--;
			}
		}
		// Button 2 "Skip Next"
		if (pin.getName() == "Skip Next" && state == PinState.HIGH) {
			logger.debug("lineList.size() = " + lineList.size());
			logger.debug("lineListSize = " + lineListSize);
			if (lineListSize < lineList.size() - 1) {
				gp.writeLineToLCD(LCD_ROW_1, lineList.get(lineListSize + 1));
				lineListSize++;
			}
		}

	}
}
