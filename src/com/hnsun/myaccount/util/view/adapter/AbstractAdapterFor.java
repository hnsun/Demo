package com.hnsun.myaccount.util.view.adapter;

import java.util.List;

import com.hnsun.myaccount.util.UtilBoss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 适配接合抽象
 * @author hnsun
 * @date 2016/09/22
 */
public abstract class AbstractAdapterFor<E> extends BaseAdapter {
	
	public AbstractAdapterFor(Context context, List<E> dataSource) {
		if(UtilBoss.ObjUtil.isNull(context)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.context = context;
		this.dataSource = dataSource;
		this.layoutInflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return UtilBoss.IfUtil.isEmpty(dataSource) ? 0 : dataSource.size();
	}

	@Override
	public Object getItem(int index) {
		return dataSource.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int index, View view, ViewGroup viewGroup) {
		return forView(index, view, dataSource.get(index));
	}
	
	public void updateListView(List<E> dataSource) {
		this.dataSource = dataSource;
		this.notifyDataSetChanged();
	}
	
	protected List<E> getDataSource() {
		return dataSource;
	}

	protected LayoutInflater getLayoutInflater() {
		return layoutInflater;
	}

	protected abstract View forView(int index, View view, E data); //子类必须实现的视图

	private Context context;
	private List<E> dataSource;
	private LayoutInflater layoutInflater;
}
