package com.hnsun.myaccount.util;

import android.content.Context;

import com.hnsun.myaccount.business.account.AccountHandler;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.file.PathUtil;
import com.hnsun.myaccount.util.log.LogFactory;
import com.hnsun.myaccount.util.net.NetFactory;
import com.hnsun.myaccount.util.platform.AppUtil;
import com.hnsun.myaccount.util.platform.AssetsUtil;
import com.hnsun.myaccount.util.platform.SharedPreferencesUtil;
import com.hnsun.myaccount.util.platform.SkinManager;

/**
 * 初始化应用帮助类
 * @author hnsun
 * @date 2016/08/15
 */
public class InitUtil {

	private InitUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

	public static void init(Context context) { //初始化应用入口
		InitUtil.initNow(context); //当下需要初始化
		InitUtil.initLater(context); //可以稍后初始化
	}
	
	/**
	 * 当下需要初始化
	 * @param context
	 */
	private static void initNow(Context context) {
		SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
		if(UtilBoss.ObjUtil.isNull(SystemStatus.uniqueCode)) SystemStatus.uniqueCode = InitUtil.initBy(sharedPreferencesUtil, SharedPreferencesUtil.KEY_APP_UNIQUECODE, CodeBoss.MessCode.uuid()); //应用唯一标识符
		if(UtilBoss.ObjUtil.isNull(SystemStatus.directory)) SystemStatus.directory = InitUtil.initBy(sharedPreferencesUtil, SharedPreferencesUtil.KEY_APP_DIRECTORY, ApplicationDatas.APP_DIRECTORY); //当前系统总目录
		if(UtilBoss.ObjUtil.isNull(SystemStatus.server)) SystemStatus.server = ApplicationDatas.APP_SERVER_TEST; //访问服务器
		if(UtilBoss.ObjUtil.isNull(SystemStatus.exitStyle)) SystemStatus.exitStyle = ApplicationDatas.EXIT_TOAST; //退出方式
		if(UtilBoss.ObjUtil.isNull(SystemStatus.curAccount)) SystemStatus.curAccount = AccountHandler.getAccount(context); //用户
		if(UtilBoss.ObjUtil.isNull(SystemStatus.typeface)) SystemStatus.typeface = AssetsUtil.ttf(context, AssetsUtil.TTF_UMINE); //字体
		
		if(UtilBoss.ObjUtil.isNull(SystemStatus.logLevel)) SystemStatus.logLevel = ApplicationDatas.LOG_LEVEL; //日志级别
		if(UtilBoss.ObjUtil.isNull(SystemStatus.logDebug)) SystemStatus.logDebug = ApplicationDatas.LOG_DEBUG; //日志是否调试
		if(UtilBoss.ObjUtil.isNull(SystemStatus.logSave)) SystemStatus.logSave = ApplicationDatas.LOG_SAVE; //是否保存日志
		if(UtilBoss.ObjUtil.isNull(SystemStatus.logDefaultTag)) SystemStatus.logDefaultTag = ApplicationDatas.APP_NAME; //日志默认标签
		if(UtilBoss.ObjUtil.isNull(SystemStatus.loggerName)) SystemStatus.loggerName = ApplicationDatas.LOG_CAT; //当前日志记录器名
		if(UtilBoss.ObjUtil.isNull(SystemStatus.logger)) SystemStatus.logger = LogFactory.log(); //初始化日志记录器

		PathUtil.mkDipPath(AppUtil.getInnerPath(context, AppUtil.INNER_DIR_SKIN)); //程序内置皮肤目录

		if(UtilBoss.ObjUtil.isNull(SystemStatus.netState)) SystemStatus.netState = true; //网络连接
		if(UtilBoss.ObjUtil.isNull(SystemStatus.netOldEntity)) SystemStatus.netOldEntity = false; //网络旧的封装方式
		if(UtilBoss.ObjUtil.isNull(SystemStatus.netterName)) SystemStatus.netterName = ApplicationDatas.NET_APACHE; //当前网络记录器名
		if(UtilBoss.ObjUtil.isNull(SystemStatus.netter)) SystemStatus.netter = NetFactory.net(context); //初始化网络记录器
	}
	
	/**
	 * 可以稍后初始化
	 * @param context
	 */
	private static void initLater(Context context) {
		SkinManager.init(context); //皮肤字体
	}
	
	/**
	 * 配置文件一致化
	 * @param sharedPreferencesUtil
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private static String initBy(SharedPreferencesUtil sharedPreferencesUtil, String key, String defaultValue) {
		String ret = null;
		UtilBoss.ConditionUtil.nt(sharedPreferencesUtil, key);
		
		ret = sharedPreferencesUtil.lockLoad(key);
		if(UtilBoss.StrUtil.isEmpty(ret)) {
			ret = defaultValue;
			sharedPreferencesUtil.lockPut(key, ret);
		}
		
		return ret;
	} 
}
