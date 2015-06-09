package com.stone.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.stone.customview.R;
import com.stone.customview.util.PointUtil;

import java.util.List;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/5/29 14 24
 */
public class BackView extends ViewGroup {

    private List<List<PointUtil.MyPoint>> mPoints;
    private int backw, backh, mAreaw, mAreah;
    private int mMin;
    private Bitmap mBack;
    private List<PointUtil.LTRB> mLtrbList;

    public BackView(Context context) {
        super(context);
        init();
    }

    public BackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        PointUtil instance = PointUtil.getInstance(getContext());
        mPoints = instance.getPointsScale();
        mLtrbList = instance.getLtrbList();


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        mBack = BitmapFactory.decodeResource(getResources(), R.drawable.p0, options);
        //有options后 ，此时返回的bitmap为null
//        backw = mBack.getWidth();
//        backh = mBack.getHeight();
        backw = options.outWidth;
        backh = options.outHeight;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension();

        mAreaw = getMeasuredWidth();
        mAreah = getMeasuredHeight();
        mMin = Math.min(mAreaw, mAreah);

        PointUtil.getInstance(getContext()).storeRawPoints(mMin);

//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
//            System.out.println("c-width" + getChildAt(i).getMeasuredWidth() +","+ getChildAt(i).getMeasuredHeight());
//        }

        if (mLtrbList != null && !mLtrbList.isEmpty()) {
            PointUtil.LTRB ltrb = mLtrbList.get(0);
            int wSpec = MeasureSpec.makeMeasureSpec((int) ((ltrb.r - ltrb.l) * mMin), MeasureSpec.EXACTLY);
            int hSpec = MeasureSpec.makeMeasureSpec((int) ((ltrb.b - ltrb.t) * mMin), MeasureSpec.EXACTLY);
            measureChildren(wSpec, hSpec);
        } else
            measureChildren(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (i >= mPoints.size()) {
                    break;
                }
                PointUtil.LTRB ltrb = mLtrbList.get(i);
                l = (int) (ltrb.l * mMin);
                t = (int) (ltrb.t * mMin);
                r = (int) (ltrb.r * mMin);
                b = (int) (ltrb.b * mMin);
                getChildAt(i).layout(l, t, r, b);
//                System.out.println("l=" + l + ",t=" + t + ",r=" + r + ",b=" + b);

//                getChildAt(i).layout(36, 462 , 226, 685);

            }
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }
}
