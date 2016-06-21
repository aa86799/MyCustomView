package com.stone.taichi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/6/21 13 42
 */
public class TaichiView extends View {

    private Paint mPaint;
    private int mDegrees;

    public TaichiView(Context context) {
        this(context, null);
    }

    public TaichiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaichiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = Math.min(getWidth(), getHeight());
        canvas.rotate(mDegrees, w / 2, w / 2);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

        canvas.drawCircle(w / 2, w / 2, w / 2 + 5, mPaint);

        canvas.drawArc(new RectF(0, 0, w, w), 90, 180, true, mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawArc(new RectF(0, 0, w, w), 270, 180, true, mPaint);

        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(w / 2, w / 4, w / 4, mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(w / 2, w / 4 * 3, w / 4, mPaint);

        canvas.drawCircle(w / 2, w / 4, w / 16, mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(w / 2, w / 4 * 3, w / 16, mPaint);

    }

    public void setDegrees(int degrees) {
        this.mDegrees = degrees;
        invalidate();
    }


}
