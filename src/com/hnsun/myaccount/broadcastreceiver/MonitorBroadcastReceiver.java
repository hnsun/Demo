package com.hnsun.myaccount.broadcastreceiver;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.platform.NetUtil;
import com.hnsun.myaccount.util.platform.ViewUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * 全局广播接收器
 * @author hnsun
 * @date 2016/09/21
 */
public class MonitorBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(MonitorBroadcastReceiver.ACTION_SYSTEM_NETWORK.equals(intent.getAction())) { //网络监测
			if(NetUtil.checkNetworkInfo(context)) SystemStatus.netState = true; //网络接通设为true
			else { //网络断开 判断原来状态
				if(SystemStatus.netState) {
					SystemStatus.netState = false;
					ViewUtil.displayToast(context, R.string.msg_net_disconnected);
				}
			}
		} else if(MonitorBroadcastReceiver.ACTION_LOCAL.equals(intent.getAction())) { //当前应用
		}
	}

	public static final String TAG_CMD = "TAG_CMD";
	public static final String TAG_OBJ = "TAG_OBJ";
	
	public static final String ACTION_LOCAL = "com.hnsun.u.broadcastreceiver.MonitorBroadcastReceiver";
	public static final String ACTION_SYSTEM_NETWORK = ConnectivityManager.CONNECTIVITY_ACTION;
}
