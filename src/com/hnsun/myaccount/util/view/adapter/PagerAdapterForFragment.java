package com.hnsun.myaccount.util.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 视图数据绑定
 * @author hnsun
 * @date 2016/09/24
 */
public class PagerAdapterForFragment extends FragmentStatePagerAdapter {
    
    public PagerAdapterForFragment(FragmentManager manager, SparseArray<Fragment> dataSource) {
    	super(manager);
        this.dataSource = dataSource;
    }

	@Override
	public int getCount() {
		int ret = 0;
		ret = UtilBoss.IfUtil.isEmpty(dataSource) ? 0 : dataSource.size();
		return ret;
	}

	@Override
	public Fragment getItem(int index) {
		return dataSource.get(index);
	}

    private SparseArray<Fragment> dataSource;
}
