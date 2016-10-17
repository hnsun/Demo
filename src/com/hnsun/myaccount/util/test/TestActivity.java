package com.hnsun.myaccount.util.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.util.IOUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.log.LogFactory;
import com.hnsun.myaccount.util.net.ApacheNet;
import com.hnsun.myaccount.util.net.URLConnectionNet;
import com.hnsun.myaccount.util.platform.ViewUtil;

/**
 * 测试界面
 * @author hnsun
 * @date 2016/08/30
 */
public class TestActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_test);
		getActionBar().setTitle(R.string.btn_test);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		vLayer = findViewById(R.id.vLayer);
		txtvShow = (TextView) findViewById(R.id.txtvShow);
		ivShow = (ImageView) findViewById(R.id.ivShow);
		wvShow = (WebView) findViewById(R.id.wvShow);
		
		btnTest1 = (Button) findViewById(R.id.btnTest1);
		btnTest2 = (Button) findViewById(R.id.btnTest2);
		btnTest3 = (Button) findViewById(R.id.btnTest3);
		btnTest4 = (Button) findViewById(R.id.btnTest4);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		setExit(false);
		views = new View[] { vLayer, txtvShow, ivShow, wvShow };
		ViewUtil.setVisibility(View.GONE, views);

		txtvShow.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动  
		txtvShow.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		ViewUtil.displayToast(instance, "测试页面");
		vLayer.setOnClickListener(onClickListener);
		txtvShow.setOnClickListener(onClickListener);
		ivShow.setOnClickListener(onClickListener);
		wvShow.setOnClickListener(onClickListener);
		
		btnTest1.setOnClickListener(onClickListener);
		btnTest2.setOnClickListener(onClickListener);
		btnTest3.setOnClickListener(onClickListener);
		btnTest4.setOnClickListener(onClickListener);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch(item.getItemId()) {
			case android.R.id.home: finish(); break;
			default : ret = super.onOptionsItemSelected(item); break;
		}
		return ret;
	}

	private Context instance;
	private View[] views;

	private View vLayer; //掩盖层
	private TextView txtvShow; //文字展示
	private ImageView ivShow; //图片展示
	private WebView wvShow; //网页展示
	
	private Button btnTest1; //从网络中获取一张图片 成功
	private Button btnTest2; //单纯显示html网页，即不含js，css等的 成功
	private Button btnTest3; //从网络中获取网页展示，含js，css等 数据获取成功显示不是很成功
	private Button btnTest4; //从HttpClient和HttpURLConnection中获取数据 成功
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnTest1:
					ViewUtil.setVisibility(View.VISIBLE, vLayer, ivShow);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							InputStream stream = null;
							try {
								URL url = new URL("http://img5.duitang.com/uploads/item/201601/09/20160109192452_yFtwV.thumb.700_0.jpeg");
								stream = url.openStream();
								final Bitmap bitmap = BitmapFactory.decodeStream(stream);
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										ivShow.setImageBitmap(bitmap);
									}
								});
							} catch (MalformedURLException e) {
								LogFactory.log().e(e);
							} catch (IOException e) {
								LogFactory.log().e(e);
							} finally {
								try {
									if(UtilBoss.ObjUtil.isNotNull(stream)) stream.close();
								} catch (IOException e) {
									LogFactory.log().e(e);
								}
							}
						}
					}).start();
					break;
				case R.id.btnTest2:
					ViewUtil.setVisibility(View.VISIBLE, vLayer, txtvShow);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							String content = "<html><body><p>这是标题</p><img src='http://p3.so.qhmsg.com/sdr/1365_768_/t015ba1cfaeb51179fd.jpg' /></body></html>";
							final Spanned spanned = Html.fromHtml(content, new Html.ImageGetter() {
								
								@Override
								public Drawable getDrawable(String source) {
									Drawable drawable = null;
									try {
										URL url = new URL(source);
										drawable = Drawable.createFromStream(url.openStream(), "");
										drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
									} catch (MalformedURLException e) {
										LogFactory.log().e(e);
									} catch (IOException e) {
										LogFactory.log().e(e);
									}
									return drawable;
								}
							}, null);
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									txtvShow.setText(spanned);
								}
							});
						}
					}).start();
					txtvShow.scrollTo(0, 0);
					break;
				case R.id.btnTest3:
					ViewUtil.setVisibility(View.VISIBLE, vLayer, wvShow);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								URL url = new URL("https://www.baidu.com/");
								InputStream is = url.openConnection().getInputStream();
								final String content = IOUtil.convertStreamToString(is);
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
//										String data = content.replaceAll("%", "%25").replaceAll("#", "%23").replaceAll("\\", "%27").replaceAll("?", "%3f"); //数据里面特殊符号
										wvShow.loadDataWithBaseURL("https://www.baidu.com/", content, "text/html", ApplicationDatas.APP_ENCODING, null); //图片显示
//										wvShow.getSettings().setDefaultTextEncodingName(ApplicationDatas.APP_ENCODING); //网页内容编码方式
//										wvShow.loadData(content, "text/html", ApplicationDatas.APP_ENCODING); //单显示 乱码
//										txtvShow.setText(content); //未经转化的html
									}
								});
							} catch (MalformedURLException e) {
								LogFactory.log().e(e);
							} catch (IOException e) {
								LogFactory.log().e(e);
							}
						}
					}).start();
					break;
				case R.id.btnTest4:
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							new ApacheNet(instance).doGet("http://www.baidu.com/", null);
							new URLConnectionNet(instance).doGet("http://www.baidu.com/", null);
						}
					}).start();
					break;
				default: ViewUtil.setVisibility(View.GONE, views); break;
			}
		}
	};
	
	{
		this.instance = TestActivity.this;
	}
}
