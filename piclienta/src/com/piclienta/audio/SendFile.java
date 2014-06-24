package com.piclienta.audio;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Intent;
import android.os.AsyncTask;

import com.util.FileEvent;
import com.util.PacketSender;

public class SendFile implements Runnable {
	private Socket socket = null;
	private ObjectOutputStream outputStream = null;
	private boolean isConnected = false;
	private FileEvent fileEvent = null;
	private String destinationPath = "/home/pi/Desktop";
	public String filename = null;
	private String sourceFilePath = "/storage/emulated/0/audiorecordtest.3gp";
	PacketSender ps = new PacketSender();
	String ip;
	InetAddress ipAddress;
	String msg = "connect";

	public SendFile(Intent intent) {
		filename = intent.getExtras().getString("audioFileName");
		ip = intent.getExtras().getString("ip");
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Connect with server code running in local host or in any other host
	 */
	public void connect() {
		while (!isConnected) {
			try {
				socket = new Socket(ip.trim(), 4445);
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				isConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sending FileEvent object.
	 */
	public void sendFile(String filename) {
		fileEvent = new FileEvent();
		String fileName = filename;
		fileEvent.setDestinationDirectory(destinationPath);
		fileEvent.setFilename(fileName);
		fileEvent.setSourceDirectory(sourceFilePath);
		File file = new File(sourceFilePath);
		if (file.isFile()) {
			try {
				DataInputStream diStream = new DataInputStream(new FileInputStream(file));
				long len = (int) file.length();
				byte[] fileBytes = new byte[(int) len];
				int read = 0;
				int numRead = 0;
				while (read < fileBytes.length
						&& (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
					read = read + numRead;
				}
				fileEvent.setFileSize(len);
				fileEvent.setFileData(fileBytes);
				fileEvent.setStatus("Success");
				diStream.close();
				outputStream.writeObject(fileEvent);
				System.out.println("Done...Going to exit");
			} catch (Exception e) {
				e.printStackTrace();
				fileEvent.setStatus("Error");
			}
		} else {
			System.out.println("path specified is not pointing to a file");
			fileEvent.setStatus("Error");
			;
		}
	}

	@Override
	public void run() {
		GetIpAddress gia = new GetIpAddress(SendFile.this);
		gia.execute("0");

	}

	class GetIpAddress extends AsyncTask {
		String ip;
		InetAddress ipAddress;

		GetIpAddress(SendFile sf) {
			ip = sf.ip;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				ipAddress = InetAddress.getByName(ip.trim());
				ps.sendPacket(ipAddress, msg, false);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			synchronized (ipAddress) {
				try {
					ipAddress.wait(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			setIpAddress(ipAddress);
			connect();
			sendFile(filename);
			return null;
		}

	}
}
