package com.hnsun.myaccount.util.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.hnsun.myaccount.model.DownloadModel;
import com.hnsun.myaccount.model.dbo.PlfDownloadthread;
import com.hnsun.myaccount.support.offline.PlfDownloadthreadOffline;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 下载器（单个对象下载）
 * @author hnsun
 * @date 2016/10/05
 */
public class Downloader {
	
	public Downloader(Context context, Handler handler, String name, String downloadUrl) {
		if(UtilBoss.ObjUtil.isNull(context) || UtilBoss.ObjUtil.isNull(handler)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		if(UtilBoss.StrUtil.isEmpty(name) || UtilBoss.StrUtil.isEmpty(downloadUrl)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.context = context;
		this.handler = handler;
		this.name = name;
		this.downloadUrl = downloadUrl;
		this.savedFor = PathUtil.SystemPath.get(PathUtil.SystemPath.DOWNLOAD) + PathUtil.separatorBefore(name);
	}

	/**
	 * 获得文件大小
	 * @param downloadUrl
	 * @return
	 */
	public static int getFileSize(String downloadUrl) { //获得文件大小
		int ret = 0;
		UtilBoss.ConditionUtil.n(downloadUrl);
		
		HttpURLConnection conn = null;
		try {
			URL url = new URL(downloadUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000); //链接时间
			conn.setRequestMethod(ConstantsUtil.PRONOUN_HTTP_GET);
			
			conn.connect();
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) ret = conn.getContentLength();
		} catch (IOException e) {
			LogFactory.log().e(e, "输入输出有问题");
		} finally {
			if(UtilBoss.ObjUtil.isNotNull(conn)) conn.disconnect();
		}
		
		return ret;
	}
	
	/**
	 * 将分为线程存入数据库
	 * @param context
	 * @param threadCount
	 * @param model
	 * @return
	 */
	public static List<PlfDownloadthread> toDatabase(Context context, int threadCount, DownloadModel model) { //将分为线程存入数据库
		List<PlfDownloadthread> ret = new ArrayList<PlfDownloadthread>();
		UtilBoss.ConditionUtil.n(context, model);
		if(PlfDownloadthreadOffline.existUrl(context, model.getUrl())) return ret;
		
		int fileSize = Downloader.getFileSize(model.getUrl());
		if(fileSize > 0) {
			model.setSize(fileSize);
			model.setCompletedSize(0);
			int range = model.getSize() / threadCount; //每个线程分量 
			for(int i = 0; i < threadCount; i++) { //最后一个线程可能不满或有多
				PlfDownloadthread record = new PlfDownloadthread(CodeBoss.MessCode.uuid(), model.getUrl(), i, i * range, (i + 1) * range - 1, 0);
				if(i == (threadCount - 1)) record.setDownloadthreadEnd(model.getSize() - 1);
				ret.add(record);
			}
			PlfDownloadthreadOffline.inserts(context, ret);
		}
		return ret;
	}
	
	public void fillDownloadInfo() { //获取并设置各线程处理的数据
		model.setName(name);
		model.setUrl(downloadUrl);
		
		if(PlfDownloadthreadOffline.existUrl(context, downloadUrl)) { //已经有过下载记录
			plfDownloadthreads = PlfDownloadthreadOffline.listUrl(context, downloadUrl);
			int size = 0, completedSize = 0;
			for(PlfDownloadthread plfDownloadthread : plfDownloadthreads) {
				size += plfDownloadthread.getDownloadthreadEnd() - plfDownloadthread.getDownloadthreadStart() + 1;
				completedSize += plfDownloadthread.getDownloadthreadCompleted();
			}
			model.setSize(size);
			model.setCompletedSize(completedSize);
			if(completedSize == size) plfDownloadthreads.clear();
		} else plfDownloadthreads = Downloader.toDatabase(context, threadCount, model);
	}
	
	public void download() { //下载
		if(UtilBoss.IfUtil.isEmpty(plfDownloadthreads)) return;
		if(state == Downloader.STATE_ING) return;
		if(!new File(savedFor).exists()) {
			RandomAccessFile rFile = null;
			try {
				new File(savedFor).createNewFile();
				rFile = new RandomAccessFile(savedFor, "rwd"); //要下载的时候创建
				rFile.setLength(model.getSize());
			} catch (IOException e) {
				LogFactory.log().e(e, "输入输出有问题");
			} finally {
				try {
					if(UtilBoss.ObjUtil.isNotNull(rFile)) rFile.close();
				} catch (IOException e) {
					LogFactory.log().e(e, "输入输出有问题");
				}
			}
		}
		
		state = Downloader.STATE_ING;
		for(PlfDownloadthread plfDownloadthread : plfDownloadthreads) { //多条线程处理
			new DownloadThread(plfDownloadthread).start();
		}
	}

	public void pause() { //暂停
		state = Downloader.STATE_PAUSE;
	}
	
	public void deleteFile() { //存在才删除
		FileUtil.deleteFile(savedFor);
	}
	
	public DownloadModel getModel() { //获得当前下载的信息
		return model;
	}

	public boolean state(int state) { //是否为当前状态
		return this.state == state;
	}
	
	private int state; //状态
	private int threadCount; //使用的线程数
	private String name; //文件名称
	private String downloadUrl; //下载地址
	private String savedFor; //保存的位置
	
	private Context context; //上下文
	private Handler handler; //界面数据处理
	private DownloadModel model; //总的下载数据信息封装
	private List<PlfDownloadthread> plfDownloadthreads; //下载数据的拆分
	
	public static final int STATE_INIT = 0x01; //还未开始下载
	public static final int STATE_ING = 0x02; //正在下载
	public static final int STATE_PAUSE = 0x03; //暂停状态
	
	{
		this.state = Downloader.STATE_INIT;
		this.threadCount = ApplicationDatas.DOWNLOAD_THREADCOUNT;
		this.model = new DownloadModel();
	}
	
	/**
	 * 下载线程类
	 * @author hnsun
	 * @date 2016/10/05
	 */
	private class DownloadThread extends Thread {
		
		public DownloadThread(PlfDownloadthread record) {
			this.record = record;
		}
		
		@Override
		public void run() {
			int start = record.getDownloadthreadStart() + record.getDownloadthreadCompleted();
			HttpURLConnection conn = null;
            RandomAccessFile rFile = null;
            InputStream is = null;
			
			try {
				URL url = new URL(record.getDownloadthreadUrl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5 * 1000); //链接时间
				conn.setRequestMethod(ConstantsUtil.PRONOUN_HTTP_GET);
				conn.setRequestProperty("Range", "bytes=" + start + "-" + record.getDownloadthreadEnd());
				conn.connect();
				if(conn.getResponseCode() == HttpURLConnection.HTTP_OK  || conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
					rFile = new RandomAccessFile(savedFor, "rwd");
					rFile.seek(start);
					
					is = conn.getInputStream();
					byte[] buffer = new byte[ConstantsUtil.BYTE_BUFFER_SIZE];
					int len = -1;
					while((len = is.read(buffer)) != -1) {
						rFile.write(buffer, 0, len);
						record.setDownloadthreadCompleted(record.getDownloadthreadCompleted() + len);
						PlfDownloadthreadOffline.update(context, record);
						CodeBoss.MessCode.handlerMsg(handler, len, record.getDownloadthreadUrl());
						if(state == Downloader.STATE_PAUSE) break;
					}
				}
			} catch (IOException e) {
				LogFactory.log().e(e, "输入输出有问题");
			} finally {
				try {
					if(UtilBoss.ObjUtil.isNotNull(is)) is.close();
					if(UtilBoss.ObjUtil.isNotNull(rFile)) rFile.close();
					if(UtilBoss.ObjUtil.isNotNull(conn)) conn.disconnect();
				} catch (IOException e) {
					LogFactory.log().e(e, "输入输出有问题");
				}
			}
		}
		
		private PlfDownloadthread record;
	}
}
