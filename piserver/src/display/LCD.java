package display;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pins.GPIO;
import pins.GPIOFactory;

public class LCD implements Runnable{
	private static Logger logger = LogManager.getLogger(LCD.class.getName());
	public final static int LCD_ROW_1 = 0;
	public final static int LCD_ROW_2 = 1;
	final GPIO gp = GPIOFactory.getInstance();
	public String persistentLCDLine = "";
	ArrayList<String> lineList = new ArrayList<>();
	int lineListSize = 0;

	private void writeLineSingleLine(String line, boolean temporary) {
		logger.debug("lineList.size() = " + lineList.size());
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
						lineListSize = lineList.size();
					}

				} else {
					lineList.add(line);
					lineListSize = lineList.size();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else {
		gp.writeLineToLCD(LCD_ROW_1, line);
		persistentLCDLine = line;
		lineList.add(line);
		lineListSize = lineList.size();
		}
	}

	public void writeLine(String line) {
		writeLineSingleLine(line, false);
	}

	public void writeLineTemporary(String line) {
		writeLineSingleLine(line, true);
	}

	public void getButtonState() {
		boolean[] buttonState = gp.checkButtons();
		if (buttonState[0] == true) {
			logger.debug("lineListSize = " + lineListSize);
			if (lineListSize > 0) {
				logger.debug("lineListSize = " + lineListSize);
				gp.writeLineToLCD(LCD_ROW_1, lineList.get(lineListSize - 1));
				lineListSize--;
			}
		}
		if (buttonState[1] == true) {
			logger.debug("lineListSize = " + lineListSize);
			if (lineListSize < lineList.size()-1) {
				logger.debug("lineListSize = " + lineListSize);
				gp.writeLineToLCD(LCD_ROW_1, lineList.get(lineListSize + 1));
				lineListSize++;
			}
		}
	}

	@Override
	public void run() {
		getButtonState();
		
	}
}
