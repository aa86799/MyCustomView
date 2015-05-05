package com.stone.customview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.stone.customview.R;

import java.util.List;
import java.util.Map;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/4/28 11 02
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {

    private List<String> mGroupDataList;
    private Map<String, List<String>> mChildDataMap;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public ExpandableAdapter(Context context, List<String> groupDataList,
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
            convertView = mLayoutInflater.inflate(R.layout.group, null);
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

    private static class GroupHolder {
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


    private class SimpleAdapter extends SimpleExpandableListAdapter {

        public SimpleAdapter(Context context, List<? extends Map<String, ?>> groupData,
                             int groupLayout, String[] groupFrom, int[] groupTo,
                             List<? extends List<? extends Map<String, ?>>> childData,
                             int childLayout, String[] childFrom, int[] childTo) {
            super(context, groupData, groupLayout, groupFrom, groupTo,
                    childData, childLayout, childFrom, childTo);
        }

        public SimpleAdapter(Context context, List<? extends Map<String, ?>> groupData,
                             int expandedGroupLayout, int collapsedGroupLayout,
                             String[] groupFrom, int[] groupTo,
                             List<? extends List<? extends Map<String, ?>>> childData,
                             int childLayout, String[] childFrom, int[] childTo) {
            super(context, groupData, expandedGroupLayout, collapsedGroupLayout, groupFrom,
                    groupTo, childData, childLayout, childFrom, childTo);
        }

        public SimpleAdapter(Context context, List<? extends Map<String, ?>> groupData,
                             int expandedGroupLayout, int collapsedGroupLayout,
                             String[] groupFrom, int[] groupTo,
                             List<? extends List<? extends Map<String, ?>>> childData,
                             int childLayout, int lastChildLayout,
                             String[] childFrom, int[] childTo) {
            super(context, groupData, expandedGroupLayout, collapsedGroupLayout, groupFrom,
                    groupTo, childData, childLayout, lastChildLayout, childFrom, childTo);
        }

    }
}
