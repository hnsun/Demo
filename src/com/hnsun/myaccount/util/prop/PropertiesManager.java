package com.hnsun.myaccount.util.prop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 属性文件的管理类
 * @author hnsun
 * @date 2016/08/26
 */
public class PropertiesManager {
	
	public static PropertiesManager getInstance(String propPath) { //全路径 获取管理器
		UtilBoss.ConditionUtil.n(propPath);
		PropertiesManager manager = new PropertiesManager();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(propPath);
			manager.getProp().load(fis);
		} catch (FileNotFoundException e) {
			LogFactory.log().e(e, "文件找不到");
		} catch (IOException e) {
			LogFactory.log().e(e, "输入输出有问题");
		} finally {
			try {
				if(UtilBoss.ObjUtil.isNotNull(fis)) fis.close();
				fis = null;
			} catch (IOException e) {
				LogFactory.log().e(e, "输入输出有问题");
			}
		}
		return manager;
	}

	public int getInt(String key) {
		UtilBoss.ConditionUtil.n(key);
		return Integer.parseInt(getProp().getProperty(key));
	}
	
	public String getString(String key) {
		UtilBoss.ConditionUtil.n(key);
		return getProp().getProperty(key);
	}
	
	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		UtilBoss.ConditionUtil.n(prop);
		this.prop = prop;
	}

	public Properties prop;
	
	{
		this.prop = new Properties();
	}
}
