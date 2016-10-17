package com.hnsun.myaccount.util;

import java.io.File;
import java.util.UUID;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.business.account.AccountHandler;
import com.hnsun.myaccount.business.passcode.AppLockManager;
import com.hnsun.myaccount.mess.ArgumentCallback;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.model.dbo.TblUser;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.data.ResUuidName;
import com.hnsun.myaccount.util.file.FileUtil;
import com.hnsun.myaccount.util.file.PathUtil;
import com.hnsun.myaccount.util.platform.DensityUtil;
import com.hnsun.myaccount.util.platform.ResUtil;
import com.hnsun.myaccount.util.platform.ViewUtil;
import com.hnsun.myaccount.view.IfDialog;

/**
 * 默认且重复使用的代码帮助类
 * @author hnsun
 * @date 2016/08/15
 * 类型：MessCode、ViewCode、DataCode、
 * 区别：带有一定的自身业务逻辑
 */
public class CodeBoss {

	private CodeBoss() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /*************
     *****
     **        混杂代码 ：MessCode
     **
     *****
     *******************************************************/
	public static class MessCode {
		
		private MessCode() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 返回UUID
		 * @return
		 */
		public static String uuid() {
			return UUID.randomUUID().toString();
		}
		
		/**
		 * 退出应用 返回true 表示已处理，否者未处理
		 * @return
		 */
		public static boolean exitApplication() {
			boolean ret = true;
			
			switch(SystemStatus.exitStyle) {
				case ApplicationDatas.EXIT_DIALOG: break;
				case ApplicationDatas.EXIT_TOAST:
				default: ret = false; break;
			}
			
			return ret;
		}
		
		/**
		 * 异步消息传递
		 * @param handler
		 * @param from
		 * @param obj
		 */
		public static void handlerMsg(Handler handler, int from, Object obj) {
    		UtilBoss.ConditionUtil.n(handler);
    		
			Message msg = Message.obtain();
			msg.what = from;
			msg.obj = obj;
			handler.sendMessage(msg);
		}
		
		/**
		 * 是否为登陆状态否者清除处理
		 * @param context
		 * @return
		 */
		public static boolean accountState(Context context) {
			boolean ret = false;
    		UtilBoss.ConditionUtil.n(context);
    		
    		if(!(ret = AccountHandler.checkAccount(context))) { //未登录状态（没登陆过或过期）
    			CodeBoss.MessCode.logoutUser(context);
    		}
			
			return ret;
		}
		
		/**
		 * 退出登陆
		 * @param context
		 */
		public static void logoutUser(Context context) {
    		UtilBoss.ConditionUtil.n(context);
    		
    		SystemStatus.curAccount = null;
    		AccountHandler.removeAccount(context);
			if(AppLockManager.getInstance().isEnabled()) AppLockManager.getInstance().getAppLock().set(null);
		}
		
		/**
		 * 还没开发的功能
		 * @param context
		 */
		public static void development(Context context) {
    		UtilBoss.ConditionUtil.n(context);
    		
			ViewUtil.displayToast(context, R.string.msg_development);
		}
	
		/**
		 * 下载文件存在判断
		 * @param context
		 * @param fileName
		 */
		public static void downloadExist(Context context, String fileName, ArgumentCallback callback) {
    		UtilBoss.ConditionUtil.nt(context, fileName);

    		final String name = fileName;
    		final ArgumentCallback call = callback;
    		new IfDialog(context).show(ResUtil.getText(context, R.string.msg_file_again), new IfDialog.OnConfirmClickListener() {
				
				@Override
				public void onClick(View view) {
					FileUtil.deleteFile(PathUtil.SystemPath.get(PathUtil.SystemPath.DOWNLOAD) + PathUtil.separatorBefore(name));
					if(UtilBoss.ObjUtil.isNotNull(call)) call.action();
				}
			}, IfDialog.Type.WARNING);
		}
		
		/**
		 * 获得已登陆用户Id
		 * @param context
		 * @return
		 */
		public static String accountId(Context context) {
			String ret = null;
    		UtilBoss.ConditionUtil.n(context);

			if(CodeBoss.MessCode.accountState(context) && UtilBoss.ObjUtil.isNotNull(SystemStatus.curAccount)) { //已登陆
				ret = SystemStatus.curAccount.name;
			}
			return ret;
		}
		
		/**
		 * 获得已登陆用户Id
		 * @param context
		 * @return
		 */
		public static String accountName(Context context) {
			String ret = null;
    		UtilBoss.ConditionUtil.n(context);

			if(CodeBoss.MessCode.accountState(context) && UtilBoss.ObjUtil.isNotNull(SystemStatus.curAccount)) { //已登陆
				ret = SystemStatus.curAccount.name;
			}
			return ret;
		}
	}

    /*************
     *****
     **        视图代码 ：ViewCode
     **
     *****
     *******************************************************/
	public static class ViewCode {
		
		private ViewCode() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 密码是否可见状态
		 * @param et
		 * @param eye
		 * @param isVisible
		 */
		public static void etPasswordVisible(EditText et, View eye, boolean isVisible) {
    		UtilBoss.ConditionUtil.n(et, eye);
    		
			if(isVisible) { //密码可见
				et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
				eye.setBackgroundResource(R.drawable.et_ic_eye_on);
			} else { //密码不可见
				et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				eye.setBackgroundResource(R.drawable.et_ic_eye_normal);
			}
			if(UtilBoss.ObjUtil.isNotNull(SystemStatus.typeface)) et.setTypeface(SystemStatus.typeface); //改变状态会重置字体
			et.setSelection(et.getText().toString().length());
		}
		
		/**
		 * 填充数据并返回相应导航
		 * @param dataSource
		 * @param llGuide
		 * @param ress
		 * @return
		 */
		public static View[] dotsFill(Context context, SparseArray<View> dataSource, LinearLayout llGuide, int[] ress) {
			View[] ret = null;
    		UtilBoss.ConditionUtil.n(context, dataSource, llGuide, ress);
    		
			int margin = DensityUtil.dp2px(context, 2);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dp2px(context, 6), DensityUtil.dp2px(context, 6));
			layoutParams.setMargins(margin, margin, margin, margin);
			
			ret = new View[ress.length];
			for(int i = 0; i < ress.length; i++) {
				View view = new View(context);
				view.setBackgroundResource(ress[i]);
				dataSource.put(i, view);
				
				View dot = new View(context);
				if(i == 0) dot.setBackgroundResource(R.color.defaultTheme);
				else dot.setBackgroundResource(R.color.white);
				
				llGuide.addView(dot, layoutParams);
				ret[i] = dot;
			}
			return ret;
		}
	
		/**
		 * 根据字符串设置图片
		 * @param imageView
		 * @param str
		 */
		public static void userIcon(ImageView imageView, String str) {
    		UtilBoss.ConditionUtil.n(imageView);
			
    		if(UtilBoss.StrUtil.isEmpty(str)) imageView.setImageResource(R.drawable.ic_contact_robot); //机器人
    		else { //普通用户
    			String photo = new ResUuidName(str).gPhoto();
    			File file = new File(PathUtil.SystemPath.get(PathUtil.SystemPath.USER_IMAGE) + PathUtil.separatorBefore(photo));
    			if(file.exists()) imageView.setImageURI(Uri.fromFile(file));
    			else imageView.setImageResource(R.drawable.default_ic_user);
    		}
		}
		
		/**
		 * 根据类型设置图片
		 * @param imageView
		 * @param mine
		 */
		public static void imgFromMine(ImageView imageView, String mine) {
    		UtilBoss.ConditionUtil.nt(imageView, mine);
    		
    		if(mine.equals("png") || mine.equals("jpg")) {
    			imageView.setImageResource(R.drawable.img_mine_picture);
    		} else if(mine.equals("txt") || mine.equals("doc")) {
    			imageView.setImageResource(R.drawable.img_mine_document);
    		} else if(mine.equals("zip") || mine.equals("rar")) {
    			imageView.setImageResource(R.drawable.img_mine_parcel);
    		} else {
    			imageView.setImageResource(R.drawable.img_mine_default);
    		}
		}
	
		/**
		 * 选项卡按钮
		 * @param context
		 * @param name
		 * @return
		 */
		public static Button tabButton(Context context, String name) {
			Button ret = null;
    		UtilBoss.ConditionUtil.nt(context, name);
    		
			ret = new Button(context);
			ret.setFocusable(true);
			ret.setBackgroundResource(R.drawable.btn_incolor);
			ret.setTextColor(context.getResources().getColorStateList(R.drawable.txt_default));
			return ret;
		}
	}

    /*************
     *****
     **        数据代码 ：DataCode
     **
     *****
     *******************************************************/
	public static class DataCode {
		
		private DataCode() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 获取从服务器返回的许国际化的相关处理
		 * @param context
		 * @param res
		 * @return
		 */
		public static String i18nFromServer(Context context, String res) {
			String ret = null;
    		UtilBoss.ConditionUtil.nt(context, res);
    		
    		ret = ResUtil.getText(context, res.replaceAll("\\.", "_"));
    		return ret;
		}
		
		/**
		 * 获取当前的机器人对象
		 * @param context
		 * @return
		 */
		public static TblUser robotUser(Context context) {
			TblUser ret = null;
    		UtilBoss.ConditionUtil.nt(context);

    		ret = new TblUser();
    		ret.setUserLogname(ResUtil.getText(context, R.string.app_name));
    		
    		return ret;
		}
	}
}
