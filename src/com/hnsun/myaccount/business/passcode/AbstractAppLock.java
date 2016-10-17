package com.hnsun.myaccount.business.passcode;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;

/**
 * 抽象应用锁中心控制
 * @author hnsun
 * @date 2016/09/25
 */
public abstract class AbstractAppLock implements Application.ActivityLifecycleCallbacks { //生命周期回调函数

	@Override
	public void onActivityCreated(Activity act, Bundle bundle) {}

	@Override
	public void onActivitySaveInstanceState(Activity act, Bundle bundle) {}

	@Override
	public void onActivityStarted(Activity act) {}

	@Override
	public void onActivityResumed(Activity act) {
		if(act.getClass() ==  PasscodeUnlockActivity.class) return;
		if(disabledActivities.contains(act.getClass().getName())) return; //取消加密的
		resume();
	}
	
	@Override
	public void onActivityPaused(Activity act) {
		if(act.getClass() ==  PasscodeUnlockActivity.class) return;
		if(disabledActivities.contains(act.getClass().getName())) return; //取消加密的
		pause();
	}
	
	@Override
	public void onActivityStopped(Activity act) {}

	@Override
	public void onActivityDestroyed(Activity act) {}
	
	public abstract void enable(); //开启应用锁
	
	public abstract void disable(); //关闭应用锁
	
	public abstract void set(String passcode); //设置密码
	
	public abstract void force(); //强制下次访问要密码
	
	public abstract void resume(); //开始
	
	public abstract void pause(); //结束
	
	public abstract boolean basicDone(); //是否有开启应用锁的基本数据
	
	public abstract boolean verify(String passcode); //验证密码的正确性
	
	public void setLockTimeout(int lockTimeout) {
		this.lockTimeout = lockTimeout;
	}
	
	public void addDisabledActivities(String... names) {
		for(String name : names) {
			if(UtilBoss.StrUtil.isEmpty(name) || disabledActivities.contains(name)) continue;
			disabledActivities.add(name);
		}
	}
	
	protected int lockTimeout; //失效时间
	protected List<String> disabledActivities; //不需要应用锁
	
	{
		this.lockTimeout = ApplicationDatas.PASSCODE_TIMEOUT;
		this.disabledActivities = new ArrayList<String>();
	}
}
