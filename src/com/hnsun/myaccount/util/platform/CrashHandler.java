package com.hnsun.myaccount.util.platform;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;

import com.hnsun.myaccount.application.ExitApplication;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录错误报告.（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class CrashHandler implements UncaughtExceptionHandler {

	private CrashHandler() {}

	public static CrashHandler getInstance() {
		if(UtilBoss.ObjUtil.isNull(instance)) instance = new CrashHandler();
		return instance;
	}

	/**
	 * 初始化
	 * @param context
	 */
	public void init(Context context) {
		this.context = context;
		this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler(); //获取系统默认的UncaughtException处理器
		Thread.setDefaultUncaughtExceptionHandler(this); //设置该CrashHandler为程序的默认处理器
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 * @param thread
	 * @param ex
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		handleException(ex);
		if (!handleException(ex) && UtilBoss.ObjUtil.isNotNull(defaultHandler)) {
			ExitApplication.getInstance().exit(); //如果用户没有处理则让系统默认的异常处理器来处理
			defaultHandler.uncaughtException(thread, ex);
		} else {
			UtilBoss.ThreadUtil.sleepBy(3 * 1000);
			ExitApplication.getInstance().exit(); //退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (UtilBoss.ObjUtil.isNull(ex)) return false;
		
		new Thread() { //使用Toast来显示异常信息
			
			@Override
			public void run() {
				Looper.prepare();
				ViewUtil.displayToast(context, "很抱歉,程序出现异常,即将退出.");
				Looper.loop();
			}
		}.start();
		collectDeviceInfo(context); //保存日志文件
		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * @param ctx
	 */
	private void collectDeviceInfo(Context ctx) {
		LogFactory.log().i("collectDeviceInfo......");
		LogFactory.log().i("***DeviceInfo***");
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if(UtilBoss.ObjUtil.isNotNull(pi)) {
				String versionName = UtilBoss.ObjUtil.isNull(pi.versionName) ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				LogFactory.log().i("versionName:" + versionName);
				LogFactory.log().i("versionCode:" + versionCode);
			}
		} catch (NameNotFoundException e) {
			LogFactory.log().e(e);
		}
		
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				LogFactory.log().e(field.getName() + ":" + field.get(null).toString());
			} catch (Exception e) {
				LogFactory.log().e(e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * @param ex
	 * @return
	 */
	private String saveCrashInfo2File(Throwable ex) {
		LogFactory.log().i("***CrashExceptionInfo***");
		StringBuffer sb = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		ex.printStackTrace();
		Throwable cause = ex.getCause();
		while(UtilBoss.ObjUtil.isNotNull(cause)) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		LogFactory.log().i("Exception:" + sb.toString());
		return sb.toString();
	}

	private Context context;
	private static CrashHandler instance;
	private UncaughtExceptionHandler defaultHandler; //系统默认的UncaughtException处理类
}
