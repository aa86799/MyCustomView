package com.stone.pintu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.stone.pintu.view.utils.GamePintuLayout;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/8/5 14 55
 */
public class MainActivity extends Activity {

    private GamePintuLayout gpl;
    private TextView tvLevel;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        gpl = (GamePintuLayout) findViewById(R.id.gpl_pintu);
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvTime = (TextView) findViewById(R.id.tv_time);

        gpl.setTimeEnabled(true);
        gpl.setGamePintuListener(new GamePintuLayout.GamePintuListener() {
            @Override
            public void nextLevel(int nextLevel) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Game Info")
                        .setMessage("LEVEL UP")
                        .setPositiveButton("NEXT LEVEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gpl.nextLevel();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
                tvLevel.setText(nextLevel + "");
            }

            @Override
            public void timeChanged(int currentTime) {
                tvTime.setText(currentTime + "");
            }

            @Override
            public void gameover() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Game Info")
                        .setMessage("GAME OVER")
                        .setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gpl.restart();
                            }
                        })
                        .setNegativeButton("QUIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpl.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpl.resume();
    }
}
