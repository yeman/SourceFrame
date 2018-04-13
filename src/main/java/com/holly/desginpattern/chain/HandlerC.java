package com.holly.desginpattern.chain;

public class HandlerC extends Handler {

	@Override
	public void chain(Handler handler) {
		
		if("HandlerC".equals(handler.getClass().getSimpleName())){
			System.out.println("HandlerC 处理请求");
		}else{
			chain.chain(handler);
		}
		
	}

}
