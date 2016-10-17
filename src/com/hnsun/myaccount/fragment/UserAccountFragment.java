package com.hnsun.myaccount.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.fragment.common.BaseFragment;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * 用户账户碎片
 * @author hnsun
 * @date 2016/09/03
 */
public class UserAccountFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_account, container, false);
		return view;
	}
	
	@Override
	protected void initData(View view, Bundle savedInstanceState) {
		txtvName = (TextView) view.findViewById(R.id.txtvName);
		txtvDesc = (TextView) view.findViewById(R.id.txtvDesc);
		btnAlbum = (Button) view.findViewById(R.id.btnAlbum);
		btnAccountBook = (Button) view.findViewById(R.id.btnAccountBook);
		btnCollection = (Button) view.findViewById(R.id.btnCollection);
		btnZone = (Button) view.findViewById(R.id.btnZone);
		btnSetting = (Button) view.findViewById(R.id.btnSetting);
		btnLogout = (Button) view.findViewById(R.id.btnLogout);
		ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
		rlUser = (RelativeLayout) view.findViewById(R.id.rlUser);
		
		String accountId = CodeBoss.MessCode.accountId(getActivity());
		if(!UtilBoss.StrUtil.isEmpty(accountId)) { //已登陆
			txtvName.setText(CodeBoss.MessCode.accountName(getActivity()));
			txtvDesc.setText("一个懒人"); //根据ID查找
			CodeBoss.ViewCode.userIcon(ivIcon, SystemStatus.curAccount.name);
		}
	}
	
	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnAlbum.setOnClickListener(onClickListener);
		btnAccountBook.setOnClickListener(onClickListener);
		btnCollection.setOnClickListener(onClickListener);
		btnZone.setOnClickListener(onClickListener);
		btnSetting.setOnClickListener(onClickListener);
		btnLogout.setOnClickListener(onClickListener);
		rlUser.setOnClickListener(onClickListener);
	}

	private TextView txtvName;
	private TextView txtvDesc;
	private Button btnAlbum;
	private Button btnAccountBook;
	private Button btnCollection;
	private Button btnZone;
	private Button btnSetting;
	private Button btnLogout;
	private ImageView ivIcon;
	private RelativeLayout rlUser;
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnAlbum: getOnSkipClickListenter(UserAccountFragment.TO_ALBUM).onAction(view); break;
				case R.id.btnAccountBook: getOnSkipClickListenter(UserAccountFragment.TO_ACCOUNTBOOK).onAction(view); break;
				case R.id.btnCollection: getOnSkipClickListenter(UserAccountFragment.TO_COLLECTION).onAction(view); break;
				case R.id.btnZone: getOnSkipClickListenter(UserAccountFragment.TO_ZONE).onAction(view); break;
				case R.id.btnSetting: getOnSkipClickListenter(UserAccountFragment.TO_SETTING).onAction(view); break;
				case R.id.rlUser: getOnSkipClickListenter(UserAccountFragment.TO_USER).onAction(view); break;
				case R.id.btnLogout: getOnSkipClickListenter(UserAccountFragment.TO_LOGOUT).onAction(view); break;
			}
		}
	};
	
	public static final int TO_USER = 0x120401;
	public static final int TO_ALBUM = 0x120402;
	public static final int TO_ACCOUNTBOOK = 0x120403;
	public static final int TO_COLLECTION = 0x120404;
	public static final int TO_ZONE = 0x120405;
	public static final int TO_SETTING = 0x120406;
	public static final int TO_LOGOUT = 0x120407;
}