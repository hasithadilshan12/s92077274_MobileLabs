package com.s92077274.hasitha_all_labs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {


    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private boolean isTemperatureSensorAvailable;


    private TextView textViewTemperature;
    private TextView textViewThreshold;
    private TextView textViewStatus;
    private CardView statusIndicatorCard;


    private MediaPlayer mediaPlayer;


    private final float TEMPERATURE_THRESHOLD = 74.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);


        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewThreshold = findViewById(R.id.textViewThreshold);
        textViewStatus = findViewById(R.id.textViewStatus);
        statusIndicatorCard = findViewById(R.id.statusIndicatorCard);


        textViewThreshold.setText(String.format("%.1f °C", TEMPERATURE_THRESHOLD));


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTemperatureSensorAvailable = true;
        } else {
            textViewTemperature.setText("N/A");
            textViewStatus.setText("Error");
            isTemperatureSensorAvailable = false;

            Toast.makeText(this, "Ambient Temperature Sensor is not available on this device.", Toast.LENGTH_LONG).show();
        }


        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float currentTemperature = sensorEvent.values[0];

            textViewTemperature.setText(String.format("%.1f °C", currentTemperature));


            if (currentTemperature > TEMPERATURE_THRESHOLD) {

                textViewStatus.setText("Alert!");

                statusIndicatorCard.setCardBackgroundColor(Color.parseColor("#F44336"));


                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            } else {

                textViewStatus.setText("Normal");

                statusIndicatorCard.setCardBackgroundColor(Color.parseColor("#6EE7B7"));


                if (mediaPlayer != null && mediaPlayer.isPlaying()) {

                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isTemperatureSensorAvailable) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isTemperatureSensorAvailable) {
            sensorManager.unregisterListener(this);
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
