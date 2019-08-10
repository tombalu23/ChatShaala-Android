package com.example.insta_clone;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * NOTE by Balaji (10-8-19)
 * Stack Overflow code to set uniform image sizes in GridView (Profile Activity)
 * Use SquareImageView instead of regular ImageView in layout_grid_imageview.xml
 */



public class SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }}
