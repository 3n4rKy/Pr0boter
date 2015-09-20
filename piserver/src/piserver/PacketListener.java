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
	static boolean buttonState[];
	
	public static void main(String[] args) throws InterruptedException, IOException {
		LCD lcd = new LCD();
		Receiver receiver = new Receiver(lcd);
		receiver.addLoopListener(lcd);
		receiver.receive();
	}

	
}
