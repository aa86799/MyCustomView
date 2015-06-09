package com.stone.customview.widget;

import com.stone.customview.R;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MyScrollViewOriginal extends ViewGroup {
	
	private GestureDetector mDetector;
	private MyScrollerOriginal mScroller;

	public MyScrollViewOriginal(Context context, AttributeSet attrs) {
		super(context, attrs);
		
//		setBackgroundColor(color.darker_gray); //设置了背景后，触发了viewgroup的ondraw
		init(context);
	}
	
	public interface onPageChangeListener {
		void moveTo(int index);
	}
	
	private onPageChangeListener mOnPageChangeListener;
	public void setOnPageChangeListener(onPageChangeListener mOnPageChangeListener) {
		this.mOnPageChangeListener = mOnPageChangeListener;
	}
	

	private void init(Context context) {
		mDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				scrollBy((int) distanceX, 0);
				return true;
			}
			
		});
		
		mScroller = new MyScrollerOriginal();
	}
	
	private float startY;
	@Override //在test-view中 横向滑动值过大时才拦截 以执行onTouchEvent
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = ev.getX();
			startY = ev.getY();
			this.mDetector.onTouchEvent(ev);//只有down时 监听到ev在滚动时 就要滚动当前的view
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = ev.getX();
			float moveY = ev.getY();
			float distanceX = Math.abs(moveX - startX);//向左可能为负
			float distanceY = Math.abs(moveY - startY);
			if (distanceX > distanceY && distanceX > 5) {//x>y 水平移动  >5滑动整个view
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		return false; 
	}
	
	private float startX; //开始按下的x
	private int pageIndex; //当前显示的page页index
	private float distanceX; // MotionEvent.ACTION_UP后 滑动太生硬，添加一个距离，用于滑动处理
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			float endX = event.getX();
			if (endX - startX > getWidth() / 2) {//右滑 显示上一个
				moveTo(--pageIndex);
			} else if (startX - endX > getWidth() / 2) {//左滑 显示下一个
				moveTo(++pageIndex);
			} else {
				moveTo(pageIndex);
			}
			break;

		default:
			break;
		}
		return true;
	}
	
	//滑动页面
	public void moveTo(int i) {
		if (pageIndex == getChildCount()) pageIndex = getChildCount() - 1;
		else if (pageIndex < 0) pageIndex = 0;
		else pageIndex = i;
		
		if (mOnPageChangeListener != null) {
			mOnPageChangeListener.moveTo(pageIndex);
		}
		
//		scrollTo(pageIndex * getWidth(), 0); 隐藏掉，用mScroller来移动
		
		//还需要滚动的距离
		this.distanceX = pageIndex * getWidth() - getScrollX(); //需要显示的page的起始X-已滑动(move拖动时)的x
		System.out.println("888===" +  getWidth());
		//开始滚动
		mScroller.startScroll(getScrollX(), distanceX, 0, 0); //getScrollX 上次滚动的x
		
		invalidate();
	}

	@Override
	public void computeScroll() { //invalidate后会调用
		if (mScroller.computeScrollOffset()) {//还需要滚动
			float currX = mScroller.getCurrX();
			scrollTo((int) currX, 0);
			invalidate();
		}
		System.out.println("oncomputescroll");
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//测量子view  以便test view显示
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		System.out.println("onMeasure");
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			view.layout(i * getWidth(), 0, getWidth() + i * getWidth(), getHeight());
		}
		System.out.println("onlayout");
	}
	
	@Override
	public void draw(Canvas canvas) {
		System.out.println("draw");
		super.draw(canvas);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		System.out.println("ondraw");
		super.onDraw(canvas);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		System.out.println("dispatchDraw");
		super.dispatchDraw(canvas);
	}
	
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		System.out.println("drawChild");
		return super.drawChild(canvas, child, drawingTime);
	}
}
