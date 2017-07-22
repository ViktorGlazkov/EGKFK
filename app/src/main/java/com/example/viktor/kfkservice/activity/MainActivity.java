package com.example.viktor.kfkservice.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.viktor.kfkservice.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startMeasurmentActivity(View view) {
        Intent intent = new Intent(this, MeasurementActivity.class);
        startActivity(intent);
    }
}
