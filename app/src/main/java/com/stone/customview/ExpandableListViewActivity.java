package com.stone.customview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.stone.customview.adapter.ExpandableAdapter;
import com.stone.customview.view.StickListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExpandableListViewActivity extends Activity
        implements StickListView.OnHeaderUpdateListener {

    private ExpandableListView mListView;
    private List<String> mGroupDataList;
    private Map<String, List<String>> mChildDataMap;
    private ExpandableAdapter mAdapter;


    @Override
    public void updateHeaderView(View mHeaderView, int firstVisibleGroupPos) {

    }

    @Override
    public View getHeaderView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);

        mListView = (ExpandableListView) findViewById(R.id.el_listview);

        initDats();

        mAdapter = new ExpandableAdapter(this, mGroupDataList, mChildDataMap);
        mListView.setAdapter(mAdapter);

    }

    private void initDats() {
        mGroupDataList = new ArrayList<String>();
        mChildDataMap = new HashMap<String, List<String>>();
        List<String> childList;
        String groupKey;
        for (int i = 1; i <= 15; i++) {
            groupKey = "group " + i;
            childList = new ArrayList<String>();
            for (int j = 1; j <= 6; j++) {
                childList.add("child " + j);
            }
            mGroupDataList.add(groupKey);
            mChildDataMap.put(groupKey, childList);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expandable_list_view, menu);
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

