package com.hnsun.myaccount.application;

import android.app.Application;
import android.content.Context;

import com.hnsun.myaccount.business.passcode.AppLockManager;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.InitUtil;

/**
 * 整个系统初始化及特殊数据共享
 * @author hnsun
 * @date 2016/08/20
 */
public class DefaultApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        instance = DefaultApplication.this;
        
    	InitUtil.init(instance); //初始化
		if(CodeBoss.MessCode.accountState(instance) && !AppLockManager.getInstance().isEnabled()) {
			AppLockManager.getInstance().enableIfAvailable((DefaultApplication) instance); //应用锁
		}
    }
    
    private Context instance;
}
