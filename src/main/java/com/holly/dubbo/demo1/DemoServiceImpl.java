package com.holly.dubbo.demo1;

public class DemoServiceImpl implements DemoService {

	@Override
	public String saySomething(String str) {
		
		return "test "+str;
	}

}
