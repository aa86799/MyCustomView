package haqu.com.stone.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * desc   :
 * author : stone
 * email  : aa86799@163.com
 */

public class MainActivity extends Activity {

    private String tag = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acti_main);

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
        System.out.println("stone-activity--dispatchTouchEvent--" + getAction(event.getAction()) + "--" + flag);
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
        System.out.println("stone-activity--onTouchEvent--" + getAction(event.getAction()) + "--" + flag);
        return !flag ? super.onTouchEvent(event) : flag;
    }

    public static String getAction(int type) {
        switch (type) {
            case 0:
                return "down";
            case 1:
                return "up";
            case 2:
                return "move";
            default:
                return "unknow";
        }
    }
}
