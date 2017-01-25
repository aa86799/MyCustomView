/**
 * Copyright © 2013 CVTE. All Rights Reserved.
 */
package com.stone.canvaspath.inhale;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;

/**
 * @description TODO
 * @author Taolin
 * @date Dec 30, 2013
 * @version v1.0
 */

public class InhaleMesh extends Mesh {

    private Path mFirstPath;
    private Path mSecondPath;
    private PathMeasure mFirstPathMeasure;
    private PathMeasure mSecondPathMeasure;

    public InhaleMesh(int width, int height) {
        super(width, height);
        mFirstPath = new Path();
        mSecondPath = new Path();
        mFirstPathMeasure = new PathMeasure();
        mSecondPathMeasure = new PathMeasure();
    }

    @Override
    public void buildPaths(float endX, float endY) {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, did you call setBitmapSize(int, int) method?");
        }
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        float w = mBmpWidth;
        float h = mBmpHeight;

        mFirstPath.reset();
        mSecondPath.reset();

        mFirstPath.moveTo(0, 0);
        mSecondPath.moveTo(w, 0);
        mFirstPath.lineTo(0, h);
        mSecondPath.lineTo(w, h);

        mFirstPath.quadTo(0+50, (endY + h) / 2, endX, endY);
        mSecondPath.quadTo(w-50, (endY + h) / 2, endX, endY);
    }

    @Override
    public void buildMeshes(int timeIndex) {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, did you call setBitmapSize(int, int) method?");
        }
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        float[] pos1 = {0.0f, 0.0f};
        float[] pos2 = {0.0f, 0.0f};
        float firstLen = mFirstPathMeasure.getLength();
        float secondLen = mSecondPathMeasure.getLength();

        float len1 = firstLen / mVerticalSplit; //左边第一条path纵向每段的len
        float len2 = secondLen / mVerticalSplit;

        float firstPointDist = timeIndex * len1; //动画每一段时间内的运动距离 *len1 = (0, 左p len)
        float secondPointDist = timeIndex * len2;
        float height = mBmpHeight;

        mFirstPathMeasure.getPosTan(firstPointDist, pos1, null); //运动中，path1实际图片显示的起点
        mFirstPathMeasure.getPosTan(firstPointDist + height, pos2, null); //终点

        float x1 = pos1[0];
        float x2 = pos2[0];
        float y1 = pos1[1];
        float y2 = pos2[1];
        float firstDist = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));//path1上下两点的距离
        float firstSplitDist = firstDist / mVerticalSplit; //上下两点的距离分vs段

        mSecondPathMeasure.getPosTan(secondPointDist, pos1, null);
        mSecondPathMeasure.getPosTan(secondPointDist + height, pos2, null);
        x1 = pos1[0];
        x2 = pos2[0];
        y1 = pos1[1];
        y2 = pos2[1];

        float secondDist = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        float secondSplitDist = secondDist / mVerticalSplit;

        int index = 0;
        for (int y = 0; y <= mVerticalSplit; y++) {
            //一定时间内运动的位置+（y向上 0~mVerticalSplit）

            /*
            y= (0, vs) => y * firstSplitDist = (0, firstDist)=>(0, 运动中path1上下两点的距离)
            (0, firstDist)+firstPointDist => (0,上下两点的距离) + (一定时间内运动距离)=>(0, 上下两点的距离+path1运动到的距离）最大值是图片消失了
             */
            mFirstPathMeasure.getPosTan(y * firstSplitDist + firstPointDist, pos1, null);
            mSecondPathMeasure.getPosTan(y * secondSplitDist + secondPointDist, pos2, null);

            float fx1 = pos1[0];
            float fx2 = pos2[0];
            float fy1 = pos1[1];
            float fy2 = pos2[1];

            float dy = fy2 - fy1; //左右点y距
            float dx = fx2 - fx1; //左右点x距

            for (int x = 0; x <= mHorizontalSplit; x++) {
                float fx = dx * x / mHorizontalSplit;
                float fy = dy * x / mHorizontalSplit;

                mVertices[index * 2 + 0] = fx + fx1;
                mVertices[index * 2 + 1] = fy + fy1;
//                mVertices[2*x + mVerticalSplit * y + 0] = fx + fx1;
//                mVertices[2*x + mVerticalSplit * y + 1] = fy + fy1;

                index += 1;
            }
        }
    }

    public Path[] getPaths() {
        return new Path[]{mFirstPath, mSecondPath};
    }
}