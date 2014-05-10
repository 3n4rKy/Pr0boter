package piserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class PacketListener {
	static String[] str;
	static String regex = ";";

	public static void main(String[] args) throws SocketException, IOException {
		// TODO Auto-generated method stub
		DatagramSocket serverSocket = new DatagramSocket(9876);
		
		while (true) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			str = sentence.split(regex);
			Move mv = new Move(str[0],str[1],str[2],str[3]);
		}
	}
}
