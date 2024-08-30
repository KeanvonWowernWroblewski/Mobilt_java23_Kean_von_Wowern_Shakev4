package com.example.shakeappv3;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Chronometer chronometer;
    private TextView accelerometerValues;
    private boolean shake = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometerValues = findViewById(R.id.accelVal);
        chronometer = findViewById(R.id.chrono);
        Button resetButton = findViewById(R.id.reset);

        // sets up the sensor manager and accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // starts the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        resetButton.setOnClickListener(v -> resetChronometer());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float accelX = event.values[0];
        float accelY = event.values[1];
        float accelZ = event.values[2];

        // updates and displays accelerometer values on the gui
        accelerometerValues.setText(String.format("X: %.2f\nY: %.2f\nZ: %.2f", accelX, accelY, accelZ));

        // detects if the user shakes the phone and resets the timer
        if (Math.abs(accelX) > 13 || Math.abs(accelY) > 13 || Math.abs(accelZ) > 13) {
            if (!shake) {
                shake = true;
                Toast.makeText(this, "Shaking it isn't going to do anything", Toast.LENGTH_SHORT).show();
                resetChronometer();
            }
        } else {
            shake = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}