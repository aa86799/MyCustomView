package com.stone.customview;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	protected <T> T getView(int resID) {
		return (T)findViewById(resID);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
