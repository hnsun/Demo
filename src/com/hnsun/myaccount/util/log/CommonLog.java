package com.hnsun.myaccount.util.log;

import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * 普通的日志
 * @author hnsun
 * @date 2016/08/15
 */
public class CommonLog extends AbstractLog {

	@Override
	protected void verbose(Object obj) {
		info(obj);
	}

	@Override
	protected void verboseSave(Object obj) {
		infoSave(obj);
	}

	@Override
	protected void debug(Object obj) {
		info(obj);
	}

	@Override
	protected void debugSave(Object obj) {
		infoSave(obj);
	}
	
	@Override
	protected void info(Object obj) {
		System.out.println(UtilBoss.StrUtil.null2Empty(obj));
	}

	@Override
	protected void infoSave(Object obj) {
		StringBuilder builder = new StringBuilder();
		String str = getFunctionName();
		if(UtilBoss.ObjUtil.isNotNull(str)) builder.append(str + " - ");
		builder.append(UtilBoss.StrUtil.null2Empty(obj));
		savePoint(builder.toString());
	}

	@Override
	protected void warn(Object obj) {
		info(obj);
	}

	@Override
	protected void warnSave(Object obj) {
		infoSave(obj);
	}

	@Override
	protected void error(Object obj) {
		if(obj instanceof Exception) {
			UtilBoss.ConditionUtil.n(obj);
			
			Exception e = (Exception) obj;
			e.printStackTrace();
		} else info(obj);
	}

	@Override
	protected void errorSave(Object obj) {
		if(obj instanceof Exception) {
			UtilBoss.ConditionUtil.n(obj);
			
			Exception e = (Exception) obj;
			StringBuilder builder = new StringBuilder();
			String str = getFunctionName(); //位置信息
			StackTraceElement[] elements = e.getStackTrace(); //异常信息
			
			if(UtilBoss.ObjUtil.isNotNull(str)) builder.append(str + " - ");
			builder.append(e + ConstantsUtil.CODE_LINE_FEED);
			
			if(!UtilBoss.IfUtil.isEmpty(elements)) {
				for (StackTraceElement element : elements) {
					if(UtilBoss.ObjUtil.isNotNull(element) && UtilBoss.ObjUtil.isNotNull(element.getFileName())) 
						builder.append("[" + element.getFileName() + ":" + element.getLineNumber() + "]" + ConstantsUtil.CODE_LINE_FEED);
				}
			}
			
			savePoint(builder.toString());
		} else infoSave(obj);
	}
}
