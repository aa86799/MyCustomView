package com.stone.canvaspath;

import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;

import com.stone.canvaspath.earth.EarthPathView;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/5/29 15 27
 */
public class EarthActivity extends Activity {

    private EarthPathView mPathView;
    private Path mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int w = getResources().getDisplayMetrics().widthPixels;
        int h = getResources().getDisplayMetrics().heightPixels;

        mPathView = new EarthPathView(this);

        setContentView(mPathView);

        int min = Math.min(w, h);
        buildPath(w / 2 + 100, h / 2 + 100, min / 4);

        mPathView.setPath(mPath);

        mPathView.startAnim();
    }

    private void buildPath(float x, float y, float radius) {
        mPath = new Path();
        mPath.addCircle(x, y, radius, Path.Direction.CW);
    }

}
