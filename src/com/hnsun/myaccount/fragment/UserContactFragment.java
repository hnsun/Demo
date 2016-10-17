package com.hnsun.myaccount.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.os.Message;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.fragment.common.BaseFragment;
import com.hnsun.myaccount.mess.ContextHandler;
import com.hnsun.myaccount.mess.TextChangeAdapter;
import com.hnsun.myaccount.model.SortModel;
import com.hnsun.myaccount.model.dbo.TblUser;
import com.hnsun.myaccount.support.offline.TblUserOffline;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.ComparatorBoss;
import com.hnsun.myaccount.util.I18nUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.view.CharacterParser;
import com.hnsun.myaccount.util.view.ViewHolder;
import com.hnsun.myaccount.util.view.adapter.AdapterForSortList;
import com.hnsun.myaccount.view.ClearEditText;
import com.hnsun.myaccount.view.LetterBarView;
import com.hnsun.myaccount.view.LetterBarView.OnTouchLetterListener;

/**
 * 联系碎片
 * @author hnsun
 * @date 2016/09/03
 */
public class UserContactFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_contact, container, false);
		return view;
	}
	
	@Override
	protected void initData(View view, Bundle savedInstanceState) {
		txtvDialog = (TextView) view.findViewById(R.id.txtvDialog);
		btnAdd = (Button) view.findViewById(R.id.btnAdd);
		lvContent = (ListView) view.findViewById(R.id.lvContent);
		rlRobot = (RelativeLayout) view.findViewById(R.id.rlRobot);
		rlLabel = (RelativeLayout) view.findViewById(R.id.rlLabel);
		rlQrscan = (RelativeLayout) view.findViewById(R.id.rlQrscan);
		cetSearch = (ClearEditText) view.findViewById(R.id.cetSearch);
		lbvBar = (LetterBarView) view.findViewById(R.id.lbvBar);
		
		lvContent.setAdapter(adapter = new AdapterForSortList<SortModel<TblUser>>(getActivity(), R.layout.listview_item_contact_icon, dataSource) {

			@Override
			protected void dataInjected(int index, View view, SortModel<TblUser> data) {
				final TblUser user = data.getData();
				int section = getSectionForPosition(index);
				
				if(index == getPositionForSection(section)) { //字母第一行出现
					ViewHolder.get(view, R.id.txtvCatalog, TextView.class).setVisibility(View.VISIBLE);
					ViewHolder.get(view, R.id.txtvCatalog, TextView.class).setText(String.valueOf(data.getSort()));
				} else ViewHolder.get(view, R.id.txtvCatalog, TextView.class).setVisibility(View.GONE);
				
				OnClickListener onItemClick = new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						getOnSkipClickListenter(UserContactFragment.TO_LISTITEM, user).onAction(view);
					}
				};
				
				String content = I18nUtil.i18nTogether(getActivity(), user.getUserLogname(), user.getUserCnname(), user.getUserEnname());
				ViewHolder.get(view, R.id.txtvContent, TextView.class).setText(content);
				ViewHolder.get(view, R.id.txtvContent, TextView.class).setOnClickListener(onItemClick);
				
				RelativeLayout rlContent = ViewHolder.get(view, R.id.rlContent, RelativeLayout.class);
				if(UtilBoss.ObjUtil.isNotNull(rlContent)) rlContent.setOnClickListener(onItemClick);
				
				ImageView ivIcon = ViewHolder.get(view, R.id.ivIcon, ImageView.class);
				if(UtilBoss.ObjUtil.isNotNull(ivIcon)) CodeBoss.ViewCode.userIcon(ivIcon, user.getUserId());
			}
		});
		lbvBar.setTxtvBigLetter(txtvDialog);
		tblUserOffline = new TblUserOffline(getActivity(), tblUserOfflineHandler.setObj(UserContactFragment.this));
	}
	
	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnAdd.setOnClickListener(onClickListener);
		rlRobot.setOnClickListener(onClickListener);
		rlLabel.setOnClickListener(onClickListener);
		rlQrscan.setOnClickListener(onClickListener);
		lbvBar.setOnTouchLetterListener(new OnTouchLetterListener() {
			
			@Override
			public void onTouch(char letter) {
				int index = adapter.getPositionForSection(letter);
				if(index != -1) lvContent.setSelection(index);
			}
		});
		cetSearch.addTextChangedListener(textWatcher);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tblUserOffline.list();
	}

	private void fillDataSource(List<TblUser> list) { //填充数据
		dataSource.clear();
		if(UtilBoss.IfUtil.isEmpty(list)) return;
		
		for(TblUser tblUser : list) {
			String sort = UtilBoss.StrUtil.substring(CharacterParser.getInstance().spellingStr(tblUser.getUserLogname()).toUpperCase(Locale.getDefault()), 0, 1);
			dataSource.add(new SortModel<TblUser>(sort.matches("[A-Z]") ? sort.charAt(0) : '#', tblUser));
		}
		Collections.sort(dataSource, new ComparatorBoss.SortComparator()); //排序
		adapter.updateListView(dataSource);
	}
	
	private void filterDataSource(String filter) { //筛选
		List<SortModel<TblUser>> datas = new ArrayList<SortModel<TblUser>>();
		
		if(UtilBoss.StrUtil.isEmpty(filter)) datas.addAll(dataSource);
		else {
			datas.clear();
			for(SortModel<TblUser> sortModel : dataSource) {
				String data = sortModel.getData().getUserLogname();
				if(data.indexOf(filter) != -1 || CharacterParser.getInstance().spellingStr(data).startsWith(filter)) datas.add(sortModel);
			}
		}
		Collections.sort(datas, new ComparatorBoss.SortComparator()); //排序
		adapter.updateListView(datas);
	}
	
	private TblUserOffline tblUserOffline;
	private List<SortModel<TblUser>> dataSource;
	private AdapterForSortList<SortModel<TblUser>> adapter;
	
	private TextView txtvDialog;
	private Button btnAdd;
	private ListView lvContent;
	private RelativeLayout rlRobot;
	private RelativeLayout rlLabel;
	private RelativeLayout rlQrscan;
	private ClearEditText cetSearch;
	private LetterBarView lbvBar;
	
	private TextWatcher textWatcher = new TextChangeAdapter() {

		@Override
		public void onTextChanged(CharSequence str, int start, int count, int after) {
			filterDataSource(str.toString());
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.rlRobot: getOnSkipClickListenter(UserContactFragment.TO_ROBOT).onAction(view); break;
				case R.id.rlLabel: getOnSkipClickListenter(UserContactFragment.TO_LABEL).onAction(view); break;
				case R.id.rlQrscan: getOnSkipClickListenter(UserContactFragment.TO_QRSCAN).onAction(view); break;
				case R.id.btnAdd: getOnSkipClickListenter(UserContactFragment.TO_ADD).onAction(view); break;
			}
		}
	};
	
	private static ContextHandler tblUserOfflineHandler = new ContextHandler() { //用户线上主线程
		
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case TblUserOffline.LIST:
					List<TblUser> list = (List<TblUser>) msg.obj;
					((UserContactFragment) getObj()).fillDataSource(list);
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	public static final int TO_LISTITEM = 0x120201;
	public static final int TO_ADD = 0x120202;
	public static final int TO_ROBOT = 0x120203;
	public static final int TO_LABEL = 0x120204;
	public static final int TO_QRSCAN = 0x120205;
	
	{
		this.dataSource = new ArrayList<SortModel<TblUser>>();
	}
}