package com.stone.customview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.stone.customview.widget.MyScrollViewOriginal;
import com.stone.customview.widget.MyScrollViewOriginal.onPageChangeListener;

public class ScrollViewOriginalActivity extends Activity {
	
	RadioGroup mRadioGroup;
	MyScrollViewOriginal mScrollView;
	
	int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mm);
		mRadioGroup = (RadioGroup) findViewById(R.id.rg_pages);
		mScrollView = (MyScrollViewOriginal) findViewById(R.id.sv_pages);
		
		initViews();
	}

	private void initViews() {
		ImageView imageView;
		for (int i = 0; i < images.length; i++) {
			imageView = new ImageView(this);
			imageView.setImageResource(images[i]);
			mScrollView.addView(imageView);
		}
		
		View view = View.inflate(this, R.layout.test, null);
		mScrollView.addView(view, 2);
		
		for (int i = 0; i < mScrollView.getChildCount(); i++) {
			RadioButton radioButton = new RadioButton(this);
			radioButton.setId(i);
			radioButton.setText(i + 1 + "");
			mRadioGroup.addView(radioButton);
		}
		
		mRadioGroup.check(0);
//		mRadioGroup.setOrientation(RadioGroup.HORIZONTAL);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mScrollView.moveTo(checkedId);
			}
		});
		
		mScrollView.setOnPageChangeListener(new onPageChangeListener() {
			
			@Override
			public void moveTo(int index) {
				mRadioGroup.check(index);
			}
		});
	}

}
