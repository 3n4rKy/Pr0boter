package com.piclienta.audio;

import java.io.IOException;

import com.piclienta.R;
import com.piclienta.R.id;
import com.piclienta.R.layout;
import com.piclienta.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.os.Build;

public class Recorder extends Activity implements OnClickListener{

	private static final String LOG_TAG = "AudioRecordTest";
	public static String mFileName = null;

	private Button mRecordButton = null;
	private Button mStopRecordButton = null;
	private MediaRecorder mRecorder = null;

	private Button mPlayButton = null;
	private Button mStopPlayButton = null;
	private MediaPlayer mPlayer = null;
	private Button mSendButton = null;
	public String audioFileName = "audiorecordtest.3gp";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recorder);
		mRecordButton = (Button) findViewById(R.id.record);
		mPlayButton = (Button) findViewById(R.id.play);
		mStopRecordButton = (Button) findViewById(R.id.stoprecord);
		mStopPlayButton = (Button) findViewById(R.id.stopplay);
		mSendButton = (Button) findViewById(R.id.sendFile);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recorder, menu);
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

	private void onRecord(boolean start) {
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}

	private void onPlay(boolean start) {
		if (start) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}

	private void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
	}

	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording() {
		mRecorder = new MediaRecorder();
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/" + audioFileName;
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	@Override
	public void onClick(View v) {
		if (v == mRecordButton) {
			startRecording();
		}
		if (v == mStopRecordButton){
			stopRecording();
		}
		if (v == mPlayButton){
			startPlaying();
		}
		if (v == mStopPlayButton) {
			stopPlaying();
		}
		if (v== mSendButton) {
			Thread audioSend = new Thread(new SendFile(Recorder.this));
			audioSend.start();
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
			View rootView = inflater.inflate(R.layout.fragment_recorder, container, false);
			return rootView;
		}
	}

}
