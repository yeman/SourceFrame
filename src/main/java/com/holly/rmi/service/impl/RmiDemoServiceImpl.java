package com.holly.rmi.service.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.holly.domain.Dept;
import com.holly.domain.UserInfo;
import com.holly.rmi.service.RmiDemoService;

public class RmiDemoServiceImpl extends UnicastRemoteObject implements RmiDemoService{

	public RmiDemoServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public UserInfo queryUserInfo(String userId) throws RemoteException{
		 Dept dept = new Dept("0001", "测试", "0000");
		UserInfo u = new UserInfo(userId, userId,dept, userId, userId);
		return u;
	}

}
