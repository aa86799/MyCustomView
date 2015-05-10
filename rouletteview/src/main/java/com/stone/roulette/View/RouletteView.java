package com.stone.roulette.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.stone.roulette.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 等分圆 旋转轮盘
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/5/8 16 43
 */
public class RouletteView extends View {

    private int mScreenWith, mScreenHeight;
    private int mRadius;
    private int mMin;
    private int mPart; //等分数
    private int mCenterX;
    private int mCenterY;
    private List<Integer> mColorList;
    private boolean mAnimRunFlag;
    private boolean isInit = true; //是否是初始状态

    public RouletteView(Context context) {
        this(context, null);
    }

    public RouletteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouletteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScreenWith = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mMin = Math.min(mScreenWith, mScreenHeight);
        mRadius = mMin / 2;  //使用小边作为半径默认值

        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRadius, getResources().getDisplayMetrics());
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RouletteSurfaceView);
        mRadius = (int) array.getDimension(R.styleable.RouletteSurfaceView_radius, mRadius);
        mPart = array.getInt(R.styleable.RouletteSurfaceView_part, mPart);


        mColorList = new ArrayList<Integer>();
        for (int i = 0; i < mPart; i++) {
            mColorList.add(getColor());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInit) {
            isInit = false;

            Paint paint = new Paint();
//        paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#abc365"));
            //绘制在横向居中 顶部 位置
            mCenterX = (mMin - 2*mRadius)/2 + mRadius;
            mCenterY = mRadius;
            canvas.drawCircle(mCenterX, mCenterY, mRadius, paint);

            RectF rectF = new RectF(mCenterX - mRadius, 0, mCenterX + mRadius, 2*mRadius);
//        RectF rectF = new RectF(100,0,200,200);
            paint.setStrokeWidth(6);
            paint.setStyle(Paint.Style.FILL);

            //等分圆 绘制扇形
            for (int i = 0; i < mPart; i++) {
                paint.setColor(mColorList.get(i));
                canvas.drawArc(rectF, 360 / mPart * i, 360 / mPart, true, paint);
            }
        } else {
            //使非初始状态，旋转画布
            if (mAnimRunFlag) {
                canvas.rotate(1);
                System.out.println("中华人民共和国");
            }
        }



        //记录扇形区 判断点在哪个区

    }

    public void startRotate() {
        mAnimRunFlag = true;
       /*
        使用动画，会调用 onDraw
         */
      /* RotateAnimation rotateAnim = new RotateAnimation(
                0, 18000, Animation.ABSOLUTE, mCenterX, Animation.ABSOLUTE, mCenterY
        );
        rotateAnim.setDuration(3600 * 10);
        startAnimation(rotateAnim);*/
    }

    public void stopRotate() {
        mAnimRunFlag = false;
    }


    /**
     * 随机颜色
     * @return
     */
    private int getColor() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String temp;
        //rgb三个区域
        for (int i = 0; i < 3; i++) {
            temp = Integer.toHexString(random.nextInt(0xFF));
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            sb.append(temp);
        }
        return Color.parseColor("#" + sb.toString());
    }


}
