package com.stone.zhima.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * 仿芝麻信用分析的 正多边形绘制
 * author : stone
 * email  : aa86799@163.com
 * time   : 2016/11/17 14 12
 */

public class ZhimaView extends View {
    private Paint mPaint;
    private float mR, mCx, mCy;
    private static final int mN = 10;
    private static final float DEGREES_UNIT = 360 / mN; //正N边形每个角  360/mN能整除
    private static float mRotateDegrees = 30;

    public ZhimaView(Context context) {
        this(context, null);
    }

    public ZhimaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZhimaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float mW = getMeasuredWidth();
        float mH = getMeasuredHeight();

        mCx = mW / 2;
        mCy = mH / 2;
        mR = Math.min(mCx, mCy) / 4 * 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);

        /*
        方法一：利用两点间距离公式得出两个方程，然后消元求解，先解出y值，再解出x值
        这里仅仅绘制出正多边形
        缺陷：> 要求夹角度数 必须整除360
             > 后期绘制文本、图片 难以实现
         */

      /*  float d = (float) (2 * mR * Math.sin(Math.toRadians(DEGREES_UNIT / 2)));
        float c = mCy - mR;
        float y = (d * d + mCy * mCy - c * c - mR * mR) / (2 * (mCy - c));
        float x = (float) (mCx + Math.sqrt(-1 * c * c + 2 * c * y + d * d - y * y));

        canvas.rotate((float) (Math.random()*360), mCx, mCy);
        for (int i = 0; i < mN; i++) {
            canvas.save();
            canvas.rotate(DEGREES_UNIT * i, mCx, mCy);
            canvas.drawLine(mCx, mCy, mCx, c, mPaint);
            canvas.drawLine(mCx, c, x, y, mPaint);
            canvas.restore();
        }*/

        /*
         方法二：利用三角函数
         */

        canvas.save();
        canvas.rotate(mRotateDegrees, mCx, mCy);
        float bx, by;
        ArrayList<PointF> points = new ArrayList<>();
        for (int i = 0; i < mN; i++) {
            bx = mCx + (float) (mR * Math.sin((90 - DEGREES_UNIT * i) * Math.PI / 180));
            by = mCy + (float) (mR * Math.cos((90 - DEGREES_UNIT * i) * Math.PI / 180));
            canvas.drawLine(mCx, mCy, bx, by, mPaint);
            points.add(new PointF(bx, by));
        }

        PointF p, p1;
        for (int i = 0; i < points.size() - 1; i++) {
            p = points.get(i);
            p1 = points.get(i + 1);
            canvas.drawLine(p.x, p.y, p1.x, p1.y, mPaint);
        }
        p = points.get(0);
        p1 = points.get(points.size() - 1);
        canvas.drawLine(p.x, p.y, p1.x, p1.y, mPaint);
        canvas.restore();

        /*
        如果要在圆上各顶点外绘制文字图片 只需要增大半径

        假设对图片上任意点(x,y)，绕一个坐标点(cx,cy)逆时针旋转a角度后的新的坐标设为(x0, y0)，有公式
         x0 = (x - cx)*cos(a) - (y - cy)*sin(a) + cx;
         y0 = (x - cx)*sin(a) + (y - cy)*cos(a) + cy;
         */
        points.clear();
        float r = mR + mR / 8;
        for (int i = 0; i < mN; i++) {
            bx = mCx + (float) (r * Math.sin((90 - DEGREES_UNIT * i) * Math.PI / 180));
            by = mCy + (float) (r * Math.cos((90 - DEGREES_UNIT * i) * Math.PI / 180));
            points.add(new PointF(bx, by));
        }

        mPaint.setTextSize(mR / 15);

        float rx, ry;
        for (int i = 0; i < mN; i++) {
            p = points.get(i);
            rx = (float) (mCx + (p.x - mCx) * Math.cos(mRotateDegrees * Math.PI / 180) - (p.y - mCy)
                    * Math.sin(mRotateDegrees * Math.PI / 180));
            ry = (float) (mCy + (p.x - mCx) * Math.sin(mRotateDegrees * Math.PI / 180) + (p.y - mCy)
                    * Math.cos(mRotateDegrees * Math.PI / 180));

            String text = "item-" + i;
            float tw = mPaint.measureText(text);
            float tx = rx - tw / 2;
            System.out.println(mPaint.getFontMetricsInt().top);
            System.out.println(mPaint.getFontMetricsInt().bottom);
            float ty = ry + mPaint.getFontMetricsInt().bottom;

            canvas.drawText(text, tx, ty, mPaint);


        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mRotateDegrees = (float) (Math.random() * 90);
        invalidate();
        return true;
    }
}
