package com.hnsun.myaccount.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.QrcodeUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ScreenUtil;

/**
 * 二维码界面
 * @author hnsun
 * @date 2016/10/10
 */
public class QrcodeActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_qrcode);
		getActionBar().setTitle(R.string.txt_qrcode);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		txtvName = (TextView) findViewById(R.id.txtvName);
		ivQrCode = (ImageView) findViewById(R.id.ivQrCode);
		
		setExit(false);
		String accountId = CodeBoss.MessCode.accountId(instance);
		Bitmap bitmap = QrcodeUtil.get(instance, accountId, ScreenUtil.getScreenWidth(instance)); //, PhotoUtil.drawableToBitmap(ResUtil.getDrawable(instance, R.drawable.default_ic_user))
		if(!UtilBoss.StrUtil.isEmpty(accountId)) {
			txtvName.setText(CodeBoss.MessCode.accountName(instance));
			ivQrCode.setImageBitmap(bitmap);
		}
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case android.R.id.home: ((QrcodeActivity) instance).finish(); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}

	private Context instance;
	
	private TextView txtvName;
	private ImageView ivQrCode;
	
	{
		this.instance = QrcodeActivity.this;
	}
}
