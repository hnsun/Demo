package com.hnsun.myaccount.business.passcode;

import java.util.Date;

import android.app.Application;
import android.content.Intent;

import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.platform.AppUtil;
import com.hnsun.myaccount.util.platform.SharedPreferencesUtil;

/**
 * 默认锁屏
 * @author hnsun
 * @date 2016/09/26
 */
public class DefaultAppLock extends AbstractAppLock {

	public DefaultAppLock(Application application) {
		this.application = application;
		this.settings = new SharedPreferencesUtil(application, SharedPreferencesUtil.FILENAME_PASSCODE_CONFIG);
	}

	@Override
	public void enable() {
		if(AppUtil.getVersionSDK() < 14) return;
		if(basicDone()) { //已经有密码锁的基本数据
			application.unregisterActivityLifecycleCallbacks(DefaultAppLock.this);
			application.registerActivityLifecycleCallbacks(DefaultAppLock.this);
		}
	}

	@Override
	public void disable() {
		if(AppUtil.getVersionSDK() < 14) return;
		application.unregisterActivityLifecycleCallbacks(DefaultAppLock.this);
	}

	@Override
	public void set(String passcode) {
		if(UtilBoss.StrUtil.isEmpty(passcode)) { //移除
			settings.remove(SharedPreferencesUtil.KEY_PASSCODE_CODE);
			disable();
		} else {
			encrypt(ApplicationDatas.PASSCODE_SALT + passcode);
			enable();
		}
	}

	@Override
	public void force() {
		lostFocusDate = null;
	}

	@Override
	public void resume() {
		if(isLock()) {
			Intent intent = new Intent(application.getApplicationContext(), PasscodeUnlockActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			application.startActivity(intent);
		}
	}

	@Override
	public void pause() {
		lostFocusDate = new Date();
	}
	
	@Override
	public boolean basicDone() {
		boolean ret = false;
		if(settings.contains(SharedPreferencesUtil.KEY_PASSCODE_CODE)) ret = true;
		return ret;
	}

	@Override
	public boolean verify(String passcode) {
		String stored = settings.lockLoad(SharedPreferencesUtil.KEY_PASSCODE_CODE);
		passcode = ApplicationDatas.PASSCODE_SALT + passcode;
		
		if(passcode.equals(stored)) {
			lostFocusDate = new Date();
			return true;
		}
		return false;
	}
	
	private void encrypt(String passcode) {
		settings.lockPut(SharedPreferencesUtil.KEY_PASSCODE_CODE, passcode);
	}
	
	private boolean isLock() {
		if(!basicDone()) return false;
		if(UtilBoss.ObjUtil.isNull(lostFocusDate)) return true;
		
		int current = lockTimeout;
		lockTimeout = ApplicationDatas.PASSCODE_TIMEOUT; //重置
		int passed = Math.abs((int) (new Date().getTime() - lostFocusDate.getTime()));
		if(passed >= current) {
			lostFocusDate = null;
			return true;
		}
		return false;
	}
	
	private Application application;
	private Date lostFocusDate;
	private SharedPreferencesUtil settings;
}
