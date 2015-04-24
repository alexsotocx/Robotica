package com.robotica.clase1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;



public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener, SensorEventListener {

  private TextView mTextConnectionStatus;
  private SwitchCompat mSwitch1;
  private TextView mTextIp;
  private TextView mTextProgress;
  private TextView mTextProgress2;
  private SeekBar mSeekBar;
  private SeekBar mSeekBar2;
  private SwitchCompat mSwitch2;
  private SwitchCompat mSwitch3;
  private SwitchCompat mSwitchAccelerometers;
  private TextView hexText;
  //acelerometro
  private SensorManager sensorManager;
  private Sensor accelerometer;
  private boolean isAccelerometerActive = false;
  private boolean isConnected = false;
  private long lastUpdate;

  private TCPConnection connection;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    mTextConnectionStatus = (TextView) findViewById(R.id.connection_status_text);
    mTextProgress = (TextView) findViewById(R.id.progress1);
    mTextIp = (TextView) findViewById(R.id.ip_address);
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
    mSwitch1 = (SwitchCompat) findViewById(R.id.switch1);
    mSwitch2 = (SwitchCompat) findViewById(R.id.switch2);
    mSwitch3 = (SwitchCompat) findViewById(R.id.switch3);
    mSwitchAccelerometers = (SwitchCompat) findViewById(R.id.switch_acceloremter);
    mSwitch1.setOnCheckedChangeListener(this);
    mSwitch2.setOnCheckedChangeListener(this);
    mSwitch3.setOnCheckedChangeListener(this);
    mSwitchAccelerometers.setOnCheckedChangeListener(this);

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


  private void updateHexText() {
    String t2 = mSwitch2.isChecked() ? "01" : "00";
    String t3 = mSwitch3.isChecked() ? "01" : "00";
    String s1 = (mSeekBar.getProgress() < 16 ? "0" : "") + Integer.toHexString(mSeekBar.getProgress());
    String s2 = (mSeekBar2.getProgress() < 16 ? "0" : "") + Integer.toHexString(mSeekBar2.getProgress());
    String current = ("7E" + t2 + t3 + s1 + s2 + "00").toUpperCase();
    hexText.setText(current);
    char _7E = (char) 126;
    char B1 = (char) (mSwitch2.isChecked() ? 1:0);
    char B2 = (char) (mSwitch3.isChecked() ? 1:0);
    char S1 = (char) mSeekBar.getProgress();
    char S2 = (char) mSeekBar2.getProgress();
    try {
      if (connection != null) {
        mTextConnectionStatus.setText(connection.client.conection());
        connection.client.sendMessage(""+ _7E + "0" +  B1 + B2 + S2 + S1);
      }
    } catch (Exception e) {
      //mTextConnectionStatus.setText("Error");
    }

  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    switch (buttonView.getId()) {
      case R.id.switch1:
        if (isChecked) {
          try {
            connection = new TCPConnection(mTextIp.getText().toString());
            connection.execute("");
            mTextConnectionStatus.setText("En espera");
          } catch (Exception e) {
            mSwitch1.setChecked(false);
            mTextConnectionStatus.setText("Error");
          }
        } else {
          try {
            mTextConnectionStatus.setText(connection.client.stopClient());
          } catch (Throwable throwable) {
            throwable.printStackTrace();
          }
        }
        break;
      case R.id.switch_acceloremter:
        isAccelerometerActive = isChecked;
        break;
    }
    if (buttonView.getId() != R.id.switch1)
      updateHexText();

  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    Sensor sensor = event.sensor;
    if (sensor.getType() == Sensor.TYPE_ACCELEROMETER && isAccelerometerActive) {
      float x = event.values[0];
      float y = event.values[1];
      float z = event.values[2];

      long currentTime = System.currentTimeMillis();
      if ((currentTime - lastUpdate) > 10) {
        lastUpdate = currentTime;
        mSeekBar.setProgress((int) (z * 10));
        mSeekBar2.setProgress((int) (y * 5 + 50));

      }


    }

  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }


  private class TCPConnection extends AsyncTask<String, String, TCPClient> implements TCPClient.OnMessageReceived {
    private String ipAddress;
    private TCPClient client;

    public TCPConnection(String ipAddress) {
      this.ipAddress = ipAddress;
    }


    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(TCPClient tcpClient) {
      super.onPostExecute(tcpClient);
    }

    @Override
    protected void onProgressUpdate(String... values) {

    }


    @Override
    public void messageReceived(String message) {
      publishProgress(message);
      Log.d("TCP message", message);
    }

    @Override
    protected TCPClient doInBackground(String... params) {
      client = new TCPClient(ipAddress, this, mTextConnectionStatus);
      client.run();
      return client;
    }
  }

}
