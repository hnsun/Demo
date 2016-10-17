package com.hnsun.myaccount.fragment;

import com.hnsun.myaccount.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * 设置碎片（视图不美观，暂无用）
 * @author hnsun
 * @date 2016/09/01
 */
public class SettingFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
	}
}
