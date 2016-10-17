package com.hnsun.myaccount.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 流之间转换（摘抄）
 * @author hnsun
 * @date 2016/08/15
 */
public class IOUtil {

	private IOUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

	/**
	 * 将输入流转换成字节流
	 * @param input
	 * @return
	 */
	public static byte[] toBytes(InputStream input) {
		byte[] ret = null;
		ByteArrayOutputStream byteOut = null;
		UtilBoss.ConditionUtil.n(input);
		
		try {
			byteOut = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int read = 0;
			while((read = input.read(b)) > 0) {
				byteOut.write(b, 0, read);
			}
			ret = byteOut.toByteArray();
		} catch (IOException e) {
			LogFactory.log().e(e, "流输入输出有错误");
		} finally {
			try {
				if(UtilBoss.ObjUtil.isNotNull(byteOut)) byteOut.close();
				if(UtilBoss.ObjUtil.isNotNull(input)) input.close();
			} catch (IOException e) {
				LogFactory.log().e(e, "流输入输出有错误");
			}
		}
		return ret;
	}

	/**
	 * 将文件读取为一个字符串
	 * @param input
	 * @return
	 */
	public static String toString(File file) {
		String ret = null;
		UtilBoss.ConditionUtil.n(file);
		
		try {
			ret = IOUtil.toString(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			LogFactory.log().e(e, "文件未找到");
		}
		return ret;
	}

	/**
	 * 将输入流转换为一个串
	 * @param input
	 * @return
	 */
	public static String toString(InputStream input) {
		String ret = null;
		UtilBoss.ConditionUtil.n(input);
		
		ret = IOUtil.toStringWithLineBreak(input, null);
		return ret;
	}

	/**
	 * 以指定编码格式将输入流按行置入一个List<String>
	 * @param input
	 * @return
	 */
	public static List<String> toLines(InputStream input, String encoding) {
		List<String> ret = new ArrayList<String>();
		InputStreamReader insreader = null;
		BufferedReader bin = null;
		UtilBoss.ConditionUtil.nt(input, encoding);
		
		try {
			insreader = new InputStreamReader(input, encoding);
			bin = new BufferedReader(insreader);
			
			String line;
			while(UtilBoss.ObjUtil.isNotNull(line = bin.readLine())) {
				ret.add(line);
			}
		} catch (UnsupportedEncodingException e) {
			LogFactory.log().e(e, "不支持的编码");
		} catch (IOException e) {
			LogFactory.log().e(e, "流输入输出有错误");
		} finally {
			try {
				if(UtilBoss.ObjUtil.isNotNull(bin)) bin.close();
				if(UtilBoss.ObjUtil.isNotNull(insreader)) insreader.close();
				if(UtilBoss.ObjUtil.isNotNull(input)) input.close();
			} catch (IOException e) {
				LogFactory.log().e(e, "流输入输出有错误");
			}
		}
		return ret;
	}

	/**
	 * 以UTF-8格式将输入流按行置入一个List<String>
	 * @param input
	 * @return
	 */
	public static List<String> toLines(InputStream input) {
		UtilBoss.ConditionUtil.n(input);
		
		return IOUtil.toLines(input, ApplicationDatas.APP_ENCODING);
	}

	/**
	 * 转换为每行补充指定换行符(例如："/n"，"</br>")
	 * @param input
	 * @param lineBreak
	 * @return
	 */
	public static String toStringWithLineBreak(InputStream input, String lineBreak) {
		UtilBoss.ConditionUtil.nt(input, lineBreak);
		
		StringBuilder sb = new StringBuilder(20480);
		List<String> lines = IOUtil.toLines(input);
		for(String line : lines) {
			sb.append(line);
			if(UtilBoss.ObjUtil.isNotNull(lineBreak)) sb.append(lineBreak);
		}
		return sb.toString();
	}

	/**
	 * 将字符串转出到指定文件
	 * @param saveFile
	 * @param content
	 */
	public static void toFile(File saveFile, String content) {
		UtilBoss.ConditionUtil.nt(saveFile, content);
		
		File parent = saveFile.getParentFile();
		if (!parent.exists()) parent.mkdirs();
		PrintWriter out = null;
		
		try {
			out = new PrintWriter(new FileWriter(saveFile));
			out.print(content);
			out.flush();
		} catch (IOException e) {
			LogFactory.log().e(e, "流输入输出有错误");
		} finally {
			if (UtilBoss.ObjUtil.isNotNull(out)) out.close();
		}
	}

	/**
	 * 将一组文件打zip包
	 * @param srcFiles
	 * @param targetFileName
	 */
	public static void filesToZip(List<File> srcFiles, String targetFileName) {
		UtilBoss.ConditionUtil.nt(srcFiles, targetFileName);
		
		String fileOutName = targetFileName + ".zip";
		byte[] buf = new byte[1024];
		FileInputStream in = null;
		FileOutputStream fos = null;
		ZipOutputStream out = null;
		
		try {
			fos = new FileOutputStream(fileOutName);
			out = new ZipOutputStream(fos);
			for (File file : srcFiles) {
				in = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(file.getName()));
				int len;
				while((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
			}
		} catch (IOException e) {
			LogFactory.log().e(e, "流输入输出有错误");
		} finally {
			try {
				if (UtilBoss.ObjUtil.isNotNull(in)) in.close();
				if (UtilBoss.ObjUtil.isNotNull(fos)) {
					out.closeEntry();
					out.close();
					fos.close();
				}
			} catch (IOException e) {
				LogFactory.log().e(e, "流输入输出有错误");
			}
		}
	}

    /**
     * 把Stream转换成String
     * @param input
     * @return
     */
    public static String convertStreamToString(InputStream input) {
		UtilBoss.ConditionUtil.n(input);
		
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while(UtilBoss.ObjUtil.isNotNull(line = reader.readLine())) {
                sb.append(line + ConstantsUtil.CODE_LINE_FEED);
            }
        } catch (IOException e) {
			LogFactory.log().e(e, "流输入输出有错误");
        } finally {
            try {
				if(UtilBoss.ObjUtil.isNotNull(input)) input.close();
            } catch (IOException e) {
    			LogFactory.log().e(e, "流输入输出有错误");
            }
        }
        
        return sb.toString();
    }
}
