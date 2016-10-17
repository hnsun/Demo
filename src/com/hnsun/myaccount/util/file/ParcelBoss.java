package com.hnsun.myaccount.util.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 解压缩全集
 * @author hnsun
 * @date 2016/08/22
 *  类型：Zip,
 */
public class ParcelBoss {
	
	private ParcelBoss() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /*************
     *****
     **        zip格式 ：Zip
     **
     *****
     *******************************************************/
	public static class Zip {
		
		private Zip() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		/**
		 * 压缩
		 * @param srcDirOrFile 压缩对象
		 * @param name 含路径的名称
		 */
		public static void zip(String srcDirOrFile, String name) {
			UtilBoss.ConditionUtil.n(srcDirOrFile);
			
			String pathName = null;
			if(UtilBoss.StrUtil.isEmpty(name)) { //未提供新名称
				File src = new File(srcDirOrFile);
				if(!src.exists()) UtilBoss.ExceptionUtil.throwUnsupportedOperation("This file not exist.");
				
				name = srcDirOrFile; //默认为需要压缩的内容的名称为压缩包名称
				if(src.isFile() && srcDirOrFile.indexOf(".") > 0) name = UtilBoss.StrUtil.substring(srcDirOrFile, 0, srcDirOrFile.lastIndexOf(".")); 
			}
			
			pathName = name;
			if(!pathName.endsWith(".zip")) pathName += ".zip";
			
			ZipOutputStream zos = null;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(pathName);
				zos = new ZipOutputStream(fos);
				String dest = name.contains(File.separator) ? UtilBoss.StrUtil.substring(name, name.lastIndexOf(File.separator) + 1) : name;
				ParcelBoss.Zip.zipAll(zos, srcDirOrFile, dest);
				zos.finish();
			} catch (FileNotFoundException e) {
            	LogFactory.log().e(e, "文件不存在");
			} catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
			} finally {
				try {
					if(UtilBoss.ObjUtil.isNotNull(zos)) zos.close();
					if(UtilBoss.ObjUtil.isNotNull(fos)) fos.close();
				} catch (IOException e) {
	            	LogFactory.log().e(e, "输入输出有问题");
				}
			}
		}

		/**
		 * 解压
		 * @param zip
		 * @param dest
		 * @param type
		 */
		public static void unzip(String zip, String dest, String type) {
			UtilBoss.ConditionUtil.n(type);
			if(ParcelBoss.Zip.UNZIP_NEW.equals(type)) {
				unzipNew(zip, dest);
			} else if(ParcelBoss.Zip.UNZIP_OLD.equals(type)) {
				unzipOld(zip, dest);
			}
		}
		
		/**
		 * 压缩文件及文件夹
		 * @param zos
		 * @param src
		 * @param dest
		 */
		private static void zipAll(ZipOutputStream zos, String src, String dest) {
			UtilBoss.ConditionUtil.nt(zos, src, dest);
			
			File file = new File(src);
			if(!file.exists()) UtilBoss.ExceptionUtil.throwUnsupportedOperation("This file not exist.");
			
			if(file.isDirectory()) {
				File[] files = file.listFiles();
				for (File child : files) {
					if(child.isDirectory()) {
						String newDest = dest + PathUtil.separatorBefore(child.getName());
						ParcelBoss.Zip.zipDir(zos, newDest);
						ParcelBoss.Zip.zipAll(zos, child.getPath(), newDest);
					} else ParcelBoss.Zip.zipFile(zos, dest, child);
				}
			} else ParcelBoss.Zip.zipFile(zos, dest, file);
		}
		
		/**
		 * 压缩目录
		 * @param zos
		 * @param dir
		 */
		private static void zipDir(ZipOutputStream zos, String dir) {
			UtilBoss.ConditionUtil.nt(zos, dir);
			
			try {
				if(!dir.endsWith(File.separator)) dir = PathUtil.separatorAfter(dir); //一定要分割符做结尾否者被认为是文件
				ZipEntry entry = new ZipEntry(dir);
				zos.putNextEntry(entry);
				zos.closeEntry();
			} catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
			}
		}
	
		/**
		 * 压缩文件
		 * @param zos
		 * @param dir
		 * @param file
		 */
		private static void zipFile(ZipOutputStream zos, String dir, File file) {
			UtilBoss.ConditionUtil.nt(zos, dir, file);
			InputStream is = null;
			
			try {
				ZipEntry entry = new ZipEntry(dir + PathUtil.separatorBefore(file.getName()));
				zos.putNextEntry(entry);
				
				is = new FileInputStream(file);
				int length = 0;
				byte[] buffer = new byte[ConstantsUtil.BYTE_BUFFER_SIZE];
				
				while((length = is.read(buffer, 0, (ConstantsUtil.BYTE_BUFFER_SIZE))) != -1) {
					zos.write(buffer, 0, length);
				}
				zos.flush();
				zos.closeEntry();
			} catch (FileNotFoundException e) {
            	LogFactory.log().e(e, "文件不存在");
			} catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
			} finally {
				try {
					if(UtilBoss.ObjUtil.isNotNull(is)) is.close();
				} catch (IOException e) {
	            	LogFactory.log().e(e, "输入输出有问题");
				}
			}
		}
	
		/**
		 * 解压 (getNextEntry())
		 * @param zip
		 * @param dest
		 */
		private static void unzipNew(String zip, String dest) {
			UtilBoss.ConditionUtil.n(zip);
			if(UtilBoss.StrUtil.isEmpty(dest)) dest = UtilBoss.StrUtil.substring(zip, 0, zip.lastIndexOf("."));
			
			ZipInputStream zin = null;
			FileInputStream fin = null;
			FileOutputStream fos = null;
			String curName = "";
			ZipEntry entry = null;
			try {
				fin = new FileInputStream(zip);
				zin = new ZipInputStream(fin);
				while(UtilBoss.ObjUtil.isNotNull(entry = zin.getNextEntry())) {
					curName = dest + PathUtil.separatorBefore(entry.getName());
					if(entry.isDirectory()) PathUtil.mkDipPath(curName);
					else {
						PathUtil.mkDipPath(UtilBoss.StrUtil.substring(curName, 0, curName.lastIndexOf(File.separator)));
						File file = new File(curName);
						file.createNewFile();
						
						fos = new FileOutputStream(file);
						int length = 0;
						byte[] buffer = new byte[ConstantsUtil.BYTE_BUFFER_SIZE];
						while((length = zin.read(buffer, 0, ConstantsUtil.BYTE_BUFFER_SIZE)) != -1) {
							fos.write(buffer, 0, length);
							fos.flush();
						}
					}
				}
			} catch (FileNotFoundException e) {
            	LogFactory.log().e(e, "文件不存在");
			} catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
			} finally {
				try {
					if(UtilBoss.ObjUtil.isNotNull(zin)) zin.close();
					if(UtilBoss.ObjUtil.isNotNull(fin)) fin.close();
					if(UtilBoss.ObjUtil.isNotNull(fos)) fos.close();
				} catch (IOException e) {
	            	LogFactory.log().e(e, "输入输出有问题");
				}
			}
		}

		/**
		 * 解压 (element)
		 * @param zip
		 * @param dest
		 */
		private static void unzipOld(String zip, String dest) {
			UtilBoss.ConditionUtil.n(zip);
			if(UtilBoss.StrUtil.isEmpty(dest)) dest = UtilBoss.StrUtil.substring(zip, 0, zip.lastIndexOf("."));
			
			InputStream is = null;
			OutputStream os = null;
			FileOutputStream fos = null;
			ZipFile zipFile = null;
			try {
				zipFile = new ZipFile(zip);
				Enumeration<? extends ZipEntry> entryEnum = zipFile.entries(); //所有文件
				if(UtilBoss.ObjUtil.isNotNull(entryEnum)) {
					ZipEntry zipEntry = null;
					while(entryEnum.hasMoreElements()) {
						zipEntry = entryEnum.nextElement();  //一层目录一层目录遍历
						if(zipEntry.isDirectory()) { //记录路径 getName() 自带路径
							PathUtil.mkDipPath(dest + PathUtil.separatorBefore(zipEntry.getName()));
							continue;
						}

						if(zipEntry.getSize() > 0) { //文件
							String curName = dest + PathUtil.separatorBefore(zipEntry.getName());
							PathUtil.mkDipPath(UtilBoss.StrUtil.substring(curName, 0, curName.lastIndexOf(File.separator)));
							File tmpFile = new File(curName);
							if(!tmpFile.exists()) tmpFile.createNewFile(); //文件实体
							
							fos = new FileOutputStream(tmpFile);
							os = new BufferedOutputStream(fos); //某文件夹的输出流
							is = zipFile.getInputStream(zipEntry); //具体某个的输入流
							int length = 0;
							byte[] buffer = new byte[ConstantsUtil.BYTE_BUFFER_SIZE];
							while((length = is.read(buffer, 0 , ConstantsUtil.BYTE_BUFFER_SIZE)) != -1) {
								os.write(buffer, 0, length);
								os.flush(); //每次刷出
							}
						}
					}
				}
			} catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
			} finally {
				try {
					if(UtilBoss.ObjUtil.isNotNull(is)) is.close();
					if(UtilBoss.ObjUtil.isNotNull(os)) os.close();
					if(UtilBoss.ObjUtil.isNotNull(fos)) fos.close();
				} catch (IOException e) {
	            	LogFactory.log().e(e, "输入输出有问题");
				}
			}
		}
		
		public static final String UNZIP_NEW = "UNZIP_NEW";
		public static final String UNZIP_OLD = "UNZIP_OLD";
	}
}
