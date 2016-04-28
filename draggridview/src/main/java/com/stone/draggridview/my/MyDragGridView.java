package com.stone.draggridview.my;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/4/27 17 38
 */
public class MyDragGridView extends GridView implements AdapterView.OnItemLongClickListener {


    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private int mStatusHeight;
    private Vibrator mVibrator;
    private int mOffset2Left;//gv距屏幕左
    private int mOffset2Top;
    private int mDownX;
    private int mDownY;
    private int mMoveX;
    private int mMoveY;
    private View mStartDragView;
    private ImageView mImageView;
    private int mDownScrollBorder;
    private int mUpScrollBorder;
    private boolean mIsDrag;
    private OnChanageListener onChanageListener;
    private int mDragPosition;
    private View mLastHiddenView;
    private int mSpeed = 30;


    public MyDragGridView(Context context) {
        this(context, null);
    }

    public MyDragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mStatusHeight = getWindowStatusHeight(context);

        setOnItemLongClickListener(this);
    }

    private int getWindowStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                if (mOffset2Left == 0) {
                    mOffset2Left = (int) (ev.getRawX() - mDownX);
                }
                if (mOffset2Top == 0) {
                    mOffset2Top = (int) (ev.getRawY() - mDownY);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsDrag && mStartDragView != null) {
                    mMoveX = (int) ev.getX();
                    mMoveY = (int) ev.getY();
                    onDragItem();
                    return true; //若return false， 会走父view本身的scroll
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsDrag) {
                    removeCallbacks(mScrollRunnable);
                    onStopDrag();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void onStopDrag() {
//        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
//        if (view != null) {
//            view.setVisibility(View.VISIBLE);
//        }
        if (mLastHiddenView != null) {
            mLastHiddenView.setVisibility(View.VISIBLE);
        }

        mWindowManager.removeView(mImageView);
        mImageView = null;
        mIsDrag = false;

        mLastHiddenView = null;
    }

    /**
     * 判定 自动 滚动，并在滚动期间触发item交换
     */
    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {

            int scrollY = 0;
            if (mMoveY > mUpScrollBorder) {// 3/4h  上滚动
                scrollY = mSpeed;
                postDelayed(mScrollRunnable, 25);
            } else if (mMoveY < mDownScrollBorder) { // 1/4h  下滚动
                scrollY = -mSpeed;
                postDelayed(mScrollRunnable, 25);
            } else {
                scrollY = 0;
            }

            //当我们的手指到达GridView向上或者向下滚动的偏移量的时候，可能我们手指没有移动，但是DragGridView在自动的滚动
            //所以我们在这里调用下onSwapItem()方法来交换item
            onSwapItem();

//            scrollBy(0, scrollY); //只能用下面的
            smoothScrollBy(scrollY, 10);//api8 android2.2
        }
    };

    /**
     * 拖动item，在里面实现了item镜像的位置更新，item的相互交换以及GridView的自行滚动
     */
    private void onDragItem() {
        mLayoutParams.x += mMoveX - mDownX;
        mLayoutParams.y += mMoveY - mDownY;
        mWindowManager.updateViewLayout(mImageView, mLayoutParams);
        mDownX = mMoveX;
        mDownY = mMoveY;

        onSwapItem();

        post(mScrollRunnable);
    }

    private void onSwapItem() {
        int tempPosition = pointToPosition(mMoveX, mMoveY);
//        System.out.println(tempPosition + ",," + mDragPosition);
//        System.out.println("movey--" + mMoveY);
        if (tempPosition != mDragPosition && tempPosition != AbsListView.INVALID_POSITION) {
            if (onChanageListener != null) {
                onChanageListener.onChange(mDragPosition, tempPosition);//处理数据的交换
            }

            //旧位置上item显示
            if (mLastHiddenView == null) {
                mStartDragView.setVisibility(View.VISIBLE);
            } else {
                mLastHiddenView.setVisibility(View.VISIBLE);
            }

            //新位置上的item隐藏掉
            mLastHiddenView = getChildAt(tempPosition - getFirstVisiblePosition());//在gv中getChildAt只能拿到屏幕可见的view
            mLastHiddenView.setVisibility(View.INVISIBLE);

            mDragPosition = tempPosition;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        //获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
        if (mDownScrollBorder == 0) {
            mDownScrollBorder = getHeight() / 4;
        }
        //获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
        if (mUpScrollBorder == 0) {
            mUpScrollBorder = getHeight() * 3 / 4;
        }

        mStartDragView = view;
        mDragPosition = position;

        mVibrator.vibrate(50); //震动一下

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        createDragImage(bitmap);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        mStartDragView.setVisibility(View.INVISIBLE);//隐藏该item

        mIsDrag = true; //设置可以拖拽

        return false;
    }

    private void createDragImage(Bitmap bitmap) {
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.x = mStartDragView.getLeft() + mOffset2Left;
        mLayoutParams.y = mStartDragView.getTop() + mOffset2Top - mStatusHeight;
        mLayoutParams.format = PixelFormat.TRANSLUCENT; //图形其它区透明
        mLayoutParams.alpha = 0.55f;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mLayoutParams.width =  WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height =  WindowManager.LayoutParams.WRAP_CONTENT;
        mImageView = new ImageView(getContext());
        mImageView.setImageBitmap(bitmap);
//        mImageView.setBackgroundColor(Color.RED);
        mWindowManager.addView(mImageView, mLayoutParams);
    }

    public void setOnChangeListener(OnChanageListener onChanageListener) {
        this.onChanageListener = onChanageListener;
    }

    public interface OnChanageListener {

        /**
         * 当item交换位置的时候回调的方法，我们只需要在该方法中实现数据的交换即可
         *
         * @param form 开始的position
         * @param to   拖拽到的position
         */
        void onChange(int form, int to);
    }
}
