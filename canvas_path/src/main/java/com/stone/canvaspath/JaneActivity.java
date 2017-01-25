package com.stone.canvaspath;

import android.app.Activity;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;

import com.stone.canvaspath.jane.JaneView;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/6/3 16 44
 */
public class JaneActivity extends Activity {

    private Path mPath;
    private JaneView mJaneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mJaneView = new JaneView(this);
        setContentView(mJaneView);

        buildPath();

        mJaneView.setPath(mPath);

//        mJaneView.setOutRectWH(800, 800);
        mJaneView.setOutRectWH(500, 500);

//        mJaneView.setStartPoint(100, 400);
        mJaneView.setStartPoint(100, 100);

        mJaneView.setBitmapRes(R.drawable.mn);

    }

    private void buildPath() {
        mPath = new Path();
//        mPath.addCircle(500, 800, 400, Path.Direction.CCW);
//        mPath.addArc(new RectF(100, 400, 900, 1200), 0, 360);
        mPath.addArc(new RectF(100, 100, 600, 600), 0, 360);
    }

}
