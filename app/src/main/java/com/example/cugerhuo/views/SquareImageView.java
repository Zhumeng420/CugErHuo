package com.example.cugerhuo.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 正方形的ImageView
 * @Author: 李柏睿
 * @Time: 2023/4/19 20:03
 */
public class SquareImageView extends androidx.appcompat.widget.AppCompatImageView {

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize, widthSize);
    }
}
