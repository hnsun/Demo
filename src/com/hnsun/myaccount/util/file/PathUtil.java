package com.hnsun.myaccount.util.file;

import java.io.File;

import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * 路径处理
 * @author hnsun
 * @date 2016/08/15
 * 规范：路径末尾不跟分隔符
 */
public class PathUtil {
	
	/**
	 * 部分路径前添加分隔符
	 * @param str
	 * @return
	 */
	public static String separatorBefore(String str) {
		UtilBoss.ConditionUtil.n(str);
		
		return File.separator + str;
	}
	
	/**
	 * 部分路径后添加分隔符
	 * @param str
	 * @return
	 */
	public static String separatorAfter(String str) {
		UtilBoss.ConditionUtil.n(str);
		
		return str + File.separator;
	}

	/**
	 * 获取路径的父路径， 若没有返回本身
	 * @param dipPath
	 * @return
	 */
	public static String parentPath(String dipPath) {
		UtilBoss.ConditionUtil.n(dipPath);
		
		if(dipPath.contains(File.separator)) return UtilBoss.StrUtil.substring(dipPath, 0, dipPath.lastIndexOf(File.separator));
		
		return dipPath;
	}
	
	/**
	 * 多层路径创建
	 * @param dipPath
	 */
    public static void mkDipPath(String dipPath) {
		UtilBoss.ConditionUtil.n(dipPath);
		
        File file = new File(dipPath);
        if(!file.exists()) file.mkdirs();
    }

    /**
     * 从路径获取文件名
     * @param path
     * @return
     */
    public static String getFileName(String path) {
    	String ret = null;
		UtilBoss.ConditionUtil.n(path);
    	
        int start = path.lastIndexOf(File.separator);
        int end = path.lastIndexOf(".");
        if(start != -1 && end != -1) ret = UtilBoss.StrUtil.substring(path, start + 1, end);
        
        return ret;
    }
    
    /**
     * 系统相对目录层级
     * @author hnsun
     * @date 2016/10/04
     */
    public static class SystemPath {
    	
    	/**
    	 * 获得整体目录
    	 * @param path
    	 * @return
    	 */
    	public static String get(String path) {
    		String ret = null;
    		
    		ret = SystemStatus.directory;
    		if(!UtilBoss.StrUtil.isEmpty(path)) ret += PathUtil.separatorBefore(path);
    		PathUtil.mkDipPath(ret); //用时还不存在则创建
    		
    		return ret;
    	}

    	public static final String TEST = "Test"; //测试
    	public static final String LOG = "Log"; //日志
    	public static final String DOWNLOAD = "Download"; //下载
    	
    	public static final String USER = "User"; //用户
    	public static final String USER_IMAGE = PathUtil.SystemPath.USER + PathUtil.separatorBefore("Image"); //用户图片
    }
}
