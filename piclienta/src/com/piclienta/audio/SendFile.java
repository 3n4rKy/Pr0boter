package com.piclienta.audio;
import java.io.*;
import java.net.Socket;

import com.util.FileEvent;

public class SendFile implements Runnable {
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    private FileEvent fileEvent = null;
    private String destinationPath = "/home/pi/Desktop";
    public String filename = null;
    private String sourceFilePath = "/storage/emulated/0/audiorecordtest.3gp";

    public SendFile(Recorder rec) {
    	filename = rec.audioFileName;
    }
  
    /**
    * Connect with server code running in local host or in any other host
    */
    public void connect() {
        while (!isConnected) {
            try {
                socket = new Socket("192.168.2.101", 4445);
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
        //String fileName = filename.substring(0, filename.lastIndexOf("/") + 1);
        fileEvent.setDestinationDirectory(destinationPath);
        fileEvent.setFilename(fileName);
        fileEvent.setSourceDirectory(sourceFilePath);
        File file = new File(fileName);
        if (file.isFile()) {
            try {
                DataInputStream diStream = new DataInputStream(new FileInputStream(file));
                long len = (int) file.length();
                byte[] fileBytes = new byte[(int) len];
                int read = 0;
                int numRead = 0;
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read,
                        fileBytes.length - read)) >= 0) {
                    read = read + numRead;
                }
                fileEvent.setFileSize(len);
                fileEvent.setFileData(fileBytes);
                fileEvent.setStatus("Success");
            } catch (Exception e) {
                e.printStackTrace();
                fileEvent.setStatus("Error");
            }
        } else {
            System.out.println("path specified is not pointing to a file");
            fileEvent.setStatus("Error");
        }
        //Now writing the FileEvent object to socket
        try {
            outputStream.writeObject(fileEvent);
            System.out.println("Done...Going to exit");
            Thread.sleep(3000);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

	@Override
	public void run() {
		connect();
		sendFile(filename);
		
	}
}

