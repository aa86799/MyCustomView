/**
 * Copyright © 2013 CVTE. All Rights Reserved.
 */
package com.stone.canvaspath;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stone.canvaspath.baiduread.InhaleMesh;
import com.stone.canvaspath.baiduread.LeftMesh;
import com.stone.canvaspath.baiduread.RightMesh;

import java.util.ArrayList;

/**
 * @author Taolin
 * @version v1.0
 * @description TODO
 * @date Dec 30, 2013
 */
public class LeftRightInhaleActivity extends Activity {

    private static final boolean DEBUG_MODE = true;
    private LeftMesh mLeftView;
    private RightMesh mRightView;
    ViewPager mViewPager;
    ArrayList<View> viewList = new ArrayList<View>();
    MyAdapter adapter;
    float mpositionOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_acti);
//        Log.i("yunli", "dip2px 270 = " + dip2px(getApplicationContext(), 270));
//        Log.i("yunli", "dip2px 80 = " + dip2px(getApplicationContext(), 80));
        mLeftView = (LeftMesh) findViewById(R.id.left_view);
        mRightView = (RightMesh) findViewById(R.id.right_view);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view1, null);
        View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view2, null);
        viewList.add(view1);
        viewList.add(view2);
        adapter = new MyAdapter();
        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int arg2) {
                // TODO Auto-generated method stub
//				Log.i("yunli", "position = " + position + ",positionOffset = " + positionOffset);
                mpositionOffset = positionOffset;

                if (position == 1 && mpositionOffset == 0) {
                    mpositionOffset = 1;
                }
                if (mLeftView != null) {
                    mLeftView.drawMeshes((int) (mpositionOffset * InhaleMesh.HORIZONTAL_SPLIT));
                    mRightView.drawMeshes((int) ((1.0 - mpositionOffset) * InhaleMesh.HORIZONTAL_SPLIT));
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Log.i("yunli", "scale = " + scale);
        return (int) (dipValue * scale + 0.5f);
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(viewList.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    }
}
