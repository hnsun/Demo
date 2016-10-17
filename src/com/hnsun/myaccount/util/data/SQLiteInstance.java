package com.hnsun.myaccount.util.data;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.file.FileUtil;
import com.hnsun.myaccount.util.file.PathUtil;
import com.hnsun.myaccount.util.platform.AppUtil;
import com.hnsun.myaccount.util.platform.SharedPreferencesUtil;

/**
 * 数据库实例
 * @author hnsun
 * @date 2016/09/06
 */
public class SQLiteInstance {

	private SQLiteInstance(Context context) { //getApplicationContext()
		if(UtilBoss.ObjUtil.isNull(context)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.context = context;
	}
	
	public static synchronized SQLiteInstance getInstance(Context context) {
		if(UtilBoss.ObjUtil.isNull(instance)) instance = new SQLiteInstance(context);
		
		return instance;
	}
	
	public SQLiteDatabase get() {
		if(UtilBoss.ObjUtil.isNull(db) || !db.isOpen()) openDatabase();
		return db;
	}
	
	public void close() {
		if(UtilBoss.ObjUtil.isNotNull(db) && db.isOpen()) db.close();
		if(UtilBoss.ObjUtil.isNotNull(helper)) helper.close();
	}
	
	public boolean delete() {
		return context.deleteDatabase(ApplicationDatas.DATABASE_NAME);
	}
	
	private void openDatabase() {
		String dir = AppUtil.getInnerPath(context, AppUtil.INNER_DIR_DATABASE);
		String path = dir + PathUtil.separatorBefore(ApplicationDatas.DATABASE_NAME);
		
		if(!new File(path).exists()) { //数据库文件不存在时
        	PathUtil.mkDipPath(dir);
        	//文件拷贝
		}
		helper = new SQLiteHelper(context);
		db = helper.getWritableDatabase(); //SQLiteDatabase.openOrCreateDatabase(path, null); 需处理升级
		db.execSQL("PRAGMA synchronous = FULL"); //安全不破坏数据库，但效率较低
		
		SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, SharedPreferencesUtil.FILENAME_DATABASE_CONFIG);
		String vacuum = sharedPreferencesUtil.lockLoad(SharedPreferencesUtil.KEY_DATABASE_VACUUM);
		if(UtilBoss.StrUtil.isEmpty(vacuum) || "Y".equals(vacuum)) db.execSQL("VACUUM"); //清空删除区域
		sharedPreferencesUtil.lockPut(SharedPreferencesUtil.KEY_DATABASE_VACUUM, "N");
	}
	
    public static void copyDatabase(Context context) { //导出数据库文件
		String path = AppUtil.getInnerPath(context, AppUtil.INNER_DIR_DATABASE) + PathUtil.separatorBefore(ApplicationDatas.DATABASE_NAME);
        String outPath = PathUtil.SystemPath.get(PathUtil.SystemPath.DOWNLOAD)+ PathUtil.separatorBefore(ApplicationDatas.DATABASE_NAME); //最外层目录

        if(new File(path).exists()) FileUtil.copyFile(path, outPath);
    }
    
	private static SQLiteInstance instance; //单例实例
	
	private Context context; //上下文
	private SQLiteHelper helper; //数据库的帮助类
	private SQLiteDatabase db; //当前数据库
}
