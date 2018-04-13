package com.holly.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.holly.rmi.service.RmiDemoService;
import com.holly.rmi.service.impl.RmiDemoServiceImpl;

public class TestRMI {

	public static void main(String[] args) throws RemoteException, NamingException {
		System.setProperty("java.rmi.server.host", "127.0.0.1");
		Context context = new InitialContext();
		LocateRegistry.createRegistry(8080);
		RmiDemoService demoService = new RmiDemoServiceImpl();
		context.rebind("rmi://127.0.0.1:8080/demoService", demoService);
		System.out.println("服务器向命名表注册一个远程服务对象");
	}
}
