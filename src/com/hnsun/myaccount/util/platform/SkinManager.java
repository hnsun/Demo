package com.hnsun.myaccount.util.platform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.file.FileUtil;
import com.hnsun.myaccount.util.file.ParcelBoss;
import com.hnsun.myaccount.util.file.PathUtil;
import com.hnsun.myaccount.util.log.LogFactory;
import com.hnsun.myaccount.util.prop.FromTheme;

/**
 * 皮肤管理器
 * @author hnsun
 * @date 2016/08/21
 */
public class SkinManager {

    private SkinManager() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
    
    /**
     * 初始化皮肤及字体
     * @param context
     */
    public static void init(Context context) {
    	UtilBoss.ConditionUtil.n(context);
    	
    	//内置的皮肤文件放入程序文件夹
    	SkinManager.resPackage(context, AssetsUtil.SKIN_UMINE, SkinManager.FROM_RAW);
    	
    	SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
    	String skin = sharedPreferencesUtil.lockLoad(SharedPreferencesUtil.KEY_APP_SKIN);
    	if(UtilBoss.ObjUtil.isNull(skin)) {
    		skin = SkinManager.NAME_UMINE;
    		sharedPreferencesUtil.lockPut(SharedPreferencesUtil.KEY_APP_SKIN, skin);
    	}

		String path = AppUtil.getInnerPath(context, AppUtil.INNER_DIR_SKIN) + PathUtil.separatorBefore(skin) + PathUtil.separatorBefore(SkinManager.FONT_UMINE);
		SystemStatus.typeface = new File(path).exists() ? Typeface.createFromFile(path) : SystemStatus.typeface;
    }
    
    /**
     * 获取皮肤资源包 默认为用户目录
     * @param context
     * @param name
     * @param from
     * @return
     */
    public static void resPackage(Context context, String name, String from) {
    	UtilBoss.ConditionUtil.nt(context, name, from);

    	String inner = AppUtil.getInnerPath(context, AppUtil.INNER_DIR_SKIN) + PathUtil.separatorBefore(name);
    	if(new File(inner).exists()) return ; //没更新时不予处理 记录皮肤包更新时间
    	
    	String zip = inner + ".zip";
    	InputStream in = null;
    	try {
			if(SkinManager.FROM_RAW.equals(from)) { //自带
				in = AssetsUtil.inputStream(context, AssetsUtil.SKIN, name);
			} else if(SkinManager.FROM_OUT.equals(from)) { //用户提供
			}
	    	
	    	FileUtil.copyFile(in, zip);
	    	ParcelBoss.Zip.unzip(zip, AppUtil.getInnerPath(context, AppUtil.INNER_DIR_SKIN), ParcelBoss.Zip.UNZIP_NEW);
	    	FileUtil.deleteFile(zip);
		} catch (IOException e) {
        	LogFactory.log().e(e, "输入输出有问题");
        } finally {
            try {
                if(UtilBoss.ObjUtil.isNotNull(in)) in.close();
            } catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
            }
        }
    }
    
    /**
     * 根据ID获取资源信息(类型,名称)
     * @param context
     * @param res
     * @return
     */
    public static String resInfoFrom(Context context, int res) {
    	UtilBoss.ConditionUtil.n(context, res);
    	String ret = null;

    	Resources resources = context.getResources();
    	if(res != -1) ret = resources.getResourceTypeName(res) + "," + resources.getResourceEntryName(res);
        return ret;
    }
    
    /**
     * 获取要处理的皮肤对象
     * 均设置为引用形式，获取不了style里的值
     * background：1.没有设置，返回（默认值） 2.设置png，返回（drawable,名）3.设置xml，返回（drawable,名）4.设置@color，返回（color,名）
     * textColor：设置@color，返回（color,名）
     * @param activity
     * @param arrSkinViewInfo
     * @param skinName
     */
    public static Map<String, String> skinAttrs(Context context, AttributeSet attrs) {
    	Map<String, String> ret = new HashMap<String, String>(); //当前皮肤化属性集合
    	UtilBoss.ConditionUtil.n(context, attrs);
    	
    	boolean forStyle = false;

    	//textColor【（color，名）】
    	String textColor = SkinManager.resInfoFrom(context, attrs.getAttributeResourceValue(ConstantsUtil.NAMESPACE_ANDROID, SkinManager.ATTR_TEXT_COLOR, -1));
    	if(!UtilBoss.StrUtil.isEmpty(textColor)) ret.put(SkinManager.ATTR_TEXT_COLOR, textColor);
    	else { //style
    		forStyle = true;
    	}
    	
    	//background【（color，名）或（drawable，名）】
    	String background = SkinManager.resInfoFrom(context, attrs.getAttributeResourceValue(ConstantsUtil.NAMESPACE_ANDROID, SkinManager.ATTR_BACKGROUND, -1));
    	if(!UtilBoss.StrUtil.isEmpty(background)) ret.put(SkinManager.ATTR_BACKGROUND, background);
    	else { //style
    		forStyle = true;
    	}

		int style = attrs.getAttributeResourceValue(null, SkinManager.ATTR_STYLE, -1);
    	if(style != -1 && forStyle) {
    		int[] attrsInt = { android.R.attr.textColor, android.R.attr.background, };
			TypedArray typedArray = context.getTheme().obtainStyledAttributes(style, attrsInt);

			if(!ret.containsKey(SkinManager.ATTR_TEXT_COLOR)) {
		    	String a = SkinManager.resInfoFrom(context, typedArray.getResourceId(0, -1));
		    	if(!UtilBoss.StrUtil.isEmpty(a)) ret.put(SkinManager.ATTR_TEXT_COLOR, a);
			}
			if(!ret.containsKey(SkinManager.ATTR_BACKGROUND)) {
		    	String a = SkinManager.resInfoFrom(context, typedArray.getResourceId(1, -1));
		    	if(!UtilBoss.StrUtil.isEmpty(a)) ret.put(SkinManager.ATTR_BACKGROUND, a);
			}
			typedArray.recycle();
    	}
    	
		return ret;
    }
    
    /**
     * 初始化时，根据配置文件换成当下皮肤
     * @param activity
     * @param arrSkinViewInfo
     */
    public static void initSkin(Activity activity, SparseArray<Map<String, String>> arrSkinViewInfo) {
    	UtilBoss.ConditionUtil.n(activity, arrSkinViewInfo);
    	
    	SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(activity);
    	String skin = sharedPreferencesUtil.lockLoad(SharedPreferencesUtil.KEY_APP_SKIN);
    	
    	if(UtilBoss.StrUtil.isEmpty(skin)) return ; //不正常的切入不做皮肤处理
    	if(!skin.equals(SkinManager.NAME_UMINE)) SkinManager.skin(activity, arrSkinViewInfo, skin); //不是默认皮肤时进行重新设置之
    }
    
    /**
     * 更改配置文件换成当下皮肤
     * @param activity
     * @param arrSkinViewInfo
     */
    public static void changeSkin(Activity activity, SparseArray<Map<String, String>> arrSkinViewInfo, String skinName) {
    	UtilBoss.ConditionUtil.nt(activity, arrSkinViewInfo, skinName);
    	
    	SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(activity);
    	String skin = sharedPreferencesUtil.lockLoad(SharedPreferencesUtil.KEY_APP_SKIN);
    	
    	if(UtilBoss.StrUtil.isEmpty(skin)) return ; //不正常的切入不做皮肤处理
    	if(!skin.equals(skinName)) { //不是默认皮肤时进行重新设置之
    		SkinManager.skin(activity, arrSkinViewInfo, skinName);
    		sharedPreferencesUtil.lockPut(SharedPreferencesUtil.KEY_APP_SKIN, skinName);
    	}
    }
    
    /**
     * 换皮肤
     * @param activity
     * @param arrSkinViewInfo
     * @param skinName
     */
    private static void skin(Activity activity, SparseArray<Map<String, String>> arrSkinViewInfo, String skinName) {
    	UtilBoss.ConditionUtil.nt(activity, arrSkinViewInfo, skinName);
    	
		View view = null;
		String dir = AppUtil.getInnerPath(activity, (AppUtil.INNER_DIR_SKIN + PathUtil.separatorBefore(skinName)));
		for(int i = 0; i < arrSkinViewInfo.size(); i++) { //单个控件
			int key = arrSkinViewInfo.keyAt(i);
			Map<String, String> map = arrSkinViewInfo.get(key);
			
			view = activity.findViewById(key);
			for(Map.Entry<String, String> entry : map.entrySet()) { //控件的相关属性
				String name = entry.getKey();
				String[] value = entry.getValue().split(",");
				if(SkinManager.ATTR_BACKGROUND.equals(name)) { //背景
					if(value[0].equals(SkinManager.TYPE_DRAWABLE)) { //图片
						SkinManager.skinPhoto(activity, view, dir, value[1]);
					} else if(value[0].equals(SkinManager.TYPE_COLOR)) { //颜色
						String color = FromTheme.getInstance(dir + PathUtil.separatorBefore("value") + PathUtil.separatorBefore("color.properties")).getColor(value[1]);
						if(!UtilBoss.StrUtil.isEmpty(color)) view.setBackgroundColor(android.graphics.Color.parseColor(color));
					}
				} else if(SkinManager.ATTR_TEXT_COLOR.equals(name)) { //文字颜色
					String color = FromTheme.getInstance(dir + PathUtil.separatorBefore("value") + PathUtil.separatorBefore("color.properties")).getColor(value[1]);
					if(value[0].equals(SkinManager.TYPE_COLOR) && !UtilBoss.StrUtil.isEmpty(color)) ViewUtil.changeTextColor(view, color);
				}
			}
		}

		String path = dir + PathUtil.separatorBefore(SkinManager.FONT_UMINE);
		if(new File(path).exists()) {
			SystemStatus.typeface = Typeface.createFromFile(path);
			ViewUtil.changeFonts(activity.findViewById(R.id.layoutWrapper), activity, null);
		}
    }
    
    /**
     * 换皮肤背景图片
     * @param activity
     * @param view
     * @param from
     * @param name
     */
    private static void skinPhoto(Activity activity, View view, String from, String name) {
    	UtilBoss.ConditionUtil.nt(activity, view, from, name);
    	
    	String path = from + PathUtil.separatorBefore("drawable");
    	
    	path += PathUtil.separatorBefore(name + ".png"); //png
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if(UtilBoss.ObjUtil.isNotNull(bitmap)) view.setBackground(new BitmapDrawable(activity.getResources(), bitmap));

        path = from + PathUtil.separatorBefore("drawable") + PathUtil.separatorBefore(name + "_on.png"); //_on.png
        Bitmap bitmapOn = BitmapFactory.decodeFile(path);
        path = from + PathUtil.separatorBefore("drawable") + PathUtil.separatorBefore(name + "_normal.png"); //_normal.png
        Bitmap bitmapNormal = BitmapFactory.decodeFile(path);
        if(UtilBoss.ObjUtil.isNotNull(bitmapOn) || UtilBoss.ObjUtil.isNotNull(bitmapNormal)) {
            StateListDrawable state = new StateListDrawable();
            if(UtilBoss.ObjUtil.isNotNull(bitmapOn)) {
            	Drawable drawable = new BitmapDrawable(activity.getResources(), bitmapOn);
            	state.addState(new int[] { android.R.attr.state_selected }, drawable);
            	state.addState(new int[] { android.R.attr.state_pressed }, drawable);
            	state.addState(new int[] { android.R.attr.state_focused }, drawable);
            }
            if(UtilBoss.ObjUtil.isNotNull(bitmapNormal)) state.addState(new int[] {}, new BitmapDrawable(activity.getResources(), bitmapNormal));
            view.setBackground(state);
        }
    	
//    	path = from + PathUtil.separatorBefore("drawable") + PathUtil.separatorBefore(name + ".xml"); //xml
//        if(new File(path).exists()) view.setBackground(PhotoUtil.fromXml(activity, path));
//
//        String onPath = from + PathUtil.separatorBefore("drawable") + PathUtil.separatorBefore(name + "_on.xml"); //_on.xml
//        String normalPath = from + PathUtil.separatorBefore("drawable") + PathUtil.separatorBefore(name + "_normal.xml"); //_normal.xml
//        File on = new File(onPath);
//        File normal = new File(normalPath);
//        if(on.exists() || normal.exists()) {
//            StateListDrawable state = new StateListDrawable();
//            if(on.exists()) {
//            	Drawable drawable = PhotoUtil.fromXml(activity, onPath);
//            	state.addState(new int[] { android.R.attr.state_selected }, drawable);
//            	state.addState(new int[] { android.R.attr.state_pressed }, drawable);
//            	state.addState(new int[] { android.R.attr.state_focused }, drawable);
//            }
//            if(normal.exists()) state.addState(new int[] {}, PhotoUtil.fromXml(activity, normalPath));
//            view.setBackground(state);
//        }
    }

	/********************************************
	 * 皮肤化相关（FROM皮肤包来源 NAME皮肤包名称 FONT皮肤包内置字体 ATTR要处理的属性）
	 ********************************************/
    
    public static final String FROM_RAW = "FROM_RAW";
    public static final String FROM_OUT = "FROM_OUT";
    
    public static final String NAME_UMINE = "umine";
    
    public static final String FONT_UMINE = "umine.ttf";

    public static final String TYPE_DRAWABLE = "drawable";
    public static final String TYPE_COLOR = "color";

    public static final String ATTR_STYLE = "style";
    public static final String ATTR_BACKGROUND = "background"; //背景：xml（直接引用 间接引用） png（引用） 颜色（引用 非引）
    public static final String ATTR_TEXT_COLOR = "textColor"; //字体颜色： 引用 非引
}
