package com.stone.canvaspath.baiduread.indicator;


public abstract class baseIndicator {
	protected int HORIZONTAL_SPLIT = 10;
	protected int VERTICAL_SPLIT = 6;
	protected int mBmpWidth = -1;
	protected int mBmpHeight = -1;
	protected final float[] mVertices = new float[2 * ((1 + this.HORIZONTAL_SPLIT) * (1 + this.VERTICAL_SPLIT))];

	public final int getHorizontal() {
		return this.HORIZONTAL_SPLIT;
	}

	public final void addMesh(float paramFloat1, float paramFloat2) {
		int j = 0;
		for (int i = 0; i <= VERTICAL_SPLIT; i++) {
			float f1 = paramFloat2 * i / VERTICAL_SPLIT;
			for (int k = 0; k <= HORIZONTAL_SPLIT; k++) {
				float f2 = paramFloat1 * k / HORIZONTAL_SPLIT;
				float[] arrayOfFloat = this.mVertices;
				arrayOfFloat[(0 + j * 2)] = f2;
				arrayOfFloat[(1 + j * 2)] = f1;
				j++;
			}
		}
	}

	public final void setWidthandHeight(int width, int height) {
		this.mBmpWidth = width;
		this.mBmpHeight = height;
	}

	public final int getVertical() {
		return this.VERTICAL_SPLIT;
	}

	public final float[] getAllPoints() {
		return this.mVertices;
	}
	
}
