package com.hnsun.myaccount.util.platform;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 资源相关（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class ResUtil {

    private ResUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /**
     * 获取资源 字符串
     * @param context
     * @param res
     * @return
     */
    public static String getText(Context context, String res) {
    	String ret = null;
		UtilBoss.ConditionUtil.nt(context, res);
		
		Resources resources = context.getResources();
		int indentify = resources.getIdentifier(res, "string", context.getPackageName());
		if(indentify > 0) ret = ResUtil.getText(context, indentify);
        return ret;
    }

    /**
     * 获取资源 字符串
     * @param context
     * @param res
     * @return
     */
    public static String getText(Context context, int res) {
		UtilBoss.ConditionUtil.n(context);
        return (String) context.getResources().getText(res);
    }

    /**
     * 获取资源 字符串 含冒号
     * @param context
     * @param res
     * @return
     */
    public static String getTextColon(Context context, int res) {
		UtilBoss.ConditionUtil.n(context);
        return (String) context.getResources().getText(res) + "：";
    }

    /**
     *  获取资源 颜色
     * @param context
     * @param res
     * @return
     */
    public static int getColor(Context context, int res) {
		UtilBoss.ConditionUtil.n(context);
        return context.getResources().getColor(res);
    }

    /**
     *  获取资源 颜色
     * @param context
     * @param res
     * @return
     */
    public static ColorStateList getColorState(Context context, int res) {
		UtilBoss.ConditionUtil.n(context);
        return context.getResources().getColorStateList(res);
    }

    /**
     * 获取资源 颜色 以 #******的形式
     * @param context
     * @param res
     * @return
     */
    public static String getColorHexa(Context context, int res) {
		UtilBoss.ConditionUtil.n(context);
        return String.format("#%X", getColor(context, res));
    }

    /**
     * 获取资源 长度
     * @param context
     * @param res
     * @return
     */
    public static float getDimen(Context context, int res) {
		UtilBoss.ConditionUtil.n(context);
        return  context.getResources().getDimension(res);
    }

    /**
     * 获取资源 图片
     * @param context
     * @param res
     * @return
     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, int res) {
		UtilBoss.ConditionUtil.n(context);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return context.getResources().getDrawable(res, null);
//        } else {
        return context.getResources().getDrawable(res);
//        }
    }

    /**
     * 获取资源 图片 以一定的宽高输出
     * @param context
     * @param res
     * @param width
     * @param height
     * @return
     */
    @SuppressWarnings("deprecation")
	public static Drawable getDrawable(Context context, int res, int width, int height) {
		UtilBoss.ConditionUtil.n(context);
		
        Drawable drawable = getDrawable(context, res);
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap oldbmp = PhotoUtil.drawableToBitmap(drawable); //drawable转换成bitmap
        Matrix matrix = new Matrix(); //创建操作图片用的Matrix对象
        //计算缩放比例
        float sx = ((float) width / w);
        float sy = ((float) height / h);
        matrix.postScale(sx, sy); //设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, w, h, matrix, true); //建立新的bitmap，其内容是对原bitmap的缩放后的图
        return new BitmapDrawable(newbmp);
    }

    /*************
     *****
     **        获取TypedArray的相关值
     **
     *****
     *******************************************************/
    public static class FromAttrs {
		
		private FromAttrs() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 获取字符串
		 * @param context
		 * @param typedArray
		 * @param index
		 * @return
		 */
		public static CharSequence text(Context context, TypedArray typedArray, int index) {
			CharSequence ret = null;
			UtilBoss.ConditionUtil.n(context, typedArray);
			
			int resId = typedArray.getResourceId(index, 0);
			if(resId == 0) ret = typedArray.getText(index);
			else ret = ResUtil.getText(context, resId);
			
			return ret;
		}

		/**
		 * 获取颜色
		 * @param context
		 * @param typedArray
		 * @param index
		 * @return
		 */
		public static int color(Context context, TypedArray typedArray, int index) {
			int ret = ResUtil.FromAttrs.DEFAULT_COLOR;
			UtilBoss.ConditionUtil.n(context, typedArray);
			
			int resId = typedArray.getResourceId(index, 0);
			if(resId == 0) ret = typedArray.getColor(index, ResUtil.FromAttrs.DEFAULT_COLOR);
			else ret = ResUtil.getColor(context, resId);
			
			return ret;
		}

		/**
		 * 获取尺寸
		 * @param context
		 * @param typedArray
		 * @param index
		 * @return
		 */
		public static float dimension(Context context, TypedArray typedArray, int index) {
			float ret = 0;
			UtilBoss.ConditionUtil.n(context, typedArray);
			
			int resId = typedArray.getResourceId(index, 0);
			if(resId == 0) ret = typedArray.getDimension(index, 0);
			else ret = ResUtil.getDimen(context, resId);
			
			return ret;
		}
	    
	    public static final int DEFAULT_COLOR = 0XFFFFFFFF;
    }
}
