package com.stone.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.stone.customview.R;
import com.stone.customview.util.PointUtil;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/5/28 17 59
 */
/*
根据父级指定的 图形可视区 点的绝对坐标，计算 其 相对坐标  进行绘制
计算图片与当前view的宽高比例， 缩放图片，以适应至少一条边=view的一条边，并且另一条边>=view的另一边
拖动子view时，判定图片是否超过多边形 可视区， 动画 回落

 */
public class MyImageLayout extends RelativeLayout {

    private PaintFlagsDrawFilter mDrawFilter;

    public MyImageLayout(Context context) {
        this(context, null);
    }

    public MyImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);

//        setBackgroundColor(getColor());



    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
//                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    private int mReallyIndex; //在父级中的child-index
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.setDrawFilter(mDrawFilter);

        Path path = new Path();

        int ratioX = getLeft(); //相对于屏幕原点
        int ratioY = getTop();

        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            int indexOfChild = ((ViewGroup) parent).indexOfChild(this);
            if (indexOfChild != -1) {
                mReallyIndex = indexOfChild;
//                System.out.println("mReallyIndex=>" + mReallyIndex);
            }
        }

        PointUtil instance = PointUtil.getInstance(getContext());
        List<PointUtil.MyPoint> reallyPoints = instance.getReallyPoints(mReallyIndex);

        if (reallyPoints != null) {
            for (int i = 0; i < reallyPoints.size(); i++) {
                PointUtil.MyPoint p = reallyPoints.get(i);
                if (i == 0) {
                    path.moveTo(p.x - ratioX, p.y - ratioY); //内部点实际坐标-顶点 即 相对于顶点的坐标
                } else {
                    path.lineTo(p.x - ratioX, p.y - ratioY);
                }

            }

/*      int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int min = Math.min(w, h);
        int r = min / 2; //边长
//        System.out.println("sin==" + Math.sin(StrictMath.toRadians(30)));
        //30度 60度 90度   边长比   1:根3:2
        int a = r / 2; //短边
        int b = (int) Math.sqrt(r * r - a * a); //长边
      for (int i = 0; i < 6; i++) {
            switch (i) {
                case 0:
                    path.moveTo(0, min / 2);
                    continue;
                case 1:
                    path.lineTo(a, (min / 2 - b));
                    continue;
                case 2:
                    path.lineTo(a + r, (min / 2 - b));
                    continue;
                case 3:
                    path.lineTo(min, min / 2);
                    continue;
                case 4:
                    path.lineTo(a + r, (min / 2 + b));
                    continue;
                case 5:
                    path.lineTo(a, (min / 2 + b));
                    continue;
            }
        }*/

            path.close();

//            canvas.clipPath(path, Region.Op.INTERSECT);//相交
//            canvas.clipPath(path, Region.Op.UNION);//并集
            canvas.clipPath(path, Region.Op.REPLACE);//替换
//            canvas.clipPath(path, Region.Op.DIFFERENCE);//差异 不同  显示不同区
//            canvas.clipPath(path, Region.Op.REVERSE_DIFFERENCE);//反向：差异 不同  如果前置了DIFFERENCE  可以通过该项 反转
//            canvas.clipPath(path, Region.Op.XOR);//异或集  相交区空出，其他区可见

            canvas.clipPath(path);//默认INTERSECT
            canvas.save();

            Paint paint = new Paint();
            paint.setColor(Color.argb(200, 125, 125, 125));//透明灰
//            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
//            paint.setStyle(Paint.Style.STROKE);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawPath(path, paint);
        canvas.restore();




        }



        super.dispatchDraw(canvas);



    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }


    float downx;
    float downy;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
//                setBackgroundColor(Color.argb(100, 125,125,125)); //设背景 灰色
                break;
            case MotionEvent.ACTION_MOVE:
                int movex = (int) event.getX();
                int movey = (int) event.getY();
                int dx = (int) (downx - movex);
                int dy = (int) (downy - movey);

                getChildAt(0).scrollBy(dx, dy);
                downx = movex;
                downy = movey;
//                ImageView iv;iv.setImageMatrix();
                break;
            case MotionEvent.ACTION_UP:
//                setBackgroundColor(0);  //背景无色
                //界限判定
                sureBounds();

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
        }
        return true;
    }

    private void sureBounds() {
        View view = getChildAt(0);
        if (view != null) {
            view.setDrawingCacheEnabled(true);
//            view.buildDrawingCache(); //可以不调用 get时会自动判断如果没有cache，会调用buildcache
            Bitmap drawingCache = view.getDrawingCache();
            if (drawingCache != null) {
                int imgw = drawingCache.getWidth();
                int imgh = drawingCache.getHeight();
                int w = getWidth();
                int h = getHeight();
                System.out.println(imgw + "," + imgh + "," +w + "," + h);
                /*
                192,221,190,221  高等
                 192,221,192,220 宽等
                 */
                System.out.println(view.getLeft() + "," + view.getTop());
                System.out.println(view.getWidth());

                System.out.println(((BitmapDrawable)((ImageView)view).getDrawable()).getBitmap().getWidth() + " 只"
                        + (view.getDrawingCache()==null));
                if (imgw == w) {

                }
                if (imgh == h) {

                }
                /*
                imageview的wh 大于等于当前layout  layout就是可视区
                拖动imageview后，改变的是谁？
                 */
            }
            view.setDrawingCacheEnabled(false);
        }


    }

    private int measureHeight(int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int result = 0; //结果
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:  // 子容器可以是声明大小内的任意大小
//                Log.e("", "子容器可以是声明大小内的任意大小");
//                Log.e(Tag, "大小为:"+specSize);
                result = specSize;
                break;
            case MeasureSpec.EXACTLY: //父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间.  比如EditTextView中的DrawLeft
//                Log.e(Tag, "父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间");
//                Log.e(Tag, "大小为:"+specSize);
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:  //父容器对于子容器没有任何限制,子容器想要多大就多大. 所以完全取决于子view的大小
//                Log.e(Tag, "父容器对于子容器没有任何限制,子容器想要多大就多大");
//                Log.e(Tag, "大小为:"+specSize);
                result = specSize;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 随机颜色
     *
     * @return
     */
    private int getColor() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String temp;
        //rgb三个区域
        for (int i = 0; i < 3; i++) {
            temp = Integer.toHexString(random.nextInt(0xFF));
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            sb.append(temp);
        }
        return Color.parseColor("#" + sb.toString());
    }
}
