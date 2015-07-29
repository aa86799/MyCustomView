package com.stone.guaguaka.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.stone.guaguaka.R;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/7/27 16 16
 */
public class GuaGuaKaView extends View {

    private Paint mOuterPaint;
    private Path mPath;
    private Canvas mCanvas; //关联bitmap的canvas
    private Bitmap mBitmap;
    private float mLastX;
    private float mLastY;
    //    private PorterDuffXfermode mPorterDuffXfermode;
    private Bitmap mCoverBitmap; //遮盖图片
    private Paint mInnerPaint;

    private AtomicBoolean mComplete;
    private String mInfo;
    private Rect mTextRect;

    private onGuaGuaKaCompletedListener mOnGuaGuaKaCompletedListener;

    public interface onGuaGuaKaCompletedListener {
        void complete(String message);
    }

    public void setOnGuaGuaKaCompletedListener(onGuaGuaKaCompletedListener mOnGuaGuaKaCompletedListener) {
        this.mOnGuaGuaKaCompletedListener = mOnGuaGuaKaCompletedListener;
    }

    public GuaGuaKaView(Context context) {
        this(context, null);
    }

    public GuaGuaKaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaKaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mOuterPaint = new Paint();
        mPath = new Path();
        mCoverBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.fg_guaguaka)).getBitmap();
        mInnerPaint = new Paint();

        mInfo = "￥ 5 0 0";
        mComplete = new AtomicBoolean(false);
        mTextRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
//        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
//        mCanvas.drawBitmap(mCoverBitmap, 0, 0, null);
        mCanvas.drawColor(Color.parseColor("#abc777"));//图片中有空白像素的地方，先在位图上绘制一个底色
        mCanvas.drawBitmap(mCoverBitmap, null, new Rect(0, 0, w, h), null); //再贴上遮盖的图片

        setOuterPaint();
        setInnerPaint();

    }

    private void setInnerPaint() {
        mInnerPaint.setColor(Color.MAGENTA);
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setDither(true); //防抖动
        /*
        Paint.Join 连续画笔衔接时
        MITER 在外边缘以一个锐角连接
        ROUND 以圆弧
        BEVEL 以直线
         */
        mInnerPaint.setStrokeJoin(Paint.Join.MITER);
        /*
        Paint.Cap 指定对于 线和路径(lines and paths) 的开始和结束点的处理方式
        BUTT  ends with the path  不超越它
        ROUND  with the center at the end of the path 半圆
        SQUARE  with the center at the end of the path 方形
         */
        mInnerPaint.setStrokeCap(Paint.Cap.SQUARE);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeWidth(5);
        mInnerPaint.setTextAlign(Paint.Align.CENTER);
        mInnerPaint.setTextSize(150);
        mInnerPaint.getTextBounds(mInfo, 0, mInfo.length(), mTextRect); //计算文本 宽高 放入rect


    }

    private void setOuterPaint() {
        mOuterPaint.setColor(Color.RED);
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setDither(true); //防抖动
        /*
        Paint.Join 连续画笔衔接时
        MITER 在外边缘以一个锐角连接
        ROUND 以圆弧
        BEVEL 以直线
         */
        mOuterPaint.setStrokeJoin(Paint.Join.ROUND);
        /*
        Paint.Cap 指定对于 线和路径(lines and paths) 的开始和结束点的处理方式
        BUTT  ends with the path  不超越它
        ROUND  with the center at the end of the path 半圆
        SQUARE  with the center at the end of the path 方形
         */
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeWidth(30);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mComplete.get()) {
            return true;
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(mLastX - x);
                float dy = Math.abs(mLastY - y);
                if (dx >= 5 || dy >= 5) { //大于5像素才
                    mPath.lineTo(x, y);
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (!mComplete.get()) {
                    new Thread(mCalcComplete).start();
                }
                break;
        }
        if (!mComplete.get()) {
            invalidate(); //每一个action都刷新
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor("#cccccc")); //一个底灰色

//        float textWidth = mInnerPaint.measureText(mInfo);
//        canvas.drawText(mInfo, (mCoverBitmap.getWidth() - textWidth) / 2, mCoverBitmap.getHeight() / 4 * 3, mInnerPaint);

        canvas.drawText(mInfo, getWidth() / 2, (getHeight() + mTextRect.height()) / 2, mInnerPaint);

        drawPath();
        if (!mComplete.get()) {
            canvas.drawBitmap(mBitmap, 0, 0, null); //View的canvas，绘制bitmap
        } else {
            if (mOnGuaGuaKaCompletedListener != null) {
                mOnGuaGuaKaCompletedListener.complete(mInfo);
            }
        }
    }


    private void drawPath() {
        mOuterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOuterPaint); //在mBitmap绘制path
    }

    private Runnable mCalcComplete = new Runnable() {
        @Override
        public void run() {
            int w = mBitmap.getWidth();
            int h = mBitmap.getHeight();
            int[] pixels = new int[w * h];
            mBitmap.getPixels(pixels, 0, w, 0, 0, w, h);
            int wipe = 0;
            int total = w*h;
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int k = i+j*w;
                    if (pixels[k] == 0) {
                        wipe++;
                    }
                }
            }
            int percent = wipe*100/total;
            if (percent > 55) {
                mComplete.set(true);
                postInvalidate();
            }
        }
    };
}
