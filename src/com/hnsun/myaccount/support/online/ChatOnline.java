package com.hnsun.myaccount.support.online;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * 聊天在线
 * @author hnsun
 * @date 2016/09/23
 */
public class ChatOnline {
	
	public ChatOnline(Handler handler) { this.handler = handler; }
	
	public void robot(String msg) { //与机器人
		UtilBoss.ConditionUtil.n(msg);
		UtilBoss.ThreadUtil.openThread(callback, ChatOnline.FROM_ROBOT, msg);
	}

	private Handler handler; //回调给UI线程操作
	private ArgumentCallback callback = new ArgumentCallback() { //在异线程的操作
		
		@Override
		public void action(Object... objs) {
			int from = (Integer) objs[0];
			String urlStr = null;
			Map<String, Object> params = new HashMap<String, Object>();
			
			switch(from) {
				case ChatOnline.FROM_ROBOT:
					urlStr = "http://www.tuling123.com/openapi/api";
					params.clear();
					params.put("key", "ced418c6ec52439ba38a379d7f717c05");
					params.put("info", objs[1]);
					params.put("userid", SystemStatus.uniqueCode);
					
					CodeBoss.MessCode.handlerMsg(handler, from, SystemStatus.netter.doPost(urlStr, params));
					break;
			}
		}
	};
	
	public static final int FROM_ROBOT = 0x001;
}
