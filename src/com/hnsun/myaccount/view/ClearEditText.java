package com.hnsun.myaccount.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.mess.TextChangeAdapter;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ResUtil;

/**
 * 带清除的输入框
 * @author hnsun
 * @date 2016/09/24
 */
public class ClearEditText extends EditText {

	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData(attrs, defStyleAttr);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) { //根据点击位置确定按钮
		if(UtilBoss.ObjUtil.isNotNull(getCompoundDrawables()[2])) {
			switch(event.getAction()) {
				case MotionEvent.ACTION_UP:
					int touchX = getWidth() - getPaddingRight(); //右边图标位置
					boolean touchDelete = (event.getX() > (touchX - deleteDrawable.getIntrinsicWidth()) && event.getX() < touchX); 
					if(touchDelete) setText("");
					break;
			}
		}
		return super.onTouchEvent(event);
	}

	private void initData(AttributeSet attrs, int defStyleAttr) {
		deleteDrawable = getCompoundDrawables()[2]; //右侧图标
		if(UtilBoss.ObjUtil.isNull(deleteDrawable)) deleteDrawable = ResUtil.getDrawable(getContext(), R.drawable.et_ic_delete);
		deleteDrawable.setBounds(0, 0, deleteDrawable.getIntrinsicWidth(), deleteDrawable.getIntrinsicHeight());
		
		setDeleteDrawableVisible(false);
		addTextChangedListener(textWatcher); //add可以多个不影响
		setOnFocusChangeListener(onFocusChangeListener);
	}
	
	private void setDeleteDrawableVisible(boolean visible) { //控制清除图标的显示
		Drawable right = visible ? deleteDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}
	
	private Drawable deleteDrawable; //清除图标
	
	private TextWatcher textWatcher = new TextChangeAdapter() {

		@Override
		public void onTextChanged(CharSequence str, int start, int count, int after) {
			setDeleteDrawableVisible(str.length() > 0);
		}
	};
	
	private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() { //根据内容显示图标
		
		@Override
		public void onFocusChange(View view, boolean hasFocus) {
			setDeleteDrawableVisible(hasFocus && getText().length() > 0);
		}
	};
}
