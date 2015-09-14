package piserver;

import pins.GPIO;
import pins.GPIOFactory;

public class Move {
	String CMD_FORWARD_1 = "cmd_forward_1";
	String CMD_FORWARD_0 = "cmd_forward_0";
	String CMD_BACKWARD_1 = "cmd_backward_1";
	String CMD_BACKWARD_0 = "cmd_backward_0";
	String CMD_LEFT_1 = "cmd_left_1";
	String CMD_LEFT_0 = "cmd_left_0";
	String CMD_RIGHT_1 = "cmd_right_1";
	String CMD_RIGHT_0 = "cmd_right_0";
	
	String CMD_FORWARD;
	String CMD_BACKWARD;
	String CMD_LEFT;
	String CMD_RIGHT;
	
	boolean forward;
	boolean backward;
	boolean left;
	boolean right;
	
	final GPIO gp = GPIOFactory.getInstance();
	
	public Move() {
			
	}
	
	public void command(String cmd_forward, String cmd_backward, String cmd_left, String cmd_right) throws InterruptedException {
		CMD_FORWARD = cmd_forward;
		CMD_BACKWARD = cmd_backward;
		CMD_LEFT = cmd_left;
		CMD_RIGHT = cmd_right;
		// create soft-pwm pins (min=0 ; max=100)
		
		if (CMD_FORWARD.equals(CMD_FORWARD_1)) forward = true;
		if (CMD_FORWARD.equals(CMD_FORWARD_0)) forward = false;
		if (CMD_BACKWARD.equals(CMD_BACKWARD_1)) backward = true;
		if (CMD_BACKWARD.equals(CMD_BACKWARD_0)) backward = false;
		if (CMD_LEFT.equals(CMD_LEFT_1)) left = true;
		if (CMD_LEFT.equals(CMD_LEFT_0)) left = false;
		if (CMD_RIGHT.equals(CMD_RIGHT_1)) right = true;
		if (CMD_RIGHT.equals(CMD_RIGHT_0)) right = false;
		gp.moveForward(forward, backward);
			
		
	}
}
