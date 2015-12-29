package com.stone.bitmapandanimator.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

import com.stone.bitmapandanimator.R;

/**
 * canvas.drawBitmap(bitmap, srcRect, dstRect, paint);
 * 改变srcRect的ltrb，即改变显示的原图内容区；为null时，表示整个原图作为内容
 * 不断改变dstRect的ltrb，可造成位移和缩放的效果
 *
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/12/29 12 39
 */
public class BitmapTransView extends View {

    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap;
    private Rect mSrcRect; //源只能是Rect
    private RectF mDstRect; //目标可以是Rect|RectF

    public BitmapTransView(Context context) {
        this(context, null);
    }

    public BitmapTransView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapTransView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.danfan), null, options);
//            System.out.println(options.outWidth +"," + options.outHeight);
//            options.inJustDecodeBounds = false;
            mBitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.mv));
            System.out.println(mBitmap.getWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawOnLeftTop(canvas);

//        drawOnCenter(canvas);
//        drawOnCenter2(canvas);

        if (mDstRect != null) {
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null);
        }
    }

    /**
     * 原图尺寸绘制到左上角
     *
     * @param canvas
     */
    private void drawOnLeftTop(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * 原图尺寸绘制到中心
     * 目标尺寸为原图尺寸
     *
     * @param canvas
     */
    private void drawOnCenter(Canvas canvas) {
        float left = (mWidth - mBitmap.getWidth()) / 2;
        float top = (mHeight - mBitmap.getHeight()) / 2;
        canvas.drawBitmap(mBitmap, null, mDstRect = new RectF(left, top, left + mBitmap.getWidth(),
                top + mBitmap.getHeight()), null);
    }

    /**
     * 原图尺寸四边各减6分之一，作为内容，绘制到中心点右下偏移100像素
     * 目标尺寸为原图尺寸的一半
     *
     * @param canvas
     */
    private void drawOnCenter2(Canvas canvas) {
        float left = (mWidth - mBitmap.getWidth()) / 2;
        float top = (mHeight - mBitmap.getHeight()) / 2;
        mSrcRect = new Rect(mBitmap.getWidth() / 6, mBitmap.getHeight() / 6,
                mBitmap.getWidth() - mBitmap.getWidth() / 6, mBitmap.getHeight() - mBitmap.getHeight() / 6);
        canvas.drawBitmap(mBitmap, mSrcRect, mDstRect = new RectF(left + 100, top + 100,
                left + 100 + mBitmap.getWidth() / 2, top + 100 + mBitmap.getHeight() / 2), null);
    }

    /**
     * 利用值属性目标位移
     *
     * @param left
     * @param top
     * @param duration
     */
    private void drawTranslate(final int left, final int top, long duration) {
        mSrcRect = null;

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);//0到1的计算过程
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                float l = left + 0 * value; //left 偏移
                float t = top + 300 * value;//top 偏移
                mDstRect = new RectF(l, t,
                        l + mBitmap.getWidth(), t + mBitmap.getHeight());
                postInvalidate();
            }
        });
        animator.start();
    }

    /**
     * 利用值属性目标宽高从小变大，绘制起始点为left、top
     *
     * @param left
     * @param top
     * @param duration
     */
    private void drawZoomIn(final int left, final int top, long duration) {
        mSrcRect = null;

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);//0到1的计算过程
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                mDstRect = new RectF(left, top,
                        left + mBitmap.getWidth() * value, top + mBitmap.getHeight() * value);
                postInvalidate();
            }
        });
        animator.start();
    }

    /**
     * 利用值属性目标宽从大变小，绘制起始点为left、top
     *
     * @param left
     * @param top
     * @param duration
     */
    private void drawZoomOutWidth(final int left, final int top, long duration) {
        mSrcRect = null;

        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);//1到0的计算过程
        animator.setDuration(duration);
        animator.setInterpolator(new BounceInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                mDstRect = new RectF(left, top,
                        left + mBitmap.getWidth() * value, top + mBitmap.getHeight());
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int left = (int) (event.getX());
                int top = (int) (event.getY());
//                drawTranslate(left, top, 1000);
//                drawZoomIn(left, top, 1000);
                drawZoomOutWidth(left, top, 2000);
                break;
        }

        return super.onTouchEvent(event);
    }
}
