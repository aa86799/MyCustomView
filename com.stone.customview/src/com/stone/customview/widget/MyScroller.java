package com.stone.customview.widget;

import android.os.SystemClock;

public class MyScroller {
	
	/**
	 * 在X轴移动的起始坐标
	 */
	private float startX;
	/**
	 * 在Y轴移动的起始坐标
	 */
	private float startY;
	/**
	 * 在X轴移动的距离
	 */
	private float distanceX;
	/**
	 * 在Y轴移动的距离
	 */
	private float distanceY;
	/**
	 * 开始移动的起始时间
	 */
	private long startTime;
	/**
	 * 是否移动完成
	 * false:没有完成
	 * true:移动完成
	 */
	private boolean isFinish;
	
	/**
	 * 移动这个动画所画的总时间
	 */
	private long totalTime = 500;
	private float currX;

	public void startScroll(float startX,float startY,float distanceX,float distanceY){
		this.startX = startX;
	    this.startY = startY;
		this.distanceX = distanceX;
		this.distanceY = distanceY;
		this.startTime = SystemClock.uptimeMillis();
		this.isFinish = false;
		
	}
	
	/**
	 * 计算偏移量
	 * 移动一小段的时间
	 * 移动一小段距离
	 * 移动一小段对应的坐标
	 * 移动的平均速度
	 * true:正在移动
	 * false:移动结束
	 * @return
	 */
	public boolean computeScrollOffset(){
		if(isFinish){
			//移动结束
			return false;
		}
		long endTime = SystemClock.uptimeMillis();
		long passTime = endTime - startTime;
		if(passTime < totalTime){
			//还在移动
//			float volecityX = distanceX / totalTime;
			//距离 = 时间* 速度
			//这一小段的距离
			float distanceSmallX  =  passTime * distanceX / totalTime;
			System.out.println("000+" + distanceSmallX);
			//移动一小段转换对应的坐标
			currX = startX + distanceSmallX;
		}else{
			currX = startX + distanceX;//本身要移动到的点
			//移动结束
			isFinish = true;
		}
		
		return true;
	}

	public float getCurrX() {
		return currX;
	}

}
