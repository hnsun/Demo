package com.hnsun.myaccount.business.passcode;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.InitActivity;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.platform.ResUtil;
import com.hnsun.myaccount.view.IfDialog;

/**
 * 解锁界面
 * @author hnsun
 * @date 2016/09/26
 */
public class PasscodeUnlockActivity extends AbstractPasscodeActivity {

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		((PasscodeUnlockActivity) instance).finish();
	}

	@Override
	protected void basicCreated() {
		btnHelp = (Button) findViewById(R.id.btnHelp);
		btnHelp.setText(ResUtil.getText(instance, R.string.btn_help));
		btnHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
	    		new IfDialog(instance).show(ResUtil.getText(instance, R.string.msg_login_reset), new IfDialog.OnConfirmClickListener() {
					
					@Override
					public void onClick(View view) {
						CodeBoss.MessCode.logoutUser(instance); 
						instance.startActivity(new Intent(instance, InitActivity.class));
					}
				}, IfDialog.Type.WARNING);
			}
		});
	}

	@Override
	protected void whenOperated(String passcode) {
		if(AppLockManager.getInstance().getAppLock().verify(passcode)) {
			setResult(RESULT_OK);
			((PasscodeUnlockActivity) instance).finish();
		} else {
			Thread shake = new Thread() {
				
				@Override
				public void run() {
					showMsg(AbstractPasscodeActivity.Type.ERROR);
					reset();
				}
			};
			runOnUiThread(shake);
		}
	}

	private Context instance;
	
	private Button btnHelp; //帮助
	
	{
		this.instance = PasscodeUnlockActivity.this;
	}
}
