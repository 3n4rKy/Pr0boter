package piserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.system.NetworkInfo;
import com.util.PacketSender;

import audio.DownloadFile;
import display.LCD;
/**
 * Is responsible for receiving network packets, sends them to Move class, prints messages on LCDisplay and receives 
 * voice downloads.
 * 
 * @author nrk
 *
 */
public class PacketListener {
	
	static String[] str;
	static String regex = ";";
	static int port = 9876;
	static DatagramSocket serverSocket;
	private static Logger logger = LogManager.getLogger(Main.class.getName());
	static boolean buttonState[];
	private static LCD lcd;
	String[] ipAddress;
	String msg = null;
	PacketSender ps = new PacketSender();

	public PacketListener(LCD lcd) {
		PacketListener.lcd = lcd;
	}
	
	/**
	 * Exchanges ipAddress with client and sends move commands to Move   
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void receive() throws IOException, InterruptedException {
		lcd.writeLineTemporary("Receiver activated");
		ipAddress = NetworkInfo.getIPAddresses();
		try {
			serverSocket = new DatagramSocket(port);
			logger.info("Start DatagramSocket");
			lcd.writeLineTemporary("Start DatagramSocket");
		} catch (SocketException e) {
			logger.error(e.getMessage());
		}
		
		lcd.writeLine(ipAddress[0] + ":" + port);
		Move mv = new Move();

		if (serverSocket != null) {
			while (true) {
			receiving(mv);	
			}
		} else {
			logger.error("socket could not be created: exit");
			return;
		}
	}
	
	private void receiving(Move mv) throws IOException, InterruptedException {
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivePacket);
		String sentence = new String(receivePacket.getData());
		str = sentence.split(regex);
		logger.debug(sentence);

		if (ipAddress == null) {
			logger.error("Could not detect Pi IP adapter");
			return;
		} else if (sentence.contains("ping")) {
			for (int i = 0; i < 1; i++) {
				msg = "pong;" + ipAddress[0];
				ps.sendPacket(InetAddress.getByName("255.255.255.255"), msg, true);
				lcd.writeLineTemporary("IP address exchanged");
			}
		} else if (sentence.contains("connect")) {
			download();
			lcd.writeLine("Download requested");
		} else if (sentence.contains("cmd_")) {
			mv.command(str[0], str[1], str[2], str[3], str[4], str[5], str[6],str[7], str[8], str[9], str[10]);
		}
		receiveData = null;
		receivePacket = null;
		sentence = null;
	}
	
	/**
	 * receives audio file
	 */
	public static void download() {

		Thread thread = new Thread(new DownloadFile());
		thread.start();
		logger.info("Download accepted");
	}
}
