package display;

import pins.GPIO;
import pins.GPIOFactory;

public class LCD {
	public final static int LCD_ROW_1 = 0;
	public final static int LCD_ROW_2 = 1;
	final GPIO gp = GPIOFactory.getInstance();
	public String persistentLCDLine = "";
	
	
	private void writeLineSingleLine(String line, boolean temporary) {
		if (temporary) {
			
			try {
				gp.writeLineToLCD(LCD_ROW_1, line);
				Thread.sleep(500);
				gp.clearLineToLCD();
				gp.writeLineToLCD(LCD_ROW_1, persistentLCDLine);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		gp.writeLineToLCD(LCD_ROW_1, line);
		persistentLCDLine = line;
	}
    
    public void writeLine(String line) {
    	writeLineSingleLine(line, false);
    }
    public void writeLineTemporary(String line) {
    	writeLineSingleLine(line, true);
    }
       
}
