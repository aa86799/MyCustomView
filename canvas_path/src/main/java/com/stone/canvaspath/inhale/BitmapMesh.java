/**
 * Copyright © 2013 CVTE. All Rights Reserved.
 */
package com.stone.canvaspath.inhale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;

import com.stone.canvaspath.R;


/**
 * @author Taolin
 * @version v1.0
 * @description TODO
 * @date Dec 30, 2013
 */

public class BitmapMesh {

    public static class SampleView extends View {

        private static final int WIDTH = 40;
        private static final int HEIGHT = 40;

        private Bitmap mBitmap;
        private boolean mIsDebug;
        private Paint mPaint;
        private float[] mInhalePoint;
        private InhaleMesh mInhaleMesh;

        public SampleView(Context context) {
            super(context);
            setFocusable(true);

//            mBitmap = BitmapFactory.decodeResource(getResources(),
//                    R.drawable.aa);

//            Matrix m = new Matrix();
//            m.postScale(0.5f, 0.5f);
//            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = true; //只读宽高边界
            mBitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.aa), null, options);
            int imgw = options.outWidth; //=w  因为 layoutParams=-1
            int imgh = options.outHeight; //=h
            options.inJustDecodeBounds = false;
            options.inSampleSize = calculateInSampleSize(options, imgw, imgh);

            //缩放后的
            mBitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.aa), null, options);


            mPaint = new Paint();
            mInhalePoint = new float[]{0, 0};
            mInhaleMesh = new InhaleMesh(WIDTH/2, HEIGHT/3);
            mInhaleMesh.setBitmapSize(mBitmap.getWidth(), mBitmap.getHeight());
        }

        public void setIsDebug(boolean isDebug) {
            mIsDebug = isDebug;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            float bitmapWidth = mBitmap.getWidth();
            float bitmapHeight = mBitmap.getHeight();

            buildPaths(bitmapWidth / 2, h - 20);
            buildMesh(bitmapWidth, bitmapHeight);
        }

        public boolean startAnimation(boolean reverse) {
            Animation anim = this.getAnimation();
            if (null != anim && !anim.hasEnded()) {
                return false;
            }

            PathAnimation animation = new PathAnimation(0, HEIGHT, reverse,
                    new PathAnimation.IAnimationUpdateListener() {
                        @Override
                        public void onAnimUpdate(int index) {
                            mInhaleMesh.buildMeshes(index);
                            invalidate();
                        }
                    });

            if (null != animation) {
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.setDuration(1000);
                this.startAnimation(animation);
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFCCCCCC);
            /*
-bitmap: 就是将要扭曲的图像
-meshWidth: 需要的横向网格数目
-meshHeight: 需要的纵向网格数目
-verts: 网格顶点坐标数组。
-vertOffset: verts数组中开始跳过的(x,y)对的数目。偏移后，要保证之后的条目数至少有(meshWidth+1)*(meshHeight+1)个
-colors: 颜色数组，可以为空。指定每个顶点的颜色，数组长度至少要 (meshWidth + 1)*(meshHeight + 1)+colorOffset
-colorOffset：在绘制前，跳过的colors数组中的偏移量
-paint:

其中，verts是个一维数组，保存所有顶点坐标信息。偶数项保存x坐标，奇数项保存y坐标。
比如有有meshWidth*meshHeight个网格，如果vertOffset为0，那么算上两端就有(meshWidth+1)*(meshHeight+1)个顶点，
verts数组就应该至少长度为(meshWidth+1)*(meshHeight+1)。

             */
            canvas.drawBitmapMesh(mBitmap,
                    mInhaleMesh.getWidth(),
                    mInhaleMesh.getHeight(),
                    mInhaleMesh.getVertices(),
                    0, null, 0, null);

            // Draw the target point.
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(2);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mInhalePoint[0], mInhalePoint[1], 5, mPaint);

            if (mIsDebug) {
                // Draw the mesh vertices.
//                canvas.drawPoints(mInhaleMesh.getVertices(), mPaint);
                // Draw the paths
                mPaint.setColor(Color.BLUE);
                mPaint.setStyle(Paint.Style.STROKE);
                Path[] paths = mInhaleMesh.getPaths();
                for (Path path : paths) {
//                    canvas.drawPath(path, mPaint);
                }
            }
        }

        private void buildMesh(float w, float h) {
            mInhaleMesh.buildMeshes(w, h);
        }

        private void buildPaths(float endX, float endY) {
            mInhalePoint[0] = endX;
            mInhalePoint[1] = endY;
            mInhaleMesh.buildPaths(endX, endY);
        }

        private int mLastPointX = 0;
        private int mLastPointY = 0;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float[] pt = {event.getX(), event.getY()};

            if (event.getAction() == MotionEvent.ACTION_UP) {
                int x = (int) pt[0];
                int y = (int) pt[1];
                if (mLastPointX != x || mLastPointY != y) {
                    mLastPointX = x;
                    mLastPointY = y;
                    buildPaths(pt[0], pt[1]);
                    invalidate();
                }
            }
            return true;
        }
    }

    private static class PathAnimation extends Animation {

        public interface IAnimationUpdateListener {
            public void onAnimUpdate(int index);
        }

        private int mFromIndex;
        private int mEndIndex;
        private boolean mReverse;
        private IAnimationUpdateListener mListener;

        public PathAnimation(int fromIndex, int endIndex, boolean reverse,
                             IAnimationUpdateListener listener) {
            mFromIndex = fromIndex;
            mEndIndex = endIndex;
            mReverse = reverse;
            mListener = listener;
        }

//        public boolean getTransformation(long currentTime, Transformation outTransformation) {
//            return super.getTransformation(currentTime, outTransformation);
//        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Interpolator interpolator = this.getInterpolator();
            if (null != interpolator) {
                float value = interpolator.getInterpolation(interpolatedTime);
                interpolatedTime = value;
            }
            if (mReverse) {
                interpolatedTime = 1.0f - interpolatedTime; //插值取反(0~1 变成 1~0)
            }
//            System.out.println(interpolatedTime);
            //运动时间interpolatedTime 内的一个距离: currentIndex
            int currentIndex = (int) (mFromIndex + (mEndIndex - mFromIndex) * interpolatedTime);

            if (null != mListener) {
                mListener.onAnimUpdate(currentIndex);
            }
        }
    }


    /**
     * 计算 图片缩小比例
     *
     * @param options
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //先根据宽度进行缩小
        while (width / inSampleSize > reqWidth) {
            inSampleSize++;
        }
        //然后根据高度进行缩小
        while (height / inSampleSize > reqHeight) {
            inSampleSize++;
        }
        return inSampleSize;
    }
}