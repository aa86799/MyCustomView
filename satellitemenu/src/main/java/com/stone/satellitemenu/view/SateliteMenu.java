package com.stone.satellitemenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.stone.satellitemenu.R;

/**
 * 卫星式菜单
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/5/7 14 17
 */

public class SateliteMenu extends ViewGroup {

    private int mPosition;
    private float mRadius;
    private final int LEFT_TOP = 1;
    private final int RIGHT_TOP = 2;
    private final int LEFT_BOTTOM = 4;
    private final int RIGHT_BOTTOM = 8;

    public SateliteMenu(Context context) {
        super(context);
        init();
    }

    public SateliteMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SateliteMenu);
        mPosition = typedArray.getInt(R.styleable.SateliteMenu_position, 1);
        mRadius = typedArray.getDimension(R.styleable.SateliteMenu_radius, 0);

        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View root = getChildAt(0);
        measureChild(root, widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View root = getChildAt(0);
        switch (mPosition) {
            case LEFT_TOP:
                System.out.println(root.getWidth());
                root.layout(l, t, root.getWidth(), root.getHeight());
                break;
            case RIGHT_TOP:
                break;
            case LEFT_BOTTOM:
                break;
            case RIGHT_BOTTOM:
                break;
        }
    }
}
