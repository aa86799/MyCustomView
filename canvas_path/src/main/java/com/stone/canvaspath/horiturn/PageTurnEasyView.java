package com.stone.canvaspath.horiturn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/6/23 15 07
 */
public class PageTurnEasyView extends View {

    private Bitmap foreImage;
    private Bitmap bgImage;
    private PointF touchPt;
    private GradientDrawable shadowDrawableLeft;
    private GradientDrawable shadowDrawableRight;
    private ColorMatrixColorFilter mColorMatrixFilter;
    private Scroller mScroller;
    private int lastTouchX;

    public PageTurnEasyView(Context context) {
        this(context, null);
    }

    public PageTurnEasyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageTurnEasyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        touchPt = new PointF(-1, -1);

        //ARGB A(0-透明,255-不透明)
        int[] color = {0xb0abc777, 0x00000000};
//        int[] color = {0xb0333333, 0x00333333};
        shadowDrawableLeft = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color);
        shadowDrawableLeft.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        shadowDrawableRight = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
        shadowDrawableRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        float array[] = {
                1, 0, 0, 0, 0,     //R
                1, 0, 0, 0, 0,     //G
                1, 0, 0, 0, 0,     //B
                0.5f,  0, 0, 0, 0}; //A
        ColorMatrix cm = new ColorMatrix();
        cm.set(array);

//      cm.setSaturation(0);
        mColorMatrixFilter = new ColorMatrixColorFilter(cm);
        //利用滚动条来实现接触点放开后的动画效果
        mScroller = new Scroller(context);
    }


    public Bitmap getForeImage() {
        return foreImage;
    }

    public void setForeImage(Bitmap foreImage) {
        this.foreImage = foreImage;
    }

    public Bitmap getBgImage() {
        return bgImage;
    }

    public void setBgImage(Bitmap bgImage) {
        this.bgImage = bgImage;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        drawPageEffect(canvas);
    }

    /**
     * 画前景图片
     *
     * @param canvas
     */
    private void drawForceImage(Canvas canvas) {
        // TODO Auto-generated method stub

        if (foreImage != null) {
            canvas.drawBitmap(foreImage, 0, 0, null);
        }
    }

    /**
     * 画背景图片
     *
     * @param canvas
     */
    private void drawBgImage(Canvas canvas, Path path) {
        // TODO Auto-generated method stub
        if (bgImage != null) {
            canvas.save();

            //只在与路径相交处画图
//            canvas.clipPath(path, Region.Op.INTERSECT);
            canvas.clipPath(path);
            canvas.drawBitmap(bgImage, 0, 0, null);
            canvas.restore();
        }
    }

    /**
     * 画翻页效果
     *
     * @param canvas
     */
    private void drawPageEffect(Canvas canvas) {
        // TODO Auto-generated method stub
        drawForceImage(canvas); //绘制前图
        Paint mPaint = new Paint();
        int w = getWidth();
        int h = getHeight();
        if (touchPt.x != -1 && touchPt.y != -1) {
            //翻页左侧书边  这竖线没啥用
//            mPaint.setColor(Color.RED);
//            canvas.drawLine(touchPt.x, 0, touchPt.x, screenHeight, mPaint);

            //左侧书边画阴影
            shadowDrawableLeft.setBounds((int) touchPt.x - 50, 0, (int) touchPt.x, h);
            shadowDrawableLeft.draw(canvas);

            //翻页右边对折处
            float halfCut = touchPt.x + (w - touchPt.x) / 2;
            canvas.drawLine(halfCut, 0, halfCut, h, mPaint);

            //对折处左侧画翻页图片背面
            Rect backArea = new Rect((int) touchPt.x, 0, (int) halfCut, h);
            Paint backPaint = new Paint();
//            backPaint.setColor(0xffdacab0);
            backPaint.setColor(0xffff0000);
            canvas.drawRect(backArea, backPaint);

            //将翻页图片正面进行处理水平翻转并平移到touchPt.x点
            Paint fbPaint = new Paint();
            fbPaint.setColorFilter(mColorMatrixFilter);
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1);//水平翻转镜像
            /*
            在裁剪区域内绘制 水平镜像图 canvas中的matrix的scale|rotate|skew都会进行位移
            scale x = -1;  类似页面整体往左翻
            所以要右移width+touchX
             */
            matrix.postTranslate(foreImage.getWidth() + touchPt.x, 0);

            canvas.save();//保存图层
//            canvas.clipRect(backArea, Region.Op.INTERSECT);
            canvas.clipRect(backArea); //剪切区域 剪切后的坐标相对值不会被影响
            canvas.drawBitmap(foreImage, matrix, fbPaint); //
//            canvas.drawBitmap(foreImage, matrix, null);
            //
//            canvas.drawBitmap(foreImage, null, new RectF(200, 0, screenWidth, screenHeight), fbPaint);
            canvas.restore(); //恢复到最近保存的图层

            Path bgPath = new Path();
            //可以显示背景图的区域
            bgPath.addRect(new RectF(halfCut, 0, w, h), Path.Direction.CW);
            //对折出右侧画背景
            drawBgImage(canvas, bgPath);

            //对折处画右侧阴影
            shadowDrawableRight.setBounds((int) halfCut, 0, (int) halfCut + 50, h);
            shadowDrawableRight.draw(canvas);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchPt.x = event.getX();
            touchPt.y = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            lastTouchX = (int) touchPt.x;
            touchPt.x = event.getX();
            touchPt.y = event.getY();

            postInvalidate();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            int dx, dy;

            dy = 0;

            //向右滚动
            if (lastTouchX < touchPt.x) {//当前点大
                dx = (int) ((foreImage.getWidth() - touchPt.x) + 50);
            } else {
                //向左滑动
                /*
                scroll时   scroll.finalX = touchP.x + dx
                若touch700 width 1000,  则 finalX= -300  这样是不对的
                 */
//                dx = - foreImage.getWidth();

                /*
         dx = - touchPt.x;  则最后touchPt.x=0; 会绘制一半的对折
         dx = -halfCut;   还是上面的数据 halfCut = -850; finalX =-150
            新的halfCut = touchPt.x + (w - touchPt.x) / 2 = -150+555=405  也不对的 ,要求最后halfCut = 0
            0 = touchPt.x + (w - touchPt.x) / 2 => w-x=-2x=> w=-x=> tx=-w

            又因 tx : tx+dx;  即求  tx + dx = -w;  dx = -w-tx
                 */
                dx = (int) (-touchPt.x - foreImage.getWidth() - 50);
            }

            mScroller.startScroll((int) touchPt.x, (int) touchPt.y, dx, dy, 1000);
            postInvalidate();
        }
        //必须为true，否则无法获取ACTION_MOVE及ACTION_UP事件
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            touchPt.x = mScroller.getCurrX();
            touchPt.y = mScroller.getCurrY();

            postInvalidate();
        }
        super.computeScroll();
    }
}
