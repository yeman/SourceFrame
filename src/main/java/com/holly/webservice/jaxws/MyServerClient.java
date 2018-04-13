package com.holly.webservice.jaxws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.holly.domain.UserInfo;

public class MyServerClient {

	public static void main(String[] args) {
		try {
			URL url = new URL("http://localhost:9999/ns");
			String nameSpace="http://jaxws.webservice.holly.com/";
			QName qname = new QName(nameSpace,"myservice");
			Service service = Service.create(url, qname);
			IMyService myservice = service.getPort(IMyService.class);
			myservice.sayHello("你好");	
			UserInfo userinfo = myservice.login("jack", "123456");
			System.out.println("userinfo"+ userinfo);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
