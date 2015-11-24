package piserver;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import display.LCD;
import video.VideoProvider;

public class Main {

	private static Logger logger = LogManager.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws InterruptedException, IOException {
		logger.info("#### Start Server ####");
		Thread startCamera = new Thread(new VideoProvider());
		logger.debug("Camera thread created");
		startCamera.start();
		logger.debug("Camera should be started");
		LCD lcd = new LCD();
		PacketListener packetListener = new PacketListener(lcd);
		packetListener.receive();
	}
}
