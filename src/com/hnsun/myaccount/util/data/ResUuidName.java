package com.hnsun.myaccount.util.data;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 以UUID主键定资源文件名称
 * @author hnsun
 * @date 2016/10/04
 */
public class ResUuidName {

	public ResUuidName(String uuid) { 
		if(UtilBoss.ObjUtil.isNull(uuid)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.uuid = uuid;
	}
	
	public String gPhoto() {
		StringBuilder builder = new StringBuilder();
		builder.append(uuid);
		builder.append(UtilBoss.StrUtil.isEmpty(type) ? ResUuidName.Photo.T_HEAD : type);
		builder.append(UtilBoss.StrUtil.isEmpty(count) ? "001" : count);
		builder.append(UtilBoss.StrUtil.isEmpty(state) ? ResUuidName.Photo.S_ED : state);
		builder.append("." + ApplicationDatas.APP_FORMAT_PICTURE);
		return builder.toString();
	}

	public ResUuidName type(String type) {
		this.type = type;
		return this;
	}

	public ResUuidName count(String count) {
		this.count = count;
		return this;
	}

	public ResUuidName state(String state) {
		this.state = state;
		return this;
	}
	
	private String uuid; //uuid
	private String type; //两位类型
	private String count; //三位序号
	private String state; //两位状态
	
	/**
	 * 图片
	 * @author hnsun
	 * @date 2016/10/04
	 */
	public static class Photo {
		
		public static final String T_HEAD = "01"; //头像
		public static final String T_IMAGE = "02"; //相片

		public static final String S_ED = "01"; //处理完毕
		public static final String S_ING = "02"; //正在处理
	}
}
