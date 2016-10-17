package com.hnsun.myaccount.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.fragment.MallFileFragment;
import com.hnsun.myaccount.fragment.NavigationFragment;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.model.dbo.TblFile;
import com.hnsun.myaccount.service.MonitorService;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ViewUtil;
import com.hnsun.myaccount.view.SlidingLinearLayout;

/**
 * 文件商城界面
 * @author hnsun
 * @date 2016/10/08
 */
public class MallFileActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_mall_file);
		getActionBar().setTitle(R.string.title_mall_file);
		getActionBar().setLogo(R.drawable.ic_file);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(UtilBoss.ObjUtil.isNull(savedInstanceState)) {
			Fragment fragment = NavigationFragment.newInstance(NavigationFragment.FLAG_MALL_FILE);
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.add(R.id.flNavigation, (NavigationFragment) fragment, "Navigation");
			transaction.commit();
		}
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		flContent = (FrameLayout) findViewById(R.id.flContent);
		sllContent = (SlidingLinearLayout) findViewById(R.id.sllContent);
		
		setExit(false);
		sllContent.setScrollEvent(flContent);
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		setOnFragmentClickListener(new OnFragmentClickListener() {
			
			@Override
			public void onFragmentClick(View view, int tag, Object... objs) {
				switch(tag) {
					case NavigationFragment.TO_ZONE: startActivity(new Intent(instance, DownloadActivity.class)); break;
					case NavigationFragment.FOR_ITEM: 
						Fragment fragment = MallFileFragment.newInstance((Integer) objs[0]);
						FragmentTransaction ft = getFragmentManager().beginTransaction();
						ft.replace(R.id.flContent, fragment); //替换那个 一定要是 ViewGroup
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//						ft.addToBackStack(null); //使后退到这个事务
						ft.commit();
						
						if(UtilBoss.ObjUtil.isNotNull(sllContent)) sllContent.scrollToRightLayout();
						break;
					case MallFileFragment.FOR_ITEM:
						if(!SystemStatus.netState) {
							ViewUtil.displayToast(instance, R.string.msg_net_disconnected);
							return;
						}
						
						Intent intent =new Intent();
						intent.setAction(MonitorService.ACTION_DOWNLOAD);
						intent.putExtra("TblFile", (TblFile) objs[0]);
						sendBroadcast(intent);
						ViewUtil.displayToast(instance, R.string.msg_success_download);
						break;
					case MallFileFragment.TO_MENU: 
						if(sllContent.isLeftLayoutVisible()) sllContent.scrollToRightLayout();
						else sllContent.scrollToLeftLayout();
						break;
					case MallFileFragment.TO_ZONE: startActivity(new Intent(instance, DownloadActivity.class)); break;
				}
			}
		});
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case android.R.id.home: ((MallFileActivity) instance).finish(); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}
	
	public SlidingLinearLayout getSllContent() {
		return sllContent;
	}

	private Context instance;
	
	private FrameLayout flContent;
	private SlidingLinearLayout sllContent;
	
	{
		this.instance = MallFileActivity.this;
	}
}
