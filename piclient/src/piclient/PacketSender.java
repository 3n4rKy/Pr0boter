package piclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketSender {
	public void sendPacket(InetAddress ipAddress, String sentence) throws IOException {
		// TODO Auto-generated method stub
		DatagramSocket clientSocket = new DatagramSocket();

		byte[] sendData = new byte[1024];

		//sentence = "CMD_FORWARD";
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, 9876);
		try {
		clientSocket.send(sendPacket);
		}
		catch (IOException e){
			e.getMessage();			
		}
		clientSocket.close();
	}

}
