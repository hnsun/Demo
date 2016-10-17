package com.hnsun.myaccount.model;

/**
 * 下载相关信息
 * @author hnsun
 * @date 2016/10/05
 */
public class DownloadModel {

	public DownloadModel() {}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		
		DownloadModel model = (DownloadModel) obj;
		if(model.getUrl().equals(getUrl())) return true;
		
		return false;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCompletedSize() {
		return completedSize;
	}
	
	public DownloadModel setCompletedSize(int completedSize) {
		this.completedSize = completedSize;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public DownloadModel setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getUrl() {
		return url;
	}
	
	public DownloadModel setUrl(String url) {
		this.url = url;
		return this;
	}

	private int size; //大小
	private int completedSize; //已下载大小
	private String name; //文件名称
	private String url; //下载地址
}
