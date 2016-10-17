package com.hnsun.myaccount.util.platform;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.file.PathUtil;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 应用信息相关（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class AppUtil {

    private AppUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /**
     * 获取应用程序名称
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
    	String ret = null;
		UtilBoss.ConditionUtil.n(context);
    	
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            ret = context.getResources().getString(packageInfo.applicationInfo.labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            LogFactory.log().e(e);
        }
        
        return ret;
    }

    /**
     * 获取应用程序版本名称信息
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
    	String ret = null;
		UtilBoss.ConditionUtil.n(context);
    	
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            ret = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogFactory.log().e(e);
        }
        
        return ret;
    }

    /**
     * 获取应用程序版本
     * @param context
     * @return 
     */
    public static int getVersionCode(Context context) { //Build.VERSION.SDK
    	int ret = 0;
		UtilBoss.ConditionUtil.n(context);
    	
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            ret = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogFactory.log().e(e);
        }
        
        return ret;
    }

    /**
     * 获取应用程序SDK版本
     * @return 
     */
    public static int getVersionSDK() { //Build.VERSION.SDK
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获得程序内部的储存路径
     * @param context
     * @return
     */
    public static String getInnerPath(Context context) {
		UtilBoss.ConditionUtil.n(context);
        return PathUtil.separatorBefore("data") + PathUtil.separatorBefore("data") + PathUtil.separatorBefore(context.getPackageName());
//      return UtilBoss.StrUtil.substring(context.getFilesDir().getPath(), 0, context.getFilesDir().getPath().lastIndexOf("/")); //activity
    }

    /**
     * 根据名称获得程序内部的储存路径
     * @param context
     * @param name
     * @return
     */
    public static String getInnerPath(Context context, String name) {
		UtilBoss.ConditionUtil.n(context);
        return AppUtil.getInnerPath(context) + PathUtil.separatorBefore(name);
    }

    public static final String INNER_DIR_SKIN = "skins";
    public static final String INNER_DIR_DATABASE = "databases";
}
