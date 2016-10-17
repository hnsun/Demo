package com.hnsun.myaccount.model.dbo;

import java.io.Serializable;
import java.util.Date;

/**
 * 下载记录
 * @author hnsun
 * @date 2016/10/05
 */
public class PlfDownloadrecord implements Serializable {

	public PlfDownloadrecord() {}

	public String getDownloadrecordId() {
		return downloadrecordId;
	}

	public void setDownloadrecordId(String downloadrecordId) {
		this.downloadrecordId = downloadrecordId;
	}

	public Date getDownloadrecordDate() {
		return downloadrecordDate;
	}

	public void setDownloadrecordDate(Date downloadrecordDate) {
		this.downloadrecordDate = downloadrecordDate;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	private String downloadrecordId;
	private Date downloadrecordDate;
	private String fileId;
	
	private static final long serialVersionUID = 1L;
}
