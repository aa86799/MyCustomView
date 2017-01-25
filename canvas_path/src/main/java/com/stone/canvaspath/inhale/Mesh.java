/**
 * Copyright © 2013 CVTE. All Rights Reserved.
 */
package com.stone.canvaspath.inhale;

/**
 * @description TODO
 * @author Taolin
 * @date Dec 30, 2013
 * @version v1.0
 */

public abstract class Mesh {//网格

    private static final int HORIZONTAL_SPLIT = 40;
    private static final int VERTICAL_SPLIT = 40;
    /**
     * splitting partitions in horizontal and vertical
     */
    protected int mHorizontalSplit; //横向分隔块
    protected int mVerticalSplit; //纵向分隔块

    protected int mBmpWidth = -1;
    protected int mBmpHeight = -1;
    protected final float[] mVertices; //所有网格的 xy数组

    public Mesh() {
        mHorizontalSplit = HORIZONTAL_SPLIT;
        mVerticalSplit = VERTICAL_SPLIT;
        mVertices = new float[(mHorizontalSplit + 1) * (mVerticalSplit + 1) * 2];
    }

    public Mesh(int width, int height) {
        mHorizontalSplit = width;
        mVerticalSplit = height;
        mVertices = new float[(mHorizontalSplit + 1) * (mVerticalSplit + 1) * 2];
    }

    public float[] getVertices() {
        return mVertices;
    }

    public int getWidth() {
        return mHorizontalSplit;
    }

    public int getHeight() {
        return mVerticalSplit;
    }

    public static void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x; //索引偶数为x
        array[index * 2 + 1] = y; //索引奇数为y
    }

    public void setBitmapSize(int w, int h) {
        mBmpWidth = w;
        mBmpHeight = h;
    }

    /**
     * 根据参数 构建path
     * @param endX
     * @param endY
     */
    public abstract void buildPaths(float endX, float endY);

    /**
     *  根据index构建网格mesh
     * @param index
     */
    public abstract void buildMeshes(int index);

    /**
     * 根据宽高 构建网格mesh
     * @param w
     * @param h
     */
    public void buildMeshes(float w, float h) {
        int index = 0;
        for (int y = 0; y <= mVerticalSplit; y++) {
            float fy = y * h / mVerticalSplit; //y坐标
            for (int x = 0; x <= mHorizontalSplit; x++) {
                float fx = x * w / mHorizontalSplit; //x坐标
                setXY(mVertices, index, fx, fy);
                index++;
            }
        }

        /*微调vertices*/
        index = 0;
        for (int y = 0; y <= mVerticalSplit; y++) {
            float fy = mVertices[2*index + 1];

            for (int x = 0; x <= mHorizontalSplit; x++) {
                float fx = mVertices[2*index+0];
//                if (x == mHorizontalSplit / 2) {
//                    fx += 3;
//                    fy += 10;
//                }

//                if (y == mVerticalSplit / 2 + 3) {
//                    fx += 20;
////                    fy += 3;
//                }

                setXY(mVertices, index, fx, fy);
                index++;
            }
        }
    }
}