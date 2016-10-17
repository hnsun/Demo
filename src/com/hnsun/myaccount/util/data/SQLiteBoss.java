package com.hnsun.myaccount.util.data;

import android.database.sqlite.SQLiteDatabase;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 数据库相关语句处理
 * @author hnsun
 * @date 2016/09/06
 * 类型：Init、Db、Data、
 */
public class SQLiteBoss {
	
	private SQLiteBoss() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /*************
     *****
     **        初始化数据库 ：Init
     **
     *****
     *******************************************************/
	public static class Init {
		
		private Init() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 执行初始化数据库语句
		 * @param db
		 */
		public static void run(SQLiteDatabase db) {
			UtilBoss.ConditionUtil.n(db);
			
			String statement = null;
			
			statement = "CREATE TABLE IF NOT EXISTS TBL_USER (" +
					"	USER_ID VARCHAR(36) PRIMARY KEY NOT NULL," +
					"	USER_LOGNAME NVARCHAR(20) NOT NULL," +
					"	USER_LOGPASSWORD NVARCHAR NOT NULL," +
					"	USER_CNNAME NVARCHAR(100)," +
					"	USER_ENNAME VARCHAR(100)," +
					"	USER_INFO NVARCHAR," +
					"	LAST_UPDATE_DATE DATETIME NOT NULL," +
					"	DELETED CHAR(1) NOT NULL DEFAULT 'N' CHECK(DELETED = 'N' OR DELETED = 'Y'))";
			db.execSQL(statement); //用户
			
			statement = "CREATE TABLE IF NOT EXISTS TBL_FILE (" +
					"	FILE_ID VARCHAR(36) PRIMARY KEY NOT NULL," +
					"	FILE_NAME NVARCHAR(100) NOT NULL," +
					"	FILE_PLATFORM CHAR(1) NOT NULL DEFAULT 'A' CHECK(FILE_PLATFORM = 'A' OR FILE_PLATFORM = 'S' OR FILE_PLATFORM = 'C' OR FILE_PLATFORM = 'P')," +
					"	FILE_URL NVARCHAR NOT NULL," +
					"	FILE_DATE DATETIME NOT NULL," +
					"	FILE_DESC NVARCHAR," +
					"	LAST_UPDATE_DATE DATETIME NOT NULL," +
					"	DELETED CHAR(1) NOT NULL DEFAULT 'N' CHECK(DELETED = 'N' OR DELETED = 'Y'))";
			db.execSQL(statement); //文件
			
			statement = "CREATE TABLE IF NOT EXISTS PLF_DOWNLOADRECORD (" +
					"	DOWNLOADRECORD_ID VARCHAR(36) PRIMARY KEY NOT NULL," +
					"	DOWNLOADRECORD_DATE DATETIME NOT NULL," +
					"	FILE_ID VARCHAR(36) NOT NULL)";
			db.execSQL(statement); //下载记录
			
			statement = "CREATE TABLE IF NOT EXISTS PLF_DOWNLOADTHREAD (" +
					"	DOWNLOADTHREAD_ID VARCHAR(36) PRIMARY KEY NOT NULL," +
					"	DOWNLOADTHREAD_URL NVARCHAR NOT NULL," +
					"	DOWNLOADTHREAD_THREAD INTEGER NOT NULL DEFAULT 0 CHECK(DOWNLOADTHREAD_THREAD >= 0)," +
					"	DOWNLOADTHREAD_START INTEGER NOT NULL," +
					"	DOWNLOADTHREAD_END INTEGER NOT NULL," +
					"	DOWNLOADTHREAD_COMPLETED INTEGER NOT NULL)";
			db.execSQL(statement); //下载线程控制
		}
	}

    /*************
     *****
     **        更新数据库 ：Db
     **
     *****
     *******************************************************/
	public static class Db {
		
		private Db() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
	}

    /*************
     *****
     **        更新数据库数据 ：Data
     **
     *****
     *******************************************************/
	public static class Data {
		
		private Data() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
	}
}
