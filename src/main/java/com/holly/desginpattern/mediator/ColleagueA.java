package com.holly.desginpattern.mediator;

public  class ColleagueA extends Colleague{

	public ColleagueA(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void notify(String message) {
		System.out.println("ColleagueA 收到消息 <----  "+message);
		
	}
	
	
}
