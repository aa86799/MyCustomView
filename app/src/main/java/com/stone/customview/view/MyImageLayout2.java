package com.stone.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.stone.customview.util.PointUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
public class MyImageLayout2 extends View {

    private PaintFlagsDrawFilter mDrawFilter;
    private Bitmap mImgitmap;

    public MyImageLayout2(Context context) {
        this(context, 0);
    }
    public MyImageLayout2(Context context, int resid) {
        super(context);
        mImgitmap = createBitmap(resid);
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
//        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    private int mReallyIndex; //在父级中的child-index
    @Override
    protected void onDraw(Canvas canvas) {
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
            paint.setColor(Color.argb(20, 125, 125, 125));//透明灰
//            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
//            paint.setStyle(Paint.Style.STROKE);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawPath(path, paint);
            canvas.restore();

            canvas.drawBitmap(mImgitmap, 0, 0, null);

        }

        super.dispatchDraw(canvas);
    }

    List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    public Bitmap createBitmap(int resid) {
        InputStream is = this.getResources().openRawResource(resid);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options); //图片信息 保存到options

        float imgw = options.outWidth;
        float imgh = options.outHeight;

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
//        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap sharkBitmap = sharkColor(bitmap);
        bitmaps.add(sharkBitmap);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return sharkBitmap;

//        BitmapDrawable bd = (BitmapDrawable) iv.getDrawable();
//        System.out.println(ivw+"," + bd.getBitmap().getWidth());
    }

    int ar = new Random().nextInt(2);
    int nr = new Random().nextInt(3);
    int ng = new Random().nextInt(3);
    int nb = new Random().nextInt(3);
    private Bitmap sharkColor(Bitmap mBitmap) {
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        int[] mArrayColor = new int[mArrayColorLengh];
        int count = 0;
        int random = new Random().nextInt(10000) + 10000;
        for (int i = 0; i < mBitmapHeight % random; i++) {//高(宽循环) =》一行一行
            for (int j = 0; j < mBitmapWidth % random; j++) {
                //获得Bitmap 图片中每一个点的color颜色值
                int color = mBitmap.getPixel(j, i);

                //将颜色值存在一个数组中 方便后面修改
//                mArrayColor[count] = color;

                //如果你想做的更细致的话 可以把颜色值的A R G B 拿到做响应的处理
                int a = Color.alpha(color);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                /*
                r 变小 偏青
                g 变小 偏洋红
                b 变小 偏黄
                 */

                int newColor = Color.argb(a>>ar, r>>nr, g>>ng, b>>nb);
                mArrayColor[count] = newColor;

                count++;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(mArrayColor, mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_4444);
        return bitmap;
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

                scrollBy(dx, dy);
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
        View view = this;
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
