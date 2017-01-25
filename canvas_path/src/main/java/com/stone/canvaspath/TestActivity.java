package com.stone.canvaspath;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 16/5/18 10 21
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(new MyView(this));

        SparseArray<Point> points = new SparseArray<>();
        for (int i = 0; i < 4; i++) {
            points.put(i, new Point((int) (300 + Math.random() * 1080 / 2), (int) (300 + Math.random() * 1920 / 2)));
        }

        PathView pathView = new PathView(this);
        Path path = new Path();
//        Point point;
//        for (int i = 0; i < points.size(); i++) {
//            point = points.get(i);
//            System.out.println(point.x + "," + point.y);
//            if (i == 0) {
//                path.moveTo(point.x, point.y);  //路径原点
//            } else {
//                path.lineTo(point.x, point.y);  //默认第一个会从canvas的原点绘制
//            }
//        }
//        path.close();

        setContentView(pathView);
//        pathView.setPath(path);

        path.addRect(100, 100, 500, 300, Path.Direction.CCW);
//        path.addArc(200, 200, 400, 500, 50, 250); //api21 later
        path.addArc(new RectF(200, 200, 400, 500), 0, 90);
        path.addCircle(600, 600, 30, Path.Direction.CW);
//        path.addOval(200, 200, 400, 500, Path.Direction.CCW);//api21 later
        path.addOval(new RectF(100, 1000 / 2, 400, 1100 / 2), Path.Direction.CCW);
        path.addRoundRect(new RectF(200, 1000 / 2, 700, 1100), 10, 10, Path.Direction.CW);

        path.setFillType(Path.FillType.WINDING); //取path所有所在区域 path处为paint色，相交处为canvas色
//        path.setFillType(Path.FillType.INVERSE_WINDING); //取path所有未占区域 path处为canvas色，相交处为paint色
//        path.setFillType(Path.FillType.EVEN_ODD); //取path所在并不相交区域 path处为paint色，相交处为canvas色
//        path.setFillType(Path.FillType.INVERSE_EVEN_ODD); //取path未占或相交区域  path处为canvas色，相交处为paint色

//        path.toggleInverseFillType(); // 切换成相反的 填充模式

        path = new Path();
        path.moveTo(20, 20); //起始点
        path.lineTo(100, 100); //连接
        path.lineTo(200, 100);
        path.lineTo(400, 50);
//        path.close();
        path.rMoveTo(50, 50); //相对于最后一个点的50，50
        path.lineTo(150, 150);
        path.rLineTo(50, 50); //相对于最后一个点
//        path.arcTo(new RectF(300, 300, 400, 400), 0, 90, true); //弧线区rect，开始角度，sweep角度，是否moveto到新点(rect的lt)
        path.arcTo(new RectF(300, 300, 400, 400), 0, 90, false); //弧线区rect，开始角度，sweep角度，是否moveto到新点(rect的lt)
//        path.close(); //连接move点与lineto的最后一点 */

        /*path.moveTo(400, 600);
//        path.setLastPoint(20,80); // 设置图形的最后一个点位置。如果画的是个封闭图形，而这个点不在图形线上，那么这个点与最后一个图形连上线完成封闭
        path.cubicTo(400, 600, 250, 500, 400, 750);
        path.cubicTo(400, 750, 550, 500, 400, 600);
        path.close();

        path.moveTo(800, 600);
        path.quadTo(650, 560, 800, 750);
        path.quadTo(950, 560, 800, 600);*/


//        path.moveTo(400, 900);
//        path.quadTo(250, 850, 400, 1000);
//        path.quadTo(550, 850, 400, 900);

        pathView.setPath(path);

        pathView.startAnim();

//        path.getFillType()
//        path.isInverseFillType(); // 是否是inverse 逆填充模式
//        path.isEmpty(); //path是否为空，如果path不包含任何线条和曲线，则返回true，否则返回false

        RectF rr = new RectF();
        System.out.println(path.isRect(rr)); //如果path指定的是一个Rect，则返回true并填充传入参数rect，否则返回false
        System.out.println(rr);

        path.computeBounds(rr, false); //计算path所在区域，并将结果写入bounds   boolean参数未被使用
        System.out.println(rr);

        path.incReserve(24); //提示path将添加多少个point， 以致path更有效的分配存储空间

//        path.reset(); //清空path，但不改变fillType的设置

//        path.rewind(); //清除掉path里的线条和曲线,但是会保留内部的数据结构以便重用;

//        path.isConvex();//api21 path是否是凸面的 凸形的
//        path.set();  //set a new Path
//        path.setLastPoint(x, y);
//        path.transform(Matrix matrix); //path中的点进行矩阵变换
//        path.transform(Matrix matrix, Path dst); //path中的点进行矩阵变换，将结果写入到dst中。 如果dst为null，改变原来的path
//        path.op(path, Path.Op.XOR)
//        path.op(path1, path2, Path.Op.DIFFERENCE);
//        path.offset(x, y);// 偏移
//        path.offset(x, y, Path dst); //偏移后的Path写入到dst中，如果dst为null，则改变原来的path
    }

    class MyView extends View {
        float phase;
        PathEffect[] effects = new PathEffect[7];
        int[] colors;
        private Paint paint;
        Path path;

        public MyView(Context context) {
            super(context);
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            //创建,并初始化Path
            path = new Path();
            path.moveTo(0, 0);
            for (int i = 1; i <= 15; i++) {
                //生成15个点,随机生成它们的坐标,并将它们连成一条Path
                path.lineTo(i * 20, i * (float) Math.random() * 10);
            }
            //初始化七个颜色
            colors = new int[]{
                    Color.BLACK, Color.BLUE, Color.CYAN,
                    Color.GREEN, Color.MAGENTA, Color.RED, Color.YELLOW
            };
        }

        protected void onDraw(Canvas canvas) {
            //将背景填充成白色
            canvas.drawColor(Color.WHITE);
            //-------下面开始初始化7中路径的效果
            //使用路径效果
            effects[0] = null;
            //使用CornerPathEffect路径效果: 使用圆角来代替尖角，从而对图形尖锐的边角 进行平滑处理
            effects[1] = new CornerPathEffect(30); //角落路径效果， 传入角落的圆角半径
            //DashPathEffect：创建一个虚线的轮廓(短横线/小圆点)   只对Paint的Style设为STROKE 或STROKE_AND_FILL 时有效
            effects[2] = new DashPathEffect(new float[]{5, 25, 15, 10}, phase); //传入间隔数组 与 偏移
            //DiscretePathEffect 与DashPathEffect相似，但是添加了随机性， 需要指定每一段的长度和与原始路径的偏离值
            effects[3] = new DiscretePathEffect(3.0f, 5.0f); //离散路径效果
            //PathDashPathEffect 定义一个新的路径，并将其用作原始路径的 轮廓标记
            Path p = new Path();
            p.addRect(0, 0, 8, 10, Path.Direction.CW);
            effects[4] = new PathDashPathEffect(p, 12, phase, PathDashPathEffect.Style.TRANSLATE); //定义一个新的路径，并将其用作原始路径的 轮廓标记
            //初始化PathDashPathEffect
            effects[5] = new ComposePathEffect(effects[2], effects[4]); //组合 在路径上先使用第一种效果，再在此基础上 应用第二种效果
            effects[6] = new SumPathEffect(effects[4], effects[3]); //添加两种效果，将两种效果结合起来
            //将画布平移
            canvas.translate(108, 58);
            //依次使用7中不同路径效果,7种不同的颜色来绘制路径
            for (int i = 0; i < effects.length; i++) {
                paint.setPathEffect(effects[i]);
                paint.setColor(colors[i]);
                path.setFillType(Path.FillType.WINDING); //paint-style 为 stroke时 无效
//                canvas.drawPath(path, paint);
//                canvas.translate(0, 160);


            }
            //改变phase值,形成动画效果
            phase += 1;
            invalidate();

            path = new Path();
            path.addArc(new RectF(200, 450, 400, 650), 50, 250); //受硬件加速影响 如果开启，则看不到效果   默认是开启的
//            path.addRect(new RectF(350, 650, 450, 800), Path.Direction.CCW); //受硬件加速影响 如果开启，则看不到效果   默认是开启的
//            path.setFillType(Path.FillType.WINDING);
            canvas.drawPath(path, new Paint());


        }
    }
}
