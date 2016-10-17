package com.hnsun.myaccount.util.net;

import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;

import android.content.Context;

/**
 * 网络工厂
 * @author hnsun
 * @date 2016/09/11
 */
public class NetFactory {
	
	/**
	 * 根据全局变量获取相应网络访问器 使用getApplicationContext()
	 * @param context
	 * @return
	 */
	public static NetUsb net(Context context) {
		UtilBoss.ConditionUtil.n(context);
		if(UtilBoss.StrUtil.isEmpty(SystemStatus.netterName)) SystemStatus.netterName = ApplicationDatas.NET_APACHE;
		
		if(ApplicationDatas.NET_APACHE.equals(SystemStatus.netterName)) SystemStatus.netter = NetFactory.apacheNet(context);
		if(ApplicationDatas.NET_URLCONNECTION.equals(SystemStatus.netterName)) SystemStatus.netter = NetFactory.uRLConnectionNet(context);
		return SystemStatus.netter;
	}

	/**
	 * 获取ApacheNet
	 * @param context
	 * @return
	 */
	public static NetUsb apacheNet(Context context) {
		UtilBoss.ConditionUtil.n(context);
		
		if(UtilBoss.ObjUtil.isNull(SystemStatus.netter)) SystemStatus.netter = new ApacheNet(context);
		else {
			String name = SystemStatus.netter.getClass().getName();
			if(!ApplicationDatas.NET_APACHE.equals(UtilBoss.StrUtil.substring(name, name.lastIndexOf(".") + 1))) SystemStatus.netter = new ApacheNet(context);
		}
		
		return SystemStatus.netter;
	}
	
	/**
	 * 获取URLConnectionNet
	 * @param context
	 * @return
	 */
	public static NetUsb uRLConnectionNet(Context context) {
		UtilBoss.ConditionUtil.n(context);
		
		if(UtilBoss.ObjUtil.isNull(SystemStatus.netter)) SystemStatus.netter = new URLConnectionNet(context);
		else {
			String name = SystemStatus.netter.getClass().getName();
			if(!ApplicationDatas.NET_URLCONNECTION.equals(UtilBoss.StrUtil.substring(name, name.lastIndexOf(".") + 1))) SystemStatus.netter = new URLConnectionNet(context);
		}
		
		return SystemStatus.netter;
	}
}
