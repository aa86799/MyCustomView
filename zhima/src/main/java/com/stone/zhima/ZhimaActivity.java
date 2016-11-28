package com.stone.zhima;

import android.app.Activity;
import android.os.Bundle;

import com.stone.zhima.view.ZhimaView;

/**
 * 仿芝麻信用分析的 正多边形绘制
 * author : stone
 * email  : aa86799@163.com
 * time   : 2016/11/17 14 10
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
