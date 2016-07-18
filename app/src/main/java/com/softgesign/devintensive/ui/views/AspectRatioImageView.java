package com.softgesign.devintensive.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.softgesign.devintensive.R;

/**
 * Created by Пан on 13.07.2016.
 */
public class AspectRatioImageView extends ImageView {

    private static final float DEFAULT_ASPECT_RATIO = 1.73f;
    private final float aspectRatio;

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a =context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
        aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspect_ratio, DEFAULT_ASPECT_RATIO);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth;
        int newHeight;
        newWidth=getMeasuredWidth();
        newHeight=(int)(newWidth/aspectRatio);
        setMeasuredDimension(newWidth,newHeight);

    }
}
