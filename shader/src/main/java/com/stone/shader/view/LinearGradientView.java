package com.stone.shader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * desc   : LinearGradient实现文本每个字符颜色变化
 * author : stone
 * email  : aa86799@163.com
 * time   : 2016/11/29 17 33
 */
public class LinearGradientView extends View {

    private LinearGradient mShader;
    private String mText = "中华人民共和国";
    private Paint mPaint;
    private int mTextSize = 40;
    private float mStrWidth;
    private Matrix mMatrix;
    private float mTranslateX;
    private int mSingleTextWidth;
    private int mCount = 1;//一次性跑多少个文字的个数
    private float mX, mY;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };

    public LinearGradientView(Context context) {
        this(context, null);
    }

    public LinearGradientView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMatrix = new Matrix();

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);

        mStrWidth = mPaint.measureText(mText);

        mSingleTextWidth = (int) (mCount * mStrWidth / mText.length());//计算一个文字的宽度

        mShader = new LinearGradient(0, 0, mSingleTextWidth, mTextSize,
//        mShader = new LinearGradient(0, 0, mSingleTextWidth, 0,
//        mShader = new LinearGradient(0, 0, -mSingleTextWidth, 0,
                new int[]{0xddff0000, 0xffffffff},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(mText, mX, mY, mPaint);
        mTranslateX += mSingleTextWidth;//每次平移一个文字的宽度
        if (mTranslateX >= mStrWidth + Math.abs(mSingleTextWidth)) {
            mSingleTextWidth = -mSingleTextWidth;
        }
        if (mTranslateX < 0) {
            mSingleTextWidth = -mSingleTextWidth;
        }
        //实现平移一个文字的宽度
        mMatrix.setTranslate(mTranslateX + mX, mY);
        mShader.setLocalMatrix(mMatrix);

        mHandler.sendEmptyMessageDelayed(0, 200);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mX = event.getX();
        mY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mHandler.removeMessages(0);
            mHandler.sendEmptyMessage(0);
        }
        return true;
    }
}
