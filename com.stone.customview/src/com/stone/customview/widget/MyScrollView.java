package com.stone.customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/*
 * 整个的思路：
 * 1.在Activity中，加载使用自定义view的layout，add测试数据view到scrollerview
 * 2.在onlayout中设置子view的位置 使用view.layout
 * 3.考虑触摸滑动切换页面的实现。
 * 		重写onTouchEvent。当up时，判断是左滑还是右滑，左滑显示下一页，右滑显示上一页
 * 4.使用自定义滑动管理器
 * 5.
 * 		
 */
public class MyScrollView extends ViewGroup {

	private MyScroller scroller; //滚动器
	private GestureDetector gestureDetector; //手势探测器

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	/**
	 * 
	 * 监听页面的改变
	 */
	public interface OnPageChangeListener {
		/**
		 * 当页面改变的时候，回调改方法
		 * 
		 * @param index
		 *            ： 页面的下标位置
		 */
		public void moveTo(int index);
	}

	public OnPageChangeListener pageChangeListener;

	/**
	 * 设置监听页面
	 * 
	 * @param pageChangeListener
	 */
	public void setOnPageChangeListener(OnPageChangeListener pageChangeListener) {
		this.pageChangeListener = pageChangeListener;
	}

	private void initView(Context context) {
		scroller = new MyScroller();
		gestureDetector = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener() {

					@Override //distanceX :在X轴方向要移动的距离 distanceY :在Y轴方向要移动的距离
					public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
						/**
						 * 移动指定的距离 X: X轴方法的距离 Y: Y轴方向的距离
						 */
						scrollBy((int) distanceX, 0);//水平移动 距离：distanceX
						/**
						 * 移动到指定的点 x：X轴坐标点 y：Y轴坐标点
						 */
						// scrollTo(x, y);
						return true;
					}
			
		});

	}

	private float startX;
	/**
	 * 代表页面的下标的位置
	 */
	private int pageIndex;
	/**
	 * 一次按下的X坐标
	 */
	private float downX;
	/**
	 * 一次按下的Y坐标
	 */
	private float downY;
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean result = false;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN://按下
			gestureDetector.onTouchEvent(ev);
			//1.记录第一次按下的坐标
			downX = ev.getX();
			downY = ev.getY();

			break;
		case MotionEvent.ACTION_MOVE://移动
			//2.来到新的点
			float newdownX = ev.getX();
			float newdownY = ev.getY();
			
			//3.计算在水平方向和竖直方向滑动的距离
			//水平方向滑动的距离
			int distanceX =  (int) Math.abs(newdownX - downX);
			//竖直方向滑动的距离
			int distanceY =  (int) Math.abs(newdownY - downY);
			
			if(distanceX > distanceY&&distanceX >5){//distanceX > distanceY 表示是横向滑动
				result = true;
			}
			break;
		case MotionEvent.ACTION_UP://离开屏幕
			break;

		default:
			break;
		}
		return result;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);//父类执行了一些东西， 需要调用
		gestureDetector.onTouchEvent(event);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			float endX = event.getX();
			if (endX - startX > getWidth() / 2) { //右滑
				moveTo(pageIndex - 1);
			} else if (startX - endX > getWidth() / 2) {//左滑
				moveTo(pageIndex + 1);
			} else {//未滑动到一半时，回滚到当前页  自添加的,修复滑动到中间不动了的bug
				moveTo(pageIndex);
			}
			break;

		default:
			break;
		}
		
		return true;
	}
	
	/**
	 * 根据下标移动到指定的页面
	 * @param i
	 */
	public void moveTo(int index) {
		if (index < 0) index = 0;
		else if (index > getChildCount() - 1) index = getChildCount() - 1;
		this.pageIndex = index;
		
		if (pageChangeListener != null) {
			pageChangeListener.moveTo(pageIndex);
		}
		
		float distanceX = pageIndex * getWidth() - getScrollX();
		scroller.startScroll(getScrollX(), 0, distanceX, 0);
		invalidate();//重写draw 和 computeScroll
	}
	

	@Override
	public void computeScroll() {
		super.computeScroll();
		/*
		 * 滚动管理器中设定了最大需要的滚动时间，当不满足该时间时，computeScrollOffset return true.
		 */
		if (scroller.computeScrollOffset()) {
			// 要移动到的X轴坐标，Y轴坐标为0
			float x = scroller.getCurrX();//currX 就是一定时间内需要滚动的到的x点

			scrollTo((int) x, 0);

			// 又要导致computeScroll()
			invalidate();
		}

	}
	
	/**
	 * 1.测量View的尺寸，如果当前View是ViewGroup有义务测量它的孩子； 2.测量过程，先测量父类，在测量还在 3.测量不只一次
	 * 4.widthMeasureSpec和heightMeasureSpec它不是单纯的宽和高 里面包含父类给孩子的宽和父类给孩子的模式
	 * 5.模式：未指定；一定范围内；至多； 
	 * 总结：1.根据widthMeasureSpec得到父类给的宽和父类给的模式
	 * 		2.计算当前控件的宽方式，距离右边的值简单padding,得到宽
	 * 		3.计算孩子的宽MeasureSpec.getSize(widthMeasureSpec)和孩子的模式
	 * 				MeasureSpec.getMode(widthMeasureSpec) 
	 * 		4.根据得到的宽和模式，重新分配孩子的新的宽
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);// 执行父类的方法
		// 父类给孩子的宽
		int size = MeasureSpec.getSize(widthMeasureSpec);
		// 父类给孩子的模式
		int mode = MeasureSpec.getMode(widthMeasureSpec);
//		System.out.println(size + "  ----  " + mode);
		// getWidth();
		// 把孩子遍历出来，测量
		// 根据父类给的宽和模式又得出孩子的孩子的宽
		int newWidth = MeasureSpec.makeMeasureSpec(size, mode);
//		for (int i = 0; i < getChildCount(); i++) {
//			View view = getChildAt(i);
//			// 父类的宽和高有多少就给多少给孩子 子view占满父view
//			view.measure(widthMeasureSpec, heightMeasureSpec);
//		}
//		for (int i = 0; i < getChildCount(); i++) {
//			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
//		}
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		
//		getChildMeasureSpec(spec, padding, childDimension)
		
	}
	
	/**
	 * 布局显示过程中的一个方法，如果该View是一个ViewGroup那么， 就有义务指定孩子的位置和大小，普通的View不用实现这个方法
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (!changed) return;
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			//子view位置
			view.layout(i * getWidth(), 0, getWidth() + i * getWidth(),
					getHeight());
		}
	}

}
