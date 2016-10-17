package com.hnsun.myaccount.util.view.adapter;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.widget.SectionIndexer;

import com.hnsun.myaccount.usb.SortUsb;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * 对于排序List数据的适配接合
 * @author hnsun
 * @date 2016/09/24
 */
public abstract class AdapterForSortList<E extends SortUsb> extends AbstractAdapterFor<E> implements SectionIndexer {
	
	public AdapterForSortList(Context context, int resLayout, List<E> dataSource) {
		super(context, dataSource);
		if(resLayout == 0) UtilBoss.ExceptionUtil.throwIllegalArgumentInit();
		this.resLayout = resLayout;
	}
	
	@Override
	public int getPositionForSection(int section) { //根据排序字获取位置
		for(int i = 0; i < getCount(); i++) { //遍历找到第一个
			char sort = String.valueOf(getDataSource().get(i).getSort()).toUpperCase(Locale.getDefault()).charAt(0);
			if(section == sort) return i;
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int index) { //根据位置获取排序字
		return getDataSource().get(index).getSort();
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	protected View forView(int index, View view, E data) {
		if(UtilBoss.ObjUtil.isNull(view)) view = getLayoutInflater().inflate(resLayout, null);
		
		dataInjected(index, view, data);
		return view;
	}

	protected abstract void dataInjected(int index, View view, E data); //必须实现的数据内容填充

	private int resLayout;
}
