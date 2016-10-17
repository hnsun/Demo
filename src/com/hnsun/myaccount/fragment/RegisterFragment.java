package com.hnsun.myaccount.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.fragment.common.BaseFragment;
import com.hnsun.myaccount.mess.TextChangeAdapter;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.view.click.OnNoDoubleClickListener;

/**
 * 注册碎片
 * @author hnsun
 * @date 2016/08/28
 */
public class RegisterFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container, false);
		return view;
	}
	
	@Override
	protected void initData(View view, Bundle savedInstanceState) {
		etAccount = (EditText) view.findViewById(R.id.etAccount);
		etPassword = (EditText) view.findViewById(R.id.etPassword);
		etPasswordAgain = (EditText) view.findViewById(R.id.etPasswordAgain);
		ivAccountDelete = (ImageView) view.findViewById(R.id.ivAccountDelete);
		ivPasswordDelete = (ImageView) view.findViewById(R.id.ivPasswordDelete);
		ivPasswordEye = (ImageView) view.findViewById(R.id.ivPasswordEye);
		ivPasswordAgainDelete = (ImageView) view.findViewById(R.id.ivPasswordAgainDelete);
		ivPasswordAgainEye = (ImageView) view.findViewById(R.id.ivPasswordAgainEye);
		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		btnRegister = (Button) view.findViewById(R.id.btnRegister);
		btnForgetPassword = (Button) view.findViewById(R.id.btnForgetPassword);
	}
	
	@Override
	protected void initAction(Bundle savedInstanceState) {
		etAccount.addTextChangedListener(twAccount);
		etPassword.addTextChangedListener(twPassword);
		etPasswordAgain.addTextChangedListener(twPasswordAgain);
		ivAccountDelete.setOnClickListener(onClickListener);
		ivPasswordDelete.setOnClickListener(onClickListener);
		etAccount.setOnFocusChangeListener(onAccountFocusChangeListener);
		ivPasswordEye.setOnTouchListener(onPwdTouchListener);
		ivPasswordAgainDelete.setOnClickListener(onClickListener);
		ivPasswordAgainEye.setOnTouchListener(onPwdAgainTouchListener);
		btnRegister.setOnClickListener(onNoDoubleClickListener);
		btnLogin.setOnClickListener(onClickListener);
		btnForgetPassword.setOnClickListener(onClickListener);
	}
	
	public EditText getEtAccount() {
		return etAccount;
	}
	
	private EditText etAccount;
	private EditText etPassword;
	private EditText etPasswordAgain;
	private ImageView ivAccountDelete;
	private ImageView ivPasswordDelete;
	private ImageView ivPasswordEye;
	private ImageView ivPasswordAgainDelete;
	private ImageView ivPasswordAgainEye;
	private Button btnLogin;
	private Button btnRegister;
	private Button btnForgetPassword;
	
	private TextWatcher twAccount = new TextChangeAdapter() {
		
		@Override
		public void afterTextChanged(Editable editable) {
			if(editable.toString().length() > 0) ivAccountDelete.setVisibility(View.VISIBLE);
			else ivAccountDelete.setVisibility(View.GONE);
		}
	};
	
	private TextWatcher twPassword = new TextChangeAdapter() {
		
		@Override
		public void afterTextChanged(Editable editable) {
			if(editable.toString().length() > 0) ivPasswordDelete.setVisibility(View.VISIBLE);
			else ivPasswordDelete.setVisibility(View.GONE);
		}
	};
	
	private TextWatcher twPasswordAgain = new TextChangeAdapter() {
		
		@Override
		public void afterTextChanged(Editable editable) {
			if(editable.toString().length() > 0) ivPasswordAgainDelete.setVisibility(View.VISIBLE);
			else ivPasswordAgainDelete.setVisibility(View.GONE);
		}
	};
	
	private OnFocusChangeListener onAccountFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View view, boolean hasFocus) {
			if(!hasFocus) getOnSkipClickListenter(RegisterFragment.FOR_ACCOUNT_LOSEFOCUS, etAccount.getText()).onAction(view);
		}
	}; 
	
	private OnTouchListener onPwdTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			boolean ret = true;
			switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN: CodeBoss.ViewCode.etPasswordVisible(etPassword, ivPasswordEye, true); break;
				case MotionEvent.ACTION_UP: CodeBoss.ViewCode.etPasswordVisible(etPassword, ivPasswordEye, false); break;
				default: ret = false; break;
			}
			return ret;
		}
	};
	
	private OnTouchListener onPwdAgainTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			boolean ret = true;
			switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN: CodeBoss.ViewCode.etPasswordVisible(etPasswordAgain, ivPasswordAgainEye, true); break;
				case MotionEvent.ACTION_UP: CodeBoss.ViewCode.etPasswordVisible(etPasswordAgain, ivPasswordAgainEye, false); break;
				default: ret = false; break;
			}
			return ret;
		}
	};
	
	private OnNoDoubleClickListener onNoDoubleClickListener = new OnNoDoubleClickListener() {
		
		@Override
		public void onNoDoubleClick(View view) {
			switch(view.getId()) {
				case R.id.btnRegister:
					//注册信息判断
					getOnSkipClickListenter(RegisterFragment.TO_REGISTER, etAccount.getText(), etPassword.getText(), etPasswordAgain.getText()).onAction(view);
					break;
			}
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.ivAccountDelete: etAccount.setText("");
				case R.id.ivPasswordDelete:
				case R.id.ivPasswordAgainDelete: 
					etPassword.setText("");
					etPasswordAgain.setText(""); 
					break;
				case R.id.btnLogin: getOnSkipClickListenter(RegisterFragment.TO_LOGIN).onAction(view); break;
				case R.id.btnForgetPassword: getOnSkipClickListenter(RegisterFragment.TO_FORGETPASSWORD).onAction(view); break;
			}
		}
	};
	
	public static final int TO_LOGIN = 0x120201;
	public static final int TO_REGISTER = 0x120202;
	public static final int TO_FORGETPASSWORD = 0x120203;
	public static final int FOR_ACCOUNT_LOSEFOCUS = 0x120204;
}