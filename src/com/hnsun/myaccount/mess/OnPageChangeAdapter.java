package com.hnsun.myaccount.mess;

import android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * 页面改变适配器
 * @author hnsun
 * @date 2016/09/25
 */
public class OnPageChangeAdapter implements OnPageChangeListener {

	@Override
	public void onPageScrollStateChanged(int state) {} //state : 1按下 0松开 2新的有没有开始滑动 或者说是否决定滑动到下家 下家可见 但没到

	@Override
	public void onPageScrolled(int page, float positionOffset, int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int page) {}

}
