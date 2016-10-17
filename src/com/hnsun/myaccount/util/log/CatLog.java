package com.hnsun.myaccount.util.log;

import android.util.Log;

import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * LogCat输出日志
 * @author hnsun
 * @date 2016/08/18
 */
public class CatLog extends AbstractLog {
	
	@Override
	protected void verbose(Object obj) {
        if (SystemStatus.logLevel <= Log.VERBOSE) {
            String name = getFunctionName();
            String str = UtilBoss.StrUtil.null2Empty(obj);
            Log.v(getTag(), (UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str)));
        }
	}

	@Override
	protected void verboseSave(Object obj) {
        if (SystemStatus.logLevel <= Log.VERBOSE) {
	        String name = getFunctionName();
	        String str = UtilBoss.StrUtil.null2Empty(obj);
	        savePoint(UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str));
        }
	}

	@Override
	protected void debug(Object obj) {
        if (SystemStatus.logLevel <= Log.DEBUG) {
            String name = getFunctionName();
            String str = UtilBoss.StrUtil.null2Empty(obj);
            Log.d(getTag(), (UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str)));
        }
	}

	@Override
	protected void debugSave(Object obj) {
        if (SystemStatus.logLevel <= Log.DEBUG) {
	        String name = getFunctionName();
	        String str = UtilBoss.StrUtil.null2Empty(obj);
	        savePoint(UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str));
        }
	}

	@Override
	protected void info(Object obj) {
        if (SystemStatus.logLevel <= Log.INFO) {
            String name = getFunctionName();
            String str = UtilBoss.StrUtil.null2Empty(obj);
            Log.i(getTag(), (UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str)));
        }
	}

	@Override
	protected void infoSave(Object obj) {
        if (SystemStatus.logLevel <= Log.INFO) {
            String name = getFunctionName();
            String str = UtilBoss.StrUtil.null2Empty(obj);
            savePoint(UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str));
        }
	}

	@Override
	protected void warn(Object obj) {
        if (SystemStatus.logLevel <= Log.WARN) {
            String name = getFunctionName();
            String str = UtilBoss.StrUtil.null2Empty(obj);
            Log.w(getTag(), (UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str)));
        }
	}

	@Override
	protected void warnSave(Object obj) {
        if (SystemStatus.logLevel <= Log.WARN) {
	        String name = getFunctionName();
	        String str = UtilBoss.StrUtil.null2Empty(obj);
	        savePoint(UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str));
        }
	}

	@Override
	protected void error(Object obj) {
        if (SystemStatus.logLevel <= Log.ERROR) {
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

                Log.e(getTag(), builder.toString());
    		} else {
                String name = getFunctionName();
                String str = UtilBoss.StrUtil.null2Empty(obj);
                Log.e(getTag(), (UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str)));
    		}
        }
	}

	@Override
	protected void errorSave(Object obj) {
        if (SystemStatus.logLevel <= Log.ERROR) {
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
			} else {
	            String name = getFunctionName();
	            String str = UtilBoss.StrUtil.null2Empty(obj);
	            savePoint(UtilBoss.StrUtil.isEmpty(name) ? str : (name + " - " + str));
			}
		}
	}
}
