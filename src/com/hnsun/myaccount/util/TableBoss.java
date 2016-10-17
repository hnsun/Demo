package com.hnsun.myaccount.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hnsun.myaccount.util.log.LogFactory;

/**
 * 数据表相对应类的属性名
 * @author hnsun
 * @date 2016/08/15
 * 类型：CfgSystable, CfgSystablecolumn, CfgSystablecolumnkey, CfgSyslog, CfgSysinternationalization, CfgSysmessage, CfgSyspredefine, CfgSysintroduction, TblUser, TblRole, TblAuthority, 
 *           RelUserrole, RelRoleauth, TblKind, TblItem, TblAccount, TblEstate, TblConsumption, TblTestconn, PlfDownload,
 */
public class TableBoss {

	private TableBoss() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

	/**
	 * 判断是否为字段名
	 * @param name
	 * @return
	 */
	public static boolean isColumnName(String name) {
		UtilBoss.ConditionUtil.n(name);
		
		return !("TAG".equals(name) || "CLASS".equals(name) || "TYPES".equals(name) || "INDEXS".equals(name));
	}

	/**
	 * 将属性名变换成数据库用的名
	 * @param value
	 * @return
	 */
	public static String toDatabaseName(String value) {
		String ret = value;
		UtilBoss.ConditionUtil.n(value);
		
		ret = UtilBoss.StrUtil.downFirstLetter(ret); //第一字母小写
		Pattern pattern = Pattern.compile("[A-Z]+"); //匹配大写
		Matcher matcher = pattern.matcher(ret);
		
		while(matcher.find()) {
			String str = matcher.group();
			ret = ret.replaceFirst(str, ("_" + str.toLowerCase(Locale.getDefault())));
		}
		
		return ret.toUpperCase(Locale.getDefault());
	}

	/**
	 * 将属性名变换成字段名
	 * @param value
	 * @return
	 */
	public static String toColumnName(String value) {
		String ret = value;
		UtilBoss.ConditionUtil.n(value);
		
		ret = "[" + TableBoss.toDatabaseName(ret) + "]";
		return ret;
	}

	/**
	 * 获取当前class属性名及与其相对应的所有字段名
	 * @param clazz
	 * @return
	 */
	public static Map<String, String> valueColumns(String clazz) {
		Map<String, String> ret = new HashMap<String, String>();
		UtilBoss.ConditionUtil.n(clazz);

		try {
			Field[] fields = ReflectUtil.getClazz(clazz).getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				if(TableBoss.isColumnName(fieldName)) ret.put((String) field.get(clazz), fieldName);
			}
		} catch (IllegalAccessException e) {
			LogFactory.log().e(e, "没有权限获取");
		}
		
		return ret;
	}
	
	/************************************************
	 * 表表：CFG_SYSTABLE
	 *************************************************/
	public static class CfgSystable {

		private CfgSystable() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String SYSTABLE_ID = "systableId";
		public static final String SYSTABLE_NAME = "systableName";
		public static final String SYSTABLE_FKINFOS = "systableFkinfos";
		public static final String SYSTABLE_ISCASCADE = "systableIscascade";
		public static final String SYSTABLE_UPDATEINIT = "systableUpdateinit";
		public static final String SYSTABLE_ANDSORT = "systableAndsort";
		public static final String SYSTABLE_KEYCASE = "systableKeycase";
		public static final String SYSTABLE_DESC = "systableDesc";
		
		public static final String TAG = "CfgSystable";
		public static final String CLASS = CfgSystable.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "String", "String", "String", "String", "String", }; 
		public static final String[] INDEXS = { TableBoss.CfgSystable.SYSTABLE_ID, TableBoss.CfgSystable.SYSTABLE_NAME, TableBoss.CfgSystable.SYSTABLE_FKINFOS, TableBoss.CfgSystable.SYSTABLE_ISCASCADE, TableBoss.CfgSystable.SYSTABLE_UPDATEINIT, TableBoss.CfgSystable.SYSTABLE_ANDSORT, TableBoss.CfgSystable.SYSTABLE_KEYCASE, TableBoss.CfgSystable.SYSTABLE_DESC, }; 
	}
	
	/************************************************
	 * 表字段表：CFG_SYSTABLECOLUMN
	 *************************************************/
	public static class CfgSystablecolumn {

		private CfgSystablecolumn() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String SYSTABLECOLUMN_ID = "systablecolumnId";
		public static final String SYSTABLECOLUMN_NAME = "systablecolumnName";
		public static final String SYSTABLECOLUMN_ANDUPPER = "systablecolumnAndupper";
		public static final String SYSTABLECOLUMN_NOTUPDATE = "systablecolumnNotupdate";
		public static final String SYSTABLECOLUMN_TOANDROID = "systablecolumnToandroid";
		public static final String SYSTABLECOLUMN_KEYCASE = "systablecolumnKeycase";
		public static final String SYSTABLECOLUMN_DESC = "systablecolumnDesc";
		public static final String SYSTABLE_ID = "systableId";
		
		public static final String TAG = "CfgSystablecolumn";
		public static final String CLASS = CfgSystablecolumn.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "String", "String", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.CfgSystablecolumn.SYSTABLECOLUMN_ID, TableBoss.CfgSystablecolumn.SYSTABLECOLUMN_NAME, TableBoss.CfgSystablecolumn.SYSTABLECOLUMN_ANDUPPER, TableBoss.CfgSystablecolumn.SYSTABLECOLUMN_NOTUPDATE, TableBoss.CfgSystablecolumn.SYSTABLECOLUMN_TOANDROID, TableBoss.CfgSystablecolumn.SYSTABLECOLUMN_KEYCASE, TableBoss.CfgSystablecolumn.SYSTABLECOLUMN_DESC, TableBoss.CfgSystablecolumn.SYSTABLE_ID, };
	}
	
	/************************************************
	 * 表字段关键字表：CFG_SYSTABLECOLUMNKEY
	 *************************************************/
	public static class CfgSystablecolumnkey {

		private CfgSystablecolumnkey() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String SYSTABLECOLUMNKEY_ID = "systablecolumnkeyId";
		public static final String SYSTABLECOLUMNKEY_NAME = "systablecolumnkeyName";
		public static final String SYSTABLECOLUMNKEY_PRIORITY = "systablecolumnkeyPriority";
		public static final String SYSTABLECOLUMNKEY_GROUP = "systablecolumnkeyGroup";
		public static final String SYSTABLECOLUMNKEY_INCOMBINATIVE = "systablecolumnkeyIncombinative";
		public static final String SYSTABLECOLUMNKEY_DESC = "systablecolumnkeyDesc";
		public static final String SYSTABLECOLUMN_ID = "systablecolumnId";
		
		public static final String TAG = "CfgSystablecolumnkey";
		public static final String CLASS = CfgSystablecolumnkey.class.getName();
		public static final String[] TYPES = { "String", "String", "int", "int", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.CfgSystablecolumnkey.SYSTABLECOLUMNKEY_ID, TableBoss.CfgSystablecolumnkey.SYSTABLECOLUMNKEY_NAME, TableBoss.CfgSystablecolumnkey.SYSTABLECOLUMNKEY_PRIORITY, TableBoss.CfgSystablecolumnkey.SYSTABLECOLUMNKEY_GROUP, TableBoss.CfgSystablecolumnkey.SYSTABLECOLUMNKEY_INCOMBINATIVE, TableBoss.CfgSystablecolumnkey.SYSTABLECOLUMNKEY_DESC, TableBoss.CfgSystablecolumnkey.SYSTABLECOLUMN_ID, };
	}
	
	/************************************************
	 * 日志表：CFG_SYSLOG
	 *************************************************/
	public static class CfgSyslog {

		private CfgSyslog() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String SYSLOG_ID = "syslogId";
		public static final String SYSLOG_HOSTNAME = "syslogHostname";
		public static final String SYSLOG_LOGINNAME = "syslogLoginname";
		public static final String SYSLOG_DATE = "syslogDate";
		public static final String SYSLOG_STATEMENT = "syslogStatement";

		public static final String TAG = "CfgSyslog";
		public static final String CLASS = CfgSyslog.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "Date", "String", };
		public static final String[] INDEXS = { TableBoss.CfgSyslog.SYSLOG_ID, TableBoss.CfgSyslog.SYSLOG_HOSTNAME, TableBoss.CfgSyslog.SYSLOG_LOGINNAME, TableBoss.CfgSyslog.SYSLOG_DATE, TableBoss.CfgSyslog.SYSLOG_STATEMENT, };
	}
	
	/************************************************
	 * 国际化表：CFG_SYSINTERNATIONALIZATION
	 *************************************************/
	public static class CfgSysinternationalization {

		private CfgSysinternationalization() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String SYSINTERNATIONALIZATION_ID = "sysinternationalizationId";
		public static final String SYSINTERNATIONALIZATION_CHINESE = "sysinternationalizationChinese";
		public static final String SYSINTERNATIONALIZATION_ENGLISH = "sysinternationalizationEnglish";
		public static final String SYSINTERNATIONALIZATION_RESOURCE = "sysinternationalizationResource";
		public static final String SYSINTERNATIONALIZATION_WHERE = "sysinternationalizationWhere";

		public static final String TAG = "CfgSysinternationalization";
		public static final String CLASS = CfgSysinternationalization.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.CfgSysinternationalization.SYSINTERNATIONALIZATION_ID, TableBoss.CfgSysinternationalization.SYSINTERNATIONALIZATION_CHINESE, TableBoss.CfgSysinternationalization.SYSINTERNATIONALIZATION_ENGLISH, TableBoss.CfgSysinternationalization.SYSINTERNATIONALIZATION_RESOURCE, TableBoss.CfgSysinternationalization.SYSINTERNATIONALIZATION_WHERE, };
	}
	
	/************************************************
	 * 系统信息表：CFG_SYSMESSAGE
	 *************************************************/
	public static class CfgSysmessage {

		private CfgSysmessage() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String SYSMESSAGE_ID = "sysmessageId";
		public static final String SYSMESSAGE_CONTENT = "sysmessageContent";
		public static final String SYSMESSAGE_CODE = "sysmessageCode";
		public static final String SYSMESSAGE_KEYSECTION = "sysmessageKeysection";
		public static final String SYSMESSAGE_TYPE = "sysmessageType";
		public static final String SYSMESSAGE_DESC = "sysmessageDesc";

		public static final String TAG = "CfgSysmessage";
		public static final String CLASS = CfgSysmessage.class.getName();
		public static final String[] TYPES = { "String", "String", "int", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.CfgSysmessage.SYSMESSAGE_ID, TableBoss.CfgSysmessage.SYSMESSAGE_CONTENT, TableBoss.CfgSysmessage.SYSMESSAGE_CODE, TableBoss.CfgSysmessage.SYSMESSAGE_KEYSECTION, TableBoss.CfgSysmessage.SYSMESSAGE_TYPE, TableBoss.CfgSysmessage.SYSMESSAGE_DESC, };
	}
	
	/************************************************
	 * 预定义表：CFG_SYSPREDEFINE
	 *************************************************/
	public static class CfgSyspredefine {

		private CfgSyspredefine() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String SYSPREDEFINE_ID = "syspredefineId";
		public static final String SYSPREDEFINE_NAME = "syspredefineName";
		public static final String SYSPREDEFINE_CONTENT = "syspredefineContent";
		public static final String SYSPREDEFINE_USAGE = "syspredefineUsage";
		public static final String SYSPREDEFINE_KEYTYPE = "syspredefineKeytype";
		public static final String SYSPREDEFINE_FROMTYPE = "syspredefineFromtype";
		public static final String SYSPREDEFINE_DESC = "syspredefineDesc";

		public static final String TAG = "CfgSyspredefine";
		public static final String CLASS = CfgSyspredefine.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "String", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.CfgSyspredefine.SYSPREDEFINE_ID, TableBoss.CfgSyspredefine.SYSPREDEFINE_NAME, TableBoss.CfgSyspredefine.SYSPREDEFINE_CONTENT, TableBoss.CfgSyspredefine.SYSPREDEFINE_USAGE, TableBoss.CfgSyspredefine.SYSPREDEFINE_KEYTYPE, TableBoss.CfgSyspredefine.SYSPREDEFINE_FROMTYPE, TableBoss.CfgSyspredefine.SYSPREDEFINE_DESC, };
	}
	
	/************************************************
	 * 相关说明表：CFG_SYSINTRODUCTION
	 *************************************************/
	public static class CfgSysintroduction {

		private CfgSysintroduction() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String SYSINTRODUCTION_ID = "sysintroductionId";
		public static final String SYSINTRODUCTION_NAME = "sysintroductionName";
		public static final String SYSINTRODUCTION_CONTENT = "sysintroductionContent";
		public static final String SYSINTRODUCTION_KEYSECTION = "sysintroductionKeysection";
		public static final String SYSINTRODUCTION_TYPE = "sysintroductionType";

		public static final String TAG = "CfgSysintroduction";
		public static final String CLASS = CfgSysintroduction.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.CfgSysintroduction.SYSINTRODUCTION_ID, TableBoss.CfgSysintroduction.SYSINTRODUCTION_NAME, TableBoss.CfgSysintroduction.SYSINTRODUCTION_CONTENT, TableBoss.CfgSysintroduction.SYSINTRODUCTION_KEYSECTION, TableBoss.CfgSysintroduction.SYSINTRODUCTION_TYPE, };
	}
	
	/************************************************
	 * 用户表：TBL_USER
	 *************************************************/
	public static class TblUser {

		private TblUser() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String USER_ID = "userId";
		public static final String USER_LOGNAME = "userLogname";
		public static final String USER_LOGPASSWORD = "userLogpassword";
		public static final String USER_CNNAME = "userCnname";
		public static final String USER_ENNAME = "userEnname";
		public static final String USER_INFO = "userInfo";

		public static final String TAG = "TblUser";
		public static final String CLASS = TblUser.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.TblUser.USER_ID, TableBoss.TblUser.USER_LOGNAME, TableBoss.TblUser.USER_LOGPASSWORD, TableBoss.TblUser.USER_CNNAME, TableBoss.TblUser.USER_ENNAME, TableBoss.TblUser.USER_INFO, };
	}
	
	/************************************************
	 * 角色表：TBL_ROLE
	 *************************************************/
	public static class TblRole {

		private TblRole() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String ROLE_ID = "roleId";
		public static final String ROLE_NAME = "roleName";
		public static final String ROLE_KEYSECTION = "roleKeysection";
		public static final String ROLE_DESC = "roleDesc";

		public static final String TAG = "TblRole";
		public static final String CLASS = TblRole.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.TblRole.ROLE_ID, TableBoss.TblRole.ROLE_NAME, TableBoss.TblRole.ROLE_KEYSECTION, TableBoss.TblRole.ROLE_DESC, };
	}
	
	/************************************************
	 * 权限表：TBL_AUTHORITY
	 *************************************************/
	public static class TblAuthority {

		private TblAuthority() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String AUTHORITY_ID = "authorityId";
		public static final String AUTHORITY_NAME = "authorityName";
		public static final String AUTHORITY_KEYSECTION = "authorityKeysection";
		public static final String AUTHORITY_DESC = "authorityDesc";

		public static final String TAG = "TblAuthority";
		public static final String CLASS = TblAuthority.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.TblAuthority.AUTHORITY_ID, TableBoss.TblAuthority.AUTHORITY_NAME, TableBoss.TblAuthority.AUTHORITY_KEYSECTION, TableBoss.TblAuthority.AUTHORITY_DESC, };
	}
	
	/************************************************
	 * 用户角色表：REL_USERROLE
	 *************************************************/
	public static class RelUserrole {

		private RelUserrole() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String USERROLE_ID = "userroleId";
		public static final String USER_ID = "userId";
		public static final String ROLE_ID = "roleId";

		public static final String TAG = "RelUserrole";
		public static final String CLASS = RelUserrole.class.getName();
		public static final String[] TYPES = { "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.RelUserrole.USERROLE_ID, TableBoss.RelUserrole.USER_ID, TableBoss.RelUserrole.ROLE_ID, };
	}
	
	/************************************************
	 * 角色权限表：REL_ROLEAUTH
	 *************************************************/
	public static class RelRoleauth {

		private RelRoleauth() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String ROLEAUTH_ID = "roleauthId";
		public static final String ROLE_ID = "roleId";
		public static final String AUTHORITY_ID = "authorityId";

		public static final String TAG = "RelRoleauth";
		public static final String CLASS = RelRoleauth.class.getName();
		public static final String[] TYPES = { "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.RelRoleauth.ROLEAUTH_ID, TableBoss.RelRoleauth.ROLE_ID, TableBoss.RelRoleauth.AUTHORITY_ID, };
	}
	
	/************************************************
	 * 分类表：TBL_KIND
	 *************************************************/
	public static class TblKind {

		private TblKind() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String KIND_ID = "kindId";
		public static final String KIND_NAME = "kindName";
		public static final String KIND_INDEX = "kindIndex";
		public static final String KIND_NUMSTR = "kindNumstr";
		public static final String KIND_DESC = "kindDesc";
		public static final String KIND_UPPER_ID = "kindUpperId";

		public static final String TAG = "TblKind";
		public static final String CLASS = TblKind.class.getName();
		public static final String[] TYPES = { "String", "String", "int", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.TblKind.KIND_ID, TableBoss.TblKind.KIND_NAME, TableBoss.TblKind.KIND_INDEX, TableBoss.TblKind.KIND_NUMSTR, TableBoss.TblKind.KIND_DESC, TableBoss.TblKind.KIND_UPPER_ID, };
	}
	
	/************************************************
	 * 物品表：TBL_ITEM
	 *************************************************/
	public static class TblItem {

		private TblItem() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String ITEM_ID = "itemId";
		public static final String ITEM_NAME = "itemName";
		public static final String ITEM_CALL = "itemCall";
		public static final String ITEM_INDEX = "itemIndex";
		public static final String ITEM_ICON = "itemIcon";
		public static final String ITEM_PHOTO = "itemPhoto";
		public static final String ITEM_DESC = "itemDesc";
		public static final String KIND_ID = "kindId";

		public static final String TAG = "TblItem";
		public static final String CLASS = TblItem.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "int", "String", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.TblItem.ITEM_ID, TableBoss.TblItem.ITEM_NAME, TableBoss.TblItem.ITEM_CALL, TableBoss.TblItem.ITEM_INDEX, TableBoss.TblItem.ITEM_ICON, TableBoss.TblItem.ITEM_PHOTO, TableBoss.TblItem.ITEM_DESC, TableBoss.TblItem.KIND_ID, };
	}
	
	/************************************************
	 * 文件表：TBL_FILE
	 *************************************************/
	public static class TblFile {

		private TblFile() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String FILE_ID = "fileId";
		public static final String FILE_NAME = "fileName";
		public static final String FILE_PLATFORM = "filePlatform";
		public static final String FILE_URL = "fileUrl";
		public static final String FILE_DATE = "fileDate";
		public static final String FILE_DESC = "fileDesc";

		public static final String TAG = "TblFile";
		public static final String CLASS = TblFile.class.getName();
		public static final String[] TYPES = { "String", "String", "String", "String", "Date", "String", };
		public static final String[] INDEXS = { TableBoss.TblFile.FILE_ID, TableBoss.TblFile.FILE_NAME, TableBoss.TblFile.FILE_PLATFORM, TableBoss.TblFile.FILE_URL, TableBoss.TblFile.FILE_DATE, TableBoss.TblFile.FILE_DESC, };
	}
	
	/************************************************
	 * 账本表：TBL_ACCOUNT
	 *************************************************/
	public static class TblAccount {

		private TblAccount() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String ACCOUNT_ID = "accountId";
		public static final String ACCOUNT_DATE = "accountDate";
		public static final String ACCOUNT_VIRTUALSUM = "accountVirtualsum";
		public static final String ACCOUNT_REALSUM = "accountRealsum";
		public static final String ACCOUNT_INTENT = "accountIntent";
		public static final String ACCOUNT_CAUSATION = "accountCausation";
		public static final String USER_ID = "userId";

		public static final String TAG = "TblAccount";
		public static final String CLASS = TblAccount.class.getName();
		public static final String[] TYPES = { "String", "Date", "BigDecimal", "BigDecimal", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.TblAccount.ACCOUNT_ID, TableBoss.TblAccount.ACCOUNT_DATE, TableBoss.TblAccount.ACCOUNT_VIRTUALSUM, TableBoss.TblAccount.ACCOUNT_REALSUM, TableBoss.TblAccount.ACCOUNT_INTENT, TableBoss.TblAccount.ACCOUNT_CAUSATION, TableBoss.TblAccount.USER_ID, };
	}
	
	/************************************************
	 * 不动产表：TBL_ESTATE
	 *************************************************/
	public static class TblEstate {

		private TblEstate() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String ESTATE_ID = "estateId";
		public static final String ESTATE_DATE = "estateDate";
		public static final String ESTATE_SUM = "estateCost";
		public static final String USER_ID = "userId";

		public static final String TAG = "TblEstate";
		public static final String CLASS = TblEstate.class.getName();
		public static final String[] TYPES = { "String", "Date", "BigDecimal", "String", };
		public static final String[] INDEXS = { TableBoss.TblEstate.ESTATE_ID, TableBoss.TblEstate.ESTATE_DATE, TableBoss.TblEstate.ESTATE_SUM, TableBoss.TblEstate.USER_ID, };
	}
	
	/************************************************
	 * 消费记录表：TBL_CONSUMPTION
	 *************************************************/
	public static class TblConsumption {

		private TblConsumption() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String CONSUMPTION_ID = "consumptionId";
		public static final String CONSUMPTION_DATE = "consumptionDate";
		public static final String CONSUMPTION_COST = "consumptionCost";
		public static final String CONSUMPTION_INOUT = "consumptionInout";
		public static final String CONSUMPTION_NECESSARY = "consumptionNecessary";
		public static final String CONSUMPTION_FROMTYPE = "consumptionFromtype";
		public static final String CONSUMPTION_EXTRADESC = "consumptionExtradesc";
		public static final String USER_ID = "userId";
		public static final String ITEM_ID = "itemId";

		public static final String TAG = "TblConsumption";
		public static final String CLASS = TblConsumption.class.getName();
		public static final String[] TYPES = { "String", "Date", "BigDecimal", "String", "String", "String", "String", "String", "String", };
		public static final String[] INDEXS = { TableBoss.TblConsumption.CONSUMPTION_ID, TableBoss.TblConsumption.CONSUMPTION_DATE, TableBoss.TblConsumption.CONSUMPTION_COST, TableBoss.TblConsumption.CONSUMPTION_INOUT, TableBoss.TblConsumption.CONSUMPTION_NECESSARY, TableBoss.TblConsumption.CONSUMPTION_FROMTYPE, TableBoss.TblConsumption.CONSUMPTION_EXTRADESC, TableBoss.TblConsumption.USER_ID, TableBoss.TblConsumption.ITEM_ID, };
	}
	
	/************************************************
	 * 测试表：TBL_TESTCONN
	 *************************************************/
	public static class TblTestconn {

		private TblTestconn() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String TESTCONN_ID = "testconnId";
		public static final String TESTCONN_CONTENT = "testconnContent";

		public static final String TAG = "TblTestconn";
		public static final String CLASS = TblTestconn.class.getName();
		public static final String[] TYPES = { "String", "String", };
		public static final String[] INDEXS = { TableBoss.TblTestconn.TESTCONN_ID, TableBoss.TblTestconn.TESTCONN_CONTENT, };
	}
	
	/************************************************
	 * 下载记录表：PLF_DOWNLOADRECORD
	 *************************************************/
	public static class PlfDownloadrecord {

		private PlfDownloadrecord() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String DOWNLOADRECORD_ID = "downloadrecordId";
		public static final String DOWNLOADRECORD_DATE = "downloadrecordDate";
		public static final String FILE_ID = "fileId";

		public static final String TAG = "PlfDownloadrecord";
		public static final String CLASS = PlfDownloadrecord.class.getName();
		public static final String[] TYPES = { "String", "Date", "String", };
		public static final String[] INDEXS = { TableBoss.PlfDownloadrecord.DOWNLOADRECORD_ID, TableBoss.PlfDownloadrecord.DOWNLOADRECORD_DATE, TableBoss.PlfDownloadrecord.FILE_ID, };
	}
	
	/************************************************
	 * 下载线程管理表：PLF_DOWNLOADTHREAD
	 *************************************************/
	public static class PlfDownloadthread {

		private PlfDownloadthread() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
		
		public static final String DOWNLOADTHREAD_ID = "downloadthreadId";
		public static final String DOWNLOADTHREAD_URL = "downloadthreadUrl";
		public static final String DOWNLOADTHREAD_THREAD = "downloadthreadThread";
		public static final String DOWNLOADTHREAD_START = "downloadthreadStart";
		public static final String DOWNLOADTHREAD_END = "downloadthreadEnd";
		public static final String DOWNLOADTHREAD_COMPLETED = "downloadthreadCompleted";

		public static final String TAG = "PlfDownloadthread";
		public static final String CLASS = PlfDownloadthread.class.getName();
		public static final String[] TYPES = { "String", "String", "Integer", "Integer", "Integer", "Integer", };
		public static final String[] INDEXS = { TableBoss.PlfDownloadthread.DOWNLOADTHREAD_ID, TableBoss.PlfDownloadthread.DOWNLOADTHREAD_URL, TableBoss.PlfDownloadthread.DOWNLOADTHREAD_THREAD, TableBoss.PlfDownloadthread.DOWNLOADTHREAD_START, TableBoss.PlfDownloadthread.DOWNLOADTHREAD_END, TableBoss.PlfDownloadthread.DOWNLOADTHREAD_COMPLETED, };
	}
	
	public static final String LAST_UPDATE_DATE = "lastUpdateDate";
	public static final String DELETED = "deleted";

	public static final String CLASS = TableBoss.class.getName();
	public static final String[] TYPES = { "Date", "String", };
}
