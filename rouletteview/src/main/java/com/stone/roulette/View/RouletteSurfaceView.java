package com.stone.roulette.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
public class RouletteSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Canvas mCanvas;
    private Thread mThread;
    private int mScreenWith, mScreenHeight;
    private int mRadius;
    private int mMin;
    private int mPart; //等分数
    private int mCenterX;
    private int mCenterY;
    private float mAngle;
    private List<Integer> mColorList;
    private boolean mRunFlag;
    private boolean mRunEnd;
    private long mStartRunTime, mEndRunTime;
    private float mDeltaAngle; //偏转角度
    private OnTouchRouletteListener mOnTouchRouletteListener;

    public interface OnTouchRouletteListener {
        void click(int position);
    }

    public RouletteSurfaceView(Context context) {
        this(context, null);
    }

    public RouletteSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouletteSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScreenWith = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mMin = Math.min(mScreenWith, mScreenHeight);
        mRadius = mMin / 2;  //使用小的一边作为半径默认值

        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRadius, getResources().getDisplayMetrics());
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RouletteSurfaceView);
        mRadius = (int) array.getDimension(R.styleable.RouletteSurfaceView_radius, mRadius);
        mPart = array.getInt(R.styleable.RouletteSurfaceView_part, mPart);


        mColorList = new ArrayList<Integer>();
        for (int i = 0; i < mPart; i++) {
            mColorList.add(getColor());
        }

        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this); //为SurfaceView添加状态监听
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //设置画笔无锯齿
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.FILL);
        setFocusable(true); //设置焦点

    }

    public void setOnTouchRouletteListener(OnTouchRouletteListener onTouchRouletteListener) {
        this.mOnTouchRouletteListener = onTouchRouletteListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("onMeasure----");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        System.out.println("onlayout----");
    }

    @Override //surfaceview的ondraw不是会调用的
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //记录扇形区 判断点在哪个区

    }

    public void startRotate() {
        mRunFlag = true;
        mRunEnd = false;
        mThread = new Thread(this);
        mThread.start();
        mStartRunTime = System.currentTimeMillis();
    }

    public void stopRotate() {
        mRunEnd = true;//要停止run
        mEndRunTime = System.currentTimeMillis();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initDraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        while (mRunFlag) {
            long start = System.currentTimeMillis();
            logic();
            myDraw();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void initDraw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas == null) {
                return;
            }
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mCanvas.drawPaint(paint);

            //绘制在横向居中 顶部 位置
            mCenterX = (mMin - 2*mRadius)/2 + mRadius;
            mCenterY = mRadius;
            mCanvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);

            RectF rectF = new RectF(mCenterX - mRadius, 0, mCenterX + mRadius, 2*mRadius);

            //等分圆 绘制扇形
            for (int i = 0; i < mPart; i++) {
                mPaint.setColor(mColorList.get(i));
                mCanvas.drawArc(rectF, 360 / mPart * i, 360 / mPart, true, mPaint);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }

    }
    private void myDraw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas == null) {
                return;
            }
            //画布旋转后，再重新绘制
            mCanvas.rotate(mAngle, mCenterX, mCenterY);

            RectF rectF = new RectF(mCenterX - mRadius, 0, mCenterX + mRadius, 2*mRadius);
            //等分圆 绘制扇形
            for (int i = 0; i < mPart; i++) {
                mPaint.setColor(mColorList.get(i));
                mCanvas.drawArc(rectF, 360.0f / mPart * i, 360.0f / mPart, true, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }



    private void logic() {
        if (!mRunEnd) {
            if (System.currentTimeMillis() - mStartRunTime > 1500) {
                mAngle += 30;
            } else if (System.currentTimeMillis() - mStartRunTime > 500) {
                mAngle += 10;
            } else if (System.currentTimeMillis() - mStartRunTime > 50) {
                mAngle += 5;
            }
        } else {
            if (System.currentTimeMillis() - mEndRunTime > 1500) {
                mAngle += 0;
                mRunFlag = false;
            } else if (System.currentTimeMillis() - mEndRunTime > 1000) {
                mAngle += 5;
            }  else if (System.currentTimeMillis() - mEndRunTime > 500) {
                mAngle += 10;
            } else if (System.currentTimeMillis() - mEndRunTime > 50) {
                mAngle += 15;
            }
        }

    }

    public boolean getRunStats() {
        return mRunFlag;
    }

    public void setPart(int part) {
        this.mPart = part;
        initDraw();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDeltaAngle = mAngle % 360;
                System.out.println("mDeltaAngle" + mDeltaAngle);
                //计算触摸的位置
                float x = event.getX();
                float y = event.getY();
                System.out.println(whichSector(x, y, mRadius));
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 计算点在那个扇形区域
     * @param X
     * @param Y
     * @param R 半径
     * @return
     */
    private int whichSector(double X, double Y, double R) {

        double mod;
        mod = Math.sqrt(X * X + Y * Y); //将点(X,Y)视为复平面上的点，与复数一一对应，现求复数的模。
        double offset_angle;
        double arg;
        arg = Math.round(Math.atan2(Y, X) / Math.PI * 180);//求复数的辐角。
        arg = arg < 0? arg+360:arg;
        if(mAngle%360 < 0){
            offset_angle = 360+mAngle%360;
        }else{
            offset_angle = mAngle%360;
        }
        if (mod > R) { //如果复数的模大于预设的半径，则返回0。
            return -2;
        } else { //根据复数的辐角来判别该点落在那个扇区。
            for(int i=0; i<mPart; i++){
                if(isSelect(arg, i,offset_angle) || isSelect(360+arg, i,offset_angle)){
                    return i;
                }
            }
        }
        return -1;
    }
    /**
     * 判读该区域是否被选中
     * @param arg 角度
     * @param i
     * @param offsetAngle 偏移角度
     * @return 是否被选中
     */
    private boolean isSelect(double arg, int i, double offsetAngle) {
        return arg>(i*mAngle+offsetAngle%360) && arg<((i+1)*mAngle+offsetAngle%360);
    }
}
