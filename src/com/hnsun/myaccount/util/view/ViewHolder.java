package com.hnsun.myaccount.util.view;

import com.hnsun.myaccount.util.UtilBoss;

import android.util.SparseArray;
import android.view.View;

/**
 * 视图持有器（两种方式）
 * @author hnsun
 * @date 2016/09/07
 */
public class ViewHolder {

	private ViewHolder(View view) {
		if(UtilBoss.ObjUtil.isNull(view)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.view = view;
		if(UtilBoss.ObjUtil.isNull(view.getTag())) {
			this.viewHolder = new SparseArray<View>();
			this.view.setTag(this.viewHolder);
		}
	}
	
	public static ViewHolder getHolder(View view) {
		return new ViewHolder(view);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends View> T get(int id, Class<T> clazz) {
		View child = viewHolder.get(id);
		if(UtilBoss.ObjUtil.isNull(child)) {
			child = view.findViewById(id);
			viewHolder.put(id, child);
		}
		return (T) child;
	}
	
	/**
	 * 获得复用子View
	 * @param view
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id, Class<T> clazz) {
		UtilBoss.ConditionUtil.n(view);
		
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if(UtilBoss.ObjUtil.isNull(viewHolder)) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		
		View child = viewHolder.get(id);
		if(UtilBoss.ObjUtil.isNull(child)) {
			child = view.findViewById(id);
			viewHolder.put(id, child);
		}
		return (T) child;
	}
	
	private View view;
	private SparseArray<View> viewHolder;
}
