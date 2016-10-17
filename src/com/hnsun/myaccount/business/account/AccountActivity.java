package com.hnsun.myaccount.business.account;

import android.accounts.AccountAuthenticatorActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.mess.ContextHandler;
import com.hnsun.myaccount.mess.TextChangeAdapter;
import com.hnsun.myaccount.model.NetModel;
import com.hnsun.myaccount.model.dbo.TblUser;
import com.hnsun.myaccount.support.online.TblUserOnline;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ViewUtil;

/**
 * 安卓系统调用添加用户时显示界面
 * @author hnsun
 * @date 2016/09/20
 */
public class AccountActivity extends AccountAuthenticatorActivity {

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		initView(icicle);
		initData(icicle);
		initAction(icicle);
	}
	
	private void initView(Bundle icicle) {
		setContentView(R.layout.activity_account);
	}
	
	private void initData(Bundle icicle) {
		etAccount = (EditText) findViewById(R.id.etAccount);
		etPassword = (EditText) findViewById(R.id.etPassword);
		
		ivAccountDelete = (ImageView) findViewById(R.id.ivAccountDelete);
		ivPasswordDelete = (ImageView) findViewById(R.id.ivPasswordDelete);
		ivPasswordEye = (ImageView) findViewById(R.id.ivPasswordEye);
		
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnExit = (Button) findViewById(R.id.btnExit);

		tblUserOnline = new TblUserOnline(tblUserOnlineHandler.setContext(instance));
	}
	
	private void initAction(Bundle icicle) {
		etAccount.addTextChangedListener(twAccount);
		etPassword.addTextChangedListener(twPassword);
		ivAccountDelete.setOnClickListener(onClickListener);
		ivPasswordDelete.setOnClickListener(onClickListener);
		ivPasswordEye.setOnTouchListener(onTouchListener);
		btnLogin.setOnClickListener(onClickListener);
		btnExit.setOnClickListener(onClickListener);
	}
	
	private Context instance;
	private TblUserOnline tblUserOnline;

	private EditText etAccount;
	private EditText etPassword;
	private ImageView ivAccountDelete;
	private ImageView ivPasswordDelete;
	private ImageView ivPasswordEye;
	private Button btnLogin;
	private Button btnExit;
	
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
					String account = etAccount.getText().toString();
					String password = etPassword.getText().toString();
					//登录信息判断
					if(!UtilBoss.IfUtil.isEmpty(account, password)) tblUserOnline.login(account, password);
					else {
						ViewUtil.displayToast(instance, R.string.msg_input);
					}
				case R.id.btnExit: ((AccountActivity) instance).finish(); break;
			}
		}
	};
	
	private static ContextHandler tblUserOnlineHandler = new ContextHandler() { //用户线上主线程
		
		@Override
		public void handleMessage(Message msg) {
			NetModel model = (NetModel) msg.obj;
			switch(msg.what) {
				case TblUserOnline.FROM_LOGIN:
					if(model.isRetIs()) {
						//登陆成功后添加进安卓系统里
						NetModel.NetResult result = model.getRetObjJsonNetResult();
						if(ConstantsUtil.PRONOUN_SUCCESS.equalsIgnoreCase(result.getnStatus())) {
							TblUser user = result.getnJsonObj(TblUser.class);
							Bundle bundle = AccountHandler.refreshAccount(getContext(), user.getUserLogname(), user.getUserLogpassword());
							if(UtilBoss.ObjUtil.isNotNull(bundle)) ((AccountActivity) getContext()).setAccountAuthenticatorResult(bundle);
						} else ViewUtil.displayToast(getContext(), CodeBoss.DataCode.i18nFromServer(getContext(), result.getnMsg()));
					} else ViewUtil.displayToast(getContext(), model.getRetFail());
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	{
		this.instance = AccountActivity.this;
	}
}
