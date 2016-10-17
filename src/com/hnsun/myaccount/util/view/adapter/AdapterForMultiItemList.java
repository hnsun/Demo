package com.hnsun.myaccount.util.view.adapter;

import java.util.List;

import android.content.Context;

import com.hnsun.myaccount.usb.MultiItemUsb;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * 对于多样Item的List数据的适配接合
 * @author hnsun
 * @date 2016/09/23
 */
public abstract class AdapterForMultiItemList<E extends MultiItemUsb> extends AbstractAdapterFor<E> {
	
	public AdapterForMultiItemList(Context context, List<E> dataSource, int typeCount) {
		super(context, dataSource);
		if(typeCount == 0) UtilBoss.ExceptionUtil.throwIllegalArgumentInit();
		this.typeCount = typeCount;
	}

	@Override
	public int getItemViewType(int index) {
		return getDataSource().get(index).getItemType();
	}

	@Override
	public int getViewTypeCount() {
		return typeCount;
	}

	private int typeCount;
}
