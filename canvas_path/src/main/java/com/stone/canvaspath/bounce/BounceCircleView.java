package com.stone.canvaspath.bounce;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;


/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/6/27 18 37
 */
public class BounceCircleView extends View {

    private Path mPath;
    private float mRadius;
    private static final float c = 0.5522847498f;
    private float mControl;
    private Paint mPaint;

    private int mSubW, mSubH;
    private int mSubCount;
    private int mSelectedIndex;
    private int mToIndex;
    private float mTranslateX, mTranslateY;
    private int mHspan = 120;
    private Scroller mScroller;
    private boolean mIsStartAnim;
    private float mMaxDrag;
    private float mCurDrag;
    private Direction mCurDirection;
    private boolean mIsStartLastHalf;
    private float mPercent;

    private enum Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT;
    }

    public BounceCircleView(Context context) {
        this(context, null);
    }

    public BounceCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new Scroller(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mSubCount == 0) {
            return;
        }

        mRadius = Math.min(mSubW, mSubH) / 2;
        /*
        要平移的半径点
         */
        mTranslateX = mSelectedIndex * mRadius * 2 + mRadius + (mSelectedIndex + 1) * mHspan;
        mTranslateY =  100 + mRadius;

        if (!mIsStartAnim) {
            canvas.translate(mTranslateX, mTranslateY);
        } else {
            int space = Math.abs(mToIndex - mSelectedIndex);
            float max = mRadius * space * 2 + space * mHspan - mRadius;
            if (mCurDirection == Direction.LEFT_TO_RIGHT) {
                canvas.translate(mTranslateX + max * mPercent, mTranslateY);
            } else {
                canvas.translate(mTranslateX + max * mPercent, mTranslateY);
            }

        }

        mControl = c * mRadius;

        mPath = new Path();
        if (!mIsStartAnim) {
            mPath.moveTo(mRadius, 0);
            mPath.cubicTo(mRadius, mControl, mControl, mRadius, 0, mRadius); //右下
            mPath.cubicTo(-mControl, mRadius, -mRadius, mControl, -mRadius, 0); //左下
            mPath.cubicTo(-mRadius, -mControl, -mControl, -mRadius, 0, -mRadius); //左上
            mPath.cubicTo(mControl, -mRadius, mRadius, -mControl, mRadius, 0); //右上
        } else {
            mCurDrag /= 3;
            switch (mCurDirection) {
                case LEFT_TO_RIGHT: {
                    mPath.moveTo(mRadius+mCurDrag, 0);
                    if (!mIsStartLastHalf) {
                        mPath.cubicTo(mRadius+mCurDrag, mControl, mControl, mRadius, 0, mRadius); //右下
                        mPath.cubicTo(-mControl, mRadius, -mRadius, mControl, -mRadius, 0); //左下
                        mPath.cubicTo(-mRadius, -mControl, -mControl, -mRadius, 0, -mRadius); //左上
                        mPath.cubicTo(mControl, -mRadius, mRadius+mCurDrag, -mControl, mRadius+mCurDrag, 0); //右上

                    } else {
                        mPath.cubicTo(mRadius+mCurDrag, mControl, mControl, mRadius-mCurDrag, 0, mRadius); //右下
                        mPath.cubicTo(-mControl, mRadius+20, -mRadius-mCurDrag, mControl, -mRadius-mCurDrag, 0); //左下
                        mPath.cubicTo(-mRadius-mCurDrag, -mControl, -mControl, -mRadius-20, 0, -mRadius); //左上
                        mPath.cubicTo(mControl, -mRadius-20, mRadius+mCurDrag, -mControl, mRadius+mCurDrag, 0); //右上
                    }


                }
                    break;

                case RIGHT_TO_LEFT: {
                    mPath.moveTo(mRadius, 0);
                    mPath.cubicTo(mRadius, mControl, mControl, mRadius, 0, mRadius); //右下
                    mPath.cubicTo(-mControl, mRadius, -mRadius+mCurDrag, mControl, -mRadius+mCurDrag, 0); //左下
                    mPath.cubicTo(-mRadius+mCurDrag, -mControl, -mControl, -mRadius, 0, -mRadius); //左上
                    mPath.cubicTo(mControl, -mRadius, mRadius, -mControl, mRadius, 0); //右上
                }
                    break;
            }
        }

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.STROKE);

        canvas.drawPath(mPath, mPaint);



      /*  mRadius = Math.min(getWidth(), getHeight()) / 2;
        canvas.translate(mRadius, mRadius);
        mRadius /= 8;
        mControl = c * mRadius;

        mPath = new Path();
        mPath.moveTo(mRadius, 0);
        mPath.cubicTo(mRadius, mControl, mControl, mRadius, 0, mRadius); //右下
        mPath.cubicTo(-mControl, mRadius, -mRadius, mControl, -mRadius, 0); //左下
        mPath.cubicTo(-mRadius, -mControl, -mControl, -mRadius, 0, -mRadius); //左上
        mPath.cubicTo(mControl, -mRadius, mRadius, -mControl, mRadius, 0); //右上

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.STROKE);

        canvas.drawPath(mPath, mPaint);*/

    }

    public void setSubItemCount(int i) {
        mSubCount = i;
    }

    public void setSubWidthAndHeight(int width, int height) {
        this.mSubW = width;
        this.mSubH = height;
        invalidate();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void startAnim(int index) {
        if (mIsStartAnim || index == mSelectedIndex) return;

        this.mToIndex = index;

        int space = Math.abs(mToIndex - mSelectedIndex);
        mMaxDrag = mRadius * space * 2 + space * mHspan - mRadius;
        mMaxDrag /= 2;

        ValueAnimator anim = ValueAnimator.ofFloat(0, mMaxDrag);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                if (mSelectedIndex > mToIndex) {//such as : from 3 to 2
                    mCurDirection = Direction.RIGHT_TO_LEFT;
                    mCurDrag = -1;
                    mPercent = mCurDrag * fraction;
                } else {
                    mCurDirection = Direction.LEFT_TO_RIGHT;
                    mCurDrag = 1;
                    mPercent = mCurDrag * fraction;
                }
//                invalidate();


                mCurDrag *= (float) animation.getAnimatedValue() * 2;
                if (mCurDrag >= mMaxDrag) {
                    mCurDrag = mMaxDrag;
                } else if (mCurDrag <= -mMaxDrag) {
                    mCurDrag = -mMaxDrag;
                }

                if (fraction > 0.5f) {
                    mIsStartLastHalf = true;
                }

                invalidate();
            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsStartAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsStartAnim = false;
                mSelectedIndex = mToIndex;
                mIsStartLastHalf = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        anim.setDuration(2000);
        anim.start();
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();

    }
}
