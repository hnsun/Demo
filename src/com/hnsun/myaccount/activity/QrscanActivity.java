package com.hnsun.myaccount.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.comyou.qrscan.QRScanListener;
import com.comyou.qrscan.QRScanManager;
import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.util.platform.AnimUtil;
import com.hnsun.myaccount.util.platform.ScreenUtil;
import com.hnsun.myaccount.util.platform.ViewUtil;
import com.hnsun.myaccount.view.ScanLineView;

/**
 * 二维码扫描界面
 * @author hnsun
 * @date 2016/10/10
 */
public class QrscanActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_qrscan);
		getActionBar().setTitle(R.string.txt_qrscan);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		btnQrcode = (Button) findViewById(R.id.btnQrcode);
		rlConent = (RelativeLayout) findViewById(R.id.rlContent);
		slvLine = (ScanLineView) findViewById(R.id.slvLine);
		
		setExit(false);
		manager = new QRScanManager(qrScanListener);
		manager.setCropRect(initContent());
		manager.initWithSurfaceView(QrscanActivity.this, R.id.svContent);
		manager.setBeepResource(R.raw.beep);
		slvLine.startAnimation(AnimUtil.updown());
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnQrcode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				startActivity(new Intent(instance, QrcodeActivity.class));
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		manager.onResume();
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		manager.onPause();
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.onDestroy();
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case android.R.id.home: ((QrscanActivity) instance).finish(); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}

	private Rect initContent() {
		int screenWidth = ScreenUtil.getScreenWidth(instance);
		
		LayoutParams params = (LayoutParams) rlConent.getLayoutParams();
		int x = params.leftMargin;
		int y = params.topMargin;
		int width = screenWidth - 2 * x;
		int height = width;
		
		params.height = height;
		rlConent.setLayoutParams(params);
		return new Rect(x, y, width + x, height + y);
	}
	
	private Context instance;
	private QRScanManager manager;
	
	private Button btnQrcode;
	private RelativeLayout rlConent;
	private ScanLineView slvLine;
	
	private QRScanListener qrScanListener = new QRScanListener() {
		
		@Override
		public void onScanResult(String content) {
			ViewUtil.displayToast(instance, content);
			((QrscanActivity) instance).finish();
		}
	};
	
	{
		this.instance = QrscanActivity.this;
	}
}
