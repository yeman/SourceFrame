package com.holly.desginpattern.chain;

public class HandlerA extends Handler {

	@Override
	public void chain(Handler handler) {
		
		if("HandlerA".equals(handler.getClass().getSimpleName())){
			System.out.println("HandlerA 处理请求");
		}else{
			chain.chain(handler);
		}
		
	}

}
