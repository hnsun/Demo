package com.hnsun.myaccount.util.data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.database.Cursor;

import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.ReflectUtil;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * Bean之间相互转化 如VO、PO及BO等
 * @author hnsun
 * @date 2016/08/15
 */
public class OTransfer {

	private OTransfer() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
	
	/**
	 * 将对象相应属性转化为某类对象相应属性，含父类属性
	 * @param from
	 * @param toClass
	 * @return
	 */
	public static <T> T from(Object from, Class<T> toClass) {
		T ret = null;
		UtilBoss.ConditionUtil.n(from, toClass);

		ret = ReflectUtil.newInstance(toClass);
		Class<?> fromClass = from.getClass();
		Map<String, Method> getMethod = ReflectUtil.methodStartsWith(fromClass, ConstantsUtil.PRONOUN_GET);
		Map<String, Method> setMethod = ReflectUtil.methodStartsWith(toClass, ConstantsUtil.PRONOUN_SET);
		
		Iterator<Entry<String, Method>> iter = getMethod.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, Method> entry = iter.next();
			
			String setName = UtilBoss.StrUtil.tranFirstLetter(entry.getKey(), 's');
			if(setMethod.containsKey(setName)) ReflectUtil.toMethod(ret, setMethod.get(setName), ReflectUtil.fromMethod(from, entry.getValue())); //存在便设置
		}
		
		return ret;
	}
	
	/**
	 * 单个数据根据相对应的属性名赋值转化
	 * @param columns
	 * @param from
	 * @param toClass
	 * @return
	 */
	public static <T> T from(String[] columns, Object[] from, Class<T> toClass) {
		T ret = null;
		UtilBoss.ConditionUtil.n(columns, from, toClass);

		ret = ReflectUtil.newInstance(toClass);
		Map<String, Method> setMethod = ReflectUtil.methodStartsWith(toClass, ConstantsUtil.PRONOUN_SET);
		for(int i = 0; i < columns.length; i ++) {
			String setName = ConstantsUtil.PRONOUN_SET + UtilBoss.StrUtil.upFirstLetter(columns[i]);
			if(setMethod.containsKey(setName)) ReflectUtil.toMethod(ret, setMethod.get(setName), from[i]); //存在便设置
		}
		return ret;
	}
	
	/**
	 * 整个组数据根据相对应的属性名赋值转化 
	 * @param columns
	 * @param from
	 * @param toClass
	 * @return
	 */
	public static <T> List<T> from(String[] columns, List<Object[]> from, Class<T> toClass) {
		List<T> ret = null;
		UtilBoss.ConditionUtil.n(columns, from, toClass);

		ret = new ArrayList<T>();
		for (Object[] objs : from) {
			ret.add(OTransfer.from(columns, objs, toClass));
		}
		return ret;
	}

	/**
	 * 将当前光标位置转化为单个对象
	 * @param cursor
	 * @param toClass
	 * @return
	 */
	public static <T> T from(Cursor cursor, Class<T> toClass, int tranType) {
		T ret = ReflectUtil.newInstance(toClass);
		UtilBoss.ConditionUtil.n(cursor, toClass);

		Map<String, Method> setMethod = ReflectUtil.methodStartsWith(toClass, ConstantsUtil.PRONOUN_SET);
		String[] columns = cursor.getColumnNames(); //字段名
		for(int i = 0; i < columns.length; i ++) {
			String setName = ConstantsUtil.PRONOUN_SET;
			switch(tranType) {
				case OTransfer.COLUMN_UPFIRST: setName += UtilBoss.StrUtil.upFirstLetter(columns[i]); break;
				case OTransfer.COLUMN_DATABASE: setName += UtilBoss.StrUtil.camel(columns[i], "_"); break;
				default : setName += columns[i]; break;
			}
			if(setMethod.containsKey(setName)) { //存在便设置
				switch(cursor.getType(i)) {
					case Cursor.FIELD_TYPE_BLOB: ReflectUtil.toMethod(ret, setMethod.get(setName), cursor.getBlob(i)); break;
					case Cursor.FIELD_TYPE_FLOAT: ReflectUtil.toMethod(ret, setMethod.get(setName), cursor.getFloat(i)); break;
					case Cursor.FIELD_TYPE_INTEGER: ReflectUtil.toMethod(ret, setMethod.get(setName), cursor.getInt(i)); break;
					case Cursor.FIELD_TYPE_STRING: ReflectUtil.toMethod(ret, setMethod.get(setName), cursor.getString(i)); break;
					default: ReflectUtil.toMethod(ret, setMethod.get(setName), cursor.getString(i)); break;
				}
			}
		}
		
		return ret;
	}

	/**
	 * 将数据库返回的数据转化为单个对象
	 * @param cursor
	 * @param toClass
	 * @return
	 */
	public static <T> T toObj(Cursor cursor, Class<T> toClass, int tranType) {
		T ret = null;
		UtilBoss.ConditionUtil.n(cursor, toClass);

		if(cursor.moveToNext()) ret = OTransfer.from(cursor, toClass, tranType); //由引用者关闭光标
		return ret;
	}

	/**
	 * 将数据库返回的数据转化为所有对象
	 * @param cursor
	 * @param toClass
	 * @return
	 */
	public static <T> List<T> toList(Cursor cursor, Class<T> toClass, int tranType) {
		List<T> ret = null;
		UtilBoss.ConditionUtil.n(cursor, toClass);
		
		if(cursor.moveToNext()) {
			ret = new ArrayList<T>();
			ret.add(OTransfer.from(cursor, toClass, tranType));
			
			while(cursor.moveToNext()) {
				ret.add(OTransfer.from(cursor, toClass, tranType));
			}
		}
		
		return ret;
	}
	
	public static final int COLUMN_UPFIRST = 0x210101; //第一个字母大写
	public static final int COLUMN_DATABASE = 0x210102; //数据库字段转化
}
