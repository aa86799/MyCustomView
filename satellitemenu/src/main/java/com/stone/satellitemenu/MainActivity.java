package com.stone.satellitemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.stone.satellitemenu.view.SateliteMenu;


public class MainActivity extends Activity {

    private SateliteMenu mSateliteMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSateliteMenu = (SateliteMenu) findViewById(R.id.sm_menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_leftTop) {
            return true;
        }
        if (id == R.id.action_rightTop) {

            return true;
        }
        if (id == R.id.action_leftBottom) {
            return true;
        }
        if (id == R.id.action_rightBottom) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
