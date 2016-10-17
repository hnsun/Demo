package com.hnsun.myaccount.util.prop;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 来自主题属性文件部分
 * @author hnsun
 * @date 2016/06/26
 */
public class FromTheme extends PropertiesInitAbstract {

	private FromTheme(String partFrom) { 
		if(UtilBoss.StrUtil.isEmpty(partFrom)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.partFrom = partFrom;
		setManager(PropertiesManager.getInstance(partFrom));
	}

	public static synchronized FromTheme getInstance(String partFrom) { //确保访问的都是同一主题内容
		if(UtilBoss.ObjUtil.isNull(instance) || !instance.partFrom.equals(partFrom)) instance = new FromTheme(partFrom);
		return instance;
	}

	public String getColor(String key) { //获取颜色值
		UtilBoss.ConditionUtil.n(key);
		return manager.getString(key);
	}

	private static FromTheme instance; //单例实例
	
	private String partFrom;
}
