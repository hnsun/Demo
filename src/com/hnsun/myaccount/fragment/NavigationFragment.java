package com.hnsun.myaccount.fragment;

import java.util.Arrays;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.fragment.common.BaseFragment;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.platform.ResUtil;
import com.hnsun.myaccount.util.platform.ViewUtil;
import com.hnsun.myaccount.util.view.ViewHolder;
import com.hnsun.myaccount.util.view.adapter.AdapterForList;

/**
 * 导航列碎片
 * @author hnsun
 * @date 2016/10/09
 */
public class NavigationFragment extends BaseFragment {
	
	public static NavigationFragment newInstance(int flag) {
		NavigationFragment ret = new NavigationFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(NavigationFragment.TAG_FLAG, flag);
		ret.setArguments(bundle);
		return ret;
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_navigation, container, false);
		return view;
	}
	
	@Override
	protected void initData(View view, Bundle savedInstanceState) {
		btnZone = (Button) view.findViewById(R.id.btnZone);
		lvContent = (ListView) view.findViewById(R.id.lvContent);

		flag = getArguments().getInt(NavigationFragment.TAG_FLAG);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
//		for(Integer resId : getResourceId()) {
//			adapter.add(ResUtil.getText(getActivity(), resId));
//		}
		lvContent.setAdapter(new AdapterForList<Integer>(getActivity(), R.layout.listview_item_navigation, Arrays.asList(getResourceId())) {
			
			@Override
			protected void dataInjected(int index, View view, Integer data) {
				ViewHolder.get(view, R.id.txtvContent, TextView.class).setText(ResUtil.getText(getActivity(), data));
			}
		});
		lvContent.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		ViewUtil.setListViewHeightBasedOnChildren(lvContent);
		
		if(UtilBoss.ObjUtil.isNotNull(savedInstanceState)) {
			selectedIndex = savedInstanceState.getInt(NavigationFragment.TAG_SELECTED, 0);
			showedIndex = savedInstanceState.getInt(NavigationFragment.TAG_SHOWED, -1);
		}
		showContent(selectedIndex);
	}
	
	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnZone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				getOnSkipClickListenter(NavigationFragment.TO_ZONE).onAction(view);
			}
		});
		lvContent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
				showContent(index);
			}
		});
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(NavigationFragment.TAG_SELECTED, selectedIndex);
		outState.putInt(NavigationFragment.TAG_SHOWED, showedIndex);
	}

	private Integer[] getResourceId() {
		Integer[] ret = null;
		
		switch(flag) {
			case NavigationFragment.FLAG_MALL_Commodity: break;
			case NavigationFragment.FLAG_MALL_FILE: 
				ret = ApplicationDatas.NAVIGATION_MALL_FILE; 
				btnZone.setText(ResUtil.getText(getActivity(), R.string.btn_download_zone));
				break;
			case NavigationFragment.FLAG_MALL_Article: break;
			default: break;
		}
		return ret;
	}
	
	private void showContent(int index) {
		selectedIndex = index;
		lvContent.setItemChecked(index, true);
		
		if(selectedIndex != showedIndex) {
			getOnSkipClickListenter(NavigationFragment.FOR_ITEM, index).onAction(null);
			showedIndex = selectedIndex;
		}
	}
	
	private int flag;
	private int selectedIndex; //当前选择项
	private int showedIndex; //当前展示项
	
	private Button btnZone;
	private ListView lvContent;

	public static final String TAG_FLAG = "TAG_FLAG";
	public static final String TAG_SELECTED = "TAG_SELECTED";
	public static final String TAG_SHOWED = "TAG_SHOWED";
	
	public static final int FLAG_MALL_Commodity = 0x0001; //商品商城
	public static final int FLAG_MALL_FILE = 0x0002; //文件商城
	public static final int FLAG_MALL_Article = 0x0003; //读物商城
	
	public static final int TO_ZONE = 0x120101;
	public static final int FOR_ITEM = 0x120102;
	
	{
		this.selectedIndex = 0; //默认选择第一项
		this.showedIndex = -1; //还没显示
	}
}