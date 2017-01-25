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
public class CubicCurveView extends View {

    private Path mPath;
    private PointF mStartPoint, mEndPoint;
    private float mTouchX, mTouchY;
    private float mDefX, mDefY;
    private Paint mPaint;

    public CubicCurveView(Context context) {
        this(context, null);
    }

    public CubicCurveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CubicCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mStartPoint = new PointF(200, 500);
        mEndPoint = new PointF(900, 500);

        mDefX = 600;
        mDefY = 300;
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
        mPath.cubicTo(mTouchX, mTouchY, mDefX, mDefY, mEndPoint.x, mEndPoint.y);

        canvas.drawPath(mPath, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mTouchX, mTouchY, 30, mPaint);
        canvas.drawCircle(mDefX, mDefY, 30, mPaint);

        //第二步 使第二控制点为终点
        mPath = new Path();
        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.quadTo(mTouchX, mTouchY, mDefX, mDefY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        canvas.drawPath(mPath, mPaint);

        //第三步 以touch点为起点
        mPath = new Path();
        mPath.moveTo(mTouchX, mTouchY);
        mPath.quadTo(mDefX, mDefY, mEndPoint.x, mEndPoint.y);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(mPath, mPaint);

    }

    public float cc (float p0, float p1, float p2, float t) {

        float p3 = (1 - t) * p0 + p1 * t;
        float p4 = (1 - t) * p1 + p2 * t;
        float p5 = (1 - t) * p3 + p4 * t;
        return 0;
    }
}
