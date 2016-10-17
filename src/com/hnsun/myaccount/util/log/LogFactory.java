package com.hnsun.myaccount.util.log;

import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;

/**
 * 日志工厂
 * @author hnsun
 * @date 2016/08/15
 */
public class LogFactory {

	/**
	 * 根据全局变量当前运行日志名来获取默认的日志器 并以当前类名为Tag
	 * @return
	 */
	public static LogUsb log() {
		if(UtilBoss.StrUtil.isEmpty(SystemStatus.loggerName)) SystemStatus.loggerName = ApplicationDatas.LOG_CAT;
		
		if(ApplicationDatas.LOG_COMMON.equals(SystemStatus.loggerName)) SystemStatus.logger = LogFactory.commonLog();
		if(ApplicationDatas.LOG_CAT.equals(SystemStatus.loggerName)) SystemStatus.logger = LogFactory.catLog();
		return SystemStatus.logger;
	}

	/**
	 * 获取默认的CommonLog 以当前类名为Tag
	 * @return
	 */
	public static LogUsb commonLog() {
		if(UtilBoss.ObjUtil.isNull(SystemStatus.logger)) SystemStatus.logger = new CommonLog();
		else {
			String name = SystemStatus.logger.getClass().getName();
			if(!ApplicationDatas.LOG_COMMON.equals(UtilBoss.StrUtil.substring(name, name.lastIndexOf(".") + 1))) SystemStatus.logger = new CommonLog();
		}
		
		SystemStatus.logger.setTag(UtilBoss.ClassUtil.getCurClassName());
		return SystemStatus.logger;
	}

	/**
	 * 获取默认的CatLog 以当前类名为Tag
	 * @return
	 */
	public static LogUsb catLog() {
		if(UtilBoss.ObjUtil.isNull(SystemStatus.logger)) SystemStatus.logger = new CatLog();
		else {
			String name = SystemStatus.logger.getClass().getName();
			if(!ApplicationDatas.LOG_CAT.equals(UtilBoss.StrUtil.substring(name, name.lastIndexOf(".") + 1))) SystemStatus.logger = new CatLog();
		}
		
		SystemStatus.logger.setTag(UtilBoss.ClassUtil.getCurClassName());
		return SystemStatus.logger;
	}
}
