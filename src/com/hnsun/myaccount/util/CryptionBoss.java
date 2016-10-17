package com.hnsun.myaccount.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 加解密
 * @author hnsun
 * @date 2016/08/15
 * 类型：MD5, Base64,
 */
public class CryptionBoss {
	
	private CryptionBoss() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

	/**
	 * MD5
	 */
	public static class MD5 {

		private MD5() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 将字符串变为字节数组并加密后转化返回加密数据
		 * @param str
		 * @return
		 */
		public static String getCode(String str) {
			String ret = null;
			UtilBoss.ConditionUtil.n(str);
			
			try {
				ret = new String(str);
				MessageDigest md5 = MessageDigest.getInstance("MD5"); //MD5加密器
				ret = bytes2String(md5.digest(str.getBytes())); //加密转化
			} catch (NoSuchAlgorithmException e) {
				LogFactory.log().e(e, "没有这个加密器");
			}
			
			return ret;
		}
		
		/**
		 * 将字节数组转化为相对应的字符串
		 * @param bBytes
		 * @return
		 */
		public static String bytes2String(byte[] bBytes) {
			UtilBoss.ConditionUtil.n(bBytes);
			
			StringBuffer buffer = new StringBuffer();
			for (byte bByte : bBytes) {
				buffer.append(CryptionBoss.MD5.byte2ArrayString(bByte));
			}
			return buffer.toString();
		}
		
		/**
		 * 字节转为16进制数字数组字符串
		 * @param bByte
		 * @return
		 */
		public static String byte2ArrayString(byte bByte) {
			int iByte = bByte;
			if(iByte < 0) iByte += 256;
			return CryptionBoss.MD5.DIGITS[iByte / 16] + CryptionBoss.MD5.DIGITS[iByte % 16]; 
		}

		/**
		 * 字节转为数字字符串
		 * @param bByte
		 * @return
		 */
		public static String byte2NumString(byte bByte) { 
			int iByte = bByte; 
			if(iByte < 0) iByte += 256;
			return String.valueOf(iByte);
		}
		
		private static final String[] DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" }; //16个加密混肴码
	}
	
	/**
	 * Base64
	 */
	public static class Base64 {

		private Base64() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

		/**
		 * 加密
		 * @param str
		 * @return
		 */
		public static String encode(String str) {
			String ret = null;
			UtilBoss.ConditionUtil.n(str);
			
			try {
				ret = android.util.Base64.encodeToString(str.getBytes(ApplicationDatas.APP_ENCODING), android.util.Base64.DEFAULT);
			} catch (UnsupportedEncodingException e) {
				LogFactory.log().e(e, "不支持的编码");
			}
			return ret;
	    }
	
	    /**
	     * 解密
	     * @param str
	     * @return
	     */
	    public static String decode(String str) {
			String ret = null;
			UtilBoss.ConditionUtil.n(str);
			
			try {
				ret = new String(android.util.Base64.decode(str.getBytes(), android.util.Base64.DEFAULT), ApplicationDatas.APP_ENCODING);
			} catch (UnsupportedEncodingException e) {
				LogFactory.log().e(e, "不支持的编码");
			}
			return ret;
	    }
	}
}
