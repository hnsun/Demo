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
import com.hnsun.myaccount.business.passcode.AbstractPasscodeActivity;
import com.hnsun.myaccount.business.passcode.AppLockManager;
import com.hnsun.myaccount.business.passcode.PasscodeManageActivity;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.platform.ResUtil;
import com.hnsun.myaccount.util.platform.SharedPreferencesUtil;
import com.hnsun.myaccount.util.platform.ViewUtil;
import com.hnsun.myaccount.view.DescButton;

/**
 * 设置界面
 * @author hnsun
 * @date 2016/08/30
 */
public class SettingActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_setting);
		getActionBar().setTitle(R.string.btn_setting);
		getActionBar().setLogo(R.drawable.ic_setting);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		btnPasscodeState = (Button) findViewById(R.id.btnPasscodeState);
		btnPasscodeUpdate = (Button) findViewById(R.id.btnPasscodeUpdate);
		btnSysDirectory = (DescButton) findViewById(R.id.btnSysDirectory);
		
		setExit(false);
		btnSysDirectory.setInfoText(new SharedPreferencesUtil(instance).lockLoad(SharedPreferencesUtil.KEY_APP_DIRECTORY));
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnPasscodeState.setOnClickListener(onClickListener);
		btnPasscodeUpdate.setOnClickListener(onClickListener);
		btnSysDirectory.setOnClickListener(onClickListener);
	}
	
    @Override
	protected void onStart() {
		super.onStart();
    	if(AppLockManager.getInstance().getAppLock().basicDone()) {
    		btnPasscodeState.setText(ResUtil.getText(instance, R.string.btn_passcode_close));
//    		btnPasscodeUpdate.setVisibility(View.VISIBLE);
    		btnPasscodeUpdate.setEnabled(true);
    		btnPasscodeUpdate.setTextColor(ResUtil.getColor(instance, R.color.txtTxtvDetailInfo));
    	} else {
    		btnPasscodeState.setText(ResUtil.getText(instance, R.string.btn_passcode_open));
//    		btnPasscodeUpdate.setVisibility(View.GONE);
    		btnPasscodeUpdate.setEnabled(false);
    		btnPasscodeUpdate.setTextColor(ResUtil.getColor(instance, R.color.defaultFontDown));
    	}
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) {
            case PasscodeManageActivity.TYPE_ENABLE:
                if(resultCode == RESULT_OK) ViewUtil.displayToast(instance, R.string.msg_passcode_open);
                break;
            case PasscodeManageActivity.TYPE_DISABLE:
                if(resultCode == RESULT_OK) ViewUtil.displayToast(instance, R.string.msg_passcode_cancel);
                break;
            case PasscodeManageActivity.TYPE_CHANGE:
                if(resultCode == RESULT_OK) ViewUtil.displayToast(instance, R.string.msg_passcode_update);
                break;
        }
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case android.R.id.home: ((SettingActivity) instance).finish(); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}

	private Context instance;
	
	private Button btnPasscodeState;
	private Button btnPasscodeUpdate;
	private DescButton btnSysDirectory;
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnPasscodeState: 
			        int type = AppLockManager.getInstance().getAppLock().basicDone() ? PasscodeManageActivity.TYPE_DISABLE :PasscodeManageActivity.TYPE_ENABLE;
			        Intent intent1 = new Intent(instance, PasscodeManageActivity.class);
			        intent1.putExtra(PasscodeManageActivity.FLAG_TYPE, type);
			        intent1.putExtra(AbstractPasscodeActivity.FLAG_PASSCODE_TITLE, ResUtil.getText(instance, R.string.title_passcode_input));
			        startActivityForResult(intent1, type);
					break;
				case R.id.btnPasscodeUpdate: 
					Intent intent2 = new Intent(instance, PasscodeManageActivity.class);
					intent2.putExtra(PasscodeManageActivity.FLAG_TYPE, PasscodeManageActivity.TYPE_CHANGE);
					intent2.putExtra(AbstractPasscodeActivity.FLAG_PASSCODE_TITLE, ResUtil.getText(instance, R.string.title_passcode_input_old));
			        startActivityForResult(intent2, PasscodeManageActivity.TYPE_CHANGE);
					break;
				case R.id.btnSysDirectory: CodeBoss.MessCode.development(instance); break;
			}
		}
	};
	
	{
		this.instance = SettingActivity.this;
	}
}
