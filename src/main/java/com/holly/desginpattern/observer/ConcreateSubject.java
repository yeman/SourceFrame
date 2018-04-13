package com.holly.desginpattern.observer;

public class ConcreateSubject extends Subject {

	public ConcreateSubject(String name) {
		super(name);
	}

	@Override
	public boolean getState() {
		return this.state;
	}

	@Override
	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public void notifyOberserver() {
		System.out.println("============ "+ this.name + "发布消息====");
		if(state){
			for(OberServer oberServer : list){
				oberServer.update();
			}
		}

	}

}
