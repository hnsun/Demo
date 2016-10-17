package com.hnsun.myaccount.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.MallFileActivity;
import com.hnsun.myaccount.fragment.common.BaseFragment;
import com.hnsun.myaccount.mess.ContextHandler;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.model.dbo.TblFile;
import com.hnsun.myaccount.support.offline.PlfDownloadthreadOffline;
import com.hnsun.myaccount.support.offline.TblFileOffline;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.platform.ResUtil;
import com.hnsun.myaccount.util.view.ViewHolder;
import com.hnsun.myaccount.util.view.adapter.AdapterForList;

/**
 * 文件碎片
 * @author hnsun
 * @date 2016/10/09
 */
public class MallFileFragment extends BaseFragment {
	
	public static MallFileFragment newInstance(int index) {
		MallFileFragment ret = new MallFileFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(MallFileFragment.TAG_INDEX, index);
		ret.setArguments(bundle);
		return ret;
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mall_file, container, false);
		return view;
	}
	
	@Override
	protected void initData(View view, Bundle savedInstanceState) {
		txtvPosition = (TextView) view.findViewById(R.id.txtvPosition);
		btnMenu = (Button) view.findViewById(R.id.btnMenu);
		btnZone = (Button) view.findViewById(R.id.btnZone);
		lvContent = (ListView) view.findViewById(R.id.lvContent);

		((MallFileActivity) getActivity()).getSllContent().setScrollEvent(lvContent);
		lvContent.setAdapter(adapter = new AdapterForList<TblFile>(getActivity(), R.layout.listview_item_mall_file, dataSource) {

			@Override
			protected void dataInjected(int index, View view, TblFile data) {
				String[] str = data.getFileName().split("\\.");
				ViewHolder.get(view, R.id.txtvName, TextView.class).setText(str[0]);
				ViewHolder.get(view, R.id.txtvIcon, TextView.class).setText(str[1].toUpperCase(Locale.getDefault()));
				ViewHolder.get(view, R.id.txtvIcon, TextView.class).setBackgroundResource(typeIcon); //根据文件类型设置
				ViewHolder.get(view, R.id.txtvDesc, TextView.class).setText(data.getFileDesc());
				
				final TblFile file = data;
				Button btnDownload = ViewHolder.get(view, R.id.btnDownload, Button.class);
				btnDownload.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						getOnSkipClickListenter(MallFileFragment.FOR_ITEM, file).onAction(view);
						if(SystemStatus.netState) btnState(view, true);
					}
				});
				btnState(btnDownload, PlfDownloadthreadOffline.existUrl(getActivity(), file.getFileUrl()));
			}
			
			private void btnState(View view, boolean pressed) {
				if(pressed) {
					view.setBackgroundColor(ResUtil.getColor(getActivity(), R.color.transparent));
					((TextView) view).setTextColor(ResUtil.getColor(getActivity(), R.color.red));
					((TextView) view).setText(ResUtil.getText(getActivity(), R.string.btn_added));
				} else {
					view.setBackgroundResource(R.drawable.btn_rect_not);
					((TextView) view).setTextColor(ResUtil.getColor(getActivity(), R.color.white));
					((TextView) view).setText(ResUtil.getText(getActivity(), R.string.btn_download));
				}
			}
		});
		tblFileOffline = new TblFileOffline(getActivity(), tblFileOfflineHandler.setObj(MallFileFragment.this));
		fromDatabase();
	}
	
	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnMenu.setOnClickListener(onClickListener);
		btnZone.setOnClickListener(onClickListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		fromDatabase();
	}

	private void fromDatabase() {
		int resId = ApplicationDatas.NAVIGATION_MALL_FILE[getArguments().getInt(MallFileFragment.TAG_INDEX)];
		txtvPosition.setText(ResUtil.getText(getActivity(), resId));
		switch(resId) {
			case R.string.txt_home: 
				typeIcon = R.drawable.bg_business_mall_file_home;
				tblFileOffline.list(); 
				break;
			case R.string.txt_text: 
				typeIcon = R.drawable.bg_business_mall_file_txt;
				tblFileOffline.list(); 
				break;
			case R.string.txt_picture: 
				typeIcon = R.drawable.bg_business_mall_file_img;
				tblFileOffline.list(); 
				break;
			case R.string.txt_other: 
				typeIcon = R.drawable.bg_business_mall_file_other;
				fillDataSource(new ArrayList<TblFile>());
				break;
			default: break;
		}
	}
	
	public void fillDataSource(List<TblFile> list) {
		dataSource.clear();
		if(UtilBoss.ObjUtil.isNull(list)) return;
		
		dataSource.addAll(list);
		adapter.updateListView(dataSource);
	}

	private int typeIcon;
	private TblFileOffline tblFileOffline;
	private List<TblFile> dataSource;
	private AdapterForList<TblFile> adapter;
	
	private TextView txtvPosition;
	private Button btnMenu;
	private Button btnZone;
	private ListView lvContent;
	
	private static ContextHandler tblFileOfflineHandler = new ContextHandler() { //文件线上主线程
		
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case TblFileOffline.LIST:
					List<TblFile> list = (List<TblFile>) msg.obj;
					((MallFileFragment) getObj()).fillDataSource(list);
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnMenu: getOnSkipClickListenter(MallFileFragment.TO_MENU).onAction(view); break;
				case R.id.btnZone: getOnSkipClickListenter(MallFileFragment.TO_ZONE).onAction(view); break;
			}
		}
	};

	public static final String TAG_INDEX = "TAG_INDEX";

	public static final int FOR_ITEM = 0x120201;
	public static final int TO_MENU = 0x120202;
	public static final int TO_ZONE = 0x120203;
	
	{
		this.dataSource= new ArrayList<TblFile>(); 
	}
}