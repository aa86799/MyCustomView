package com.stone.customview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.stone.customview.widget.MyScrollView;
import com.stone.customview.widget.MyScrollViewUseSystemScroller;

public class ScrollViewActivityUseSystemScroller extends Activity {
	private MyScrollViewUseSystemScroller myscrollVew;
	private RadioGroup radio_group;

	// 图片id
	private int ids[] = { R.drawable.a1, R.drawable.a2, R.drawable.a3,
			R.drawable.a4, R.drawable.a5, R.drawable.a6 };

//	private ArrayList<ImageView> imageViews;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroll_main_use_system_scroll);
		myscrollVew = (MyScrollViewUseSystemScroller) findViewById(R.id.myscrollVew);
		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		
		
		//初始化数据
//		imageViews = new ArrayList<ImageView>();
		for(int i=0;i<ids.length;i++){
			
			//根据资源ID创建图片-具体的某一个页面
			ImageView iv = new ImageView(this);
			iv.setImageResource(ids[i]);
			
			myscrollVew.addView(iv);
		}
		
		//添加测试页面
		View view = View.inflate(this, R.layout.test, null);
		myscrollVew.addView(view, 2);
		
		
		//根据有多少个页面添加多少个点
		for(int i=0;i<myscrollVew.getChildCount();i++){
			RadioButton button = new RadioButton(this);
			button.setId(i);
			//添加到radioGroup
			radio_group.addView(button);
			if(i == 0){
				button.setChecked(true);
			}
			
		}
		
		//设置监听页面改变
		myscrollVew.setOnPageChangeListener(new MyScrollViewUseSystemScroller.OnPageChangeListener() {
			//刚好的ID和对应的位置下标相对应
			@Override
			public void moveTo(int index) {
				//设置其他点都是默认
//				for(int i=0;i<radio_group.getChildCount();i++){
//					RadioButton button = (RadioButton) radio_group.getChildAt(i);
//					button.setChecked(false);
//				}
//				//设置当前页面对应的下标高亮
//				RadioButton button = (RadioButton) radio_group.getChildAt(index);
//				button.setChecked(true);
				radio_group.check(index);
				
				
				
			}
		});
		
		//设置点击那个下标点就跳转到那个要么
		radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//刚好id的值和下标位置一一对应
			//checkedId :当成是下标点的位置
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				myscrollVew.moveTo(checkedId);//跳转到具体的位置
			}
		});
		
		
		
	}
}
