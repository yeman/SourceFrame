package com.holly.desginpattern.mediator;

public  class ColleagueB extends Colleague{

	public ColleagueB(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void notify(String message) {
		System.out.println("ColleagueB  收到消息 <----"+message);
		
	}
	
}
