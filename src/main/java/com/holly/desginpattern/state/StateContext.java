package com.holly.desginpattern.state;
//维护状态类

public class StateContext {
	private State state;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
		System.out.println("当前状态"+state.getClass().getName());
	}
	StateContext(State state){
		this.state = state;
	}

	public void request(){
		state.handle(this);
	}

}
