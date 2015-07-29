package com.stone.guaguaka;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.stone.guaguaka.view.GuaGuaKaView;
import com.stone.guaguaka.view.GuaView;

public class MainActivity extends AppCompatActivity {

//    GuaGuaKaView mGuaGuaKaView;
//    GuaGuaKaView mGuaGuaKaView2;
    GuaView mGuaGuaKaView;
    GuaView mGuaGuaKaView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGuaGuaKaView = (GuaView) findViewById(R.id.st_guaguaka);
        mGuaGuaKaView.setOnGuaGuaKaCompletedListener(new GuaView.onGuaGuaKaCompletedListener() {
            @Override
            public void complete(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        mGuaGuaKaView2 = (GuaView) findViewById(R.id.st_guaguaka2);
        mGuaGuaKaView2.setOnGuaGuaKaCompletedListener(new GuaView.onGuaGuaKaCompletedListener() {
            @Override
            public void complete(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
