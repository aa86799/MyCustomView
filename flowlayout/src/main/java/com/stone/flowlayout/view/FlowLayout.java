package com.stone.flowlayout.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/8/4 15 08
 */
public class FlowLayout extends ViewGroup {

    private List<List<View>> mAllViews; // 内部list就是一行的view  所有views List<List>
    private List<Integer> mLineHeight; //每一行的高度

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAllViews = new ArrayList<>();
        mLineHeight = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0, height = 0; //整体的宽高
        int lineWidth = 0, lineHeight = 0; //行 宽高
        int childCount = getChildCount();
        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (childWidth + lineWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) { //新行
                width = Math.max(width, lineWidth); //比较宽
                lineWidth = childWidth; //重置
                height += lineHeight; //整体高度 +=
                lineHeight = childHeight; //重置
            } else {//同一行
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == childCount - 1) {
                /*
                如果i是新行:  上面执行新行代码块，width和height都是基于上一次的lineWidth和lineHeight值更新的
                如果i是同行:  上面执行同行代码块，没有更新width和height
                所以 width和height 需要最后更新一次
                */

                width = Math.max(width, lineWidth); //比较宽
                height += lineHeight;
            }
        }


        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : MeasureSpec.makeMeasureSpec(width,modeWidth),
                modeWidth == MeasureSpec.EXACTLY ? sizeHeight : MeasureSpec.makeMeasureSpec(height,modeHeight)
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            mAllViews.clear();
            mLineHeight.clear();
            List<View> lineViews = new ArrayList<>();
            int w = getWidth(); //当前ViewGroup的宽
            int lineWidth = 0, lineHeight = 0;
            int childCount = getChildCount();
            View child;
            int childWidth = 0, childHeight = 0;
            for (int i = 0; i < childCount; i++) {
                child = getChildAt(i);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();

                //如果要换行
                if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > w - getPaddingLeft() - getPaddingRight()) {
                    mLineHeight.add(lineHeight);
                    mAllViews.add(lineViews);

                    lineWidth = 0;
                    lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                    lineViews = new ArrayList<>();
                }
                lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
                lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
                lineViews.add(child);

                if (child instanceof TextView) {
                    ((TextView) child).setTextColor(getColor());
                }
            }
            //end item
            mLineHeight.add(lineHeight);
            mAllViews.add(lineViews);

            int left = getPaddingLeft(); //ViewGroup的padding值：表示每个内容的边距
            int top = getPaddingTop();
            int lineCount = mAllViews.size();
            for (int i = 0; i < lineCount; i++) {
                lineViews = mAllViews.get(i);
                lineHeight = mLineHeight.get(i);
                for (int j = 0; j < lineViews.size(); j++) {
                    child = lineViews.get(j);
                    if (child.getVisibility() == View.GONE) {
                        continue;
                    }
                    MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                    int lc = left + lp.leftMargin;
                    int tc = top + lp.topMargin;
                    int rc = lc + child.getMeasuredWidth();
                    int bc = tc + child.getMeasuredHeight();

                    child.layout(lc, tc, rc, bc);

                    left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

                }
                left = getPaddingLeft();
                top += lineHeight;
            }

        }
    }

    /**
     * 随机颜色
     * @return
     */
    private int getColor() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String temp;
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
