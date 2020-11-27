package com.example.a2ndminiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    CountUppyTimer timer;

    // experimental values for hi and lo magnitude limits
    private final double HI_STEP = 11.0;     // upper mag limit
    private final double LO_STEP = 8.0;      // lower mag limit
    boolean highLimit = false;      // detect high limit
    int counter = 0;                // step counter
    int seconds;
    double energy, distance;
    TextView  tvTimer, tvSteps;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvTimer = findViewById(R.id.tvTimer);
        tvSteps = findViewById(R.id.tvSteps);

        // we are going to use the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        timer = new CountUppyTimer(300000) {  // should be high for the run (ms)
            public void onTick(int second) {
                tvTimer.setText(String.valueOf(second));
                seconds = second;
            }
        };
    }

    public void doStart(View view) {
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        timer.start();
        Toast.makeText(this, "Started counting", Toast.LENGTH_LONG).show();
    }

    public void doStop(View view) {
        mSensorManager.unregisterListener(this);
        timer.cancel();
        Toast.makeText(this, "Stopped Run", Toast.LENGTH_LONG).show();
    }

    public void doReset(View view) {
        mSensorManager.unregisterListener(this);
        tvSteps.setText("0");
        counter = 0;
        seconds = 0;
        tvTimer.setText("0");
        Toast.makeText(this, "Reset", Toast.LENGTH_LONG).show();
    }

    public void doRun(View view)
    {
        Intent  run = new Intent(view.getContext(),Page2.class);

        energy = doCalories();
        distance = doDistance();
        Bundle b = new Bundle();
        b.putInt("Steps",counter);
        b.putDouble("Distance", distance);
        b.putDouble("nrg", energy);
        b.putInt("Time", seconds);
        run.putExtras(b);
        startActivity(run);
    }

    public double doCalories()
    {
        double cal = counter * 0.04;
        return cal;
    }


    public double doDistance()
    {
        double dis = counter * 0.08;
        return dis;
    }






    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];



        // get a magnitude number using Pythagorus's Theorem
        double mag = round(Math.sqrt((x*x) + (y*y) + (z*z)), 2);


        // for me! if msg > 11 and then drops below 9, we have a step
        // you need to do your own mag calculating
        if ((mag > HI_STEP) && (highLimit == false)) {
            highLimit = true;
        }
        if ((mag < LO_STEP) && (highLimit == true)) {
            // we have a step
            counter++;
            tvSteps.setText(String.valueOf(counter));
            highLimit = false;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}