package com.hnsun.myaccount.support.offline;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.model.dbo.PlfDownloadthread;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.TableBoss;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.OTransfer;
import com.hnsun.myaccount.util.data.SQLiteInstance;

/**
 * 下载线程管理离线
 * @author hnsun
 * @date 2016/10/03
 */
public class PlfDownloadthreadOffline {

	public PlfDownloadthreadOffline(Context context, Handler handler) { 
		if(UtilBoss.ObjUtil.isNull(context) || UtilBoss.ObjUtil.isNull(handler)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.context = context;
		this.handler = handler; 
	}

	public void list() { //获得所有下载记录
		UtilBoss.ThreadUtil.openThread(callback, PlfDownloadthreadOffline.LIST);
	}

	public void listDistinctUrl() { //获得所有下载记录连接
		UtilBoss.ThreadUtil.openThread(callback, PlfDownloadthreadOffline.LIST_DISTINCTURL);
	}

	public synchronized static List<PlfDownloadthread> listUrl(Context context, String url) { //获得该条下载连接所有下载记录
		List<PlfDownloadthread> ret = null;
		UtilBoss.ConditionUtil.nt(context, url);

		String statement = "SELECT * FROM " + TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.TAG) + " WHERE " + TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.DOWNLOADTHREAD_URL) + " = ?;";
		SQLiteDatabase db = SQLiteInstance.getInstance(context).get();
		Cursor cursor = db.rawQuery(statement, new String[] { url, });
		ret = OTransfer.toList(cursor, PlfDownloadthread.class, OTransfer.COLUMN_DATABASE);
		cursor.close();
		
		return ret;
	}
	
	public synchronized static boolean existUrl(Context context, String url) { //是否是已经有下载过
		boolean ret = false;
		UtilBoss.ConditionUtil.nt(context, url);

		SQLiteDatabase db = SQLiteInstance.getInstance(context).get();
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.TAG) + " WHERE " + TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.DOWNLOADTHREAD_URL) + " = ?;", new String[] { url });
		cursor.moveToFirst();
		ret = (cursor.getInt(0) > 0);
		cursor.close();
		
		return ret;
	}
	
	public synchronized static void inserts(Context context, List<PlfDownloadthread> records) { //插入新的下载记录
		UtilBoss.ConditionUtil.n(context, records);
		
		String statement = "INSERT INTO " + TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.TAG) + " VALUES(?, ?, ?, ?, ?, ?);";
		SQLiteDatabase db = SQLiteInstance.getInstance(context).get();
		for(PlfDownloadthread record : records) {
			Object[] args = { CodeBoss.MessCode.uuid(), record.getDownloadthreadUrl(), record.getDownloadthreadThread(), record.getDownloadthreadStart(), record.getDownloadthreadEnd(), record.getDownloadthreadCompleted(), };
			db.execSQL(statement, args);
		}
	}
	
	public synchronized static void update(Context context, PlfDownloadthread record) { //更新线程的下载记录
		UtilBoss.ConditionUtil.n(context, record);
		
		String statement = "UPDATE " + TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.TAG) + " SET " + 
				TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.DOWNLOADTHREAD_COMPLETED) + " = ? WHERE " + 
				TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.DOWNLOADTHREAD_URL) + " = ? AND " +
				TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.DOWNLOADTHREAD_THREAD) + " = ?;";
		SQLiteDatabase db = SQLiteInstance.getInstance(context).get();
		Object[] args = { record.getDownloadthreadCompleted(), record.getDownloadthreadUrl(), record.getDownloadthreadThread(), };
		db.execSQL(statement, args);
	}
	
	public synchronized static void deleteUrl(Context context, String url) { //删除某链接线程的下载记录
		UtilBoss.ConditionUtil.nt(context, url);
		
		SQLiteDatabase db = SQLiteInstance.getInstance(context).get();
		db.delete(TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.TAG), TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.DOWNLOADTHREAD_URL) + " = ?", new String[] { url, });
	}

	private Context context; //上下文
	private Handler handler; //回调给UI线程操作
	
	private ArgumentCallback callback = new ArgumentCallback() { //在异线程的操作
		
		@Override
		public void action(Object... objs) {
			int from = (Integer) objs[0];
			SQLiteDatabase db = SQLiteInstance.getInstance(context).get();
			String statement = null;
			Cursor cursor = null;
			
			switch(from) {
				case PlfDownloadthreadOffline.LIST:
					statement = "SELECT * FROM " + TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.TAG) + ";";
					
					cursor = db.rawQuery(statement, null);
					List<PlfDownloadthread> list = OTransfer.toList(cursor, PlfDownloadthread.class, OTransfer.COLUMN_DATABASE);
					cursor.close();
					CodeBoss.MessCode.handlerMsg(handler, from, UtilBoss.ObjUtil.isNotNull(list) ? list : new ArrayList<PlfDownloadthread>());
					break;
				case PlfDownloadthreadOffline.LIST_DISTINCTURL:
					statement = "SELECT DISTINCT " + TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.DOWNLOADTHREAD_URL) + " FROM " + TableBoss.toDatabaseName(TableBoss.PlfDownloadthread.TAG) + ";";
					
					cursor = db.rawQuery(statement, null);
					List<PlfDownloadthread> listDistinctUrl = OTransfer.toList(cursor, PlfDownloadthread.class, OTransfer.COLUMN_DATABASE);
					cursor.close();
					CodeBoss.MessCode.handlerMsg(handler, from, UtilBoss.ObjUtil.isNotNull(listDistinctUrl) ? listDistinctUrl : new ArrayList<PlfDownloadthread>());
					break;
			}
		}
	};
	
	public static final int LIST = 0x001;
	public static final int LIST_DISTINCTURL = 0x002;
}
