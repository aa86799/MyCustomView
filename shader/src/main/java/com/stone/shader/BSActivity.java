package com.stone.shader;

import android.app.Activity;
import android.os.Bundle;

import com.stone.shader.view.BitmapShaderView;

/**
 * desc   :
 * author : stone
 * email  : aa86799@163.com
 * time   : 2016/11/28 16 37
 */
public class BSActivity extends Activity {

    private BitmapShaderView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = new BitmapShaderView(this);
        setContentView(mView);
    }
}
