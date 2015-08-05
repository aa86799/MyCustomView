package com.stone.flowlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import com.stone.flowlayout.view.FlowLayout;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/8/4 15 06
 */
public class MainActivity extends Activity {

    FlowLayout mFlowLayout;

    String[] ary = {"小米4s小米电源", "Button ImageView", "TextView", "Helloworld","割双眼皮", "Weclome Hi ",
            "雾岛奈津美", "TextView", "多彩多姿","旁流水处理器", " 股票周末开户", "Button ImageView",
            "TextView", "Helloworld","Button ImageView", "TextView", "Helloworld",
            " 内衣加盟李宁T恤", "Button ImageView", "TextView", "Helloworld","双眼皮埋线不雅视频",
            "Button Text", "吴莫愁捉妖记彩蛋", "令计划之弟潜逃美国","哈林指甲发黑掉落~#$%$%^*"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mFlowLayout = (FlowLayout) findViewById(R.id.fl_layout);

        Button btn;
        for (int i = 0; i < ary.length; i++) {
            btn = new Button(this);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            lp.topMargin = i*5;
            lp.leftMargin = i*5;
            btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_bg));
            btn.setLayoutParams(lp);
            btn.setText(ary[i]);
            mFlowLayout.addView(btn);
        }
    }
}
