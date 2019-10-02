package jp.aoyama.a5816071.tn_rri;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.hardware.Sensor.TYPE_HEART_BEAT;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private TextView buttonView;
    double heart_rate_interval=0;
    double value=0;
    int count=0;
    double time=0;
    SensorManager mSensorManager;
    Date date=new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_kkmmss");
    String filename = sdf.format(date) + ".csv";
    String dataFile=String.valueOf(date);
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonView = (TextView) findViewById(R.id.touched);
        buttonView.setText("No button has been pressed\n"+date);

        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::WakelockTag");
        wakeLock.acquire();
        // Enables Always-on
        setAmbientEnabled();
    }
    protected void onResume() {
        super.onResume();
        Sensor sensor = mSensorManager.getDefaultSensor(TYPE_HEART_BEAT);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG,"onAccuracyChanged - accuracy: " + accuracy);
    }

    public void onSensorChanged(SensorEvent event) {

        time = event.timestamp/Math.pow(10, 6);
        double x = event.values[0];

        heart_rate_interval=Math.abs(heart_rate_interval-time);
        if(count!=0) {
            value = value + heart_rate_interval;
        }

        String s = String.valueOf(x);
        String t = String.valueOf(heart_rate_interval);
        String u = String.valueOf(value);
        heart_rate_interval=time;
        count++;

        sampleFileOutput(filename,count, t, u);
    }

    public void count_1(View view){
        buttonView.setText("Button1 was pressed\n");
        labelFileOutput(1);
    }
    public void count_2(View view){
        buttonView.setText("Button2 was pressed\n");
        labelFileOutput(2);
    }
    public void count_3(View view){
        buttonView.setText("Button3 was pressed\n");
        labelFileOutput(3);
    }
    public void count_4(View view){
        buttonView.setText("Button4 was pressed\n");
        labelFileOutput(4);
    }

    private void sampleFileOutput(String filename, int count, String data, String time){
        OutputStream out;
        Date date=new Date();
        if(count!=1) {
            try {
                out = openFileOutput(filename, MODE_PRIVATE | MODE_APPEND);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));

                writer.append(data + ", " + time + date +"\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void labelFileOutput(int number){
        OutputStream out;
        Date date=new Date();
        try {
            out = openFileOutput("label", MODE_PRIVATE | MODE_APPEND);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));

            writer.append(number + ", " + date +"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
