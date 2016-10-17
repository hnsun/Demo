package com.hnsun.myaccount.model.dbo;

import java.io.Serializable;
import java.util.Date;

import com.hnsun.myaccount.util.data.json.NotSerialAnnotation;

/**
 * 用户
 * @author hnsun
 * @date 2016/09/18
 */
public class TblUser implements Serializable {

	public TblUser() {}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserLogname() {
		return userLogname;
	}

	public void setUserLogname(String userLogname) {
		this.userLogname = userLogname;
	}

	public String getUserLogpassword() {
		return userLogpassword;
	}

	public void setUserLogpassword(String userLogpassword) {
		this.userLogpassword = userLogpassword;
	}

	public String getUserCnname() {
		return userCnname;
	}

	public void setUserCnname(String userCnname) {
		this.userCnname = userCnname;
	}

	public String getUserEnname() {
		return userEnname;
	}

	public void setUserEnname(String userEnname) {
		this.userEnname = userEnname;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public char getDeleted() {
		return deleted;
	}

	public void setDeleted(char deleted) {
		this.deleted = deleted;
	}

	private String userId;
	private String userLogname;
	private String userLogpassword;
	private String userCnname;
	private String userEnname;
	private String userInfo;
	
	@NotSerialAnnotation private Date lastUpdateDate;
	@NotSerialAnnotation private char deleted;
	
	private static final long serialVersionUID = 1L;
	
	{
		this.deleted = 'N';
	}
}
