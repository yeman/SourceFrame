package com.holly.desginpattern.observer;

public  class OberServerB extends OberServer{

	OberServerB(String name, Subject subject) {
		super(name, subject);
	}

	@Override
	public void update() {
		System.out.println(this.name+"状态:"+this.subject.state+" 收到消息");
	}


}
