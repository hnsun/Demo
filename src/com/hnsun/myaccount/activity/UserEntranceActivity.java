package com.hnsun.myaccount.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.business.account.AccountHandler;
import com.hnsun.myaccount.business.passcode.AppLockManager;
import com.hnsun.myaccount.fragment.LoginFragment;
import com.hnsun.myaccount.fragment.RegisterFragment;
import com.hnsun.myaccount.mess.ContextHandler;
import com.hnsun.myaccount.model.NetModel;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.model.dbo.TblUser;
import com.hnsun.myaccount.support.online.TblUserOnline;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ViewUtil;

/**
 * 用户入口界面
 * @author hnsun
 * @date 2016/08/23
 */
public class UserEntranceActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_user_entrance);
		getActionBar().hide();
		
		if(UtilBoss.ObjUtil.isNull(savedInstanceState)) {
			fragment = new LoginFragment();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.add(R.id.flContent, (LoginFragment) fragment, "Login");
			transaction.commit();
		}
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		setExit(false);
		tblUserOnline = new TblUserOnline(tblUserOnlineHandler.setContext(instance));
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		setOnFragmentClickListener(new OnFragmentClickListener() {
			
			@Override
			public void onFragmentClick(View view, int tag, Object... objs) {
				String account = null;
				String password = null;
				switch (tag) {
					case LoginFragment.TO_LOGIN:
						account = UtilBoss.StrUtil.getByObj(objs[0]);
						password = UtilBoss.StrUtil.getByObj(objs[1]);
						//登录信息判断
						if(!UtilBoss.IfUtil.isEmpty(account, password)) {
//							tblUserOnline.login(account, password);
							TblUser user = new TblUser();
							user.setUserLogname(account);
							user.setUserLogpassword(password);
							Bundle bundle = AccountHandler.refreshAccount(instance, user.getUserLogname(), user.getUserLogpassword());
							if(UtilBoss.ObjUtil.isNotNull(bundle)) {
								if(!AppLockManager.getInstance().isEnabled()) AppLockManager.getInstance().enableIfAvailable(getApplication()); //应用锁
								SystemStatus.curAccount = AccountHandler.getAccount(instance);
								startActivity(new Intent(instance, UserMainActivity.class));
							}
						} else ViewUtil.displayToast(instance, R.string.msg_input);
						break;
					case LoginFragment.TO_REGISTER: replace(R.id.flContent, new RegisterFragment(), "Register"); break;
					case LoginFragment.TO_FORGETPASSWORD: ViewUtil.displayToast(instance, "忘记密码"); break;
					case RegisterFragment.TO_LOGIN: replace(R.id.flContent, new LoginFragment(), "Login"); break;
					case RegisterFragment.TO_REGISTER: 
						account = UtilBoss.StrUtil.getByObj(objs[0]);
						password = UtilBoss.StrUtil.getByObj(objs[1]);
						String passwordAgain = UtilBoss.StrUtil.getByObj(objs[2]);
						//登录信息判断
						if(!UtilBoss.IfUtil.isEmpty(account, password, passwordAgain)) {
							if(password.equals(passwordAgain)) tblUserOnline.register(account, password);
							else ViewUtil.displayToast(instance, R.string.validate_equalTo_password);
						} else ViewUtil.displayToast(instance, R.string.msg_input);
						break;
					case RegisterFragment.TO_FORGETPASSWORD: ViewUtil.displayToast(instance, "忘记密码"); break;
					case RegisterFragment.FOR_ACCOUNT_LOSEFOCUS: 
						account = UtilBoss.StrUtil.getByObj(objs[0]);
						if(!UtilBoss.IfUtil.isEmpty(account)) tblUserOnline.logExist(account);
						break;
				}
			}
		});
	}
	
	private void replace(int res, Fragment fragment, String tag) {
		this.fragment = fragment;
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(res, fragment, tag);
		transaction.commit();
	}
	
	private void whenAccountInput(boolean isAgain) {
		if(isAgain) {
			ViewUtil.displayToast(instance, R.string.validate_remote_again);
			if(fragment instanceof RegisterFragment) {
				EditText et = ((RegisterFragment) fragment).getEtAccount();
				et.requestFocus(); //获得焦点
				et.setSelectAllOnFocus(true); //内容全选
			}
		}
	}

	private Context instance;
	private TblUserOnline tblUserOnline;
	
	private Fragment fragment;
	
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
							if(UtilBoss.ObjUtil.isNotNull(bundle)) {
								if(!AppLockManager.getInstance().isEnabled()) AppLockManager.getInstance().enableIfAvailable(((UserEntranceActivity) getContext()).getApplication()); //应用锁
								SystemStatus.curAccount = AccountHandler.getAccount(getContext());
								getContext().startActivity(new Intent(getContext(), UserMainActivity.class));
							}
						} else ViewUtil.displayToast(getContext(), CodeBoss.DataCode.i18nFromServer(getContext(), result.getnMsg()));
					} else ViewUtil.displayToast(getContext(), model.getRetFail());
					break;
				case TblUserOnline.FROM_REGISTER:
					if(model.isRetIs()) {
						NetModel.NetResult result = model.getRetObjJsonNetResult();
						ViewUtil.displayToast(getContext(), CodeBoss.DataCode.i18nFromServer(getContext(), result.getnMsg()));
						if(ConstantsUtil.PRONOUN_SUCCESS.equalsIgnoreCase(result.getnStatus())) ((UserEntranceActivity) getContext()).replace(R.id.flContent, new LoginFragment(), "Login");
					} else ViewUtil.displayToast(getContext(), model.getRetFail());
					break;
				case TblUserOnline.FROM_LOGEXIST:
					if(model.isRetIs()) {
						((UserEntranceActivity) getContext()).whenAccountInput(!Boolean.parseBoolean(UtilBoss.StrUtil.getByObj(model.getRetObj())));
					} else ViewUtil.displayToast(getContext(), model.getRetFail());
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	{
		this.instance = UserEntranceActivity.this;
	}
}
