package com.stone.customview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class WaterFallActivity extends BaseActivity {
	
	private ListView lvLeft, lvMiddle, lvRight;
	private int[] images = {R.drawable.j10, R.drawable.j11, R.drawable.j12};
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.waterfall);
		lvLeft = getView(R.id.lv_left);
		lvMiddle = getView(R.id.lv_middle);
		lvRight = getView(R.id.lv_right);
		
		lvLeft.setAdapter(new MyAdapter());
		lvMiddle.setAdapter(new MyAdapter());
		lvRight.setAdapter(new MyAdapter());
	};
	
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return images.length * 5;
		}

		@Override
		public Object getItem(int position) {
			return images[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {//会图片乱跳，这里不管了
			ImageView iv = (ImageView) View.inflate(WaterFallActivity.this, R.layout.waterfall_item, null);
			iv.setBackgroundResource(images[(int)(Math.random() * images.length * 5 % 3)]);
			return iv;
		}
		
	}
}
