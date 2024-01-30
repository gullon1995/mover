package com.example.mover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long LastUpdate=0;
    private float last_x,last_y, last_z;
    private static final int SHAKE_THRESHOLD=600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager miSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> listasensores = miSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        for(Sensor sensor: listasensores){
            Log.e("sensor",sensor.getName());
        }
        senSensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer=senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float x= sensorEvent.values[0];
            float y= sensorEvent.values[1];
            float z= sensorEvent.values[2];

            Log.e("sensor",""+x);
            Log.e("sensor",""+y);
            Log.e("sensor",""+z);

            long curTime = System.currentTimeMillis();
            if((curTime-LastUpdate)>100){
                long diffTime= (curTime-LastUpdate);
                LastUpdate = curTime;

                float speed = Math.abs(x+y+z-last_x-last_y)/diffTime *10000;
                if(speed> SHAKE_THRESHOLD){
                    agitar();
                }
                last_x= x;
                last_y= y;
                last_z= z;
            }
        }
    }

    public void agitar(){
        Context context = getApplicationContext();
        CharSequence text="AGITADO!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }
}