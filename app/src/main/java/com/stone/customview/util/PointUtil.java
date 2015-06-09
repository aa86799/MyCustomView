package com.stone.customview.util;

import android.content.Context;

import com.stone.customview.model.PuzzleBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/6/3 16 56
 */
public class PointUtil {

    private Context context;
    private PointUtil(Context context) {
        this.context = context;
        init();
    }

    private static class Builder {
        private static PointUtil mInstance;
        private static PointUtil getmInstance(Context context) {
            if (mInstance != null) return mInstance;
            return mInstance = new PointUtil(context);
        }
    }

    public static PointUtil getInstance(Context context) {
        return Builder.getmInstance(context);
    }


    private List<List<MyPoint>> mPoints;
    private List<List<MyPoint>> mReallyPoints;
    private List<LTRB> mLtrbList;

    public List<List<MyPoint>> getPointsScale() {
        return mPoints;
    }

    public List<MyPoint> getReallyPoints(int index) {
        if (index >= mReallyPoints.size()) return null;
        return mReallyPoints.get(index);
    }

    public List<LTRB> getLtrbList() {
        return mLtrbList;
    }

    private void init() {
        PuzzleBean bean = JsonLoad.readLocalJson(context, "PuzzleWall01e.json");
        String[] points = bean.getPoint().get(9);
        mPoints = new ArrayList<List<MyPoint>>();
        List<MyPoint> innerList;
        MyPoint mypoint = null;
        LinkedList<Float> pointListX, pointListY;
        mLtrbList = new ArrayList<LTRB>();
        LTRB ltrb;
        for (int j = 0; j < points.length; j++) {
            innerList = new ArrayList<MyPoint>();
            String[] split = points[j].split(",");

            pointListX = new LinkedList<Float>();
            pointListY = new LinkedList<Float>();
            for (int i = 0; i < split.length; i++) {
                if (i % 2 == 0) {
                    mypoint = new MyPoint();
                    mypoint.x = Float.valueOf(split[i]);
                    pointListX.add(mypoint.x);
                } else {
                    if (mypoint != null) {
                        mypoint.y = Float.valueOf(split[i]);
                        innerList.add(mypoint);
                        pointListY.add(mypoint.y);
                    }
                }
            }
            //最左的x 是上的y； 最右的x，最底的y
            //确定layout child
            Collections.sort(pointListX);
            Collections.sort(pointListY);
            ltrb = new LTRB();
            ltrb.l = pointListX.getFirst(); //left scale x
            ltrb.r = pointListX.getLast();
            ltrb.t = pointListY.getFirst();
            ltrb.b = pointListY.getLast();
            mLtrbList.add(ltrb);

            mPoints.add(innerList);
        }
    }

    public void storeRawPoints(int len) {
        mReallyPoints = new ArrayList<List<MyPoint>>();
        List<MyPoint> innerList;
        MyPoint p;
        for (int j = 0; j < mPoints.size(); j++) {
            List<PointUtil.MyPoint> myPoints = mPoints.get(j); //一个list就是一个多边形的所有点 scale
            innerList = new ArrayList<MyPoint>();
            for (int i = 0; i < myPoints.size(); i++) {
                p = myPoints.get(i);
                innerList.add(new MyPoint(p.x * len, p.y * len));
            }
            mReallyPoints.add(innerList);
        }
    }


    public  static class MyPoint {
        //x y  是 scale
        public float x, y;

        public MyPoint() {

        }
        public MyPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class LTRB {
        public float l, t, r, b;
    }
}
