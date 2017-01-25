package com.stone.canvaspath;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.stone.canvaspath.bounce.BounceCircleView;
import com.stone.canvaspath.bounce.MagicCircle;

public class BounceCircleActivity extends AppCompatActivity {

    private BounceCircleView mBounceCircleView;
    private Button mBtn1, mBtn2, mBtn3;
    private boolean isFirst = true;

    private MagicCircle mMagicCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.bounce_acti);

        mBtn1 = (Button) findViewById(R.id.btn01);
        mBtn2 = (Button) findViewById(R.id.btn02);
        mBtn3 = (Button) findViewById(R.id.btn03);

        mBounceCircleView = (BounceCircleView) findViewById(R.id.bcv);
        mBounceCircleView.setSubItemCount(3);

        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAnim(0);
            }
        });

        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAnim(1);
            }
        });

        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAnim(2);
            }
        });

        mMagicCircle = (MagicCircle) findViewById(R.id.mc);
    }

    private void clickAnim(int index) {
        mBounceCircleView.startAnim(index);

        mMagicCircle.startAnimation();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst) {
            isFirst = !isFirst;
            mBounceCircleView.setSubWidthAndHeight(mBtn1.getWidth(), mBtn1.getHeight());
        }
    }
}
