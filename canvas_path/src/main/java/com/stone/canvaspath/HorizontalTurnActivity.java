package com.stone.canvaspath;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import com.stone.canvaspath.horiturn.PageTurnEasyView;

public class HorizontalTurnActivity extends AppCompatActivity {

    private PageTurnEasyView pageTurnEasyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pageTurnEasyView = new PageTurnEasyView(this);

        setContentView(pageTurnEasyView);


        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

//        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.jsy2);
//        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.jsy1);
//        Bitmap foreImage = Bitmap.createScaledBitmap(bm1, width, height,false);
//        Bitmap bgImage = Bitmap.createScaledBitmap(bm2, width, height,false);
//        pageTurnEasyView.setBgImage(bgImage);
//        pageTurnEasyView.setForeImage(foreImage);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        DisplayMetrics display = getResources().getDisplayMetrics();
        int width = display.widthPixels;
        int height = display.heightPixels;

//        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();  //获取window中程序实际所在view区域 不含标题栏、状态栏
        View content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        height = content.getBottom() - content.getTop();

        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.jsy2);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.jsy1);
        Bitmap foreImage = Bitmap.createScaledBitmap(bm1, width, height,false);
        Bitmap bgImage = Bitmap.createScaledBitmap(bm2, width, height,false);
        pageTurnEasyView.setBgImage(bgImage);
        pageTurnEasyView.setForeImage(foreImage);
    }
}
