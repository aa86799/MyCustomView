package com.stone.surfaceview;

import android.app.Activity;
import android.os.Bundle;

import com.stone.surfaceview.view.MySurfaceView1;
import com.stone.surfaceview.view.MySurfaceView2;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/12/15 00 42
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MySurfaceView2 sv = new MySurfaceView2(this);
        setContentView(sv);
    }
}
