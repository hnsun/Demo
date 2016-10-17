package com.hnsun.myaccount.util;

/**
 * 常量集
 * @author hnsun
 * @date 2016/08/15
 */
public class ConstantsUtil {

	private ConstantsUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

	//固定写法
	public static final int BYTE_BUFFER_SIZE = 2 << 10; //缓存大小
	public static final String CODE_LINE_FEED = "\r\n"; //换行
	public static final String NULL_STRING = "N/A"; //没有的值
	public static final char[] LETTER_EN = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'S', 'Y', 'Z' };

	//命名空间
	public static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"; //android

	//时间格式
	public static final String FORMAT_DATE_DATETIME = "yyyy-MM-dd HH:mm:ss"; //基本形式
	public static final String FORMAT_DATE_DATETIME_SQL = "yyyy-MM-dd HH:mm:ss.SSS"; //数据库形式
	public static final String FORMAT_DATE_DATETIME_LOG = "[yyyy-MM-dd HH:mm:ss]"; //日志使用形式
	public static final String FORMAT_DATE_DATETIME_JS = "yyyy:MM:dd:HH:mm:ss"; //脚本处理使用形式
	
	//图片格式
	public static final String FORMAT_PICTURE_PNG = "png"; //png
	public static final String FORMAT_PICTURE_JPG = "jpg"; //jpg
	
	//编码
	public static final String ENCODING_UTF8 = "UTF-8"; //UTF-8
	public static final String ENCODING_GBK = "GBK"; //GBK

	//代词
	public static final char PRONOUN_Y = 'Y';
	public static final char PRONOUN_N = 'N';
	public static final String PRONOUN_SET = "set";
	public static final String PRONOUN_GET = "get";
	public static final String PRONOUN_IN = "in";
	public static final String PRONOUN_OUT = "out";
	public static final String PRONOUN_SUCCESS = "SUCCESS";
	public static final String PRONOUN_FAIL = "FAIL";
	public static final String PRONOUN_HTTP_GET = "GET";
	public static final String PRONOUN_HTTP_POST = "POST";
}
