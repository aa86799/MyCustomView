package com.stone.testimg.firework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.stone.testimg.R;

import java.util.ArrayList;
import java.util.List;

/**
 * desc   :
 * author:ShiZongyin
 * email:shizongyin@znds.com
 * time   : 14/01/2017 11 08
 */
public class SpringAnimView111 extends RelativeLayout {

    private FireworksView mFireworks;
    private List<ImageView> mIvSkyStrips;
    private boolean mIsMeasured;
    private List<Point> mPoints;

    {
        mPoints = new ArrayList<>();
        mPoints.add(new Point(98, 100));
        mPoints.add(new Point(138, 118));
        mPoints.add(new Point(248, 108));
        mPoints.add(new Point(293, 135));
        mPoints.add(new Point(425, 171));
        mPoints.add(new Point(457, 247));
        mPoints.add(new Point(546, 239));
        mPoints.add(new Point(722, 230));
        mPoints.add(new Point(783, 190));
        mPoints.add(new Point(836, 218));
    }

    public SpringAnimView111(Context context) {
        this(context, null);
    }

    public SpringAnimView111(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpringAnimView111(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        /*
        > 烟花 在一定高度以内 出现
        >  彩带 随机飞舞
        >  小鸡定时轮换图片
        > 横幅裁剪 及 文字展现
        > 层次 前中后：横幅-鸡-烟花
         */
//        mFireworks = new FireworksView(getContext());
        mIvSkyStrips = new ArrayList<>();
    }

//    private AnimationSet getAlphaAndTransAnim(int toX, int toY) {//冲天条
//        AnimationSet set = new AnimationSet(false);
//        TranslateAnimation translateAnimation = new TranslateAnimation(
//                Animation.ABSOLUTE, toX, Animation.ABSOLUTE, toX,
//                Animation.RELATIVE_TO_PARENT, 1, Animation.ABSOLUTE, toY);
//        translateAnimation.setDuration(500);
//        set.addAnimation(translateAnimation);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0xff, 0);
//        set.addAnimation(alphaAnimation);
//        return set;
//    }

 /*   @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private AnimatorSet getAlphaAndTransAnim(View view, float toY) {//冲天条
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator trans = ObjectAnimator.ofFloat(view, "translationY", getHeight(), toY);
        ObjectAnimator alpha = ObjectAnimator.ofInt(view, "alpha", 0xff, 0);
//        set.playTogether(trans, alpha);
        set.playSequentially(trans, alpha);
    return set;
    }*/

    private Animation getAlphaAndTransAnim(int toY) {//冲天条
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
//        set.addAnimation(translateAnimation);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0xff, 0);
//        set.addAnimation(alphaAnimation);
        return translateAnimation;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //1320*780
        if (!mIsMeasured) {
            mIsMeasured = true;
//            addView(mFireworks, UIFactory.createRelativeLayoutParams(0, 0, -2, -2));
            for (int i = 0, len = 10; i < len; i++) {
                final ImageView iv = new ImageView(getContext());
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                int x = (int) (i * getMeasuredWidth() / (len + 1) + Math.random() * 100);
                int y = (int) (Math.random() * i * getMeasuredHeight() / (len + 1) / 2 + 100);
                System.out.println("xxx" + x + ", yyy" + y);
                addView(iv, createRelativeLayoutParams(x, y, 8, 124));
                ImageLoader.getInstance().displayImage("", iv, createDefDisplayOptions(R.mipmap.strip0));
//                final AnimationSet set = getAlphaAndTransAnim(x, y);
                final Animation set = getAlphaAndTransAnim(y);
//                final AnimatorSet set = getAlphaAndTransAnim(iv, y);
                set.setDuration(1000);
                set.setRepeatMode(Animation.RESTART);
                set.setRepeatCount(Animation.INFINITE);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv.startAnimation(set);
//                        set.start();
                    }
                }, (long) (Math.random() * 1500));
//                }, 0);
            }
        }

    }

    private static class StripView extends View {
        private PointF mStripPoint;
        private Bitmap mBitmap;

        public StripView(Context context) {
            super(context);
        }

        public void setPoint(PointF stripPoint) {
            mBitmap = getBitmapFromLocal(R.mipmap.strip0);
            mStripPoint = stripPoint;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, 0, 0, null);
            }
        }

    }

    private static class FireworksView extends View {
        private Bitmap mBitmap;
        private Bitmap mStripBitmap;
        private PointF mFwPoint, mStripPoint;
        private Paint mPaint;
        private Rect mSrc;
        private RectF mDst;
        private final int TYPE_STRIP = 0;
        private final int TYPE_Fireworks = 1;
        private int mCurType = -1;

        public FireworksView(Context context) {
            super(context);
            mStripBitmap = getBitmapFromLocal(R.mipmap.strip0);
            mPaint = new Paint();
            mSrc = new Rect();
            mDst = new RectF();
            mSrc.left = 0;
            mSrc.top = 0;
        }

        public void setResAndPoint(int resId, PointF fwPoint, PointF stripPoint) {
            mBitmap = getBitmapFromLocal(resId);
            mFwPoint = fwPoint;
            mStripPoint = stripPoint;
            mSrc.right = mBitmap.getWidth();
            mSrc.bottom = mBitmap.getHeight();
            startAnim();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (mCurType == -1) {
                return;
            }
            mDst.left = 0;
            mDst.top = 0;
            mDst.right = 0;
            mDst.bottom = 0;
//            canvas.drawBitmap(mBitmap, mSrc, mDst, mPaint);

            if (mCurType == 0) {
                canvas.drawBitmap(mStripBitmap, mFwPoint.x, mFwPoint.y, null);
            } else if (mCurType == 1) {

                canvas.drawBitmap(mBitmap, mFwPoint.x, mFwPoint.y, null);
            }

        }

        public void startAnim() {
            switch (mCurType) {
                case -1: {
                    mCurType = 0;


                    invalidate();
                }
                break;
                case 0: {

                }
                break;
                case 1: {

                }
                break;
            }
        }
    }

//    private static class FireworksView extends View {
//
//        private int[] mRes = {
//                R.mipmap.fireworks1, R.mipmap.fireworks2, R.mipmap.fireworks3,
//                R.mipmap.fireworks4, R.mipmap.fireworks5
//        };
//        private ArrayList<Bitmap> mBitmaps;
//        private Rect mDst, mSrc;
//        private int mMaxItemWH;
//        private boolean mIsMeasured;
//        private int[] mWHs;
//        private PointF[] mPointFs;
//        private int mMultiple = 1;
//        private Paint mPaint;
//
//        public FireworksView(Context context) {
//            super(context);
//
//            mBitmaps = new ArrayList<>();
//            for (int i = 0, len = mRes.length; i < len; i++) {
//                mBitmaps.add(ImageLoaderWrapper.getBitmapFromLocal(mRes[i]));
//            }
//            Collections.shuffle(mBitmaps);
//            mSrc = new Rect();
//            mDst = new Rect();
//            mWHs = new int[mRes.length];
//            mPointFs = new PointF[mRes.length * mMultiple];
//            mPaint = new Paint();
//        }
//
//        @Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            if (!mIsMeasured) {
//                mIsMeasured = true;
//                mMaxItemWH = getMeasuredWidth() / (mBitmaps.size());
//                mSrc.left = 0;
//                mSrc.top = 0;
//
//                for (int i = 0, len = mBitmaps.size(); i < len; i++) {
//                    int wh = mMaxItemWH - (int) (Math.random() * mMaxItemWH / 2);
//                    mWHs[i] = wh;
//                }
//
//                for (int i = 0, len = mBitmaps.size(); i < len * mMultiple; i++) {
//                    int x = i * mMaxItemWH + (int)(mMaxItemWH / 3 * Math.random());
//                    int y = getMeasuredHeight() / 7 + (int) (Math.random() * getMeasuredHeight() / 8);
//                    mPointFs[i] = new PointF(x, y);
//                }
//
//                startAnim();
//            }
//        }
//
//        float percent;
//        int alpha = 255;
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            for (int i = 0, len = mBitmaps.size(); i < len * mMultiple; i++) {
//                Bitmap bitmap = mBitmaps.get(i % len);
//                mSrc.right = bitmap.getWidth();
//                mSrc.bottom = bitmap.getHeight();
//                mDst.left = (int) ((int) mPointFs[i].x - mWHs[i % len] / 2 * percent);
//                mDst.top = (int) ((int) mPointFs[i].y - mWHs[i % len] / 2 * percent);
//                mDst.right = (int) (mDst.left + mWHs[i % len] * percent);
//                mDst.bottom = (int) (mDst.top + mWHs[i % len] * percent);
//
//                canvas.drawBitmap(bitmap, mSrc, mDst, mPaint);
//                System.out.println("0000---" + mDst.width() + ",," + mDst.height());
//
//            }
//        }

//        private void startAnim() {
//            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    percent = animation.getAnimatedFraction();
//                    invalidate();
//                }
//            });
//            animator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
////                    startAlphaAnim();
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
//            animator.setDuration(1500);
//            animator.setInterpolator(new AccelerateDecelerateInterpolator());
//            animator.setRepeatCount(Animation.INFINITE);
//            animator.start();
//        }
//
//        private void startAlphaAnim() {
//            ValueAnimator animator = ValueAnimator.ofInt(alpha, 0);
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    alpha = (int) animation.getAnimatedValue();
//                    mPaint.setAlpha(alpha);
//                    invalidate();
//                }
//            });
//            animator.setDuration(1000);
//            animator.start();
//        }
//    }

    public static RelativeLayout.LayoutParams createRelativeLayoutParams(int x, int y, int w, int h) {
        RelativeLayout.LayoutParams params = new LayoutParams(w, h);
        params.setMargins(x, y, 0, 0);
        return params;
    }

    public static Bitmap getBitmapFromLocal(int resId) {
        if (resId != 0) {
            return ImageLoader.getInstance().loadImageSync("drawable://" + resId, createLocalResDefDisplayOptions());
        } else {
            return null;
        }
    }

    public static DisplayImageOptions createDefDisplayOptions(int resId) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageForEmptyUri(resId)
                .showImageOnFail(resId)
                .showImageOnLoading(resId)
                .build();
    }

    private static DisplayImageOptions createLocalResDefDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .resetViewBeforeLoading(true)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .bitmapConfig(Bitmap.Config.ARGB_8888)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
    }
}
