package com.stone.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/6/23 11 33
 */
public class TestCanvasView extends View {


    public TestCanvasView(Context context) {
        super(context);
    }

    public TestCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.save();
        int px = getMeasuredWidth();
        int py = getMeasuredHeight();
        canvas.rotate(90, px / 2, py / 2);  //顺时针旋转90˚
//        canvas.rotate(90);

// Draw up arrow  因为canvas旋转了90度，而下面的绘制的坐标也旋转了
        canvas.drawLine(px / 2, 0, 0, py / 2, paint); //左斜线
        canvas.drawLine(px / 2, 0, px, py / 2, paint);//右斜线
        canvas.drawLine(px / 2, 0, px / 2, py, paint);//中间竖直线

        canvas.restore(); //恢复到之前未旋转的状态，已绘制的不变

        System.out.println("cout:zz::" + canvas.getSaveCount());

        canvas.save();
        canvas.scale(0.5f, 0.5f, px / 2, py / 2);
//        canvas.scale(0.5f, 0.5f, 100, 100);
//        canvas.scale(0.5f, 0.5f);

        paint.setColor(Color.BLUE);
        canvas.drawLine(px / 2, 0, 0, py / 2, paint); //左斜线
        canvas.drawLine(px / 2, 0, px, py / 2, paint);//右斜线
        canvas.drawLine(px / 2, 0, px / 2, py / 2, paint);//中间竖直线

        canvas.restore(); //恢复到之前未旋转的状态，已绘制的不变

// Draw circle
        canvas.drawCircle(px - 50, py - 50, 10, paint);//偏右下 如果不恢复canvas状态，那就偏左下了

//        canvas.drawPicture(Picture);


//        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(75, 75, 75, paint);

        canvas.saveLayerAlpha(0, 0, 180, 180, 0x66, LAYER_FLAGS);
        canvas.translate(30, 30);
        paint.setColor(Color.RED);
        canvas.drawCircle(75, 75, 75, paint);
        canvas.restore();

//        canvas.saveLayer(0, 0, 200, 200, paint, LAYER_FLAGS);
        canvas.saveLayerAlpha(0, 0, 200, 200, 0x44, LAYER_FLAGS);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(125, 125, 75, paint);
        canvas.restore();


        canvas.saveLayerAlpha(75, 75, 185 + 75, 185 + 70, 0x88, LAYER_FLAGS);
        paint.setColor(Color.CYAN);
        canvas.drawCircle(185, 185, 75, paint);
        paint.setColor(Color.MAGENTA);
        canvas.drawCircle(260, 260, 75, paint);
        canvas.restore();

        canvas.drawRoundRect(new RectF(200, 10, 400, 80), 10, 80, paint);
        System.out.println("cout:::" + canvas.getSaveCount());

        canvas.drawRect(300, 300, 500, 500, paint);
        canvas.save();
        canvas.skew(0.6f, 0);//sx或sy为倾斜角度的tan值， 在x方向上倾斜45度 >> tan(45) = 1  0.6=tan(31˚)
        canvas.drawRect(300, 300, 500, 500, paint);
        canvas.restore();

        canvas.save();
        canvas.skew(-0.5f, 0.5f);//sx或sy为倾斜角度的tan值
        canvas.drawRect(300, 300, 500, 500, paint);
        canvas.restore();


//        canvas.drawRect(0,0,100,50,paint);
        canvas.save();
//        canvas.translate(100, 100);
//        canvas.drawRect(0,600,300,700,paint);
        canvas.rotate(90);
//        canvas.translate(-100,-100);
        canvas.drawRect(0, 0, 100, 50, paint);
        canvas.restore();

//        canvas.save();
//        canvas.rotate(180);
//        canvas.drawRect(-200,-200,100,50,paint);
//        canvas.restore();

        paint.setColor(Color.BLACK);
        canvas.save();
//        canvas.rotate(90, px/2, py/2);
//        canvas.scale(0.5f,0.5f); //
        canvas.drawLine(0, 0, 100, 500, paint);

        canvas.translate(50, 50);
        canvas.scale(0.5f, 0.5f);
        paint.setColor(Color.BLUE);
        canvas.drawLine(0, 0, 100, 500, paint);

        canvas.translate(-50, -50);
//        canvas.scale(0.5f, 0.5f, 50, 50);
//        canvas.drawRect(0,0,100,50,paint);
        paint.setColor(Color.CYAN);
        canvas.drawLine(0, 0, 100, 500, paint);

        canvas.restore();

//        canvas.save();
//        Region region = new Region();
//        region.set(new Rect(150, 0, 600, 700));
//        region.op(new Rect(0,0, 200, 300), Region.Op.REPLACE);
//        canvas.clipRegion(region); //裁剪范围
//        canvas.drawColor(Color.RED);//将bitmap绘制到相应的region
//        canvas.restore();

    }

    int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
            | Canvas.CLIP_TO_LAYER_SAVE_FLAG; //Canvas.ALL_SAVE_FLAG
}
