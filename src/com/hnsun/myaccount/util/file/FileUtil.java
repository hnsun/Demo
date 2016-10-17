package com.hnsun.myaccount.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 文件帮助类
 * @author hnsun
 * @date 2016/08/15
 */
public class FileUtil {

	private FileUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

	/**
	 * 多层路径判断及文件创建
	 * @param dipPath
	 */
    public static void mkDipPath(String dipPath) {
		UtilBoss.ConditionUtil.n(dipPath);
		
        File file = new File(dipPath);
        if (!file.exists()) {
            try {
            	PathUtil.mkDipPath(PathUtil.parentPath(dipPath));
                file.createNewFile();
            } catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
            }
        }
    }

    /**
     * 删除指定路径的文件
     * @param path
     */
    public static void deleteFile(String path) {
		UtilBoss.ConditionUtil.n(path);
		
        try {
            File file = new File(path);
            if(file.exists()) file.delete();
        } catch (Exception e) {
        	LogFactory.log().e(e);
        }
    }

    /**
     * 复制单个文件 以新名字存在
     * @param srcFile 包含路径的源文件 如：E:/phsftp/src/abc.txt
     * @param targetFile 目标文件；若文件目录不存在则自动创建  如：E:/phsftp/dest/aaa.txt
     */
    public static void copyFile(String srcFile, String targetFile) {
		UtilBoss.ConditionUtil.n(srcFile, targetFile);
		
    	FileInputStream in = null;
        try {
            in = new FileInputStream(srcFile);
            FileUtil.copyFile(in, targetFile);
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
     * 复制单个文件 以新名字存在
     * @param srcIn 源文件输入流
     * @param targetFile 目标文件；若文件目录不存在则自动创建  如：E:/phsftp/dest/aaa.txt
     */
    public static void copyFile(InputStream srcIn, String targetFile) {
		UtilBoss.ConditionUtil.nt(srcIn, targetFile);
		
    	FileOutputStream out = null;
    	
        try {
            out = new FileOutputStream(targetFile);
            PathUtil.mkDipPath(UtilBoss.StrUtil.substring(targetFile, 0, targetFile.lastIndexOf(File.separator)));
            
            int length;
            byte buffer[] = new byte[ConstantsUtil.BYTE_BUFFER_SIZE];
            while((length = srcIn.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();
        } catch (IOException e) {
        	LogFactory.log().e(e, "输入输出有问题");
        } finally {
            try {
                if(UtilBoss.ObjUtil.isNotNull(out)) out.close();
            } catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
            }
        }
    }
}
