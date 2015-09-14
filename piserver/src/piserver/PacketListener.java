package piserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audio.DownloadFile;

import com.pi4j.system.NetworkInfo;
import com.util.PacketSender;

import display.LCD;

public class PacketListener {
	static String[] str;
	static String regex = ";";
	static int port = 9876;
	static DatagramSocket serverSocket;
	private static Logger logger = LogManager.getLogger(PacketListener.class.getName());

	public static void main(String[] args) throws InterruptedException, IOException {
		LCD lcd = new LCD();
		
		logger.info("#### Start Server ####");
		lcd.writeLineTemporary("Server started");

		String[] ipAddress = NetworkInfo.getIPAddresses();
		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			logger.error(e.getMessage());
		}
		PacketSender ps = new PacketSender();
		lcd.writeLine(ipAddress[0]+":"+port);
		String msg = null;
		Move mv = new Move();

		if (serverSocket != null) {
			while (true) {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());
				str = sentence.split(regex);
				logger.debug(sentence);
				if (sentence.contains("ping")) {
					for (int i = 0; i < 2; i++) {
						if (ipAddress != null) {
							msg = "pong;" + ipAddress[0];
							ps.sendPacket(InetAddress.getByName("255.255.255.255"), msg, true);
						} else {
							logger.error("Could not detect Pi IP adapter");
							return;
						}
					}
				} else if (sentence.contains("connect")) {
					download();
				} else if (sentence.contains("cmd_")) {
					mv.command(str[0], str[1], str[2], str[3]);
				}
				receiveData = null;
				receivePacket = null;
				sentence = null;
			}
		} else {
			logger.error("socket could not created: exit");
			return;
		}
	}

	public static void download() {

		Thread thread = new Thread(new DownloadFile());
		thread.start();
		logger.info("Connect accept");
	}
}
