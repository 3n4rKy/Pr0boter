package display;

import java.util.ArrayList;

import pins.GPIO;
import pins.GPIOFactory;

public class LCD {
	public final static int LCD_ROW_1 = 0;
	public final static int LCD_ROW_2 = 1;
	final GPIO gp = GPIOFactory.getInstance();
	public String persistentLCDLine = "";
	ArrayList<String> lineList = new ArrayList<>();
	
	private void writeLineSingleLine(String line, boolean temporary) {
		if (temporary) {
			
			try {
				gp.writeLineToLCD(LCD_ROW_1, line);
				Thread.sleep(500);
				gp.clearLineToLCD();
				gp.writeLineToLCD(LCD_ROW_1, persistentLCDLine);
				if (!lineList.get(lineList.size()-1).equals(line)) {
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
    
    
}
