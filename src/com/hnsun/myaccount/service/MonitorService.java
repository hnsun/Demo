package com.hnsun.myaccount.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.model.DownloadModel;
import com.hnsun.myaccount.model.dbo.TblFile;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.file.Downloader;

/**
 * 全局应用监测服务
 * @author hnsun
 * @date 2016/09/21
 */
public class MonitorService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		registerReceiver(downloadBroadcastReceiver, new IntentFilter(MonitorService.ACTION_DOWNLOAD));
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(downloadBroadcastReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	private void downloadFile(TblFile file) {
		UtilBoss.ThreadUtil.openThread(new ArgumentCallback() {
			
			@Override
			public void action(Object... objs) {
				DownloadModel model = new DownloadModel();
				model.setName(((TblFile) objs[0]).getFileName());
				model.setUrl(((TblFile) objs[0]).getFileUrl());
				Downloader.toDatabase(instance, ApplicationDatas.DOWNLOAD_THREADCOUNT, model);
			}
		}, file);
	}
	
	private Context instance;
	
	private BroadcastReceiver downloadBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			downloadFile((TblFile) intent.getSerializableExtra("TblFile"));
		}
	};
	
	public static final String ACTION_DOWNLOAD = "ACTION_DOWNLOAD";
	
	{
		this.instance = MonitorService.this;
	}
}
