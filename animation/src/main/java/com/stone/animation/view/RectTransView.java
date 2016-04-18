package com.stone.animation.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;

/**
 * 按当前view的宽高进行矩形移动 内容图
 *
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/4/15 10 18
 */
public class RectTransView extends View {

    private Bitmap mBitmap;

    private int bw;
    private int bh;
    private int l, t;
    private int speed = 10;
    private int state;
    private float degress;

    public RectTransView(Context context) {
        super(context);
//		setBackgroundColor(Color.parseColor("#dcaa0000"));
    }

    public void setBitmapResid(int resid) {
        mBitmap = BitmapFactory.decodeStream(getResources().openRawResource(resid));
        bw = mBitmap.getWidth();
        bh = mBitmap.getHeight();

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmap == null) {
            return;
        }
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        Matrix matrix;
        speed = (int) (Math.random() * 20);
        switch (state) {
            case 0: {
                if (l + speed <= w - bw) {
                    l += speed;
                } else {
                    l = w - bw;
                    state = 1;
                }
            }
            break;

            case 1: {
                if (t + speed <= h - bh) {
                    t += speed;
                } else {
                    t = h - bh;
                    state = 2;
                }
            }
            break;

            case 2: {
                if (l > speed) {
                    l -= speed;
                } else {
                    l = 0;
                    state = 3;
                }

            }
            break;

            case 3: {
                if (t > speed) {
                    t -= speed;
                } else {
                    t = 0;
                    state = 0;
                }
            }
            break;
        }

        matrix = new Matrix();
        matrix.postTranslate(l, t);
        degress += 4;
        matrix.postRotate(degress);

        Bitmap createBitmap = Bitmap.createBitmap(mBitmap, 0, 0, bw, bh, matrix, true);

        canvas.drawBitmap(createBitmap, null, new Rect(l, t, l + bw, t + bh), null);

        invalidate();
    }
}
