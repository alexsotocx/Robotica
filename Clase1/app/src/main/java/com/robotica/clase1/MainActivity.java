package com.robotica.clase1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	private Button mButtonDisconnect;
	private Button mButtonConnect;
	private TextView mTextConnectionStatus;
	private TextView mTextProgress;
	private SeekBar mSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mButtonDisconnect = (Button) findViewById(R.id.button_connect);
		mButtonConnect = (Button) findViewById(R.id.button_disconnect);
		mTextConnectionStatus = (TextView) findViewById(R.id.connection_status_text);
		mTextProgress = (TextView) findViewById(R.id.progress1);
		mSeekBar = (SeekBar) findViewById(R.id.slider_1);
		mButtonDisconnect.setOnClickListener(new clickListener());
		mButtonConnect.setOnClickListener(new clickListener());
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mTextProgress.setText("" + progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	private class clickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.button_disconnect:
					mTextConnectionStatus.setText(getString(R.string.off));
					break;
				case R.id.button_connect:
					mTextConnectionStatus.setText(getString(R.string.on));
					break;
			}

		}
	}
}
