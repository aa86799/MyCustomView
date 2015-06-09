package com.stone.customview.widget;

import android.os.SystemClock;

public class MyScrollerOriginal {
	
	private float scrollX, distanceX, scrollY, distanceY;
	private long startTime;
	public void startScroll(float scrollX, float distanceX, float scrollY, float distanceY) {
		this.scrollX = scrollX;
		this.scrollY = scrollY;
		this.distanceX = distanceX;
		this.distanceY = distanceY;
		
		this.startTime = SystemClock.uptimeMillis();
		isFinish = false;
	}
	
	private float currX; //当前移动的距离
	public float getCurrX() {
		return currX;
	}
	
	private boolean isFinish;
	private int maxAnimTime = 600;
	
	public boolean computeScrollOffset() {//view重绘后调用，再重绘再调用
		if (isFinish) {
			return false;
		}
		
		long endTime = SystemClock.uptimeMillis();
		long deltaTime = endTime - startTime; //间隔时间
		if (deltaTime < maxAnimTime) {
			float curDistanceX = deltaTime * distanceX / maxAnimTime;//间隔时间内所需要要滚动的距离
//			System.out.println("deltaTime=" + deltaTime + ",distanceX=" + distanceX +", curDistanceX=" + curDistanceX);
			currX = scrollX + curDistanceX;
		} else {
			isFinish = true;
		}
		
		return true;
	}
}
