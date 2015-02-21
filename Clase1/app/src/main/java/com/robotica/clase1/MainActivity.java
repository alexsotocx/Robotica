package com.robotica.clase1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener, SensorEventListener {

	private TextView mTextConnectionStatus;
	private Switch mSwitch1;
	private TextView mTextProgress;
	private TextView mTextProgress2;
	private SeekBar mSeekBar;
	private SeekBar mSeekBar2;
	private Switch mSwitch2;
	private Switch mSwitch3;
	private TextView hexText;

    //acelerometro
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float lastX=0;
    private float lastY=0;
    private float lastZ=0;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTextConnectionStatus = (TextView) findViewById(R.id.connection_status_text);
		mTextProgress = (TextView) findViewById(R.id.progress1);
		mTextProgress2 = (TextView) findViewById(R.id.progress2);
		mSeekBar = (SeekBar) findViewById(R.id.slider_1);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mTextProgress.setText("" + progress);
				updateHexText();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		mSeekBar2 = (SeekBar) findViewById(R.id.slider_2);
		mSeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mTextProgress2.setText("" + progress);
				updateHexText();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});



		mSwitch1 = (Switch) findViewById(R.id.switch1);
		mSwitch2 = (Switch) findViewById(R.id.switch2);
		mSwitch3 = (Switch) findViewById(R.id.switch3);
		mSwitch1.setOnCheckedChangeListener(this);
		mSwitch2.setOnCheckedChangeListener(this);
		mSwitch3.setOnCheckedChangeListener(this);

		hexText = (TextView) findViewById(R.id.hex_text);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            // fail! we don't have an accelerometer!
        }

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



	private void updateHexText(){
		String t2 = mSwitch2.isChecked()? "01" : "00";
		String t3 = mSwitch3.isChecked()? "01" : "00";
		String s1 = (mSeekBar.getProgress() < 16 ? "0":"") +  Integer.toHexString(mSeekBar.getProgress());
		String s2 = (mSeekBar2.getProgress() < 16 ? "0":"") +  Integer.toHexString(mSeekBar2.getProgress());
		hexText.setText(("7E  " + t2 + "  " + t3 + "  " + s1 + "  " + s2 + "  00").toUpperCase() );
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()){
			case R.id.switch1:
				mTextConnectionStatus.setText(isChecked? getString(R.string.on) : getString(R.string.off));
		}
		updateHexText();

	}

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x=event.values[0];
        float y=event.values[1];
        float z=event.values[2];

        int deltaX=(int) (x-lastX);
        int deltaY=(int) (y-lastY);
        int deltaZ=(int) (z-lastZ);

        lastX=x;
        lastY=y;
        lastZ=z;

        mSeekBar.incrementProgressBy(deltaX);
        mSeekBar2.incrementProgressBy(deltaY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
