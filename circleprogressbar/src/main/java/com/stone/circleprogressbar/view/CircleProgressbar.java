package com.stone.circleprogressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.stone.circleprogressbar.R;


/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/9/14 09 10
 */
public class CircleProgressbar extends View {

    private float mProgress;
    private int mBarColor;
    private int mTextColor;
    private float mTextSize;

    public CircleProgressbar(Context context) {
        this(context, null);
    }

    public CircleProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressbar);
        mBarColor = array.getColor(R.styleable.CircleProgressbar_barColor, Color.GRAY);
        mTextColor = array.getColor(R.styleable.CircleProgressbar_textColor, Color.GRAY);
        mProgress = array.getFloat(R.styleable.CircleProgressbar_progress, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setProgress(float count) {
        mProgress = count;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        int strokeWidth = 20;
        int radius = w / 2 - strokeWidth / 2;//大圆 半径
        Bitmap barBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true); //抗锯齿
        paint.setStrokeWidth(strokeWidth); //描边宽
        paint.setDither(true); //防抖动

        /* 外边框 */
        Canvas circleCanvas = new Canvas(barBitmap);
        RectF rect = new RectF(0, 0, w, h);
        paint.setColor(Color.argb(0x11, 0xcc, 0xcc, 0xcc));
//        circleCanvas.drawRect(rect, paint); //没啥用  只是看外边框的

        /* 内圆 */
        paint.setColor(Color.CYAN);
        paint.setShader(new LinearGradient(0, 0, w, h, Color.RED, Color.GREEN, Shader.TileMode.CLAMP));
        circleCanvas.drawCircle(w / 2, h / 2, radius - strokeWidth / 2, paint);
        paint.setShader(null);

        /* 外圆 */
        paint.setColor(mBarColor);
        paint.setStyle(Paint.Style.STROKE);
        circleCanvas.drawCircle(w / 2, h / 2, radius, paint);

        /* 圆弧 */
        paint.setShader(new LinearGradient(0, 0, w, h,
                new int[]{Color.GREEN, Color.MAGENTA, Color.CYAN, Color.RED},
                new float[]{0.2f, 0.5f, 0.7f, 1.0f}, Shader.TileMode.CLAMP));
        float cur = mProgress * 360 * 0.01f;
        circleCanvas.drawArc(new RectF(strokeWidth / 2, strokeWidth / 2, w - strokeWidth / 2, h - strokeWidth / 2),
                360 - 45, cur, false, paint);
        paint.setShader(null);

        /* 文本 */
        paint.setColor(mTextColor);

        paint.setTextAlign(Paint.Align.LEFT);//default
        String percent = mProgress + "%";
        if (mTextSize == 0) {
            calcTextSize(paint, w);
        } else {
            paint.setTextSize(mTextSize);
        }
        paint.setStyle(Paint.Style.FILL);
        circleCanvas.drawText(percent, w / 2 - paint.measureText(percent) / 2, h / 2 + paint.getTextSize() / 2, paint);

        canvas.drawBitmap(barBitmap, 0, 0, paint);
    }

    /**
     * 计算并设置最适合的textSize
     *
     * @param paint
     * @param max    最大宽度
     */
    private void calcTextSize(Paint paint, int max) {
        float width = paint.measureText("99.99%");
        while (width < max * 3 / 5) {
            paint.setTextSize(paint.getTextSize() + 5);
            width = paint.measureText("92.88%");
        }
        mTextSize = paint.getTextSize();
    }

}
