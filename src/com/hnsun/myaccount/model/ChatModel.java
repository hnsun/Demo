package com.hnsun.myaccount.model;

import java.util.Date;

/**
 * 交流使用的存储对象
 * @author hnsun
 * @date 2016/09/22
 */
public class ChatModel {
	
	public ChatModel() {}
	
	public ChatModel(Type type, String msg) {
		this.type = type;
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	private String msg; //信息内容
	private Date datetime; //传送时间
	private Type type; //发送还是接受
	
	{
		this.datetime = new Date();
	}
	
	/**
	 * LEFT:接收 RIGHT:发送
	 * @author hnsun
	 * @date 2016/09/22
	 */
	public enum Type {
		LEFT, RIGHT
	}
	
	/**
	 * 机器人返回结果对象
	 * @author hnsun
	 * @date 2016/09/23
	 */
	public static class RobotResult {
		
		public int getCode() {
			return code;
		}
		
		public void setCode(int code) {
			this.code = code;
		}
		
		public String getText() {
			return text;
		}
		
		public void setText(String text) {
			this.text = text;
		}
		
		public String getUrl() {
			return url;
		}
		
		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getList() {
			return list;
		}
		
		public void setList(String list) {
			this.list = list;
		}
		
		private int code;
		private String text;
		private String url;
		private String list;
	}
}
