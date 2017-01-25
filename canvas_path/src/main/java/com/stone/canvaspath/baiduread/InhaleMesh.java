package com.stone.canvaspath.baiduread;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;

/*add by liyun 20150327 add InhaleMesh*/
public class InhaleMesh extends Mesh {

    private Path mFirstPath;
    private Path mSecondPath;
    private Path mThirdPath;
    private Path mForthPath;
    private PathMeasure mFirstPathMeasure;
    private PathMeasure mSecondPathMeasure;
    private float mPathendX;
    private float mPathendY;
    private int mTimeIndex = 0;
    public InhaleMesh(int width, int height) {
        super(width, height);
        mFirstPath = new Path();
        mSecondPath = new Path();
        mThirdPath = new Path();
        mForthPath = new Path();
        mFirstPathMeasure = new PathMeasure();
        mSecondPathMeasure = new PathMeasure();
    }

    @Override
    public void buildPaths(float endX, float endY) {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, did you call setBitmapSize(int, int) method?");
        }
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);
        
        mPathendX = endX;
        mPathendY = endY;

        float w = mBmpWidth;
        float h = mBmpHeight;

        mFirstPath.reset();
        mSecondPath.reset();
        mFirstPath.moveTo(0, h/2);
        mSecondPath.moveTo(0, h/2);
        mFirstPath.cubicTo(0, h/2-10,endX/8,10,3*endX/ 8,10);
        mSecondPath.cubicTo(0, h/2 +10,endX/8,h-10, 3*endX/8, h-10);

        mFirstPath.quadTo((5*endX ) / 8, 15, endX, endY-4);
        mSecondPath.quadTo((5*endX ) / 8,h-15, endX, endY+4);
    }

    public void buildPaths(int timeIndex){
    	float w = mBmpWidth;
        float h = mBmpHeight;
        
        mTimeIndex = timeIndex;
    	
    	mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);
        float[] pos1 = {0.0f, 0.0f};
        float[] pos2 = {0.0f, 0.0f};
        float firstLen = mFirstPathMeasure.getLength();
        float secondLen = mSecondPathMeasure.getLength();
        float len1 = firstLen / mHorizontalSplit;
        float len2 = secondLen / mHorizontalSplit;
        float firstPointDist = timeIndex * len1;
        float secondPointDist = timeIndex * len2;
        mFirstPathMeasure.getPosTan(firstPointDist, pos1, null);
		mSecondPathMeasure.getPosTan(secondPointDist, pos2, null);
		
		w = mPathendX;
    	
		Log.i("yunli", "timeIndex = " + timeIndex +", w = " + w);
		if(timeIndex < 4*mHorizontalSplit/10){
			mThirdPath.reset();
			mForthPath.reset();
	        mThirdPath.moveTo(pos1[0], h/2);
	        mForthPath.moveTo(pos2[0], h/2);
	        mThirdPath.cubicTo(pos1[0], pos1[1],w/8+pos1[0],10,3*w/8,10);
	        mForthPath.cubicTo(pos2[0], pos2[1],w/8+pos2[0],h-10, 3*w/8, h-10);
	
	        mThirdPath.quadTo((5*w ) / 8, 15, w, h/2-4);
	        mForthPath.quadTo((5*w ) / 8,h-15, w, h/2+4);
		}else if(timeIndex == 4*mHorizontalSplit/10){
			mThirdPath.reset();
			mForthPath.reset();
	        mThirdPath.moveTo(pos1[0], h/2);
	        mForthPath.moveTo(pos2[0], h/2);
	        mThirdPath.cubicTo(pos1[0], pos1[1],5*w/8,15,w,h/2-4);
	        mForthPath.cubicTo(pos2[0], pos2[1],5*w/8,h-15, w, h/2+4);
	        
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
//    	long temp = System.currentTimeMillis();
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, did you call setBitmapSize(int, int) method?");
        }
        if(timeIndex == 0){
        	mFirstPathMeasure.setPath(mFirstPath, false);
            mSecondPathMeasure.setPath(mSecondPath, false);
        }else{
        	mFirstPathMeasure.setPath(mThirdPath, false);
        	mSecondPathMeasure.setPath(mForthPath, false);
        }

        int index = 0;
        float[] pos1 = {0.0f, 0.0f};
        float[] pos2 = {0.0f, 0.0f};
        float firstLen = mFirstPathMeasure.getLength();
        float secondLen = mSecondPathMeasure.getLength();
        float len1 = firstLen / mHorizontalSplit;
        float len2 = secondLen / mHorizontalSplit;
        for (int x = 0; x <= mHorizontalSplit; x++) {
			mFirstPathMeasure.getPosTan( x * len1, pos1, null);
			mSecondPathMeasure.getPosTan(x * len2, pos2, null);
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
    
//    @Override
//    public void buildMeshes(int timeIndex) {
////    	long temp = System.currentTimeMillis();
//        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
//            throw new IllegalArgumentException(
//                    "Bitmap size must be > 0, did you call setBitmapSize(int, int) method?");
//        }
//        mFirstPathMeasure.setPath(mFirstPath, false);
//        mSecondPathMeasure.setPath(mSecondPath, false);
//
//        int index = 0;
//        float[] pos1 = {0.0f, 0.0f};
//        float[] pos2 = {0.0f, 0.0f};
//        float firstLen = mFirstPathMeasure.getLength();
//        float secondLen = mSecondPathMeasure.getLength();
//        float len1 = firstLen / mHorizontalSplit;
//        float len2 = secondLen / mHorizontalSplit;
//
//        float firstPointDist = timeIndex * len1;
//        float secondPointDist = timeIndex * len2;
//        Log.i("yunli", "firstPointDist = " + firstPointDist);
//        for (int x = 0; x <= mHorizontalSplit; x++) {
//        	Log.i("yunli", "x*len1 = " + x*len1);
////        	if(firstPointDist > x*len1)
////        		return ;
//			mFirstPathMeasure.getPosTan( x * len1- firstPointDist, pos1, null);
//			mSecondPathMeasure.getPosTan(x * len2- secondPointDist, pos2, null);
//            float fx1 = pos1[0];
//            float fx2 = pos2[0];
//            float fy1 = pos1[1];
//            float fy2 = pos2[1];
//
//            float dy = fy2 - fy1;
//            float dx = fx2 - fx1;
//
//            for (int y = 0; y <= mVerticalSplit; y++) {
//                float fx = dx * y / mVerticalSplit;
//                float fy = dy * y / mVerticalSplit;
//                mVertices[index * 2 + 0] = fx + fx1+ firstPointDist;
//                mVertices[index * 2 + 1] = fy + fy1; 
//                index += 1;
//            }
//        }
//    }

    public Path[] getPaths() {
    	if(mTimeIndex == 0){
    		return new Path[]{mFirstPath, mSecondPath};
    	}else{
    		return new Path[]{mThirdPath, mForthPath};
    	}
    }

	
}