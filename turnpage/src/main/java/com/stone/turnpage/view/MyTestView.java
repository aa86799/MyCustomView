package com.stone.turnpage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 测试canvas.translate(x,y)  canvas.scale(-1,1)
 * author : stone
 * email  : aa86799@163.com
 * time   : 2016/11/4 10 55
 */

public class MyTestView extends View {
    private int mW, mH;
    public MyTestView(Context context) {
        super(context);
    }

    public MyTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path p = new Path();
        p.moveTo(400, 400);
        p.lineTo(500, 500);
        p.lineTo(600, 400);
        p.close();

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawPath(p, paint);

        float x = (float) Math.sqrt(100*100*2);//左
        float y = (float) Math.sqrt(100*100*2);//右
        float c = (float) Math.sqrt(200*200*2);

        float sin = x / c;
        System.out.println("sin值" + sin);
        float aDegrees = (float) (Math.asin(sin) * 180 / Math.PI);
        System.out.println("角度" + aDegrees);

        canvas.save();
//        canvas.clipPath(p);
        canvas.translate(400, 400);
//        paint.setColor(Color.CYAN);
//        paint.setStrokeWidth(10);
//        canvas.drawLine(0, 0, 400, 400, paint);

        paint.setColor(Color.RED);
        canvas.drawPath(p, paint);

        canvas.rotate(aDegrees);//30度
        paint.setColor(Color.GRAY);
        canvas.drawPath(p, paint);

        canvas.restore();


        canvas.save();
        canvas.translate(400, 400);
        canvas.rotate(90 - aDegrees); //60度
        paint.setColor(Color.BLUE);
        canvas.drawPath(p, paint);
        canvas.restore();


        canvas.save();
        canvas.translate(400, 400);//原点移到(400,400)
        canvas.rotate(90 - aDegrees); //以该原点旋转
//        canvas.translate(0, 100);
        canvas.translate(0, -mH);
        canvas.scale(-1, 1);
        canvas.translate(-mW, 0);
        paint.setColor(Color.WHITE);
        canvas.drawPath(p, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(mW/2, mH/2);
        canvas.scale(-1, 1);//以原点上的y轴，向左翻转  x方向从右向左
        canvas.drawRect(new Rect(0, 0, mW / 4, mH), paint);
        paint.setColor(Color.BLUE);
        canvas.drawLine(50, 100, 120, 180, paint);

        paint.setColor(Color.LTGRAY);
//        canvas.scale(-1, 1);//以原点上的y轴，向左翻转
        canvas.drawRect(new Rect(mW / 4, 0, mW/2-50, mH), paint);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = getMeasuredWidth();
        mH = getMeasuredHeight();
    }
}
