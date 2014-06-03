package com.piclienta;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class GetIpWorker extends AsyncTask<String, String, String> {

	@Override
protected String doInBackground(String... params) {
		try {
			PacketSender ps = new PacketSender();
			ps.sendPacket(InetAddress.getByName("255.255.255.255"), "ping", true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	return null;
}
	@Override
	protected void onPostExecute(String result) {
		
	}
	@Override
	protected void onProgressUpdate(String... str) {
		
	}

	@Override
	protected void onPreExecute() {
	}
}


