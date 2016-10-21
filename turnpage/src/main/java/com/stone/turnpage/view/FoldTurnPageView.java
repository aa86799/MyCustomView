package com.stone.turnpage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/9/20 11 18
 *
 * 折线翻页
 */

public class FoldTurnPageView extends View {

    private float mTouchX, mTouchY;
    private Path mPath;
    private Paint mPaint;
    private int mW, mH;
    private Region mRegionShortSize;
    private int mBuffArea = 20;

    public FoldTurnPageView(Context context) {
        this(context, null);
    }

    public FoldTurnPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldTurnPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        mRegionShortSize = new Region();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FoldTurnPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTouchX = event.getX();
        mTouchY = event.getY();
        System.out.println(mTouchY);
        if (!mRegionShortSize.contains((int)mTouchX, (int)mTouchY)) {
            /*
             如果不在则通过x坐标强行重算y坐标
             通过圆的标准方程: (x-a)^2+(y-b)^2=r^2 (a,b)为圆心 r为半径  x,y为圆弧上的一点
             y - b = Math.sqrt(r^2 - (x-a)^2)  => y = Math.sqrt(r^2 - (x-a)^2) + b
             或
             -(y - b) = Math.sqrt(r^2 - (x-a)^2) => y = -1 * Math.sqrt(r^2 - (x-a)^2) + b
              */
//            mTouchY = (float) (Math.sqrt((Math.pow(mW, 2) - Math.pow(mTouchX, 2))) + mH); // 使用这个明显值偏大 比mH大
            mTouchY = (float) (-1 * Math.sqrt((Math.pow(mW, 2) - Math.pow(mTouchX, 2))) + mH);

        }

        /*
        缓冲区域判断
        mTouchY 越接近mH  折线出的就不能形成∆AOB了 而是一个矩形
         */
        float area = mH - mBuffArea;
        if (mTouchY >= area) {
            mTouchY = area;
        }

        invalidate();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = getMeasuredWidth();
        mH = getMeasuredHeight();

        computeShortSizeRegion();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();


//        canvas.clipRegion(mRegionShortSize);

        canvas.drawColor(Color.parseColor("#d8ccaa00"));

        float k = mW - mTouchX;
        float l = mH - mTouchY;
        float c = (float) (Math.pow(k, 2) + Math.pow(l, 2));
        float x = c / (2 * k);
        float y = c / (2 * l);

        mPath.moveTo(mTouchX, mTouchY); //O点
        mPath.lineTo(mW - x, mH); //A点
        mPath.lineTo(mW, mH - y);   //B点
        mPath.close();

        mPaint.setColor(Color.RED);
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(Color.GREEN);
        mPath.reset();
        mPath.addCircle(0, mH, mW, Path.Direction.CCW);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 计算短边的有效区域
     */
    private void computeShortSizeRegion() {
        // 短边圆形路径对象
        Path pathShortSize = new Path();
        // 添加圆形到Path
        pathShortSize.addCircle(0, mH, mW, Path.Direction.CCW);
        RectF bounds = new RectF();
        pathShortSize.computeBounds(bounds, true);
        //region.setPath   参数Region clip,   用于裁剪
        boolean flag = mRegionShortSize.setPath(pathShortSize, new Region((int)bounds.left, (int)bounds.top,
                (int)bounds.right, (int)bounds.bottom));
//        boolean flag = mRegionShortSize.setPath(pathShortSize, new Region(0, 1920-1080, 500, 1920));
        System.out.println(bounds + ",," + flag);
        System.out.println(mRegionShortSize);

    }


}
