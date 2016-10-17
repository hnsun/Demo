package com.hnsun.myaccount.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.mess.ContextHandler;
import com.hnsun.myaccount.model.DownloadModel;
import com.hnsun.myaccount.model.dbo.PlfDownloadthread;
import com.hnsun.myaccount.model.dbo.TblFile;
import com.hnsun.myaccount.support.offline.PlfDownloadthreadOffline;
import com.hnsun.myaccount.support.offline.TblFileOffline;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.file.Downloader;
import com.hnsun.myaccount.util.platform.ResUtil;
import com.hnsun.myaccount.util.view.ViewHolder;
import com.hnsun.myaccount.util.view.adapter.AdapterForList;
import com.hnsun.myaccount.view.RefreshableLinearLayout;
import com.hnsun.myaccount.view.RefreshableLinearLayout.OnTouchListViewListener;
import com.hnsun.myaccount.view.RefreshableLinearLayout.PullToRefreshListener;

/**
 * 下载界面
 * @author hnsun
 * @date 2016/10/05
 */
public class DownloadActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_download);
		getActionBar().setTitle(R.string.btn_download);
		getActionBar().setLogo(R.drawable.ic_download);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		lvContent = (ListView) findViewById(R.id.lvContent);
		rllContent = (RefreshableLinearLayout) findViewById(R.id.rllContent);
		
		setExit(false);
		lvContent.setAdapter(adapter = new AdapterForList<DownloadModel>(instance, R.layout.listview_item_download, dataSource) {

			@Override
			protected void dataInjected(int index, View view, DownloadModel data) {
				String[] str = data.getName().split("\\.");
				ViewHolder.get(view, R.id.txtvName, TextView.class).setText(str[0]);
				CodeBoss.ViewCode.imgFromMine(ViewHolder.get(view, R.id.ivIcon, ImageView.class), str[1]);
				
				final TextView txtvState = ViewHolder.get(view, R.id.txtvState, TextView.class);
				final LinearLayout llContent = ViewHolder.get(view, R.id.llContent, LinearLayout.class);
				final DownloadModel model = data;
				
				ViewHolder.get(view, R.id.txtvSize, TextView.class).setText(String.format("%.2f", model.getSize() * 1.0 / 1024 / 1024) + "M");
				ViewHolder.get(view, R.id.txtvProgress, TextView.class).setText(UtilBoss.StrUtil.getByObj(model.getCompletedSize() * 100 / model.getSize()) + "%");
				ProgressBar pbIng = ViewHolder.get(view, R.id.pbIng, ProgressBar.class);
				pbIng.setMax(model.getSize());
				pbIng.setProgress(model.getCompletedSize());
				txtvState.setText(ResUtil.getText(instance, R.string.txt_download_paused));
				llContent.setVisibility(View.GONE);
				
				OnClickListener onItemClickListener = new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						switch(view.getId()) {
							case R.id.rlContent: 
								if(llContent.getVisibility() == View.GONE) {
									if(!UtilBoss.StrUtil.isEmpty(expandUrl)) ViewHolder.get(views.get(expandUrl), R.id.llContent, LinearLayout.class).setVisibility(View.GONE);
									llContent.setVisibility(View.VISIBLE);
									expandUrl = model.getUrl();
								} else {
									llContent.setVisibility(View.GONE);
									expandUrl = null;
								}
								break;
							case R.id.btnState: 
								Button btnState = ((Button) view);
								Downloader downloader = downloaders.get(model.getUrl());
								if(downloader.state(Downloader.STATE_ING)) {
									btnState.setText(ResUtil.getText(instance, R.string.btn_download));
									txtvState.setText(ResUtil.getText(instance, R.string.txt_download_paused));
									downloader.pause();
									ableRefresh();
								} else {
									btnState.setText(ResUtil.getText(instance, R.string.btn_pause));
									txtvState.setText(ResUtil.getText(instance, R.string.txt_download_ing));
									downloader.download();
									rllContent.setAble(false);
								}
								llContent.setVisibility(View.GONE);
								break;
							case R.id.btnDelete: remove(model); break;
						}
					}
				};
				ViewHolder.get(view, R.id.rlContent, RelativeLayout.class).setOnClickListener(onItemClickListener);
				ViewHolder.get(view, R.id.btnDelete, Button.class).setOnClickListener(onItemClickListener);
				ViewHolder.get(view, R.id.btnState, Button.class).setOnClickListener(onItemClickListener);

				if(model.getCompletedSize() == model.getSize()) {
					ViewHolder.get(view, R.id.txtvState, TextView.class).setText(ResUtil.getText(instance, R.string.txt_download_ed));
					ViewHolder.get(view, R.id.btnState, Button.class).setVisibility(View.GONE);
				} else ViewHolder.get(view, R.id.btnState, Button.class).setVisibility(View.VISIBLE);
				views.put(model.getUrl(), view);
			}
		});
		plfDownloadthreadOffline = new PlfDownloadthreadOffline(instance, plfDownloadthreadOfflineHandler.setContext(instance));
		plfDownloadthreadOffline.listDistinctUrl();
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		rllContent.setOnRefreshListener(new PullToRefreshListener() {
			
			@Override
			public void onRefresh() {
				UtilBoss.ThreadUtil.sleepBy(1 * 1000);
				reset();
				plfDownloadthreadOffline.listDistinctUrl();
			}
		}, 0);
		rllContent.setOnTouchListViewListener(new OnTouchListViewListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent event) { //将展示中的隐藏
				if(!UtilBoss.StrUtil.isEmpty(expandUrl)) {
					ViewHolder.get(views.get(expandUrl), R.id.llContent, LinearLayout.class).setVisibility(View.GONE);
					expandUrl = null;
				}
				ableRefresh();
				return false;
			}
		});
	}

	@Override
    protected void onDestroy() {
        super.onDestroy();
        downloaders = null;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case android.R.id.home: ((DownloadActivity) instance).finish(); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}
	
	private void ableRefresh() {
		boolean isDownloading = false;
		for(Map.Entry<String, Downloader> entry : downloaders.entrySet()) {
			Downloader downloader = entry.getValue();
			if(downloader.state(Downloader.STATE_ING)) {
				isDownloading = true;
				break;
			}
		}
		rllContent.setAble(!isDownloading);
	}

	private void add(TblFile file) {
		UtilBoss.ThreadUtil.openThread(new ArgumentCallback() {
			
			@Override
			public void action(Object... objs) {
				Downloader downloader = downloaders.get(UtilBoss.StrUtil.getByObj(objs[1]));
				if(UtilBoss.ObjUtil.isNull(downloader)) {
					downloader = new Downloader(instance, downloadHandler.setContext(instance), UtilBoss.StrUtil.getByObj(objs[0]), UtilBoss.StrUtil.getByObj(objs[1]));
					downloaders.put(UtilBoss.StrUtil.getByObj(objs[1]), downloader);
				}
				downloader.fillDownloadInfo();
				CodeBoss.MessCode.handlerMsg(handler.setContext(instance), 0, downloader.getModel());
			}
		}, file.getFileName(), file.getFileUrl());
	}
	
	private void remove(DownloadModel model) {
		Downloader downloader = downloaders.get(model.getUrl());
		if(UtilBoss.ObjUtil.isNotNull(downloader)) {
			downloader.pause(); //关闭线程
			downloader.deleteFile(); //删除临时文件
			downloaders.remove(model.getUrl());
		}
		
		expandUrl = null;
		views.remove(model.getUrl());
		dataSource.remove(model);
		adapter.updateListView(dataSource);
		PlfDownloadthreadOffline.deleteUrl(instance, model.getUrl());
	}
	
	private void reset() {
		views.clear();
		downloaders.clear();
		dataSource.clear();
	}
	
	private Context instance;
	private String expandUrl; //展开的行
	private Map<String, View> views; //每个视图
	private Map<String, Downloader> downloaders; //各自下载器
	private List<DownloadModel> dataSource;
	private AdapterForList<DownloadModel> adapter;
	private PlfDownloadthreadOffline plfDownloadthreadOffline;
	
	private ListView lvContent;
	private RefreshableLinearLayout rllContent;
	
	private static ContextHandler handler = new ContextHandler() {
		
		@Override
		public void handleMessage(Message msg) {
			if(UtilBoss.ObjUtil.isNotNull(msg.obj)) {
				DownloadModel model = (DownloadModel) msg.obj;
				if(!((DownloadActivity) getContext()).dataSource.contains(model)) {
					((DownloadActivity) getContext()).dataSource.add(model);
					((DownloadActivity) getContext()).adapter.updateListView(((DownloadActivity) getContext()).dataSource);
				} 
			}
		}
	};
	
	private static ContextHandler downloadHandler = new ContextHandler() {
		
		@Override
		public void handleMessage(Message msg) {
			int increment = msg.what;
			String url = (String) msg.obj;
			
			View view = ((DownloadActivity) getContext()).views.get(url);
			if(UtilBoss.ObjUtil.isNotNull(view)) {
				ProgressBar pbIng = ViewHolder.get(view, R.id.pbIng, ProgressBar.class);

				pbIng.incrementProgressBy(increment);
				ViewHolder.get(view, R.id.txtvProgress, TextView.class).setText(UtilBoss.StrUtil.getByObj(pbIng.getProgress() * 100 / pbIng.getMax()) + "%");
				if(pbIng.getProgress() == pbIng.getMax()) { //下载完成
//					pbIng.setProgress(0); //重置
//					PlfDownloadthreadOffline.deleteUrl(getContext(), url);
//					((DownloadActivity) getContext()).views.remove(url);
					((DownloadActivity) getContext()).downloaders.remove(url);
					ViewHolder.get(view, R.id.txtvState, TextView.class).setText(ResUtil.getText(getContext(), R.string.txt_download_ed));
					ViewHolder.get(view, R.id.btnState, Button.class).setVisibility(View.GONE);
				}
			}
		}
	};
	
	private static ContextHandler plfDownloadthreadOfflineHandler = new ContextHandler() { //下载线程线上主线程
		
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case PlfDownloadthreadOffline.LIST_DISTINCTURL:
					List<PlfDownloadthread> list = (List<PlfDownloadthread>) msg.obj;
					for(PlfDownloadthread plfDownloadthread : list) {
						((DownloadActivity) getContext()).add(TblFileOffline.getFromUrl(getContext(), plfDownloadthread.getDownloadthreadUrl()));
					}
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	{
		this.instance = DownloadActivity.this;
		this.views = new HashMap<String, View>();
		this.downloaders = new HashMap<String, Downloader>();
		this.dataSource = new ArrayList<DownloadModel>();
	}
}
