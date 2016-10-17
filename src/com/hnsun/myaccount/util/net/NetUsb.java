package com.hnsun.myaccount.util.net;

import java.util.Map;

import com.hnsun.myaccount.model.NetModel;

/**
 * 网络访问必须方法
 * @author hnsun
 * @date 2016/09/15
 */
public interface NetUsb {

	public NetModel doGet(String strUrl, Map<String, Object> params); //Get方式访问
	
	public NetModel doPost(String strUrl, Map<String, Object> params); //Post方式访问
}
