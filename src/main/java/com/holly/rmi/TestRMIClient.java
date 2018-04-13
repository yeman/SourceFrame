package com.holly.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.holly.domain.UserInfo;
import com.holly.rmi.service.RmiDemoService;

public class TestRMIClient {

	public static void main(String[] args) throws RemoteException {
		String url="rmi://127.0.0.1:8080/";
		String serviceName="demoService";
		try {
			Context context = new InitialContext();
			RmiDemoService demoService = (RmiDemoService)context.lookup(url+serviceName);
			System.out.println("demoService是"+demoService.getClass().getName()+"的实例");
			Class[] interfaces =demoService.getClass().getInterfaces();
			for(Class c: interfaces){
				System.out.println("@@@@"+c.getName());
			}
			UserInfo userInfo = demoService.queryUserInfo("1");
			System.out.println(userInfo);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
