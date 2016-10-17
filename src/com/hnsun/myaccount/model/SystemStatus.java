package com.hnsun.myaccount.model;

import android.accounts.Account;
import android.graphics.Typeface;

import com.hnsun.myaccount.util.log.LogUsb;
import com.hnsun.myaccount.util.net.NetUsb;

/**
 * 运行时参数
 * @author hnsun
 * @date 2016/08/15
 */
public class SystemStatus {
	
	public static String uniqueCode; //设备唯一标识
	public static String server; //当前服务器
	public static String directory; //当前系统总目录
	public static Account curAccount; //当前用户
	public static Typeface typeface; //系统字体
	
	public static Integer logLevel; //日志级别
	public static Boolean logDebug; //是否调试
	public static Boolean logSave; //是否保存
	public static String logDefaultTag; //默认日志标志
	public static String loggerName; //日志器名
	public static LogUsb logger; //普通日志对象

	public static Boolean netState; //网络状态是否连接
	public static Boolean netOldEntity; //是否为旧的Entity封装
	public static String netterName; //网络器名
	public static NetUsb netter; //普通网络对象
	
	public static Integer exitStyle; //退出方式
}
