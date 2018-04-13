package com.holly.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.holly.webservice.adapter.NillableAdapter;

@XmlRootElement
public class Dept implements Serializable {
	
	private String deptId;
	
	
	private String deptName;
	
	
	private String parentDeptId;
	
	@XmlJavaTypeAdapter(value=NillableAdapter.class)
	@XmlElement(name="deptId",required=true,nillable=true,type=String.class)
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@XmlJavaTypeAdapter(value=NillableAdapter.class)
	@XmlElement(name="deptName",required=true,nillable=true,type=String.class)
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@XmlJavaTypeAdapter(value=NillableAdapter.class)
	@XmlElement(name="parentDeptId",required=true,nillable=true,type=String.class)
	public String getParentDeptId() {
		return parentDeptId;
	}

	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = parentDeptId;
	}

	public Dept(String deptId, String deptName, String parentDeptId) {
		super();
		this.deptId = deptId;
		this.deptName = deptName;
		this.parentDeptId = parentDeptId;
	}

	public Dept() {
		super();
	}

	@Override
	public String toString() {
		return "Dept [deptId=" + deptId + ", deptName=" + deptName + ", parentDeptId=" + parentDeptId + "]";
	}
	
}
