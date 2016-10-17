package com.hnsun.myaccount.business.passcode;

import android.content.Context;
import android.os.Bundle;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ResUtil;

/**
 * 应用锁锁管理界面
 * @author hnsun
 * @date 2016/09/26
 */
public class PasscodeManageActivity extends AbstractPasscodeActivity {

	@Override
	protected void basicCreated() {
		Bundle extras = getIntent().getExtras();
		if(UtilBoss.ObjUtil.isNotNull(extras)) type = extras.getInt(PasscodeManageActivity.FLAG_TYPE, -1);
	}

	@Override
	protected void whenOperated(String passcode) {
		switch(type) {
			case PasscodeManageActivity.TYPE_ENABLE: 
                if(UtilBoss.StrUtil.isEmpty(this.passcode)) { //第一次输入
                    this.passcode = passcode;
                    setPasscodeTitle(ResUtil.getText(instance, R.string.title_passcode_again));
                    reset();
                } else {
                    if(this.passcode.equals(passcode)) {
                        setResult(RESULT_OK);
                        AppLockManager.getInstance().getAppLock().set(this.passcode);
            			((PasscodeManageActivity) instance).finish();
                    } else {
                    	this.passcode = null;
                        setPasscodeTitle(ResUtil.getText(instance, R.string.title_passcode_input));
                        showMsg(AbstractPasscodeActivity.Type.ERROR);
                        reset();
                    }
                }
				break;
			case PasscodeManageActivity.TYPE_DISABLE: 
                if(AppLockManager.getInstance().getAppLock().verify(passcode)) {
                    setResult(RESULT_OK);
                    AppLockManager.getInstance().getAppLock().set(null);
        			((PasscodeManageActivity) instance).finish();
                } else {
                	showMsg(AbstractPasscodeActivity.Type.ERROR);
                    reset();
                }
				break;
			case PasscodeManageActivity.TYPE_CHANGE: 
                if(AppLockManager.getInstance().getAppLock().verify(passcode)) {
                    type = PasscodeManageActivity.TYPE_ENABLE;
                    setPasscodeTitle(ResUtil.getText(instance, R.string.title_passcode_input_new));
                } else showMsg(AbstractPasscodeActivity.Type.ERROR);
                reset();
				break;
		}
	}

	private Context instance;
    private int type;
    private String passcode;

	public static final int TYPE_ENABLE = 0x110101;
	public static final int TYPE_DISABLE = 0x110102;
	public static final int TYPE_CHANGE = 0x110103;
    public static final String FLAG_TYPE = "FLAG_TYPE";

    {
		this.instance = PasscodeManageActivity.this;
    	this.type = -1;
    	this.passcode = null;
    }
}
