package com.hnsun.myaccount.util.log;

/**
 * 日志器统一接口
 * @author hnsun
 * @date 2016/08/15
 */
public interface LogUsb {
	
	public void setTag(String tag);
	
	public String getTag();
	
	public void v(Object obj); //verbose 黑色
	
	public void d(Object obj); //debug 蓝色

	public void i(Object obj); //info 绿色
	
	public void w(Object obj); //warn 橙色
	
	public void e(Object obj); //error 红色
	
	public void e(Exception e, String desc); //error desc
}
