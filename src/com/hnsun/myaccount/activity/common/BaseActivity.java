package com.hnsun.myaccount.activity.common;

/**
 * 普通活动界面统一基类
 * @author hnsun
 * @date 2016/08/18
 */
public abstract class BaseActivity extends AbstractActivity {
	
	{ //类初始化时
		setInitActivityListener(new InitActivityListener() {
			
			@Override
			public void beforeInit() {
//				requestWindowFeature(Window.FEATURE_NO_TITLE); //不要标题
			}
		});
	}
}
