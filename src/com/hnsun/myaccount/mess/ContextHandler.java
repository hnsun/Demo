package com.hnsun.myaccount.mess;

import android.content.Context;
import android.os.Handler;

/**
 * 使其可使用上下文及附带信息
 * @author hnsun
 * @date 2016/09/17
 */
public class ContextHandler extends Handler {

	public ContextHandler() {}

	public ContextHandler(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}

	public ContextHandler setContext(Context context) {
		this.context = context;
		return this;
	}

	public Object getObj() {
		return obj;
	}

	public ContextHandler setObj(Object obj) {
		this.obj = obj;
		return this;
	}

	private Context context;
	private Object obj;
}
