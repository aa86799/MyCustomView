package com.stone.roulette.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class RoundMenuView extends ImageView implements GestureDetector.OnGestureListener {
    private static final int childMenuSize = 8;
    private static final float childAngle = 360f / childMenuSize;
    private float offsetAngle = 0;
    private Paint paint;
    private GestureDetector gestureDetector;
    private int selectId = -1;

    public RoundMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        gestureDetector = new GestureDetector(this);
    }


    /**
     * 画扇形
     *
     * @param canvas
     * @param rectF
     */
    private void drawArc(Canvas canvas, RectF rectF) {

        for (int i = 0; i < childMenuSize; i++) {
            paint.setColor(Color.BLUE);

            if (i == selectId) { //如果是选中就将扇形画成实心的,否则画空心的扇形
                paint.setStyle(Paint.Style.FILL);
                canvas.drawArc(rectF, i * childAngle + offsetAngle, childAngle,
                        true, paint);
            } else {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rectF, i * childAngle + offsetAngle, childAngle,
                        true, paint);
            }

            //计算扇形中心点的坐标
            double x = 200 + getRoundX(200f / 3 * 2, i, childMenuSize, offsetAngle + childAngle / 2);
            double y = 200 + getRoundY(200f / 3 * 2, i, childMenuSize, offsetAngle + childAngle / 2);

            String str = "菜单" + i;
            //计算文字宽高
            Rect rect = new Rect();
            paint.getTextBounds(str, 0, str.length(), rect);
            int strW = rect.width();
            int strH = rect.height();
            paint.setColor(Color.WHITE);
            paint.setTextSize(18);
            canvas.drawText(str, (float) x - strW / 2, (float) y - strH / 2, paint);

        }
    }

    /**
     * 计算圆形等分扇形的点Y坐标
     *
     * @param r            圆形直径
     * @param i            第几个等分扇形
     * @param n            等分扇形个数
     * @param offset_angle 与y轴偏移角度
     * @return Y坐标
     */
    private double getRoundY(float r, int i, int n, float offset_angle) {
        return r * Math.sin(i * 2 * Math.PI / n + Math.PI / 180 * offset_angle);
    }

    /**
     * 计算圆形等分扇形的点X坐标
     *
     * @param r            圆形直径
     * @param i            第几个等分扇形
     * @param n            等分扇形个数
     * @param offset_angle 与X轴偏移角度
     * @return x坐标
     */
    private double getRoundX(float r, int i, int n, float offset_angle) {
        return r * Math.cos(i * 2 * Math.PI / n + Math.PI / 180 * offset_angle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectId = whichSector(event.getX() - 200, event.getY() - 200, 200);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        RectF rectF = new RectF(0, 0, 400, 400);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.LTGRAY);
        canvas.drawCircle(200, 200, 200, paint);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(200, 200, 190, paint);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(200, 200, 180, paint);

        // 画空心扇形
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        drawArc(canvas, new RectF(20, 20, 380, 380));

        // 画中心外圆
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(200, 200, 25, paint);

        // 画三角形
        Path path = new Path();
        path.moveTo(175, 200);// 此点为多边形的起点
        path.lineTo(225, 200);
        path.lineTo(200, 240);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);

        // 画中心内圆
        paint.setColor(Color.MAGENTA);
        canvas.drawCircle(200, 200, 20, paint);


    }

    /**
     * 计算两个坐标点与圆点之间的夹角
     *
     * @param px1 点1的x坐标
     * @param py1 点1的y坐标
     * @param px2 点2的x坐标
     * @param py2 点2的y坐标
     * @return 夹角度数
     */
    private double calculateScrollAngle(float px1, float py1, float px2,
                                        float py2) {
        double radian1 = Math.atan2(py1, px1);
        double radian2 = Math.atan2(py2, px2);
        double diff = radian2 - radian1;
        return Math.round(diff / Math.PI * 180);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        float tpx = e2.getX() - 200;
        float tpy = e2.getY() - 200;
        float disx = (int) distanceX;
        float disy = (int) distanceY;
        double scrollAngle = calculateScrollAngle(tpx, tpy, tpx + disx, tpy
                + disy);
        offsetAngle -= scrollAngle;
        selectId = whichSector(0, 40, 200);//0,40是中心三角定点相对于圆点的坐标
        invalidate();
        Log.e("CM", "offsetAngle:" + offsetAngle);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        return false;
    }


    /**
     * 计算点在那个扇形区域
     *
     * @param X
     * @param Y
     * @param R 半径
     * @return
     */
    private int whichSector(double X, double Y, double R) {

        double mod;
        mod = Math.sqrt(X * X + Y * Y); //将点(X,Y)视为复平面上的点，与复数一一对应，现求复数的模。
        double offset_angle;
        double arg;
        arg = Math.round(Math.atan2(Y, X) / Math.PI * 180);//求复数的辐角。
        arg = arg < 0 ? arg + 360 : arg;
        if (offsetAngle % 360 < 0) {
            offset_angle = 360 + offsetAngle % 360;
        } else {
            offset_angle = offsetAngle % 360;
        }
        if (mod > R) { //如果复数的模大于预设的半径，则返回0。
            return -2;
        } else { //根据复数的辐角来判别该点落在那个扇区。
            for (int i = 0; i < childMenuSize; i++) {
                if (isSelect(arg, i, offset_angle) || isSelect(360 + arg, i, offset_angle)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 判读该区域是否被选中
     *
     * @param arg         角度
     * @param i
     * @param offsetAngle 偏移角度
     * @return 是否被选中
     */
    private boolean isSelect(double arg, int i, double offsetAngle) {
        return arg > (i * childAngle + offsetAngle % 360) && arg < ((i + 1) * childAngle + offsetAngle % 360);
    }

}