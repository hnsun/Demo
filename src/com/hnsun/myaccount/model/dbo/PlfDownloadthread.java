package com.hnsun.myaccount.model.dbo;

import java.io.Serializable;

/**
 * 下载线程管理
 * @author hnsun
 * @date 2016/10/03
 */
public class PlfDownloadthread implements Serializable {

	public PlfDownloadthread() {}

	public PlfDownloadthread(String downloadthreadId, String downloadthreadUrl,
			Integer downloadthreadThread, Integer downloadthreadStart,
			Integer downloadthreadEnd, Integer downloadthreadCompleted) {
		this.downloadthreadId = downloadthreadId;
		this.downloadthreadUrl = downloadthreadUrl;
		this.downloadthreadThread = downloadthreadThread;
		this.downloadthreadStart = downloadthreadStart;
		this.downloadthreadEnd = downloadthreadEnd;
		this.downloadthreadCompleted = downloadthreadCompleted;
	}

	public String getDownloadthreadId() {
		return downloadthreadId;
	}

	public void setDownloadthreadId(String downloadthreadId) {
		this.downloadthreadId = downloadthreadId;
	}

	public String getDownloadthreadUrl() {
		return downloadthreadUrl;
	}

	public void setDownloadthreadUrl(String downloadthreadUrl) {
		this.downloadthreadUrl = downloadthreadUrl;
	}

	public Integer getDownloadthreadThread() {
		return downloadthreadThread;
	}

	public void setDownloadthreadThread(Integer downloadthreadThread) {
		this.downloadthreadThread = downloadthreadThread;
	}

	public Integer getDownloadthreadStart() {
		return downloadthreadStart;
	}

	public void setDownloadthreadStart(Integer downloadthreadStart) {
		this.downloadthreadStart = downloadthreadStart;
	}

	public Integer getDownloadthreadEnd() {
		return downloadthreadEnd;
	}

	public void setDownloadthreadEnd(Integer downloadthreadEnd) {
		this.downloadthreadEnd = downloadthreadEnd;
	}

	public Integer getDownloadthreadCompleted() {
		return downloadthreadCompleted;
	}

	public void setDownloadthreadCompleted(Integer downloadthreadCompleted) {
		this.downloadthreadCompleted = downloadthreadCompleted;
	}

	private String downloadthreadId;
	private String downloadthreadUrl;
	private Integer downloadthreadThread;
	private Integer downloadthreadStart;
	private Integer downloadthreadEnd;
	private Integer downloadthreadCompleted;
	
	private static final long serialVersionUID = 1L;
}
