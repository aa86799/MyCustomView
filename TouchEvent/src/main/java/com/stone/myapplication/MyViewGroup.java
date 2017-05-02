package haqu.com.stone.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import static haqu.com.stone.myapplication.MainActivity.getAction;

/**
 * desc   :
 * author : stone
 * email  : aa86799@163.com
 * time   : 27/04/2017 17 39
 */
public class MyViewGroup extends ViewGroup {

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            getChildAt(0).layout(l, t, r, b);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean flag;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag = false;
                break;

            case MotionEvent.ACTION_MOVE:
                flag = false;
                break;

            case MotionEvent.ACTION_UP:
                flag = false;
                break;

            default:
                flag = false;
                break;
        }
        System.out.println("stone-viewGroup--dispatchTouchEvent--" + getAction(event.getAction()) + "--" + flag);
        return !flag ? super.dispatchTouchEvent(event) : flag;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean flag;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag = false;
                break;

            case MotionEvent.ACTION_MOVE:
                flag = true;
                break;

            case MotionEvent.ACTION_UP:
                flag = false;
                break;

            default:
                flag = false;
                break;
        }
        System.out.println("stone-viewGroup--onInterceptTouchEvent--" + getAction(event.getAction()) + "--" + flag);
        return !flag ? super.onInterceptTouchEvent(event) : flag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean flag;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag = false;
                break;

            case MotionEvent.ACTION_MOVE:
                flag = false;
                break;

            case MotionEvent.ACTION_UP:
                flag = false;
                break;

            default:
                flag = false;
                break;
        }
        System.out.println("stone-viewGroup--onTouchEvent--" + getAction(event.getAction()) + "--" + flag);
        return !flag ? super.onTouchEvent(event) : flag;
    }
}
