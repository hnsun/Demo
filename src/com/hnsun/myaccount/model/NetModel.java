package com.hnsun.myaccount.model;

import java.io.InputStream;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.json.GsonJson;

/**
 * 网络返回对象
 * @author hnsun
 * @date 2016/09/15
 */
public class NetModel {

	public NetModel() {}

	public NetModel(boolean retIs, String retFail) {
		this.retIs = retIs;
		this.retFail = retFail;
	}

	public NetModel(boolean retIs, Object retObj) {
		this.retIs = retIs;
		this.retObj = retObj;
	}

	public NetModel(boolean retIs, BothModel<Long, InputStream> retStream) {
		this.retIs = retIs;
		this.retStream = retStream;
	}

	public boolean isRetIs() {
		return retIs;
	}
	
	public NetModel setRetIs(boolean retIs) {
		this.retIs = retIs;
		return this;
	}
	
	public String getRetFail() {
		return retFail;
	}

	public NetModel setRetFail(String retFail) {
		this.retFail = retFail;
		return this;
	}

	public Object getRetObj() {
		return retObj;
	}
	
	public NetModel.NetResult getRetObjJsonNetResult() {
		NetModel.NetResult ret = null;
		if(UtilBoss.StrUtil.isEmpty(UtilBoss.StrUtil.getByObj(getRetObj()))) return ret;
		
		ret = new GsonJson().from(UtilBoss.StrUtil.getByObj(getRetObj()), NetModel.NetResult.class);
		return ret;
	}
	
	public <T> T getRetObjJsonObj(Class<T> clazz) {
		T ret = null;
		UtilBoss.ConditionUtil.n(clazz);
		if(UtilBoss.StrUtil.isEmpty(UtilBoss.StrUtil.getByObj(getRetObj()))) return ret;
		
		ret = new GsonJson().from(UtilBoss.StrUtil.getByObj(getRetObj()), clazz);
		return ret;
	}

	public <T> List<T> getRetObjJsonList(TypeToken<List<T>> typeToken) {
		List<T> ret = null;
		UtilBoss.ConditionUtil.n(typeToken);
		if(UtilBoss.StrUtil.isEmpty(UtilBoss.StrUtil.getByObj(getRetObj()))) return ret;
		
		ret = new GsonJson().from(UtilBoss.StrUtil.getByObj(getRetObj()), typeToken);
		return ret;
	}

	public NetModel setRetObj(Object retObj) {
		this.retObj = retObj;
		return this;
	}

	public BothModel<Long, InputStream> getRetStream() {
		return retStream;
	}

	public NetModel setRetStream(BothModel<Long, InputStream> retStream) {
		this.retStream = retStream;
		return this;
	}

	private boolean retIs; //成功与否
	private String retFail; //不成功，用于信息
	private Object retObj; //可能的普通对象，用于信息
	private BothModel<Long, InputStream> retStream; //流及其长度，用于下载
	
	{
		this.retIs = false;
	}
	
	/**
	 * 结果对象
	 * @author hnsun
	 * @date 2016/09/21
	 */
	public static class NetResult {

		public String getnObject() {
			return nObject;
		}
		
		public void setnObject(String nObject) {
			this.nObject = nObject;
		}
		
		public String getnJson() {
			return nJson;
		}
		
		public <T> T getnJsonObj(Class<T> clazz) {
			T ret = null;
			UtilBoss.ConditionUtil.n(clazz);
			if(UtilBoss.StrUtil.isEmpty(UtilBoss.StrUtil.getByObj(nJson))) return ret;
			
			ret = new GsonJson().from(UtilBoss.StrUtil.getByObj(nJson), clazz);
			return ret;
		}
		
		public void setnJson(String nJson) {
			this.nJson = nJson;
		}
		
		public String getnAction() {
			return nAction;
		}
		
		public void setnAction(String nAction) {
			this.nAction = nAction;
		}
		
		public String getnMsg() {
			return nMsg;
		}
		
		public void setnMsg(String nMsg) {
			this.nMsg = nMsg;
		}
		
		public String getnStatus() {
			return nStatus;
		}
		
		public void setnStatus(String nStatus) {
			this.nStatus = nStatus;
		}
		
		private String nObject;
		private String nJson;
		private String nAction;
		private String nMsg;
		private String nStatus;
	}
}
