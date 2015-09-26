package piserver;

import java.io.IOException;
import java.net.DatagramSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import display.LCD;

public class Main {
	static String[] str;
	static String regex = ";";
	static int port = 9876;
	static DatagramSocket serverSocket;
	private static Logger logger = LogManager.getLogger(Main.class.getName());
	static boolean buttonState[];
	
	public static void main(String[] args) throws InterruptedException, IOException {
		logger.info("#### Start Server ####");
		LCD lcd = new LCD();
		PacketListener packetListener = new PacketListener(lcd);
		packetListener.receive();
	}
}
