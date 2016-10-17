package com.hnsun.myaccount.support.offline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.model.dbo.TblUser;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.TableBoss;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.OTransfer;
import com.hnsun.myaccount.util.data.SQLiteInstance;

/**
 * 用户离线
 * @author hnsun
 * @date 2016/09/28
 */
public class TblUserOffline {

	public TblUserOffline(Context context, Handler handler) { 
		if(UtilBoss.ObjUtil.isNull(context) || UtilBoss.ObjUtil.isNull(handler)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.context = context;
		this.handler = handler; 
	}
	
	public void list() {
		UtilBoss.ThreadUtil.openThread(callback, TblUserOffline.LIST);
	}
	
	public synchronized static TblUser get(Context context, String recordId) {
		TblUser ret = null;
		UtilBoss.ConditionUtil.nt(context, recordId);
		
		SQLiteDatabase db = SQLiteInstance.getInstance(context).get();
		Cursor cursor = db.query(TableBoss.toDatabaseName(TableBoss.TblUser.TAG), null, TableBoss.toDatabaseName(TableBoss.DELETED) + " = ? AND " + TableBoss.toDatabaseName(TableBoss.TblUser.USER_ID) + " = ?", new String[] { "N", recordId, }, null, null, null);
		ret = OTransfer.toObj(cursor, TblUser.class, OTransfer.COLUMN_DATABASE);
		cursor.close();
		
		return ret;
	}
	
	public synchronized static void insert(Context context, TblUser record) {
		UtilBoss.ConditionUtil.n(context, record);
		
		SQLiteDatabase db = SQLiteInstance.getInstance(context).get();
		ContentValues values = new ContentValues();
		values.put(TableBoss.toDatabaseName(TableBoss.TblUser.USER_ID), CodeBoss.MessCode.uuid());
		values.put(TableBoss.toDatabaseName(TableBoss.TblUser.USER_LOGNAME), record.getUserLogname());
		values.put(TableBoss.toDatabaseName(TableBoss.TblUser.USER_LOGPASSWORD), record.getUserLogpassword());
		values.put(TableBoss.toDatabaseName(TableBoss.TblUser.USER_CNNAME), record.getUserCnname());
		values.put(TableBoss.toDatabaseName(TableBoss.TblUser.USER_ENNAME), record.getUserEnname());
		values.put(TableBoss.toDatabaseName(TableBoss.TblUser.USER_INFO), record.getUserInfo());
		values.put(TableBoss.toDatabaseName(TableBoss.LAST_UPDATE_DATE), UtilBoss.DatetimeUtil.formatDate2Str(new Date(), ConstantsUtil.FORMAT_DATE_DATETIME));
		values.put(TableBoss.toDatabaseName(TableBoss.DELETED), "N");
		db.insert(TableBoss.toDatabaseName(TableBoss.TblUser.TAG), null, values);
	}

	private Context context; //上下文
	private Handler handler; //回调给UI线程操作
	
	private ArgumentCallback callback = new ArgumentCallback() { //在异线程的操作
		
		@Override
		public void action(Object... objs) {
			int from = (Integer) objs[0];
			SQLiteDatabase db = SQLiteInstance.getInstance(context).get();
			
			switch(from) {
				case TblUserOffline.LIST:
					Cursor cursor = db.query(TableBoss.toDatabaseName(TableBoss.TblUser.TAG), null, TableBoss.toDatabaseName(TableBoss.DELETED) + " = ?", new String[] { "N", }, null, null, null);
					List<TblUser> list = OTransfer.toList(cursor, TblUser.class, OTransfer.COLUMN_DATABASE);
					cursor.close();
					CodeBoss.MessCode.handlerMsg(handler, from, UtilBoss.ObjUtil.isNotNull(list) ? list : new ArrayList<TblUser>());
					break;
			}
		}
	};
	
	public static final int LIST = 0x001;
}
