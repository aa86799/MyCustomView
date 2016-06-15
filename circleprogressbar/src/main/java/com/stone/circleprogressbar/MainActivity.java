package com.stone.circleprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import com.stone.circleprogressbar.view.CircleProgressbar;
import com.stone.circleprogressbar.view.ObliqueProgressbar;

import java.math.BigDecimal;
import java.util.Random;

public class MainActivity extends Activity {

    private CircleProgressbar mCircleProgressbar;
    private float mCircleProgress;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mCircleProgress > 100f) {
                mCircleProgress = 100.00f;
                mCircleProgressbar.setProgress(twoFraction(mCircleProgress));
                return;
            }
            mCircleProgressbar.setProgress(twoFraction(mCircleProgress));
            mCircleProgress += 2.33f;
            mCircleProgress = twoFraction(mCircleProgress);
            mHandler.sendEmptyMessageDelayed(0, new Random().nextInt(50) + 200);
        }
    };

    private ObliqueProgressbar mObliqueProgressbar;
    private float mObProgress;
    private Handler mObHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (mObProgress >= 1.1f) {
                mObliqueProgressbar.setProgress(mObProgress);
                return;
            }
            mObProgress += 0.04;
            mObliqueProgressbar.setProgress(mObProgress);
            mObHandler.sendEmptyMessageDelayed(0, new Random().nextInt(50) + 400);
        }
    };

    private ProgressBar mProgressBar;
    private Handler mHoriHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = mProgressBar.getProgress();
            mProgressBar.setProgress(++progress);
            if (progress >= 100) {
                return;

            }
            mHoriHandler.sendEmptyMessageDelayed(0, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mCircleProgressbar = (CircleProgressbar) findViewById(R.id.cp_bar);
        mHandler.sendEmptyMessage(0);

        mObliqueProgressbar = (ObliqueProgressbar) findViewById(R.id.op_bar);
        mObHandler.sendEmptyMessage(0);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_bar);
        mHoriHandler.sendEmptyMessageDelayed(0, 100);

    }

    private float twoFraction(float param) {
        BigDecimal decimal = new BigDecimal(param);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
