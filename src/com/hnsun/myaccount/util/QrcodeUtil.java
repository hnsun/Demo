package com.hnsun.myaccount.util;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.log.LogFactory;
import com.hnsun.myaccount.util.platform.PhotoUtil;

/**
 * 二维码帮助类
 * @author hnsun
 * @date 2016/10/09
 */
public class QrcodeUtil {

	private QrcodeUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

	/**
	 * 获得二维码
	 * @param context
	 * @param content
	 * @param length
	 * @return
	 */
	public static Bitmap get(Context context, String content, int length) {
		Bitmap ret = null;
		UtilBoss.ConditionUtil.nt(context, content);
		
		try {
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, ApplicationDatas.APP_ENCODING);
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, length, length, hints);
			
			int[] pixels = new int[length * length];
			for(int y = 0; y < length; y++) { //长
				for(int x= 0; x < length;x++) { //宽
					pixels[y * length + x] = bitMatrix.get(x, y) ? 0xff000000 : 0xffffffff;
				}
			}
			ret = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
			ret.setPixels(pixels, 0, length, 0, 0, length, length);
		} catch (WriterException e) {
			LogFactory.log().e(e);
		}
		
		return ret;
	}

	/**
	 * 获得加商标的二维码
	 * @param context
	 * @param content
	 * @param length
	 * @param logo
	 * @return
	 */
	public static Bitmap get(Context context, String content, int length, Bitmap logo) {
		Bitmap ret = null;
		UtilBoss.ConditionUtil.nt(context, content, logo);
		
		Bitmap src = QrcodeUtil.get(context, content, length);
        if(UtilBoss.ObjUtil.isNull(src)) return null;
 
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
 
        if(srcWidth == 0 || srcHeight == 0) return null;
        if(logoWidth == 0 || logoHeight == 0) return src;
 
        try {
            float scaleFactor = srcWidth * 1.0f / 6 / logoWidth; //logo大小为二维码整体大小的1/5
            ret = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
            
            Canvas canvas = new Canvas(ret);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            ret = null;
			LogFactory.log().e(e);
        }
 
		return ret;
	}

	/**
	 * 获取二维码内信息
	 * @param bitmap
	 * @return
	 */
	public static String scan(Bitmap bitmap) {
		String ret = null;
		UtilBoss.ConditionUtil.n(bitmap);

		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, ApplicationDatas.APP_ENCODING);
		QrcodeUtil.RGBLuminanceSource source = new QrcodeUtil.RGBLuminanceSource(bitmap);
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		Result result;
		try {
			result = reader.decode(binaryBitmap);
			result = reader.decode(binaryBitmap, hints);
			ret = result.getText();
		} catch (NotFoundException e) {
			LogFactory.log().e(e);
		} catch (ChecksumException e) {
			LogFactory.log().e(e);
		} catch (FormatException e) {
			LogFactory.log().e(e);
		}
		
		return ret;
	}
	
	/**
	 * 反处理
	 * @author hnsun
	 * @date 2016/10/10
	 */
	public static class RGBLuminanceSource extends LuminanceSource {
		
		public RGBLuminanceSource(String path) {
			this(PhotoUtil.getBitmapFrom(path));
		}
		
		public RGBLuminanceSource(Bitmap bitmap) {
			super(bitmap.getWidth(), bitmap.getHeight());
			
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int[] pixels = new int[width * height];
			bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
			
			luminances = new byte[width * height];
			for(int y = 0; y < height; y++) {
				int offset = width * y;
				for(int x = 0; x < width; x++) {
					int pixel = pixels[offset + x];
					int r = (pixel >> 16) & 0xff;
					int g = (pixel >> 8) & 0xff;
					int b = pixel & 0xff;
					luminances[offset + x] = (byte) ((r == g && g == b) ? r : (r + g + g + b) >> 2);
				}
			}
		}

		@Override
		public byte[] getMatrix() {
			return luminances;
		}

		@Override
		public byte[] getRow(int y, byte[] row) {
			if(y < 0 || y >= getHeight()) throw new IllegalArgumentException("Requested row is outside the image: " + y);
			
			int width = getWidth();
			if(UtilBoss.ObjUtil.isNull(row) || row.length < width) row = new byte[width];
			System.arraycopy(luminances, y * width, row, 0, width);
			return row;
		}

		private final byte[] luminances;
	}
}
