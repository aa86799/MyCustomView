package com.stone.satellitemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.stone.satellitemenu.view.SateliteMenu;


public class MainActivity extends Activity {

    private SateliteMenu mSateliteMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSateliteMenu = (SateliteMenu) findViewById(R.id.sm_menu);

        mSateliteMenu.setOnMenuItemClickListener(new SateliteMenu.onMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), view.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });

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
            mSateliteMenu.setPosition(SateliteMenu.Position.POS_LEFT_TOP);
            return true;
        }
        if (id == R.id.action_rightTop) {
            mSateliteMenu.setPosition(SateliteMenu.Position.POS_RIGHT_TOP);
            return true;
        }
        if (id == R.id.action_leftBottom) {
            mSateliteMenu.setPosition(SateliteMenu.Position.POS_LEFT_BOTTOM);
            return true;
        }
        if (id == R.id.action_rightBottom) {
            mSateliteMenu.setPosition(SateliteMenu.Position.POS_RIGHT_BOTTOM);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
