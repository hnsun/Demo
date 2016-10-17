package com.hnsun.myaccount.application;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.CrashHandler;

/**
 * 退出程序管理（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class ExitApplication extends Application {

	private ExitApplication() {}

	public static ExitApplication getInstance() { //单例模式中获取唯一的ExitApplication实例
		if (UtilBoss.ObjUtil.isNull(instance)) instance = new ExitApplication();
		return instance;
	}

	@Override
	public void onCreate() { super.onCreate(); }

	public void addActivity(Activity activity) { //添加Activity 到容器中
		activityList.add(activity);
		CrashHandler.getInstance().init(activity);
	}

	public int getSize(){
		return activityList.size();
	}

	public void removeActivityLast() {
		Activity activity = null;
		if(activityList.size() > 0) {
			activity = activityList.get(activityList.size() - 1);
			activityList.remove(activity);
			activity.finish();
		}
	}

	public void removeActivity(Activity activity){
		activityList.remove(activity);
	}

	public void addService(Service service) {
		serviceList.add(service);
		CrashHandler.getInstance().init(service);
	}

	public void exit() { //遍历所有Activity并finish
		for(Service service : serviceList) {
			service.stopSelf();
		}
		for(Activity activity : activityList) {
			activity.finish();
		}
		
		serviceList.clear();
		activityList.clear();
		System.exit(0);
	}

	private static ExitApplication instance;

	private static List<Activity> activityList = new LinkedList<Activity>();
	private static List<Service> serviceList = new LinkedList<Service>();
	
	static {
		ExitApplication.instance = new ExitApplication(); //确保线程安全
	}
}
