package com.hnsun.myaccount.util.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.hnsun.myaccount.util.CryptionBoss;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * SharedPreferences 帮助类
 * @author hnsun
 * @date 2016/08/20
 */
public class SharedPreferencesUtil {

    public SharedPreferencesUtil(Context context) { //默认为配置信息
		if(UtilBoss.ObjUtil.isNull(context)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SharedPreferencesUtil.FILENAME_APP_CONFIG, Context.MODE_PRIVATE);
    }

    public SharedPreferencesUtil(Context context, String name) { //指定名称
		if(UtilBoss.ObjUtil.isNull(context) || UtilBoss.ObjUtil.isNull(name)) UtilBoss.ExceptionUtil.throwIllegalArgumentInit();
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void reset(String name) { //重新获得一个配置文件
		UtilBoss.ConditionUtil.n(name);
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public boolean put(String key, Object obj) { //根据类型调用不同的保存方法
		UtilBoss.ConditionUtil.n(key);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (obj instanceof String) {
            editor.putString(key, (String) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else {
            editor.putString(key, UtilBoss.StrUtil.getByObj(obj));
        }

        return SharedPreferencesCompat.apply(editor);
    }

    public boolean putAll(String key, List<?> list) { //保存一个List的数据
    	boolean ret = false;
		UtilBoss.ConditionUtil.n(key);
		
        int size = list.size();
        if (size < 1) return ret;
        
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < size; i++) {
        	Object obj = list.get(i);
            if (obj instanceof String) {
                editor.putString(key, (String) obj);
            } else if (obj instanceof Integer) {
                editor.putInt(key, (Integer) obj);
            } else if (obj instanceof Boolean) {
                editor.putBoolean(key, (Boolean) obj);
            } else if (obj instanceof Float) {
                editor.putFloat(key, (Float) obj);
            } else if (obj instanceof Long) {
                editor.putLong(key, (Long) obj);
            } else editor.putString(key, UtilBoss.StrUtil.getByObj(obj));
        }
        ret = SharedPreferencesCompat.apply(editor);
        return ret;
    }

    public String loadStr(String key) {
		UtilBoss.ConditionUtil.n(key);
        return sharedPreferences.getString(key, null);
    }

    public int loadInt(String key) {
		UtilBoss.ConditionUtil.n(key);
        return sharedPreferences.getInt(key, 0);
    }

    public float loadFloat(String key) {
		UtilBoss.ConditionUtil.n(key);
        return sharedPreferences.getFloat(key, 0f);
    }

    public long loadLong(String key) {
		UtilBoss.ConditionUtil.n(key);
        return sharedPreferences.getLong(key, 0l);
    }

    public boolean loadBoolean(String key) {
		UtilBoss.ConditionUtil.n(key);
        return sharedPreferences.getBoolean(key, false);
    }

    public Object load(String key, Object defaultObject) { //根据默认值得到数据具体类型，然后调用相对方法获取值
		UtilBoss.ConditionUtil.n(key);
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else return sharedPreferences.getString(key, UtilBoss.StrUtil.getByObj(defaultObject));
    }

    public Map<String, ?> loadAll() { //返回所有的键值对
        return sharedPreferences.getAll();
    }

    public boolean lockPut(String key, Object obj) { //根据类型调用不同的保存方法转为String加密保存
		UtilBoss.ConditionUtil.n(key);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        
         try { //加密
        	 editor.putString(key, CryptionBoss.Base64.encode(UtilBoss.StrUtil.null2Empty(obj)));
         } catch (Exception e) {
        	 LogFactory.log().e(e);
         }

        return SharedPreferencesCompat.apply(editor);
    }

    public String lockLoad(String key) { //获取加密的String
		UtilBoss.ConditionUtil.n(key);
		String ret = sharedPreferences.getString(key, null);
		
		if(!UtilBoss.StrUtil.isEmpty(ret)) ret = CryptionBoss.Base64.decode(ret);
		
        return ret;
    }

    public boolean contains(String key) { //查询某个key是否已经存在
		UtilBoss.ConditionUtil.n(key);
        return sharedPreferences.contains(key);
    }

    public boolean remove(String key) {
		UtilBoss.ConditionUtil.n(key);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        return SharedPreferencesCompat.apply(editor);
    }

    public boolean removeAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return SharedPreferencesCompat.apply(editor);
    }

    private Context context;
    private SharedPreferences sharedPreferences;

    /*************
     * ****
     * *        用途类型_模块标志_内容描述_相关说明
     * ****
     *******************************************************/

    public static final String FILENAME_APP_CONFIG = "FILENAME_APP_CONFIG";
    public static final String KEY_APP_UNIQUECODE = "KEY_APP_UNIQUECODE";
    public static final String KEY_APP_DIRECTORY = "KEY_APP_DIRECTORY";
    public static final String KEY_APP_SKIN = "KEY_APP_SKIN";

    public static final String FILENAME_PASSCODE_CONFIG = "FILENAME_PASSCODE_CONFIG";
    public static final String KEY_PASSCODE_CODE = "KEY_PASSCODE_CODE";

    public static final String FILENAME_DATABASE_CONFIG = "FILENAME_DATABASE_CONFIG";
    public static final String KEY_DATABASE_VACUUM = "KEY_DATABASE_VACUUM";

    public static final String FILENAME_NET_CONFIG = "FILENAME_NET_CONFIG";
	public static final String KEY_NET_COOKIE_VERSION = "KEY_NET_COOKIE_VERSION";
	public static final String KEY_NET_COOKIE_PATH = "KEY_NET_COOKIE_PATH";
	public static final String KEY_NET_COOKIE_DOMAIN = "KEY_NET_COOKIE_DOMAIN";
	public static final String KEY_NET_COOKIE_EXPIRY_DATE = "KEY_NET_COOKIE_EXPIRY_DATE";
	
	public static final String FILENAME_ACCOUNT_CONFIG = "FILENAME_ACCOUNT_CONFIG";
	public static final String KEY_ACCOUNT = "KEY_ACCOUNT";
	
	public static final String FILENAME_VIEW_CONFIG = "FILENAME_VIEW_CONFIG";
	public static final String KEY_VIEW_REFRESH = "KEY_VIEW_REFRESH";

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {

        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() { //反射查找apply的方法，是否存在该方法
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            	LogFactory.log().e(e, "查找apply方法失败");
            }
            return null;
        }

        public static boolean apply(SharedPreferences.Editor editor) { //如果找到则使用apply执行，否则使用commit
            try {
                if (UtilBoss.ObjUtil.isNotNull(sApplyMethod)) {
                    sApplyMethod.invoke(editor); //异步
                    return true;
                }
            } catch (IllegalArgumentException e) {
    			LogFactory.log().e(e, "参数有问题");
            } catch (IllegalAccessException e) {
    			LogFactory.log().e(e, "没有合法获取权限");
            } catch (InvocationTargetException e) {
    			LogFactory.log().e(e, "反射调用的方法出错");
            }
            return editor.commit();//同步
        }

        private static final Method sApplyMethod = findApplyMethod();
    }
}
