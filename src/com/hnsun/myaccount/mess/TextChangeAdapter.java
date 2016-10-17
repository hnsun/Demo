package com.hnsun.myaccount.mess;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * EditText的文字改变的时候适配器
 * @author hnsun
 * @date 2016/08/30
 */
public class TextChangeAdapter implements TextWatcher {

	@Override
	public void afterTextChanged(Editable editable) {}

	@Override
	public void beforeTextChanged(CharSequence str, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence str, int start, int count, int after) {}
}
