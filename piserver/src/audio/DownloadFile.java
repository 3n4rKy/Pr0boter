package audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.util.FileEvent;

public class DownloadFile implements Runnable {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private FileEvent fileEvent;
    private File dstFile = null;
    private FileOutputStream fileOutputStream = null;

    private static Logger logger = LoggerFactory.getLogger(DownloadFile.class);
    		
    
    public DownloadFile() {

    }

    /**
    * Accepts socket connection
    */
    public void doConnect() {
        try {
        	if (serverSocket == null) {
            serverSocket = new ServerSocket(4445);
        	}
            socket = serverSocket.accept();
            
        } catch (IOException e) {
        	logger.error("TCP connection could not be established");
            e.printStackTrace();
        }
    }

    /**
    * Reading the FileEvent object and copying the file to disk.
    */
    public void downloadFile() {
        try {
        	if (inputStream == null) {
        	inputStream = new ObjectInputStream(socket.getInputStream());
        	}
            fileEvent = (FileEvent) inputStream.readObject();
            System.out.println(fileEvent.getFilename());
            System.out.println(fileEvent.getDestinationDirectory());
            
            if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
               logger.error("Error occurred ..So exiting");
                System.exit(0);
            }
            
            String outputFile = fileEvent.getDestinationDirectory() +"/" + fileEvent.getFilename();
            System.out.println(outputFile);
            if (!new File(fileEvent.getDestinationDirectory()).exists()) {
                new File(fileEvent.getDestinationDirectory()).mkdirs();
            }
            dstFile = new File(outputFile);
            fileOutputStream = new FileOutputStream(dstFile);
            fileOutputStream.write(fileEvent.getFileData());
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println("Output file : " + outputFile + " is successfully saved ");
            serverSocket.close();
            socket.close();
            
            Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","play " + outputFile});
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void run() {
		doConnect();
		downloadFile();
	}
}
