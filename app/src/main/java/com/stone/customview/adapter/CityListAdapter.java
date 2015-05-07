package com.stone.customview.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stone.customview.R;
import com.stone.customview.model.City;
import com.stone.customview.service.MySectionIndexer;
import com.stone.customview.view.PinnedHeaderListView;


public class CityListAdapter extends BaseAdapter implements PinnedHeaderListView.PinnedHeaderAdapter, OnScrollListener {
	private List<City> mList;
	private MySectionIndexer mIndexer;
	private Context mContext;
	private int mLocationPosition = -1;
	private LayoutInflater mInflater;

	public CityListAdapter(List<City> mList, MySectionIndexer mIndexer,
			Context mContext) {
		this.mList = mList;
		this.mIndexer = mIndexer;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.select_city_item, null);

			holder = new ViewHolder();
			holder.group_title = (TextView) view.findViewById(R.id.group_title);
			holder.city_name = (TextView) view.findViewById(R.id.city_name);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		City city = mList.get(position);
		
		int section = mIndexer.getSectionForPosition(position);
		if (mIndexer.getPositionForSection(section) == position) {
			holder.group_title.setVisibility(View.VISIBLE);
			holder.group_title.setText(city.getSortKey());
		} else {
			holder.group_title.setVisibility(View.GONE);
		}
		
		holder.city_name.setText(city.getName());

		return view;
	}

	public static class ViewHolder {
		public TextView group_title;
		public TextView city_name;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0
				|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = mIndexer.getSectionForPosition(realPosition);
		int nextSectionPosition = mIndexer.getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub
		int realPosition = position;
		int section = mIndexer.getSectionForPosition(realPosition);
		String title = (String) mIndexer.getSections()[section];
		((TextView) header.findViewById(R.id.group_title)).setText(title);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}

	}
}
