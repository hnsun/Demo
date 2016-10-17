package com.hnsun.myaccount.util.platform;

import android.os.Environment;
import android.os.StatFs;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 储存卡信息相关（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class SDCardUtil {

    private SDCardUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /**
     * 判断SDCard是否可用
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     * @return
     */
    @SuppressWarnings("deprecation")
	public static long getSDCardAllSize() {
    	long ret = 0;
    	
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            long availableBlocks = (long) stat.getAvailableBlocks() - 4; //获取空闲的数据块的数量
            long freeBlocks = stat.getAvailableBlocks(); //获取单个数据块的大小（byte）
            ret = freeBlocks * availableBlocks;
        }
        return ret;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    @SuppressWarnings("deprecation")
	public static long getFreeBytes(String filePath) {
		UtilBoss.ConditionUtil.n(filePath);
		
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath(); //如果是sd卡的下的路径，则获取sd卡可用容量
        } else filePath = Environment.getDataDirectory().getAbsolutePath(); //如果是内部存储的路径，则获取内存存储的可用容量
        StatFs stat = new StatFs(filePath);
        return stat.getBlockSize() * ((long) stat.getAvailableBlocks() - 4);
    }

    /**
     * 获取系统存储路径
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }
}
