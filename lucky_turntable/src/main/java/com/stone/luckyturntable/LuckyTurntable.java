package com.stone.luckyturntable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 整体是在一个正方形中，绘制一个圆盘
 * 如若要指定旋转到某个范围中，mStartAngle=一个角度范围中的数值即可
 * 对于中奖概率，没有具体编写，思路是：停止后，speed=0时，所在index为有奖品时，匹配随机数，
 *   若匹配，不作任何操作；不匹配，mStartAngle+=360*1.00f/mItemCount; 向前滚动一个item
 *
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/12/21 10 03
 */
public class LuckyTurntable extends BaseSurfaceView {

    private String[] mTitles;
    private int[] mImgRes;
    private int[] mColors;

    private int mItemCount;
    private Bitmap[] mImgsBitmap;

    private RectF mRangeRectF; //内盘块的矩形范围
    private int mDiameter; //直径
    private int mRadius; //半径
    private int mPadding;//只看paddingLeft

    private Paint mArcPaint;
    private Paint mTextPaint;
    private float mTextSize;

    private double mSpeed;
    private volatile float mStartAngle;
    private boolean mIsShouldEnd; //是否点击停止
    private List<Range> mRangeList; //每个item对应的角度范围

    private OnStopRotateListener mOnStopRotateListener;
    private OnWinListener mOnWinListener;

    public LuckyTurntable(Context context) {
        super(context);

        setBackgroundColor(Color.parseColor("#7dabc777"));

        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);

    }

    public void setTiles(String[] titles) {
        this.mTitles = titles;

        mItemCount = mTitles.length;

        mRangeList = new ArrayList<Range>();
        float angle = 360 * 1.00f / mItemCount;
        /*
        指针在270度
        item0, [225,270]
        item1, [180, 225]
         */
        float from;
        float end;
        for (int i = 0; i < mItemCount; i++) {
            from = 270 - (i + 1) * angle;
            end = from + angle;
            if (from < 0) {
                from += 360;
            }
            if (end < 0) {
                end += 360;
            }
            //每个扇形块对应的角度范围
            mRangeList.add(new Range(from, end));
        }
    }

    public void setImgRes(int[] imgRes) {
        this.mImgRes = imgRes;

        mImgsBitmap = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mImgsBitmap[i] = BitmapFactory.decodeStream(getResources().openRawResource(mImgRes[i]));
        }
    }

    public void setItemBackColors(int[] colors) {
        this.mColors = colors;
    }

    public interface OnStopRotateListener {
        void onStopRotate();
    }

    public void setOnStopRotateListener(OnStopRotateListener onStopRotateListener) {
        this.mOnStopRotateListener = onStopRotateListener;
    }

    public interface OnWinListener {
        void onWin(int index);

        void onFail();
    }

    public void setOnWinListener(OnWinListener onWinListener) {
        this.mOnWinListener = onWinListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int edgeLength = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = getPaddingLeft();
        mDiameter = edgeLength - mPadding * 2;
        mRadius = mDiameter / 2;
        setMeasuredDimension(edgeLength, edgeLength);

        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, edgeLength / 20, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(mTextSize);

        mRangeRectF = new RectF(mPadding, mPadding, mDiameter + mPadding, mDiameter + mPadding);
    }

    @Override
    protected void drawer(Canvas canvas) {
        drawBg(canvas);
        drawPiece(canvas);

    }

    private void drawBg(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        mArcPaint.setColor(Color.MAGENTA);
        canvas.drawCircle(getMeasuredHeight() / 2, getMeasuredHeight() / 2, getMeasuredHeight() / 2, mArcPaint);

    }

    private void drawPiece(Canvas canvas) {
        float sweepAngle = 360 * 1.00f / mItemCount;
        float tempAngle;
        for (int i = 0; i < mItemCount; i++) {
            mArcPaint.setColor(mColors[i]);
            tempAngle = mStartAngle + i * sweepAngle;
            canvas.drawArc(mRangeRectF, tempAngle, sweepAngle, true, mArcPaint);
            drawText(canvas, mTitles[i], tempAngle, sweepAngle);
            drawImg(canvas, mImgsBitmap[i], tempAngle);
        }
        if (isStart()) {
            mStartAngle += (float) mSpeed + Math.random() * 10;//改变下轮绘制的起始偏移角度
        }

        if (mIsShouldEnd) {
            mSpeed -= 1;

            if (mSpeed <= 0) {
                mSpeed = 0;

                if (mOnStopRotateListener != null) {
                    mOnStopRotateListener.onStopRotate();
                }
                //比对是否在item的角度范围内
                float actualAngle = mStartAngle % 360;
                Range range;
                boolean win = false;
                for (int i = 0; i < mRangeList.size(); i++) {
                    range = mRangeList.get(i);
//                    System.out.println("start angle: " + mRangeList.get(i).from);
//                    System.out.println("end angle: " + mRangeList.get(i).end);

                    if (actualAngle > range.from && actualAngle < range.end) {//在范围中
                        if (mOnWinListener != null) {
                            mOnWinListener.onWin(i);
                        }
                        win = true;
                        break;
                    }
                }
                if (!win && mOnWinListener != null) { //不在范围中
                    mOnWinListener.onFail();
                }

                mIsShouldEnd = false;
            }
        }

    }

    private void drawText(Canvas canvas, String text, float startAngle, float sweepAngle) {
        Path path = new Path();
        path.addArc(mRangeRectF, startAngle, sweepAngle);
        float textWidth = mTextPaint.measureText(text);
        //弧长=弧度*半径
        float arcw = (float) (mRadius * Math.PI / 180 * sweepAngle); //弧长
        canvas.drawTextOnPath(text, path, (arcw - textWidth) / 2, mTextSize + mPadding, mTextPaint);
    }

    private void drawImg(Canvas canvas, Bitmap bitmap, float mStartAngle) {
        int bitmapWith = mRadius / 4;//定死宽度
        float r = mRadius / 2; //斜边
        float l = (float) (r * Math.cos(Math.PI / 180 * (mStartAngle + 360 / mItemCount / 2))); //半角邻边x
        float t = (float) (r * Math.sin(Math.PI / 180 * (mStartAngle + 360 / mItemCount / 2))); //半角对边y
        l += mRadius + mPadding; //屏幕中的x点，该点应该是图片的中心点
        t += mRadius + mPadding;
        l -= bitmapWith / 2;
        t -= bitmapWith / 2;
        canvas.drawBitmap(bitmap, null, new RectF(l, t, l + bitmapWith, t + bitmapWith), null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public void luckyStart() {
        mIsShouldEnd = false;
        if (mSpeed == 0) {
            mSpeed = 10;
        }
    }

    public void luckyStop() {
        mIsShouldEnd = true;
    }

    public boolean isStart() {
        return mSpeed > 0;
    }

    public boolean isShouldEnd() {
        return mIsShouldEnd;
    }

    private class Range {
        float from;
        float end;

        public Range(float from, float end) {
            this.from = from;
            this.end = end;
        }
    }
}
