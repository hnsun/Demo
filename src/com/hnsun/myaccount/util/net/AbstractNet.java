package com.hnsun.myaccount.util.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.model.NetModel;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.log.LogFactory;
import com.hnsun.myaccount.util.platform.ResUtil;

/**
 * 网络访问部分规范实现
 * @author hnsun
 * @date 2016/09/15
 */
public abstract class AbstractNet implements NetUsb {
	
	public AbstractNet(Context context) {
		if(UtilBoss.ObjUtil.isNull(context)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.context = context;
	}

	@Override
	public NetModel doGet(String strUrl, Map<String, Object> params) { //Get方式访问 
		UtilBoss.ConditionUtil.n(strUrl);
		NetModel ret = checkNet();
		if(UtilBoss.ObjUtil.isNotNull(ret)) return ret; //无网络
		else { //有网络
			ret = contentGet(paramsGet(strUrl, params));
		}
		
		return ret;
	}

	@Override
	public NetModel doPost(String strUrl, Map<String, Object> params) { //Post方式访问
		UtilBoss.ConditionUtil.n(strUrl);
		NetModel ret = checkNet();
		if(UtilBoss.ObjUtil.isNotNull(ret)) return ret; //无网络
		else { //有网络
			ret = contentPost(strUrl, params);
		}
		
		return ret;
	}
	
	protected abstract NetModel contentGet(String strUrl); //Get方式访问内容 不需处理参数
	
	protected abstract NetModel contentPost(String strUrl, Map<String, Object> params); //Post方式访问内容
	
	protected String paramsGet(String strUrl, Map<String, Object> params) { //Get方式的参数设置
		UtilBoss.ConditionUtil.n(strUrl);
		StringBuilder builder = new StringBuilder();

		builder.append(strUrl);
		if(!UtilBoss.IfUtil.isEmpty(params)) {
			try {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					builder.append("&" + entry.getKey());
					builder.append("=" + URLEncoder.encode(UtilBoss.StrUtil.null2Empty(entry.getValue()), ApplicationDatas.APP_ENCODING));
				}
			} catch (UnsupportedEncodingException e) {
				LogFactory.log().e(e, "不支持的编码");
			}
		}
		return builder.toString().replaceFirst("&", "?");
	}

	protected List<NameValuePair> paramsPost(Map<String, Object> params) { //Post方式的参数设置
		List<NameValuePair> ret = null;
		if(UtilBoss.IfUtil.isEmpty(params)) return ret;
		
		ret = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			ret.add(new BasicNameValuePair(entry.getKey(), UtilBoss.StrUtil.null2Empty(entry.getValue())));
		}
		return ret;
	}
	
	protected Context getContext() {
		return this.context;
	}
	
	private NetModel checkNet() { //是否有网络 有网络返回null
		NetModel ret = null;
		
		if(!SystemStatus.netState) {
			ret = new NetModel();
			ret.setRetIs(false).setRetFail(ResUtil.getText(getContext(), R.string.msg_net_disconnected));
		}
		return ret;
	}
	
	private Context context;
	
	protected static final int TIMEOUT_REQUEST = 15 * 1000;
	protected static final int TIMEOUT_CONNECTION = 15 * 1000;
}
