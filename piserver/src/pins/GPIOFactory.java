package pins;

/**
 * Ensures only one GPIO declaration is initialized
 * @author nrk
 *
 */
public class GPIOFactory {
	private static GPIO pins = null; 
	public static GPIO getInstance() {
        // if a controller has not been created, then create a new instance
        // Note: this is not thread safe singleton 
        if (pins == null) {
            pins = new GPIO();
        }
        // else return a copy of the existing controller
        return pins;
    }
}
