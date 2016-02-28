package piserver;

public class SerialComFactory {
	private static SerialCom sc = null; 
	public static SerialCom getInstance() {
        // if a controller has not been created, then create a new instance
        // Note: this is not thread safe singleton 
        if (sc == null) {
        	sc = new SerialCom();
        }
        // else return a copy of the existing controller
        return sc;
    }
}
