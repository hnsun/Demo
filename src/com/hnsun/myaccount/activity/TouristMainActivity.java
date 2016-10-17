package com.hnsun.myaccount.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.mess.OnPageChangeAdapter;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.platform.DensityUtil;
import com.hnsun.myaccount.util.view.adapter.PagerAdapterForView;

/**
 * 游客主界面
 * @author hnsun
 * @date 2016/08/30
 */
public class TouristMainActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_tourist_main);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		btnLoginForword = (Button) findViewById(R.id.btnLoginForword);
		vpBanner = (ViewPager) findViewById(R.id.vpBanner);
		flContent = (FrameLayout) findViewById(R.id.flContent);
		rlService1 = (RelativeLayout) findViewById(R.id.rlService1);
		rlService2 = (RelativeLayout) findViewById(R.id.rlService2);
		rlService3 = (RelativeLayout) findViewById(R.id.rlService3);

		int margin = DensityUtil.dp2px(instance, 10);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(margin, margin, margin, DensityUtil.dp2px(instance, 3));
		layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		llGuide = new LinearLayout(instance);
		llGuide.setOrientation(LinearLayout.HORIZONTAL);
		flContent.addView(llGuide, layoutParams);
		
		dots = CodeBoss.ViewCode.dotsFill(instance, dataSource, llGuide, ApplicationDatas.SLIDE_BANNER);
		vpBanner.setAdapter(new PagerAdapterForView(dataSource).setAgain(true));
		vpBanner.setCurrentItem(dataSource.size() * 100); //可左可右
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		btnLoginForword.setOnClickListener(onClickListener);
		rlService1.setOnClickListener(onClickListener);
		rlService2.setOnClickListener(onClickListener);
		rlService3.setOnClickListener(onClickListener);
		vpBanner.setOnPageChangeListener(onPageChangeListener);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		this.isLoop = true;
		UtilBoss.ThreadUtil.openThread(new ArgumentCallback() {
			
			@Override
			public void action(Object... objs) {
				SystemClock.sleep(ApplicationDatas.SLIDE_INTERVEL);
				while(isLoop) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(isLoop) vpBanner.setCurrentItem(vpBanner.getCurrentItem() + 1);
						}
					});
					SystemClock.sleep(ApplicationDatas.SLIDE_INTERVEL);
				}
			}
		});
	}

	@Override
	public void onStop() {
		super.onStop();
		this.isLoop = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tourist_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case R.id.btnInfo: startActivity(new Intent(instance, InfoActivity.class)); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}
	
	private Context instance;
	private boolean isLoop; //是否循环
	private View[] dots; //引导
	private SparseArray<View> dataSource;

	private Button btnLoginForword;
	private ViewPager vpBanner;
	private FrameLayout flContent;
	private RelativeLayout rlService1;
	private RelativeLayout rlService2;
	private RelativeLayout rlService3;
	private LinearLayout llGuide;
	
	private OnPageChangeListener onPageChangeListener = new OnPageChangeAdapter() {
		
		@Override
		public void onPageSelected(int page) {
			for(int i = 0; i < dots.length; i++) {
				if(i == page % dataSource.size()) dots[i].setBackgroundResource(R.color.defaultTheme);
				else dots[i].setBackgroundResource(R.color.white);
			}
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnLoginForword: startActivity(new Intent(instance, UserEntranceActivity.class)); break;
				case R.id.rlService1: break;
				case R.id.rlService2: break;
				case R.id.rlService3: break;
			}
		}
	};
	
	{
		this.instance = TouristMainActivity.this;
		this.dataSource = new SparseArray<View>();
	}
}
