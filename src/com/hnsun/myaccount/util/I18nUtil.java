package com.hnsun.myaccount.util;

import java.util.Locale;

import android.content.Context;

/**
 * 国际化帮助类
 * @author hnsun
 * @date 2016/08/15
 */
public class I18nUtil {

	private I18nUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

	/**
	 * 获取相对应系统语言内容
	 * @param context
	 * @param defaultStr
	 * @param strs
	 * @return
	 */
	public static String i18n(Context context, String defaultStr, String... strs) { //zh、en
		String ret = null;
		UtilBoss.ConditionUtil.nt(context, defaultStr);
		
		int language = I18nUtil.language(context);
		ret = from(language, defaultStr, strs);
		return ret;
	}
	
	/**
	 * 将各种语言合并获取
	 * @param context
	 * @param defaultStr
	 * @param strs
	 * @return
	 */
	public static String i18nTogether(Context context, String defaultStr, String... strs) {
		String ret = null;
		UtilBoss.ConditionUtil.nt(context, defaultStr);
		
		int language = I18nUtil.language(context);
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < strs.length; i++) {
			if(i == language) continue;
			else {
				if(!UtilBoss.StrUtil.isEmpty(strs[i])) builder.append("," + strs[i]);
			}
		}
		ret = from(language, defaultStr, strs) + (UtilBoss.StrUtil.isEmpty(builder.toString()) ? "" : "(" + UtilBoss.StrUtil.substring(builder.toString(), 1) + ")");
		
		return ret;
	}
	
	/**
	 * 当前语言
	 * @param context
	 * @return
	 */
	public static int language(Context context) {
		int ret = -1;
		UtilBoss.ConditionUtil.nt(context);

		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		ret = I18nUtil.ZH;
		if(language.endsWith("zh")) { //汉语
			ret = I18nUtil.ZH;
		} else if(language.endsWith("en")) { //英语
			ret = I18nUtil.EN;
		}
		return ret;
	}

	/**
	 * 获取相对应语言内容
	 * @param language
	 * @param defaultStr
	 * @param strs
	 * @return
	 */
	public static String from(int language, String defaultStr, String... strs) {
		String ret = null;
		UtilBoss.ConditionUtil.nt(defaultStr);
		
		ret = defaultStr;
		if(!UtilBoss.IfUtil.isEmpty(strs)) {
			switch(language) {
				case I18nUtil.ZH: if(strs.length > 0) ret = strs[0]; break; //汉语
				case I18nUtil.EN: if(strs.length > 1) ret = strs[1]; break; //英语
			}
		}
		return ret;
	}
	
	public static final int ZH = 0;
	public static final int EN = 1;
	
	/********************************************
	 * 标志引用
	 ********************************************/
}
