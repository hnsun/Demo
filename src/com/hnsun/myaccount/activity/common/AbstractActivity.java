package com.hnsun.myaccount.activity.common;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.application.ExitApplication;
import com.hnsun.myaccount.broadcastreceiver.MonitorBroadcastReceiver;
import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.ReflectUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.UtilBoss.ExceptionUtil;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.platform.SkinManager;
import com.hnsun.myaccount.util.platform.ViewUtil;

/**
 * 所有Activity都需实现的抽象类
 * @author hnsun
 * @date 2016/08/18
 * 处理：模块化接口、后退管理、皮肤管理、碎片跳转处理、
 */
public abstract class AbstractActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.instance = AbstractActivity.this;
        ExitApplication.getInstance().addActivity((AbstractActivity) instance);
		
		init(savedInstanceState);
	}

	@Override
	protected void onResume() {
		monitorBroadcastReceiver = new MonitorBroadcastReceiver();
		IntentFilter netStateFilter = new IntentFilter();
//		netStateFilter.addAction(MonitorBroadcastReceiver.ACTION_LOCAL);
		netStateFilter.addAction(MonitorBroadcastReceiver.ACTION_SYSTEM_NETWORK);
		registerReceiver(monitorBroadcastReceiver, netStateFilter);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		unregisterReceiver(monitorBroadcastReceiver);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		ExitApplication.getInstance().removeActivity((AbstractActivity) instance);
		super.onDestroy();
	}

	@Override
	public View onCreateView(View parent, String name, Context context, AttributeSet attrs) { //只加载一次, 对需皮肤化信息储存
		Integer id = attrs.getAttributeResourceValue(ConstantsUtil.NAMESPACE_ANDROID, "id", -1);
		String tag = attrs.getAttributeValue(ConstantsUtil.NAMESPACE_ANDROID, "tag");
		if(UtilBoss.ObjUtil.isNotNull(id) && UtilBoss.ObjUtil.isNotNull(tag) && tag.contains(ApplicationDatas.VIEW_TAG_SKIN)) { //需要皮肤化的视图
			Map<String, String> map = SkinManager.skinAttrs(context, attrs); //当前皮肤化属性集合
			arrSkinViewInfo.put(id, map);
		}
		return super.onCreateView(parent, name, context, attrs);
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if(featureId == Window.FEATURE_ACTION_BAR && UtilBoss.ObjUtil.isNotNull(menu)) { //展示隐藏的菜单显示时带上图标
			if(menu.getClass().getSimpleName().equals("MenuBuilder")) ReflectUtil.toMethodName(menu, "setOptionalIconsVisible", true);
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public void onBackPressed() {
		if(isExit() && !CodeBoss.MessCode.exitApplication()) { //最外层给予选择性退出 否则其他操作
			if(exitClickAgain) {
				ViewUtil.displayToast(instance, R.string.msg_exit);
				exitClickAgain = false;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						UtilBoss.ThreadUtil.sleepBy(2 * 1000, new ArgumentCallback() {
							
							@Override
							public void action(Object... objs) {
								exitClickAgain = true;
							}
						});
					}
				}).start();
				return ;
			} else ExitApplication.getInstance().exit(); //退出
		}
		
		super.onBackPressed();
	}
	
	protected void initContentView(int res) { //皮肤同时设置更新
		setContentView(res);
		SkinManager.initSkin((AbstractActivity) instance, arrSkinViewInfo);
		ViewUtil.changeFonts(findViewById(R.id.layoutWrapper), instance, null);
	}
	
	protected abstract void initView(Bundle savedInstanceState); //界面准备
	
	protected abstract void initData(Bundle savedInstanceState); //数据准备
	
	protected abstract void initAction(Bundle savedInstanceState); //事件准备
	
	private void init(Bundle savedInstanceState) { //对初始化进一步封装统一化
		if(UtilBoss.ObjUtil.isNotNull(getInitActivityListener())) getInitActivityListener().beforeInit();
		
		initView(savedInstanceState);
		initData(savedInstanceState);
		initAction(savedInstanceState);
		
		setActionOverflowAlways();
	}
	
	private void setActionOverflowAlways() { //顶部标题栏省略字段永远显示
		ViewConfiguration config = ViewConfiguration.get(instance);
		ReflectUtil.setAttrValue(config, "sHasPermanentMenuKey", false);
	}
	
	private boolean isExit() { return isExit; } //不提供外部调用

	protected void setExit(boolean isExit) { this.isExit = isExit; } //子类设置

	public OnFragmentClickListener getOnFragmentClickListener() { //随处访问
		return onFragmentClickListener;
	}

	protected void setOnFragmentClickListener(OnFragmentClickListener onFragmentClickListener) { //子类中设置
		this.onFragmentClickListener = onFragmentClickListener;
	}

	private InitActivityListener getInitActivityListener() { return initActivityListener; } //不提供外部调用

	protected void setInitActivityListener(InitActivityListener initActivityListener) { //为空才能设置，要在代码块中设置
		if(UtilBoss.ObjUtil.isNull(getInitActivityListener())) this.initActivityListener = initActivityListener;
		else ExceptionUtil.throwUnsupportedOperation("'setInitActivityListener' cannot be operated. because is not null.");
	}
	
	protected SparseArray<Map<String, String>> getArrSkinViewInfo() {
		return arrSkinViewInfo;
	}

	private Context instance;
	private boolean exitClickAgain; //当下后退是否允许退出还是等待下次
	
	private boolean isExit; //当前的Activity后退是否为退出操作
	private OnFragmentClickListener onFragmentClickListener; //在底层子类需要时进行设置
	private InitActivityListener initActivityListener; //不允许外部引用，旨在全局类中实现
	private MonitorBroadcastReceiver monitorBroadcastReceiver; //全局广播 不知为何执行两次
	private SparseArray<Map<String, String>> arrSkinViewInfo; //皮肤更新对象ID，及其他一些需皮肤更换如字体颜色背景颜色或图片的集合
	
	{
		this.exitClickAgain = true; //默认等待下次点击后退
		this.isExit = true; //默认后退为退出操作
		this.arrSkinViewInfo = new SparseArray<Map<String, String>>(); //皮肤对象
	}
	
	/**
	 * 碎片页面按钮跳转事件
	 * @author hnsun
	 * @date 2016/08/27
	 */
	public interface OnFragmentClickListener {
		
		public void onFragmentClick(View view, int tag, Object... objs);
	}
	
	/**
	 * 初始化时监听事件
	 * @author hnsun
	 * @date 2016/08/18
	 */
	protected interface InitActivityListener {
		
		public void beforeInit();
	}
}
