package com.stone.surfaceview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.stone.surfaceview.R;


/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/12/15 01 54
 *
 * 在一张大图中绘制一个小图，并旋转它
 * 使用lockCanvas(Rect dirty); 优化效率
 * 意指 该矩形内被重绘，矩形外保存至下一次调用,即下一帧
 * 注意SurfaceView会缓存前两帧，当背景绘制的帧次少于3帧时可能会在变化矩形区闪烁
 *
 */
public class MySurfaceView2 extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap mBack;
    private Bitmap mBall;
    private SurfaceHolder mHolder;
    private boolean mIsRun;

    public MySurfaceView2(Context context) {
        super(context);
        mBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        mBall = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mHolder = this.getHolder();// 获取holder
        mHolder.addCallback(this);
        mIsRun = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 启动自定义线程
        new Thread(new MyRunn()).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRun = false;
    }

    // 自定义线程类
    class MyRunn implements Runnable {

        @Override
        public void run() {
            Canvas canvas = null;
            int rotate = 0;// 旋转角度变量
            int frameCount = 0;
            while (true) {
                try {
                    int x = (mBack.getWidth() - mBall.getWidth()) / 2;
                    int y = (mBack.getHeight() - mBall.getHeight()) / 2;

                    // 获取画布
                    canvas = mHolder.lockCanvas(new Rect(x, y, x + mBall.getWidth(),y + mBall.getHeight()));
                    //测试在中心绘圆
//                    Paint paint = new Paint();
//                    paint.setColor(Color.RED);
//                    canvas.drawCircle(mBack.getWidth() / 2, mBack.getHeight() / 2, 10, paint);

                    //绘制背景
                    if (frameCount < 3) {
                        canvas.drawBitmap(mBack, 0, 0, null);
                        frameCount++;
                    }
                    //创建矩阵以控制图片旋转和平移
                    Matrix m = new Matrix();
                    // 设置旋转角度
                    m.postRotate((rotate += 30) % 360, mBall.getWidth() / 2, mBall.getHeight() / 2);
                    // 设置左边距和上边距
                    m.postTranslate(x, y) ;
                    // 绘制ball
                    canvas.drawBitmap(mBall, m, null);
                    // 休眠以控制最大帧频为每秒约30帧
                    Thread.sleep(33);
                } catch (Exception e) {

                } finally {
                    if (mIsRun) {
                        mHolder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
                    }
                }
            }
        }

    }
}