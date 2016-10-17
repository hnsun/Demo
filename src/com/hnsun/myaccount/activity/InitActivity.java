package com.hnsun.myaccount.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.util.CodeBoss;

/**
 * 初始化界面
 * @author hnsun
 * @date 2016/08/20
 */
public class InitActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_init);
		getActionBar().hide();
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		btnUser = (Button) findViewById(R.id.btnUser);
		btnTourist = (Button) findViewById(R.id.btnTourist);
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnUser.setOnClickListener(onClickListener);
		btnTourist.setOnClickListener(onClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(CodeBoss.MessCode.accountState(instance)) { //已登陆
			startActivity(new Intent(instance, UserMainActivity.class));
			((InitActivity) instance).finish();
		}
	}

	private Context instance;
	
	private Button btnUser;
	private Button btnTourist;
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnUser: startActivity(new Intent(instance, UserEntranceActivity.class)); break;
				case R.id.btnTourist: startActivity(new Intent(instance, TouristMainActivity.class)); break;
			}
		}
	};
	
	{
		this.instance = InitActivity.this;
	}
}
