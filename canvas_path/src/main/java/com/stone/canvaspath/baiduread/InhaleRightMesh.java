package com.stone.canvaspath.baiduread;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
/*add by liyun 20150327 add InhaleRightMesh*/
public class InhaleRightMesh extends Mesh {

    private Path mRightFirstPath;
    private Path mRightSecondPath;
    private PathMeasure mRightFirstPathMeasure;
    private PathMeasure mRightSecondPathMeasure;
    private float mPathendX;
    private float mPathendY;
    private int mTimeIndex = 0;
    private Path mThirdPath;
    private Path mForthPath;

    public InhaleRightMesh(int width, int height) {
        super(width, height);
        mRightFirstPath = new Path();
        mRightSecondPath = new Path();
        mThirdPath = new Path();
        mForthPath = new Path();
        mRightFirstPathMeasure = new PathMeasure();
        mRightSecondPathMeasure = new PathMeasure();
    }

    @Override
    public void buildPaths(float endX, float endY) {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, did you call setBitmapSize(int, int) method?");
        }
        mRightFirstPathMeasure.setPath(mRightFirstPath, false);
        mRightSecondPathMeasure.setPath(mRightSecondPath, false);

        float w = mBmpWidth;
        float h = mBmpHeight;
        
        mPathendX = endX;
        mPathendY = endY;

        mRightFirstPath.reset();
        mRightSecondPath.reset();

        mRightFirstPath.moveTo(w, h/2);
        mRightSecondPath.moveTo(w, h/2);
        mRightFirstPath.cubicTo(w, h/2-10,15*endX/8,10,13*endX/ 8,10);
        mRightSecondPath.cubicTo(w, h/2+10,15*endX/8,h-10, 13*endX/8, h-10);

        mRightFirstPath.quadTo(11*endX/ 8, 15, endX, endY-4);
        mRightSecondPath.quadTo(11*endX/8,h-15, endX, endY+4);
    }
    

    public void buildPaths(int timeIndex){
    	float w = mBmpWidth;
        float h = mBmpHeight;
        
        mTimeIndex = timeIndex;
    	
        mRightFirstPathMeasure.setPath(mRightFirstPath, false);
        mRightSecondPathMeasure.setPath(mRightSecondPath, false);
        float[] pos1 = {0.0f, 0.0f};
        float[] pos2 = {0.0f, 0.0f};
        float firstLen = mRightFirstPathMeasure.getLength();
        float secondLen = mRightSecondPathMeasure.getLength();
        float len1 = firstLen / mHorizontalSplit;
        float len2 = secondLen / mHorizontalSplit;
        float firstPointDist = timeIndex * len1;
        float secondPointDist = timeIndex * len2;
        mRightFirstPathMeasure.getPosTan(firstPointDist, pos1, null);
        mRightSecondPathMeasure.getPosTan(secondPointDist, pos2, null);
		
		w = mPathendX;
    	
//		Log.i("yunli", "timeIndex = " + timeIndex +", w = " + w);
		if(timeIndex < 4*mHorizontalSplit/10){
			mThirdPath.reset();
			mForthPath.reset();
	        mThirdPath.moveTo(pos1[0], h/2);
	        mForthPath.moveTo(pos2[0], h/2);
	        mThirdPath.cubicTo(pos1[0], pos1[1],pos1[0]-w/8,10,13*w/8,10);
	        mForthPath.cubicTo(pos2[0], pos2[1],pos2[0]-w/8,h-10, 13*w/8, h-10);
	
	        mThirdPath.quadTo((11*w ) / 8, 15, w, h/2-4);
	        mForthPath.quadTo((11*w ) / 8,h-15, w, h/2+4);
		}else if(timeIndex == 4*mHorizontalSplit/10){
			mThirdPath.reset();
			mForthPath.reset();
	        mThirdPath.moveTo(pos1[0], h/2);
	        mForthPath.moveTo(pos2[0], h/2);
	        mThirdPath.cubicTo(pos1[0], pos1[1],11*w/8,10,w,h/2-4);
	        mForthPath.cubicTo(pos2[0], pos2[1],11*w/8,h-10, w, h/2+4);
	        
		}else{
			mThirdPath.reset();
			mForthPath.reset();
	        mThirdPath.moveTo(pos1[0], h/2);
	        mForthPath.moveTo(pos2[0], h/2);
	        mThirdPath.quadTo(pos1[0], pos1[1], w, h/2-4);
	        mForthPath.quadTo(pos2[0], pos2[1], w, h/2+4);
		}
    	
    }
    
    @Override
    public void buildMeshes(int timeIndex) {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, did you call setBitmapSize(int, int) method?");
        }
        
        if(timeIndex == 0){
        	mRightFirstPathMeasure.setPath(mRightFirstPath, false);
            mRightSecondPathMeasure.setPath(mRightSecondPath, false);
        }else{
        	mRightFirstPathMeasure.setPath(mThirdPath, false);
        	mRightSecondPathMeasure.setPath(mForthPath, false);
        }

        int index = 0;
        float[] pos1 = {0.0f, 0.0f};
        float[] pos2 = {0.0f, 0.0f};
        float firstLen = mRightFirstPathMeasure.getLength();
        float secondLen = mRightSecondPathMeasure.getLength();

        float len1 = firstLen / mHorizontalSplit;
        float len2 = secondLen / mHorizontalSplit;

        for (int x = 0; x <= mHorizontalSplit ; x++) {
        	
            mRightFirstPathMeasure.getPosTan(x * len1, pos1, null);
            mRightSecondPathMeasure.getPosTan(x * len2, pos2, null);

            float fx1 = pos1[0];
            float fx2 = pos2[0];
            float fy1 = pos1[1];
            float fy2 = pos2[1];

            float dy = fy2 - fy1;
            float dx = fx2 - fx1;

            for (int y = 0; y <= mVerticalSplit; y++) {
                float fx = dx * y / mVerticalSplit;
                float fy = dy * y / mVerticalSplit;

                mVertices[index * 2 + 0] = fx + fx1;
                mVertices[index * 2 + 1] = fy + fy1;
                
                
                index += 1;
            }
        }
    }

    public Path[] getPaths() {
    	if(mTimeIndex == 0){
    		return new Path[]{mRightFirstPath, mRightSecondPath};
    	}else{
    		return new Path[]{mThirdPath, mForthPath};
    	}
    }

	
}