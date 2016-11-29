package com.stone.shader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * desc   :
 * author : stone
 * email  : aa86799@163.com
 * time   : 2016/11/28 16 37
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_acti);

    }

    private void startActi(Class<? extends Activity> clz) {
        startActivity(new Intent(this, clz));
    }

    public void bs(View view) {
        startActi(BSActivity.class);
    }

    public void lg(View view) {
        startActi(LGActivity.class);
    }
}
