package com.hnsun.myaccount.util.data.json;

import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * Gson包处理Json
 * @author hnsun
 * @date 2016/09/19
 */
public class GsonJson {
	
	public GsonJson() {
		this(ConstantsUtil.FORMAT_DATE_DATETIME);
	}
	
	public GsonJson(String dateFormat) {
		if(UtilBoss.StrUtil.isEmpty(dateFormat)) UtilBoss.ExceptionUtil.throwIllegalArgumentInit();
		builder = new GsonBuilder().setDateFormat(dateFormat).setExclusionStrategies(new ExclusionStrategy() { //不转化字段策略
			
			@Override
			public boolean shouldSkipField(FieldAttributes field) {
				NotSerialAnnotation annotation = field.getAnnotation(NotSerialAnnotation.class);
				if(UtilBoss.ObjUtil.isNotNull(annotation)) return true; //true不转化
				return false;
			}
			
			@Override
			public boolean shouldSkipClass(Class<?> clazz) { return false; }
		}); //时间格式 不序列化列
	}
	
	public GsonJson notNull() { //为空的字段不转化
		builder.serializeNulls();
		return this;
	}

	public GsonJson reBuild() { //当改变了参数时则调用
		gson = builder.create();
		return this;
	}
	
	public <T> T from(String data, Class<T> clazz) { //非泛型
		T ret = null;
		UtilBoss.ConditionUtil.nt(data, clazz);
		
		ret = getGson().fromJson(data, clazz);
		return ret;
	}
	
	public <T> List<T> from(String data, TypeToken<List<T>> token) { //泛型
		List<T> ret = null;
		UtilBoss.ConditionUtil.nt(data, token);
		
		ret = getGson().fromJson(data, token.getType()); //T.class不行要直接泛型
		return ret;
	}
	
	public String to(Object obj) { //非泛型
		String ret = null;
		UtilBoss.ConditionUtil.n(obj);
		
		ret = getGson().toJson(obj);
		return ret;
	}
	
	public <T> String to(List<T> list) { //非泛型
		String ret = null;
		UtilBoss.ConditionUtil.n(list);
		
		ret = getGson().toJson(list, new TypeToken<List<T>>() {}.getType());
		return ret;
	}

	public Gson getGson() {
		if(UtilBoss.ObjUtil.isNull(gson)) gson = builder.create();
		return gson;
	}

	public GsonBuilder getBuilder() {
		return builder;
	}

	private Gson gson;
	private GsonBuilder builder;
}
