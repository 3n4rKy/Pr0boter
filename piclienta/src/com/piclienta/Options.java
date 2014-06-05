package com.piclienta;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Options extends Activity implements OnClickListener {
	String ip;
	Button dialogButton;
	Context context;
	EditText editSetIp;
	Button btnGetIp;
	Thread receiveIp = null;
	DatagramSocket serverSocket = null;
	Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_ip);
		editSetIp = (EditText) findViewById(R.id.editIp);
		dialogButton = (Button) findViewById(R.id.save);

		ip = getIntent().getExtras().getString("ip");
		editSetIp.setText(ip);

		btnGetIp = (Button) findViewById(R.id.getIp);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void finish(Intent intent) {

		setResult(RESULT_OK, intent);
		super.finish();
	}

	@Override
	public void onClick(View v) {
		if (v == dialogButton) {
			Intent intent = new Intent();
			intent.putExtra("ip", ip);
			Bundle myBundle = new Bundle();
			ip = editSetIp.getText().toString();
			myBundle.putString("ip", ip);
			intent.putExtras(myBundle);
			finish(intent);
		}

		if (v == btnGetIp) {

			final Handler handler = new Handler();
			 timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					handler.post(new Runnable() {
						public void run() {
							GetIpWorker giw = new GetIpWorker();
							giw.execute("0");
						}
					});
				}
			};
			timer.schedule(task, 0, 100);
			try {
				serverSocket = new DatagramSocket(9876);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.receiveIp = new Thread(new ReceiveIp(Options.this));
			this.receiveIp.start();
					
			
		}
	}
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_set_i, container, false);
			return rootView;
		}
	}

}
