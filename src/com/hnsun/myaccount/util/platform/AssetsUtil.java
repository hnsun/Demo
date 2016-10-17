package com.hnsun.myaccount.util.platform;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Typeface;

import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.file.PathUtil;

/**
 * 自带资源相关
 * @author hnsun
 * @date 2016/08/23
 */
public class AssetsUtil {

    private AssetsUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /**
     * 获得字体（访问不许皮肤化的字体）
     * @param context
     * @param name
     * @return
     */
    public static Typeface ttf(Context context, String name) {
    	UtilBoss.ConditionUtil.nt(context, name);
    	return Typeface.createFromAsset(context.getAssets(), AssetsUtil.TTF + PathUtil.separatorBefore(name + "." + AssetsUtil.TTF));
    }
    
    /**
     * 获得皮肤（含有资源及可皮肤化的字体）
     * @param context
     * @param name
     * @return
     * @throws IOException
     */
    public static InputStream skin(Context context, String name) throws IOException {
    	UtilBoss.ConditionUtil.nt(context, name);
    	return context.getAssets().open(AssetsUtil.SKIN + PathUtil.separatorBefore(name + "." + AssetsUtil.SKIN));
    }
    
    /**
     * 获得输入流
     * @param context
     * @param type
     * @param name
     * @return
     * @throws IOException
     */
    public static InputStream inputStream(Context context, String type, String name) throws IOException {
    	UtilBoss.ConditionUtil.nt(context, type, name);
    	return context.getAssets().open(type + PathUtil.separatorBefore(name + "." + type));
    }

	/********************************************
	 * 内置的assets各文件名
	 ********************************************/
    public static final String TTF = "ttf"; //访问不许皮肤化的字体
    public static final String SKIN = "skin"; //含有资源及可皮肤化的字体
    
    public static final String TTF_BRAND = "brand";
    public static final String TTF_UMINE = "umine";

    public static final String SKIN_UMINE = "umine";
}
