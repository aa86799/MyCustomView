package com.stone.guaguaka.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/5/3 20 27
 */
public class OverlayImageView extends View {

    private List<Bitmap> mBitmaps;
    private List<Point> mPoints;
    private int mWidth;
    private int mHeight;
    private int mPosition;
    private boolean mIsTouched;

    public OverlayImageView(Context context) {
        this(context, null);
    }

    public OverlayImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mBitmaps = new ArrayList<>();
        this.mPoints = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBitmaps.isEmpty()) {
            return;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmaps.isEmpty()) {
            return;
        }
        Bitmap outBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
        Canvas outCanvas = new Canvas(outBitmap);
        outCanvas.drawColor(Color.parseColor("#abc777"));


        Point point;
        for (int i = 0, len = mBitmaps.size(); i < len; ++i) {

            point = mPoints.get(i);

            if (mPosition != i) {
                outCanvas.drawBitmap(mBitmaps.get(i), point.x, point.y, null);
            }
        }

        /*
            如果不使用paint，那么默认情况下，后绘制的在上层
            这里有意将postion上的放在前面绘制，配合paint.setXfermode 绘制层次

            这里要注意一点的就是：
            如果paint 被new Canvas(bitmap)操作，这个canvas默认就是一个图层
            如果paint 被外部的canvas操作，需要给它指定图层，指定后设置的xfermode才有需要的效果，否则效果达不到期望,让人难以琢磨
         */

        Paint paint = new Paint();
        int saveLayerCount = canvas.saveLayer(0, 0, mWidth, mHeight, paint, Canvas.ALL_SAVE_FLAG);//存为新图层

        canvas.drawBitmap(mBitmaps.get(mPosition), mPoints.get(mPosition).x, mPoints.get(mPosition).y, paint); //dst
//        paint.setColor(Color.BLACK);
//        paint.setAlpha(50);
//        Point p = mPoints.get(mPosition);
//        canvas.drawRect(p.x, p.y, p.x + mWidth / 2, p.y + mHeight / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
//        paint.setAlpha(255);
        canvas.drawBitmap(outBitmap, 0, 0, paint); //src
        paint.setXfermode(null);

        canvas.restoreToCount(saveLayerCount); //恢复保存的图层


    }

    public void setImages(int... resId) {
        InputStream is;
        Bitmap bitmap;
        for (int i = 0; i < resId.length; i++) {
            is = getResources().openRawResource(resId[i]);
            bitmap = BitmapFactory.decodeStream(is);
            mBitmaps.add(bitmap);
        }

        mWidth = mBitmaps.get(0).getWidth() * 2;
        mHeight = mBitmaps.get(0).getHeight() * 2;

        for (int i = 0, len = mBitmaps.size(); i < len; ++i) {
            int x, y;
            if (i == 4) {
                x = mWidth / 4;
                y = mHeight / 4;
            } else {
                if (i % 2 == 0) {
                    x = 0;
                } else {
                    x = mWidth / 2;
                }

                if (i <= 1) {
                    y = 0;
                } else {
                    y = mHeight / 2;
                }
            }
            mPoints.add(new Point(x, y));

        }

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (mPosition != getTouchPosition(x, y)) {
                    mPosition = getTouchPosition(x, y);
                }
                mIsTouched = true;
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                mIsTouched = false;
                invalidate();
                break;


        }
        return super.onTouchEvent(event);
    }

    private int getTouchPosition(int x, int y) {
        int position = 0;
        Point p;
        for (int i = 0, len = mPoints.size(); i < len; i++) {
            p = mPoints.get(i);
            if (isCollision(x, y, p.x, p.y, mWidth / 2, mHeight / 2)) {
                position = i;
            }
        }
        return position;
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
    private boolean isCollision(int x1, int y1, int x2, int y2, int w, int h) {
        if (x1 >= x2 && x1 <= x2 + w && y1 >= y2 && y1 <= y2 + h) {
            return true;
        }
        return false;
    }
}
