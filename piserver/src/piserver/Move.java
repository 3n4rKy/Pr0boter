package piserver;

import java.io.IOException;
import java.util.BitSet;

import pins.GPIO;
import pins.GPIOFactory;

/**
 * Sends moving commands via GPIO to the engines
 *
 * @author nrk
 *
 */
public class Move {
	String CMD_FORWARD_1 = "cmd_forward_1";
	String CMD_FORWARD_0 = "cmd_forward_0";
	String CMD_FORWARD_LEFT_1 = "cmd_forwardleft_1";
	String CMD_FORWARD_LEFT_0 = "cmd_forwardleft_0";
	String CMD_FORWARD_RIGHT_1 = "cmd_forwardright_1";
	String CMD_FORWARD_RIGHT_0 = "cmd_forwardright_0";
	String CMD_BACKWARD_1 = "cmd_backward_1";
	String CMD_BACKWARD_0 = "cmd_backward_0";
	String CMD_BACKWARD_LEFT_1 = "cmd_backwardleft_1";
	String CMD_BACKWARD_LEFT_0 = "cmd_backwardleft_0";
	String CMD_BACKWARD_RIGHT_1 = "cmd_backwardright_1";
	String CMD_BACKWARD_RIGHT_0 = "cmd_backwardright_0";
	String CMD_STRAFE_LEFT_1 = "cmd_strafeleft_1";
	String CMD_STRAFE_LEFT_0 = "cmd_strafeleft_0";
	String CMD_STRAFE_RIGHT_1 = "cmd_straferight_1";
	String CMD_STRAFE_RIGHT_0 = "cmd_straferight_0";
	String CMD_LEFT_1 = "cmd_left_1";
	String CMD_LEFT_0 = "cmd_left_0";
	String CMD_RIGHT_1 = "cmd_right_1";
	String CMD_RIGHT_0 = "cmd_right_0";

	boolean forward;
	boolean forwardLeft;
	boolean forwardRight;
	boolean backward;
	boolean backwardLeft;
	boolean backwardRight;
	boolean strafeLeft;
	boolean strafeRight;
	boolean left;
	boolean right;
	int powerLevel;
	int bitCommandLength = 10;
	
	
	BitSet bitCommand = new BitSet();
	byte[] byteCommand;
	
	final GPIO gp = GPIOFactory.getInstance();
	SerialCom sc = SerialComFactory.getInstance();

	public Move() {

	}

	public void command(String cmd_forward, String cmd_forward_left, String cmd_forward_right, String cmd_backward,
			String cmd_backward_left, String cmd_backward_right, String cmd_strafe_left, String cmd_strafe_right,
			String cmd_left, String cmd_right, String powerLevelString) throws InterruptedException {
		powerLevel = Integer.parseInt(powerLevelString.trim());
		bitCommand.set(10);
		cleanCommand();
		
		if (cmd_forward.equals(CMD_FORWARD_1)) {
			forward = true;
			bitCommand.set(0, true);
			//gp.moveForward(powerLevel);
		}
		if (cmd_forward_left.equals(CMD_FORWARD_LEFT_1)) {
			forwardLeft = true;
			bitCommand.set(1, true);
			//gp.moveForwardLeft(powerLevel);
		}
		if (cmd_forward_right.equals(CMD_FORWARD_RIGHT_1)) {
			forwardRight = true;
			bitCommand.set(2, true);
			//gp.moveForwardRight(powerLevel);
		}
		if (cmd_backward.equals(CMD_BACKWARD_1)) {
			backward = true;
			bitCommand.set(3, true);
			//gp.moveBackward(powerLevel);
		}
		if (cmd_backward_left.equals(CMD_BACKWARD_LEFT_1)) {
			backwardLeft = true;
			bitCommand.set(4, true);
			//gp.moveBackwardLeft(powerLevel);
		}
		if (cmd_backward_right.equals(CMD_BACKWARD_RIGHT_1)) {
			backwardRight = true;
			bitCommand.set(5, true);
			//gp.moveBackwardRight(powerLevel);
		}
		if (cmd_strafe_left.equals(CMD_STRAFE_LEFT_1)) {
			strafeLeft = true;
			bitCommand.set(6, true);
			//gp.moveStrafeLeft(powerLevel);
		}
		if (cmd_strafe_right.equals(CMD_STRAFE_RIGHT_1)) {
			strafeRight = true;
			bitCommand.set(7, true);
			//gp.moveStrafeRight(powerLevel);
		}
		if (cmd_left.equals(CMD_LEFT_1)) {
			left = true;
			bitCommand.set(8, true);
			//gp.moveTurnLeft(powerLevel);
		}
		if (cmd_right.equals(CMD_RIGHT_1)) {
			right = true;
			bitCommand.set(9, true);
			//gp.moveTurnRight(powerLevel);
		}
		System.out.println("bitCommand: "+bitCommand);
		byteCommand = bitCommand.toByteArray();
		System.out.println("byteCommand: "+byteCommand);
		try {
			sc.sendByte(byteCommand, powerLevel);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void cleanCommand() {
		for (int i = 0; i<bitCommandLength; i++) {
			bitCommand.set(i,false);
			System.out.println("bitCommand.set: " + i); 
		}
	}
}
