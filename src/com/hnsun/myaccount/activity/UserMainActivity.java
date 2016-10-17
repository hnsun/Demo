package com.hnsun.myaccount.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.fragment.UserAccountFragment;
import com.hnsun.myaccount.fragment.UserContactFragment;
import com.hnsun.myaccount.fragment.UserHomeFragment;
import com.hnsun.myaccount.fragment.UserMessageFragment;
import com.hnsun.myaccount.model.dbo.TblUser;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ResUtil;
import com.hnsun.myaccount.util.platform.ViewUtil;
import com.hnsun.myaccount.util.test.TestActivity;
import com.hnsun.myaccount.view.IfDialog;

/**
 * 用户主界面
 * @author hnsun
 * @date 2016/08/29
 */
public class UserMainActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_user_main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //android:windowSoftInputMode="adjustPan" 直接遮盖组件显示
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		btnBarHome = (Button) findViewById(R.id.btnBarHome);
		btnBarContact = (Button) findViewById(R.id.btnBarContact);
		btnBarMessage = (Button) findViewById(R.id.btnBarMessage);
		btnBarAccount = (Button) findViewById(R.id.btnBarAccount);

		if(UtilBoss.ObjUtil.isNull(savedInstanceState)) barButtonSelected(UserMainActivity.BAR_HOME);
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnBarHome.setOnClickListener(onClickListener);
		btnBarContact.setOnClickListener(onClickListener);
		btnBarMessage.setOnClickListener(onClickListener);
		btnBarAccount.setOnClickListener(onClickListener);
		
		setOnFragmentClickListener(new OnFragmentClickListener() {
			
			@Override
			public void onFragmentClick(View view, int tag, Object... objs) {
				switch(tag) {
					case UserHomeFragment.TO_SERVICE_1: break;
					case UserHomeFragment.TO_SERVICE_2: break;
					case UserHomeFragment.TO_SERVICE_3: break;
					case UserContactFragment.TO_LISTITEM:
						Intent intent = new Intent(instance, ChatActivity.class);
						intent.putExtra(ChatActivity.FLAG_CHATTER, (TblUser) objs[0]);
						startActivity(intent);
						break;
					case UserContactFragment.TO_ROBOT: startActivity(new Intent(instance, ChatActivity.class)); break;
					case UserContactFragment.TO_LABEL: ViewUtil.displayToast(instance, "标签"); break;
					case UserContactFragment.TO_QRSCAN: startActivity(new Intent(instance, QrscanActivity.class)); break;
					case UserContactFragment.TO_ADD: startActivity(new Intent(instance, UserAddActivity.class)); break;
					case UserAccountFragment.TO_USER: startActivity(new Intent(instance, UserInfoActivity.class)); break;
					case UserAccountFragment.TO_ZONE: startActivity(new Intent(instance, ZoneActivity.class)); break;
					case UserAccountFragment.TO_SETTING: startActivity(new Intent(instance, SettingActivity.class)); break;
					case UserAccountFragment.TO_LOGOUT: 
			    		new IfDialog(instance).show(ResUtil.getText(instance, R.string.msg_logout), new IfDialog.OnConfirmClickListener() {
							
							@Override
							public void onClick(View view) {
								CodeBoss.MessCode.logoutUser(instance); 
								instance.startActivity(new Intent(instance, TouristMainActivity.class));
							}
						}, IfDialog.Type.WARNING);
						break;
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case R.id.btnRecordAccount: CodeBoss.MessCode.development(instance); break;
			case R.id.btnInfo: startActivity(new Intent(instance, InfoActivity.class)); break;
			case R.id.btnMall: startActivity(new Intent(instance, MallActivity.class)); break;
			case R.id.btnTest: startActivity(new Intent(instance, TestActivity.class)); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}
	
	private void barButtonSelected(int barBtn) { //底部按钮选择
		if(curPart == barBtn) return; //已经是当前显示
		
		ColorStateList colorNormal = ResUtil.getColorState(instance, R.drawable.txt_default);
		btnBarHome.setTextColor(colorNormal);
		btnBarContact.setTextColor(colorNormal);
		btnBarMessage.setTextColor(colorNormal);
		btnBarAccount.setTextColor(colorNormal);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		switch(barBtn) {
			case UserMainActivity.BAR_HOME: 
				transaction.replace(R.id.flContent, new UserHomeFragment(), "UserHome"); 
				btnBarHome.setTextColor(ResUtil.getColor(instance, R.color.defaultFontOn));
				break;
			case UserMainActivity.BAR_CONTACT: 
				transaction.replace(R.id.flContent, new UserContactFragment(), "UserContact"); 
				btnBarContact.setTextColor(ResUtil.getColor(instance, R.color.defaultFontOn));
				break;
			case UserMainActivity.BAR_MESSAGE: 
				transaction.replace(R.id.flContent, new UserMessageFragment(), "UserMessage"); 
				btnBarMessage.setTextColor(ResUtil.getColor(instance, R.color.defaultFontOn));
				break;
			case UserMainActivity.BAR_ACCOUNT: 
				transaction.replace(R.id.flContent, new UserAccountFragment(), "UserAccount"); 
				btnBarAccount.setTextColor(ResUtil.getColor(instance, R.color.defaultFontOn));
				break;
		}
		
		transaction.commit();
		curPart = barBtn;
	}

	private Context instance;
	private int curPart; //当前显示
	
	private Button btnBarHome;
	private Button btnBarContact;
	private Button btnBarMessage;
	private Button btnBarAccount;
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnBarHome: barButtonSelected(UserMainActivity.BAR_HOME); break;
				case R.id.btnBarContact: barButtonSelected(UserMainActivity.BAR_CONTACT); break;
				case R.id.btnBarMessage: barButtonSelected(UserMainActivity.BAR_MESSAGE); break;
				case R.id.btnBarAccount: barButtonSelected(UserMainActivity.BAR_ACCOUNT); break;
			}
		}
	};
	
	private static final int BAR_HOME = 0x110101;
	private static final int BAR_CONTACT = 0x110102;
	private static final int BAR_MESSAGE = 0x110103;
	private static final int BAR_ACCOUNT = 0x110104;
	
	{
		this.instance = UserMainActivity.this;
	}
}
