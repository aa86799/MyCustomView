package com.stone.turnpage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stone.turnpage.view.HorizonTurnPageView;

import java.util.ArrayList;
import java.util.List;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/7/8 10 43
 */
public class HorizonTurnPageActivity extends AppCompatActivity {

    private HorizonTurnPageView mLineTurnPageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLineTurnPageView = new HorizonTurnPageView(this);

        setContentView(mLineTurnPageView);

        List<Bitmap> bitmaps = new ArrayList<>();
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.jsy2);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.jsy1);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
        bitmaps.add(bm1);
        bitmaps.add(bm2);
        bitmaps.add(bm3);
        mLineTurnPageView.setBitmaps(bitmaps);

    }
}
