package com.stone.bitmapandanimator;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.stone.bitmapandanimator.view.BitmapTransView;

import java.util.Random;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/12/29 12 36
 */
public class MainActivity extends Activity {

    private BitmapTransView mBitmapTransView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBitmapTransView = new BitmapTransView(this);
        setContentView(mBitmapTransView);

    }


}
