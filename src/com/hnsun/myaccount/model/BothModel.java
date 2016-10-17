package com.hnsun.myaccount.model;

/**
 * 两者相对应的对象
 * @author hnsun
 * @date 2016/08/15
 */
public class BothModel<T, E> {
	
	public BothModel() {}

	public BothModel(T t, E e) {
		this.t = t;
		this.e = e;
	}

	public T getT() {
		return t;
	}
	
	public BothModel<T, E> setT(T t) {
		this.t = t;
		return this;
	}
	
	public E getE() {
		return e;
	}
	
	public BothModel<T, E> setE(E e) {
		this.e = e;
		return this;
	}
	
	private T t; //对象t
	private E e; //对象e
}
