package com.stone.customview.widget;

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

public class WaterWaveView extends View {
	
	private Paint paint;
	
	public WaterWaveView(Context context) {
		super(context);
	}
	
	public WaterWaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void initView() {
		paint = new Paint();
		paint.setAntiAlias(true);//抗锯齿
		paint.setStyle(Style.STROKE);//绘制轮廓
		
//		paint.setColor(Color.parseColor("#ff0000C6"));
//		System.out.println(getColor());
		paint.setColor(getColor());
		radius = 10;
		paint.setStrokeWidth(radius / 3); //轮廓宽度
	}
	
	private int radius;//圆的半径 
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			//圆的半径变大
			radius+=10;
			
			//圆环的半径变小
			paint.setStrokeWidth(radius / 5); //轮廓宽度
			
			//透明度变暗 即变低
			int alpha = paint.getAlpha() - 5;
			if (alpha < 0) alpha = 0;
			paint.setAlpha(alpha);
			
			invalidate();
		};
	};
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		paint.setAlpha(-100);
		
		if (startX != 0 && startY != 0) {
			if (paint.getAlpha() > 0) { //透明度大>0 才绘制
				//打开注释会发现，设置了透明度为负数后，会自动转成某个正数.
	//			System.out.println("ondraw"+ paint.getAlpha());
				
	//			canvas.drawCircle(getWidth()/2, getHeight()/2, radius, paint);
				canvas.drawCircle(startX, startY, radius, paint);
				
				handler.sendEmptyMessageDelayed(0, 50);
			}
		}
	}
	
	private float startX, startY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY();
			
			initView();//重写设置一次画笔
			
			invalidate(); //绘制
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		return true;
	}
	
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
