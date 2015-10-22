package com.stone.circleprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.stone.circleprogressbar.view.CircleProgressbar;
import com.stone.circleprogressbar.view.ObliqueProgressbar;

import java.math.BigDecimal;
import java.util.Random;

public class MainActivity extends Activity {

    private CircleProgressbar mProgressbar;
    private float mCircleProgress;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mCircleProgress > 100f) return;
            mProgressbar.setProgress(twoFraction(mCircleProgress));
            mCircleProgress += 2.33f;
            mCircleProgress = twoFraction(mCircleProgress);
            mHandler.sendEmptyMessageDelayed(0, new Random().nextInt(50) + 200);
        }
    };

    private ObliqueProgressbar mObliqueProgressbar;
    private float mProgress;
    private Handler mObHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (mProgress >= 1.0f) {
                mObliqueProgressbar.setProgress(0);
                return;
            }
            mProgress += 0.04;
            mObliqueProgressbar.setProgress(mProgress);
            mObHandler.sendEmptyMessageDelayed(0, new Random().nextInt(50) + 400);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mProgressbar = (CircleProgressbar) findViewById(R.id.cp_bar);
        mHandler.sendEmptyMessage(0);

        mObliqueProgressbar = (ObliqueProgressbar) findViewById(R.id.op_bar);
        mObHandler.sendEmptyMessage(0);

    }

    private float twoFraction(float param) {
        BigDecimal decimal = new BigDecimal(param);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
