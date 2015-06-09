package com.stone.customview;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.stone.customview.model.PuzzleBean;
import com.stone.customview.util.JsonLoad;
import com.stone.customview.util.PointUtil;
import com.stone.customview.view.BackView;
import com.stone.customview.view.MyImageLayout;
import com.stone.customview.view.PuzzleLayout;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/5/28 09 55
 */
public class PuzzleActivity extends Activity {

    List<ImageView> imgViewList;
    BackView backView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.puzzle);

        int sw = getResources().getDisplayMetrics().widthPixels;
        int sh = getResources().getDisplayMetrics().heightPixels;

        backView = new BackView(this);
        setContentView(backView);

        imgViewList = new ArrayList<ImageView>();

        PointUtil instance = PointUtil.getInstance(this);
        List<List<PointUtil.MyPoint>> pointsScale = instance.getPointsScale();
        MyImageLayout imageLayout;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
        final Random random = new Random();
        for (int i = 0; i < pointsScale.size(); i++) {
            imageLayout = new MyImageLayout(this);
            backView.addView(imageLayout);

            final ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageLayout.addView(imageView);
            //CENTER_CROP使用图片一边等于view的一边，另一边大于或等于， 居中显示
            //CENTER_INSIDE                    , 另一边小于或等于， 居中显示
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();

            /*
            这里imageview被 父级 -> imagelayout measure后 才有宽高
             */
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    switch (random.nextInt(3)) {
                        case 0:
                            setImg(imageView, R.drawable.a1);
                            break;
                        case 1:
                            setImg(imageView, R.drawable.a2);
                            break;
                        default:
                            setImg(imageView, R.drawable.a3);
                            break;
                    }

                    return true;
                }
            });

            /*
            这里imageview被 父级 -> imagelayout measure后 才有宽高  监听这个 没用
             */
           /* viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setImg(imageView, R.drawable.a1);
                    System.out.println("bbb" + imageView.getMeasuredWidth());
                }
            });*/

            imgViewList.add(imageView);
        }
    }

    private int getImageByReflect(String imageName) {
        try {
            Field field = Class.forName(getPackageName()+".R$drawable").getField(imageName);
            return field.getInt(field);
        } catch (Exception e) {

        }
        return 0;
    }

    List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    public void setImg(ImageView iv, int resid) {
        InputStream is = this.getResources().openRawResource(resid);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options); //图片信息 保存到options

        float ivw = iv.getWidth();
        float ivh = iv.getHeight();
        float imgw = options.outWidth;
        float imgh = options.outHeight;

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap sharkBitmap = sharkColor(bitmap);
        iv.setImageBitmap(sharkBitmap);
        bitmaps.add(sharkBitmap);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

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
    protected void onDestroy() {
        super.onDestroy();
        for (Bitmap bitmap : bitmaps) {
            bitmap.recycle();
            bitmap = null;
        }
        bitmaps.clear();
        bitmaps = null;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        int childCount = backView.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            MyImageLayout child = (MyImageLayout) backView.getChildAt(i);
//            ImageView imageView = (ImageView) child.getChildAt(0);
//            setImg(imageView, R.drawable.a1);
//            System.out.println("bbb" + child.getChildAt(0).getMeasuredWidth());
//        }
    }

    /*
        外层 不规则：自定义view，绘制点成图
        它应该是一个viewgroup  内部再放置imageview

         */

    //提示
    public static int[] images = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4,
            R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9};

}
