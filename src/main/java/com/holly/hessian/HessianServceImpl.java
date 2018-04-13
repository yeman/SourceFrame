package com.holly.hessian;

import java.util.HashMap;

import com.holly.domain.Dept;
import com.holly.domain.UserInfo;

public class HessianServceImpl implements IHessianService{
	private static HashMap<String,UserInfo> map;
	static{
		Dept dept = new Dept("org0001","测试部","org0000");
		UserInfo userinfo1 = new UserInfo("u0001", "u0001", dept, "测试", "123");
		UserInfo userinfo2 = new UserInfo("u0002", "u0001", dept, "测试", "123");
		map.put("u0001", userinfo1);
		map.put("u0002", userinfo2);
	}
	@Override
	public UserInfo queryUserInfo(String userId) {
		
		return map.get(userId);
	}

}
