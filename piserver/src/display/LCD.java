package display;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pins.GPIO;
import pins.GPIOFactory;

public class LCD {
	private static Logger logger = LogManager.getLogger(LCD.class.getName());
	public final static int LCD_ROW_1 = 0;
	public final static int LCD_ROW_2 = 1;
	final GPIO gp = GPIOFactory.getInstance();
	public String persistentLCDLine = "";
	ArrayList<String> lineList = new ArrayList<>();

	private void writeLineSingleLine(String line, boolean temporary) {
		logger.debug("lineList.size() = " + lineList.size());
		if (lineList.size() > 100) {
			lineList.remove(0);
		}
		if (temporary) {
			try {
				gp.writeLineToLCD(LCD_ROW_1, line);
				Thread.sleep(500);
				gp.clearLineToLCD();
				gp.writeLineToLCD(LCD_ROW_1, persistentLCDLine);
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

		}
		gp.writeLineToLCD(LCD_ROW_1, line);
		persistentLCDLine = line;
		lineList.add(line);
	}

	public void writeLine(String line) {
		writeLineSingleLine(line, false);
	}

	public void writeLineTemporary(String line) {
		writeLineSingleLine(line, true);
	}

	public boolean[] getButtonState() {
		boolean[] buttonState = gp.checkButtons();
		if (buttonState[0] == true)
			gp.ledOn();
		if (buttonState[0] == true)
			gp.ledOff();
		return buttonState;
	}

}
