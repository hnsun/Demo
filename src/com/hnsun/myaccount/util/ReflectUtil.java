package com.hnsun.myaccount.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 反射的相关帮助类
 * @author hnsun
 * @date 2016/08/15
 */
public class ReflectUtil {

	private ReflectUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

	/**
	 * 获得某个名称的Class
	 * @param packageName
	 * @return
	 */
	public static Class<?> getClazz(String packageName) {
		Class<?> ret = null;
		UtilBoss.ConditionUtil.n(packageName);
		
		try {
			ret = Class.forName(packageName);
		} catch (ClassNotFoundException e) {
			LogFactory.log().e(e, "没有相关的类");
		}
		
		return ret;
	}
	
	/**
	 * 根据Class 构建对象
	 * @param clazz 对象的Class
	 * @param params 构建所需参数
	 * @return
	 * 不适用情况：有两个及以上构造函数的参数数量相同时
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> clazz, Object... params) {
		T ret = null;
		UtilBoss.ConditionUtil.n(clazz);
		
		try {
			if(UtilBoss.IfUtil.isEmpty(params)) return clazz.newInstance();
			
			Constructor<?>[] constructors = clazz.getConstructors(); //所有构造器 含有父类构造器
			for (Constructor<?> constructor : constructors) {
				Class<?>[] paramClazzs = constructor.getParameterTypes(); //当前构造器所有参数的类型
				
				if(UtilBoss.IfUtil.isEmpty(params) && UtilBoss.IfUtil.isEmpty(paramClazzs)) { //构造无参对象
					ret = (T) constructor.newInstance();
					break;
				}
				
				if(paramClazzs.length == params.length) { //没有判断类型相同数量参数 暂且认为同一个
					for (int i = 0; i < paramClazzs.length; i++) {
						params[i] = paramClazzs[i].cast(params[i]); //转换为相对应类型 当前类型或其子类类型
					}
					
					ret = (T) constructor.newInstance(params);
					break;
				}
			}
		} catch (InstantiationException e) {
			LogFactory.log().e(e, "实例化问题");
		} catch (IllegalAccessException e) {
			LogFactory.log().e(e, "没有合法获取权限");
		} catch (SecurityException e) {
			LogFactory.log().e(e, "不安全");
		} catch (IllegalArgumentException e) {
			LogFactory.log().e(e, "参数有问题");
		} catch (InvocationTargetException e) {
			LogFactory.log().e(e, "反射调用的方法出错");
		}
		
		return ret;
	}
	
	/**
	 * 获取某对象中某方法的返回值
	 * @param record
	 * @param method
	 * @param arguments
	 * @return
	 */
	public static <T> Object fromMethod(T record, Method method, Object... arguments) {
		Object ret = null;
		UtilBoss.ConditionUtil.n(record, method);
		
        try {
			ret = method.invoke(record, arguments);
		} catch (IllegalAccessException e) {
			LogFactory.log().e(e, "没有合法获取权限");
		} catch (SecurityException e) {
			LogFactory.log().e(e, "不安全");
		} catch (IllegalArgumentException e) {
			LogFactory.log().e(e, "参数有问题");
		} catch (InvocationTargetException e) {
			LogFactory.log().e(e, "反射调用的方法出错");
		}
		
		return ret;
	}
	
	/**
	 * 获取某对象中某方法的返回值
	 * @param record
	 * @param methodName
	 * @param arguments
	 * @return
	 */
	public static <T> Object fromMethodName(T record, String methodName, Object... arguments) {
		Object ret = null;
		UtilBoss.ConditionUtil.nt(record, methodName);
		
        try {
			Method method = record.getClass().getMethod(methodName, UtilBoss.ClassUtil.fromObjs(arguments));
			ret = ReflectUtil.fromMethod(record, method, arguments);
		} catch (NoSuchMethodException e) {
			LogFactory.log().e(e, "没有相关方法");
		} catch (SecurityException e) {
			LogFactory.log().e(e, "不安全");
		} catch (IllegalArgumentException e) {
			LogFactory.log().e(e, "参数有问题");
		}
		
		return ret;
	}
	
	/**
	 * 设置某对象中某方法的值
	 * @param record
	 * @param method
	 * @param value
	 */
	public static <T, E> void toMethod(T record, Method method, E value) {
		UtilBoss.ConditionUtil.n(record, method);
		
        try {
        	if(value instanceof String) {
            	Class<?> clazz = method.getParameterTypes()[0];
        		method.invoke(record, UtilBoss.ObjUtil.getByStr((String) value, clazz));
        	} else method.invoke(record, value);
		} catch (IllegalAccessException e) {
			LogFactory.log().e(e, "没有合法获取权限");
		} catch (SecurityException e) {
			LogFactory.log().e(e, "不安全");
		} catch (IllegalArgumentException e) {
			LogFactory.log().e(e, "参数有问题");
		} catch (InvocationTargetException e) {
			LogFactory.log().e(e, "反射调用的方法出错");
		}
	}
	
	/**
	 * 设置某对象中某方法的值
	 * @param record
	 * @param methodName
	 * @param arguments
	 * @return
	 */
	public static <T> void toMethodName(T record, String methodName, Object... arguments) {
		UtilBoss.ConditionUtil.nt(record, methodName);
		
        try {
			Method method = record.getClass().getDeclaredMethod(methodName, UtilBoss.ClassUtil.fromObjs(arguments));
			method.setAccessible(true);
			method.invoke(record, arguments);
		} catch (NoSuchMethodException e) {
			LogFactory.log().e(e, "没有相关方法");
		} catch (SecurityException e) {
			LogFactory.log().e(e, "不安全");
		} catch (IllegalArgumentException e) {
			LogFactory.log().e(e, "参数有问题");
		} catch (IllegalAccessException e) {
			LogFactory.log().e(e, "没有合法获取权限");
		} catch (InvocationTargetException e) {
			LogFactory.log().e(e, "反射调用的方法出错");
		}
	}
	
	/**
	 * 普通对象获取属性值
	 * @param record
	 * @param attributeName
	 * @return
	 */
	public static <T> Object getAttrValue(T record, String attributeName) {
		Object ret = null;
		UtilBoss.ConditionUtil.nt(record, attributeName);
		
		try {
			Field field = record.getClass().getDeclaredField(attributeName); //获得属性对象
			field.setAccessible(true); //修改权限
			ret = field.get(record);
		} catch (NoSuchFieldException e) {
			LogFactory.log().e(e, "该类没有此属性");
		} catch (IllegalAccessException e) {
			LogFactory.log().e(e, "没有权限获取");
		}
		
		return ret;
	}
	
	/**
	 * 普通对象设置属性值
	 * @param record
	 * @param attributeName
	 * @param value
	 */
	public static <T, E> void setAttrValue(T record, String attributeName, E value) {
		UtilBoss.ConditionUtil.nt(record, attributeName);
		
		try {
			Field field = record.getClass().getDeclaredField(attributeName); //获得属性对象
			field.setAccessible(true); //修改权限

        	if(value instanceof String) {
            	Class<?> clazz = field.getType();
            	field.set(record, UtilBoss.ObjUtil.getByStr((String) value, clazz));
        	} else field.set(record, value); //field.getType().cast(value)
		} catch (NoSuchFieldException e) {
			LogFactory.log().e(e, "该类没有此属性");
		} catch (IllegalAccessException e) {
			LogFactory.log().e(e, "没有权限获取");
		} catch (ClassCastException e) {
			LogFactory.log().e(e, "构造方法的参数不对应");
		}
	}

	/**
	 * 类名获取静态的属性值
	 * @param clazz
	 * @param attributeName
	 * @return
	 */
	public static Object getAttrValue(Class<?> clazz, String attributeName) {
		Object ret = null;
		UtilBoss.ConditionUtil.nt(clazz, attributeName);
		
		try {
			Field field = clazz.getDeclaredField(attributeName); //获得属性对象
			field.setAccessible(true); //修改权限
			ret = field.get(clazz);
		} catch (NoSuchFieldException e) {
			LogFactory.log().e(e, "该类没有此属性");
		} catch (IllegalAccessException e) {
			LogFactory.log().e(e, "没有权限获取");
		}
		
		return ret;
	}
	
	/**
	 * 获取属性类型
	 * @param clazz
	 * @param attributeName
	 * @return
	 */
	public static <T> String getAttrType(Class<T> clazz, String attributeName) {
		String ret = null;
		UtilBoss.ConditionUtil.nt(clazz, attributeName);
		
		try {
			Field field = clazz.getDeclaredField(attributeName); //获得属性对象
			ret = field.getType().getSimpleName();
		} catch (NoSuchFieldException e) {
			LogFactory.log().e(e, "该类没有此属性");
		} 
		
		return ret;
	}

	/**
	 * 普通对象获取属性与值之间键值对
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> getAttrValues(Object obj) {
		Map<String, Object> result = new HashMap<String, Object>();

		Field[] fields = obj.getClass().getDeclaredFields(); //所有属性
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				result.put(field.getName(), field.get(obj));
			} catch (IllegalAccessException e) {
				LogFactory.log().e(e, "没有权限获取");
			}
		}

		return result;
	}

	/**
	 * 获取当前Class所有以start开头的属性名值键值对
	 * @param clazz
	 * @param start
	 * @return
	 */
	public static Map<String, Object> attributeStartsWith(Class<?> clazz, String start) {
		Map<String, Object> ret = new HashMap<String, Object>();
		UtilBoss.ConditionUtil.nt(clazz, start);

		try {
			Field[] fields = clazz.getDeclaredFields(); //获得所有属性对象
			for (Field field : fields) {
				field.setAccessible(true); //修改权限
				String fieldName = field.getName();
				if(UtilBoss.StrUtil.isEmpty(start) || fieldName.startsWith(start)) ret.put(fieldName, field.get(clazz));
			}
		} catch (IllegalAccessException e) {
			LogFactory.log().e(e, "没有权限获取");
		}
		
		return ret;
	}
	
	/**
	 * 获取某个Class所有以start开头的方法名方法键值对
	 * @param clazz
	 * @param start
	 * @return
	 */
	public static Map<String, Method> methodStartsWith(Class<?> clazz, String start) {
		Map<String, Method> ret = new HashMap<String, Method>();
		UtilBoss.ConditionUtil.nt(clazz, start);
		
		Method[] methods = clazz.getMethods(); //所有公用方法
		for(Method method : methods) {
			String methodName = method.getName();
			if(UtilBoss.StrUtil.isEmpty(start) || methodName.startsWith(start)) ret.put(methodName, method);
		}
		
		return ret;
	} 
}
