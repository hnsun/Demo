package com.hnsun.myaccount.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ResUtil;

/**
 * 自动为字符串添加冒号
 * @author hnsun
 * @date 2016/08/23
 */
public class ColonTextView extends TextView {

	public ColonTextView(Context context) {
		this(context, null);
	}

	public ColonTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColonTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData(attrs, defStyleAttr);
	}

	private void initData(AttributeSet attrs, int defStyleAttr) {
		CharSequence text = null;

		if(UtilBoss.ObjUtil.isNotNull(getContext()) && UtilBoss.ObjUtil.isNotNull(attrs)) {
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ColonTextView);
			text = ResUtil.FromAttrs.text(getContext(), typedArray, R.styleable.ColonTextView_text);
			typedArray.recycle();
		}

		setText((UtilBoss.StrUtil.isEmpty((String) text) ? getText() : text) + "：");
	}
	
}
