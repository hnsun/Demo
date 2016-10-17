package com.hnsun.myaccount.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.util.ReflectUtil;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.platform.ResUtil;

/**
 * 应用信息界面
 * @author hnsun
 * @date 2016/09/01
 */
public class InfoActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_info);
		getActionBar().hide();
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		btnReturn = (Button) findViewById(R.id.btnReturn);
		gvMenu = (GridView) findViewById(R.id.gvMenu);
		
		setExit(false);
		gvMenu.setAdapter(new SimpleAdapter(instance, fillMenu(), R.layout.gridview_item_info, new String[] { "image", "txt" }, new int[] { R.id.ivIcon, R.id.txtvText }));
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				((InfoActivity) instance).finish();
			}
		});
		gvMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
				Map<String, Object> map = (Map<String, Object>) adapterView.getAdapter().getItem(index);
				String url = (String) map.get("url");
				startActivity(new Intent(instance, ReflectUtil.getClazz("com.hnsun.myaccount.activity." + url)));
			}
		});
	}
	
	private List<Map<String, Object>> fillMenu() { //填充菜单
		List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < ApplicationDatas.TOURIST_MENU_TEXT.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("txt", ResUtil.getText(instance, ApplicationDatas.TOURIST_MENU_TEXT[i]));
			map.put("image", ApplicationDatas.TOURIST_MENU_IMAGE[i]);
			map.put("url", ApplicationDatas.TOURIST_MENU_URL[i]);
			ret.add(map);
		}
		return ret;
	}

	private Context instance;
	private Button btnReturn;
	private GridView gvMenu;
	
	{
		this.instance = InfoActivity.this;
	}
}
