package com.stone.cus.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.stone.cus.R;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/8/29 16 06
 */
/*
构造方法: (Context context)  以代码调用
构造方法: (Context context, AttributeSet attrs)  以xml调用
    可以指定调用第三或第四种构造方法
构造方法: (Context context, AttributeSet attrs, int defStyleAttr)
    defStyleAttr 默认Style. 可以传入当前Theme中含有的一个style；如果传入0或不存在于当前Theme中的style，则不被应用
构造方法: (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    需要在5.0(api21)以上才可使用   (不能使用api版本判断的方式,来调用构造方法. 因必须在第一行语句就调用构造方法)
    defStyleRes 表示一个默认Style的资源.  如R.style.StoneStyle2

若定义了相同的属性, 那么被应用的优先级:  attrs > defStyleAttr > defStyleRes
 */
public class CustomView extends Button {

    public CustomView(Context context) {
        this(context, null);
        System.out.println("构造函数1");
    }

    public CustomView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this(context, attrs, 0);
        this(context, attrs, R.style.StoneStyle2);
        System.out.println("构造函数2");

    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
        this(context, attrs, defStyleAttr, R.style.StoneStyle1);
        System.out.println(Build.VERSION.SDK_INT);

        /*
        super 和this 的构造方法调用必须在第一行就使用;
        所以 像下面这样写是不行的
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            this(context, attrs, defStyleAttr, R.style.StoneStyle);
        }
        System.out.println("构造函数3");

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        System.out.println("构造函数4");

        int attributeCount = attrs.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = attrs.getAttributeName(i);
            System.out.println("attributeName----> " + attributeName + ", 值是" + attrs.getAttributeValue(i));
        }

//        TintTypedArray ta = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.customView);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.customView);
        String atr2 = ta.getString(R.styleable.customView_atr2);
        System.out.println("atr2====>" + atr2);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
