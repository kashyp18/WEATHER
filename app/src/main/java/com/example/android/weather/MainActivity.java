package com.example.android.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView mTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextview = (TextView) findViewById(R.id.weather_data);

        String[] weather = Weather.ListDetails();


        for (String Weather : weather) {
            mTextview.append(Weather + "\n\n\n");
        }


    }
}
