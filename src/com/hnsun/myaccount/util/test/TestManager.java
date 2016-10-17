package com.hnsun.myaccount.util.test;

import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 程序测试入口 通过将代码放置程序某处
 * @author hnsun
 * @date 2016/08/15
 */
public class TestManager {
	
    /************************************************************************************
     **************************** 系统上测试
     ************************************************************************************/

	/**
	 * 无返回值的测试（系统）
	 */
	public static void testVoid(Object... params) { 
		try {
//			String s = "你好，马勒戈壁";
//			String ss = CryptionBoss.Base64.encode(s);
//			String sss = CryptionBoss.Base64.decode("dWFjY291bnQ=");
//			System.out.println(ss);
//			System.out.println(sss);
//			System.out.println(UtilBoss.ObjUtil.getByStr("d", char.class));
			
//			TblUser record = new TblUser();
//			record.setUserId(CodeBoss.MessCode.uuid());
//			record.setUserInfo("你好 大笨猪");
//			record.setUserCnname("卡拉机");
//			record.setUserLogname("啦卡机");
//			record.setLastUpdateDate(new Date());
//			XMLByDOM4j.generateXmlFile(new XMLByDOM4j<TblUser>(record).toDocument(), PathUtil.SystemPath.get(PathUtil.SystemPath.TEST) + PathUtil.separatorBefore("test.xml"));
//			List<TblUser> list = new XMLByDOM4j<TblUser>(TblUser.class).fromDocument(XMLByDOM4j.fromXmlFile(PathUtil.SystemPath.get(PathUtil.SystemPath.TEST) + PathUtil.separatorBefore("test.xml")));
//			System.out.println("ID " + list.get(0).getUserId());
			
//			ParcelBoss.Zip.zip(PathUtil.SystemPath.get(PathUtil.SystemPath.TEST) + PathUtil.separatorBefore("test.xml"), null);
//			ParcelBoss.Zip.unzip(PathUtil.SystemPath.get(PathUtil.SystemPath.TEST) + PathUtil.separatorBefore("test.zip"), PathUtil.SystemPath.get(PathUtil.SystemPath.TEST), ParcelBoss.Zip.UNZIP_NEW);
		} catch (Exception e) {
			LogFactory.log().e(e);
			LogFactory.log().e(e, "sssss");
		}
	}

	/**
	 * 有返回值的测试（系统）
	 */
	public static Object testRet(Object... params) {
		try {
			
		} catch (Exception e) {
			LogFactory.log().e(e);
		}
		
		return null;
	}
	
    /************************************************************************************
     **************************** 本类中调试
     ************************************************************************************/
	
	public static void main(String[] args) { //基本测试
	}
}
