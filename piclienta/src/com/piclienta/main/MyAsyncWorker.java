package com.piclienta.main;

import java.net.InetAddress;

import com.util.CommandPrepare;
import com.util.PacketSender;

import android.R.bool;
import android.os.AsyncTask;
import android.widget.TextView;

public class MyAsyncWorker extends AsyncTask<String, String, String> {
	private String resp;
	private boolean pressedBtnForward;
	private boolean pressedBtnBackward;
	private boolean pressedBtnLeft;
	private boolean pressedBtnRight;
	private boolean pressedBtnForwardLeft;
	private boolean pressedBtnForwardRight;
	private boolean pressedBtnBackwardLeft;
	private boolean pressedBtnBackwardRight;
	private boolean pressedBtnStrafeLeft;
	private boolean pressedBtnStrafeRight;
	private boolean pressedForward;
	private boolean pressedBackward;
	private boolean pressedLeft;
	private boolean pressedRight;
	private boolean pressedForwardLeft;
	private boolean pressedForwardRight;
	private boolean pressedBackwardLeft;
	private boolean pressedBackwardRight;
	private boolean pressedStrafeLeft;
	private boolean pressedStrafeRight;
	private boolean returnEarly;
	private InetAddress ipAddress;
	private String ip;
	private TextView textDrive;
	private boolean send;
	private int powerLevel;

	CommandPrepare cp = new CommandPrepare();

	public MyAsyncWorker(MainActivity ma) {
		pressedForward = ma.btnForward.isPressed();
		pressedBackward = ma.btnBackward.isPressed();
		pressedLeft = ma.btnLeft.isPressed();
		pressedRight = ma.btnRight.isPressed();
		pressedForwardLeft = ma.btnForwardLeft.isPressed();
		pressedForwardRight = ma.btnForwardRight.isPressed();
		pressedBackwardLeft = ma.btnBackwardLeft.isPressed();
		pressedBackwardRight = ma.btnBackwardRight.isPressed();
		pressedStrafeLeft = ma.btnStrafeLeft.isPressed();
		pressedStrafeRight = ma.btnStrafeRight.isPressed();
		powerLevel = ma.progress;

		this.ip = ma.ip;
		this.textDrive = ma.textDrive;
		this.send = ma.send;
	}

	@Override
	protected String doInBackground(String... params) {
		StringBuilder sb = new StringBuilder();
		PacketSender ps = new PacketSender();
		try {
			int time = Integer.parseInt(params[0]);
			if (!returnEarly) {
				if (time > 0) {
					Thread.sleep(time);
					publishProgress("Geschlafen für " + time + " Millisekunden");
				}

				if (pressedBtnForward) {
					sb.append("forward");
				} else if (pressedBtnBackward) {
					sb.append("backward");
				} else if (pressedBtnLeft) {
					sb.append("turn left");
				} else if (pressedBtnRight) {
					sb.append("turn right");
				} else if (pressedBtnForwardLeft) {
					sb.append("forward left");
				} else if (pressedBtnForwardRight) {
					sb.append("forward right");
				} else if (pressedBtnBackwardLeft) {
					sb.append("backward left");
				} else if (pressedBtnBackwardRight) {
					sb.append("backward right");
				} else if (pressedBtnStrafeRight) {
					sb.append("strafe right");
				} else if (pressedBtnStrafeLeft) {
					sb.append("strafe left");
				} else {
					sb.append("stop");
				}

				resp = sb.toString();
				String command = cp.setCommand(pressedBtnForward, pressedBtnForwardLeft, pressedBtnForwardRight,
						pressedBtnBackward, pressedBtnBackwardLeft, pressedBtnBackwardRight, pressedBtnStrafeLeft,
						pressedBtnStrafeRight, pressedBtnLeft, pressedBtnRight, powerLevel);
				ipAddress = InetAddress.getByName(ip.trim());

				ps.sendPacket(ipAddress, command, false);
			} else {
				return null;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			resp = e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			resp = e.getMessage();
		}

		return resp;
	}

	@Override
	protected void onPostExecute(String result) {
		textDrive.setText(result);
	}

	@Override
	protected void onProgressUpdate(String... str) {
		textDrive.setText(str[0]);
	}

	@Override
	protected void onPreExecute() {
		pressedBtnForward = pressedForward;
		pressedBtnForwardLeft = pressedForwardLeft;
		pressedBtnForwardRight = pressedForwardRight;
		pressedBtnBackward = pressedBackward;
		pressedBtnBackwardLeft = pressedBackwardLeft;
		pressedBtnBackwardRight = pressedBackwardRight;
		pressedBtnStrafeLeft = pressedStrafeLeft;
		pressedBtnStrafeRight = pressedStrafeRight;
		pressedBtnLeft = pressedLeft;
		pressedBtnRight = pressedRight;
		
		returnEarly = !send;
	}
}
