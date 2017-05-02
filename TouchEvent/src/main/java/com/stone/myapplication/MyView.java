package haqu.com.stone.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static haqu.com.stone.myapplication.MainActivity.getAction;

/**
 * desc   :
 * author : stone
 * email  : aa86799@163.com
 * time   : 27/04/2017 17 45
 */
public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        System.out.println("stone-view--dispatchTouchEvent--" + getAction(event.getAction()) + "--" + flag);
        return !flag ? super.dispatchTouchEvent(event) : flag;
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
        System.out.println("stone-view--onTouchEvent--" + getAction(event.getAction()) + "--" + flag);
        return !flag ? super.onTouchEvent(event) : flag;
    }

    private Paint mPaint = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.RED);
        canvas.drawCircle(getWidth()/2, getHeight()/2, Math.min(getWidth(), getHeight())/2, mPaint);
    }
}
