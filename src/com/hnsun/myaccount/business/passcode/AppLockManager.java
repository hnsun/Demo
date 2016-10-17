package com.hnsun.myaccount.business.passcode;

import android.app.Application;

import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.AppUtil;

/**
 * 应用锁管理器
 * @author hnsun
 * @date 2016/09/26
 */
public class AppLockManager {

	public static AppLockManager getInstance() {
		if(UtilBoss.ObjUtil.isNull(instance)) instance = new AppLockManager();
		return instance;
	}
	
	public void enableIfAvailable(Application application) { //默认是否可用，可用则用之
		UtilBoss.ConditionUtil.n(application);
		if(AppUtil.getVersionSDK() >= 14) {
			appLock = new DefaultAppLock(application);
			appLock.enable();
		}
	}
	
	public boolean isEnabled() { //是否开启
		if(UtilBoss.ObjUtil.isNull(appLock)) {
			return false;
		} else if(appLock instanceof DefaultAppLock) {
			return AppUtil.getVersionSDK() >= 14;
		} else return true;
	}
	
	public void setLocker(AbstractAppLock appLock) {
		UtilBoss.ConditionUtil.n(appLock);
		if(UtilBoss.ObjUtil.isNotNull(this.appLock)) this.appLock.disable();
		this.appLock = appLock;
	}
	
	public AbstractAppLock getAppLock() {
		return appLock;
	}
	
	public void setTimeout(int timeout) { //单次设置
		UtilBoss.ConditionUtil.n(appLock);
		appLock.setLockTimeout(timeout);
	}
	
	private static AppLockManager instance; //单例实例
	
	private AbstractAppLock appLock;
	
	static {
		instance = new AppLockManager();
	}
}
