package com.hnsun.myaccount.model;

import com.hnsun.myaccount.usb.SortUsb;

/**
 * 排序对象
 * @author hnsun
 * @date 2016/09/24
 */
public class SortModel<T> implements SortUsb {
	
	public SortModel() {}
	
	public SortModel(T data) {
		this.data = data;
	}
	
	public SortModel(char sort, T data) {
		this.sort = sort;
		this.data = data;
	}

	public char getSort() {
		return sort;
	}

	public SortModel<T> setSort(char sort) {
		this.sort = sort;
		return this;
	}

	public T getData() {
		return data;
	}
	
	public SortModel<T> setData(T data) {
		this.data = data;
		return this;
	}
	
	private char sort; //字母
	private T data; //对应数据
	
	{
		this.sort = '#';
	}
}
