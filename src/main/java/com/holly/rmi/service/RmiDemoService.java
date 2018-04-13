package com.holly.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.holly.domain.UserInfo;

public interface RmiDemoService extends Remote{
	public UserInfo queryUserInfo(String userId) throws RemoteException; 
}
