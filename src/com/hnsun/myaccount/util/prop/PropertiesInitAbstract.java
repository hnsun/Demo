package com.hnsun.myaccount.util.prop;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 初步封装，供某种类使用属性文件管理类进一步封装
 * @author hnsun
 * @date 2016/08/26
 */
public abstract class PropertiesInitAbstract implements PropertiesInitUsb {

	public int getInt(String key) { //获得Int类型值
		return manager.getInt(key);
	}
	
	public String getString(String key) { //获得String类型值
		return manager.getString(key);
	}
	
	public PropertiesManager getManager() {
		return manager;
	}

	public void setManager(PropertiesManager manager) {
		UtilBoss.ConditionUtil.n(manager);
		this.manager = manager;
	}

	protected PropertiesManager manager;
}
