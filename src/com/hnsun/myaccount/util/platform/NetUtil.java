package com.hnsun.myaccount.util.platform;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 网络信息相关（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class NetUtil {

    private NetUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
    	boolean ret = false;
		UtilBoss.ConditionUtil.n(context);
    	
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (UtilBoss.ObjUtil.isNotNull(manager)) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (UtilBoss.ObjUtil.isNotNull(info) && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) ret = true;
            }
        }
        
        return ret;
    }

    /**
     * 判断是否是wifi连接
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
    	boolean ret = false;
		UtilBoss.ConditionUtil.n(context);
    	
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (UtilBoss.ObjUtil.isNotNull(manager)) ret = manager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
        
        return ret;
    }

    /**
     * 打开网络设置界面
     * @param activity
     */
    public static void openSetting(Activity activity) {
		UtilBoss.ConditionUtil.n(activity);
		
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 判断有无网络链接
     * @param context
     * @return
     */
    public static boolean checkNetworkInfo(Context context) {
    	boolean ret = false;
		UtilBoss.ConditionUtil.n(context);
    	
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) ret = true;
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) ret = true;
        
        return ret;
    }
}
