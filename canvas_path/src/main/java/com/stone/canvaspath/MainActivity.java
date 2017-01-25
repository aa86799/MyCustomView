package com.stone.canvaspath;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/5/18 10 21
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_acti);


        System.out.println("90度正切值" + Math.tan(Math.PI / 2));
        System.out.println("90度正切值对应弧度：" + Math.atan(Math.tan(Math.PI / 2)));
        System.out.println("90度正切值对应弧度, 所对应的角度：" +
                Math.toDegrees(Math.atan(Math.tan(Math.PI / 2))));

        System.out.println("---------------");

        double x = 90, y = 60; //如角A的对边是y，邻边为x
        System.out.println("正切对应的弧度值：" + Math.atan2(y, x));
        System.out.println("该弧度对应的角度值：" + Math.toDegrees(Math.atan2(y, x)));
        System.out.println("该弧度对应的正切值：" + Math.tan(Math.atan2(y, x)));
        System.out.println("正切值：" + (y / x));
        System.out.println("正切对应的弧度值：" + Math.atan(y / x));
    }

    private void startActi(Class<? extends Activity> clz) {
        startActivity(new Intent(this, clz));
    }

    public void testPath(View view) {
        startActi(TestActivity.class);
    }

    public void earth(View view) {
        startActi(EarthActivity.class);
    }

    public void flow(View view) {
        startActi(FlowActivity.class);
    }

    public void arrowRotate(View view) {
        startActi(ArrowRotateActivity.class);
    }

    public void jane(View view) {
        startActi(JaneActivity.class);
    }

    public void bitmapMesh(View view) {
        startActi(BitmapMeshActivity.class);
    }

    public void inhale(View view) {
        startActi(InhaleActivity.class);
    }

    public void leftRightInhale(View view) {
        startActi(LeftRightInhaleActivity.class);
    }

    public void waterWave(View view) {
        startActi(WaterWaveTransActivity.class);
    }

    public void horiTurn(View view) {
        startActi(HorizontalTurnActivity.class);
    }

    public void bounceCircle(View view) {
        startActi(BounceCircleActivity.class);
    }

    public void quad(View view) {
        startActivity(new Intent(this, BezierActivity.class).putExtra("type", 0));
    }

    public void dynamicQuad(View view) {
        startActi(BezierActivity.class);
        startActivity(new Intent(this, BezierActivity.class).putExtra("type", 1));
    }

    public void cubic(View view) {
        startActi(BezierActivity.class);
        startActivity(new Intent(this, BezierActivity.class).putExtra("type", 2));
    }

}
