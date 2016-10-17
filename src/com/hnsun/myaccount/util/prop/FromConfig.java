package com.hnsun.myaccount.util.prop;


/**
 * 来自配置属性文件部分
 * @author hnsun
 * @date 2016/08/26
 */
public class FromConfig extends PropertiesInitAbstract {
	
	private FromConfig() {}

	public static FromConfig getInstance(String partFrom) { //获得配置文件的管理能力
		instance = new FromConfig();
		instance.setManager(PropertiesManager.getInstance(partFrom));
		return instance;
	}
	
	private static FromConfig instance; //非单例实例
}
