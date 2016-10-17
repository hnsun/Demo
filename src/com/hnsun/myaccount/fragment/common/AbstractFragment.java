package com.hnsun.myaccount.fragment.common;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.AbstractActivity;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ViewUtil;

/**
 * 所有Fragment都需实现的抽象类（皮肤化失败）
 * @author hnsun
 * @date 2016/08/27
 * 处理：跳转处理、
 */
public abstract class AbstractFragment extends Fragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof AbstractActivity) this.activity = (AbstractActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View ret = initView(inflater, container, savedInstanceState);
		initData(ret, savedInstanceState);
		initAction(savedInstanceState);
		ViewUtil.changeFonts(ret.findViewById(R.id.flayoutWrapper), getActivity(), null);
		return ret;
	}
	
	protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState); //视图创建
	
	protected abstract void initData(View view, Bundle savedInstanceState); //数据准备

	protected abstract void initAction(Bundle savedInstanceState); //事件处理

	protected OnSkipClickListenter getOnSkipClickListenter(int tag, Object... objs) { return new OnSkipClickListenter(tag, objs); } //获取跳转事件处理

	private AbstractActivity activity; //当前实例
	
	/**
	 * 当需要切换Activity或Fragment的操作时统一实现
	 * @author hnsun
	 * @date 2016/08/27
	 */
	protected class OnSkipClickListenter {
		
		public OnSkipClickListenter(int tag, Object... objs) { 
			this.tag = tag;
			this.objs = objs;
		}
		
		public void onAction(View view) {
			if(UtilBoss.ObjUtil.isNull(activity)) return ;
			if(UtilBoss.ObjUtil.isNotNull(activity.getOnFragmentClickListener())) activity.getOnFragmentClickListener().onFragmentClick(view, tag, objs);
		}

		private int tag;
		private Object[] objs;
	}
}
