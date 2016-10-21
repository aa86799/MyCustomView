package com.stone.turnpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/9/20 10 46
 */

public class MainActivity extends Activity {

    @BindView(R.id.btnHorizon)
    Button mBtnHorizon;

    @BindView(R.id.btnFold)
    Button mBtnFold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnHorizon)
    void lineTurnPage() {
        startActivity(HorizonTurnPageActivity.class);
    }

    @OnClick(R.id.btnFold)
    void foldTurnPage() {
        startActivity(FoldActivity.class);
    }



    private void startActivity(Class <? extends Activity> clz) {
        startActivity(new Intent(this, clz));
    }
}
