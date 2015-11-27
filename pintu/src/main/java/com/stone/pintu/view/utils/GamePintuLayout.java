package com.stone.pintu.view.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stone.pintu.R;

import java.util.Collections;
import java.util.List;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/8/10 16 36
 *
 * 正方形切图  n * n,  正方游戏面板  subView-rule(padding margin)
 * 动画图层(第一个点击高亮 移动动画的第二个构造方法：fromX,deltaX, fromY,deltaY)
 * 游戏监听: 下一关、时间变化(结束、重新开始)、游戏结束
 * 游戏的暂停、恢复
 */
public class GamePintuLayout extends RelativeLayout implements View.OnClickListener {

    private int mColumn = 2; //分割成几x几
    private int mPadding;
    private int mItemMargin;
    private List<ImagePiece> mItemPieces;
    private ImageView[] mItems;
    private Bitmap mBitmap;
    private boolean once; //每添加一个子view都会触发onMeasure
    private int mWidth; //游戏面板宽、高
    private int mItemWidth;

    private GamePintuListener mGamePintuListener;
    private int mLevel = 1;

    public int getLevel() {
        return mLevel;
    }

    public interface GamePintuListener {
        void nextLevel(int nextLevel);

        void timeChanged(int currentTime);

        void gameover();
    }


    public void setGamePintuListener(GamePintuListener mGamePintuListener) {
        this.mGamePintuListener = mGamePintuListener;
    }

    private static final int TIME_CHANGED = 0;
    private static final int NEXT_LEVEL = 1;
    private boolean isGameOver;
    private boolean isPause;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_CHANGED:
                    if (isGameOver || isPause) {
                        return;
                    }
                    if (mGamePintuListener != null) {
                        mGamePintuListener.timeChanged(mTime);
                        if (mTime == 0) {
                            isGameOver = true;
                            mGamePintuListener.gameover();
                        }
                    }
                    mTime--;
                    mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);
                    break;
                case NEXT_LEVEL:
                    if (mGamePintuListener != null) {
                        mGamePintuListener.nextLevel(++mLevel);
                    } else {
                        nextLevel();
                    }
                    break;
            }
        }
    };

    private boolean isTimeEnabled;
    private int mTime;

    /**
     * 设置是否启用时间
     *
     * @param isTimeEnabled
     */
    public void setTimeEnabled(boolean isTimeEnabled) {
        this.isTimeEnabled = isTimeEnabled;
    }

    public GamePintuLayout(Context context) {
        this(context, null);
    }

    public GamePintuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mItemMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom());

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.game_pintu);
        Drawable drawable = array.getDrawable(R.styleable.game_pintu_bitmap);
        mBitmap = ((BitmapDrawable) drawable).getBitmap();

    }

    private void checkTimeEnable() {
        mHandler.removeMessages(TIME_CHANGED);
        if (isTimeEnabled) {
            countTimeBaseLevel(); //根据当前等级设置时间
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    private void countTimeBaseLevel() {
        mTime = (int) (Math.pow(2, mLevel) * 30);
    }

    private int min(int... params) {
        int min = 0;
        if (params != null && params.length > 0) {
            min = params[0];
            for (int param : params) {
                if (min > param) {
                    min = param;
                }
            }
        }
        return min;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        if (!once) {
            //切图 排序
            initBitmap();

            //设置imageview的宽高属性
            initItem();
            once = true;

            checkTimeEnable();
            /*
            如果放在init中 那么find到这对象时就会被调用了。在之后设置enable就无效了
            */

        }

        setMeasuredDimension(mWidth, mWidth); //正方形
    }

    private void initBitmap() {
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mn);
        }
        Matrix matrix = new Matrix();
        float sw = mWidth * 1.00f / mBitmap.getWidth();
        float sh = mWidth * 1.00f / mBitmap.getHeight();
        matrix.postScale(sw, sh);
        Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);

        mItemPieces = ImageSplitterUtil.splitImage(mBitmap, mColumn);
        //混序
        Collections.shuffle(mItemPieces);
//        Collections.sort(mItemPieces, new Comparator<ImagePiece>() {
//            @Override
//            public int compare(ImagePiece lhs, ImagePiece rhs) {
//                return Math.random() > 0.5 ? 1 : -1;
//            }
//        });
    }

    /**
     * 设置item-imageview的宽高属性
     */
    private void initItem() {
        mItemWidth = (mWidth - mPadding * 2 - (mColumn - 1) * mItemMargin) / mColumn;
        mItems = new ImageView[mColumn * mColumn];
        int len = mItems.length;
        ImageView imageView;
        for (int i = 0; i < len; i++) {
            imageView = new ImageView(getContext());
            imageView.setOnClickListener(this);
            imageView.setImageBitmap(mItemPieces.get(i).bitmap);
            mItems[i] = imageView;
            imageView.setId(i + 1);
            imageView.setTag(i + "_" + mItemPieces.get(i).index);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
            //设置item间横向间隙
//            if (i % mColumn != (mColumn - 1)) { //不是最后一列，设置rightMargin
            if ((i + 1) % mColumn != 0) { //不是最后一列，设置rightMargin
                lp.rightMargin = mItemMargin;
            }
            if (i % mColumn != 0) {//不是第一列
                lp.addRule(RelativeLayout.RIGHT_OF, mItems[i - 1].getId());
            }

            if (i >= mColumn) { //不是第一行
                lp.topMargin = mItemMargin;
                lp.addRule(RelativeLayout.BELOW, mItems[i - mColumn].getId());
            }
            addView(imageView, lp);
        }
    }


    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View v) {
        if (isAniming) {
            return;
        }
        if (mFirst == v) { //两次点同一item 取消
            mFirst.setColorFilter(null);
            mFirst = null;
        } else {
            if (mFirst == null) {
                mFirst = (ImageView) v;
                mFirst.setColorFilter(Color.parseColor("#55ff0000")); //高亮
            } else {
                mSecond = (ImageView) v;
                exchangeView();
            }
        }

    }

    /**
     * 动画层
     */
    private RelativeLayout mAnimLayout;
    private boolean isAniming;

    /**
     * 交换item
     */
    private void exchangeView() {
        mFirst.setColorFilter(null);

        final String firstTag = (String) mFirst.getTag();
        final String secondTag = (String) mSecond.getTag();
        String[] firstParams = firstTag.split("_");
        String[] secondParams = secondTag.split("_");
        final ImagePiece firstImagePiece = mItemPieces.get(Integer.valueOf(firstParams[0]));
        final ImagePiece secondImagePiece = mItemPieces.get(Integer.valueOf(secondParams[0]));

        setUpAnimLayouyt();

        ImageView firstCopy = new ImageView(getContext());
        firstCopy.setImageBitmap(firstImagePiece.bitmap);
        LayoutParams lp1 = new LayoutParams(mItemWidth, mItemWidth);
         /*
            mAnimLayout 受padding影响
            若直接addView 则会受padding值影响，所以先减去padding 使其与外层layout一样大小
             */
        lp1.leftMargin = mFirst.getLeft() - mPadding;
        lp1.topMargin = mFirst.getTop() - mPadding;
        mAnimLayout.addView(firstCopy, lp1);//添加复制的第一个view


        ImageView secondCopy = new ImageView(getContext());
        secondCopy.setImageBitmap(secondImagePiece.bitmap);
        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        mAnimLayout.addView(secondCopy, lp2);//添加复制的第二个view


        TranslateAnimation firstAnim = new TranslateAnimation(0, mSecond.getLeft() - mFirst.getLeft(),
                0, mSecond.getTop() - mFirst.getTop());
        firstAnim.setDuration(300);
        firstAnim.setFillAfter(true);
        firstCopy.startAnimation(firstAnim);//1移动到2

        TranslateAnimation secondAnim = new TranslateAnimation(0, mFirst.getLeft() - mSecond.getLeft(),
                0, mFirst.getTop() - mSecond.getTop());
        secondAnim.setDuration(300);
        secondAnim.setFillAfter(true);
        secondCopy.startAnimation(secondAnim);//2移动到1

        firstAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);
                isAniming = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//if (mFirst == null) System.out.println("动画执行的时候需要设置点击无效 否则会刚好在start时，被上一次置为null, 这时end出现空指针");
                mFirst.setImageBitmap(secondImagePiece.bitmap);
                mSecond.setImageBitmap(firstImagePiece.bitmap);
                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);
                mFirst = mSecond = null;
                mAnimLayout.removeAllViews();
                isAniming = false;

                //判断sort
                checkSuccess();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * 构造动画层
     */
    private void setUpAnimLayouyt() {
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }
    }

    private void checkSuccess() {
        boolean isSuccess = true;
        ImageView item;
        String tag;
        String[] params;
        int index;
        for (int i = 0; i < mItems.length; i++) {
            item = mItems[i];
            tag = (String) item.getTag();
            params = tag.split("_");
            index = mItemPieces.get(Integer.valueOf(params[0])).index;
            if (index != i) {
                isSuccess = false;
            }
        }
        if (isSuccess) {
            mHandler.removeMessages(TIME_CHANGED);

            Toast.makeText(getContext(), "Success ， level up !!!",
                    Toast.LENGTH_LONG).show();
            mHandler.sendEmptyMessage(NEXT_LEVEL);

        }
    }

    public void nextLevel() {
        removeAllViews();
        mAnimLayout = null;
        mColumn++;
        initBitmap();
        initItem();
        checkTimeEnable();

    }

    public void restart() {
        isGameOver = false;
        mColumn--;
        nextLevel();
    }

    public void pause() {
        mHandler.removeMessages(TIME_CHANGED);
        isPause = true;
    }

    public void resume() {
        if (isPause) {
            isPause = false;
            mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);
        }
    }
}
