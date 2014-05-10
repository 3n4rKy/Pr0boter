package piclient;

public class CommandPrepare {
	public String setCommand(boolean forward, boolean backward, boolean left, boolean right) {
		StringBuilder sb = new StringBuilder();
		String CMD_FORWARD_1 = "cmd_forward_1";
		String CMD_FORWARD_0 = "cmd_forward_0";
		String CMD_BACKWARD_1 = "cmd_backward_1";
		String CMD_BACKWARD_0 = "cmd_backward_0";
		String CMD_LEFT_1 = "cmd_left_1";
		String CMD_LEFT_0 = "cmd_left_0";
		String CMD_RIGHT_1 = "cmd_right_1";
		String CMD_RIGHT_0 = "cmd_right_0";
		String SEPARATOR = ";";
	
		if (forward) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_FORWARD_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_FORWARD_0);
		}
		
		if (backward) {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_BACKWARD_1);
		} else {
			if (!sb.toString().equals("")) sb.append(SEPARATOR);
			sb.append(CMD_BACKWARD_0);
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
