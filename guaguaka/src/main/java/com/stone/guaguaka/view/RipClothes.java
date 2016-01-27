package com.stone.guaguaka.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.stone.guaguaka.R;

/**
 * 撕衣服
 * 跟刮刮卡类似：同样是消除上一层，保留下一层，只是这个实现核心更简单
 * 通过bitmap.setpixel() 使上一层的像素点透明，而看到下一层
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/1/27 22 37
 */
public class RipClothes extends RelativeLayout {

    private ImageView mIvTop;
    private ImageView mIvBottom;
    private int[] mImgAry = {R.drawable.nn1, R.drawable.nn2};
    private Bitmap mTopBitmap;
    private Bitmap mBottomBitmap;
    private int mMove = 20;


    public RipClothes(Context context) {
        this(context, null);
    }

    public RipClothes(Context context, AttributeSet attrs) {
        super(context, attrs);

        mIvTop = new ImageView(getContext());
        mIvBottom = new ImageView(getContext());

        addView(mIvBottom, new LayoutParams(-1, -1));
        addView(mIvTop, new LayoutParams(-1, -1));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            int w = mIvBottom.getWidth();
            int h = mIvBottom.getHeight();

            /* 底图使用ScaleType.MATRIX 缩放 */
            mIvBottom.setScaleType(ImageView.ScaleType.MATRIX);
            mBottomBitmap = BitmapFactory.decodeStream(getResources().openRawResource(mImgAry[0]));
            Matrix m = new Matrix();
            m.postScale(w * 1.00f / mBottomBitmap.getWidth(), h * 1.00f / mBottomBitmap.getHeight());
            mIvBottom.setImageMatrix(m);
            mIvBottom.setImageBitmap(mBottomBitmap);
            //这里的0~w 是要截取的宽度 不能大于原图位置
//          mBottomBitmap = Bitmap.createBitmap(mBottomBitmap, 0, 0, w, h, matrix, true);

            /* 上层bitmap需要是mutable的 */
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = true; //只读宽高边界
            mTopBitmap = BitmapFactory.decodeStream(getResources().openRawResource(mImgAry[1]), null, options);
            int imgw = options.outWidth; //=w  因为 layoutParams=-1
            int imgh = options.outHeight; //=h
            options.inJustDecodeBounds = false;
            options.inSampleSize = calculateInSampleSize(options, imgw, imgh);

            //缩放后的
            mTopBitmap = BitmapFactory.decodeStream(getResources().openRawResource(mImgAry[1]), null, options);


//            Bitmap bitmap = Bitmap.createBitmap(mTopBitmap, 0, 0, w, h);  //这返回不可变的 不行
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);//创建空的可变位图
            Canvas canvas = new Canvas(bitmap);
            Matrix matrix = new Matrix();
            float xRatio = w * 1.00f / mTopBitmap.getWidth();//若w>bitmapW，则放大；反之缩小
            float yRatio = h * 1.00f / mTopBitmap.getHeight();
            matrix.postScale(xRatio, yRatio);
            canvas.drawBitmap(mTopBitmap, matrix, null);
            mTopBitmap = bitmap;

            mIvTop.setImageBitmap(mTopBitmap);
            mIvTop.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * 计算 图片缩小比例
     *
     * @param options
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //先根据宽度进行缩小
        while (width / inSampleSize > reqWidth) {
            inSampleSize++;
        }
        //然后根据高度进行缩小
        while (height / inSampleSize > reqHeight) {
            inSampleSize++;
        }
        return inSampleSize;
    }

/*    private int RADIUS = 50;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                int touchX = (int) event.getX();
                int touchY = (int) event.getY();
                // 这个判断是计算边界，因为超过了边界为负值时会报错
                if (touchX >= RADIUS
                        & touchX <= mTopBitmap.getWidth() - RADIUS
                        & touchY >= RADIUS
                        & touchY <= mTopBitmap.getHeight() - RADIUS) {
                    for (int radius = 0; radius <= RADIUS; radius++) {//RADIUS半径内的像素点设为透明
                        for (double angle = 0; angle <= 360; angle++) {
                            double newX = touchX + radius * Math.cos(angle);
                            double newY = touchY + radius * Math.sin(angle);
                            mTopBitmap.setPixel((int) newX, (int) newY, Color.TRANSPARENT);
                        }
                    }
                }
                mIvTop.setImageBitmap(mTopBitmap);
                return  true;
        }
        return super.onTouchEvent(event);
    }*/

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();

                /*
                    以矩形方式改变位图
                    touch点为矩形中心，
                 */
                int left = x - mMove;
                int top = y - mMove;
                int right = x + mMove;
                int bottom = y + mMove;

                for (int i = left; i < right; i++) {
                    for (int j = top; j < bottom; j++) {
                        if (i >= 0 && j >= 0 && i <= mTopBitmap.getWidth() - mMove
                                && j <= mTopBitmap.getHeight() - mMove) {
                            mTopBitmap.setPixel(i, j, Color.TRANSPARENT);
                        }

                    }
                }
                mIvTop.setImageBitmap(mTopBitmap);
                return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
