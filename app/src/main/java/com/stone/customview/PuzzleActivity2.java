package com.stone.customview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.stone.customview.R;
import com.stone.customview.util.PointUtil;
import com.stone.customview.view.BackView;
import com.stone.customview.view.MyImageLayout;
import com.stone.customview.view.MyImageLayout2;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/5/28 09 55
 */
public class PuzzleActivity2 extends Activity {

    List<ImageView> imgViewList;
    BackView backView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.puzzle);

        int sw = getResources().getDisplayMetrics().widthPixels;
        int sh = getResources().getDisplayMetrics().heightPixels;

        backView = new BackView(this);
        setContentView(backView);

        imgViewList = new ArrayList<ImageView>();

        PointUtil instance = PointUtil.getInstance(this);
        List<List<PointUtil.MyPoint>> pointsScale = instance.getPointsScale();
        MyImageLayout2 imageLayout = null;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
        final Random random = new Random();
        for (int i = 0; i < pointsScale.size(); i++) {
            if (i % 3 == 0) {
                imageLayout = new MyImageLayout2(this, getImageByReflect("a1"));
            } else if (i % 3 == 1) {
                imageLayout = new MyImageLayout2(this, getImageByReflect("a2"));
            } else {
                imageLayout = new MyImageLayout2(this, getImageByReflect("a3"));
            }
            backView.addView(imageLayout);

        }
    }

    private int getImageByReflect(String imageName) {
        try {
            Field field = Class.forName(getPackageName()+".R$drawable").getField(imageName);
            return field.getInt(field);
        } catch (Exception e) {

        }
        return 0;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        for (Bitmap bitmap : bitmaps) {
//            bitmap.recycle();
//            bitmap = null;
//        }
//        bitmaps.clear();
//        bitmaps = null;

    }

    //提示
    public static int[] images = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4,
            R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9};

}
