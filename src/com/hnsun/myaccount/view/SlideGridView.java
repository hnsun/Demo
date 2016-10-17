package com.hnsun.myaccount.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 内容过多时可以滑动
 * @author hnsun
 * @date 2016/09/07
 */
public class SlideGridView extends GridView {

    public SlideGridView(Context context) {
        this(context, null);
    }

    public SlideGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //重置确定高度 以便滑动
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
