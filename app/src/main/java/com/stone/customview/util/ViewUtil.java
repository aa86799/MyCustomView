package com.stone.customview.util;

import android.view.View;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/5/4 15 21
 */
public class ViewUtil {
    /**判断触摸点是否在 可点击的view之上*/
    public static boolean isTouchPointInClickableView(View view, int x, int y) {
        if (view.isClickable() && y >= view.getTop() && y <= view.getBottom()
                && x >= view.getLeft() && x <= view.getRight()) {
            return true;
        }
        return false;
    }
}
