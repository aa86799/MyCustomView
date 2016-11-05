package com.stone.turnpage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;


/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/9/20 11 18
 * <p>
 * 折线翻页
 */

public class FoldTurnPageView extends View {

    private float mTouchX, mTouchY;
    private float mTouchUpX, mTouchUpY;//touch-up 时点的坐标
    private Path mPath;
    private Path mPathFoldAndNext;// 一个包含折叠和下一页区域的Path
    private Paint mPaint;
    private int mW, mH;
    private Region mRegionShortSize; //touch点所在范围区
    private Rect mCurRect; //当前页区域，其实就是控件的大小
    private int mBuffArea = 20; //底部缓冲区
    private float mAutoAreaRight, mAutoAreaBottom, mAutoAreaLeft;
    private boolean mIsSlide; //是否自动滑动

    private static final int LEFT_BOTTOM = 1;  //左下
    private static final int RIGHT_BOTTOM = 2; //右下
    @IntDef({LEFT_BOTTOM, RIGHT_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SlideDirection {}
    @SlideDirection
    private int mSlide;

    private static final int LONG = 1; //长边 用于折叠区 镜像计算
    private static final int SHORT = 2; //短边
    @IntDef({LONG, SHORT})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Ratio {}
    @Ratio
    private int mRatio;
    private float mDegrees;//夹角

    private List<Bitmap> mBitmaps;
    private boolean mIsLastPage; //是否最后一页
    private boolean mIsNextPage; //是否下一页
    private int mPageIndex;
    private Runnable mMsgCallback;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!mIsSlide) {
                return;
            }

            if (!mIsLastPage && mIsNextPage && (mTouchX - 40 <= -mW)) {
                mTouchX = -mW;
                mTouchY = mH;
                mPageIndex++;
                invalidate();

            } else if (mSlide == RIGHT_BOTTOM && mTouchX < mW) {//向下滑动
                mTouchX += 10;
            /*
            根据直线方程公式：(y-y1)/(y2-y1)=(x-x1)/(x2-x1)
            y=y1+(x-x1)*(y2-y1)/(x2-x1)

            mTouchUpX <==> x1  mTouchUpY <==> y1
            mW <==> x2      mH <==> y2
            不断变化的点 mTouchX <==> x   mTouchY <==> y
                 */
                mTouchY = mTouchUpY + (mTouchX - mTouchUpX) * (mH - mTouchUpY) / (mW - mTouchUpX);
                invalidate();
                sendMessageDelayed(obtainMessage(0), 25);
            } else if (mSlide == LEFT_BOTTOM && mTouchX > -mW) {//向左滑动
                mTouchX -= 40;
                mTouchY = mTouchUpY + (mTouchX - mTouchUpX) * (mH - mTouchUpY) / (-mW - mTouchUpX);
                invalidate();
                sendMessageDelayed(obtainMessage(0), 25);
            } else {
                slideStop();
            }
        }
    };

    public FoldTurnPageView(Context context) {
        this(context, null);
    }

    public FoldTurnPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldTurnPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPath = new Path();
        mPathFoldAndNext = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        mRegionShortSize = new Region();
        mCurRect = new Rect();

//        setLayerType(LAYER_TYPE_SOFTWARE, null); //关闭硬件加速 api11以上  在manifest中关闭
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FoldTurnPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        if (null == bitmaps || bitmaps.size() == 0) return;
        this.mBitmaps = bitmaps;
        System.out.println("setBitmaps");
        invalidate();
    }

    /**
     * 图片倒序:集合中最先加入的图(最后绘制)就能绘制在最上层
     */
    private void initBitmaps() {
        if (mBitmaps == null) {
            return;
        }
        List<Bitmap> temp = new ArrayList<Bitmap>();
        for (int i = mBitmaps.size() - 1; i >= 0; i--) {
            Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), getWidth(), getHeight(), true);
            temp.add(bitmap);
        }
        mBitmaps = temp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mIsNextPage = true;
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(0);
                mIsSlide = false;
                if (x < mAutoAreaLeft) { //上一页
                    mIsNextPage = false;
                    mPageIndex--;
                    mTouchX = x;
                    mTouchY = y;
                    invalidate();
                }
                if (!mIsLastPage) {
                    mTouchX = x;
                    mTouchY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsLastPage) {
                    mTouchX = x;
                    mTouchY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchX = x;
                mTouchY = y;
                if (mIsNextPage) {
                    if (x > mAutoAreaRight && y > mAutoAreaBottom) {
                        mSlide = RIGHT_BOTTOM;
                        startSlide(x, y);
                    } else if (x < mAutoAreaLeft) {
                        mSlide = LEFT_BOTTOM;
                        startSlide(x, y);
                    }
                }

                break;
        }

        return true;
    }

    private void startSlide(float x, float y ) {
        mIsSlide = true;
        mTouchUpX = x;
        mTouchUpY = y;
        mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = getMeasuredWidth();
        mH = getMeasuredHeight();

//        computeShortSizeRegion();
        // 短边圆形路径对象
        Path pathShortSize = new Path();
        // 添加圆形到Path
        pathShortSize.addCircle(0, mH, mW, Path.Direction.CCW);
        mRegionShortSize = computeRegion(pathShortSize);

        mAutoAreaRight = mW / 4 * 3;
        mAutoAreaBottom = mH / 4 * 3;
        mAutoAreaLeft = mW / 8;

        initBitmaps();

        // 计算当前页区域
        mCurRect.set(0, 0, mW, mH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


//        canvas.clipRegion(mRegionShortSize);

        canvas.drawColor(Color.parseColor("#d8ccaa00"));

        if (!mRegionShortSize.contains((int) mTouchX, (int) mTouchY)) {
            /*
             如果不在则通过x坐标强行重算y坐标
             通过圆的标准方程: (x-a)^2+(y-b)^2=r^2 (a,b)为圆心 r为半径  x,y为圆弧上的一点
             y - b = Math.sqrt(r^2 - (x-a)^2)  => y = Math.sqrt(r^2 - (x-a)^2) + b
             或
             -(y - b) = Math.sqrt(r^2 - (x-a)^2) => y = -1 * Math.sqrt(r^2 - (x-a)^2) + b
              */
//            mTouchY = (float) (Math.sqrt((Math.pow(mW, 2) - Math.pow(mTouchX, 2))) + mH); // 使用这个明显值偏大 比mH大
            mTouchY = (float) (-1 * Math.sqrt((Math.pow(mW, 2) - Math.pow(mTouchX, 2))) + mH);
        }

        /*
        缓冲区域判断
        当B点不在PB线上，而在屏幕上方之外，这时mTouchX, 偏左
            此时 mTouchY 越接近mH ，  折线出的就不能形成∆AOB了 而是一个矩形
         */
        float area = mH - mBuffArea;
        if (mTouchY >= area && !mIsSlide) {
            mTouchY = area;
        }

        float k = mW - mTouchX;
        float l = mH - mTouchY;
        float c = (float) (Math.pow(k, 2) + Math.pow(l, 2));
        float x = c / (2 * k);
        float y = c / (2 * l);

        if (x > y) {//x是长边 即向上翻
            mRatio = LONG;
            float cos = k / y;
            mDegrees = (float) (Math.acos(cos) / Math.PI * 180); //y与x轴夹角
            System.out.println("长时角度" + mDegrees);

            /*
            反余弦函数：y=arccos x； x->[-1,1]   y->[0,π]
            arccos(cos a)=a; a->[0,π]

             */
        } else {//x是短边
            mRatio = SHORT;
            float sin = (k - x) / x;
            mDegrees = (float) (Math.asin(sin) / Math.PI * 180); //x与y轴夹角
            System.out.println("短时角度" + mDegrees + "..." +sin + ".." + Math.asin(sin));
//            System.out.println("---mmmm>>>" + Math.asin(Math.sin(Math.PI/2)));

            /*
            反正弦函数：y=arcsin x; x->[-1,1]   y->[-π/2, π/2]
            arcsin(sin a) = a;  a->[-π/2, π/2]     π/2对应的角度即90度
            例 Math.asin(Math.sin(Math.PI/2)) = π/2

            Math.asin(sin) 算出一个弧度  所以要转角度
             */
        }

        mPath.reset();
        mPath.moveTo(mTouchX, mTouchY); //O点
        mPathFoldAndNext.reset();
        mPathFoldAndNext.moveTo(mTouchX, mTouchY);

        if (y > mH) {//B点超出屏幕上端
            //计算，BN边
            float bn = y - mH;
            //MN=BN/BD*OD
            float mn = bn / (y - (mH - mTouchY)) * (mW - mTouchX);
            //QN=BN/BP*AP
            float qn = bn / y * x;
            mPath.lineTo(mW - mn, 0);  //M点
            mPath.lineTo(mW - qn, 0);  //Q点
            mPath.lineTo(mW - x, mH);  //A点 在底部

            /*
            生成包含折叠和下一页的路径
            OMNPA 五点
             */
            mPathFoldAndNext.lineTo(mW - mn, 0); //M
            mPathFoldAndNext.lineTo(mW, 0);      //N
            mPathFoldAndNext.lineTo(mW, mH);     //P
            mPathFoldAndNext.lineTo(mW - x, mH); //A

        } else {
            mPath.lineTo(mW, mH - y); //B点 在右部
            mPath.lineTo(mW - x, mH); //A点 在底部

            /*
            生成包含折叠和下一页的路径
            OBPA 四点
             */
            mPathFoldAndNext.lineTo(mW, mH - y);   //B点 在右部
            mPathFoldAndNext.lineTo(mW, mH);       //P点
            mPathFoldAndNext.lineTo(mW - x, mH);   //A点 在底部
        }
        mPath.close();
        mPathFoldAndNext.close();

        mPaint.setColor(Color.RED);
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(mPathFoldAndNext, mPaint);

        mPaint.setColor(Color.GREEN);
        Path p = new Path();
        p.addCircle(0, mH, mW, Path.Direction.CCW);
        canvas.drawPath(p, mPaint);


        drawBitmaps(canvas);
    }

    private void drawBitmaps(Canvas canvas) {
        mIsLastPage = false;
        mPageIndex = mPageIndex < 0 ? 0 : mPageIndex;
        mPageIndex = mPageIndex > mBitmaps.size() ? mBitmaps.size() : mPageIndex;
        // 计算数据起始位置
        int start = mBitmaps.size() - 2 - mPageIndex;//mBitmaps.size() - 2 表示倒数第二个
        int end = mBitmaps.size() - mPageIndex;   // end - start = 2
        /*
         * 如果数据起点位置小于0则表示当前已经到了最后一张图片
         */
        if (start < 0) {
            mIsLastPage = true; // 此时mPageIndex = size - 1

            showToast("已经最后一页了");
            // 强制重置起始位置
            start = 0;
            end = 1;
        }
//        for (int i = start; i < end; i++) {//end - start = 2   这里最多循环两次
//            if (!mIsLastPage && i == end - 1) {
//                canvas.clipRect(0, 0, mClipX, getHeight()); //之后的绘制会相对当前裁剪区
//            }
//            canvas.drawBitmap(mBitmaps.get(i), 0, 0, null);
//        }


        /*
		 * 通过路径成成区域
		 */

        /*
		 * 计算当前页的区域
		 */
        canvas.save();
        canvas.clipRect(mCurRect);
        canvas.clipPath(mPathFoldAndNext, Region.Op.DIFFERENCE);//mRegionCurrent-regionNext
        canvas.drawBitmap(mBitmaps.get(end - 1), 0, 0, null);
        canvas.restore();

        /*
        折叠区 绘制 对应当前区的镜像(背面)
         */
        canvas.save();
        canvas.clipPath(mPath);
        canvas.translate(mTouchX, mTouchY);
        if (mRatio == SHORT) {
            canvas.rotate(90 - mDegrees);//以原点tx，ty作旋转
            canvas.translate(0, -mH);//相对于tx,ty 作平移
            canvas.scale(-1, 1);//以y轴翻转  x方向变反
            canvas.translate(-mW, 0);//x反向平移
        } else {
            canvas.rotate(-(90 - mDegrees));
            canvas.translate(-mW, 0);
            canvas.scale(1, -1);
            canvas.translate(0, -mH);
        }
        canvas.drawBitmap(mBitmaps.get(end - 1), 0, 0, null);
        canvas.restore();

        /*
        下一页区域
         */
        canvas.save();
        canvas.clipPath(mPathFoldAndNext);
        canvas.clipPath(mPath, Region.Op.DIFFERENCE);
        canvas.drawBitmap(mBitmaps.get(start), 0, 0, null);
        canvas.restore();

    }

    /**
     * 通过路径计算区域
     * @param path
     * @return
     */
    private Region computeRegion(Path path) {
        Region region = new Region();
        RectF f = new RectF();
        path.computeBounds(f, true);
        region.setPath(path, new Region((int) f.left, (int) f.top, (int) f.right, (int) f.bottom));
        return region;
    }

    /**
     * 计算短边的有效区域
     */
    private void computeShortSizeRegion() {
        // 短边圆形路径对象
        Path pathShortSize = new Path();
        // 添加圆形到Path
        pathShortSize.addCircle(0, mH, mW, Path.Direction.CCW);
        RectF bounds = new RectF();
        pathShortSize.computeBounds(bounds, true);
        //region.setPath   参数Region clip,   用于裁剪
        boolean flag = mRegionShortSize.setPath(pathShortSize, new Region((int) bounds.left, (int) bounds.top,
                (int) bounds.right, (int) bounds.bottom));
//        boolean flag = mRegionShortSize.setPath(pathShortSize, new Region(0, 1920-1080, 500, 1920));
//        System.out.println(bounds + ",," + flag);
//        System.out.println(mRegionShortSize);

    }

    /**
     * 为isSlide提供对外的停止方法便于必要时释放滑动动画
     */
    public void slideStop() {
        mIsSlide = false;
    }

    private void showToast(final Object msg) {
        mHandler.removeCallbacksAndMessages(null);
        if (mMsgCallback == null) {
            mMsgCallback = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), msg.toString(), Toast.LENGTH_SHORT).show();
                }
            };
        }
        mHandler.postDelayed(mMsgCallback, 200);

    }
}
