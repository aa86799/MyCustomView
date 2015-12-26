package com.stone.ninegridlock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * 九宫格锁
 * 关于画外圆，可以先直接算出圆心点和半径，以绘制。本例先算的是圆的外矩形
 * 连线时如果感觉突兀，可记录内圆心点，以内圆心作碰撞检测
 * 可以完善的地方：保存点的顺序，onmeasure中，判断是否匹配，用于解锁方案
 *
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/12/25 22 55
 */
public class NineGridLockView extends View {

    private int mCount = 3; //n行n列
    private ArrayList<RectF> mListRectFs; //圆的外矩形
    private ArrayList<Point> mListCircle; //外圆心点
    private LinkedHashSet<Integer> mSetPoints; //记录需要连线的外圆心点在mListCircle中的索引值。LinkedHashSet线性不可重复集合，FIFO
    private Paint mPaint;
    private float mRadius; //外圆半径
    private float mMinRadius = 20; //内圆半径
    private float mStrokeWidth = 10; //绘制时的画笔宽度
    private Point mMovePoint; //记录一个手势移动时的  实时点


    public NineGridLockView(Context context) {
        this(context, null);
    }

    public NineGridLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(mStrokeWidth);

        mListRectFs = new ArrayList<RectF>();
        mListCircle = new ArrayList<Point>();
        mSetPoints = new LinkedHashSet<Integer>();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float w = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = getMeasuredWidth() * 1.00f / (mCount * 3 + 1);

        float rectWH = mRadius * 2;
        RectF rectF;
        for (int i = 0; i < mCount; i++) {
            for (int j = 0; j < mCount; j++) {
                rectF = new RectF(mRadius + 3 * j * mRadius, mRadius + 3 * i * mRadius,
                        mRadius + 3 * j * mRadius + rectWH, mRadius + 3 * i * mRadius + rectWH);
                mListRectFs.add(rectF);
                //item外框
//                canvas.drawRect(rectF, mPaint);
//                canvas.drawArc(rectF, 0, 360, true, mPaint);
                mListCircle.add(new Point((int) (rectF.left + mRadius), (int) (rectF.top + mRadius)));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mListCircle.size(); i++) {
            Point p = mListCircle.get(i);
            mPaint.setColor(Color.GRAY);
            //外圆
            canvas.drawCircle(p.x, p.y, mRadius, mPaint);

            //小圆点
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(p.x, p.y, mMinRadius, mPaint);

            //重置绘边样式
            mPaint.setStyle(Paint.Style.STROKE);
        }

        //重新绘制 需要连线的外圆  变色
        mPaint.setColor(Color.RED);
        for (int index : mSetPoints) {
            Point p = mListCircle.get(index);
            canvas.drawCircle(p.x, p.y, mRadius, mPaint);
        }

        //绘制连线
        mPaint.setColor(Color.BLACK);
        Point p1 = null, p2 = null;
        for (int index : mSetPoints) {
            if (p1 == null) {
                p1 = mListCircle.get(index);
                continue;
            }
            p2 = mListCircle.get(index);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mPaint);
            p1 = p2;
        }

        //绘制实时连线
        if (mMovePoint != null && p1 != null) {
            canvas.drawLine(p1.x, p1.y, mMovePoint.x, mMovePoint.y, mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:

                int index = touchIndex(x, y);
                if (index != -1) {
                    mSetPoints.add(index);
                }
                mMovePoint = new Point((int) event.getX(), (int) event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mMovePoint = null;
                invalidate();
                break;

        }
        return true;
    }

    /**
     * 判断触摸点在哪个item上
     * @param x
     * @param y
     * @return
     */
    private int touchIndex(float x, float y) {
        for (int i = 0; i < mListCircle.size(); i++) {
            Point p = mListCircle.get(i);
            if (isCollision(x, y, p.x, p.y, mRadius)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 点和圆形碰撞检测
     *
     * @param x1     点
     * @param y1     点
     * @param x2     圆
     * @param y2     圆
     * @param radius 半径
     * @return
     */
    private boolean isCollision(float x1, float y1, float x2, float y2, float radius) {
        if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= radius) {
            // 如果点和圆心距离小于或等于半径则认为发生碰撞
            return true;
        }
        return false;
    }

}
