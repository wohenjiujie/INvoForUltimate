package com.graduationproject.invoforultimate.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by INvo
 * on 2019-11-02.
 * 自定义 RecyclerView
 */
public class DynamicHeightRecyclerView extends RecyclerView {
    private int mMaxHeight;

    /**
     * 设置最大高度
     * @param maxHeight 最大高度 px
     */
    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
        requestLayout();
    }

    public DynamicHeightRecyclerView(Context context) {
        super(context);
    }

    public DynamicHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (mMaxHeight > 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(mMaxHeight, View.MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
