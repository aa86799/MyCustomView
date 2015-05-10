package com.stone.roulette;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stone.roulette.View.RouletteSurfaceView;


public class MainActivity extends Activity implements RouletteSurfaceView.OnTouchRouletteListener {

    private RouletteSurfaceView mRouletteView;
    private Button mBtnStart, mBtnStop;
    private int mPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.stone.roulette.R.layout.activity_main);
        mRouletteView = (RouletteSurfaceView) findViewById(com.stone.roulette.R.id.rv_roulette);

        mBtnStart = (Button) findViewById(com.stone.roulette.R.id.btn_start);
        mBtnStart.setClickable(true);
        mBtnStop = (Button) findViewById(com.stone.roulette.R.id.btn_stop);
        mBtnStop.setClickable(false);

        mBtnStart.setTextSize(24);
        mBtnStop.setTextSize(12);

        mRouletteView.setOnTouchRouletteListener(this);

        //RmActi 能判断点在哪个扇形区
//        startActivity(new Intent(this, RmActi.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.stone.roulette.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.stone.roulette.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start(View view) {
        mBtnStart.setClickable(false);
        mBtnStart.setTextSize(12);
        mRouletteView.startRotate();
        mBtnStop.setClickable(true);
        mBtnStop.setTextSize(24);

    }

    public void stop(View view) {
        mRouletteView.stopRotate();
        mBtnStop.setTextSize(12);
        mBtnStop.setClickable(false);
        while (true) {
            if (!mRouletteView.getRunStats()) {
                mBtnStart.setClickable(true);
                mBtnStart.setTextSize(24);
                System.out.println("中华人民共和国");
                break;
            }
        }

    }

    public void parts(View view) {
        if (mPart == 12) {
            mPart = 1;
        } else {
            mPart++;
            if (mPart == 7 || mPart == 11) {
                mPart++; //7或11 360除不尽
            }
        }
        ((Button) view).setText("等分圆(点一次加1等份,最多12份): " + mPart);
        mRouletteView.setPart(mPart);
    }

    @Override
    public void click(int position) {
        Toast.makeText(this, "position=" + position, Toast.LENGTH_SHORT).show();
    }
}
