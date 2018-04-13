package com.holly.webservice.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.holly.domain.Dept;
import com.holly.domain.UserInfo;

@WebService(serviceName="myservice",name="test" , endpointInterface="com.holly.webservice.jaxws.IMyService")
public class MyServiceImpl implements IMyService {
	private static List<UserInfo> userInfoList = new ArrayList<UserInfo>();
	
	@Override
	public String sayHello(String content) {
		System.out.println("sayHello:"+content);
		return "hi:"+content;
	}

	@Override
	public @WebResult(name="userInfo")UserInfo login(@WebParam(name="userName") String userName, @WebParam(name="password")String password) {
		UserInfo db = new UserInfo();
		db.setUserName(userName);
		db.setPasswd(password);
		db.setDept(new Dept("org001", "测试部门", "org0000"));
		return db;
	}

	@Override
	public UserInfo add(UserInfo userinfo) {
		userInfoList.add(userinfo);
		return userinfo;
	}

	@Override
	public List<UserInfo> userInfolist() {
		return userInfoList;
	}

}
