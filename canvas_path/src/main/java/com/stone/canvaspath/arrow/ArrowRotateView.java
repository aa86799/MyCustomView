package com.stone.canvaspath.arrow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.stone.canvaspath.R;

import java.io.InputStream;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/7/2 10 06
 */
public class ArrowRotateView extends View {

    private Path mPath;
    private Paint mPaint;
    private Bitmap mBitmap;
    private PathMeasure mPathMeasure;
    private float[] mPos, mTan;
    private Matrix mMatrix;
    private Path mDst;

    public ArrowRotateView(Context context) {
        super(context);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);

        InputStream is = getResources().openRawResource(R.drawable.heart);
        mBitmap = BitmapFactory.decodeStream(is);
    }

    public void setPath(Path path) {
        this.mPath = path;
        this.mPathMeasure = new PathMeasure(mPath, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


//        canvas.drawPath(mPath, mPaint);

        /*
        e. 截取部份
         */
        if (mDst != null) {
            canvas.drawPath(mDst, mPaint);
        }

        if (mTan == null || mPos == null) {
            return;
        }

        /*
            a. 根据计算出的当前点位置,绘制图片  发现图片偏离了
         */
//        canvas.drawBitmap(mBitmap, mPos[0], mPos[1], null);


        /*
            b. 使用matrix 平移(-w/2,-h/2)
            根据tan,拿到旋转角度, 添加到matrix中, 用于旋转图片
         */
/*        float rotate = (float) Math.toDegrees(Math.atan2(mTan[1], mTan[0]));
//        System.out.println(rotate+"..");
//        System.out.println(mPos[0] + ".." + mPos[1]);
        Matrix matrix = new Matrix();
//        matrix.postRotate(rotate, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2); //图片中心作旋转
        //图片有时在path里，有时在外   起点为0度 因为图片为有外矩形  所以要平移
//        matrix.postTranslate(mPos[0], mPos[1]);
        matrix.postTranslate(mPos[0] - mBitmap.getWidth()  / 2, mPos[1] - mBitmap.getHeight() / 2);
//        canvas.drawBitmap(mBitmap, matrix, null);*/


        /*
            c|d. 使用PathMeasure.getMatrix() 来达到 b 的效果
         */
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startAnim() {

        mPos = new float[2];
        mTan = new float[2];
        mMatrix = new Matrix();
//        mDst = new Path();

        ValueAnimator animator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        animator.setDuration(5000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                //a.
//                mPathMeasure.getPosTan(distance, mPos, null);

                //b.
//                mPathMeasure.getPosTan(distance, mPos, mTan);

                //c.
//                mPathMeasure.getMatrix(distance, mMatrix, PathMeasure.POSITION_MATRIX_FLAG); //设置matrix: 只对位置起效果
//                mMatrix.postTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);

                //d.
                //设置matrix: 对位置和Tangent都起效果
                mPathMeasure.getMatrix(distance, mMatrix, PathMeasure.POSITION_MATRIX_FLAG|PathMeasure.TANGENT_MATRIX_FLAG);
                //需要用左乘
                mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);

                /*
                e. 截取部份
                true 会moveTo新的轮廓起点
                截取的长度为StopD-StartD
                 */
                mDst = new Path();
//                mPathMeasure.getSegment((float) (distance*Math.random()), distance, mDst, true);
                mPathMeasure.getSegment(distance-100, (float)(200*Math.random() + distance), mDst, true);


                invalidate();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                boolean flag = mPathMeasure.nextContour();
                if (flag) {
                    //调用该方法后，会直接走到下一段moveTo后的  所以放在内部判断
                    startAnim();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPathMeasure = new PathMeasure(mPath, false);
                startAnim();
                break;
        }

        return true;
    }

}
