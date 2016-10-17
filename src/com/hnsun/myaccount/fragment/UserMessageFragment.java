package com.hnsun.myaccount.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.fragment.common.BaseFragment;

/**
 * 消息碎片
 * @author hnsun
 * @date 2016/09/03
 */
public class UserMessageFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_message, container, false);
		return view;
	}
	
	@Override
	protected void initData(View view, Bundle savedInstanceState) {}
	
	@Override
	protected void initAction(Bundle savedInstanceState) {}
}