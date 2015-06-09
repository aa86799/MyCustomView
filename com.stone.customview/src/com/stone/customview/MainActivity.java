package com.stone.customview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {
	
	private ListView mListView;
	private List<String> mDatas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 
		initDatas();
		mListView = (ListView) findViewById(R.id.lv_options);
		
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.main_item, R.id.tv_item, mDatas);
//		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mDatas);
		mListView.setAdapter(adapter);
		
//		mListView.setAdapter(new MyAdapter());
		
		mListView.setOnItemClickListener(this);
		
		ImageView iv = new ImageView(this);
//		iv.setBaseline(11);
	}
	
	private void initDatas() {
		mDatas = new ArrayList<String>();
		mDatas.add("自定义下拉刷新ListView");
		mDatas.add("自定义ScrollerView,使用系统的scroller,可横向滚动多个item");
		mDatas.add("自定义ScrollerView,自定义scroller原版");
		mDatas.add("自定义ScrollerView,自定义scroller自写");
		mDatas.add("自定义水波view,点击一下一个水波");
		mDatas.add("自定义水波view,滑动后一连串水波");
		mDatas.add("自定义LinearLayout,横向三个listview,点击中间view的上半部全部滑动,下半部滑动它自身");
		mDatas.add("自定义可拖动item的GridView");
	}

	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:
			startActivity(new Intent(this, RefreshListViewActi.class));
			break;
			
		case 1:
			startActivity(new Intent(this, ScrollViewActivityUseSystemScroller.class));
			break;
		case 2:
			startActivity(new Intent(this, ScrollViewActivity.class));
			break;
		case 3:
			startActivity(new Intent(this, ScrollViewOriginalActivity.class));
			break;
		case 4:
			startActivity(new Intent(this, WaterWaveActivity.class));
			break;
		case 5:
			startActivity(new Intent(this, WaterWaveWithMoveActivity.class));
			break;
		case 6:
			startActivity(new Intent(this, WaterFallActivity.class));
			break;
		case 7:
			startActivity(new Intent(this, DragGridActivity.class));
			break;

		default:
			break;
		}
	}
	
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = (TextView) View.inflate(getBaseContext(), R.layout.main_item, null).findViewById(R.id.tv_item);
			tv.setText(mDatas.get(position));
			return tv;
		}
		
	}
	
	
	//看桂枝的blog来的
	public <T> T $(int viewID)
	 {
		return (T)findViewById(viewID);
	 }
	
	void cc()	 {
		ListView lv = $(R.id.lv_options);
	}
}
