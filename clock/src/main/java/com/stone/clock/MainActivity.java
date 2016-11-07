package com.stone.clock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stone.clock.view.ClockView;

public class MainActivity extends AppCompatActivity {

    private ClockView mClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClockView = new ClockView(this);
        setContentView(mClockView);
    }

}
