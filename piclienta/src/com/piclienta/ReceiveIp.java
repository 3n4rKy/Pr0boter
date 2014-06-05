package com.piclienta;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Currency;
import java.util.Timer;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ReceiveIp implements Runnable {
	Context context;
	DatagramSocket serverSocket = null;
	Thread th = null;
	boolean stop = false;
	Timer timer;

	public ReceiveIp(Options op) {
		context = op.context;
		serverSocket = op.serverSocket;
		th = op.receiveIp;
		timer = op.timer;
	}

	public void run() {

		while (!stop) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sentence = new String(receivePacket.getData());
			if (sentence.contains("pong")) {
				String[] str = sentence.split(";");
				
				stop = true;
				timer.cancel();
				timer.purge();
			}
		}
	}
}
