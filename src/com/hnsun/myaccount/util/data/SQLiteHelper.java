package com.hnsun.myaccount.util.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库初始建立及升级处理
 * @author hnsun
 * @date 2016/09/06
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context) {
		this(context, ApplicationDatas.DATABASE_NAME, null, ApplicationDatas.DATABASE_VERSION);
	}

	public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) { //初始化数据库
		SQLiteBoss.Init.run(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //升级数据库
		db.beginTransaction(); //事务
		try {
			switch(oldVersion) {
				default: break;
			}
			db.setTransactionSuccessful(); //成功事务
		} finally {
			db.endTransaction();
		}
	}
}
