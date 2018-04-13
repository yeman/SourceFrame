package com.holly.desginpattern.chain;

public class HandlerB extends Handler {

	@Override
	public void chain(Handler handler) {
		
		if("HandlerB".equals(handler.getClass().getSimpleName())){
			System.out.println("HandlerB 处理请求");
		}else{
			chain.chain(handler);
		}
		
	}

}
