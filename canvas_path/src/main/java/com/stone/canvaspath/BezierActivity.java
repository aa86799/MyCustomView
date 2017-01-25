package com.stone.canvaspath;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.stone.canvaspath.bezier.CubicCurveView;
import com.stone.canvaspath.bezier.DynamicQuadCurveView;
import com.stone.canvaspath.bezier.QuadCurveView;

public class BezierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = null;

        int type = getIntent().getIntExtra("type", 0);

        switch (type) {
            case 0:
                view = new QuadCurveView(this);
                break;
            case 1:
                view = new DynamicQuadCurveView(this);
                break;
            case 2:
                view = new CubicCurveView(this);
                break;
        }
        if (view != null)
            setContentView(view);
    }
}
