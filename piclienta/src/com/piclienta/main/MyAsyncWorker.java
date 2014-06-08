package com.piclienta.main;

import java.net.InetAddress;

import com.util.CommandPrepare;
import com.util.PacketSender;

import android.os.AsyncTask;
import android.widget.TextView;

public class MyAsyncWorker extends AsyncTask<String, String, String> {
	private String resp;
	private boolean pressedBtnForward;
	private boolean pressedBtnBackward;
	private boolean pressedBtnLeft;
	private boolean pressedBtnRight;
	private boolean pressedForward;
	private boolean pressedBackward;
	private boolean pressedLeft;
	private boolean pressedRight;
	private boolean returnEarly;
	private InetAddress ipAddress;
	private String ip;
	private TextView textDrive;
	private boolean send;

	CommandPrepare cp = new CommandPrepare();

	public MyAsyncWorker(MainActivity ma) {
		pressedForward = ma.btnForward.isPressed();
		pressedBackward = ma.btnBackward.isPressed();
		pressedLeft = ma.btnLeft.isPressed();
		pressedRight = ma.btnRight.isPressed();
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
				} else {
					sb.append("stop");
				}

				if (pressedBtnLeft)
					sb.append(" left");
				else if (pressedBtnRight)
					sb.append(" right");
				else
					sb.append("");

				resp = sb.toString();
				String command = cp.setCommand(pressedBtnForward, pressedBtnBackward, pressedBtnLeft, pressedBtnRight);
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
		pressedBtnBackward = pressedBackward;
		pressedBtnLeft = pressedLeft;
		pressedBtnRight = pressedRight;
		returnEarly = !send;
	}
}
