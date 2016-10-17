package com.hnsun.myaccount.util.data;

import android.os.Environment;
import android.util.Log;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.file.PathUtil;

/**
 * 应用默认固定化数据
 * @author hnsun
 * @date 2016/08/15
 */
public class ApplicationDatas {

	public static final String APP_NAME = "uAccount"; //应用名称
	public static final String APP_ENCODING = ConstantsUtil.ENCODING_UTF8; //应用编码
	public static final String APP_FORMAT_PICTURE = ConstantsUtil.FORMAT_PICTURE_PNG; //应用图片默认格式
	public static final String APP_PACKAGE = "com.hnsun"; //应用包序
	public static final String APP_DIRECTORY = Environment.getExternalStorageDirectory() + PathUtil.separatorBefore(ApplicationDatas.APP_NAME); //应用系统文件夹
	public static final String APP_SERVER_TEST = "http://192.168.1.100:8080/MyAccount/android/"; //应用检测服务器
	public static final String APP_SERVER_DEV = "http://www.hnsun.com/android/"; //应用开发服务器
	public static final String APP_SERVER_OPD = "http://www.hnsun.com/android/"; //应用线上服务器

	public static final int DATABASE_VERSION = 1; //数据库版本
	public static final String DATABASE_NAME = ApplicationDatas.APP_NAME + ".db"; //数据库名称

	public static final int LOG_LEVEL = Log.VERBOSE; //日志级别
	public static final boolean LOG_DEBUG = true; //是否记录日志
	public static final boolean LOG_SAVE = false; //是否保存日志
	public static final String LOG_CAT = "CatLog"; //CatLog
	public static final String LOG_COMMON = "CommonLog"; //CommonLog

	public static final int PASSCODE_TIMEOUT =  1 * 1000; //应用锁一秒失效，若重启应用便要密码
	public static final boolean PASSCODE_ROTATION =  true; //应用锁界面是否可旋转换屏
	public static final String PASSCODE_SALT = "sadasauidhsuyeuihdahdiauhs";
	
	public static final long ACCOUNT_TIMEOUT = 3 * 24 * 60 * 60 * 1000; //三天后要重新登陆
	
	public static final int DOWNLOAD_THREADCOUNT = 3; //下载的线程数为3
	
	public static final long SLIDE_INTERVEL = 6 * 1000; //6秒滚动一次
	public static final int[] SLIDE_BANNER = { R.drawable.img_banner_1, R.drawable.img_banner_2, R.drawable.img_banner_3, R.drawable.img_banner_4, R.drawable.img_banner_5 };
	
	public static final String NET_APACHE = "ApacheNet"; //ApacheNet
	public static final String NET_URLCONNECTION = "URLConnectionNet"; //URLConnectionNet
    
    public static final String FONT_UMINE = "umine";
    public static final String FONT_BRAND = "brand";
	
	public static final int EXIT_TOAST = 0x01; //吐司方式
	public static final int EXIT_DIALOG = 0x02; //弹框方式
	
	public static final String VIEW_TAG_SKIN = "#SKIN:"; //该组件需皮肤化
	
	public static final String XML_ROOT_NAME = "Root"; //传输的XML统一外层名
	
	public static final Integer[] NAVIGATION_MALL_FILE = { R.string.txt_home, R.string.txt_text, R.string.txt_picture, R.string.txt_other, }; //文件商城
	
	public static final int[] TOURIST_MENU_TEXT = { R.string.app_info_about, R.string.app_info_contact, R.string.app_info_recruit, R.string.app_info_drawback, R.string.app_info_help, R.string.app_info_share, R.string.app_info_link, };
	public static final int[] TOURIST_MENU_IMAGE = { R.drawable.ic_about, R.drawable.ic_contact, R.drawable.ic_recruit, R.drawable.ic_drawback, R.drawable.ic_help, R.drawable.ic_share, R.drawable.ic_link, };
	public static final String[] TOURIST_MENU_URL = { "AboutActivity", "AboutActivity", "AboutActivity", "AboutActivity", "AboutActivity", "AboutActivity", "AboutActivity", };
}
