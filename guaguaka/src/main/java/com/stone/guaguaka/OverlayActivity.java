package com.stone.guaguaka;

import android.app.Activity;
import android.os.Bundle;

import com.stone.guaguaka.view.OverlayImageView;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/5/3 22 01
 */
public class OverlayActivity extends Activity {

    private OverlayImageView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = new OverlayImageView(this);
        setContentView(mView);

        mView.setImages(R.drawable.num_01, R.drawable.num_02, R.drawable.num_03,
                R.drawable.num_04, R.drawable.num_05);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        System.out.println(mView.getWidth());
    }
}
