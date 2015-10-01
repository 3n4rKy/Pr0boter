package com.util;

public class CommandPrepare {
	public String setCommand(boolean forward, boolean forwardLeft, boolean forwardRight, boolean backward, 
			boolean backwardLeft, boolean backwardRight, boolean strafeLeft, boolean strafeRight, boolean left,
			boolean right) {
		StringBuilder sb = new StringBuilder();
		String CMD_FORWARD_1 = "cmd_forward_1";
		String CMD_FORWARD_0 = "cmd_forward_0";
		String CMD_FORWARDLEFT_1 = "cmd_forwardleft_1";
		String CMD_FORWARDLEFT_0 = "cmd_forwardleft_0";
		String CMD_FORWARDRIGHT_1 = "cmd_forwardright_1";
		String CMD_FORWARDRIGHT_0 = "cmd_forwardright_0";
		String CMD_BACKWARD_1 = "cmd_backward_1";
		String CMD_BACKWARD_0 = "cmd_backward_0";
		String CMD_BACKWARDLEFT_1 = "cmd_backwardleft_1";
		String CMD_BACKWARDLEFT_0 = "cmd_backwardleft_0";
		String CMD_BACKWARDRIGHT_1 = "cmd_backwardright_1";
		String CMD_BACKWARDRIGHT_0 = "cmd_backwardright_0";
		String CMD_LEFT_1 = "cmd_left_1";
		String CMD_LEFT_0 = "cmd_left_0";
		String CMD_STRAFELEFT_1 = "cmd_strafeleft_1";
		String CMD_STRAFELEFT_0 = "cmd_strafeleft_0";
		String CMD_RIGHT_1 = "cmd_right_1";
		String CMD_RIGHT_0 = "cmd_right_0";
		String CMD_STRAFERIGHT_1 = "cmd_straferight_1";
		String CMD_STRAFERIGHT_0 = "cmd_straferight_0";
		String SEPARATOR = ";";
	
		if (forward) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_FORWARD_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_FORWARD_0);
		}
		
		if (forwardLeft) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_FORWARDLEFT_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_FORWARDLEFT_0);
		}
		
		if (forwardRight) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_FORWARDRIGHT_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_FORWARDRIGHT_0);
		}
		
		if (backward) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_BACKWARD_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_BACKWARD_0);
		}
		
		if (backwardLeft) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_BACKWARDLEFT_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_BACKWARDLEFT_0);
		}
		
		if (backwardRight) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_BACKWARDRIGHT_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_BACKWARDRIGHT_0);
		}
		
		if (strafeLeft) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_STRAFELEFT_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_STRAFELEFT_0);
		}
		
		if (strafeRight) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_STRAFERIGHT_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_STRAFERIGHT_0);
		}

		if (left) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_LEFT_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_LEFT_0);
		}
		
		if (right) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_RIGHT_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_RIGHT_0);
		}
		
		return sb.toString();
	}
}
