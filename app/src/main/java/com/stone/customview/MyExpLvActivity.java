package com.stone.customview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.customview.view.StickListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/4/28 16 14
 */
public class MyExpLvActivity extends Activity implements StickListView.OnHeaderUpdateListener {

    private StickListView mListView;
    private TextView mSlideGroupTextView;//悬浮的TV
    private ImageView mSlideImageView; //悬浮的IV

    private List<String> mGroupDataList;
    private Map<String, List<String>> mChildDataMap;
    private MyExpandableListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_exp_lv);
        mListView = (StickListView) findViewById(R.id.sl_listview);

        mListView.setOnHeaderUpdateListener(this);


        initDatas();
        mAdapter = new MyExpandableListAdapter(this, mGroupDataList, mChildDataMap);
        mListView.setAdapter(mAdapter);

        for (int i = 0, size = mGroupDataList.size(); i < size; i++) {
            mListView.expandGroup(i);//展开分组
        }
    }

    private void initDatas() {
        mGroupDataList = new ArrayList<String>();
        mChildDataMap = new HashMap<String, List<String>>();
        List<String> childList;
        String groupKey;
        for (int i = 1; i <= 10; i++) {
            groupKey = "group " + i;
            childList = new ArrayList<String>();
            for (int j = 1; j <= 5; j++) {
                childList.add("child " + j);
            }
            mGroupDataList.add(groupKey);
            mChildDataMap.put(groupKey, childList);
        }

        if (mSlideImageView != null) {
            mSlideImageView.setImageResource(R.mipmap.ic_launcher);
        }
        if (mSlideGroupTextView != null && mGroupDataList.size() > 0) {
            mSlideGroupTextView.setText(mGroupDataList.get(0));
        }
    }

    @Override
    public View getHeaderView() {
        View view = View.inflate(this, R.layout.group_header, null);
        mSlideGroupTextView = (TextView) view.findViewById(R.id.tv_group);
        mSlideImageView = (ImageView) view.findViewById(R.id.iv_group);
        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public void updateHeaderView(View mHeaderView, int firstVisibleGroupPos) {
        if (mAdapter != null) {
            String group = (String) mAdapter.getGroup(firstVisibleGroupPos);
            TextView tvGroup = (TextView) mHeaderView.findViewById(R.id.tv_group);
            tvGroup.setText(group);
        }

    }

    private class MyExpandableListAdapter extends BaseExpandableListAdapter {
        private List<String> mGroupDataList;
        private Map<String, List<String>> mChildDataMap;
        private LayoutInflater mLayoutInflater;
        private Context mContext;

        public MyExpandableListAdapter(Context context, List<String> groupDataList,
                                       Map<String, List<String>> childDataMap) {
            this.mContext = context;
            this.mGroupDataList = groupDataList;
            this.mChildDataMap = childDataMap;
            this.mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getGroupCount() {
            return mGroupDataList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mChildDataMap.get(mGroupDataList.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroupDataList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mChildDataMap.get(mGroupDataList.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override //稳定ids
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.group_header, null);
                holder = new GroupHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            holder.tvGroup.setText(mGroupDataList.get(groupPosition));
            holder.ivGroup.setImageResource(R.mipmap.ic_launcher);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            System.out.println("groupPosition=" + groupPosition);
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.child_item, null);
                holder = new ChildHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            String group = mGroupDataList.get(groupPosition);
            List<String> childList = mChildDataMap.get(group);
            String child = childList.get(childPosition);
            holder.tvChild.setText(child);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }

    static class GroupHolder {
        ImageView ivGroup;
        TextView tvGroup;
        GroupHolder (View groupView) {
            ivGroup = (ImageView) groupView.findViewById(R.id.iv_group);
            tvGroup = (TextView) groupView.findViewById(R.id.tv_group);
        }
    }

    static class ChildHolder {
        TextView tvChild;
        ChildHolder (View groupView) {
            tvChild = (TextView) groupView.findViewById(R.id.tv_child);
        }
    }
}
