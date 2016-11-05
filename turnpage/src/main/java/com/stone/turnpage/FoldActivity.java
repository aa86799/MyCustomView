package com.stone.turnpage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.stone.turnpage.view.FoldTurnPageView;
import com.stone.turnpage.view.MyTestView;

import java.util.ArrayList;
import java.util.List;

public class FoldActivity extends AppCompatActivity {


    private FoldTurnPageView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        mView = new FoldTurnPageView(this);
        setContentView(mView);

        List<Bitmap> bitmaps = new ArrayList<>();
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.jsy2);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.jsy1);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
        bitmaps.add(bm1);
        bitmaps.add(bm2);
        bitmaps.add(bm3);
        mView.setBitmaps(bitmaps);

//        MyTestView view = new MyTestView(this);
//        setContentView(view);

    }
}
