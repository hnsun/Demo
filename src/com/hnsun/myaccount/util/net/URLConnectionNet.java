package com.hnsun.myaccount.util.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.Build;

import com.hnsun.myaccount.model.NetModel;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.IOUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.log.LogFactory;
import com.hnsun.myaccount.util.platform.AppUtil;

/**
 * HttpURLConnection网络访问
 * @author hnsun
 * @date 2016/09/13
 */
public class URLConnectionNet extends AbstractNet {
	
	public URLConnectionNet(Context context) {
		super(context);
		fixVersionBug();
	}
	
	@Override
	protected NetModel contentGet(String strUrl) { //Get方式内容填充
		UtilBoss.ConditionUtil.n(strUrl);
		NetModel ret = new NetModel();

		HttpURLConnection conn = null;
		try {
			conn = connection(ConstantsUtil.PRONOUN_HTTP_GET, strUrl);
			
			conn.connect();
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//				String message = conn.getResponseMessage();
				ret.setRetIs(true).setRetObj(IOUtil.convertStreamToString(conn.getInputStream()));
			} else ret.setRetIs(false);
		} catch (IOException e) {
			ret.setRetIs(false).setRetFail(e.getMessage());
			LogFactory.log().e(e, "输入输出有问题");
		} finally {
			if(UtilBoss.ObjUtil.isNotNull(conn)) conn.disconnect();
		}
		
		return ret;
	}

	@Override
	protected NetModel contentPost(String strUrl, Map<String, Object> params) { //Post方式内容填充
		UtilBoss.ConditionUtil.n(strUrl);
		NetModel ret = new NetModel();
		
		HttpURLConnection conn = null;
		try {
			conn = connection(ConstantsUtil.PRONOUN_HTTP_POST, strUrl);
			postParams(conn.getOutputStream(), paramsPost(params));
			
			conn.connect();
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//				String message = conn.getResponseMessage();
				ret.setRetIs(true).setRetObj(IOUtil.convertStreamToString(conn.getInputStream()));
			} else ret.setRetIs(false);
		} catch (IOException e) {
			ret.setRetIs(false).setRetFail(e.getMessage());
			LogFactory.log().e(e, "输入输出有问题");
		} finally {
			if(UtilBoss.ObjUtil.isNotNull(conn)) conn.disconnect();
		}
		
		return ret;
	}

	private void fixVersionBug() { //SDK2.2之前的一个bug解决方案
		if(AppUtil.getVersionSDK() < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}
	
	private HttpURLConnection connection(String method, String strUrl) { //链接
		HttpURLConnection ret = null;
		UtilBoss.ConditionUtil.n(method, strUrl);
		
		try {
			URL url = new URL(strUrl);
			ret = (HttpURLConnection) url.openConnection();
			ret.setConnectTimeout(AbstractNet.TIMEOUT_CONNECTION); //链接时间
			ret.setReadTimeout(AbstractNet.TIMEOUT_REQUEST); //请求时间
			ret.setRequestProperty("Connection", "Keep-Alive"); //Header
			ret.setRequestProperty("RequestType", "Android"); //Header
			ret.setRequestProperty("phoneUniqueCode", SystemStatus.uniqueCode); //Header

			ret.setRequestMethod(method);
			if(ConstantsUtil.PRONOUN_HTTP_POST.equals(method)) {
				ret.setDoInput(true); //接收输入流
				ret.setDoOutput(true); //传递参数
			}
		} catch (MalformedURLException e) {
			LogFactory.log().e(e, "不正确的链接");
		} catch (IOException e) {
			LogFactory.log().e(e, "输入输出有问题");
		}
		
		return ret;
	}
	
	private void postParams(OutputStream stream, List<NameValuePair> params) { //参数进一步处理
		BufferedWriter writer = null;
		try {
			try {
				StringBuilder builder = new StringBuilder();
				for (NameValuePair param : params) {
					if(!UtilBoss.StrUtil.isEmpty(builder.toString())) builder.append("&");
					
					builder.append(URLEncoder.encode(param.getName(), ApplicationDatas.APP_ENCODING));
					builder.append("=");
					builder.append(URLEncoder.encode(param.getValue(), ApplicationDatas.APP_ENCODING));
				}
				writer = new BufferedWriter(new OutputStreamWriter(stream, ApplicationDatas.APP_ENCODING));
				writer.write(builder.toString());
				writer.flush();
			} finally {
				if(UtilBoss.ObjUtil.isNotNull(writer)) writer.close();
			}
		} catch (UnsupportedEncodingException e) {
			LogFactory.log().e(e, "不支持的编码");
		} catch (IOException e) {
			LogFactory.log().e(e, "输入输出有问题");
		}
	}
}
