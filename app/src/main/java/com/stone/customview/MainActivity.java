package com.stone.customview;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLES31;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute();
    }

    public void expandableListView(View view) {
        startActivity(new Intent(this, ExpandableListViewActivity.class));
    }

    public void myExpLv(View view) {
        startActivity(new Intent(this, MyExpLvActivity.class));
    }
    public void contacts(View view) {
        startActivity(new Intent(this, ContactsActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
