package com.hnsun.myaccount.util.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.hnsun.myaccount.model.BothModel;
import com.hnsun.myaccount.model.NetModel;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.log.LogFactory;
import com.hnsun.myaccount.util.platform.SharedPreferencesUtil;

/**
 * Apache的HttpClient实现
 * @author hnsun
 * @date 2016/09/13
 * SDK6.0之后给删除，需要引入org.apache.http.legacy.jar
 * httpcore-4.4.1.jar\httpmime-4.5.jar
 */
@SuppressWarnings("deprecation")
public class ApacheNet extends AbstractNet {
	
	public ApacheNet(Context context) {
		super(context);
	}

	@Override
	protected NetModel contentGet(String strUrl) { //GET HTTP1.1之前长度限制2048之后无限制
		UtilBoss.ConditionUtil.n(strUrl);
		NetModel ret = new NetModel();

		DefaultHttpClient client = client();
		HttpGet request = new HttpGet(strUrl);
		request.setHeaders(headers);
		
		try {
			ApacheNet.Cookies cookies = new ApacheNet.Cookies(getContext());
			client.setCookieStore(cookies.getCookie());
			
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if(isDownload) {
					HttpEntity entity = response.getEntity();
					ret.setRetIs(true).setRetStream(new BothModel<Long, InputStream>(entity.getContentLength(), entity.getContent()));
				} else ret.setRetIs(true).setRetObj(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
				cookies.setCookie(client.getCookieStore()); //将最新Cookie设置进去
			} else {
				ret.setRetIs(false).setRetFail(response.getStatusLine().toString());
			}
		} catch (ClientProtocolException e) {
			ret.setRetIs(false).setRetFail(e.getMessage());
			LogFactory.log().e(e, "处理端协议错误");
		} catch (IOException e) {
			ret.setRetIs(false).setRetFail(e.getMessage());
			LogFactory.log().e(e, "输入输出有问题");
		} finally {
			request.abort();
			client.getConnectionManager().shutdown(); //销毁Client
		}
		
		return ret;
	}

	@Override
	protected NetModel contentPost(String strUrl, Map<String, Object> params) { //POST
		UtilBoss.ConditionUtil.n(strUrl);
		NetModel ret = new NetModel();

		DefaultHttpClient client = client();
		HttpPost request = new HttpPost(strUrl);
		request.setHeaders(headers);
		
		try {
			ApacheNet.Cookies cookies = new ApacheNet.Cookies(getContext());
			client.setCookieStore(cookies.getCookie());
			if(!UtilBoss.IfUtil.isEmpty(params)) {
				HttpEntity entity = SystemStatus.netOldEntity ? entityOld(params) : entityNew(params);
				if(UtilBoss.ObjUtil.isNotNull(entity)) request.setEntity(entity);
			}
			
			HttpResponse response = client.execute(request); //执行
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if(isDownload) { //返回数据处理
					HttpEntity entity = response.getEntity();
					ret.setRetIs(true).setRetStream(new BothModel<Long, InputStream>(entity.getContentLength(), entity.getContent()));
				} else ret.setRetIs(true).setRetObj(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
				cookies.setCookie(client.getCookieStore());
			} else ret.setRetIs(false).setRetFail(response.getStatusLine().toString());
		} catch (ClientProtocolException e) {
			ret.setRetIs(false).setRetFail(e.getMessage());
			LogFactory.log().e(e, "处理端协议错误");
		} catch (IOException e) {
			ret.setRetIs(false).setRetFail(e.getMessage());
			LogFactory.log().e(e, "输入输出有问题");
		} finally {
			request.abort();
			client.getConnectionManager().shutdown(); //销毁Client
		}
		
		return ret;
	}
	
	public ApacheNet download(boolean isDownload) {
		this.isDownload = isDownload;
		return this;
	}

	private DefaultHttpClient client() {
		DefaultHttpClient ret = null;
		
		HttpParams params = new BasicHttpParams(); //参数
		HttpConnectionParams.setConnectionTimeout(params, AbstractNet.TIMEOUT_CONNECTION); //连接超时
		HttpConnectionParams.setSoTimeout(params, AbstractNet.TIMEOUT_REQUEST); //请求超时
		HttpConnectionParams.setTcpNoDelay(params, true); //不延时
		HttpConnectionParams.setSocketBufferSize(params, 8 * 1024); //缓存
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1); //http版本
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8); //编码格式
		HttpProtocolParams.setUseExpectContinue(params, true); //持续握手
		HttpClientParams.setRedirecting(params, true); //定向
		
		ret = new DefaultHttpClient(params);
		ret.setHttpRequestRetryHandler(new HttpRequestRetryHandler() { //异常自动恢复处理
		
			@Override
			public boolean retryRequest(IOException e, int executionCount, HttpContext context) {
				if(executionCount >= 3) return false; //发生异常时 重试3次以上就不要试了
				
				if(e instanceof NoHttpResponseException) return true; //服务器丢掉连接，重试
				if(e instanceof SSLHandshakeException) return false; //SSL握手异常，不重试
				
				HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
				if(request instanceof HttpEntityEnclosingRequest) return false; //请求是被认为不是幂等的，不重试 
				
				return true;
			}
		});
		
		return ret;
	}

	private HttpEntity entityNew(Map<String, Object> params) { //新方式POST参数内容获取
		HttpEntity ret = null;
		if(UtilBoss.IfUtil.isEmpty(params)) return ret;

		try {
			boolean fileFlag = false; //是否有文件上传
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setCharset(Charset.forName(HTTP.UTF_8));
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); //兼容模式
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				File file = new File(UtilBoss.StrUtil.null2Empty(entry.getValue()));
				if(file.isFile()) {
					fileFlag = true;
					builder.addPart(entry.getKey(), new FileBody(file)); //entry.getKey():"_mime_xxx" jpg\doc...
				} else builder.addTextBody(entry.getKey(), UtilBoss.StrUtil.null2Empty(entry.getValue()));
			}
			
			if(fileFlag) ret = builder.build(); //文件上传
			else {
				ret = new UrlEncodedFormEntity(paramsPost(params), HTTP.UTF_8);
			}
		} catch (UnsupportedEncodingException e) {
			LogFactory.log().e(e, "不支持的编码");
		}
		return ret;
	}
	
	private HttpEntity entityOld(Map<String, Object> params) { //旧方式POST参数内容获取
		HttpEntity ret = null;
		if(UtilBoss.IfUtil.isEmpty(params)) return ret;
		
		try {
			boolean fileFlag = false; //是否有文件上传
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				ContentBody body = null;
				File file = new File(UtilBoss.StrUtil.null2Empty(entry.getValue()));
				if(file.isFile()) {
					fileFlag = true;
					body = new FileBody(file);
				} else body = new StringBody(UtilBoss.StrUtil.null2Empty(entry.getValue()), Charset.forName(HTTP.UTF_8));
				entity.addPart(entry.getKey(), body);
			}
			
			if(fileFlag) ret = entity; //文件上传
			else {
				ret = new UrlEncodedFormEntity(paramsPost(params), HTTP.UTF_8);
			}
		} catch (UnsupportedEncodingException e) {
			LogFactory.log().e(e, "不支持的编码");
		}
		
		return ret;
	}
	
	private boolean isDownload; //是否有文件下载
	private Header[] headers; //Http提示头信息
	
	{
		this.isDownload = false;
		this.headers = new Header[] { 
				new BasicHeader("Connection", "Keep-Alive"), //长时间大处理 减少多访问
				new BasicHeader("RequestType", "Android"), //请求类型
				new BasicHeader("phoneUniqueCode", SystemStatus.uniqueCode), //设备信息
		};
	}
	
	/**
	 * cookie
	 * @author hnsun
	 * @date 2016/09/15
	 */
	public static class Cookies {
	
		public Cookies(Context context) { this.context = context; }

		public CookieStore getCookie() {
			if(UtilBoss.ObjUtil.isNull(cookies)) load();
			return cookies;
		}
	
		public void setCookie(CookieStore cookie) {
			this.cookies = cookie;
			save(cookie);
		}
	
		private void load() { //加载
			cookies = new BasicCookieStore();
			
			SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, SharedPreferencesUtil.FILENAME_NET_CONFIG);
			Map<String, ?> map = sharedPreferencesUtil.loadAll();
			for(String key : map.keySet()) {
				if(UtilBoss.StrUtil.isEmpty(key)) continue;

				if(key.indexOf("_") < 0) { //单个Cookie
					BasicClientCookie cookie = new BasicClientCookie(key, sharedPreferencesUtil.lockLoad(key));
					cookie.setVersion(Integer.parseInt(sharedPreferencesUtil.lockLoad(key + "_" + SharedPreferencesUtil.KEY_NET_COOKIE_VERSION)));
					cookie.setPath(sharedPreferencesUtil.lockLoad(key + "_" + SharedPreferencesUtil.KEY_NET_COOKIE_PATH));
					cookie.setDomain(sharedPreferencesUtil.lockLoad(key + "_" + SharedPreferencesUtil.KEY_NET_COOKIE_DOMAIN));
					String expiryDate = sharedPreferencesUtil.lockLoad(key + "_" + SharedPreferencesUtil.KEY_NET_COOKIE_EXPIRY_DATE);
					if (!UtilBoss.StrUtil.isEmpty(expiryDate)) cookie.setExpiryDate(UtilBoss.DatetimeUtil.formatStr2Date(expiryDate, ConstantsUtil.FORMAT_DATE_DATETIME));

					cookies.addCookie(cookie);
				}
			}
		}
	
		private void save(CookieStore cookies) { //保存
			SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, SharedPreferencesUtil.FILENAME_NET_CONFIG);
			sharedPreferencesUtil.removeAll();
	
			List<Cookie> list = cookies.getCookies();
			if(!UtilBoss.IfUtil.isEmpty(list)) {
				for(Cookie cookie : list) {
					sharedPreferencesUtil.lockPut(cookie.getName(), cookie.getValue());
					sharedPreferencesUtil.lockPut(cookie.getName() + "_" + SharedPreferencesUtil.KEY_NET_COOKIE_VERSION, "" + cookie.getVersion());
					sharedPreferencesUtil.lockPut(cookie.getName() + "_" + SharedPreferencesUtil.KEY_NET_COOKIE_PATH, cookie.getPath());
					sharedPreferencesUtil.lockPut(cookie.getName() + "_" + SharedPreferencesUtil.KEY_NET_COOKIE_DOMAIN, cookie.getDomain());
	
					Date expiryDate = cookie.getExpiryDate();
					if(UtilBoss.ObjUtil.isNull(expiryDate)) {} //sharedPreferencesUtil.lockPut(cookie.getName() + "_" + SharedPreferencesUtil.KEY_NET_COOKIE_EXPIRY_DATE, "");
					else {
						String strDate = UtilBoss.DatetimeUtil.formatDate2Str(expiryDate, ConstantsUtil.FORMAT_DATE_DATETIME);
						sharedPreferencesUtil.lockPut(cookie.getName() + "_" + SharedPreferencesUtil.KEY_NET_COOKIE_EXPIRY_DATE, strDate);
					}
				}
			}
		}
		
		private Context context; //上下文对象
		private CookieStore cookies; //当前会话的Cookie
	}
}
