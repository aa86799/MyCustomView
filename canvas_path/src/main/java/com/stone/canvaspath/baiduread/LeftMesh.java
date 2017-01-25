package com.stone.canvaspath.baiduread;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.stone.canvaspath.R;

/*add by liyun 20150327 add LeftMesh*/
public class LeftMesh extends View {
    private static final int WIDTH = InhaleMesh.HORIZONTAL_SPLIT;
    private static final int HEIGHT = InhaleMesh.VERTICAL_SPLIT;

    private Bitmap mBitmap;
    private Paint mPaint;
    private InhaleMesh mInhaleMesh;
    private int mPreIndex = 0;

    public LeftMesh(Context context) {
        super(context);
        InitView();

    }

    public LeftMesh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        InitView();
    }

    public LeftMesh(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        InitView();
    }

    private void InitView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.store_tab_classfiy);
        mPaint = new Paint();
        mInhaleMesh = new InhaleMesh(WIDTH, HEIGHT);
//        Log.i("yunli", "mBitmap.getWidth() = " + mBitmap.getWidth() +",mBitmap.getHeight() = " + mBitmap.getHeight());
        mInhaleMesh.setBitmapSize(270, 80);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        buildPaths(135, 40);
        buildMesh(135, 80);
    }

    public void drawMeshes(int index) {
        if (mPreIndex == index) {
            return;
        }
        mPreIndex = index;
        mInhaleMesh.buildPaths(index);
        mInhaleMesh.buildMeshes(index);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmapMesh(mBitmap,
                mInhaleMesh.getWidth(),
                mInhaleMesh.getHeight(),
                mInhaleMesh.getVertices(),
                0, null, 0, mPaint);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        Path[] paths = mInhaleMesh.getPaths();
        for (Path path : paths) {
            canvas.drawPath(path, mPaint);
        }
    }

    private void buildMesh(float w, float h) {
        mInhaleMesh.buildLeftMeshes(w, h);
    }

    private void buildPaths(float endX, float endY) {
        mInhaleMesh.buildPaths(endX, endY);
    }

}
