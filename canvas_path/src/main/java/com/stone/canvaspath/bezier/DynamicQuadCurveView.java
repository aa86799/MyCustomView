package com.stone.canvaspath.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/6/22 11 01
 */
public class DynamicQuadCurveView extends View {

    private Path mPath;
    private PointF mStartPoint, mEndPoint;
    private float mTouchX, mTouchY;
    private Paint mPaint;
    private float mT;
    private ArrayList<PointF> mPointFs;

    public DynamicQuadCurveView(Context context) {
        this(context, null);
    }

    public DynamicQuadCurveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicQuadCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mStartPoint = new PointF(200, 500);
        mEndPoint = new PointF(600, 500);

        mPointFs = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mTouchX = event.getX();
                mTouchY = event.getY();

                mT = 0;
                mPointFs.clear();
                invalidate();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mT >= 1.0f) {
            mT = 1.0f;
        }

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        mPaint.setColor(Color.GRAY);
        canvas.drawLine(mStartPoint.x, mStartPoint.y, mTouchX, mTouchY, mPaint);
        canvas.drawLine(mTouchX, mTouchY, mEndPoint.x, mEndPoint.y, mPaint);

        //完整曲线
//        mPaint.setColor(Color.RED);
//        mPath = new Path();
//        mPath.moveTo(mStartPoint.x, mStartPoint.y);
//        mPath.quadTo(mTouchX, mTouchY, mEndPoint.x, mEndPoint.y);
//        canvas.drawPath(mPath, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mTouchX, mTouchY, 10, mPaint);

        /*
        线性公式：f(t)=p0+(p1-p0)*t
                =p0+p1t-p0t
                =(1-t)p0+p1t
         */
        float p3x = (1 - mT) * mStartPoint.x + mT * mTouchX;
        float p3y = (1 - mT) * mStartPoint.y + mT * mTouchY;
        float p4x = (1 - mT) * mTouchX + mT * mEndPoint.x;
        float p4y = (1 - mT) * mTouchY + mT * mEndPoint.y;
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(p3x, p3y, p4x, p4y, mPaint);

        float p5x = (1 - mT) * p3x + mT * p4x;
        float p5y = (1 - mT) * p3y + mT * p4y;
        mPointFs.add(new PointF(p5x, p5y));
        mPaint.setColor(Color.RED);
        PointF ps, pe;
        for (int i = 1; i < mPointFs.size(); i++) {
            ps = mPointFs.get(i - 1);
            pe = mPointFs.get(i);
            canvas.drawLine(ps.x, ps.y, pe.x, pe.y, mPaint);
        }

        if (mT == 1.0f) {
            mPath = new Path();
            mPath.moveTo(mStartPoint.x, mStartPoint.y);
            mPath.quadTo(mTouchX, mTouchY, mEndPoint.x, mEndPoint.y);
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(5);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(mPath, mPaint);
        }

        if (mT < 1.0f) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mT = mT + 0.02f;
                    System.out.println(mT);
                    invalidate();
                }
            }, 50);

        }
    }
}
