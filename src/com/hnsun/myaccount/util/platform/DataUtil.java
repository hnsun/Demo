package com.hnsun.myaccount.util.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.util.Base64;

import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 数据相关（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class DataUtil {

    private DataUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
    
    /**
     * 通过路径文件生成Base64字符串
     * @param path
     * @return
     */
    @SuppressWarnings("resource")
    public static String base64From(String path) {
        String ret = null;
		UtilBoss.ConditionUtil.n(path);
        
        try {
            File file = new File(path);
            byte[] buffer = new byte[(int) file.length() + 100];
            int length = new FileInputStream(file).read(buffer);
            ret = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
        } catch (IOException e) {
        	LogFactory.log().e(e, "输入输出有问题");
        }
        return ret;
    }
}
