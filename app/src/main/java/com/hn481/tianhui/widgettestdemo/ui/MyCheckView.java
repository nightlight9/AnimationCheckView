package com.hn481.tianhui.widgettestdemo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.hn481.tianhui.widgettestdemo.R;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Author:          Leo
 * Version          V1.0
 * Date:            2016/4/18.
 * Description:
 */
public class MyCheckView extends View {

    private static final int DEFAULT_LINE_WIDTH    = 4;
    private static final int DEFAULT_TEXT_COLOR    = Color.BLACK;
    private static final int DEFAULT_TEXT_CHECKED_COLOR    = Color.WHITE;
    private static final int DEFAULT_CHECKED_COLOR = Color.RED;
    private static final int DEFAULT_UNCHECK_COLOR = Color.GRAY;
    private static final int DEFAULT_ANIM_DURATION = 150 ;


    private Paint mPaint,secondPaint;
    private int width, height;             //控件宽高
    private boolean isAnim;
    private int currentWidth;//目前动画的宽度

    //视图上的文字
    private String text;
    private int textSize;
    //视图文字的范围,想让文字居中要用到这个
    private Rect mBound;
    //是否是checked状态
    private boolean isChecked;

    private int animDuration = DEFAULT_ANIM_DURATION;
    private int unCheckColor = DEFAULT_UNCHECK_COLOR;
    private int checkedColor  = DEFAULT_CHECKED_COLOR;
    private int textColor = DEFAULT_TEXT_COLOR;
    private int textCheckedColor =DEFAULT_TEXT_CHECKED_COLOR;
    private int correctWidth = DEFAULT_LINE_WIDTH;



    public MyCheckView(Context context) {
        this(context, null);
    }

    public MyCheckView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCheckView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MyCheckView_text:
                    text = a.getString(attr);
                    break;
                case R.styleable.MyCheckView_isChecked:
                    isChecked = a.getBoolean(attr, false);
                    break;
                case R.styleable.MyCheckView_textSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;

            }
        }
        a.recycle();
        init(context);
    }

    //初始化
    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(textSize);
        //改变颜色的补充画笔
        secondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mBound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mBound);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
                    showUncheck();
                } else {
                    showCheck();
                }
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(!isChecked){
            mPaint.setColor(unCheckColor);
            canvas.drawRect(currentWidth, 0, width, height, mPaint);

            secondPaint.setColor(checkedColor);
            canvas.drawRect(0, 0, currentWidth, height, secondPaint);

            mPaint.setColor(textColor);
            canvas.drawText(text, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
        }else{
            mPaint.setColor(checkedColor);
            //canvas.drawRect(0,0,currentWidth,height,mPaint);
            canvas.drawRect(0,0,currentWidth,height,mPaint);

            secondPaint.setColor(unCheckColor);
            canvas.drawRect(currentWidth, 0, width, height, secondPaint);

            mPaint.setColor(textCheckedColor);
            canvas.drawText(text, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
        }

    }

    private void showCheck() {
        if (isAnim) {
            return;
        }
        isChecked = true;
        isAnim = true;
        ValueAnimator va = ValueAnimator.ofFloat(0, 1).setDuration(animDuration);
        va.setInterpolator(new LinearInterpolator());
        va.start();
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue(); // 0f ~ 1f
                currentWidth = (int) ( value * width);
                if (value >= 1) {

                    isAnim = false;
                    if (listener != null) {
                        listener.onCheckedChanged(MyCheckView.this, false);
                    }
                }
                invalidate();
            }
        });
    }

    private void showUncheck() {
        if (isAnim) {
            return;
        }
        isAnim = true;
        isChecked = false;
        ValueAnimator va = ValueAnimator.ofFloat(0, 1).setDuration(animDuration);
        va.setInterpolator(new LinearInterpolator());
        va.start();
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue(); // 0f ~ 1f
                currentWidth = (int) ((1 - value)  * width);
                if (value >= 1) {

                    isAnim = false;
                    if (listener != null) {
                        listener.onCheckedChanged(MyCheckView.this, true);
                    }
                }
                invalidate();
            }
        });
    }




    /**
     * 确定尺寸坐标
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height =  h - getPaddingBottom() - getPaddingTop();
        width  =  w - getPaddingLeft() - getPaddingRight();

    }


    /**
     * 返回当前选中状态
     *
     * @return
     */
    public boolean isChecked() {
        return isChecked;
    }

    private OnCheckedChangeListener listener;

    public interface OnCheckedChangeListener{
        void onCheckedChanged(View buttonView, boolean isChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }
}

