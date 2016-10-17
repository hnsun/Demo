package com.hnsun.myaccount.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.fragment.common.BaseFragment;
import com.hnsun.myaccount.mess.TextChangeAdapter;
import com.hnsun.myaccount.util.CodeBoss;

/**
 * 登陆碎片
 * @author hnsun
 * @date 2016/08/24
 */
public class LoginFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		return view;
	}
	
	@Override
	protected void initData(View view, Bundle savedInstanceState) {
		etAccount = (EditText) view.findViewById(R.id.etAccount);
		etPassword = (EditText) view.findViewById(R.id.etPassword);
		ivAccountDelete = (ImageView) view.findViewById(R.id.ivAccountDelete);
		ivPasswordDelete = (ImageView) view.findViewById(R.id.ivPasswordDelete);
		ivPasswordEye = (ImageView) view.findViewById(R.id.ivPasswordEye);
		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		btnRegister = (Button) view.findViewById(R.id.btnRegister);
		btnForgetPassword = (Button) view.findViewById(R.id.btnForgetPassword);
	}
	
	@Override
	protected void initAction(Bundle savedInstanceState) {
		etAccount.addTextChangedListener(twAccount);
		etPassword.addTextChangedListener(twPassword);
		ivAccountDelete.setOnClickListener(onClickListener);
		ivPasswordDelete.setOnClickListener(onClickListener);
		ivPasswordEye.setOnTouchListener(onTouchListener);
		btnLogin.setOnClickListener(onClickListener);
		btnRegister.setOnClickListener(onClickListener);
		btnForgetPassword.setOnClickListener(onClickListener);
	}
	
	private EditText etAccount;
	private EditText etPassword;
	private ImageView ivAccountDelete;
	private ImageView ivPasswordDelete;
	private ImageView ivPasswordEye;
	private Button btnLogin;
	private Button btnRegister;
	private Button btnForgetPassword;
	
	private TextWatcher twAccount = new TextChangeAdapter() {
		
		@Override
		public void afterTextChanged(Editable editable) {
			etPassword.setText("");
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
	
	private OnTouchListener onTouchListener = new OnTouchListener() {

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
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.ivAccountDelete: etAccount.setText("");
				case R.id.ivPasswordDelete: etPassword.setText(""); break;
				case R.id.btnLogin:
					//登录信息判断
					getOnSkipClickListenter(LoginFragment.TO_LOGIN, etAccount.getText(), etPassword.getText()).onAction(view);
					break;
				case R.id.btnRegister: getOnSkipClickListenter(LoginFragment.TO_REGISTER).onAction(view); break;
				case R.id.btnForgetPassword: getOnSkipClickListenter(LoginFragment.TO_FORGETPASSWORD).onAction(view); break;
			}
		}
	};
	
	public static final int TO_LOGIN = 0x120101;
	public static final int TO_REGISTER = 0x120102;
	public static final int TO_FORGETPASSWORD = 0x120103;
}