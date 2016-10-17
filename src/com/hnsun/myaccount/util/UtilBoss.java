package com.hnsun.myaccount.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.util.SparseArray;

import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 小工具总集
 * @author hnsun
 * @date 2016/08/15
 *  类型：ObjUtil, StrUtil, DatetimeUtil, IfUtil, ExceptionUtil, ClassUtil, ThreadUtil, ConditionUtil, 
 */
public class UtilBoss {
	
	private UtilBoss() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /*************
     *****
     **        对象处理工具 ：ObjUtil
     **
     *****
     *******************************************************/
	public static class ObjUtil {
		
		private ObjUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 是否为空
		 * @param obj
		 * @return
		 */
		public static boolean isNull(Object obj) {
			return null == obj;
		}
		
		/**
		 * 是否不为空
		 * @param obj
		 * @return
		 */
		public static boolean isNotNull(Object obj) {
			return null != obj;
		}

        /**
         * 将字符串根据class类型转换成相对应Object
         * @param str
         * @param clazz
         * @return
         */
        public static Object getByStr(String str, Class<?> clazz) {
            Object ret = null;
    		UtilBoss.ConditionUtil.n(clazz);

    		if(clazz == int.class) { //基本类型数字
    			ret = Integer.parseInt(str);
    		} else if(clazz == char.class) { //基本类型字符
    			ret = str.charAt(0);
    		} else if(clazz == long.class) { //基本类型字符
    			ret = Long.parseLong(str);
    		} else if(clazz == Date.class) { //时间
                ret = UtilBoss.DatetimeUtil.formatStr2Date(str, ConstantsUtil.FORMAT_DATE_DATETIME);
    		} else if(clazz == Integer.class) { //数字
                ret = Integer.parseInt(str);
    		} else {
                ret = str;
    		}

            return ret;
        }

        /**
         * 如果str为空或“”便把其变为null
         * @param str
         * @param clazz
         * @return
         */
        public static Object empty2null(String str, Class<?> clazz) {
    		UtilBoss.ConditionUtil.n(clazz);
            return UtilBoss.StrUtil.isEmpty(str) ? null : getByStr(str, clazz);
        }
	}

    /*************
     *****
     **        字符串处理工具 ：StrUtil
     **
     *****
     *******************************************************/
	public static class StrUtil {
		
		private StrUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 输入的字符串是否为空
		 * @param str
		 * @return
		 */
		public static boolean isEmpty(String str) {
			return UtilBoss.ObjUtil.isNull(str) || 0 == str.length() || 0 == str.replaceAll("\\s", "").length();
		}
		
		/**
		 * 根据不同的类型将对象转为相对应的字符串
		 * @param obj
		 * @return
		 */
		public static String getByObj(Object obj) {
			String ret = null;
			if(UtilBoss.ObjUtil.isNull(obj)) return ret;
			
			if(obj instanceof Date) { //时间
				ret = UtilBoss.DatetimeUtil.formatDate2Str((Date) obj, ConstantsUtil.FORMAT_DATE_DATETIME);
			} else if(obj instanceof Integer) { //数字
				ret = (Integer) obj + "";
			} else {
				ret = obj.toString();
			}
			
			return ret;
		}
	
		/**
		 * 如果对象为null 返回“”否者返回对应的字符串
		 * @param obj
		 * @return
		 */
		public static String null2Empty(Object obj) {
			return UtilBoss.ObjUtil.isNotNull(obj) ? getByObj(obj) : "";
		}
		
		/**
		 * 字符串截取
		 * @param str
		 * @param indexs
		 * @return
		 */
		public static String substring(String str, int... indexs) {
			String ret = null;
			UtilBoss.ConditionUtil.n(str);
			
			int beginIndex = 0, endIndex = str.length();
			switch (indexs.length) {
				case 0: break;
				case 1: 
					if(indexs[0] >= 0) beginIndex += indexs[0];
					if(indexs[0] < 0) endIndex += indexs[0];
					break;
				default: 
					beginIndex = indexs[0] > 0 ? indexs[0] : 0;
					if(indexs[1] >= 0) endIndex = indexs[1];
					if(indexs[1] < 0) endIndex += indexs[1];
					break;
			}
			ret = new String(str.substring(beginIndex, endIndex));
			
			return ret;
		}
	
		/**
		 * 如果字符串最尾不是换行就加上
		 * @param str
		 * @return
		 */
		public static String notNeedLine(String str) {
			UtilBoss.ConditionUtil.n(str);
			
			return str.endsWith(ConstantsUtil.CODE_LINE_FEED) ? str : (str + ConstantsUtil.CODE_LINE_FEED);
		}
		
		/**
		 * 首字母大写
		 * @param str
		 * @return
		 */
		public static String upFirstLetter(String str) {
			UtilBoss.ConditionUtil.n(str);
			
			StringBuilder ret = new StringBuilder(str);
			ret.setCharAt(0, Character.toUpperCase(ret.charAt(0)));
			return ret.toString();
		}
		
		/**
		 * 首字母小写
		 * @param str
		 * @return
		 */
		public static String downFirstLetter(String str) {
			UtilBoss.ConditionUtil.n(str);
			
			StringBuilder ret = new StringBuilder(str);
			ret.setCharAt(0, Character.toLowerCase(ret.charAt(0)));
			return ret.toString();
		}
		
		/**
		 * 首字母换成其他字母
		 * @param str
		 * @param c
		 * @return
		 */
		public static String tranFirstLetter(String str, char c) {
			UtilBoss.ConditionUtil.n(str);
			
			StringBuilder ret = new StringBuilder(str);
			ret.setCharAt(0, c);
			return ret.toString();
		}
		
		/**
		 * 转化为骆驼名(AssDssFx)
		 * @param str
		 * @param split
		 * @return
		 */
		public static String camel(String str, String split) {
			StringBuilder ret = new StringBuilder();
			UtilBoss.ConditionUtil.n(str, split);
			
			String[] sections = str.toLowerCase(Locale.getDefault()).split(split);
			for (String section : sections) {
				ret.append(UtilBoss.StrUtil.upFirstLetter(new String(section)));
			}
			
			return ret.toString();
		}
		
		/**
		 * 反骆驼化
		 * @param str
		 * @param split
		 * @return
		 */
		public static String camelReverse(String str, String split) {
			StringBuilder ret = new StringBuilder();
			UtilBoss.ConditionUtil.n(str, split);
			
			for (int i = 0; i < str.length(); i++) {
				if(Character.isUpperCase(str.charAt(i))) ret.append(split);
				ret.append(str.charAt(i));
			}
			
			return UtilBoss.StrUtil.substring(ret.toString(), 1);
		}
		
		/**
		 * 某字段重复多次组合
		 * @param str
		 * @param count
		 * @return
		 */
		public static String again(String str, int count) {
			StringBuilder ret = new StringBuilder();
			UtilBoss.ConditionUtil.n(str);
			
			for (int i = 0; i < count; i++) {
				ret.append(str);
			}
			
			return ret.toString();
		}

        /**
         * 获得首字母
         * @param str
         * @return
         */
        public static String getCapital(String str) {
			UtilBoss.ConditionUtil.n(str);
            char[] chars = str.toCharArray();
            return String.valueOf(chars[0]);
        }
	}
	
    /*************
     *****
     **        时间处理工具 ：DatetimeUtil
     **
     *****
     *******************************************************/
	public static class DatetimeUtil {
		
		private DatetimeUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * Date转为指定格式的String
		 * @param date
		 * @param format
		 * @return
		 */
		public static String formatDate2Str(Date date, String format) {
			UtilBoss.ConditionUtil.nt(date, format);
			
			return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).format(date);
		}
	
		/**
		 * String转为指定格式的Date
		 * @param date
		 * @param format
		 * @return
		 */
		public static Date formatStr2Date(String date, String format) {
			UtilBoss.ConditionUtil.n(date, format);
			
			try {
				return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).parse(date);
			} catch (ParseException e) {
            	LogFactory.log().e(e, "转化失败");
			}
			
			return null;
		}

        /**
         *  根据所选时段对时间的修改
         * @param datePart 所代表时间段
         * @param dateValue 本身时间
         * @param value 需要修改为值
         * @return
         */
        public static Date dateAdd(DatePart datePart, Date dateValue, int value) {
    		UtilBoss.ConditionUtil.n(datePart, dateValue);
    		
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dateValue);
            switch (datePart) {
                case Year: gc.add(Calendar.YEAR, value); break;
                case Momth: gc.add(Calendar.MONTH, value); break;
                case Day: gc.add(Calendar.DATE, value); break;
                case Hour: gc.add(Calendar.HOUR, value); break;
                case Mim: gc.add(Calendar.MINUTE, value); break;
                case Sec: gc.add(Calendar.SECOND, value); break;
            }
            return gc.getTime();
        }
	}

    /*************
     *****
     **        判断处理工具 ：IfUtil
     **
     *****
     *******************************************************/
	public static class IfUtil {
		
		private IfUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
	
		/**
		 * 数组是否有数据
		 * @param datas
		 * @return
		 */
		public static <E> boolean isEmpty(E[] datas) {
			return UtilBoss.ObjUtil.isNull(datas) || 0 == datas.length;
		}
		
		/**
		 * 列表是否有数据
		 * @param datas
		 * @return
		 */
		public static boolean isEmpty(List<?> datas) {
			return UtilBoss.ObjUtil.isNull(datas) || 0 == datas.size();
		}

		/**
		 * 集合是否有数据
		 * @param datas
		 * @return
		 */
		public static boolean isEmpty(Set<?> datas) {
			return UtilBoss.ObjUtil.isNull(datas) || 0 == datas.size();
		}
		
		/**
		 * 映射是否有数据
		 * @param datas
		 * @return
		 */
		public static boolean isEmpty(Map<?, ?> datas) {
			return UtilBoss.ObjUtil.isNull(datas) || 0 == datas.size();
		}
	
		/**
		 * 指数是否有数据
		 * @param datas
		 * @return
		 */
		public static <E> boolean isEmpty(SparseArray<?> datas) {
			return UtilBoss.ObjUtil.isNull(datas) || 0 == datas.size();
		}
	
		/**
		 * 是否有为空的
		 * @param strs
		 * @return
		 */
		public static boolean isEmpty(String... datas) {
			boolean ret = false;
			for(String str : datas) {
				if(UtilBoss.StrUtil.isEmpty(str)) {
					ret = true;
					break;
				}
			}
			return ret;
		}
	}
	
    /*************
     *****
     **        异常处理工具 ：ExceptionUtil
     **
     *****
     *******************************************************/
	public static class ExceptionUtil {
		
		private ExceptionUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

		/**
		 * 不合法的操作
		 */
		public static void throwUnsupportedOperation(String str) {
			throw new UnsupportedOperationException(str);
		}
		
		/**
		 * 防止实例化
		 */
		public static void throwUnsupportedOperationInit() {
			ExceptionUtil.throwUnsupportedOperation("cannot be instantiated.");
		}
		
		/**
		 * 防止不符合要求的初始化参数
		 */
		public static void throwIllegalArgumentInit() {
			throw new IllegalArgumentException("Parameter is not in conformity with the requirements when init.");
		}
		
		/**
		 * 防止空值参数
		 */
		public static void throwNullPointerArgument() {
			throw new NullPointerException("Argument can not be null.");
		}
	}

    /*************
     *****
     **        类处理工具 ：ClassUtil
     **
     *****
     *******************************************************/
	public static class ClassUtil {
		
		private ClassUtil() { ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 获取当前类的全路径及名称
		 * @return
		 */
		public static String getCurClassName() {
			String ret = null;
			
			StackTraceElement[] elements = Thread.currentThread().getStackTrace();
			if(UtilBoss.IfUtil.isEmpty(elements)) return null;

//			for (int i = elements.length - 1; i >= 0; i--) {//从外向里
			for (int i = 0; i < elements.length; i++) {//从里向外
				if(elements[i].getClassName().startsWith("com.hnsun")) { //最后一个自定义的文件名
					ret = elements[i].getClassName();
					if(ret.contains("$")) ret = UtilBoss.StrUtil.substring(ret.toString(), 0, ret.indexOf("$"));
					if(ret.endsWith("UtilBoss") || ret.endsWith("LogFactory")) continue; //最先接触是这两个类 暂时不处理
					break;
				}
			}
			
			return ret;
		}
		
		/**
		 * 从对象数组中获得他们相对应的类型数组
		 * @param objs
		 * @return
		 */
		public static Class<?>[] fromObjs(Object... objs) {
			Class<?>[] ret = new Class<?>[objs.length];
			
			for(int i = 0; i < objs.length; i++) {
				ret[i] = UtilBoss.ClassUtil.basicClazz(objs[i].getClass());
			}
			
			return ret;
		}
		
		/**
		 * 判断是否为基本类型种类 并转化之
		 * @param objs
		 * @return
		 */
		public static Class<?> basicClazz(Class<?> clazz) {
			Class<?> ret = clazz;
			UtilBoss.ConditionUtil.n(clazz);
			
			if(Byte.class.equals(clazz)) {
				ret = byte.class;
			} else if(Short.class.equals(clazz)) {
				ret = short.class;
			} else if(Integer.class.equals(clazz)) {
				ret = int.class;
			} else if(Long.class.equals(clazz)) {
				ret = long.class;
			} else if(Float.class.equals(clazz)) {
				ret = float.class;
			} else if(Double.class.equals(clazz)) {
				ret = double.class;
			} else if(Boolean.class.equals(clazz)) {
				ret = boolean.class;
			} else if(Character.class.equals(clazz)) {
				ret = char.class;
			}
			
			return ret;
		}
	}

    /*************
     *****
     **        线程处理工具 ：ThreadUtil
     **
     *****
     *******************************************************/
	public static class ThreadUtil {
		
		private ThreadUtil() { ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 阻断使用当前对象的线程
		 * @param obj
		 */
		public static void wait(Object obj) {
			UtilBoss.ConditionUtil.n(obj);
			
			try {
				obj.wait();
			} catch (InterruptedException e) {
				LogFactory.log().e(e, "睡眠被打断");
			}
		}

		/**
		 * 等待毫秒, actions[0]存在时必须执行相应方法 其余忽略
		 * @param milliseconds
		 * @param actions
		 */
		public static void sleepBy(long milliseconds, ArgumentCallback... actions) {
			try {
				Thread.sleep(milliseconds);
			} catch (InterruptedException e) {
				LogFactory.log().e(e, "睡眠被打断");
			} finally {
				if(actions.length > 0) actions[0].action();
			}
		}

		/**
		 * 开启新线程执行
		 * @param callback
		 */
		public static void openThread(final ArgumentCallback callback, final Object... objs) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					if(UtilBoss.ObjUtil.isNotNull(callback)) callback.action(objs);
				}
			}).start();
		}
	}

    /*************
     *****
     **        条件处理工具 ：ConditionUtil
     **
     *****
     *******************************************************/
	public static class ConditionUtil {
		
		private ConditionUtil() { ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 若有为空值 抛出异常
		 * @param objs
		 */
		public static void n(Object... objs) {
			for(Object obj : objs) {
				if(UtilBoss.ObjUtil.isNull(obj)) {
					UtilBoss.ExceptionUtil.throwNullPointerArgument();
					break;
				}
			}
		}

		/**
		 * 若为空值 抛出异常
		 * @param strs
		 */
		public static void n(String... strs) {
			if(UtilBoss.IfUtil.isEmpty(strs)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		}
		
		/**
		 * 根据类型为空值 抛出异常
		 * @param objs
		 */
		public static void nt(Object... objs) {
			for(Object obj : objs) {
				if(obj instanceof String) { //字符串
					if(UtilBoss.StrUtil.isEmpty((String) obj)) {
						UtilBoss.ExceptionUtil.throwNullPointerArgument();
						break;
					}
				} else { //其他
					if(UtilBoss.ObjUtil.isNull(obj)) {
						UtilBoss.ExceptionUtil.throwNullPointerArgument();
						break;
					}
				}
			}
		}
		
		/**
		 * 数组为空值 抛出异常
		 * @param objs
		 */
		public static <T> void ns(T[]... ts) {
			for(T[] t : ts) {
				if(UtilBoss.IfUtil.isEmpty(t)) {
					UtilBoss.ExceptionUtil.throwNullPointerArgument();
					break;
				}
			}
		}
	}

	/**
	 * 时间区分
	 * @author hnsun
	 * @date 2016/08/19
	 */
    public enum DatePart {
        Year, Momth, Day, Hour, Mim, Sec
    };
}
