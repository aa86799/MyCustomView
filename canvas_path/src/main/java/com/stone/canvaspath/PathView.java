package com.stone.canvaspath;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/5/19 10 08
 */
public class PathView extends View {

    private Paint mPaint;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private float[] mPoints;
    private float[] mTans;

    private boolean isFirst = true;
    private ArrayList<float[]> list;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFlags(Paint.DITHER_FLAG);


        mPoints = new float[2];
        mTans = new float[2];

    }

    public void setPath(Path path) {
        this.mPath = path;
        this.mPathMeasure = new PathMeasure(mPath, true);
        list = new ArrayList();

//        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        if (mPath == null) {
            return;
        }
        canvas.drawColor(Color.GREEN);
//        canvas.rotate(30);
//        canvas.rotate(30, 300, 300);
//        canvas.translate(200, 200);

        mPaint.setColor(Color.RED);
//        canvas.drawPath(mPath, mPaint);
//        mPath.reset();


        canvas.drawCircle(mPoints[0], mPoints[1], 20, mPaint);
//        System.out.println("--->" + Arrays.toString(mTans) + ",,," +(mPoints[0]/mPoints[1]));

        if (isFirst) {
            isFirst = false;


        } else {
            for (int i = 1; i < list.size(); i++) {
                float[] p1 = list.get(i - 1);
                float[] p2 = list.get(i);
//                mPaint.setColor(Color.BLACK);
                canvas.drawLine(p1[0], p1[1], p2[0], p2[1], mPaint);
//                System.out.println(p1[0]+" "+p1[1]+" "+p2[0]+"  "+p2[1]);
//                canvas.drawCircle(p1[0], p1[1], 40, mPaint);
            }
        }

    }

    public void startAnim() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator animator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
            System.out.println("length: " + mPathMeasure.getLength());


            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float distance = (float) animation.getAnimatedValue();
//                    System.out.println(distance);


                    if (distance > mPathMeasure.getLength() - 10) {
                        distance = mPathMeasure.getLength();

                        //传入一个开始和结束距离，然后会返回介于这之间的Path，即dstPath，它会被填充上内容
//                        mPathMeasure.getSegment(startD, stopD, dstPath, startWithMoveTo);


                        mPathMeasure.getPosTan(distance, mPoints, mTans);
                        list.add(new float[]{mPoints[0], mPoints[1]});
                        postInvalidate();

                        animation.removeAllListeners();
                        animation.cancel();

                        if (mPathMeasure.nextContour()) { //如果有下一段的新起点(moveto)
                            startAnim();
                        }
                        return;
                    }

                    //传入一个距离distance(0<=distance<=getLength())，然后会计算当前距离的坐标点和切线，注意，pos会自动填充上坐标
                    mPathMeasure.getPosTan(distance, mPoints, mTans);
                    list.add(new float[]{mPoints[0], mPoints[1]});
                    postInvalidate();
                }
            });
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setDuration(2000);
            animator.start();

            /*
            只有path第一段 才被计算了
             */
        }
    }
}
