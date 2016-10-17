package com.hnsun.myaccount.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.mess.TextChangeAdapter;
import com.hnsun.myaccount.model.dbo.TblUser;
import com.hnsun.myaccount.support.offline.TblUserOffline;
import com.hnsun.myaccount.util.platform.ViewUtil;
import com.hnsun.myaccount.util.view.CharacterParser;

/**
 * 添加联系人界面
 * @author hnsun
 * @date 2016/09/28
 */
public class UserAddActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_user_add);
		getActionBar().setTitle(R.string.btn_add);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		etAccount = (EditText) findViewById(R.id.etAccount);
		ivAccountDelete = (ImageView) findViewById(R.id.ivAccountDelete);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		
		setExit(false);
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		etAccount.addTextChangedListener(twAccount);
		ivAccountDelete.setOnClickListener(onClickListener);
		btnAdd.setOnClickListener(onClickListener);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case android.R.id.home: ((UserAddActivity) instance).finish(); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}

	private Context instance;
	
	private EditText etAccount;
	private ImageView ivAccountDelete;
	private Button btnAdd;
	
	private TextWatcher twAccount = new TextChangeAdapter() {
		
		@Override
		public void afterTextChanged(Editable editable) {
			if(editable.toString().length() > 0) ivAccountDelete.setVisibility(View.VISIBLE);
			else ivAccountDelete.setVisibility(View.GONE);
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.ivAccountDelete: etAccount.setText(""); break;
				case R.id.btnAdd: 
					TblUser user = new TblUser();
					user.setUserLogname(etAccount.getText().toString());
					user.setUserLogpassword(etAccount.getText().toString());
					user.setUserCnname(etAccount.getText().toString());
					user.setUserEnname(CharacterParser.getInstance().spellingStr(etAccount.getText().toString()));
					TblUserOffline.insert(instance, user);
					ViewUtil.displayToast(instance, R.string.msg_success);
					((UserAddActivity) instance).finish();
					break;
			}
		}
	};
	
	{
		this.instance = UserAddActivity.this;
	}
}
