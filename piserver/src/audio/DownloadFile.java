package audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.util.FileEvent;

public class DownloadFile implements Runnable {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private FileEvent fileEvent;
    private File dstFile = null;
    private FileOutputStream fileOutputStream = null;

    static final Logger logger = LogManager.getLogger(DownloadFile.class);
    		
    
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
            logger.info(fileEvent.getFilename() + " in " + fileEvent.getDestinationDirectory());
           
            
            if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
               logger.error("Error occurred ..So exiting");
                System.exit(0);
            }
            
            String outputFile = fileEvent.getDestinationDirectory() +"/" + fileEvent.getFilename();
            logger.info(outputFile);
            if (!new File(fileEvent.getDestinationDirectory()).exists()) {
                new File(fileEvent.getDestinationDirectory()).mkdirs();
            }
            dstFile = new File(outputFile);
            fileOutputStream = new FileOutputStream(dstFile);
            fileOutputStream.write(fileEvent.getFileData());
            fileOutputStream.flush();
            fileOutputStream.close();
            logger.info("Output file : " + outputFile + " is successfully saved ");
            serverSocket.close();
            socket.close();
            
            Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","play " + outputFile});
            
        } catch (IOException e) {
        	logger.error(e.getMessage());
        } catch (ClassNotFoundException e) {
        	logger.error(e.getMessage());
        }
    }

	@Override
	public void run() {
		doConnect();
		downloadFile();
	}
}
