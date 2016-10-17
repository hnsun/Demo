package com.hnsun.myaccount.business.account;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 安卓系统访问当前账户时接应
 * @author hnsun
 * @date 2016/09/20
 */
public class AccountService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		if(AccountManager.ACTION_AUTHENTICATOR_INTENT.equals(intent.getAction())) ret = getAuthenticator().getIBinder();
		return ret;
	}
	
	private AccountAuthenticator getAuthenticator() {
		if(UtilBoss.ObjUtil.isNull(accountAuthenticator)) accountAuthenticator = new AccountAuthenticator(instance);
		return accountAuthenticator;
	}

	private Context instance;
	private AccountAuthenticator accountAuthenticator;
	
	{
		this.instance = AccountService.this;
		this.accountAuthenticator = new AccountAuthenticator(instance);
	}
}
