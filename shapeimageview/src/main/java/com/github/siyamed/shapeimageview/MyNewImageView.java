package com.github.siyamed.shapeimageview;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.shader.ShaderHelper;
import com.github.siyamed.shapeimageview.shader.SvgShader;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/6/9 12 01
 */
public class MyNewImageView extends ShaderImageView {

    public MyNewImageView(Context context) {
        super(context);
    }

    public MyNewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNewImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader(R.raw.mynew);
    }
}