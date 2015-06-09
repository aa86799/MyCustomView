package com.stone.customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/*
 * 通过touch控制，判断点击在哪一个listview，touch的点是否处于屏幕的上半部 并且 是中间的listview
 * 如果是：滑动所有view，如果不是滑动当前view
 */
public class WaterFallThreeListView extends LinearLayout {
	
	public WaterFallThreeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//屏幕的三分之一
		int scaleWidth = getWidth() / getChildCount();
		int height = getHeight();
		int count = getChildCount();
		
		float x = event.getX();
		float y = event.getY();
		if (x < scaleWidth) {
//			System.out.println("left");
//			event.setLocation(x, y);//设置event的绝对位置(x,y)
			getChildAt(0).dispatchTouchEvent(event);
			
		} else if (x < 2 * scaleWidth) {
//			System.out.println("middle");
//			event.setLocation(x, y);//设置event的绝对位置(x,y)
			if (y < height / 2) {//上半部
				for (int i = 0; i < count; i++) {
					View v = getChildAt(i);
					v.dispatchTouchEvent(event);
				}
			} else {
				getChildAt(1).dispatchTouchEvent(event);
			}
			
		} else {
//			System.out.println("right");
//			event.setLocation(x, y);//设置event的绝对位置(x,y)
			getChildAt(2).dispatchTouchEvent(event);
		}
		
		return true;
	}

}
