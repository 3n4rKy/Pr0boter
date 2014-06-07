package com.piclienta;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Timer;

import android.content.Context;
import android.widget.Button;

public class ReceiveIp implements Runnable {
	Context context;
	DatagramSocket serverSocket = null;
	Thread th = null;
	boolean stop = false;
	boolean buttonEnabled;
	Timer timer;
	Button btnGetIp;
	String ip;
	Options opt;
	String PONG = "pong";

	public ReceiveIp(Options op) {
		context = op.context;
		th = op.receiveIp;
		timer = op.timer;
		btnGetIp = op.btnGetIp;
		buttonEnabled = op.buttonEnabled;
		opt = op;
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
				String[] str = sentence.split(";|\"");
				String ipAddress = str[1];

				stop = true;
				timer.cancel();
				timer.purge();
				opt.setButtonEnabled(ipAddress);

			}
		}
		serverSocket.close();
	}
}
