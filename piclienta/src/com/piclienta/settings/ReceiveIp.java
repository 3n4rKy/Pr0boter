package com.piclienta.settings;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Timer;

import com.piclienta.main.MainActivity;
import com.util.IpAddressEvent;

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
	Settings set = null;
	MainActivity mainActivity = null;
	String PONG = "pong";
	String regex = ";|\"";

	public ReceiveIp(Settings settings) {
		set = settings;
		timer = set.timer;
	}

	public ReceiveIp(MainActivity ma) {
		mainActivity = ma;
		timer = ma.timer;
	}

	public void run() {
		try {
			serverSocket = new DatagramSocket(9876);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		while (!stop) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			try {
				serverSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String sentence = new String(receivePacket.getData());
			if (sentence.contains(PONG)) {
				String[] str = sentence.split(regex);
				String ip = str[1];

				stop = true;
				timer.cancel();
				timer.purge();
				if (set != null && mainActivity == null)
					set.setButtonEnabled(ip);
				if (mainActivity != null && set == null)
					mainActivity.ip = ip;
					mainActivity.showIp();
			}
		}
		serverSocket.close();
	}
}
