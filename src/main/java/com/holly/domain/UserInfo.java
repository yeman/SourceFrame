package com.holly.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.holly.webservice.adapter.NillableAdapter;

@XmlRootElement
public class UserInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3871577297578024702L;
	

	private String userId;

	private String userCode;
	private Dept dept;
	
	private String userName;

	private String passwd;
	
	@XmlJavaTypeAdapter(value=NillableAdapter.class)
	@XmlElement(name="userId",required=true,nillable=true,type=String.class)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	@XmlJavaTypeAdapter(value=NillableAdapter.class)
	@XmlElement(name="userCode",required=true,nillable=true,type=String.class)
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	@XmlJavaTypeAdapter(value=NillableAdapter.class)
	@XmlElement(name="userName",required=true,nillable=true,type=String.class)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@XmlJavaTypeAdapter(value=NillableAdapter.class)
	@XmlElement(name="password",required=true,nillable=true,type=String.class)
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	@XmlJavaTypeAdapter(value=NillableAdapter.class)
	@XmlElement(name="dept", required=true,nillable=true,type=Dept.class)
	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public UserInfo() {
		super();
	}

	public UserInfo(String userId, String userCode, Dept dept, String userName, String passwd) {
		super();
		this.userId = userId;
		this.userCode = userCode;
		this.dept = dept;
		this.userName = userName;
		this.passwd = passwd;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userCode=" + userCode + ", dept=" + dept + ", userName=" + userName
				+ ", passwd=" + passwd + "]";
	}

}
