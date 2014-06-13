package com.piclienta.settings;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;

import com.piclienta.main.MainActivity;

import android.content.Context;
import android.widget.Button;

public class ReceiveIpForMain implements Runnable {
	Context context;
	DatagramSocket serverSocket = null;
	Thread th = null;
	boolean stop = false;
	boolean buttonEnabled;
	Timer timer;
	Button btnGetIp;
	String ip;
	MainActivity set;
	String PONG = "pong";
	String regex = ";|\"";

	public ReceiveIpForMain(MainActivity ma) {
		set = ma;
	}

	public void run() {
		try {
			serverSocket = new DatagramSocket(9876);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		while (!stop) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String sentence = new String(receivePacket.getData());
			if (sentence.contains(PONG)) {
				String[] str = sentence.split(regex);
				String ipAddress = str[1];

				stop = true;
				timer.cancel();
				timer.purge();
				try {
					set.setIpAddress(InetAddress.getByName(ipAddress));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		serverSocket.close();
	}
}
