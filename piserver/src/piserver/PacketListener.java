package piserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import audio.DownloadFile;

import com.pi4j.system.NetworkInfo;
import com.util.PacketSender;

public class PacketListener {
	static String[] str;
	static String regex = ";";
	static int port = 9876;
	private static Logger logger = LoggerFactory.getLogger(DownloadFile.class);

	public static void main(String[] args) throws SocketException, IOException, InterruptedException {
		logger.info("Start Server");

		DatagramSocket serverSocket = new DatagramSocket(port);
		PacketSender ps = new PacketSender();
		String msg = null;

		while (true) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			str = sentence.split(regex);
			System.out.println(sentence);
			if (sentence.contains("ping")) {
				for (int i = 0; i < 2; i++) {
					String[] ipAddress = NetworkInfo.getIPAddresses();
					msg = "pong;" + ipAddress[0];
					ps.sendPacket(InetAddress.getByName("255.255.255.255"), msg, true);
				}
			} else if (sentence.contains("connect")) {
				download();
			} else if (sentence.contains("cmd_")) {
				Move mv = new Move(str[0], str[1], str[2], str[3]);
			}
		}
	}

	public static void download() {

		Thread thread = new Thread(new DownloadFile());
		thread.start();
		System.out.println("connect accept");
	}
}
