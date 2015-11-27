package com.stone.customview.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/3/2 14 15
 */
public class WaterWaveViewWithMove extends View {
	
	private Wave wave;
	private List<Wave> wavesList;
	private boolean isRunning;
	private int xSlop = 10, ySlop = 10;
	
	public WaterWaveViewWithMove(Context context) {
		super(context);
		wavesList = new ArrayList<WaterWaveViewWithMove.Wave>();
	}
	
	public WaterWaveViewWithMove(Context context, AttributeSet attrs) {
		super(context, attrs);
		wavesList = new ArrayList<WaterWaveViewWithMove.Wave>();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			for (Iterator iterator = wavesList.iterator(); iterator.hasNext();) {
				Wave wave = (Wave) iterator.next();
				
				//透明度 越低越透明
				int alpha = wave.paint.getAlpha() - 5;
				if (alpha < 0) {
					iterator.remove();
					continue;
				}
				
				//圆的半径变大
				wave.radius += 10;
				
				//圆环的半径变小
				wave.paint.setStrokeWidth(wave.radius / 6); //轮廓宽度
				wave.paint.setAlpha(alpha);
				
			}
			if (wavesList.size() == 0) {
				isRunning = false;
			}
			
			invalidate();
			if (isRunning) {
				handler.sendEmptyMessageDelayed(0, 50);
			}
		};
	};
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Iterator iterator = wavesList.iterator(); iterator.hasNext();) {
			Wave wave = (Wave) iterator.next();
			if (wave.paint.getAlpha() > 0) { //透明度大>0 才绘制
				canvas.drawCircle(wave.x, wave.y, wave.radius, wave.paint);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			if (wavesList.size() == 0) {
				isRunning = true;
				handler.sendEmptyMessageDelayed(0, 50); //发送消息后，handler遍历wavesList 循环更新
			}
			//若不在上面的if里发送msg，会：手指down|move时 都会更新一下view
//			handler.sendEmptyMessageDelayed(0, 50);
		
			float x = event.getX();
			float y = event.getY();
			//当前点的x或y与最后有效点的x或y距离大于slop值 才添加新的wave
			if (wavesList.size() > 0) {
				if (Math.abs(x - wavesList.get(wavesList.size() - 1).x) > xSlop
						|| Math.abs(y - wavesList.get(wavesList.size() - 1).y) > ySlop) {
					wave = new Wave();
					wave.x = x;
					wave.y = y;
					wave.resetPaint();//重写设置一次画笔
					wavesList.add(wave);
				}
			} else {
				wave = new Wave();
				wave.x = x;
				wave.y = y;
				wave.resetPaint();//重写设置一次画笔
				wavesList.add(wave);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		return true;
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
	
	private class Wave {
		private float x, y, radius;
		private Paint paint;
		
		private void resetPaint() {
			paint = new Paint();
			paint.setAntiAlias(true);//抗锯齿
			paint.setStyle(Style.STROKE);//绘制轮廓
			paint.setColor(getColor());//随机颜色
		}
	}

}
