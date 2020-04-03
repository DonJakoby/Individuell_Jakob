package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Accelerometers extends AppCompatActivity implements SensorEventListener {
    SensorManager mSensorManager;
    Sensor mAccelerometer;
    Sensor mGyroscope;
    TextView txt_accelerometer;
    ImageView pentagram;
    MediaPlayer devilPlayer;
    MediaPlayer screamPlayer;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometers);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (mAccelerometer == null || mGyroscope == null) {
            noSensorsAlert();
        }
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        txt_accelerometer = (TextView) findViewById(R.id.txt_accelerometer);

        pentagram = (ImageView) findViewById(R.id.img_pentagram);
        devilPlayer = MediaPlayer.create(this, R.raw.devil);

        vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

        screamPlayer = MediaPlayer.create(this, R.raw.scream);
    }

    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = Math.round(event.values[0]);
            int y = Math.round(event.values[1]);
            int z = Math.round(event.values[2]);

            TextView xView = (TextView) findViewById(R.id.xView);
            TextView yView = (TextView) findViewById(R.id.yView);
            TextView zView = (TextView) findViewById(R.id.zView);

            xView.setText("X: " + x);
            yView.setText("Y: " + y);
            zView.setText("Z: " + z);

            String tilt = "Neutral";
            if (x == 0 && y == 0) {
                txt_accelerometer.setTextColor(Color.GREEN);
            } else {
                if (x > 0) {
                    tilt = "Vänster";
                } else if (x < 0) {
                    tilt = "Höger";
                } else if (y < 0) {
                    tilt = "Framåt";
                } else if (y > 0) {
                    tilt = "Bakåt";
                }
                txt_accelerometer.setTextColor(Color.RED);
            }
            txt_accelerometer.setText(tilt);

            if (x == 6 && y == 6 && z == 6) {
                pentagram.setVisibility(View.VISIBLE);
                devilPlayer.start();
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                pentagram.setVisibility(View.INVISIBLE);
            }
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            screamPlayer.start();
            System.out.print(event.values[1]);
            if (event.values[0] > 0.5f || event.values[1] > 0.5f || event.values[2] > 0.5f ||
                    event.values[0] < -0.5f || event.values[1] < -0.5f || event.values[2] < -0.5f) {
                    screamPlayer.start();
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void noSensorsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Accelerometers.")
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }
}
