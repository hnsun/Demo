package com.hnsun.myaccount.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;

/**
 * 关于界面
 * @author hnsun
 * @date 2016/08/30
 */
public class AboutActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_about);
		getActionBar().setTitle(R.string.app_info_about);
		getActionBar().setLogo(R.drawable.ic_about);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		setExit(false);
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case android.R.id.home: ((AboutActivity) instance).finish(); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}

	private Context instance;
	
	{
		this.instance = AboutActivity.this;
	}
}
