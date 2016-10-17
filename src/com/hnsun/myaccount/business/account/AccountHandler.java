package com.hnsun.myaccount.business.account;

import java.util.Date;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.platform.SharedPreferencesUtil;

/**
 * 账户本地化处理
 * @author hnsun
 * @date 2016/09/21
 */
public class AccountHandler {

	/**
	 *  更新账户
	 * @param context
	 * @param accountStr
	 * @param passwordStr
	 * @return
	 */
	public static Bundle refreshAccount(Context context, String accountStr, String passwordStr) {
		Bundle ret = null;
		UtilBoss.ConditionUtil.n(context, accountStr, passwordStr);

		AccountHandler.removeAccount(context); //保证一部机只有一个用户
		ret = AccountHandler.putAccount(context, accountStr, passwordStr);
		
		return ret;
	}

	/**
	 * 检查是否含有账户，及是否过期
	 * @param context
	 * @return
	 */
	public static boolean checkAccount(Context context) {
		boolean ret = false;
		UtilBoss.ConditionUtil.n(context);
		
		AccountManager manager = AccountManager.get(context);
		Account[] accounts = manager.getAccountsByType(ApplicationDatas.APP_PACKAGE);
		if(!UtilBoss.IfUtil.isEmpty(accounts)) {
			SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, SharedPreferencesUtil.FILENAME_ACCOUNT_CONFIG);
			String time = sharedPreferencesUtil.lockLoad(SharedPreferencesUtil.KEY_ACCOUNT + "_" + accounts[0].name);
			if(!UtilBoss.StrUtil.isEmpty(time)) {
				Date date = UtilBoss.DatetimeUtil.formatStr2Date(time, ConstantsUtil.FORMAT_DATE_DATETIME);
				if((new Date().getTime() - date.getTime()) < ApplicationDatas.ACCOUNT_TIMEOUT) ret = true; //密码正确且过期时间以内不需登陆
			}
		}
		
		return ret;
	}

	/**
	 * 账本信息存储
	 * @param context
	 * @param accountStr
	 * @param passwordStr
	 * @return
	 */
	public static Bundle putAccount(Context context, String accountStr, String passwordStr) {
		Bundle ret = null;
		UtilBoss.ConditionUtil.n(context, accountStr, passwordStr);

		AccountManager manager = AccountManager.get(context);
		Account account = new Account(accountStr, ApplicationDatas.APP_PACKAGE);
		Bundle userdata = new Bundle();
		userdata.putString("SERVER", SystemStatus.server);
		if(manager.addAccountExplicitly(account, passwordStr, userdata)) { //添加成功后将信息返回
			SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, SharedPreferencesUtil.FILENAME_ACCOUNT_CONFIG);
			sharedPreferencesUtil.lockPut(SharedPreferencesUtil.KEY_ACCOUNT + "_" + account.name, new Date());
			
			ret = new Bundle();
			ret.putString(AccountManager.KEY_ACCOUNT_NAME, accountStr);
			ret.putString(AccountManager.KEY_ACCOUNT_TYPE, ApplicationDatas.APP_PACKAGE);
		}
		return ret;
	}
	
	/**
	 * 移除所有用户
	 * @param context
	 */
	public static void removeAccount(Context context) {
		UtilBoss.ConditionUtil.n(context);

		AccountManager manager = AccountManager.get(context);
		Account[] accounts = manager.getAccountsByType(ApplicationDatas.APP_PACKAGE);
		if(!UtilBoss.IfUtil.isEmpty(accounts)) {
			SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, SharedPreferencesUtil.FILENAME_ACCOUNT_CONFIG);
			for(Account account : accounts) {
				sharedPreferencesUtil.remove(SharedPreferencesUtil.KEY_ACCOUNT + "_" + account.name);
				manager.removeAccount(account, null, null);
			}
		}
	}
	
	/**
	 * 获得当前用户
	 * @param context
	 * @return
	 */
	public static Account getAccount(Context context) {
		UtilBoss.ConditionUtil.n(context);
		Account ret = null;

		AccountManager manager = AccountManager.get(context);
		Account[] accounts = manager.getAccountsByType(ApplicationDatas.APP_PACKAGE);
		if(!UtilBoss.IfUtil.isEmpty(accounts)) {
			ret = accounts[0];
		}
		return ret;
	}
}
