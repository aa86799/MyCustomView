package com.stone.canvaspath.baiduread;

import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

public class MeltTransAnimation extends Animation {

	private int a = 0;
	private int b;
	private OnMeltTransUpdateListener c;

	public MeltTransAnimation(int paramInt) {
		this.b = paramInt;
		this.c = null;
	}

	public final void a(OnMeltTransUpdateListener paramOnMeltTransUpdateListener) {
		this.c = paramOnMeltTransUpdateListener;
	}

	protected final void applyTransformation(float paramFloat, Transformation paramTransformation) {
		Interpolator localInterpolator = getInterpolator();
		if (localInterpolator != null)
			paramFloat = localInterpolator.getInterpolation(paramFloat);
		int i = (int) (this.a + paramFloat * (this.b - this.a));
		if (this.c != null)
			this.c.a(i);
	}
	public abstract interface OnMeltTransUpdateListener
	{
	  public abstract void a(int paramInt);
	}
}
