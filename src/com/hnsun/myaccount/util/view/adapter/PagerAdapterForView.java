package com.hnsun.myaccount.util.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 视图数据绑定
 * @author hnsun
 * @date 2016/09/24
 */
public class PagerAdapterForView extends PagerAdapter {
	
    public PagerAdapterForView() {
        this.dataSource = new SparseArray<View>();
    }
    
    public PagerAdapterForView(SparseArray<View> dataSource) {
        this.dataSource = dataSource;
    }

	@Override
	public int getCount() {
		int ret = 0;
		if(again) ret = Integer.MAX_VALUE; 
		else ret = UtilBoss.IfUtil.isEmpty(dataSource) ? 0 : dataSource.size();
		return ret;
	}

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int index) {
    	Object ret = null;
    	if(again) {
            container.addView(dataSource.get(index % dataSource.size()), 0);
            ret = dataSource.get(index % dataSource.size());
    	} else {
            container.addView(dataSource.get(index), 0);
            ret = dataSource.get(index);
    	}
        return ret;
    }

    @Override
    public void destroyItem(ViewGroup container, int index, Object obj) {
    	if(again) {
    		if(dataSource.size() > 3) container.removeView(dataSource.get(index % dataSource.size()));
    	} else {
    		if(index < dataSource.size()) container.removeView(dataSource.get(index));
    	}
    }

    @Override
    public int getItemPosition(Object obj) { //viewpager会以为没有检测到item的存在从而刷新
        return POSITION_NONE;
    }
    
    public PagerAdapterForView setAgain(boolean again) {
    	this.again = again;
    	return this;
    }

    public void addItem(View view) {
        if(UtilBoss.ObjUtil.isNotNull(dataSource)) dataSource.put(getCount(), view);
        this.notifyDataSetChanged();
    }

    public void removeItem(int index) { //-1为最后即默认 否者为具体某个
        if(!UtilBoss.IfUtil.isEmpty(dataSource)) dataSource.removeAt(index != -1 ? index : dataSource.size() - 1);
        this.notifyDataSetChanged();
    }

    private boolean again;
    private SparseArray<View> dataSource;
    
    {
    	this.again = false;
    }
}
