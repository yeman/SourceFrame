package com.holly.webservice.jaxws;

import javax.xml.ws.Endpoint;

public class MyServiceServer {
	public static void main(String[] args) {
		String address = "http://localhost:9999/ns";
		Endpoint.publish(address, new MyServiceImpl());
		System.out.println("server start ...");
	}
}
