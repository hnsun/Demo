package com.hnsun.myaccount.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.fragment.common.BaseFragment;
import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.mess.OnPageChangeAdapter;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.platform.DensityUtil;
import com.hnsun.myaccount.util.view.adapter.PagerAdapterForView;

/**
 * 用户主页碎片
 * @author hnsun
 * @date 2016/09/03
 */
public class UserHomeFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_home, container, false);
		return view;
	}
	
	@Override
	protected void initData(View view, Bundle savedInstanceState) {
		vpBanner = (ViewPager) view.findViewById(R.id.vpBanner);
		flContent = (FrameLayout) view.findViewById(R.id.flContent);
		rlService1 = (RelativeLayout) view.findViewById(R.id.rlService1);
		rlService2 = (RelativeLayout) view.findViewById(R.id.rlService2);
		rlService3 = (RelativeLayout) view.findViewById(R.id.rlService3);

		int margin = DensityUtil.dp2px(getActivity(), 10);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(margin, margin, margin, DensityUtil.dp2px(getActivity(), 3));
		layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		llGuide = new LinearLayout(getActivity());
		llGuide.setOrientation(LinearLayout.HORIZONTAL);
		flContent.addView(llGuide, layoutParams);

		dots = CodeBoss.ViewCode.dotsFill(getActivity(), dataSource, llGuide, ApplicationDatas.SLIDE_BANNER);
		vpBanner.setAdapter(new PagerAdapterForView(dataSource).setAgain(true));
		vpBanner.setCurrentItem(dataSource.size() * 100); //可左可右
	}
	
	@Override
	protected void initAction(Bundle savedInstanceState) {
		vpBanner.setOnPageChangeListener(onPageChangeListener);
		rlService1.setOnClickListener(onClickListener);
		rlService2.setOnClickListener(onClickListener);
		rlService3.setOnClickListener(onClickListener);
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
					getActivity().runOnUiThread(new Runnable() {
						
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
	
	private boolean isLoop; //是否循环
	private View[] dots; //引导
	private SparseArray<View> dataSource;

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
				case R.id.rlService1: getOnSkipClickListenter(UserHomeFragment.TO_SERVICE_1).onAction(view); break;
				case R.id.rlService2: getOnSkipClickListenter(UserHomeFragment.TO_SERVICE_2).onAction(view); break;
				case R.id.rlService3: getOnSkipClickListenter(UserHomeFragment.TO_SERVICE_3).onAction(view); break;
			}
		}
	};
	
	public static final int TO_SERVICE_1 = 0x120101;
	public static final int TO_SERVICE_2 = 0x120102;
	public static final int TO_SERVICE_3 = 0x120103;
	
	{
		this.dataSource = new SparseArray<View>();
	}
}