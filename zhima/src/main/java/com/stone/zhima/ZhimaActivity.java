package com.stone.zhima;

import android.app.Activity;
import android.os.Bundle;

import com.stone.zhima.view.ZhimaView;

/**
 * desc   :
 * author : Shi Zongyin
 * email  : shizongyin@znds.com
 */

public class ZhimaActivity extends Activity {

    private ZhimaView mZhimaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mZhimaView = new ZhimaView(this);
        setContentView(mZhimaView);
    }
}
