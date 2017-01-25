package com.stone.canvaspath.jane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/6/3 16 44
 */
public class JaneView extends View {

    private Path mPath;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Bitmap mSrcBitmap;
    private int mOutW, mOutH;
    private int mX, mY, mSrcX, mSrcY;

    private int mDownX, mDownY;
    private float mOldDis;
    private float mScaleValue = 1f;
    private long mCurTime;

    /*
    要实现：
    > 一个path中显示图片，图片需要进行一定的缩放，图片的最小宽或高等于path的外矩形的宽或高；另一边等比例
    > touch-move时，图片要跟着移动；touch-up时，要有边界判断：如果有一部分在path中不可见，则回弹图片，按move时的方向，显示完整图片
    > 双指缩放图片
    > 可以再扩展下，比如双击path区，加载相册图片
     */
    public JaneView(Context context) {
        this(context, null);
    }

    public JaneView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JaneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPath == null) {
            return;
        }

        canvas.drawPath(mPath, mPaint);
        canvas.clipPath(mPath);

        if (mBitmap == null) {
            return;
        }

//        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawBitmap(mBitmap, mX, mY, null);

    }


    public void setPath(Path path) {
        this.mPath = path;

        invalidate();
    }

    public void setOutRectWH(int w, int h) {
        mOutW = w;
        mOutH = h;
    }

    public void setStartPoint(int x, int y) {
        mX = mSrcX = x;
        mY = mSrcY = y;
    }

    public void setBitmapRes(int resId) {
        if (mPath == null) {
            return;
        }

        InputStream is = getResources().openRawResource(resId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        mBitmap = BitmapFactory.decodeStream(is, null, options);

        //已知path的外矩形w h， 计算缩放比例，以便 裁剪
        float scale = calcScale(options.outWidth, options.outHeight, mOutW, mOutH);
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) scale;
        System.out.println("缩放>>" + scale);
        is = getResources().openRawResource(resId); //需要再in一次， 否则取不到
        mSrcBitmap = mBitmap = BitmapFactory.decodeStream(is, null, options);

        /*
      canvas.drawBitmap(bitmap,matrix,paint);
        matrix变化，如果有缩放，缩放后的图片宽高不超过canvas的宽高
        所以可能完整的图片就显示不了                  不符合要求
         */
//        Bitmap bm = Bitmap.createBitmap(mOutW, mOutH, Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bm);
//        Matrix matrix = new Matrix();
//        scale = calcScale(options.outWidth, options.outHeight, mOutW, mOutH);
//        matrix.postScale(scale, scale);
//        canvas.drawBitmap(mBitmap, matrix, null);
//        mBitmap = bm;

        //以w,h为目标对bitmap进行缩放，filter表示是否要对位图进行过滤(滤波)处理   没有按比例来，不符合要求
//        mBitmap = Bitmap.createScaledBitmap(mBitmap, mOutW, mOutH, true);


        /*
       Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter)
        以source-bitmap的x,y为起点，宽w高h的范围，进行matrix变化
         */
        Matrix matrix = new Matrix();
        scale = calcScale(options.outWidth, options.outHeight, mOutW, mOutH);
        matrix.postScale(scale, scale);
        //宽高值可能不符合src-bitmap，效果不符合要求
//        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mOutW, mOutH, matrix, true);
        //宽高就是 截取原图的宽高
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);


//        matrix.postScale(2, 2);
//        mBitmap = Bitmap.createBitmap(mSrcBitmap, 0, 0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), matrix, true);
    }

    /**
     * 图片按目标宽高的最小边进行缩放
     *
     * @param bmW
     * @param bmH
     * @param destW
     * @param destH
     * @return 返回统一的缩放比例
     */
    private float calcScale(int bmW, int bmH, float destW, float destH) {
        float min = Math.min(bmW, bmH);
        float scale;
        if (bmW == min) {
            scale = destW / bmW;
        } else {
            scale = destH / bmH;
        }

        return scale;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("ACTION_DOWN");
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
//                if (isCollision(mDownX, mDownY, mSrcX, mSrcY, mOutW, mOutH)) {
                if (isCollision(mDownX, mDownY, mSrcX + mOutW * 1.0f / 2, mSrcY + mOutH * 1.0f / 2, mOutH / 2)) {
                    return true;
                } else {
                    break;
                }

            case MotionEvent.ACTION_MOVE:
                System.out.println("move");
                if (event.getPointerCount() == 1 && System.currentTimeMillis() - mCurTime > 500) {
                    System.out.println("111111");
                    int moveX = (int) event.getX();
                    int moveY = (int) event.getY();
                    int dx = moveX - mDownX;
                    int dy = moveY - mDownY;
                    mX += dx;
                    mY += dy;
                    mDownX = moveX;
                    mDownY = moveY;
                    invalidate();
                    mCurTime = System.currentTimeMillis();
                } else if (event.getPointerCount() == 2 && System.currentTimeMillis() - mCurTime > 500) {

                    float ux1 = event.getX(0);
                    float uy1 = event.getY(0);
                    float ux2 = event.getX(1);
                    float uy2 = event.getY(1);
                    float disx1 = ux2 - ux1;
                    float disy1 = uy2 - uy1;
                    float newDis = (float) Math.sqrt(disx1 * disx1 + disy1 * disy1);

                    scaleImage(newDis - mOldDis);

                    if (newDis > mOldDis) { //放大

                    } else if (newDis < mOldDis) { //缩小

                    }
                    invalidate();
                    mOldDis = newDis;
                    mCurTime = System.currentTimeMillis();
                }

                break;

            case MotionEvent.ACTION_UP:
                System.out.println("ACTION_UP");
//                mX = mSrcX;
//                mY = mSrcY;  // 改用bounce计算
                int upX = (int) (mX + event.getX() - mDownX); //手指up后，图片此时的起始点
                int upY = (int) (mY + event.getY() - mDownY);
                bounce(upX, upY);
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_DOWN: {//当屏幕上已经有一个点被按住,此时再按下其他点时触发
//                System.out.println(event.getPointerCount() + "...");
                System.out.println("ACTION_POINTER_DOWN");
                float ux1 = event.getX(0);
                float uy1 = event.getY(0);
                float ux2 = event.getX(1);
                float uy2 = event.getY(1);
                float disx1 = ux2 - ux1;
                float disy1 = uy2 - uy1;
                mOldDis = (float) Math.sqrt(disx1 * disx1 + disy1 * disy1);
                mCurTime = System.currentTimeMillis();
            }
                break;

            case MotionEvent.ACTION_POINTER_UP: {
                System.out.println("ACTION_POINTER_UP");
            }
                break;

        }
        return super.onTouchEvent(event);
    }

    private void scaleImage(float dis) {
        System.out.println("图片缩放" + dis);

        /*
        move 的时候 会不停的调用该方法
        如果判断一个距离的绝对值，可以降低调用的频率，但还是会闪烁
        加一个时间值，比如两次move的间隔要超过500ms才有效
         */
        if (Math.abs(dis) < 1) {
            return;
        }

        /*
        缩放规则
         */
        if (dis > 0) { //放大
            mScaleValue += 0.1f;
        } else if (dis < 0){ //缩小
            mScaleValue -= 0.1f;
        }
        if (mScaleValue < 0.5f) {
            mScaleValue = 0.5f;
        }
        if (mScaleValue > 2.0f) {
            mScaleValue = 2.0f;
        }

        Matrix matrix = new Matrix();
        float scale = calcScale(mBitmap.getWidth(), mBitmap.getHeight(), mOutW, mOutH);
        matrix.postScale(scale*mScaleValue, scale*mScaleValue);
        //宽高就是 截取原图的宽高
        mBitmap = Bitmap.createBitmap(mSrcBitmap, 0, 0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), matrix, true);
    }

    /**
     * @param x1 点
     * @param y1 点
     * @param x2 矩形view x
     * @param y2 矩形view y
     * @param w  矩形view 宽
     * @param h  矩形view 高
     * @return
     */
    public static boolean isCollision(float x1, float y1, float x2, float y2, float w, float h) {
        if (x1 >= x2 && x1 <= x2 + w && y1 >= y2 && y1 <= y2 + h) {
            return true;
        }
        return false;
    }

    public static boolean isCollision(float x1, float y1, float x2, float y2, float r) {
        if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= r) {
            // 如果点和圆心距离小于或等于半径则认为发生碰撞
            return true;
        }
        return false;
    }

    //回弹
    private void bounce(int x, int y) {
        /*
        要求bitmap在path中，不能有空白的部分
         */
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();

        if (!(x <= mSrcX && y <= mSrcY && x + w >= mSrcX + mOutW && y + h >= mSrcY + mOutH)) {
//            System.out.println("满足了 不包含条件");

            if (x > mSrcX) {
                x = mSrcX;
            } else {
                while (x < mSrcX && x + w < mSrcX + mOutW) {
                    x++;
                }
            }

            if (y > mSrcY) {
                y = mSrcY;
            } else {
                while (y < mSrcY && y + h < mSrcY + mOutH) {
                    y++;
                }
            }
        }

        mX = x;
        mY = y;

    }
}
