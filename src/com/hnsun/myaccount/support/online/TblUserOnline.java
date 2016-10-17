package com.hnsun.myaccount.support.online;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * 用户在线
 * @author hnsun
 * @date 2016/09/17
 */
public class TblUserOnline {
	
	public TblUserOnline(Handler handler) { 
		if(UtilBoss.ObjUtil.isNull(handler)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.handler = handler; 
	}
	
	public void login(String username, String password) { //登陆
		UtilBoss.ConditionUtil.n(username, password);
		UtilBoss.ThreadUtil.openThread(callback, TblUserOnline.FROM_LOGIN, username, password);
	}
	
	public void register(String username, String password) { //注册
		UtilBoss.ConditionUtil.n(username, password);
		UtilBoss.ThreadUtil.openThread(callback, TblUserOnline.FROM_REGISTER, username, password);
	}
	
	public void logExist(String username) { //是否存在用户
		UtilBoss.ConditionUtil.n(username);
		UtilBoss.ThreadUtil.openThread(callback, TblUserOnline.FROM_LOGEXIST, username);
	}

	private Handler handler; //回调给UI线程操作
	private ArgumentCallback callback = new ArgumentCallback() { //在异线程的操作
		
		@Override
		public void action(Object... objs) {
			int from = (Integer) objs[0];
			String urlStr = null;
			Map<String, Object> params = new HashMap<String, Object>();
			
			switch(from) {
				case TblUserOnline.FROM_LOGIN:
					urlStr = SystemStatus.server + "formLoginUser.do";
					params.clear();
					params.put("userLogname", objs[1]); //request.getParameter获取或通过框架封装的
					params.put("userLogpassword", objs[2]);
					
					CodeBoss.MessCode.handlerMsg(handler, from, SystemStatus.netter.doPost(urlStr, params));
					break;
				case TblUserOnline.FROM_REGISTER:
					urlStr = SystemStatus.server + "formAddUser.do";
					params.clear();
					params.put("userLogname", objs[1]); //request.getParameter获取或通过框架封装的
					params.put("userLogpassword", objs[2]);
					
					CodeBoss.MessCode.handlerMsg(handler, from, SystemStatus.netter.doPost(urlStr, params));
					break;
				case TblUserOnline.FROM_LOGEXIST:
					urlStr = SystemStatus.server + "logExist.do";
					params.clear();
					params.put("userLogname", objs[1]); //request.getParameter获取或通过框架封装的
					
					CodeBoss.MessCode.handlerMsg(handler, from, SystemStatus.netter.doPost(urlStr, params));
					break;
			}
		}
	};
	
	public static final int FROM_LOGIN = 0x001;
	public static final int FROM_REGISTER = 0x002;
	public static final int FROM_LOGEXIST = 0x003;
}
