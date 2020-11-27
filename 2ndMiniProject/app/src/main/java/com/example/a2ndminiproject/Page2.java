package com.example.a2ndminiproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.view.View;

public class Page2 extends AppCompatActivity {

    TextView steps,date,dist, time, nrg;
    double energy, distance;
    int counter, seconds;
    Date runDate = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        date = findViewById(R.id.tvDateAns);
        date.setText(String.valueOf(df.format(runDate)));

        Bundle b = getIntent().getExtras();
        counter = b.getInt("Steps");
        seconds = b.getInt("Time");
        distance = b.getDouble("dist");
        energy = b.getDouble("nrg");
        steps = findViewById(R.id.tvStepAns);
        steps.setText(String.valueOf(counter));

        dist = findViewById(R.id.tvDistanceAbs);
        dist.setText(String.valueOf(distance));

        nrg = findViewById(R.id.tvEnergyAns);
        nrg.setText(String.valueOf(energy));

        time = findViewById(R.id.tvSecondsAns);
        time.setText(String.valueOf(seconds));

    }

    public void doFinish(View view)
    {
        finish();
    }
}