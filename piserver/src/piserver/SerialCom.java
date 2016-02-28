package piserver;

import java.io.IOException;
import java.nio.charset.Charset;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

public class SerialCom implements Runnable {
	static int BAUDRATE = 115200;
	final Charset charset = Charset.forName("ISO_8859_1");
	Serial serial;

	public void initSerialListener() throws IOException {
		System.out.println(" ... connect using settings: " + Integer.toString(BAUDRATE) + " , N, 8, 1.");

		// create an instance of the serial communications class
		serial = SerialFactory.createInstance();
		try {
			serial.open(Serial.DEFAULT_COM_PORT, BAUDRATE);
		}
		// create and register the serial data listener
		catch (SerialPortException ex) {
			System.out.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
			return;
		}
		serial.addListener(new SerialDataEventListener() {
			@Override
			public void dataReceived(SerialDataEvent event) {
				// print out the data received to the console
				try {
					System.out.print(event.getString(charset));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void sendByte(byte[] byteCommand, int powerlevel) throws IllegalStateException, IOException {
		if (byteCommand != null && serial != null) {
			serial.write(byteCommand);
		}
	}

	@Override
	public void run() {
		try {
			initSerialListener();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}