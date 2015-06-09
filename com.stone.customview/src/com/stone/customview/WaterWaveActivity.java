package com.stone.customview;

import com.stone.customview.widget.WaterWaveView;

import android.app.Activity;
import android.os.Bundle;

public class WaterWaveActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new WaterWaveView(this));
	}
}
