package com.stone.circleprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.stone.circleprogressbar.view.CircleProgressbar;

import java.math.BigDecimal;
import java.util.Random;

public class MainActivity extends Activity {

    private CircleProgressbar mProgressbar;
    private float count;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (count > 100f) return;
            mProgressbar.setProgress(twoFraction(count));
            count += 2.33f;
            count = twoFraction(count);
            mHandler.sendEmptyMessageDelayed(0, new Random().nextInt(50) + 200);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mProgressbar = (CircleProgressbar) findViewById(R.id.cp_bar);

        mHandler.sendEmptyMessage(0);

    }

    private float twoFraction(float param) {
        BigDecimal decimal = new BigDecimal(param);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
