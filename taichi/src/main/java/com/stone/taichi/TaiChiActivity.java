package com.stone.taichi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.stone.taichi.view.TaichiView;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/6/21 14 02
 */
public class TaichiActivity extends Activity {

    private TaichiView mTaichiView;
    private int mDegrees;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mDegrees += 10;
            mTaichiView.setDegrees(mDegrees == 360 ? mDegrees = 0 : mDegrees);

            mHandler.sendEmptyMessageDelayed(0, 50);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTaichiView = new TaichiView(this);
        setContentView(mTaichiView);

        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
