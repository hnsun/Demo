package com.hnsun.myaccount.business.passcode;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.AbstractActivity;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.platform.AnimUtil;
import com.hnsun.myaccount.util.platform.ViewUtil;

/**
 * 抽象应用锁管理界面
 * @author hnsun
 * @date 2016/09/26
 */
public abstract class AbstractPasscodeActivity extends AbstractActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_passcode_lock);
		getActionBar().hide();
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		txtvTitle = (TextView) findViewById(R.id.txtvTitle);
		etCode1 = (EditText) findViewById(R.id.etCode1);
		etCode2 = (EditText) findViewById(R.id.etCode2);
		etCode3 = (EditText) findViewById(R.id.etCode3);
		etCode4 = (EditText) findViewById(R.id.etCode4);
		
		setExit(false);
		etCode(etCode1, etCode2, etCode3, etCode4);
		
		Bundle extras = getIntent().getExtras();
		if(UtilBoss.ObjUtil.isNotNull(extras)) {
			String passcodeTitle = extras.getString(AbstractPasscodeActivity.FLAG_PASSCODE_TITLE);
			if(!UtilBoss.StrUtil.isEmpty(passcodeTitle)) setPasscodeTitle(passcodeTitle);
		}
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		findViewById(R.id.btnHelp).setOnClickListener(onClickListener);
		findViewById(R.id.btnBackspace).setOnClickListener(onClickListener);
		
		basicCreated();
	}

	protected abstract void basicCreated(); //视图初始化后 设置标题等
	
	protected abstract void whenOperated(String passcode); //当密码输入完后
	
	public void onNumpadClick(View view) { //数字键点击事件
		String text = ((Button) view).getText().toString();
		if(etCode1.isFocused()) {
			input(text, etCode1, etCode2);
		} else if(etCode2.isFocused()) {
			input(text, etCode2, etCode3);
		} else if(etCode3.isFocused()) {
			input(text, etCode3, etCode4);
		} else {
			etCode4.setText(text);
			if(!UtilBoss.IfUtil.isEmpty(etCode1.getText().toString(), etCode2.getText().toString(), etCode3.getText().toString(), etCode4.getText().toString()))
				whenOperated(etCode1.getText().toString() + etCode2.getText().toString() + etCode3.getText().toString() + etCode4.getText().toString()); //填完第四个值操作
		}
	}
	
	protected void showMsg(AbstractPasscodeActivity.Type type) {
		findViewById(R.id.txtvTitle).startAnimation(AnimUtil.shake(5));
		switch(type) {
			case ERROR: ViewUtil.displayToast(instance, R.string.msg_passcode_error); break;
			case DIFFERENT: ViewUtil.displayToast(instance, R.string.msg_passcode_different); break;
		}
	}
	
	protected void setPasscodeTitle(String passcodeTitle) {
		txtvTitle.setText(passcodeTitle);
	}
	
	protected void reset() { //重设
		etCode1.setText("");
    	etCode1.requestFocus();
		etCode2.setText("");
		etCode3.setText("");
		etCode4.setText("");
	}
	
	private void etCode(EditText... etCodes) { //对密码框进行处理
		for(EditText etCode: etCodes) {
			etCode.setInputType(InputType.TYPE_NULL);
			etCode.setFilters(filters);
			etCode.setOnTouchListener(onTouchListener);
			etCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
	}
	
	private void input(String text, EditText current, EditText next) { //输入数据
		UtilBoss.ConditionUtil.nt(text, current, next);
		current.setText(text);
		next.requestFocus();
		next.setText("");
	}
	
	private void backspace(EditText etCode) { //退格
		UtilBoss.ConditionUtil.n(etCode);
		etCode.requestFocus();
		etCode.setText("");
	}
	
	private Context instance; 
	private InputFilter[] filters; //输入过滤器

	private TextView txtvTitle; //标题
	protected EditText etCode1; //密码一
	protected EditText etCode2; //密码二
	protected EditText etCode3; //密码三
	protected EditText etCode4; //密码四
	
	private OnTouchListener onTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if(view instanceof EditText) ((EditText) view).setText("");
			return false;
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnHelp: break;
				case R.id.btnBackspace:
					if(etCode2.isFocused()) {
						backspace(etCode1);
					} else if(etCode3.isFocused()) {
						backspace(etCode2);
					} else if(etCode4.isFocused()) {
						backspace(etCode3);
					}
					break;
			}
		}
	};

	public static final String FLAG_PASSCODE_TITLE = "FLAG_PASSCODE_TITLE";
	
	{
		this.instance = AbstractPasscodeActivity.this;
		this.filters = new InputFilter[2];
		
		this.filters[0] = new InputFilter.LengthFilter(1); //一个
		this.filters[1] = new InputFilter() { //数字
			
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if(source.length() > 1) return "";
				if(source.length() == 0) return null;
				
				try {
					int number = Integer.parseInt(source.toString());
					if(number >= 0 && number <= 9) return String.valueOf(number);
					return "";
				} catch (NumberFormatException e) {
					return "";
				}
			}
		};
		
		setInitActivityListener(new InitActivityListener() {
			
			@Override
			public void beforeInit() {
				if(!ApplicationDatas.PASSCODE_ROTATION) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		});
	}
	
	public enum Type {
		ERROR, DIFFERENT
	}
}
