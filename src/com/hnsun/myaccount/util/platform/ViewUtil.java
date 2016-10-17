package com.hnsun.myaccount.util.platform;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * 界面相关（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class ViewUtil {

    private ViewUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /**
     * 吐司信息
     * @param context
     * @param msg
     */
    public static void displayToast(Context context, CharSequence msg) {
		UtilBoss.ConditionUtil.n(context, msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据资源id吐司信息
     * @param context
     * @param res
     */
    public static void displayToast(Context context, int res) {
        Toast.makeText(context, ResUtil.getText(context, res), Toast.LENGTH_SHORT).show();
    }

    /**
     * 修改整个界面所有控件的字体
     * @param view
     * @param context
     * @param typeface
     */
    public static void changeFonts(View view, Context context, Typeface typeface) {
		UtilBoss.ConditionUtil.n(view, context);
		
        Typeface tf = UtilBoss.ObjUtil.isNull(typeface) ? SystemStatus.typeface : typeface;
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(tf);
        } else if (view instanceof Button) {
            ((Button) view).setTypeface(tf);
        } else if (view instanceof EditText) {
            ((EditText) view).setTypeface(tf);
        } else if (view instanceof ViewGroup) {
        	ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
            	ViewUtil.changeFonts(child, context, typeface);
            }
        }
    }
    
    /**
     * 修改整个界面所有控件的字体颜色
     * @param view
     * @param color
     */
    public static void changeTextColor(View view, String color) {
		UtilBoss.ConditionUtil.nt(view, color);
		
		int c = android.graphics.Color.parseColor(color);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(c);
        } else if (view instanceof Button) {
            ((Button) view).setTextColor(c);
        } else if (view instanceof EditText) {
            ((EditText) view).setTextColor(c);
        } else if (view instanceof ViewGroup) {
        	ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
            	ViewUtil.changeTextColor(child, color);
            }
        }
    }

    /**
     * 修改整个界面所有控件的字体大小
     * @param viewGroup
     * @param size
     */
    public static void changeTextSize(ViewGroup viewGroup, int size) {
		UtilBoss.ConditionUtil.n(viewGroup);
		
        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(size);
            } else if (view instanceof Button) {
                ((Button) view).setTextSize(size);
            } else if (view instanceof EditText) {
                ((EditText) view).setTextSize(size);
            } else if (view instanceof ViewGroup) {
            	ViewUtil.changeTextSize((ViewGroup) view, size);
            }
        }
    }

    /**
     * 不改变控件位置，修改控件大小
     * @param view
     * @param width
     * @param height
     */
    public static void widthHeight(View view, int width, int height) {
		UtilBoss.ConditionUtil.n(view);
		
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    /**
     * 修改控件的高
     * @param view
     * @param height
     */
    public static void height(View view, int height) {
		UtilBoss.ConditionUtil.n(view);
		
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    /**
     * 根据图片路径去设置ImageView显示的图片
     * @param uri
     * @param imageView
     */
    public static void setImageView(String uri, ImageView imageView) {
		UtilBoss.ConditionUtil.nt(uri, imageView);
        imageView.setImageURI(Uri.fromFile(new File(uri)));
    }

    /**
     * 将Bitmap设置进ImageView中
     * @param bitmap
     * @param imageView
     */
    public static void setImageView(Bitmap bitmap, ImageView imageView) {
		UtilBoss.ConditionUtil.n(bitmap, imageView);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 改变ListView的高度 根据所有子View的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
		UtilBoss.ConditionUtil.n(listView);
		
        ListAdapter adapter = listView.getAdapter();
        if(UtilBoss.ObjUtil.isNull(adapter)) return;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView); //获得相应的子项
            listItem.measure(0, 0); //开始计算子项高度
            totalHeight += listItem.getMeasuredHeight(); //获得目前总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    
    /**
     * 设置多个视图可见度
     * @param visibility
     * @param views
     */
    public static void setVisibility(int visibility, View... views) {
    	for (View view : views) {
			view.setVisibility(visibility);
		}
    }
}
