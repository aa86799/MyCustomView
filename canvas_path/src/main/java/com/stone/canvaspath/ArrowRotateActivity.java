package com.stone.canvaspath;

import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;

import com.stone.canvaspath.arrow.ArrowRotateView;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/7/2 10 09
 */
public class ArrowRotateActivity extends Activity {

    private ArrowRotateView mArrowRotateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArrowRotateView = new ArrowRotateView(this);
        setContentView(mArrowRotateView);

        int w = getResources().getDisplayMetrics().widthPixels;
        int h = getResources().getDisplayMetrics().heightPixels;
        int r = Math.min(w, h) / 2-100;

        Path path = new Path();
        path.addCircle(w / 2, h / 2, r, Path.Direction.CW);
        path.quadTo(r, r *3/2, 100, h - 200);

        mArrowRotateView.setPath(path);

        mArrowRotateView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mArrowRotateView.startAnim();
            }
        }, 2000);
    }
}
