package com.stone.luckyturntable;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/12/20 16 55
 */
public abstract class BaseSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private boolean mIsRun;
    private Thread mThread;
    private OnSurfaceLintener mOnSurfaceLintener;

    public BaseSurfaceView(Context context) {
        this(context, null);
    }

    public BaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();
        mHolder.addCallback(this);
        mThread = new Thread(this);
    }

    public interface Drawer {
        void draw(Canvas canvas);
    }

    public interface OnSurfaceLintener {
        void onSurfaceCreated(SurfaceHolder holder);

        void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height);

        void onSurfaceDestroyed(SurfaceHolder holder);
    }

    public void setOnSurfaceLintener(OnSurfaceLintener onSurfaceLintener) {
        this.mOnSurfaceLintener = onSurfaceLintener;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsRun = true;
        mThread.start();

        if (mOnSurfaceLintener != null) {
            mOnSurfaceLintener.onSurfaceCreated(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mOnSurfaceLintener != null) {
            mOnSurfaceLintener.onSurfaceChanged(holder, format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRun = false;

        if (mOnSurfaceLintener != null) {
            mOnSurfaceLintener.onSurfaceDestroyed(holder);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mIsRun = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void run() {
        Canvas canvas = null;
        while (mIsRun) {
            try {
                long start = System.currentTimeMillis();
                canvas = mHolder.lockCanvas();
                drawer(canvas);
                long end = System.currentTimeMillis();
                Thread.sleep(50 - (end - start));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
                }
            }
        }
    }

    protected abstract void drawer(Canvas canvas);
}
