package com.stone.turnpage.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/7/8 10 35
 */
public class HorizonTurnPageView extends View {

    private Paint mPaint;
    private Path mPath;
    private List<Bitmap> mBitmaps;
    private float mClipX; //裁剪右端点坐标
    private float mCurPointX;// 指尖触碰屏幕时点X的坐标值
    private float mAutoAreaLeft, mAutoAreaRight; //控件左侧和右侧自动吸附的区域
    private boolean mIsLastPage; //是否最后一页
    private boolean mIsNextPage; //是否下一页
    private int mPageIndex;

    private Runnable mMsgCallback;
    private Handler mHandler = new Handler();

    public HorizonTurnPageView(Context context) {
        super(context);

//        ViewConfiguration.get(context).getScaledTouchSlop()
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        if (null == bitmaps || bitmaps.size() == 0) return;
        this.mBitmaps = bitmaps;

        invalidate();
    }

    /**
     * 图片倒序:集合中最先加入的图(最后绘制)就能绘制在最上层
     */
    private void initBitmaps() {
        List<Bitmap> temp = new ArrayList<Bitmap>();
        for (int i = mBitmaps.size() - 1; i >= 0; i--) {
            Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), getWidth(), getHeight(), true);
            temp.add(bitmap);
        }
        mBitmaps = temp;
    }

    private void drawBitmaps(Canvas canvas) {
/*        for (int i = 0; i < mBitmaps.size(); i++) {
//            canvas.save();

            if (i == mBitmaps.size() - 1) {//只有最后一位的图片(即原集合中的首图)时才裁剪一次
                canvas.clipRect(0, 0, mClipX, getHeight());
            }
            canvas.drawBitmap(mBitmaps.get(i), 0, 0, null);

//            canvas.restore();
        }*/

        /*
        如果图片太多,那么 其实只要绘制上下两张图即可,  而不用 所有都绘制
        代码重构如下
         */
        mIsLastPage = false;
        mPageIndex = mPageIndex < 0 ? 0 : mPageIndex;
        mPageIndex = mPageIndex > mBitmaps.size() ? mBitmaps.size() : mPageIndex;
        // 计算数据起始位置
        int start = mBitmaps.size() - 2 - mPageIndex;
        int end = mBitmaps.size() - mPageIndex;
        /*
         * 如果数据起点位置小于0则表示当前已经到了最后一张图片
         */
        if (start < 0) {
            mIsLastPage = true; // 此时mPageIndex = size - 1

            showToast("已经最后一页了");
            // 强制重置起始位置
            start = 0;
            end = 1;
        }

        for (int i = start; i < end; i++) {
            if (!mIsLastPage && i == end - 1) {
                canvas.clipRect(0, 0, mClipX, getHeight());
            }
            canvas.drawBitmap(mBitmaps.get(i), 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mIsNextPage = true;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mCurPointX = event.getX();
                if (mCurPointX < mAutoAreaLeft) {
                    mIsNextPage = false; //上一页
                    mPageIndex--;
                    mClipX = mCurPointX;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                mClipX = event.getX();
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                judgeSlideAuto();
                /*
                 * 如果当前页不是最后一页
                 * 如果是需要翻下一页
                 * 并且上一页已被clip掉
                 */
                if (!mIsLastPage && mIsNextPage && mClipX <= 0) {
                    mPageIndex++;
                    mClipX = getWidth();
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 判断是否要自动滑动: 向左或向右 滑动到底
     */
    private void judgeSlideAuto() {
        if (mClipX < mAutoAreaLeft) {// 小于 1/5 w   向左
            while (mClipX > 0) {
                mClipX--;
                invalidate();
            }

        } else if (mClipX > mAutoAreaRight) {// 大于 4/5 w  向右
            while (mClipX < getWidth()) {
                mClipX++;
                invalidate();
            }
        }
    }

    /*
    onMeasure后 调用, 如果是ViewGroup型, 在onLayout时又改变了大小才会 再次调用
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        initBitmaps();

        mClipX = getWidth();
        mAutoAreaLeft = getWidth() / 5f;
        mAutoAreaRight = getWidth() / 5f * 4;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (null == mBitmaps || mBitmaps.size() == 0) {
            return;
        }
        drawBitmaps(canvas);

    }

    private void showToast(final Object msg) {
        mHandler.removeCallbacksAndMessages(null);
        mMsgCallback = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        mHandler.postDelayed(mMsgCallback, 200);

    }

}
