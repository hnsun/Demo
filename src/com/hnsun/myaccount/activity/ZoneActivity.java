package com.hnsun.myaccount.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;

/**
 * 仓库界面
 * @author hnsun
 * @date 2016/10/15
 */
public class ZoneActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_zone);
		getActionBar().setTitle(R.string.btn_zone);
		getActionBar().setLogo(R.drawable.ic_zone);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		btnDownloadlist = (Button) findViewById(R.id.btnDownloadlist);
		
		setExit(false);
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnDownloadlist.setOnClickListener(onClickListener);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case android.R.id.home: ((ZoneActivity) instance).finish(); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}

	private Context instance;
	
	private Button btnDownloadlist;
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnDownloadlist: startActivity(new Intent(instance, DownloadActivity.class)); break;
			}
		}
	};
	
	{
		this.instance = ZoneActivity.this;
	}
}
