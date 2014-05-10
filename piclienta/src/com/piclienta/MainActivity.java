package com.piclienta;

import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnCheckedChangeListener {

	private static final int REQUEST_CODE = 10;
	Button btnForward;
	Button btnBackward;
	Button btnLeft;
	Button btnRight;
	TextView textDrive;
	private Switch btnSwitch;
	PacketSender ps = new PacketSender();
	final Context context = this;
	public String ip = "192.168.1.201";
	private InetAddress ipAddress = null;
	public boolean send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnForward = (Button) findViewById(R.id.fwd);
		btnBackward = (Button) findViewById(R.id.bwd);
		btnLeft = (Button) findViewById(R.id.lft);
		btnRight = (Button) findViewById(R.id.rgt);
		textDrive = (TextView) findViewById(R.id.textView1);
		btnSwitch = (Switch) findViewById(R.id.switch1);
		btnSwitch.setOnCheckedChangeListener(this);

		startTimer();

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {
			send = true;
			Toast.makeText(this, "Transmission enabled", Toast.LENGTH_LONG).show();
		} else if (!isChecked) {
			send = false;
			Toast.makeText(this, "Transmission disabled", Toast.LENGTH_LONG).show();
		}

	}

	public void startTimer() {
		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						MyAsyncWorker maw = new MyAsyncWorker(MainActivity.this);
						maw.execute("0");
					}
				});
			}
		};
		timer.schedule(task, 0, 100);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.about:
			Toast.makeText(this, "\u00a9 Benjamin Biedermann", Toast.LENGTH_LONG).show();
			return true;
		case R.id.setIp:
			Bundle myBundle = new Bundle();
			myBundle.putString("ip", ip);
			Intent in = new Intent(this, Options.class);
			in.putExtras(myBundle);
			startActivityForResult(in, REQUEST_CODE);

			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.activity_set_ip);

			return true;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("ip")) {
				ip = data.getExtras().getString("ip");
				Toast.makeText(this, "IP " + ip + " saved", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}
	}
}
