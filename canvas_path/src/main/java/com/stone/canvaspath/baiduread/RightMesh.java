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

/*add by liyun 20150327 add rightMesh*/
public class RightMesh extends View {


    private static final int WIDTH = InhaleRightMesh.HORIZONTAL_SPLIT;
    private static final int HEIGHT = InhaleRightMesh.VERTICAL_SPLIT;

    private Bitmap mBitmap;
    private Paint mPaint;
    private InhaleRightMesh mInRighthaleMesh;
    private int mPreIndex = 0;

    public RightMesh(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView();
    }

    public RightMesh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView();
    }

    public RightMesh(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView();
    }

    public void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.bt_animator_main);

        mPaint = new Paint();
        mInRighthaleMesh = new InhaleRightMesh(WIDTH, HEIGHT);
        mInRighthaleMesh.setBitmapSize(mBitmap.getWidth(), mBitmap.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        buildPaths(mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        buildMesh(mBitmap.getWidth() / 2, mBitmap.getHeight());
    }

    public void drawMeshes(int index) {
        if (mPreIndex == index) {
            return;
        }
        mPreIndex = index;
        mInRighthaleMesh.buildPaths(index);
        mInRighthaleMesh.buildMeshes(index);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmapMesh(mBitmap,
                mInRighthaleMesh.getWidth(),
                mInRighthaleMesh.getHeight(),
                mInRighthaleMesh.getVertices(),
                0, null, 0, mPaint);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        Path[] paths = mInRighthaleMesh.getPaths();
        for (Path path : paths) {
            canvas.drawPath(path, mPaint);
        }
    }

    private void buildMesh(float w, float h) {
        mInRighthaleMesh.buildRightMeshes(w, h);
    }

    private void buildPaths(float endX, float endY) {
        mInRighthaleMesh.buildPaths(endX, endY);
    }

}