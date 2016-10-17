package com.hnsun.myaccount.util.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 对于普通List数据的适配接合
 * @author hnsun
 * @date 2016/09/07
 */
public abstract class AdapterForList<E> extends AbstractAdapterFor<E> {
	
	public AdapterForList(Context context, int resLayout, List<E> dataSource) {
		super(context, dataSource);
		if(resLayout == 0) UtilBoss.ExceptionUtil.throwIllegalArgumentInit();
		this.resLayout = resLayout;
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
