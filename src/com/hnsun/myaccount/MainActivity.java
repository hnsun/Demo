package com.hnsun.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hnsun.myaccount.activity.InitActivity;
import com.hnsun.myaccount.activity.UserMainActivity;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.service.MonitorService;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.test.TestManager;

/**
 * 主程序入口
 * @author hnsun
 * @date 2016/08/15
 */
public class MainActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_main);
		if(UtilBoss.ObjUtil.isNotNull(getActionBar())) getActionBar().hide();

		TestManager.testVoid(); //测试数据
		startService(new Intent(instance, MonitorService.class));
    	startActivity(new Intent(instance, CodeBoss.MessCode.accountState(instance) ? UserMainActivity.class : InitActivity.class)); //是否已登陆
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //Activity切换时动画
		((MainActivity) instance).finish();
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		fillData();
//		SQLiteInstance.copyDatabase(instance);
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {}

	private void fillData() {
//		TblFile file1 = new TblFile();
//		file1.setFileDate(new Date());
//		file1.setFileDesc("弹着吉他唱着歌");
//		file1.setFilePlatform('A');
//		file1.setFileUrl("http://wt.sc.chinaz.com/Files/DownLoad/pic9/201606/fpic5106.rar");
//		file1.setFileName("吉他.rar");
//		TblFileOffline.insert(instance, file1);
//		TblFile file2 = new TblFile();
//		file2.setFileDate(new Date());
//		file2.setFileDesc("左手右手一个慢动作");
//		file2.setFilePlatform('A');
//		file2.setFileUrl("http://wt.sc.chinaz.com/Files/DownLoad/pic9/201606/apic21392.rar");
//		file2.setFileName("拉手.rar");
//		TblFileOffline.insert(instance, file2);
//		TblFile file3 = new TblFile();
//		file3.setFileDate(new Date());
//		file3.setFileDesc("我喜欢柠檬的味道");
//		file3.setFilePlatform('A');
//		file3.setFileUrl("http://wt.sc.chinaz.com/Files/DownLoad/pic9/201606/apic21402.rar");
//		file3.setFileName("柠檬.rar");
//		TblFileOffline.insert(instance, file3);
	}
	
	private Context instance;
	
	{
		this.instance = MainActivity.this;
	}
}
