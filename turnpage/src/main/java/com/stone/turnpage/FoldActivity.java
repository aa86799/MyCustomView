package com.stone.turnpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stone.turnpage.view.FoldTurnPageView;

public class FoldActivity extends AppCompatActivity {


    private FoldTurnPageView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = new FoldTurnPageView(this);
        setContentView(mView);
    }
}
