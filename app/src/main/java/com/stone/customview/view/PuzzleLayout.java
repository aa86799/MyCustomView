package com.stone.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.stone.customview.PuzzleActivity;
import com.stone.customview.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/6/1 11 06
 */
public class PuzzleLayout extends RelativeLayout {

//    public PuzzleLayout(Context context) {
//        super(context);
//
//        init();
//    }

    private List<MyPoint> mPoints;
    float downx;
    float downy;
    int backw, backh, areaw,areah;
    int min;
    Bitmap back;

    public PuzzleLayout(Context context, String point) {
        super(context);
        init(point);
    }

    private void init(String point) {
        String[] split = point.split(",");

        mPoints = new ArrayList<MyPoint>();
        MyPoint mypoint = null;
        for (int i = 0; i < split.length; i++) {
            if (i % 2 == 0) {
                mypoint = new MyPoint();
                mypoint.x = Float.valueOf(split[i]);
            } else {
                mypoint.y = Float.valueOf(split[i]);
                mPoints.add(mypoint);
            }
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        back = BitmapFactory.decodeResource(getResources(), R.drawable.p0, options);
        //有options后 ，此时返回的bitmap为null
//        backw = back.getWidth();
//        backh = back.getHeight();
        backw = options.outWidth;
        backh = options.outHeight;

    }

    private  static class MyPoint {
        float x, y;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        areaw = getMeasuredWidth();
        areah = getMeasuredHeight();
        min = Math.min(areaw, areah);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        float scalex = Float.valueOf(min) / backw;
        float scaley = Float.valueOf(min) / backh;

        Path path = new Path();
        for (int i = 0; i < mPoints.size(); i++) {
            if (i == 0) {
                path.moveTo((areaw - min) / 2 + mPoints.get(0).x * min, (areah - min) / 2 + mPoints.get(0).y * min);
            } else {
                path.lineTo((areaw - min) / 2 + mPoints.get(i).x * min, (areah - min) / 2 + mPoints.get(i).y * min);
            }
        }
        path.close();
        canvas.drawPath(path, paint);
        canvas.clipPath(path);

        /*
        首先有 外层区的 宽高
        算出每个 多边形 区的 点坐标
        以点坐标 算出其外框  rect

        有了rect，就有坐标  ltrb
        在最外层父类的onlayout{ layout child 每个多边形区的位置}
         */
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        downx = event.getX();
        downy = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getX() - downx);
                int dy = (int) (event.getY() - downy);

                int endx = getChildAt(0).getScrollX() +  dx;
                int endy = getChildAt(0).getScrollY() + dy;
                getChildAt(0).scrollTo(-endx, -endy);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
//        return super.dispatchTouchEvent(event);
    }
}
