package com.piclienta;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


public class ReceiveIp extends AsyncTask<String, String, String>{
	Context context;
	
	public ReceiveIp(Options op) {
		context = op.context;
	}
		
	
	@Override	
	protected String doInBackground(String... params) {
		 DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(9876);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			while (true) {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {
					serverSocket.receive(receivePacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String sentence = new String(receivePacket.getData());
				Toast.makeText(context, sentence, Toast.LENGTH_SHORT).show();
			}
	    }
}
