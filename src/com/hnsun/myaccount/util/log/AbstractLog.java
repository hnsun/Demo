package com.hnsun.myaccount.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.file.PathUtil;
import com.hnsun.myaccount.util.file.FileUtil;

/**
 * 统筹日志基本实现
 * @author hnsun
 * @date 2016/08/18
 */
public abstract class AbstractLog implements LogUsb {

	public AbstractLog() {}
	
	public AbstractLog(String tag) {
		if(UtilBoss.StrUtil.isEmpty(tag)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		setTag(tag);
	}

	@Override
	public void v(Object obj) {
		if(SystemStatus.logDebug) verbose(obj);
		if(SystemStatus.logSave) verboseSave(obj);
	}
	
	protected abstract void verbose(Object obj);
	
	protected abstract void verboseSave(Object obj);

	@Override
	public void d(Object obj) {
		if(SystemStatus.logDebug) debug(obj);
		if(SystemStatus.logSave) debugSave(obj);
	}
	
	protected abstract void debug(Object obj);
	
	protected abstract void debugSave(Object obj);

	@Override
	public void i(Object obj) {
		if(SystemStatus.logDebug) info(obj);
		if(SystemStatus.logSave) infoSave(obj);
	}
	
	protected abstract void info(Object obj);
	
	protected abstract void infoSave(Object obj);

	@Override
	public void w(Object obj) {
		if(SystemStatus.logDebug) warn(obj);
		if(SystemStatus.logSave) warnSave(obj);
	}
	
	protected abstract void warn(Object obj);
	
	protected abstract void warnSave(Object obj);

	@Override
	public void e(Object obj) {
		if(SystemStatus.logDebug) error(obj);
		if(SystemStatus.logSave) errorSave(obj);
	}

	@Override
	public void e(Exception e, String desc) {
		setDesc(desc);
		e(e);
	}
	
	protected abstract void error(Object obj);
	
	protected abstract void errorSave(Object obj);

	protected String getFunctionName() { //获得当前线程信息
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(UtilBoss.ObjUtil.isNull(elements)) return null;
		
		stackTrace : for(StackTraceElement element : elements) {
			if(element.isNativeMethod()) continue stackTrace; //自身方法
			if(element.getClassName().equals(Thread.class.getName())) continue stackTrace; //线程本身
			if(element.getClassName().equals(this.getClass().getName())) continue stackTrace; //当前类
			
			Class<?> clazz = this.getClass().getSuperclass();
			while(!java.lang.Object.class.getName().equals(clazz.getName())) { //该类所有父类直到该抽象类
				if(element.getClassName().equals(clazz.getName())) continue stackTrace;
				clazz = clazz.getSuperclass();
			}
			
			return "[" + Thread.currentThread().getName() + ": "
			+ element.getFileName() + ":" + element.getLineNumber() + " " + element.getMethodName() + "]";
		}
		
		return null;
	}

	protected void savePoint(String msg) { //整个文件路径及名字并记录当前点信息
		StringBuilder builder = new StringBuilder(PathUtil.SystemPath.get(PathUtil.SystemPath.LOG));
		
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
		
		format.applyPattern("yyyy");
		builder.append(PathUtil.separatorBefore(format.format(date)));
		format.applyPattern("MM");
		builder.append(PathUtil.separatorBefore(format.format(date)));
		format.applyPattern("dd");
		builder.append(PathUtil.separatorBefore(format.format(date)) + ".txt");
		
		point(builder.toString(), msg);
	}
	
	private void point(String fileName, String msg) { //当前点文件内容写入文件
		UtilBoss.ConditionUtil.n(fileName, msg);
		FileUtil.mkDipPath(fileName);
		File file = new File(fileName);
		BufferedWriter out = null;
		
		try {
			String time = UtilBoss.DatetimeUtil.formatDate2Str(new Date(), ConstantsUtil.FORMAT_DATE_DATETIME_LOG);
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(time + " " + getTag() + " " + UtilBoss.StrUtil.null2Empty(getDesc()) + ConstantsUtil.CODE_LINE_FEED + UtilBoss.StrUtil.notNeedLine(msg));
//			out.write(time + " " + getTag() + " " + UtilBoss.StrUtil.null2Empty(getDesc()) + ConstantsUtil.CODE_LINE_FEED + msg + ConstantsUtil.CODE_LINE_FEED);
		} catch (IOException e) {
			LogFactory.log().e(e, "输入输出有问题");
		} finally {
			try {
				setDesc(null); //重置
				if(UtilBoss.ObjUtil.isNotNull(out)) out.close();
				out = null;
			} catch (IOException e) {
				LogFactory.log().e(e, "输入输出有问题");
			}
		}
	}

	@Override
	public void setTag(String tag) { //设置新名称
		UtilBoss.ConditionUtil.n(tag);
		if(UtilBoss.ObjUtil.isNull(getTag()) || !getTag().equals(tag)) this.tag = tag;
	}

	@Override
	public String getTag() { //获取新名称
		return tag;
	}
	
	protected String getDesc() { //获取相关描述
		return desc;
	}
	
	protected void setDesc(String desc) { //设置相关描述
		this.desc = desc;
	}
	
	private String tag;
	private String desc;
	
	{
		this.tag = SystemStatus.logDefaultTag; //默认为应用名称
	}
}
