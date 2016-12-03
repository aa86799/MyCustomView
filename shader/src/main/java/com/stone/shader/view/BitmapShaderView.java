package com.stone.shader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.stone.shader.R;

import java.io.InputStream;

/**
 * desc   : 通过canvas绘制图形，paint中设置了BitmapShader
 * author : stone
 * email  : aa86799@163.com
 * time   : 2016/11/28 16 44
 */
public class BitmapShaderView extends View {

    private Bitmap mSrcBitmap;
    private BitmapShader mShader, mShaderScale;
    private Paint mPaint;
    private int mBmW, mBmH, mMin;
    private ShapeDrawable mDrawable;
    private int mRadius;
    private Matrix matrix;
    private int mScale;

    public BitmapShaderView(Context context) {
        this(context, null);
    }

    public BitmapShaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        InputStream is = getResources().openRawResource(R.drawable.mn);
        mSrcBitmap = BitmapFactory.decodeStream(is);
        //如果使用decodeResource，可能造成图片缩放。如图片放在hdpi，而设备是xhdpi，就会放大
//        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mn);
        mBmW = mSrcBitmap.getWidth();
        mBmH = mSrcBitmap.getHeight();
        mMin = Math.min(mBmW, mBmH);
        System.out.println("w" + mBmW + ",h" + mBmH);

        mShader = new BitmapShader(mSrcBitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);

        mPaint = new Paint();
        mPaint.setShader(mShader);

        mRadius = mMin / 3;

        mScale = 2;

        mDrawable = new ShapeDrawable(new OvalShape());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mSrcBitmap, mBmW * mScale, mBmH * mScale, true);
        mShaderScale = new BitmapShader(scaledBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);//缩放
//        mShaderScale = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);//缩放
        mDrawable.getPaint().setShader(mShaderScale);

        matrix = new Matrix();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawBitmap(mSrcBitmap, 0, 0, null); //仅原图

//        canvas.drawBitmap(mSrcBitmap, 0, 0, mPaint); //使用带有shader的paint,这跟上面是一样的效果 一般不这么用

        /*
        绘制图形时，使用了带shader的paint，那么图形区中就有对应的shader效果
        默认：canvas的起始点，即对应 BitmapShader中图片的起始点
            在不同的坐标点绘制图形，即可绘制上对应坐标区域内BitmapShader中的图片
         */
//        canvas.translate(300, 300);
//        canvas.drawRect(new Rect(0, 0, mBmW/2, mBmH/2), mPaint);

//        canvas.drawOval(new RectF(mBmW/2 + 20, mBmW/2, mBmW, mBmH), mPaint);

//        canvas.drawCircle(mBmW/2, mBmH/2, mMin/2, mPaint);

//        canvas.drawRect(0, 0, mBmW, mBmH, mPaint); //原图

        //在下方绘制原图
//        canvas.drawBitmap(mSrcBitmap, 0, 100, null);
        canvas.drawBitmap(mSrcBitmap, 0, mMin + mBmH/2 + 10, null);
        /*
        绘制使用了BitmapShader的 ShapeDrawable
        绘制到canvas上的表现：以bounds为区域进行绘制，主要是显示这一块区域在canvas上
            但是内容，不是对应坐标区的图片上的内容；而是默认从图片(0,0)开始，相同大小形状的一块区域
            所以如果要显示对应这区域的，就要使用shader有setLocalMatrix(matrix)进行平移
         */
        mDrawable.draw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        /*
        当shader的图片，相对于自身，平移到(-a,-b)时，这时的原点显示shader中原图的(a,b)位置内容
        测试时，配合  canvas.drawRect(0, 0, mBmW, mBmH, mPaint);
         */
//        matrix.setTranslate(-mBmW/2, -mBmH/2);

        /*
        测试时使mScale = 1， 配合 canvas.drawRect(0, 0, mBmW, mBmH, mPaint);
        没有缩放, 进行平移后，即drawable-bounds的起始点变化到平移点，再以下方的范围来绘制
        为什么要用半径减去x、y：
            若直接使用(-x, -y)，即平移后，显示触摸点及之下的内容(当然，如果触摸点在图片之外时，显示的内容受Shader.TileMode影响)
            (-x, -y)现在是原点，要使其在显示内容的中心点，即要使其x方向、y方向都加上一个半径值:（-x+r, -y+r）
         */
//        matrix.setTranslate(mRadius - x, mRadius - y);


        /*
        当： mScale = 1, 且 要使原图在(0, mMin + mBmH/2 + 10)位置开始显示
            即要平移到：(r - x,  -y + r + (mMin + mBmH/2 + 10);
         */
//        matrix.setTranslate(mRadius - x, mRadius - y + (mMin + mBmH/2 + 10));

        /*
        如果在上面的基础上再进行了缩放 mScale >= 2; 且canvas.drawBitmap(mSrcBitmap, 0, mMin + mBmH/2 + 10, null);
        同时配合TileMode.REPEAT来实现以下方式：
         */
//        int sy = 100 % mBmH;//测试下面的算法，配合canvas.drawBitmap(mSrcBitmap, 0, 100, null);
       int sy = (mMin + mBmH/2 + 10) % mBmH;
        /*
        同理，如果绘制原图时，有x值，要用 sx = x % mBmW;
         */
//        matrix.setTranslate(mRadius - x * mScale, mRadius - y  * mScale);//如果原图的绘制在x、y上都等于0的情况下
        matrix.setTranslate(mRadius - x * mScale, mRadius - y * mScale + sy * mScale);

        mDrawable.getPaint().getShader().setLocalMatrix(matrix);

        /*
         设置一个以(x,y)为中点的正方形范围-内容区，仅指大小；边长为mRadius
         */
        mDrawable.setBounds(x - mRadius, y - mRadius, x + mRadius, y + mRadius);

        invalidate();
        return true;
    }

}
