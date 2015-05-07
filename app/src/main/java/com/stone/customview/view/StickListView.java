package com.stone.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.stone.customview.util.ViewUtil;

/**
 * 扩展自 ExpandableListView
 * 滑动listview时，显示哪个group下的child时，就在顶部浮动哪个group
 *
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/4/28 10 40
 *
 */
/*
 仿 pinnedheaderexpandablelistview 项目  本类未写全：touch-header时相关代码
 pinnedheaderexpandablelistview 也有个bug：当group和子view不一样高时 header的滑入顶出效果不对了
  */
/*
本类编写步骤：
1. 首先定义出该类，重写构造方法
2. 在layout.xml中使用该自定义类，在layout中findById
3. 考虑浮在顶部的group-layout 应该可以人为set的，所以写一个接口，回调注入
4. 在activity中实现回调接口，注入的view属性带LayoutParams,不然会报NullPointerException
5. 重写onMeasure,测量注入的headerView(调用view.measure方法)
6. 重写onLayout,布局headerView(调用view.layout方法)
7. 因为ListView顶层继承的是ViewGroup， 所以重写dispatchDraw  (如果继承自view，重写onDraw)
     调用drawChild方法，绘制子view：headerView
8. 要想看到headerView的效果，需要绘制出StickListView，即需要StickListView有数据
      在activity中设置adapter 加载数据
9. adapter中使用headerView的layout。去掉布局中的groupIndicator图
10. 想一想要实现的效果：
    a. 置顶一个view，显示顶端可见item所在的group信息
    b. 向上滑动，当下面的group与置顶的group有重合时，会置顶的顶出屏幕；此时，原下方的group置顶
    c. 向下滑动，当滑动要显示上一group时，上一group会从屏幕顶部滑入；此时，滑入的group置顶
11. 要想实现10，重写touch相关event，因是ViewGroup, 重写 dispatchTouchEvent
    a. 发现headerView的top坐标 应该是一个动态值，因其可能在屏幕外，可能在屏幕里，还有往里往外滑的效果
           修改onLayout，使用header根据自身的top值来布局
    b. 实现AbsListView.OnScrollListener
    c. 当前滚动时，重新布局headerView
12. 在touch事件中，判断是否按在最顶层的headerview区域，
    当touch-action-down&up后，即一个onClick周期，
    回调接口 更新headerview的group内容


 */
public class StickListView extends ExpandableListView implements AbsListView.OnScrollListener {

    private View mHeaderView;
    private int mHeaderWidth;
    private int mHeaderHeight;
    private OnHeaderUpdateListener mHeaderUpdateListener;
    private View mTouchTarget;
    private boolean mActionDownHappened;
    private boolean mIsHeaderGroupClickable;
//    private OnScrollListener mScrollListener;

    public StickListView(Context context) {
        super(context);
        initView();
    }

    public StickListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StickListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        if (l == null) {
            super.setOnScrollListener(this);
        } else {
            super.setOnScrollListener(l);
        }
    }

    private void initView() {
        setOnScrollListener(this);
    }

    public interface OnHeaderUpdateListener {
        void updateHeaderView(View mHeaderView, int firstVisibleGroupPos);
        /**
         * 返回一个view对象即可
         * 注意：view必须要有LayoutParams
         */
        View getHeaderView();
    }

    public void setOnHeaderUpdateListener(OnHeaderUpdateListener listener) {
        this.mHeaderUpdateListener = listener;
        if (listener == null) {
            return;
        }
        this.mHeaderView = listener.getHeaderView();
//        int firstVisiblePos = getFirstVisiblePosition();
//        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
//        listener.updateHeaderView(mHeaderView, firstVisibleGroupPos);
//        requestLayout();
//        invalidate();
//        System.out.println("lv-setOnHeaderUpdateListener");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderWidth = mHeaderView.getMeasuredWidth();
            mHeaderHeight = mHeaderView.getMeasuredHeight();
//            measureChild(mHeaderView, MeasureSpec.EXACTLY + mHeaderWidth, MeasureSpec.EXACTLY + mHeaderHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (mHeaderView != null) {
//            System.out.println(l+","+t+","+r+","+b);
//            mHeaderView.layout(l, t, r, b);//0,0,768,0  768表示的测试的屏幕的宽度  这里发没有高度

//            mHeaderView.layout(l, t, mHeaderWidth, mHeaderHeight);
//            System.out.println(l + "," + t + "," + mHeaderWidth + "," + mHeaderHeight);//0,0,768,43

            int delta = mHeaderView.getTop();//getTop相对于父view的y向位置
            mHeaderView.layout(0, delta, mHeaderWidth, mHeaderHeight + delta);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderView != null) {
            /*
            getDrawingTime 返回开始绘制view层级的时间
             */
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        /*
        AbsListView的重要方法 pointToPosition(int x, int y)
            根据x、y坐标 返回item所在的position
         */
        int pos = pointToPosition(x, y);
        System.out.println(getLastVisiblePosition()+"last");
        return super.dispatchTouchEvent(ev);
    }

    private View getTouchTarget(View view, int x, int y) {
        if (!(view instanceof ViewGroup)) {
            return view;
        }
/*
getChildDrawingOrder与 isChildrenDrawingOrderEnabled()是属于ViewGroup的方法.
getChildDrawingOrder 用于 返回当前迭代子视图的索引.就是说 获取当前正在绘制的视图索引.
如果需要改变ViewGroup子视图绘制的顺序,则需要重载这个方法.并且需要先调用 setChildrenDrawingOrderEnabled(boolean) 方法来启用子视图排序功能.
isChildrenDrawingOrderEnabled()则是  获取当前这个ViewGroup是否是按照顺序进行绘制的
 */
        int childCount = getChildCount();
        final boolean childrenDrawingOrderEnabled = isChildrenDrawingOrderEnabled();
        for (int i = 0; i < childCount; i++) {
            //如果按顺序绘制，则返回正在绘制的索引，如不按顺序，则返回i
            int childIndex = childrenDrawingOrderEnabled ? getChildDrawingOrder(childCount, i) : i;
            View childView = getChildAt(childIndex);
        }

        return null;

    }

    protected void refreshHeader() {
        if (mHeaderView == null) {
            return;
        }
        /*
        假如只有一个group，且其下没有子view。这时getFirstVisiblePosition() 返回0
         */
        int firstVisibleItem = getFirstVisiblePosition();//返回item所在位置 group也是一个item
        /*
        getExpandableListPosition 返回在展开的列表中的位置
         */
//        System.out.println("getExpandableListPosition()="+ getExpandableListPosition(firstVisibleItem));
        /*
        getPackedPositionGroup 返回当前item(item可能是group或其子view) 所在组的位置
         */
//        System.out.println("getPackedPositionGroup()=" + getPackedPositionGroup(getExpandableListPosition(firstVisibleItem)));

        int firstGroup = getPackedPositionGroup(getExpandableListPosition(firstVisibleItem)); //首可见项所在group
        int nextGroup = getPackedPositionGroup(getExpandableListPosition(firstVisibleItem + 1));//其下一可见项所在group
        if (nextGroup != firstGroup) { //如果不在同一group
            View view = getChildAt(1); //第二个绘制的是headerView
            if (view == null) return;
            if (view.getTop() <= mHeaderHeight) {
                int delta = mHeaderHeight - view.getTop();
                mHeaderView.layout(0, -delta, mHeaderWidth, mHeaderHeight);
//                mHeaderView.layout(0, delta, mHeaderWidth, mHeaderHeight);
            } else {
                mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
            }
        } else {
            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }

        if (mHeaderUpdateListener != null) {
            mHeaderUpdateListener.updateHeaderView(mHeaderView, firstGroup);
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (totalItemCount > 0) {
            refreshHeader();
        }
    }
}
