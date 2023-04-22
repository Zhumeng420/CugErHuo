package com.example.cugerhuo.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.cugerhuo.R;

/**
 * 滑动开关选择
 * @Author: 李柏睿
 * @Time: 2023/4/22 14:11
 */
public class SlideSwitch extends View{

    /**抗锯齿*/
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public boolean isOn = false;
    float curX = 0;
    /**y固定*/
    float centerY;
    float viewWidth;
    float radius;
    /**直线段开始的位置（横坐标）*/
    float lineStart;
    /**直线段结束的位置（纵坐标）*/
    float lineEnd;
    float lineWidth;
    /**控件长度为滑动的圆的半径的倍数*/
    final int SCALE = 4;
    /**是否为默认地址*/
    int flag = 0;

    OnStateChangedListener onStateChangedListener;

    public SlideSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SlideSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideSwitch(Context context) {
        super(context);
    }

    /**
     * 触摸选择事件
     * @Author: 李柏睿
     * @Time: 2023/4/22 14:51
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        curX = event.getX();
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            if(curX > viewWidth / 2)
            {
                curX = lineEnd;
                if(false == isOn)
                {
                    //只有状态发生改变才调用回调函数， 下同
                    if(null != onStateChangedListener)
                    {
                        onStateChangedListener.onStateChanged(true);
                    }
                    isOn = true;
                }
            }
            else
            {
                curX = lineStart;
                if(true == isOn)
                {
                    if(null != onStateChangedListener)
                    {
                        onStateChangedListener.onStateChanged(false);
                    }
                    isOn = false;
                }
            }
        }
        /**通过刷新调用onDraw*/
        this.postInvalidate();
        return true;
    }

    public void setChecked(){
        flag = 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**保持宽是高的SCALE / 2倍， 即圆的直径*/
        this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredWidth() * 2 / SCALE);
        viewWidth = this.getMeasuredWidth();
        radius = viewWidth / SCALE;
        //直线宽度等于滑块直径
        lineWidth = radius * 2f;
        curX = radius;
        //centerY为高度的一半
        centerY = this.getMeasuredWidth() / SCALE;
        lineStart = radius;
        lineEnd = (SCALE - 1) * radius;
    }

    @SuppressLint({"DrawAllocation", "ResourceAsColor"})
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**限制滑动范围*/
        curX = curX > lineEnd?lineEnd:curX;
        curX = curX < lineStart?lineStart:curX;
        if(flag == 1){curX = lineEnd;}
        /**划线*/
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);
        /**左边部分的线，橘色*/
        mPaint.setColor(Color.parseColor("#FF5722"));
        canvas.drawLine(lineStart, centerY, curX, centerY, mPaint);
        /**右边部分的线，灰色*/
        mPaint.setColor(Color.LTGRAY);
        canvas.drawLine(curX, centerY, lineEnd, centerY, mPaint);
        /**画圆*/
        /**画最左和最右的圆，直径为直线段宽度， 即在直线段两边分别再加上一个半圆*/
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.LTGRAY);
        canvas.drawCircle(lineEnd, centerY, lineWidth / 2, mPaint);
        mPaint.setColor(Color.parseColor("#FF5722"));
        canvas.drawCircle(lineStart, centerY, lineWidth / 2, mPaint);
        /**圆形滑块*/
        mPaint.setColor(Color.parseColor("#F5F5F5"));
        canvas.drawCircle(curX, centerY, radius , mPaint);
    }
    /**设置开关状态改变监听器*/
    public void setOnStateChangedListener(OnStateChangedListener o)
    {
        this.onStateChangedListener = o;
    }

    /**内部接口，开关状态改变监听器*/
    public interface OnStateChangedListener
    {
        public void onStateChanged(boolean state);
    }

}
