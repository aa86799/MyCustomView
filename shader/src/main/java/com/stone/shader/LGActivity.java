package com.stone.shader;

import android.app.Activity;
import android.os.Bundle;

import com.stone.shader.view.BitmapShaderView;
import com.stone.shader.view.LinearGradientView;

/**
 * desc   :
 * author : stone
 * email  : aa86799@163.com
 * time   : 2016/11/28 16 37
 */
public class LGActivity extends Activity {

    private LinearGradientView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = new LinearGradientView(this);
        setContentView(mView);
    }
}
