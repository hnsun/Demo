package com.hnsun.myaccount.util.platform;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.file.FileUtil;
import com.hnsun.myaccount.util.file.PathUtil;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 图片工具相关（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class PhotoUtil {

    private PhotoUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
    
    /**
     * 把bitmap转换成base64
     * @param bitmap
     * @param quality
     * @return
     */
    public static String base64From(Bitmap bitmap, int quality) {
		UtilBoss.ConditionUtil.n(bitmap);
		
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    /**
     * 把base64转换成bitmap
     * @param str
     * @return
     */
    public static Bitmap bitmapFromBase64(String str) {
    	Bitmap ret = null;
		UtilBoss.ConditionUtil.n(str);
        
        try {
            byte[] bytes = null;
            bytes = Base64.decode(str, Base64.DEFAULT);
            ret = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
        	LogFactory.log().e(e);
        }
        
        return ret;
    }

    /**
     * 缩放图片
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomImg(Bitmap bitmap, int width, int height) {
		UtilBoss.ConditionUtil.n(bitmap);
		
        //获得图片的宽高
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        //计算缩放比例
        float scaleWidth = ((float) width) / w;
        float scaleHeight = ((float) height) / h;
        Matrix matrix = new Matrix(); //取得想要缩放的matrix参数 即信息封装
        matrix.postScale(scaleWidth, scaleHeight);
        
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true); //得到新的图片
    }

    /**
     * 通过文件路径获取到bitmap 按指定显示比例
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromPath(String path, int width, int height) {
		UtilBoss.ConditionUtil.n(path);
		
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true; //设置为ture只获取图片大小
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);
        int w = opts.outWidth;
        int h = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (w > width || h > height) { //缩放
            scaleWidth = ((float) w) / width;
            scaleHeight = ((float) h) / height;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), width, height, true);
    }

    /**
     * 从文件中获得Bitmap
     * @param path
     * @return
     */
    public static Bitmap getBitmapFrom(String path) {
        Bitmap ret = null;
		UtilBoss.ConditionUtil.n(path);

        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(new File(path));
            ret = BitmapFactory.decodeStream(fIn);
        } catch (FileNotFoundException e) {
        	LogFactory.log().e(e, "文件未找到");
        } finally {
            try {
                if(UtilBoss.ObjUtil.isNotNull(fIn)) fIn.close();
            } catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
            }
        }

        return ret;
    }

    /**
     * 以后缀格式储存图片
     * @param bitmap
     * @param outPath
     */
    public static void saveBy(Bitmap bitmap, String outPath) {
		UtilBoss.ConditionUtil.nt(bitmap, outPath);
		String suffix = UtilBoss.StrUtil.substring(outPath, outPath.lastIndexOf("\\.") + 1);
        FileOutputStream fOut = null;
        
        try {
            File file = new File(outPath);
            FileUtil.mkDipPath(outPath);
            fOut = new FileOutputStream(file);
            
            Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
            if(suffix.equalsIgnoreCase(ConstantsUtil.FORMAT_PICTURE_JPG)) {
            	format = Bitmap.CompressFormat.JPEG;
            } else if(suffix.equalsIgnoreCase(ConstantsUtil.FORMAT_PICTURE_PNG)) {
            	format = Bitmap.CompressFormat.PNG;
            }
            
            bitmap.compress(format, 100, fOut);
            fOut.flush();
        } catch (FileNotFoundException e) {
        	LogFactory.log().e(e, "文件未找到");
        } catch (IOException e) {
        	LogFactory.log().e(e, "输入输出有问题");
        } finally {
            try {
                if(UtilBoss.ObjUtil.isNotNull(fOut)) fOut.close();
            } catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
            }
        }
    }
    
    /**
     * 将某图片根据格式进行转换 并保存于指定目录
     * @param srcFile 原图位置
     * @param outDir 新图目录位置
     * @param imgName 文件名
     * @param pattern 格式
     * @return
     */
    public static boolean convertPattern(String srcFile, String outDir, String imgName, String pattern) {
        boolean ret = false;
		UtilBoss.ConditionUtil.n(srcFile, outDir, imgName, pattern);

        String outFile = PathUtil.separatorAfter(outDir) + imgName + "." + pattern;
        if(!UtilBoss.StrUtil.substring(srcFile, srcFile.lastIndexOf(".") + 1).equals(pattern)) { //后缀名不同
            if(pattern.equals(ConstantsUtil.FORMAT_PICTURE_JPG)) {
                saveBy(getBitmapFrom(srcFile), outFile);
                ret = true;
            }
        } else { //后缀名相同
            FileUtil.copyFile(srcFile, outFile);
            ret = true;
        }

        return ret;
    }

    /**
     * 将 Drawable 转化为 Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
    	Bitmap ret = null;
		UtilBoss.ConditionUtil.n(drawable);
    	
        //取drawable的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565; //取drawable的颜色格式
        ret = Bitmap.createBitmap(w, h, config); //建立对应bitmap
        Canvas canvas = new Canvas(ret); //建立对应bitmap的画布
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas); //把drawable内容画到画布中
        
        return ret;
    }
}
