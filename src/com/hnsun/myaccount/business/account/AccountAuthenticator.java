package com.hnsun.myaccount.business.account;

import com.hnsun.myaccount.util.log.LogFactory;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 安卓系统账户信息全局管理
 * @author hnsun
 * @date 2016/09/20
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

	public AccountAuthenticator(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, 
			String[] requiredFeatures, Bundle options) throws NetworkErrorException { //添加账户时
		LogFactory.log().i("addAccount");
		Bundle ret = new Bundle();
		Intent intent = new Intent(context, AccountActivity.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		ret.putParcelable(AccountManager.KEY_INTENT, intent);
		return ret;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, 
			Account account, Bundle options) throws NetworkErrorException {
		LogFactory.log().i("confirmCredentials");
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		LogFactory.log().i("editProperties");
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
			String authTokenType, Bundle loginOptions) throws NetworkErrorException {
		LogFactory.log().i("getAuthToken");
		return null;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		LogFactory.log().i("getAuthTokenLabel");
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
			String[] features) throws NetworkErrorException {
		LogFactory.log().i("hasFeatures");
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle loginOptions)
			throws NetworkErrorException {
		LogFactory.log().i("updateCredentials");
		return null;
	}

	private Context context;
}
