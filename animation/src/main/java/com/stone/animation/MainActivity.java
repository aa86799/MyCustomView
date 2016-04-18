package com.stone.animation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.stone.animation.view.RectTransView;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/4/18 10 17
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout layout = new RelativeLayout(this);
        setContentView(layout);

        RectTransView anim = new RectTransView(this);
        anim.setBitmapResid(R.mipmap.ic_launcher_android);

        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                widthPixels/2, heightPixels/2);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        layout.addView(anim, params);


    }
}
