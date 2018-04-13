package com.holly.webservice.jaxws;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.holly.domain.UserInfo;

@WebService
public interface IMyService {
	
	@WebResult(name="result")
	public String  sayHello(@WebParam(name="content")String content);
	
	@WebResult(name="userInfo")
	public UserInfo login(@WebParam(name="username") String userName,@WebParam(name="password") String password);
	
	@WebResult(name="userInfo")
	public UserInfo add(@WebParam(name="userInfo") UserInfo userinfo);
	
	@WebResult(name="userInfoList")
	public List<UserInfo> userInfolist();
		
	
}
