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

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/6/22 11 01
 */
public class QuadCurveView extends View {

    private Path mPath;
    private PointF mStartPoint, mEndPoint;
    private float mTouchX, mTouchY;
    private Paint mPaint;

    public QuadCurveView(Context context) {
        this(context, null);
    }

    public QuadCurveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuadCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mStartPoint = new PointF(200, 500);
        mEndPoint = new PointF(600, 500);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mTouchX = event.getX();
                mTouchY = event.getY();
                invalidate();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);

        mPath = new Path();
        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.quadTo(mTouchX, mTouchY, mEndPoint.x, mEndPoint.y);

        canvas.drawPath(mPath, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mTouchX, mTouchY, 10, mPaint);
    }
}
