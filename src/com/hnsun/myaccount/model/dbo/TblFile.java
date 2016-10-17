package com.hnsun.myaccount.model.dbo;

import java.io.Serializable;
import java.util.Date;

import com.hnsun.myaccount.util.data.json.NotSerialAnnotation;

/**
 * 文件
 * @author hnsun
 * @date 2016/10/05
 */
public class TblFile implements Serializable {

	public TblFile() {}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public char getFilePlatform() {
		return filePlatform;
	}

	public void setFilePlatform(char filePlatform) {
		this.filePlatform = filePlatform;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public char getDeleted() {
		return deleted;
	}

	public void setDeleted(char deleted) {
		this.deleted = deleted;
	}

	private String fileId;
	private String fileName;
	private char filePlatform;
	private String fileUrl;
	private Date fileDate;
	private String fileDesc;
	
	@NotSerialAnnotation private Date lastUpdateDate;
	@NotSerialAnnotation private char deleted;
	
	private static final long serialVersionUID = 1L;
	
	{
		this.deleted = 'N';
	}
}
