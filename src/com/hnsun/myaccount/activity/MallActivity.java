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
 * U城界面
 * @author hnsun
 * @date 2016/10/08
 */
public class MallActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_mall);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		btnCommodity = (Button) findViewById(R.id.btnCommodity);
		btnFile = (Button) findViewById(R.id.btnFile);
		btnArticle = (Button) findViewById(R.id.btnArticle);
		btnReturn = (Button) findViewById(R.id.btnReturn);
		
		setExit(false);
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnCommodity.setOnClickListener(onClickListener);
		btnFile.setOnClickListener(onClickListener);
		btnArticle.setOnClickListener(onClickListener);
		btnReturn.setOnClickListener(onClickListener);
	}
	
	private Context instance;
	
	private Button btnCommodity;
	private Button btnFile;
	private Button btnArticle;
	private Button btnReturn;
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnCommodity: CodeBoss.MessCode.development(instance); break;
				case R.id.btnFile: startActivity(new Intent(instance, MallFileActivity.class)); break;
				case R.id.btnArticle: CodeBoss.MessCode.development(instance); break;
				case R.id.btnReturn: ((MallActivity) instance).finish(); break;
			}
		}
	};
	
	{
		this.instance = MallActivity.this;
	}
}
